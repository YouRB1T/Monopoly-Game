package com.monopoly.websocet.massage.request.lobby;

import com.monopoly.websocet.massage.WebSocketMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorMessage extends WebSocketMessage {
    private String message;
}