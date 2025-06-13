package com.monopoly.websocet.massage.extractor;

import com.monopoly.domain.dto.request.DtoHandlerRquest;
import com.monopoly.websocet.massage.session.GameHandlerMessage;

public interface DataExtractor {
    DtoHandlerRquest extractMassageToDtoRequest(GameHandlerMessage massage);
}
