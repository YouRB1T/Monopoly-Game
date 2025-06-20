package com.monopoly.engine;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;

public interface GameSessionEngine {
    DtoHandlerResponse handleGameEvent(DtoHandlerRequest request);
}
