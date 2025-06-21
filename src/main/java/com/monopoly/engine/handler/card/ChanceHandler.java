package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoChanceHandlerRequest;
import com.monopoly.domain.engine.dto.response.card.DtoChanceHandlerResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.ChanceCard;
import com.monopoly.service.PrisonService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
@Setter
@RequiredArgsConstructor
public class ChanceHandler implements CardHandler<DtoChanceHandlerResponse, DtoChanceHandlerRequest> {
    private final Random random = new Random();

    @Autowired
    private PrisonService prisonService;

    @Override
    public DtoChanceHandlerResponse handle(DtoChanceHandlerRequest request) {
        GameSession gameSession = request.getGameSession();
        Player player = request.getPlayer();
        List<ChanceCard> chanceCards = gameSession.getBoards().get(0).getChanceCards();
        if (chanceCards.isEmpty()) {
            return new DtoChanceHandlerResponse(player, gameSession, null);
        }

        ChanceCard chanceCard = chanceCards.get(random.nextInt(chanceCards.size()));

        switch (chanceCard.getType()) {
            case GO_TO_PRISON:
                prisonService.sandToPrison(player, gameSession);
                log.info("Player " + player.getName() + " go to prison by card " + chanceCard.getTitle() + " session " + gameSession.getId());
                break;
            case FREE_PRISON_CARD:
                prisonService.giveFreePrisonCard(player);
                log.info("Player " + player.getName() + " get free prison card by card " + chanceCard.getTitle() + " session " + gameSession.getId());
                break;
        }

        return new DtoChanceHandlerResponse(player, gameSession, chanceCard);
    }

    @Override
    public Class<? extends DtoChanceHandlerRequest> getSupportedRequestType() {
        return DtoChanceHandlerRequest.class;
    }

    @Override
    public boolean canHandle(DtoChanceHandlerRequest request) {
        return request.getGameSession().getBoards().get(0).getChanceCards().isEmpty();
    }
}
