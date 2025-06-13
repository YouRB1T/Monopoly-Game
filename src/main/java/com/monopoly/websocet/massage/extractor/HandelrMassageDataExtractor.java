package com.monopoly.websocet.massage.extractor;

import com.monopoly.domain.dto.request.DtoHandlerRquest;
import com.monopoly.domain.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.dto.request.card.DtoGoPrisonRequest;
import com.monopoly.domain.dto.request.engine.DtoEndGameRequest;
import com.monopoly.mapper.GameSessionMapper;
import com.monopoly.websocet.massage.session.GameHandlerMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HandelrMassageDataExtractor implements DataExtractor{
    private final GameSessionMapper gameSessionMapper;

    @Override
    public DtoHandlerRquest extractMassageToDtoRequest(GameHandlerMessage message) {

        DtoHandlerRquest request = null;
        switch (message.getHandleType()) {
            //Engine
            case END_GAME ->
                request = new DtoEndGameRequest();
            //Card
            case BUY_PRPRTY ->
                request = new DtoBuyPropertyRequest(
                        gameSessionMapper.getPLayerById(message.getEventData().get("playerID")),

                        );
            case GO_PRSN ->
                request = new DtoGoPrisonRequest();

        }
        return request;
    }
}
