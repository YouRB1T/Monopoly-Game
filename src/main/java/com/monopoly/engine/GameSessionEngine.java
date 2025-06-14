package com.monopoly.engine;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;

public interface GameSessionEngine {
    DtoHandlerResponse handleGameEvent(DtoHandlerRequest request);
}
