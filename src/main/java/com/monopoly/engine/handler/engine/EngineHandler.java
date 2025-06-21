package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.dto.request.engine.IDtoEngineHandlerRequest;
import com.monopoly.domain.engine.dto.response.engine.IDtoEngineHandlerResponse;
import com.monopoly.engine.handler.GameHandler;

public interface EngineHandler<Res extends IDtoEngineHandlerResponse, Req extends IDtoEngineHandlerRequest>
        extends GameHandler<Req> {
    Res handle(Req gameSession);
}
