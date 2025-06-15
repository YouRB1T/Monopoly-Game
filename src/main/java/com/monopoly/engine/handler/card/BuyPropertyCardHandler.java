package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.dto.response.card.DtoBayPropertyResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.*;
import com.monopoly.service.PropertyCardService;
import com.monopoly.service.PlayerService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Setter
public class BuyPropertyCardHandler implements CardHandler<DtoBayPropertyResponse, DtoBuyPropertyRequest>{
    @Autowired
    private PlayerService playerService;


    @Override
    public DtoBayPropertyResponse handle(DtoBuyPropertyRequest request) {
        Player player = request.getPlayer();
        PropertyCard card = request.getCard();
        GameSession gameSession = request.getGameSession();

        if (PropertyCardService.isPropertyOwned(gameSession, card)) {
            throw new IllegalStateException("Карта уже принадлежит другому игроку");
        }

        if (player.getMoneys() < card.getPrice()) {
            throw new IllegalStateException("Недостаточно средств для покупки карты");
        }

        playerService.subMoneys(player, card.getPrice());
        player.getPlayerCards().add(card);
        gameSession.getPropertyCardOwners().put(card, player);
        if (hasPropertyGroup(player, gameSession.getPropertyGroups().get(card.getGroup()))) {
            appLevelForGroup(gameSession, card.getGroup());
        }

        return new DtoBayPropertyResponse(gameSession, player);
    }

    private boolean hasPropertyGroup(Player player, PropertyGroup group) {
        List<Card> cards = player.getPlayerCards().stream()
                .filter(card -> card instanceof IPropertyGroup)
                .filter(card -> ((IPropertyGroup) card).getGroup().equals(group.getName()))
                .toList();

        for (Card card : group.getPropertyCards()) {
            if (cards.contains(card)) {
                cards.remove(card);
            } else {
                return false;
            }
        }

        return true;
    }

    private void appLevelForGroup(GameSession gameSession, String group) {
        gameSession.getPropertyGroups().get(group).getPropertyCards().stream().forEach(propertyCard -> {
            if (propertyCard.getCurrentRent() < 3) {
                propertyCard.setCurrentRentLevel(3);
            }
        });
    }

    @Override
    public boolean canHandle(DtoBuyPropertyRequest request) {
        return !PropertyCardService.isPropertyOwned(request.getGameSession(), request.getCard())
                && request.getPlayer().getMoneys() > request.getCard().getPrice();
    }
}
