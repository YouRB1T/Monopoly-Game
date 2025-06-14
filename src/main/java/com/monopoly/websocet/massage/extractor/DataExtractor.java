package com.monopoly.websocet.massage.extractor;

import com.monopoly.domain.dto.request.DtoHandlerRquest;
import com.monopoly.websocet.massage.session.GameHandlerMessage;
import com.monopoly.websocet.massage.session.WebSocketMessageSession;

public interface DataExtractor {
    DtoHandlerRquest extractMassageToDtoRequest(WebSocketMessageSession massage);
}
