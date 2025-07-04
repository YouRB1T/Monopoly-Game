package com.monopoly.domain.engine.dto.request.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoBuyPropertyRequest implements IDtoCardHandlerRequest{
    private GameSession gameSession;
    private Player player;
    private PropertyCard card;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
