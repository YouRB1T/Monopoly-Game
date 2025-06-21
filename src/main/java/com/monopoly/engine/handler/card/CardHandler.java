package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.response.card.IDtoCardHandlerResponse;
import com.monopoly.domain.engine.dto.request.card.IDtoCardHandlerRequest;
import com.monopoly.engine.handler.GameHandler;

public interface CardHandler<DtoHandlerResponse extends IDtoCardHandlerResponse,
        DtoHandlerRequest extends IDtoCardHandlerRequest> extends GameHandler<DtoHandlerRequest> {
    DtoHandlerResponse handle(DtoHandlerRequest request);
    Class<? extends DtoHandlerRequest> getSupportedRequestType();
}
