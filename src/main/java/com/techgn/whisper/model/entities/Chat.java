package com.techgn.whisper.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_chat_users", columnNames = {"chat_user_one_id",
        "chat_user_two_id"}))
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_user_one_id", nullable = false, insertable = false, updatable = false)
    private Integer chatUserOneId;

    @Column(name = "chat_user_two_id", nullable = false, insertable = false, updatable = false)
    private Integer chatUserTwoId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumns(value = {
            @JoinColumn(name = "chat_user_one_id", referencedColumnName = "user_one_id"),
            @JoinColumn(name = "chat_user_two_id", referencedColumnName = "user_two_id")
    }, foreignKey = @ForeignKey(name = "fk_chat_friendship"))
    private Friendship friendship;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    public Chat() {
    }

    public Chat(Integer id) {
        this.id = id;
    }

    public Chat(LocalDateTime createdAt, Friendship friendship) {
        this.createdAt = createdAt;
        this.friendship = friendship;
    }
}
