package com.monopoly.websocet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;
import com.monopoly.engine.GameSessionEngineService;
import com.monopoly.mapper.RequestGameHandlerMessageMapper;
import com.monopoly.mapper.ResponseGameHandlerMessageMapper;
import com.monopoly.websocet.massage.request.session.RequestGameHandlerMessage;
import com.monopoly.websocet.massage.response.sessoin.ResponseGameHandlerMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameWebsocketHandlerTest {

    @Mock
    private ObjectMapper objectMapper;
    
    @Mock
    private RequestGameHandlerMessageMapper messageMapper;
    
    @Mock
    private ResponseGameHandlerMessageMapper dtoMapper;
    
    @Mock
    private GameSessionEngineService gameSessionEngineService;
    
    @Mock
    private WebSocketSession session;
    
    @Mock
    private TextMessage textMessage;
    
    @InjectMocks
    private GameWebsocketHandler handler;
    
    private RequestGameHandlerMessage requestMessage;
    private DtoHandlerRequest dtoRequest;
    private DtoHandlerResponse dtoResponse;
    private ResponseGameHandlerMessage responseMessage;
    private Set<WebSocketSession> sessions;
    
    @BeforeEach
    void setUp() throws Exception {
        // Инициализация сообщений
        requestMessage = new RequestGameHandlerMessage();
        dtoRequest = mock(DtoHandlerRequest.class);
        dtoResponse = mock(DtoHandlerResponse.class);
        responseMessage = new ResponseGameHandlerMessage(UUID.randomUUID(), "", new HashMap<>());
        
        // Настройка моков
        when(textMessage.getPayload()).thenReturn("{\"type\":\"TEST\"}");
        when(objectMapper.readValue(anyString(), eq(RequestGameHandlerMessage.class))).thenReturn(requestMessage);
        when(messageMapper.mapToRequest(requestMessage)).thenReturn(dtoRequest);
        when(gameSessionEngineService.handleGameEvent(dtoRequest)).thenReturn(dtoResponse);
        when(dtoMapper.mapToResponseHandlerMessage(eq(dtoResponse), any())).thenReturn(responseMessage);
        
        // Настройка сессий
        sessions = new HashSet<>();
        sessions.add(session);

    }
    
    @Test
    @DisplayName("Должен обрабатывать входящее сообщение")
    void shouldHandleIncomingMessage() throws Exception {
        // Act
        handler.handleMessage(session, textMessage);
        
        // Assert
        verify(objectMapper).readValue(anyString(), eq(RequestGameHandlerMessage.class));
        verify(messageMapper).mapToRequest(requestMessage);
        verify(gameSessionEngineService).handleGameEvent(dtoRequest);
        verify(dtoMapper).mapToResponseHandlerMessage(eq(dtoResponse), any());
        verify(session).sendMessage(any(TextMessage.class));
    }
    
    @Test
    @DisplayName("Должен отправлять сообщение всем подключенным клиентам")
    void shouldBroadcastToAllClients() throws Exception {
        // Arrange
        WebSocketSession anotherSession = mock(WebSocketSession.class);
        when(anotherSession.isOpen()).thenReturn(true);
        sessions.add(anotherSession);
        
        // Act
        handler.broadcastToAll(responseMessage);
        
        // Assert
        verify(objectMapper).writeValueAsString(responseMessage);
        verify(session).sendMessage(any(TextMessage.class));
        verify(anotherSession).sendMessage(any(TextMessage.class));
    }
    
    @Test
    @DisplayName("Должен обрабатывать закрытие сессии")
    void shouldHandleSessionClosed() throws Exception {
        // Act
        handler.afterConnectionClosed(session, null);
        
        // Assert

    }
}