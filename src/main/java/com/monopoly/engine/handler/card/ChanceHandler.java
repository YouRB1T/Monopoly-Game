package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoChanceHandlerRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.DtoChanceHandlerResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.ChanceCard;
import com.monopoly.domain.engine.card.special.PrisonCard;
import com.monopoly.service.PrisonService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

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
                System.out.println("Игрок " + player.getName() + " отправлен в тюрьму");
                break;
            case FREE_PRISON_CARD:
                prisonService.giveFreePrisonCard(player);
                System.out.println("Игрок " + player.getName() + " получил карту освобождения из тюрьмы");
                break;
        }

        return new DtoChanceHandlerResponse(player, gameSession, chanceCard);
    }

    @Override
    public boolean canHandle(DtoChanceHandlerRequest request) {
        return request.getGameSession().getBoards().get(0).getChanceCards().isEmpty();
    }
}
