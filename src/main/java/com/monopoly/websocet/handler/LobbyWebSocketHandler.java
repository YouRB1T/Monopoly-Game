package com.monopoly.websocet.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monopoly.domain.engine.Lobby;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.entity.PlayerEntity;
import com.monopoly.service.GameSessionCreationService;
import com.monopoly.service.LobbyService;
import com.monopoly.service.PlayerEntityService;
import com.monopoly.websocet.massage.request.lobby.*;
import com.monopoly.websocet.massage.response.lobby.LobbyStateUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class LobbyWebSocketHandler implements WebSocketHandler {
    
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
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("New connection established: {}", session.getId());
    }
    
    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        String payload = ((TextMessage) message).getPayload();
        log.info("Received message: {}", payload);
        RequestWebSocketMessageLobby requestMessage = objectMapper.readValue(payload, RequestWebSocketMessageLobby.class);
        
        if (requestMessage instanceof JoinLobbyMessage) {
            handleJoinLobby(session, (JoinLobbyMessage) requestMessage);
        } else if (requestMessage instanceof CloseLobbyMessage) {
            handleCloseLobby(session, (CloseLobbyMessage) requestMessage);
        } else if (requestMessage instanceof ExcludeLobbyMessage) {
            handleExcludePlayer(session, (ExcludeLobbyMessage) requestMessage);
        } else if (requestMessage instanceof StartGameMessage) {
            handleStartGame(session, (StartGameMessage) requestMessage);
        } else if (requestMessage instanceof UpdateLobbyPasswordMessage) {
            handleUpdatePassword(session, (UpdateLobbyPasswordMessage) requestMessage);
        }
    }
    
    private void handleJoinLobby(WebSocketSession session, JoinLobbyMessage joinMessage) throws IOException {
        UUID lobbyId = joinMessage.getLobbyId();
        Lobby lobby = lobbyService.getLobbyById(lobbyId);

        if (lobby.getPassword() != null && !lobby.getPassword().isEmpty()) {
            if (!lobby.getPassword().equals(joinMessage.getPassword())) {
                log.info("Wrong password for lobby: {}", lobbyId);
                sendErrorMessage(session, "Wrong password");
                return;
            }
        }

        Optional<PlayerEntity> playerEntityOpt = playerEntityService.getPlayerByNickname(joinMessage.getPlayerName());
        
        if (playerEntityOpt.isEmpty()) {
            sendErrorMessage(session, "Don't registered player");
            return;
        }

        Player player = playerEntityService.convertToPlayer(playerEntityOpt.get(), 0);

        lobby = lobbyService.addPlayerToLobby(lobbyId, player);
        
        lobbySessions.computeIfAbsent(lobbyId, k -> new ConcurrentHashMap<>())
                .put(session.getId(), session);
        
        sessionPlayers.put(session.getId(), player);
        log.info("Player {} joined lobby {}", player.getName(), lobbyId);
        broadcastLobbyState(lobby);
    }
    
    private void handleCloseLobby(WebSocketSession session, CloseLobbyMessage closeMessage) throws IOException {
        UUID lobbyId = closeMessage.getLobbyId();
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player player = sessionPlayers.get(session.getId());

        if (!lobby.getCreator().getId().equals(player.getId())) {
            sendErrorMessage(session, "Только создатель может закрыть лобби");
            return;
        }

        Map<String, WebSocketSession> sessions = lobbySessions.get(lobbyId);
        if (sessions != null) {
            for (WebSocketSession lobbySession : sessions.values()) {
                sendMessage(lobbySession, closeMessage);
            }

            for (String sessionId : sessions.keySet()) {
                sessionPlayers.remove(sessionId);
            }
            lobbySessions.remove(lobbyId);
        }

        lobbyService.closeLobby(lobbyId);
    }
    
    private void handleExcludePlayer(WebSocketSession session, ExcludeLobbyMessage excludeMessage) throws IOException {
        UUID lobbyId = excludeMessage.getLobbyId();
        UUID playerToExcludeId = excludeMessage.getPlayerID();
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player requestingPlayer = sessionPlayers.get(session.getId());

        if (!lobby.getCreator().getId().equals(requestingPlayer.getId())) {
            sendErrorMessage(session, "Только создатель может исключать игроков");
            return;
        }

        Map<String, WebSocketSession> sessions = lobbySessions.get(lobbyId);
        String sessionIdToRemove = null;
        
        for (Map.Entry<String, Player> entry : sessionPlayers.entrySet()) {
            if (entry.getValue().getId().equals(playerToExcludeId)) {
                sessionIdToRemove = entry.getKey();
                break;
            }
        }
        
        if (sessionIdToRemove != null) {
            WebSocketSession playerSession = sessions.get(sessionIdToRemove);
            if (playerSession != null) {
                sendMessage(playerSession, excludeMessage);
                sessions.remove(sessionIdToRemove);
            }

            lobby = lobbyService.removePlayerFromLobby(lobbyId, playerToExcludeId);
            sessionPlayers.remove(sessionIdToRemove);
            broadcastLobbyState(lobby);
        }
    }
    
    private void handleStartGame(WebSocketSession session, StartGameMessage startMessage) throws IOException {
        UUID lobbyId = startMessage.getLobbyId();
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player requestingPlayer = sessionPlayers.get(session.getId());

        if (!lobby.getCreator().getId().equals(requestingPlayer.getId())) {
            sendErrorMessage(session, "Только создатель может начать игру");
            return;
        }

        String gameSessionUrl = gameSessionCreationService.createGameSessionFromLobby(lobby, startMessage.isClassic());

        Map<String, WebSocketSession> sessions = lobbySessions.get(lobbyId);
        if (sessions != null) {
            for (WebSocketSession lobbySession : sessions.values()) {
                StartGameMessage response = new StartGameMessage();
                response.setLobbyId(lobbyId);
                response.setClassic(startMessage.isClassic());
                response.setInitialGameState(startMessage.getInitialGameState());
                response.getInitialGameState().put("sessionUrl", gameSessionUrl);
                
                sendMessage(lobbySession, response);
            }
        }
    }
    
    private void handleUpdatePassword(WebSocketSession session, UpdateLobbyPasswordMessage passwordMessage) throws IOException {
        UUID lobbyId = passwordMessage.getLobbyId();
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player requestingPlayer = sessionPlayers.get(session.getId());

        if (!lobby.getCreator().getId().equals(requestingPlayer.getId())) {
            sendErrorMessage(session, "Только создатель может изменить пароль");
            return;
        }

        lobby = lobbyService.updateLobbyPassword(lobbyId, passwordMessage.getNewPassword());

        broadcastLobbyState(lobby);
    }
    
    private void broadcastLobbyState(Lobby lobby) throws JsonProcessingException {
        log.info("Broadcasting lobby state: {}", lobby);
        Map<String, WebSocketSession> sessions = lobbySessions.get(lobby.getId());
        if (sessions != null) {
            LobbyStateUpdateMessage stateMessage = new LobbyStateUpdateMessage();
            stateMessage.setLobbyId(lobby.getId());
            stateMessage.setLobby(lobby);
            log.info("Lobby state: {}", stateMessage);
            
            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    sendMessage(session, stateMessage);
                }
            }
        }
    }
    
    private void sendMessage(WebSocketSession session, Object message) throws JsonProcessingException {
        if (session.isOpen()) {
            try {
                log.info("Sending message: {}", message);
                String json = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                log.error("Error sending message: {}", e.getMessage());
            }
        }
    }
    
    private void sendErrorMessage(WebSocketSession session, String errorMessage) throws IOException {
        ErrorMessage error = new ErrorMessage();
        error.setMessage(errorMessage);
        sendMessage(session, error);
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport error: {}", exception.getMessage());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection closed: {}", session.getId());
        Player player = sessionPlayers.remove(session.getId());
        if (player != null) {
            for (Map.Entry<UUID, Map<String, WebSocketSession>> entry : lobbySessions.entrySet()) {
                UUID lobbyId = entry.getKey();
                Map<String, WebSocketSession> sessions = entry.getValue();
                
                if (sessions.containsKey(session.getId())) {
                    sessions.remove(session.getId());
                    
                    try {
                        log.info("Removing player from lobby: {}", player.getName());
                        Lobby lobby = lobbyService.removePlayerFromLobby(lobbyId, player.getId());

                        if (lobby.getCreator().getId().equals(player.getId()) && !lobby.getPlayers().isEmpty()) {
                            lobby = lobbyService.assignNewCreator(lobbyId, lobby.getPlayers().get(0).getId());
                            log.info("New creator assigned: {}", lobby.getCreator().getName());
                            broadcastLobbyState(lobby);
                        }

                        if (lobby.getPlayers().isEmpty()) {
                            lobbyService.closeLobby(lobbyId);
                            lobbySessions.remove(lobbyId);
                        } else {
                            broadcastLobbyState(lobby);
                        }
                    } catch (Exception e) {
                        log.error("Error removing player from lobby: {}", e.getMessage());
                    }
                    
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}