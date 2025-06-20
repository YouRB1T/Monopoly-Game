package com.monopoly.exception;

import java.util.UUID;

public class GameSessionNotFoundException extends GameSessionException {
    
    public GameSessionNotFoundException(UUID sessionId) {
        super("Игровая сессия с ID " + sessionId + " не найдена");
    }
}