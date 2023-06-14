package com.techgn.whisper.controllers;

import com.techgn.whisper.commands.chats.CreateMessageCommand;
import com.techgn.whisper.commands.chats.CreateMessageCommandHandler;
import com.techgn.whisper.model.dtos.ChatDTO;
import com.techgn.whisper.model.dtos.MessageDTO;
import com.techgn.whisper.model.responses.GetChatResponse;
import com.techgn.whisper.model.responses.GetMessageResponse;
import com.techgn.whisper.queries.ChatQueries;
import com.techgn.whisper.shared.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatsController {

    private final ChatQueries chatQueries;
    private final CreateMessageCommandHandler createMessageCommandHandler;

    public ChatsController(ChatQueries chatQueries, CreateMessageCommandHandler createMessageCommandHandler) {
        this.chatQueries = chatQueries;
        this.createMessageCommandHandler = createMessageCommandHandler;
    }

    private List<GetMessageResponse> mapMessagesDtoToGetMessageResponse(List<MessageDTO> messageDTOS) {
        return messageDTOS.stream().map(m -> new GetMessageResponse(m.id(), m.text(), m.image(),
                m.createdAt(), m.updatedAt(), m.from().id(), m.to().id())).toList();
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Result<GetChatResponse>> findById(@PathVariable int chatId) {
        Result<GetChatResponse> result = new Result<>();

        Result<ChatDTO> resultGetChatDto = chatQueries.findDtoById(chatId);
        if (resultGetChatDto.isInvalid())
            return new ResponseEntity<>(result.setBusinessMessage(resultGetChatDto.getMessage()), HttpStatus.BAD_REQUEST);

        ChatDTO chatDTO = resultGetChatDto.getValue();
        result.setValue(new GetChatResponse(chatDTO.id(), chatDTO.chatUserOneId(), chatDTO.chatUserTwoId(),
                chatDTO.createdAt(), mapMessagesDtoToGetMessageResponse(chatDTO.messages())));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Result<List<GetChatResponse>>> findByUserId(@PathVariable int userId) {
        Result<List<GetChatResponse>> result = new Result<>();

        Result<List<ChatDTO>> resultChatDTOS = chatQueries.findDtosByUserId(userId);
        if (resultChatDTOS.isInvalid()) return new ResponseEntity<>(new Result<>(result), HttpStatus.BAD_REQUEST);

        List<GetChatResponse> chats = resultChatDTOS.getValue()
                .stream().map(c -> {
                    var messages = mapMessagesDtoToGetMessageResponse(c.messages());
                    return new GetChatResponse(c.id(), c.chatUserOneId(), c.chatUserTwoId(), c.createdAt(), messages);
                }).toList();
        result.setValue(chats);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Result<Integer>> createMessage(@RequestBody CreateMessageCommand command) {
        Result<Integer> resultCreateMessage = createMessageCommandHandler.handle(command);
        if (resultCreateMessage.isInvalid())
            return new ResponseEntity<>(new Result<>(resultCreateMessage), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new Result<>(resultCreateMessage), HttpStatus.OK);
    }
}
