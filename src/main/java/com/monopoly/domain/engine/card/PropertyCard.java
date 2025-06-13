package com.monopoly.domain.engine.card;

import com.monopoly.domain.engine.Player;
import com.monopoly.engine.handler.card.CardHandler;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PropertyCard extends BoardCard implements IOwner, IPrice, IRent, IPropertyGroup {
    private final Integer price;
    private final RentOfCard rentOfCard;
    private final String propertyGroup;
    private Player owner;

    public PropertyCard(UUID id, String title, String description, List<CardHandler> cardHandlers,
                        Integer cardPosition, Integer price, RentOfCard rentOfCard,
                        String propertyGroup, Player owner) {
        super(id, title, description, cardHandlers, cardPosition);
        this.price = price;
        this.rentOfCard = rentOfCard;
        this.propertyGroup = propertyGroup;
        this.owner = owner;
    }

    @Override
    public Integer getPrice() {
        return price;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public Map<Integer, Integer> getRentLevels() {
        return rentOfCard.getRentLevels();
    }

    @Override
    public Integer getCurrentRent() {
        return rentOfCard.getCurrentRent();
    }

    public void setCurrentRentLevel(Integer rentLevel) {
        rentOfCard.setCurrentRentLevel(rentLevel);
    }

    @Override
    public String getGroup() {
        return propertyGroup;
    }
}
