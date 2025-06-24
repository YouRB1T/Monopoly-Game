package com.monopoly.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.entity.PlayerEntity;
import com.monopoly.websocket.message.request.lobby.*;
import com.monopoly.websocket.message.response.lobby.*;
import com.monopoly.websocket.message.response.lobby.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Service
@Slf4j
public class LobbyHandlerService implements HandlerService {

    private final Map<Class<?>, BiConsumer<WebSocketSession, ? extends RequestWebSocketMessageLobby>> handlers = Map.of(
            CloseLobbyMessage.class, (s, m) -> handleCloseLobby(s, (CloseLobbyMessage) m),
            ExcludeLobbyMessage.class, (s, m) -> handleExcludePlayer(s, (ExcludeLobbyMessage) m),
            StartGameMessage.class, (s, m) -> handleStartGame(s, (StartGameMessage) m),
            UpdateLobbyPasswordMessage.class, (s, m) -> handleUpdatePassword(s, (UpdateLobbyPasswordMessage) m)
    );

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameSessionCreationService gameSessionCreationService;

    @Autowired
    private PlayerEntityService playerEntityService;

    private final Map<UUID, Map<String, WebSocketSession>> lobbySessions = new ConcurrentHashMap<>();
    private final Map<String, Player> sessionPlayers = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("Received message: {}", payload);

        try {
            RequestWebSocketMessageLobby requestMessage =
                    objectMapper.readValue(payload, RequestWebSocketMessageLobby.class);

            BiConsumer<WebSocketSession, RequestWebSocketMessageLobby> handler =
                    (BiConsumer<WebSocketSession, RequestWebSocketMessageLobby>)
                            handlers.get(requestMessage.getClass());

            if (handler != null) {
                handler.accept(session, requestMessage);
            } else {
                log.warn("Unknown message type received: {}", requestMessage.getClass().getSimpleName());
                sendErrorMessage(session, "Unknown message type", "UNKNOWN_MESSAGE_TYPE");
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing message: {}", e.getMessage());
            sendErrorMessage(session, "Invalid message format", "INVALID_JSON");
        } catch (Exception e) {
            log.error("Unexpected error handling message: {}", e.getMessage());
            sendErrorMessage(session, "Internal server error", "INTERNAL_ERROR");
        }
    }

    private void handleCloseLobby(WebSocketSession session, CloseLobbyMessage closeMessage) {
        try {
            UUID lobbyId = closeMessage.getLobbyId();
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            Player player = sessionPlayers.get(session.getId());

            if (player == null) {
                sendErrorMessage(session, "Player session not found", "SESSION_NOT_FOUND");
                return;
            }

            if (!lobby.getCreator().getId().equals(player.getId())) {
                sendErrorMessage(session, "Only creator can close lobby", "PERMISSION_DENIED");
                return;
            }

            Map<String, WebSocketSession> sessions = lobbySessions.get(lobbyId);
            if (sessions != null) {
                LobbyClosedMessage closedMessage = new LobbyClosedMessage(lobbyId, "Lobby closed by creator");

                sessions.entrySet().forEach(entry -> {
                    WebSocketSession lobbySession = entry.getValue();
                    String sessionId = entry.getKey();

                    sendMessage(lobbySession, closedMessage);
                    sessionPlayers.remove(sessionId);
                });

                lobbySessions.remove(lobbyId);
            }

            lobbyService.closeLobby(lobbyId);
            log.info("Lobby {} closed by creator {}", lobbyId, player.getName());

        } catch (Exception e) {
            log.error("Error handling close lobby: {}", e.getMessage());
            sendErrorMessage(session, "Error closing lobby", "CLOSE_ERROR");
        }
    }

