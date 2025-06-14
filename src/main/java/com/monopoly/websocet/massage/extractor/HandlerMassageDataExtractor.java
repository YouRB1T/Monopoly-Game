package com.monopoly.websocet.massage.extractor;

import com.monopoly.domain.dto.request.DtoHandlerRquest;
import com.monopoly.domain.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.dto.request.card.DtoGoPrisonRequest;
import com.monopoly.domain.dto.request.engine.DtoEndGameRequest;
import com.monopoly.domain.dto.request.engine.DtoStartGameRequest;
import com.monopoly.mapper.GameSessionMapper;
import com.monopoly.websocet.massage.session.EndGameMessage;
import com.monopoly.websocet.massage.session.GameHandlerMessage;
import com.monopoly.websocet.massage.session.StartGameMessage;
import com.monopoly.websocet.massage.session.WebSocketMessageSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HandlerMassageDataExtractor implements DataExtractor{
    private final GameSessionMapper gameSessionMapper;

    @Override
    public DtoHandlerRquest extractMassageToDtoRequest(WebSocketMessageSession message) {

        DtoHandlerRquest request = null;
        if (message instanceof EndGameMessage) {
            return new DtoStartGameRequest(

            );
        } else if (message instanceof StartGameMessage) {

        } else if (message instanceof GameHandlerMessage) {
            switch (((GameHandlerMessage) message).getHandleType()) {
                case GO_PRSN ->
                        ;
                case CHANCE ->
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
                case STAR_MOVE ->
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
        return request;
    }
}
