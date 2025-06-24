package com.monopoly.websocket.message.response.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorMessage extends ResponseWebSocketMessageLobby {
    private String message;
    private String errorCode;

    public ErrorMessage() {
        setType("ERROR");
    }

    public ErrorMessage(String message) {
        this();
        this.message = message;
    }

    public ErrorMessage(String message, String errorCode) {
        this();
        this.message = message;
        this.errorCode = errorCode;
    }
}
