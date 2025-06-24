package com.monopoly.mapper;

import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;
import com.monopoly.websocket.message.request.session.RequestGameHandlerMessage;
import com.monopoly.websocket.message.response.sessoin.ResponseGameHandlerMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseGameHandlerMessageMapper {

    public ResponseGameHandlerMessage mapToResponseHandlerMessage(DtoHandlerResponse response, RequestGameHandlerMessage message) {
        Map<String, Object> data = new HashMap<>();

        switch (message.getHandleType()) {
            case BUY_PRPRTY -> mapBuyProperty(data, response);
            case CHANCE -> mapChance(data, response);
            case CHNG_RENT -> mapChangeRent(data, response);
            case GO_PRSN -> mapGoToPrison(data, response);
            case PAY_RENT -> mapPayRent(data, response);
            case SALE_CARD -> mapSaleCard(data, response);
            case TREASURY -> mapTreasury(data, response);
            case UPGRD_PRPTY -> mapUpgradeProperty(data, response);
            case FREE_PRSN_CARD -> mapFreePrisonCard(data, response);
            case STRT_PLR_MOVE -> mapStartPlayerMove(data, response);
            case END_PLR_MOVE -> mapEndPlayerMove(data, response);
            case ROLL_DICE -> mapRollDice(data, response);
        };
        return new ResponseGameHandlerMessage(response.getGameSession().getId(),
                message.getHandleType().toString(), data);
    }

    private void mapRollDice(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapEndPlayerMove(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapStartPlayerMove(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapFreePrisonCard(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapUpgradeProperty(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapTreasury(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapSaleCard(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapPayRent(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapGoToPrison(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapChangeRent(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapChance(Map<String, Object> data, DtoHandlerResponse response) {

    }

    private void mapBuyProperty(Map<String, Object> data, DtoHandlerResponse response) {

    }
}
