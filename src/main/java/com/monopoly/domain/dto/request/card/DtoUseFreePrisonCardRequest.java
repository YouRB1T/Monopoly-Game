package com.monopoly.domain.dto.request.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.special.PrisonCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoUseFreePrisonCardRequest implements IDtoCardHandlerRequest{
    private GameSession gameSession;
    private Player player;
    private PrisonCard card;
}
