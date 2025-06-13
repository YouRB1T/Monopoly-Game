package com.monopoly.engine.manager;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.engine.handler.GameHandler;
import com.monopoly.websocet.massage.session.GameHandlerMessage;

import java.util.List;

public interface HandlerManager {
    List<GameHandler> getAllHandles();
    void handleEvent(GameHandlerMessage massage, GameSession gameSession);
}
