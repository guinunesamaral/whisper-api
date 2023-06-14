package com.techgn.whisper.model.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(int id,
                      String name,
                      String email,
                      String password,
                      String picture,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      List<MessageDTO> messagesSent,
                      List<MessageDTO> messagesReceived,
                      List<FriendshipDTO> friendships) {
    public UserDTO(int id) {
        this(id, null, null, null, null, null, null, null, null, null);
    }

    public UserDTO(int id, String name, String email, String picture, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(id, name, email, null, picture, createdAt, updatedAt, null, null, null);
    }

    public UserDTO(int id, String name, String email, String password, String picture, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(id, name, email, password, picture, createdAt, updatedAt, null, null, null);
    }
}
