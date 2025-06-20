package com.monopoly.domain.engine.dto.request.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoChangeRentRequest implements IDtoCardHandlerRequest{
    private GameSession gameSession;
    private PropertyCard propertyCard;
    private Integer newRentLevel;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
