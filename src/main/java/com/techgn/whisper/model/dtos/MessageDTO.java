package com.techgn.whisper.model.dtos;

import java.time.LocalDateTime;

public record MessageDTO(
        int id,
        String text,
        String image,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserDTO from,
        UserDTO to
) {
    public MessageDTO(int id, String text, LocalDateTime createdAt, LocalDateTime updatedAt, int fromId, int toId) {
        this(id, text, null, createdAt, updatedAt, new UserDTO(fromId), new UserDTO(toId));
    }

    public MessageDTO(int id, String text, String image, LocalDateTime createdAt, LocalDateTime updatedAt, int fromId,
                      int toId) {
        this(id, text, image, createdAt, updatedAt, new UserDTO(fromId), new UserDTO(toId));
    }
}
