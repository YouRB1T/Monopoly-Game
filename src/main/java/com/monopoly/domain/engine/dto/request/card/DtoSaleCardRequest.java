package com.monopoly.domain.engine.dto.request.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoSaleCardRequest implements IDtoCardHandlerRequest{
    private GameSession gameSession;
    private Player oldOwner;
    private Player newOwner;
    private PropertyCard propertyCard;
    private Integer price;
}
