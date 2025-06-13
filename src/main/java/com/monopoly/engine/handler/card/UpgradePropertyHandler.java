package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.PropertyCardService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
public class UpgradePropertyHandler implements CardHandler {
    private PropertyCard propertyCard;
    private Integer newLevel;

    @Override
    public void handle(GameSession gameSession) {
        if (!PropertyCardService.canUpgradeProperty(propertyCard, newLevel)) {
            throw new IllegalStateException("Карта не может быть улучшена");
        }

        PropertyCardService.upgradeProperty(propertyCard, newLevel);

        System.out.println("Карта " + propertyCard.getTitle() + " была улучшена");
    }

    @Override
    public boolean canHandle(GameSession gameSession) {
        return PropertyCardService.canUpgradeProperty(propertyCard, newLevel);
    }
}
