package com.monopoly.engine.manager;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.websocet.massage.session.GameHandlerMessage;

public class DtoResponseManager {
    private DtoHandlerRequest requestFromMessage(GameHandlerMessage message) {
        switch (message.getHandleType()) {
            case CHANCE ->
                ;
            case GO_PRSN ->
                ;
            case PAY_RENT ->
                ;
            case TREASURY ->
                ;
            case CHNG_RENT ->
                ;
            case ROLL_DICE ->
                ;
            case SALE_CARD ->
                ;
            case BUY_PRPRTY ->
                ;
            case UPGRD_PRPTY ->
                ;
            case END_PLR_MOVE ->
                ;
            case STRT_PLR_MOVE ->
                ;
            case FREE_PRSN_CARD ->
                ;
        }
    }
}
