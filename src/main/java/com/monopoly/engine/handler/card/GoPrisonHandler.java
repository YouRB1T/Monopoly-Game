package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoGoPrisonRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.DtoGoPrisonResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.card.special.PrisonCard;
import com.monopoly.service.PrisonService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.rmi.server.LogStream.log;

@Component
@Slf4j
public class GoPrisonHandler implements CardHandler<DtoGoPrisonResponse, DtoGoPrisonRequest> {

    @Autowired
    private PrisonService prisonService;

    @Override
    public DtoGoPrisonResponse handle(DtoGoPrisonRequest request) {
        Player player = request.getPlayer();
        GameSession gameSession = request.getGameSession();
        PrisonCard prisonCard = (PrisonCard) request.getPrisonCard();

        prisonService.sandToPrison(player, gameSession);
        log("Игрок " + player.getName() + " отправлен в тюрьму картой " + prisonCard.getTitle());
        return new DtoGoPrisonResponse(gameSession, player, prisonCard);
    }

    @Override
    public Class<? extends DtoGoPrisonRequest> getSupportedRequestType() {
        return null;
    }

    @Override
    public boolean canHandle(DtoGoPrisonRequest request) {
        return !request.getPlayer().isInPrison();
    }
}
