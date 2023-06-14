package com.techgn.whisper.commands.users;

public record CreateUserCommand(String name, String email, String password, String picture) {
}
