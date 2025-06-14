package com.monopoly.domain.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.ChanceCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoChanceHandlerResponse implements IDtoCardHandlerResponse{
    private Player player;
    private GameSession gameSession;
    private ChanceCard chanceCard;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
