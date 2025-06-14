package com.monopoly.domain.dto.response.engine;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DtoStartGameResponse implements IDtoEngineHandlerResponse{
    private UUID gameSessionId;
    private List<String> executedHandlers;
}
