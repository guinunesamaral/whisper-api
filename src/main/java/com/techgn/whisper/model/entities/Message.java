package com.techgn.whisper.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 300)
    private String text;

    @Column(length = 300)
    private String image;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "from_user_id", foreignKey = @ForeignKey(name = "fk_message_from"))
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user_id", foreignKey = @ForeignKey(name = "fk_message_to"))
    private User to;

    @ManyToOne
    @JoinColumn(name = "chat_id", foreignKey = @ForeignKey(name = "fk_message_chat"))
    private Chat chat;

    public Message() {
    }

    public Message(String text, LocalDateTime createdAt, User from, User to, Chat chat) {
        this.text = text;
        this.createdAt = createdAt;
        this.from = from;
        this.to = to;
        this.chat = chat;
    }
}
