package com.monopoly.domain.dto.response.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoSaleCardResponse implements IDtoCardHandlerResponse{
    private GameSession gameSession;
    private Player oldOwner;
    private Player newOwner;
    private PropertyCard propertyCard;
    private Integer price;
}
