package com.monopoly.domain.engine.dto.request.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoTreasuryHandlerRequest implements IDtoCardHandlerRequest{
    private GameSession gameSession;
    private Player player;
}
