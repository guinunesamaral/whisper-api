package com.techgn.whisper.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class FriendshipKey implements Serializable {
    @Column(name = "user_one_id")
    private Integer userOneId;

    @Column(name = "user_two_id")
    private Integer userTwoId;

    public FriendshipKey() {
    }

    public FriendshipKey(Integer userOneId, Integer userTwoId) {
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
    }
}
