package com.monopoly.engine;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.engine.dto.request.engine.DtoStartGameRequest;
import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;
import com.monopoly.domain.engine.dto.response.card.DtoBuyPropertyResponse;
import com.monopoly.domain.engine.dto.response.engine.DtoStartGameResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.RentOfCard;
import com.monopoly.engine.handler.card.BuyPropertyCardHandler;
import com.monopoly.engine.handler.engine.StartGameHandler;
import com.monopoly.repository.GameSessionRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameSessionEngineServiceTest {

    @Mock
    private GameSessionRedisRepository repository;
    
    @Mock
    private BuyPropertyCardHandler buyPropertyCardHandler;
    
    @Mock
    private StartGameHandler startGameHandler;
    
    @InjectMocks
    private GameSessionEngineService engineService;
    
    private GameSession gameSession;
    private Player player;
    private PropertyCard propertyCard;
    
    @BeforeEach
    void setUp() {
        player = new Player(UUID.randomUUID(), 1500, new HashSet<>());

        Map<Integer, Integer> rentLevels = new HashMap<>();
        rentLevels.put(0, 50);
        rentLevels.put(1, 100);
        RentOfCard rentOfCard = new RentOfCard(rentLevels);

        propertyCard = new PropertyCard(UUID.randomUUID(), "Property", "Property", new ArrayList<>(),
                0, 1500, rentOfCard, "null");

        gameSession = new GameSession(List.of(player), UUID.randomUUID(),
                new ArrayList<>(), new TreeMap<>(), new HashMap<>());

        when(startGameHandler.canHandle(any(DtoStartGameRequest.class))).thenReturn(true);
    }
    
    @Test
    @DisplayName("Должен обрабатывать запрос на покупку собственности")
    void shouldHandleBuyPropertyRequest() {
        // Arrange
        DtoBuyPropertyRequest request = new DtoBuyPropertyRequest(gameSession, player, propertyCard);
        DtoBuyPropertyResponse expectedResponse = new DtoBuyPropertyResponse(gameSession, player, List.of(propertyCard));
        
        when(buyPropertyCardHandler.canHandle(request)).thenReturn(true);
        when(buyPropertyCardHandler.handle(request)).thenReturn(expectedResponse);
        
        // Act
        DtoHandlerResponse response = engineService.handleGameEvent(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(buyPropertyCardHandler).handle(request);

    }
    
    @Test
    @DisplayName("Должен обрабатывать запрос на начало игры")
    void shouldHandleStartGameRequest() {
        // Arrange
        DtoStartGameRequest request = new DtoStartGameRequest(gameSession);
        DtoStartGameResponse expectedResponse = new DtoStartGameResponse(gameSession, List.of("START_GAME"));
        
        when(startGameHandler.handle(request)).thenReturn(expectedResponse);
        
        // Act
        DtoHandlerResponse response = engineService.handleGameEvent(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(startGameHandler).handle(request);

    }
    
    @Test
    @DisplayName("Должен выбрасывать исключение, если нет подходящего обработчика")
    void shouldThrowExceptionWhenNoHandlerFound() {
        // Arrange
        DtoHandlerRequest request = mock(DtoHandlerRequest.class);
        when(request.getGameSession()).thenReturn(gameSession);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class, 
            () -> engineService.handleGameEvent(request)
        );
        
        assertTrue(exception.getMessage().contains("Не найден обработчик"));
    }
}