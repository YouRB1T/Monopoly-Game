package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.engine.dto.response.card.DtoBuyPropertyResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.PropertyGroup;
import com.monopoly.domain.engine.card.RentOfCard;
import com.monopoly.service.PlayerService;
import com.monopoly.service.PropertyCardService;
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
class BuyPropertyCardHandlerTest {

    @Mock
    private PropertyCardService propertyCardService;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private BuyPropertyCardHandler handler;

    private GameSession gameSession;
    private Player player;
    private PropertyCard propertyCard;
    private PropertyGroup group;
    private DtoBuyPropertyRequest request;

    @BeforeEach
    void setUp() {
        // Инициализация игрока
        player = new Player(UUID.randomUUID(), 1500, new HashSet<>());

        // Инициализация карты собственности
        Map<Integer, Integer> rentLevels = new HashMap<>();
        rentLevels.put(0, 50);
        rentLevels.put(1, 100);
        RentOfCard rentOfCard = new RentOfCard(rentLevels);

        propertyCard = new PropertyCard(UUID.randomUUID(), "Property", "Property", new ArrayList<>(),
                0, 1500, rentOfCard, "Gtoiewr");

        group = new PropertyGroup("Gtoiewr", Set.of(propertyCard));


        // Инициализация игровой сессии
        gameSession = new GameSession(List.of(player), UUID.randomUUID(),
                new ArrayList<>(), new TreeMap<>(), new HashMap<>());

        gameSession.setPropertyCardOwners(new TreeMap<>());

        gameSession.getPropertyGroups().put("Gtoiewr", group);

        // Создание запроса
        request = new DtoBuyPropertyRequest(gameSession, player, propertyCard);
    }

    @Test
    @DisplayName("Должен обрабатывать покупку собственности")
    void shouldHandleBuyProperty() {
        // Arrange
        doNothing().when(playerService).subMoneys(player, propertyCard.getPrice());

        // Act
        DtoBuyPropertyResponse response = handler.handle(request);

        // Assert
        System.out.println(response.toString());
        assertNotNull(response);
        assertEquals(gameSession, response.getGameSession());
        assertEquals(player, response.getPlayer());
        assertEquals(propertyCard, response.getUpdatedPropertyCard().get(0));
        assertEquals(player, gameSession.getPropertyCardOwners().get(propertyCard));

        verify(playerService).subMoneys(player, propertyCard.getPrice());
    }

    @Test
    @DisplayName("Должен выбрасывать исключение при недостатке средств")
    void shouldThrowExceptionWhenNotEnoughMoney() {
        // Arrange
        player.setMoneys(100); // Меньше стоимости карты

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> handler.handle(request)
        );

        assertTrue(exception.getMessage().contains("Недостаточно средств"));
    }

    @Test
    @DisplayName("Должен проверять, может ли обработать запрос")
    void shouldCheckIfCanHandleRequest() {
        // Act & Assert
        assertTrue(handler.canHandle(request));
        assertEquals(DtoBuyPropertyRequest.class, handler.getSupportedRequestType());
    }
}