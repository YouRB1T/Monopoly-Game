package com.monopoly.domain.engine.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoChangeRentResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private PropertyCard propertyCard;
    private Integer newRentLevel;
}
