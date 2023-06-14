package com.techgn.whisper.model.responses;

import java.time.LocalDateTime;

public record GetUserResponse(int id,
                              String name,
                              String email,
                              String picture,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {

}
