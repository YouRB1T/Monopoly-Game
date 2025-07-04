package com.monopoly.service;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.Card;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.IPrice;
import com.monopoly.domain.engine.card.PropertyCard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    public void deleteCard(Card card, Player player) {
        player.getPlayerCards().remove(card);
    }

    public void addCard(Card card, Player player) {
       player.getPlayerCards().add(card);
    }

    public boolean hasCard(Card card, Player player) {
        return player.getPlayerCards().contains(card);
    }

    public void sellCard(IPrice card, Player player) {
        player.getPlayerCards().remove(card);
        player.setTotalMoneys(player.getTotalMoneys() + card.getPrice());
        player.setMoneys(player.getMoneys() + card.getPrice());
    }

    public List<PropertyCard> getPlayerProperties(GameSession session, Player player) {
        return session.getPropertyCardOwners().entrySet().stream()
                .filter(entry -> entry.getValue().equals(player))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void addMoneys(Player player, Integer amount) {
        player.setTotalMoneys(player.getTotalMoneys() + amount);
        player.setMoneys(player.getMoneys() + amount);
    }

    public void subMoneys(Player player, Integer amount) {
        player.setTotalMoneys(player.getTotalMoneys() - amount);
        player.setMoneys(player.getMoneys() - amount);
    }

    public boolean isBankrupt(Player player) {
        return player.getTotalMoneys() <= 0;
    }
}
