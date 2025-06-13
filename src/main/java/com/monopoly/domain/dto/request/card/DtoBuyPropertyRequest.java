package com.monopoly.domain.dto.request.card;

import com.monopoly.domain.engine.Player;
import com.monopoly.domain.engine.card.PropertyCard;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoBuyPropertyRequest implements IDtoCardHandlerRequest{
    private Player player;
    private PropertyCard card;
}
