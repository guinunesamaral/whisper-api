package com.techgn.whisper.model.responses;

import java.time.LocalDateTime;
import java.util.List;

public record GetChatResponse(int id, int chatUserOneId, int chatUserTwoId, LocalDateTime createdAt,
                              List<GetMessageResponse> messages) {
}
