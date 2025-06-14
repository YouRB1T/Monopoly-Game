package com.monopoly.domain.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.TreasuryCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoTreasuryHandlerResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private Player player;
    private TreasuryCard treasuryCard;
}
