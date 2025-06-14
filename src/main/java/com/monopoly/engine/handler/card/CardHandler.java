package com.monopoly.engine.handler.card;

import com.monopoly.engine.handler.GameHandler;

public interface CardHandler<DtoHandlerResponse extends com.monopoly.domain.dto.response.DtoHandlerResponse,
        DtoHandlerRequest extends com.monopoly.domain.dto.request.DtoHandlerRequest> extends GameHandler<DtoHandlerRequest> {
    DtoHandlerResponse handle(DtoHandlerRequest request);
}
