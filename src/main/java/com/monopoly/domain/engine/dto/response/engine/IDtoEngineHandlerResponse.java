package com.monopoly.domain.engine.dto.response.engine;

import com.monopoly.domain.engine.dto.response.DtoHandlerResponse;

import java.util.List;

public interface IDtoEngineHandlerResponse extends DtoHandlerResponse {
    List<String> getExecutedHandlers();
}
