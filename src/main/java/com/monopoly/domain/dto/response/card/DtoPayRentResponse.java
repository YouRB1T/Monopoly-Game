package com.monopoly.domain.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoPayRentResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private Player player;
    private PropertyCard propertyCard;
    private Player owner;
    private Integer rent;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
