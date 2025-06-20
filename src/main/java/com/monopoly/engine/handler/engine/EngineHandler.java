package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.dto.request.DtoHandlerRequest;
import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;
import com.monopoly.engine.handler.GameHandler;

public interface EngineHandler<Res extends DtoHandlerResponse, Req extends DtoHandlerRequest>
        extends GameHandler<Req> {
    Res handle(Req gameSession);
}
