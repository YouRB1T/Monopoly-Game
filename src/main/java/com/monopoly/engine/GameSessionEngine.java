package com.monopoly.engine;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.websocet.massage.session.GameHandlerMessage;

public interface GameSessionEngine {
    GameSessionResponseMessage handleGameMessage(GameHandlerMessage message);
}
