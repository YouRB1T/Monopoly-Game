package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
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
class GameSessionServiceTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private GameSessionService gameSessionService;

    private GameSession gameSession;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        // Инициализация игроков
        player1 = new Player(UUID.randomUUID(), 1500, new HashSet<>());
        player2 = new Player(UUID.randomUUID(), 1500, new HashSet<>());
        
        // Инициализация игровой сессии
        gameSession = new GameSession(List.of(player1, player2), UUID.randomUUID(), new ArrayList<>(), new TreeMap<>(), new HashMap<>());
        gameSession.setPlayerPosition(new HashMap<>());
        // Инициализация позиций игроков
        gameSession.getPlayerPosition().put(player1, 0);
        gameSession.getPlayerPosition().put(player2, 0);
    }

    @Test
    @DisplayName("Должен бросать кубики и возвращать результат")
    void shouldRollDice() {
        // Act
        Integer[] result = gameSessionService.rollDice();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertTrue(result[0] >= 1 && result[0] <= 6);
        assertTrue(result[1] >= 1 && result[1] <= 6);
    }

    @Test
    @DisplayName("Должен переводить деньги от одного игрока другому")
    void shouldTransferMoney() {
        // Arrange
        doNothing().when(playerService).subMoneys(player1, 500);
        doNothing().when(playerService).addMoneys(player2, 500);
        
        // Act
        gameSessionService.transferMoney(player1, player2, 500);
        
        // Assert
        verify(playerService).subMoneys(player1, 500);
        verify(playerService).addMoneys(player2, 500);
    }

    @Test
    @DisplayName("Должен выбрасывать исключение при недостатке средств")
    void shouldThrowExceptionWhenNotEnoughMoney() {
        // Arrange
        player1.setMoneys(300);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class, 
            () -> gameSessionService.transferMoney(player1, player2, 500)
        );
        
        assertTrue(exception.getMessage().contains("Недостаточно средств у игрока"));
    }

    @Test
    @DisplayName("Должен перемещать игрока на новую позицию")
    void shouldMovePlayerToPosition() {
        // Act
        gameSessionService.movePlayerToPosition(gameSession, player1, 10);
        
        // Assert
        assertEquals(10, gameSession.getPlayerPosition().get(player1));
        assertEquals(0, gameSession.getPlayerPosition().get(player2)); // Второй игрок не должен двигаться
    }
}