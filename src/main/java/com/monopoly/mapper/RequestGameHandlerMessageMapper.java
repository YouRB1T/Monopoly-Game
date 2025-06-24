package com.monopoly.mapper;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.domain.engine.card.special.PrisonCard;
import com.monopoly.domain.engine.dto.request.card.*;
import com.monopoly.domain.engine.enums.HandleType;
import com.monopoly.websocket.message.request.session.RequestGameHandlerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RequestGameHandlerMessageMapper {
    private final GameSessionMapper gameSessionMapper;

    public DtoHandlerRequest mapToRequest(RequestGameHandlerMessage message) {
        UUID sessionId = message.getSessionId();
        HandleType type = message.getHandleType();
        Map<String, Object> data = message.getEventData();

        GameSession gameSession = gameSessionMapper.getGameSessionById(sessionId);

        return switch (type) {
            case BUY_PRPRTY -> mapBuyProperty(data, gameSession);
            case CHANCE -> mapChance(data, gameSession);
            case CHNG_RENT -> mapChangeRent(data, gameSession);
            case GO_PRSN -> mapGoToPrison(data, gameSession);
            case PAY_RENT -> mapPayRent(data, gameSession);
            case SALE_CARD -> mapSaleCard(data, gameSession);
            case TREASURY -> mapTreasury(data, gameSession);
            case UPGRD_PRPTY -> mapUpgradeProperty(data, gameSession);
            case FREE_PRSN_CARD -> mapFreePrisonCard(data, gameSession);
            case STRT_PLR_MOVE -> mapStartPlayerMove(data, gameSession);
            case END_PLR_MOVE -> mapEndPlayerMove(data, gameSession);
            case ROLL_DICE -> mapRollDice(data, gameSession);
        };
    }

    private DtoHandlerRequest mapRollDice(Map<String, Object> data, GameSession gameSession) {

        return null;
    }

    private DtoHandlerRequest mapEndPlayerMove(Map<String, Object> data, GameSession gameSession) {
        return null;
    }

    private DtoHandlerRequest mapStartPlayerMove(Map<String, Object> data, GameSession gameSession) {
        UUID playerId = UUID.fromString(data.get("playerId").toString());

        Player player = gameSessionMapper.getPLayerById(playerId);
        Integer rewordForCircle = (Integer) gameSession.getGameRules().get("rewordForCircle");
        return new DtoStartCardMoveRequest(gameSession, player, rewordForCircle);
    }

    private DtoHandlerRequest mapFreePrisonCard(Map<String, Object> data, GameSession gameSession) {
        UUID playerId = UUID.fromString(data.get("playerId").toString());
        UUID prisonCardId = UUID.fromString(data.get("cardId").toString());

        Player player = gameSessionMapper.getPLayerById(playerId);
        PrisonCard prisonCard = (PrisonCard) gameSessionMapper.getCardById(prisonCardId);

        return new DtoUseFreePrisonCardRequest(gameSession, player, prisonCard);
    }

    private DtoHandlerRequest mapUpgradeProperty(Map<String, Object> data, GameSession gameSession) {
        UUID propertyCardId = UUID.fromString(data.get("cardId").toString());

        Integer nextLevel = (Integer) data.get("nextRendLevel");
        PropertyCard card = (PropertyCard) gameSessionMapper.getCardById(propertyCardId);

        return new DtoUpgradePropertyRequest(gameSession, card, nextLevel);
    }

    private DtoHandlerRequest mapTreasury(Map<String, Object> data, GameSession gameSession) {
        UUID playerId = UUID.fromString(data.get("playerId").toString());

        Player player = gameSessionMapper.getPLayerById(playerId);

        return new DtoTreasuryHandlerRequest(gameSession, player);
    }

    private DtoHandlerRequest mapSaleCard(Map<String, Object> data, GameSession gameSession) {
        UUID newOwnerPLayerId = UUID.fromString(data.get("playerOldOwnerId").toString());
        UUID propertyCardId = UUID.fromString(data.get("cardId").toString());

        Player newOwner = gameSessionMapper.getPLayerById(newOwnerPLayerId);
        PropertyCard propertyCard = (PropertyCard) gameSessionMapper.getCardById(propertyCardId);

        return new DtoSaleCardRequest(gameSession, gameSession.getPropertyCardOwners().get(propertyCard),
                newOwner, propertyCard, propertyCard.getPrice());
    }

    private DtoHandlerRequest mapPayRent(Map<String, Object> data, GameSession gameSession) {
        UUID playerId = UUID.fromString(data.get("playerId").toString());
        UUID propertyCardId = UUID.fromString(data.get("cardId").toString());

        Player player = gameSessionMapper.getPLayerById(playerId);
        PropertyCard propertyCard = (PropertyCard) gameSessionMapper.getCardById(propertyCardId);

        return new DtoBuyPropertyRequest(gameSession, player, propertyCard);
    }

    private DtoHandlerRequest mapGoToPrison(Map<String, Object> data, GameSession gameSession) {
        UUID playerId = UUID.fromString(data.get("playerId").toString());
        UUID prisonCardId = UUID.fromString(data.get("cardId").toString());

        Player player = gameSessionMapper.getPLayerById(playerId);
        PrisonCard prisonCard = (PrisonCard) gameSessionMapper.getCardById(prisonCardId);

        return new DtoGoPrisonRequest(gameSession, player, prisonCard);
    }

    private DtoHandlerRequest mapChangeRent(Map<String, Object> data, GameSession gameSession) {
        return null;
    }

    private DtoBuyPropertyRequest mapBuyProperty(Map<String, Object> data, GameSession gameSession) {
        UUID playerId = UUID.fromString(data.get("playerId").toString());
        UUID cardId = UUID.fromString(data.get("cardId").toString());

        Player player = gameSessionMapper.getPLayerById(playerId);
        PropertyCard card = (PropertyCard) gameSessionMapper.getCardById(cardId);

        return new DtoBuyPropertyRequest(gameSession, player, card);
    }

    private DtoChanceHandlerRequest mapChance(Map<String, Object> data, GameSession gameSession) {
        UUID playerId = UUID.fromString(data.get("playerId").toString());

        Player player = gameSessionMapper.getPLayerById(playerId);

        return new DtoChanceHandlerRequest(gameSession, player);
    }


}
