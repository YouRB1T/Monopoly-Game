package com.monopoly.domain.dto.response.engine;

import com.monopoly.domain.dto.response.DtoHandlerResponse;

import java.util.List;

public interface IDtoEngineHandlerResponse extends DtoHandlerResponse {
    List<String> getExecutedHandlers();
}
