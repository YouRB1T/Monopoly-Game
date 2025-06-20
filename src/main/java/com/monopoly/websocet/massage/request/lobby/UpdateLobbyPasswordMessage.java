package com.monopoly.websocet.massage.request.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateLobbyPasswordMessage extends ResponseWebSocketMessageLobby {
    private String newPassword;
}