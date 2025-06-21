package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoStartCardMoveRequest;
import com.monopoly.domain.engine.dto.response.card.DtoStartCardMoveResponse;
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
        log.info("Player " + request.getPlayer().getName() + " get " + request.getRewordForCircle() +
                " for circle " + " session " + request.getGameSession().getId());
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
