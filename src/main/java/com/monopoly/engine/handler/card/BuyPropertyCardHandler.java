package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoBuyPropertyRequest;
import com.monopoly.domain.engine.dto.response.card.DtoBuyPropertyResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.*;
import com.monopoly.service.PropertyCardService;
import com.monopoly.service.PlayerService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Setter
public class BuyPropertyCardHandler implements CardHandler<DtoBuyPropertyResponse, DtoBuyPropertyRequest>{
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PropertyCardService propertyCardService;


    @Override
    public DtoBuyPropertyResponse handle(DtoBuyPropertyRequest request) {
        List<Card> updated = new ArrayList<>();
        Player player = request.getPlayer();
        PropertyCard card = request.getCard();
        GameSession gameSession = request.getGameSession();

        log.info("Player " + player.getName() + " buying card " + card.getTitle() + " session " + gameSession.getId());
        if (propertyCardService.isPropertyOwned(gameSession, card)) {
            log.info("Card already owned" + " session " + gameSession.getId());
            throw new IllegalStateException("Card already owned");
        }

        if (player.getMoneys() < card.getPrice()) {
            log.info("Not enough money" + " session " + gameSession.getId());
            throw new IllegalStateException("Not enough money");
        }

        playerService.subMoneys(player, card.getPrice());
        player.getPlayerCards().add(card);
        gameSession.getPropertyCardOwners().put(card, player);
        updated.add(card);
        if (hasPropertyGroup(player, gameSession.getPropertyGroups().get(card.getGroup()))) {
            log.info("Player has property group" + " session " + gameSession.getId());
            appLevelForGroup(gameSession, card.getGroup(), updated);
        }

        return new DtoBuyPropertyResponse(gameSession, player, updated);
    }

    @Override
    public Class<? extends DtoBuyPropertyRequest> getSupportedRequestType() {
        return DtoBuyPropertyRequest.class;
    }

    private boolean hasPropertyGroup(Player player, PropertyGroup group) {
        List<Card> cards = new ArrayList<>(player.getPlayerCards().stream()
                .filter(card -> card instanceof IPropertyGroup)
                .filter(card -> ((IPropertyGroup) card).getGroup().equals(group.getName()))
                .toList());

        for (Card card : group.getPropertyCards()) {
            if (cards.contains(card)) {
                cards.remove(card);
            } else {
                return false;
            }
        }

        return true;
    }

    private void appLevelForGroup(GameSession gameSession, String group, List<Card> updated) {
        gameSession.getPropertyGroups().get(group).getPropertyCards().stream().forEach(propertyCard -> {
            if (propertyCard.getCurrentRent() < 3) {
                log.info("Upgrading property " + propertyCard.getTitle() + " to level 3" + " session " + gameSession.getId());
                propertyCard.setCurrentRentLevel(3);
                updated.add(propertyCard);
            }
        });
    }

    @Override
    public boolean canHandle(DtoBuyPropertyRequest request) {
        return !propertyCardService.isPropertyOwned(request.getGameSession(), request.getCard())
                && request.getPlayer().getMoneys() > request.getCard().getPrice();
    }
}
