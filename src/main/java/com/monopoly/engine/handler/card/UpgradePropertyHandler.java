package com.monopoly.engine.handler.card;

import com.monopoly.domain.dto.request.DtoHandlerRequest;
import com.monopoly.domain.dto.request.card.DtoUpgradePropertyRequest;
import com.monopoly.domain.dto.response.DtoHandlerResponse;
import com.monopoly.domain.dto.response.card.DtoUpgradePropertyResponse;
import com.monopoly.domain.engine.GameSession;
import com.monopoly.domain.engine.card.PropertyCard;
import com.monopoly.service.PropertyCardService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.rmi.server.LogStream.log;

@Component
@Slf4j
public class UpgradePropertyHandler implements CardHandler<DtoUpgradePropertyResponse, DtoUpgradePropertyRequest> {

    @Override
    public DtoUpgradePropertyResponse handle(DtoUpgradePropertyRequest request) {
        PropertyCard propertyCard = request.getPropertyCard();
        Integer newLevel = request.getNewLevel();

        if (!PropertyCardService.canUpgradeProperty(propertyCard, newLevel)) {
            throw new IllegalStateException("Карта не может быть улучшена");
        }

        PropertyCardService.upgradeProperty(propertyCard, newLevel);

        log("Карта " + propertyCard.getTitle() + " была улучшена до уровня" + newLevel);

        return new DtoUpgradePropertyResponse(request.getGameSession(), propertyCard, newLevel);
    }

    @Override
    public Class<? extends DtoUpgradePropertyRequest> getSupportedRequestType() {
        return DtoUpgradePropertyRequest.class;
    }

    @Override
    public boolean canHandle(DtoUpgradePropertyRequest request) {
        return PropertyCardService.canUpgradeProperty(request.getPropertyCard(), request.getNewLevel());
    }
}
