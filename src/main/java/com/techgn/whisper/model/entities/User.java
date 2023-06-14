package com.techgn.whisper.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "uzer", uniqueConstraints = @UniqueConstraint(name = "uk_uzer_email", columnNames = "email"))
public class User {
    public static final int NAME_MAX_LENGTH = 100;
    public static final int EMAIL_MAX_LENGTH = 200;
    public static final int PASSWORD_MAX_LENGTH = 72;
    public static final int PICTURE_MAX_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false, length = EMAIL_MAX_LENGTH, unique = true)
    private String email;

    @Column(nullable = false, length = PASSWORD_MAX_LENGTH)
    private String password;

    @Column(nullable = false, length = PICTURE_MAX_LENGTH)
    private String picture;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "from")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "to")
    private List<Message> messagesReceived;

    @OneToMany(mappedBy = "userOne")
    private List<Friendship> friendships;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(String name, String email, String password, LocalDateTime createdAt, String picture) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.picture = picture;
    }

    public User(Integer id, String name, String email, String password, String picture, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
