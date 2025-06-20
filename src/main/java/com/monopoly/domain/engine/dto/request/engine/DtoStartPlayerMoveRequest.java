package com.monopoly.domain.engine.dto.request.engine;

import com.monopoly.domain.engine.GameSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoStartPlayerMoveRequest implements IDtoEngineHandlerRequest{
    private GameSession gameSession;
}
