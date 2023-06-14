package com.techgn.whisper.queries;

import com.techgn.whisper.model.dtos.ChatDTO;
import com.techgn.whisper.model.dtos.MessageDTO;
import com.techgn.whisper.model.entities.Chat;
import com.techgn.whisper.repositories.ChatRepository;
import com.techgn.whisper.shared.ErrorMessages;
import com.techgn.whisper.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatQueries {

    @Autowired
    private ChatRepository chatRepository;

    public Result<List<ChatDTO>> findDtosByUserId(int userId) {
        Result<List<ChatDTO>> result = new Result<>();

        if (userId <= 0) return result.setBusinessMessage(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);

        Optional<List<Chat>> chats = chatRepository.findByUserId(userId);
        List<ChatDTO> chatDTOS = new ArrayList<>();
        if (chats.isPresent()) {
            chatDTOS = chats.get().stream()
                    .map(c -> {
                        var messages = c.getMessages().stream().map(m -> new MessageDTO(m.getId(), m.getText(), m.getImage(),
                                m.getCreatedAt(), m.getUpdatedAt(), m.getFrom().getId(), m.getTo().getId())).toList();
                        return new ChatDTO(c.getId(), c.getCreatedAt(), c.getChatUserOneId(), c.getChatUserTwoId(), messages);
                    }).toList();
        }
        result.setValue(chatDTOS);

        return result;
    }

    public Result<ChatDTO> findDtoById(int chatId) {
        Result<ChatDTO> result = new Result<>();

        if (chatId <= 0) return result.setBusinessMessage(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);
        Optional<ChatDTO> chatDTO = chatRepository
                .findById(chatId)
                .map(c -> new ChatDTO(
                        c.getId(),
                        c.getCreatedAt(),
                        c.getChatUserOneId(),
                        c.getChatUserTwoId(),
                        c.getMessages().stream().map(m -> new MessageDTO(m.getId(), m.getText(), m.getImage(),
                                m.getCreatedAt(), m.getUpdatedAt(), m.getFrom().getId(), m.getTo().getId())).toList()
                ));
        if (chatDTO.isEmpty()) return result.setBusinessMessage(ErrorMessages.CHAT_NOT_FOUND);

        result.setValue(chatDTO.get());
        return result;
    }
}
