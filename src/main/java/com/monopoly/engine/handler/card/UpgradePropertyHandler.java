package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.dto.request.card.DtoUpgradePropertyRequest;
import com.monopoly.domain.engine.dto.response.card.DtoUpgradePropertyResponse;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.PropertyCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpgradePropertyHandler implements CardHandler<DtoUpgradePropertyResponse, DtoUpgradePropertyRequest> {

    @Autowired
    private PropertyCardService propertyCardService;

    @Override
    public DtoUpgradePropertyResponse handle(DtoUpgradePropertyRequest request) {
        PropertyCard propertyCard = request.getPropertyCard();
        Integer newLevel = request.getNewLevel();

        if (!propertyCardService.canUpgradeProperty(propertyCard, newLevel)) {
            log.info("Card can't be upgraded" + " session " + request.getGameSession().getId());
            throw new IllegalStateException("Card can't be upgraded");
        }

        propertyCardService.upgradeProperty(propertyCard, newLevel);
        log.info("Card upgraded" + " session " + request.getGameSession().getId());

        return new DtoUpgradePropertyResponse(request.getGameSession(), propertyCard, newLevel);
    }

    @Override
    public Class<? extends DtoUpgradePropertyRequest> getSupportedRequestType() {
        return DtoUpgradePropertyRequest.class;
    }

    @Override
    public boolean canHandle(DtoUpgradePropertyRequest request) {
        return propertyCardService.canUpgradeProperty(request.getPropertyCard(), request.getNewLevel());
    }
}
