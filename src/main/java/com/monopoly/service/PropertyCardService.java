package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;

public class PropertyCardService {
    public static void transferProperty(GameSession session, PropertyCard card, Player from, Player to) {
        if (from != null) {
            session.getPropertyCardOwners().remove(card);
        }
        session.getPropertyCardOwners().put(card, to);
        card.setOwner(to);
    }

    public static boolean isPropertyOwned(GameSession session, PropertyCard card) {
        return session.getPropertyCardOwners().containsKey(card);
    }

    public static Player getPropertyOwner(GameSession session, PropertyCard card) {
        return session.getPropertyCardOwners().get(card);
    }

    public static Integer calculateRent(PropertyCard card) {
        return card.getRentOfCard().getCurrentRent();
    }

    public static boolean canUpgradeProperty(PropertyCard card, Integer upgradeLevel) {
        return card.getRentOfCard().getRentLevels().get(upgradeLevel) != null;
    }

    public static void upgradeProperty(PropertyCard card, Integer upgradeLevel) {
        if (canUpgradeProperty(card, upgradeLevel)) {
            card.setCurrentRentLevel(upgradeLevel);
        }
    }
}
