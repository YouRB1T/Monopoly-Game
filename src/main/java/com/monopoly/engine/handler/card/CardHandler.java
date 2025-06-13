package com.monopoly.engine.handler.card;

import com.monopoly.domain.engine.GameSession;
import com.monopoly.engine.handler.GameHandler;

public interface CardHandler extends GameHandler {
    void handle(GameSession gameSession);
}
