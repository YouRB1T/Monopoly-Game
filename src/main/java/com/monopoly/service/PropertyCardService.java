package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import org.springframework.stereotype.Service;

@Service
public class PropertyCardService {
    public void transferProperty(GameSession session, PropertyCard card, Player from, Player to) {
        if (from != null) {
            session.getPropertyCardOwners().remove(card);
        }
        session.getPropertyCardOwners().put(card, to);
    }

    public boolean isPropertyOwned(GameSession session, PropertyCard card) {
        return session.getPropertyCardOwners().containsKey(card);
    }

    public Player getPropertyOwner(GameSession session, PropertyCard card) {
        return session.getPropertyCardOwners().get(card);
    }

    public Integer calculateRent(PropertyCard card) {
        return card.getRentOfCard().getCurrentRent();
    }

    public boolean canUpgradeProperty(PropertyCard card, Integer upgradeLevel) {
        return card.getRentOfCard().getRentLevels().get(upgradeLevel) != null;
    }

    public  void upgradeProperty(PropertyCard card, Integer upgradeLevel) {
        if (canUpgradeProperty(card, upgradeLevel)) {
            card.setCurrentRentLevel(upgradeLevel);
        }
    }
}
