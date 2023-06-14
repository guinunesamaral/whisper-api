package com.techgn.whisper.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Friendship {
    @EmbeddedId
    private FriendshipKey id;

    @ManyToOne
    @MapsId("userOneId")
    @JoinColumn(name = "user_one_id", foreignKey = @ForeignKey(name = "fk_friendship_user_one"))
    private User userOne;

    @ManyToOne
    @MapsId("userTwoId")
    @JoinColumn(name = "user_two_id", foreignKey = @ForeignKey(name = "fk_friendship_user_two"))
    private User userTwo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Friendship() {
    }

    public Friendship(FriendshipKey friendshipKey, User userOne, User userTwo, LocalDateTime createdAt) {
        this.id = friendshipKey;
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.createdAt = createdAt;
    }
}