    private void handleExcludePlayer(WebSocketSession session, ExcludeLobbyMessage excludeMessage) {
        try {
            UUID lobbyId = excludeMessage.getLobbyId();
            UUID playerToExcludeId = excludeMessage.getPlayerID();
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            Player requestingPlayer = sessionPlayers.get(session.getId());

            if (requestingPlayer == null) {
                sendErrorMessage(session, "Player session not found", "SESSION_NOT_FOUND");
                return;
            }

            if (!lobby.getCreator().getId().equals(requestingPlayer.getId())) {
                sendErrorMessage(session, "Only creator can exclude players", "PERMISSION_DENIED");
                return;
            }

            Map<String, WebSocketSession> sessions = lobbySessions.get(lobbyId);
            String sessionIdToRemove = findSessionByPlayerId(playerToExcludeId);

            if (sessionIdToRemove != null && sessions != null) {
                WebSocketSession playerSession = sessions.get(sessionIdToRemove);

                if (playerSession != null) {
                    PlayerExcludedMessage excludedMessage = new PlayerExcludedMessage(
                            lobbyId, playerToExcludeId, "Excluded by lobby creator"
                    );
                    sendMessage(playerSession, excludedMessage);
                    sessions.remove(sessionIdToRemove);
                }

                lobby = lobbyService.removePlayerFromLobby(lobbyId, playerToExcludeId);
                Player excludedPlayer = sessionPlayers.remove(sessionIdToRemove);

                log.info("Player {} excluded from lobby {} by {}",
                        excludedPlayer != null ? excludedPlayer.getName() : playerToExcludeId,
                        lobbyId, requestingPlayer.getName());

                if (excludedPlayer != null) {
                    broadcastPlayerLeft(lobby, excludedPlayer, "Excluded by creator");
                }
                broadcastLobbyState(lobby);
            }

        } catch (Exception e) {
            log.error("Error handling exclude player: {}", e.getMessage());
            sendErrorMessage(session, "Error excluding player", "EXCLUDE_ERROR");
        }
    }

    private void handleStartGame(WebSocketSession session, StartGameMessage startMessage) {
        try {
            UUID lobbyId = startMessage.getLobbyId();
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            Player requestingPlayer = sessionPlayers.get(session.getId());

            if (requestingPlayer == null) {
                sendErrorMessage(session, "Player session not found", "SESSION_NOT_FOUND");
                return;
            }

            if (!lobby.getCreator().getId().equals(requestingPlayer.getId())) {
                sendErrorMessage(session, "Only creator can start game", "PERMISSION_DENIED");
                return;
            }

            if (lobby.getPlayers().size() < 2) {
                sendErrorMessage(session, "Need at least 2 players to start game", "INSUFFICIENT_PLAYERS");
                return;
            }

            String gameSessionUrl = gameSessionCreationService.createGameSessionFromLobby(lobby, startMessage.isClassic());

            Map<String, WebSocketSession> sessions = lobbySessions.get(lobbyId);
            if (sessions != null) {
                GameStartedMessage gameStartedMessage = new GameStartedMessage(lobbyId, gameSessionUrl, startMessage.isClassic());
                gameStartedMessage.getGameState().putAll(startMessage.getInitialGameState());
                gameStartedMessage.getGameState().put("sessionUrl", gameSessionUrl);

                for (WebSocketSession lobbySession : sessions.values()) {
                    sendMessage(lobbySession, gameStartedMessage);
                }
            }

            log.info("Game started for lobby {} by {}", lobbyId, requestingPlayer.getName());

        } catch (Exception e) {
            log.error("Error handling start game: {}", e.getMessage());
            sendErrorMessage(session, "Error starting game", "START_GAME_ERROR");
        }
    }

    private void handleUpdatePassword(WebSocketSession session, UpdateLobbyPasswordMessage passwordMessage) {
        try {
            UUID lobbyId = passwordMessage.getLobbyId();
            Lobby lobby = lobbyService.getLobbyById(lobbyId);
            Player requestingPlayer = sessionPlayers.get(session.getId());

            if (requestingPlayer == null) {
                sendErrorMessage(session, "Player session not found", "SESSION_NOT_FOUND");
                return;
            }

            if (!lobby.getCreator().getId().equals(requestingPlayer.getId())) {
                sendErrorMessage(session, "Only creator can change password", "PERMISSION_DENIED");
                return;
            }

            lobby = lobbyService.updateLobbyPassword(lobbyId, passwordMessage.getNewPassword());
            broadcastLobbyState(lobby);

            log.info("Password updated for lobby {} by {}", lobbyId, requestingPlayer.getName());

        } catch (Exception e) {
            log.error("Error handling update password: {}", e.getMessage());
            sendErrorMessage(session, "Error updating password", "PASSWORD_UPDATE_ERROR");
        }
    }

    @Override
    public void handleConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection closed: {} with status: {}", session.getId(), status);

