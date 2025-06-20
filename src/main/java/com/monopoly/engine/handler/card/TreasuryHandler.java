package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoTreasuryHandlerRequest;
import com.monopoly.domain.engine.dto.response.card.DtoTreasuryHandlerResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.TreasuryCard;
import com.monopoly.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class TreasuryHandler implements CardHandler<DtoTreasuryHandlerResponse, DtoTreasuryHandlerRequest> {
    private Random random = new Random();

    @Autowired
    private PlayerService playerService;

    @Override
    public DtoTreasuryHandlerResponse handle(DtoTreasuryHandlerRequest request) {
        GameSession gameSession = request.getGameSession();
        Player player = request.getPlayer();

        List<TreasuryCard> treasuryCards = gameSession.getBoards().get(0).getTreasuryCards();
        if (treasuryCards.isEmpty()) {
            return new DtoTreasuryHandlerResponse(gameSession, player, null);
        }

        TreasuryCard treasuryCard = treasuryCards.get(random.nextInt(treasuryCards.size()));

        switch (treasuryCard.getType()) {
            case MONEY_REWARD:
                playerService.addMoneys(player, treasuryCard.getValue());
                System.out.println("Игрок " + player.getName() + " получил " + treasuryCard.getValue() + " денег");
                break;
            case MONEY_PENALTY:
                playerService.subMoneys(player, treasuryCard.getValue());
                System.out.println("Игрок " + player.getName() + " заплатил штраф " + treasuryCard.getValue());
                break;
        }

        return new DtoTreasuryHandlerResponse(gameSession, player, treasuryCard);
    }

    @Override
    public Class<? extends DtoTreasuryHandlerRequest> getSupportedRequestType() {
        return DtoTreasuryHandlerRequest.class;
    }

    @Override
    public boolean canHandle(DtoTreasuryHandlerRequest request) {
        return request.getGameSession().getBoards().get(0).getTreasuryCards().isEmpty();
    }
}
