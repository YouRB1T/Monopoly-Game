package com.monopoly.domain.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoStartCardMoveResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private Player player;
    private Integer rewordForCircle;
}
