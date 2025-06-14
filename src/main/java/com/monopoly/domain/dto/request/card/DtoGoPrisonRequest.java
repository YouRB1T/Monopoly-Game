package com.monopoly.domain.dto.request.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoGoPrisonRequest implements IDtoCardHandlerRequest{
    private GameSession gameSession;
    private Player player;
    private Card prisonCard;

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
