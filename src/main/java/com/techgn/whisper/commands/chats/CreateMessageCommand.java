package com.techgn.whisper.commands.chats;

public record CreateMessageCommand(int chatId, String text, int fromId, int toId) {
}
