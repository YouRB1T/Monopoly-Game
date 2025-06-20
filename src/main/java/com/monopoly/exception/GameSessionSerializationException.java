package com.monopoly.exception;

public class GameSessionSerializationException extends GameSessionException {
    
    public GameSessionSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}