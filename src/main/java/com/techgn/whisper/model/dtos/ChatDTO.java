package com.techgn.whisper.model.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record ChatDTO(int id,
                      LocalDateTime createdAt,
                      int chatUserOneId,
                      int chatUserTwoId,
                      List<MessageDTO> messages) {
}
