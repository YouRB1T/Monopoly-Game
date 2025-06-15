package com.monopoly.engine;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.websocet.massage.request.session.RequestGameHandlerMessage;

public interface GameSessionEngine {
    DtoHandlerResponse handleGameEvent(DtoHandlerRequest request);
}
