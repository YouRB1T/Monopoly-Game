package com.monopoly.domain.engine.card.special;

import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.BoardCard;
import com.monopoly.engine.handler.card.CardHandler;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
public class PrisonCard extends BoardCard {
    private final List<Player> freePlayers;
    private final List<Player> inPrisonPLayers;

    public PrisonCard(UUID id, String title, String description, List<CardHandler> cardHandlers,
                      Integer cardPosition, List<Player> freePlayers, List<Player> inPrisonPLayers) {
        super(id, title, description, cardHandlers, cardPosition);
        this.freePlayers = freePlayers;
        this.inPrisonPLayers = inPrisonPLayers;
    }

    public void addPlayerToPrison(Player player) {
        inPrisonPLayers.add(player);
    }

    public void freePLayer(Player player) {
        if (inPrisonPLayers.contains(player)) {
            inPrisonPLayers.remove(player);
        }
        freePlayers.add(player);
    }
}
