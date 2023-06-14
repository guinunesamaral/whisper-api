package com.techgn.whisper.model.responses;

import java.time.LocalDateTime;

public record GetFriendshipResponse(GetUserResponse userOne, GetUserResponse userTwo, LocalDateTime createdAt) {
}
