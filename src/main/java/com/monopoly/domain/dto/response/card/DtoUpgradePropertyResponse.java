package com.monopoly.domain.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoUpgradePropertyResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private PropertyCard propertyCard;
    private Integer newLevel;
}