        Player player = sessionPlayers.remove(session.getId());
        if (player != null) {
            handlePlayerDisconnection(session, player);
        }
    }

    private void handlePlayerDisconnection(WebSocketSession session, Player player) {
        for (Map.Entry<UUID, Map<String, WebSocketSession>> entry : lobbySessions.entrySet()) {
            UUID lobbyId = entry.getKey();
            Map<String, WebSocketSession> sessions = entry.getValue();

            if (sessions.containsKey(session.getId())) {
                sessions.remove(session.getId());

                try {
                    log.info("Removing player {} from lobby {}", player.getName(), lobbyId);
                    Lobby lobby = lobbyService.removePlayerFromLobby(lobbyId, player.getId());

                    if (lobby.getCreator().getId().equals(player.getId()) && !lobby.getPlayers().isEmpty()) {
                        Player newCreator = lobby.getPlayers().get(0);
                        lobby = lobbyService.assignNewCreator(lobbyId, newCreator.getId());
                        log.info("New creator assigned: {}", newCreator.getName());

                        broadcastCreatorChanged(lobby, newCreator, player);
                    }

                    if (lobby.getPlayers().isEmpty()) {
                        lobbyService.closeLobby(lobbyId);
                        lobbySessions.remove(lobbyId);
                        log.info("Empty lobby {} closed", lobbyId);
                    } else {
                        broadcastPlayerLeft(lobby, player, "Disconnected");
                        broadcastLobbyState(lobby);
                    }

                } catch (Exception e) {
                    log.error("Error removing player from lobby: {}", e.getMessage());
                }

                break;
            }
        }
    }

    private void broadcastLobbyState(Lobby lobby) {
        try {
            Map<String, WebSocketSession> sessions = lobbySessions.get(lobby.getId());
            if (sessions != null) {
                LobbyStateUpdateMessage stateMessage = new LobbyStateUpdateMessage();
                stateMessage.setLobbyId(lobby.getId());
                stateMessage.setLobby(lobby);

                for (WebSocketSession session : sessions.values()) {
                    if (session.isOpen()) {
                        sendMessage(session, stateMessage);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error broadcasting lobby state: {}", e.getMessage());
        }
    }

    public void broadcastPlayerJoined(Lobby lobby, Player player) {
        try {
            Map<String, WebSocketSession> sessions = lobbySessions.get(lobby.getId());
            if (sessions != null) {
                PlayerJoinedMessage joinedMessage = new PlayerJoinedMessage(lobby.getId(), player);

                for (WebSocketSession session : sessions.values()) {
                    if (session.isOpen()) {
                        sendMessage(session, joinedMessage);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error broadcasting player joined: {}", e.getMessage());
        }
    }

    private void broadcastPlayerLeft(Lobby lobby, Player player, String reason) {
        try {
            Map<String, WebSocketSession> sessions = lobbySessions.get(lobby.getId());
            if (sessions != null) {
                PlayerLeftMessage leftMessage = new PlayerLeftMessage(lobby.getId(), player, reason);

                for (WebSocketSession session : sessions.values()) {
                    if (session.isOpen()) {
                        sendMessage(session, leftMessage);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error broadcasting player left: {}", e.getMessage());
        }
    }

    private void broadcastCreatorChanged(Lobby lobby, Player newCreator, Player previousCreator) {
        try {
            Map<String, WebSocketSession> sessions = lobbySessions.get(lobby.getId());
            if (sessions != null) {
                CreatorChangedMessage creatorMessage = new CreatorChangedMessage(lobby.getId(), newCreator, previousCreator);

                for (WebSocketSession session : sessions.values()) {
                    if (session.isOpen()) {
                        sendMessage(session, creatorMessage);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error broadcasting creator changed: {}", e.getMessage());
        }
    }

    private void sendMessage(WebSocketSession session, Object message) {
        if (session.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                log.error("Error sending message: {}", e.getMessage());
            }
        }
    }

    private void sendErrorMessage(WebSocketSession session, String errorMessage, String errorCode) {
        try {
            ErrorMessage error = new ErrorMessage(errorMessage, errorCode);
            sendMessage(session, error);
        } catch (Exception e) {
            log.error("Error sending error message: {}", e.getMessage());
        }
    }

    private String findSessionByPlayerId(UUID playerId) {
        return sessionPlayers.entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(playerId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
