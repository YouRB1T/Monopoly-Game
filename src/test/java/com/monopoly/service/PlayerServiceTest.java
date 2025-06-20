package com.monopoly.service;


import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.RentOfCard;
import com.monopoly.domain.engine.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    private Player player;

    private Card card;

    private PropertyCard propertyCard;

    private GameSession gameSession;
    @Mock
    private RentOfCard rentOfCard;

    @BeforeEach
    void setUp() {
        card = new Card(UUID.randomUUID(), "Sample card", "Sample");
        player = new Player(UUID.randomUUID(), 1500, new HashSet<>());
        gameSession = new GameSession(List.of(player), UUID.randomUUID(), new ArrayList<>(), new TreeMap<>(), new HashMap<>());
        gameSession.setPropertyCardOwners(new TreeMap<>());
        propertyCard = new PropertyCard(UUID.randomUUID(), "Property", "Property", new ArrayList<>(),
                0, 1500, rentOfCard, "null");
        gameSession.getPropertyCardOwners().put(propertyCard, player);
    }

    @Test
    @DisplayName("Должен добавлять карту игроку")
    void shouldAddCardToPlayer() {
        // Act
        playerService.addCard(card, player);
        
        // Assert
        System.out.println(player.getPlayerCards().toString());
        assertTrue(player.getPlayerCards().contains(card));
        assertEquals(1, player.getPlayerCards().size());
    }

    @Test
    @DisplayName("Должен удалять карту у игрока")
    void shouldDeleteCardFromPlayer() {
        System.out.println(player.getPlayerCards().toString());
        playerService.addCard(card, player);
        
        // Act
        playerService.deleteCard(card, player);
        System.out.println(player.getPlayerCards().toString());
        
        // Assert
        assertFalse(player.getPlayerCards().contains(card));
        assertEquals(0, player.getPlayerCards().size());
    }

    @Test
    @DisplayName("Должен проверять наличие карты у игрока")
    void shouldCheckIfPlayerHasCard() {
        // Arrange
        playerService.addCard(card, player);
        
        // Act & Assert
        assertTrue(playerService.hasCard(card, player));
        
        // Act & Assert for non-existing card
        Card anotherCard = new Card(UUID.randomUUID(), "AnotherCard", "Another Description") {};
        assertFalse(playerService.hasCard(anotherCard, player));
    }

    @Test
    @DisplayName("Должен добавлять деньги игроку")
    void shouldAddMoneyToPlayer() {
        // Arrange
        int initialMoney = player.getMoneys();
        int initialTotalMoney = player.getTotalMoneys();
        int amountToAdd = 500;
        
        // Act
        playerService.addMoneys(player, amountToAdd);
        
        // Assert
        assertEquals(initialMoney + amountToAdd, player.getMoneys());
        assertEquals(initialTotalMoney + amountToAdd, player.getTotalMoneys());
    }

    @Test
    @DisplayName("Должен вычитать деньги у игрока")
    void shouldSubtractMoneyFromPlayer() {
        // Arrange
        int initialMoney = player.getMoneys();
        int initialTotalMoney = player.getTotalMoneys();
        int amountToSubtract = 500;
        
        // Act
        playerService.subMoneys(player, amountToSubtract);
        
        // Assert
        assertEquals(initialMoney - amountToSubtract, player.getMoneys());
        assertEquals(initialTotalMoney - amountToSubtract, player.getTotalMoneys());
    }

    @Test
    @DisplayName("Должен определять банкротство игрока")
    void shouldDetermineIfPlayerIsBankrupt() {
        // Arrange - игрок не банкрот
        assertFalse(playerService.isBankrupt(player));
        
        // Act - делаем игрока банкротом
        playerService.subMoneys(player, 2000);
        
        // Assert
        assertTrue(playerService.isBankrupt(player));
    }

    @Test
    @DisplayName("Должен получать список собственности игрока")
    void shouldGetPlayerProperties() {

        List<PropertyCard> properties = playerService.getPlayerProperties(gameSession, player);

        assertEquals(1, properties.size());
        assertEquals(propertyCard, properties.get(0));
    }
}