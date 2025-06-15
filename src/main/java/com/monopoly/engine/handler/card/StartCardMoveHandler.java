package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoStartCardMoveRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.DtoStartCardMoveResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartCardMoveHandler implements CardHandler<DtoStartCardMoveResponse, DtoStartCardMoveRequest>{

    @Autowired
    private PlayerService playerService;

    @Override
    public DtoStartCardMoveResponse handle(DtoStartCardMoveRequest request) {
        playerService.addMoneys(request.getPlayer(), request.getRewordForCircle());
        return new DtoStartCardMoveResponse(request.getGameSession(), request.getPlayer(),
                request.getRewordForCircle());
    }

    @Override
    public Class<? extends DtoStartCardMoveRequest> getSupportedRequestType() {
        return DtoStartCardMoveRequest.class;
    }

    @Override
    public boolean canHandle(DtoStartCardMoveRequest request) {
        return true;
    }
}
