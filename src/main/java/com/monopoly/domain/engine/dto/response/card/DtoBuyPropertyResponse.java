package com.monopoly.domain.engine.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DtoBuyPropertyResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private Player player;
    private List<Card> updatedPropertyCard;
}
