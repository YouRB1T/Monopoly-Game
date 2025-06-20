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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
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
        // Соединение установлено, но игрок еще не присоединился к лобби
        // Это произойдет при получении JoinLobbyMessage
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = ((TextMessage) message).getPayload();
        ResponseWebSocketMessageLobby requestMessage = objectMapper.readValue(payload, ResponseWebSocketMessageLobby.class);
        
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
        
        // Проверка пароля, если он установлен
        if (lobby.getPassword() != null && !lobby.getPassword().isEmpty()) {
            if (!lobby.getPassword().equals(joinMessage.getPassword())) {
                sendErrorMessage(session, "Неверный пароль");
                return;
            }
        }
        
        // Получаем сущность игрока из базы данных
        Optional<PlayerEntity> playerEntityOpt = playerEntityService.getPlayerByNickname(joinMessage.getPlayerName());
        
        if (playerEntityOpt.isEmpty()) {
            sendErrorMessage(session, "Игрок не найден. Пожалуйста, зарегистрируйтесь.");
            return;
        }
        
        // Преобразуем сущность игрока в игрока для игровой сессии
        // Используем начальное количество денег из правил лобби или по умолчанию 1500
        int initialMoney = 1500;
        if (lobby.getGameRules().containsKey("START_MONEYS")) {
            initialMoney = (Integer) lobby.getGameRules().get("START_MONEYS");
        }
        
        Player player = playerEntityService.convertToPlayer(playerEntityOpt.get(), initialMoney);
        
        lobby = lobbyService.addPlayerToLobby(lobbyId, player);
        
        lobbySessions.computeIfAbsent(lobbyId, k -> new ConcurrentHashMap<>())
                .put(session.getId(), session);
        
        sessionPlayers.put(session.getId(), player);
        
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
    }
    
    private void sendMessage(WebSocketSession session, Object message) throws JsonProcessingException {
        if (session.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                // Логирование ошибки
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
        // Обработка ошибок транспорта
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Находим игрока и лобби
        Player player = sessionPlayers.remove(session.getId());
        if (player != null) {
            // Ищем лобби, в котором был игрок
            for (Map.Entry<UUID, Map<String, WebSocketSession>> entry : lobbySessions.entrySet()) {
                UUID lobbyId = entry.getKey();
                Map<String, WebSocketSession> sessions = entry.getValue();
                
                if (sessions.containsKey(session.getId())) {
                    sessions.remove(session.getId());
                    
                    try {
                        // Удаляем игрока из лобби
                        Lobby lobby = lobbyService.removePlayerFromLobby(lobbyId, player.getId());
                        
                        // Если это был создатель и есть другие игроки, назначаем нового создателя
                        if (lobby.getCreator().getId().equals(player.getId()) && !lobby.getPlayers().isEmpty()) {
                            lobby = lobbyService.assignNewCreator(lobbyId, lobby.getPlayers().get(0).getId());
                        }
                        
                        // Если игроков не осталось, закрываем лобби
                        if (lobby.getPlayers().isEmpty()) {
                            lobbyService.closeLobby(lobbyId);
                            lobbySessions.remove(lobbyId);
                        } else {
                            // Иначе отправляем обновление оставшимся игрокам
                            broadcastLobbyState(lobby);
                        }
                    } catch (Exception e) {
                        // Логирование ошибки
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