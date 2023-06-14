package com.techgn.whisper.model.dtos;

import java.time.LocalDateTime;

public record FriendshipDTO(
        FriendshipKeyDTO friendshipKey,
        UserDTO userOne,
        UserDTO userTwo,
        LocalDateTime createdAt
) {
    public FriendshipDTO(int userOneId, int userTwoId, LocalDateTime createdAt) {
        this(new FriendshipKeyDTO(userOneId, userTwoId), new UserDTO(userOneId), new UserDTO(userTwoId), createdAt);
    }
}
