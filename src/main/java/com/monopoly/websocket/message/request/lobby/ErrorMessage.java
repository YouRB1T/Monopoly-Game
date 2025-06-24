package com.monopoly.websocket.message.request.lobby;

import com.monopoly.websocket.message.WebSocketMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorMessage extends WebSocketMessage {
    private String message;
}