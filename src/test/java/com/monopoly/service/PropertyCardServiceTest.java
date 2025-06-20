package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.RentOfCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PropertyCardServiceTest {

    @InjectMocks
    private PropertyCardService propertyCardService;
    private GameSession gameSession;
    private Player player1;
    private Player player2;
    private PropertyCard propertyCard;

    @BeforeEach
    void setUp() {
        player1 = new Player(UUID.randomUUID(), 1500, new HashSet<>());
        player2 = new Player(UUID.randomUUID(), 1500, new HashSet<>());

        Map<Integer, Integer> rentLevels = new HashMap<>();
        rentLevels.put(0, 50);
        rentLevels.put(1, 100);
        rentLevels.put(2, 150);
        RentOfCard rentOfCard = new RentOfCard(rentLevels);

        gameSession = new GameSession(List.of(player1, player2), UUID.randomUUID(), new ArrayList<>(), new TreeMap<>(), new HashMap<>());
        gameSession.setPropertyCardOwners(new TreeMap<>());
        propertyCard = new PropertyCard(UUID.randomUUID(), "Property", "Property", new ArrayList<>(),
                0, 1500, rentOfCard, "null");
        gameSession.getPropertyCardOwners().put(propertyCard, player1);
    }

    @Test
    @DisplayName("Должен передавать собственность от одного игрока другому")
    void shouldTransferPropertyBetweenPlayers() {
        // Act
        propertyCardService.transferProperty(gameSession, propertyCard, player1, player2);
        
        // Assert
        assertEquals(player2, propertyCardService.getPropertyOwner(gameSession, propertyCard));
    }

    @Test
    @DisplayName("Должен проверять, принадлежит ли собственность кому-либо")
    void shouldCheckIfPropertyIsOwned() {
        // Act & Assert
        assertTrue(propertyCardService.isPropertyOwned(gameSession, propertyCard));
        
        // Arrange - удаляем собственность
        gameSession.getPropertyCardOwners().remove(propertyCard);
        
        // Act & Assert
        assertFalse(propertyCardService.isPropertyOwned(gameSession, propertyCard));
    }

    @Test
    @DisplayName("Должен возвращать владельца собственности")
    void shouldGetPropertyOwner() {
        // Act & Assert
        assertEquals(player1, propertyCardService.getPropertyOwner(gameSession, propertyCard));
    }

    @Test
    @DisplayName("Должен рассчитывать ренту за собственность")
    void shouldCalculateRent() {
        // Act
        propertyCardService.upgradeProperty(propertyCard, 0);
        Integer rent = propertyCardService.calculateRent(propertyCard);
        // Assert
        assertEquals(50, rent);
    }

    @Test
    @DisplayName("Должен проверять возможность улучшения собственности")
    void shouldCheckIfPropertyCanBeUpgraded() {
        // Act & Assert - можно улучшить до уровня 1
        assertTrue(propertyCardService.canUpgradeProperty(propertyCard, 1));
        
        // Act & Assert - можно улучшить до уровня 2
        assertTrue(propertyCardService.canUpgradeProperty(propertyCard, 2));
        
        // Act & Assert - нельзя улучшить до уровня 3 (не существует)
        assertFalse(propertyCardService.canUpgradeProperty(propertyCard, 3));
    }

    @Test
    @DisplayName("Должен улучшать собственность")
    void shouldUpgradeProperty() {
        // Act
        propertyCardService.upgradeProperty(propertyCard, 1);
        
        // Assert
        assertEquals(1, propertyCard.getRentOfCard().getCurrentRentLevel());
        assertEquals(100, propertyCardService.calculateRent(propertyCard));
        
        // Act - еще одно улучшение
        propertyCardService.upgradeProperty(propertyCard, 2);
        
        // Assert
        assertEquals(2, propertyCard.getRentOfCard().getCurrentRentLevel());
        assertEquals(150, propertyCardService.calculateRent(propertyCard));
    }
}