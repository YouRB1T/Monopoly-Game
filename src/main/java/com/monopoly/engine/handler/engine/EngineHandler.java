package com.monopoly.engine.handler.engine;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.engine.handler.GameHandler;

public interface EngineHandler extends GameHandler {
    void handle(GameSession gameSession);
}
