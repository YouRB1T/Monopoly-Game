package com.monopoly.domain.engine.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoGoPrisonResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private Player player;
    private Card prisonCard;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
