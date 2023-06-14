package com.techgn.whisper.model.responses;

import java.time.LocalDateTime;

public record GetMessageResponse(int id, String text, String image, LocalDateTime createdAt, LocalDateTime updatedAt,
                                 int fromId, int toId) {
}
