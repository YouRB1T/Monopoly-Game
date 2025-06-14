package com.monopoly.engine;

import com.monopoly.domain.dto.request.DtoHandlerRquest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;

public interface GameSessionEngine {
    DtoHandlerResponse handleEvent(DtoHandlerRquest request);
}
