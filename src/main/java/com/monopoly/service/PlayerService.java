package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PricedCard;
import com.monopoly.domain.engine.card.PropertyCard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerService {
    public static void deleteCard(Card card, Player player) {
        player.getPlayerCards().remove(card);
    }

    public static void addCard(Card card, Player player) {
       player.getPlayerCards().add(card);
    }

    public static boolean hasCard(Card card, Player player) {
        return player.getPlayerCards().contains(card);
    }

    public static void sellCard(PricedCard card, Player player) {
        player.getPlayerCards().remove(card);
        player.setTotalMoneys(player.getTotalMoneys() + card.getPrice());
        player.setMoneys(player.getMoneys() + card.getPrice());
    }

    public static List<PropertyCard> getPlayerProperties(GameSession session, Player player) {
        return session.getPropertyCardOwners().entrySet().stream()
                .filter(entry -> entry.getValue().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void addMoneys(Player player, Integer amount) {
        player.setTotalMoneys(player.getTotalMoneys() + amount);
        player.setMoneys(player.getMoneys() + amount);
    }

    public static void subMoneys(Player player, Integer amount) {
        player.setTotalMoneys(player.getTotalMoneys() - amount);
        player.setMoneys(player.getMoneys() - amount);
    }

    public boolean isBankrupt(Player player) {
        return player.getTotalMoneys() <= 0;
    }
}
