package com.monopoly.websocket.message.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateLobbyPasswordMessage extends RequestWebSocketMessageLobby {
    private UUID lobbyId;
    private String newPassword;

    public UpdateLobbyPasswordMessage() {
        setType("UPDATE_PASSWORD");
    }
}