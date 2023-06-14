package com.techgn.whisper.commands.chats;

import com.techgn.whisper.model.entities.Chat;
import com.techgn.whisper.model.entities.Message;
import com.techgn.whisper.model.entities.User;
import com.techgn.whisper.model.interfaces.Command;
import com.techgn.whisper.queries.UserQueries;
import com.techgn.whisper.repositories.MessageRepository;
import com.techgn.whisper.shared.ErrorMessages;
import com.techgn.whisper.shared.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.techgn.whisper.utils.StringUtils.stringHasValue;

@Service
public class CreateMessageCommandHandler implements Command<Result<Integer>, CreateMessageCommand> {

    private final UserQueries userQueries;
    private final MessageRepository messageRepository;

    public CreateMessageCommandHandler(UserQueries userService, MessageRepository messageRepository) {
        this.userQueries = userService;
        this.messageRepository = messageRepository;
    }

    @Override
    public Result<Integer> handle(CreateMessageCommand command) {
        Result<Integer> result = new Result<>();

        validate(result, command.chatId(), command.text(), command.fromId(), command.toId());
        if (result.isInvalid()) return result;

        Result<User> resultGetFrom = userQueries.findById(command.fromId());
        if (resultGetFrom.isInvalid()) result.addValidation(resultGetFrom.getMessage());

        Result<User> resultGetTo = userQueries.findById(command.toId());
        if (resultGetTo.isInvalid()) result.addValidation(resultGetTo.getMessage());

        if (result.isInvalid()) return result;

        Message message = new Message(command.text(), LocalDateTime.now(), new User(command.fromId()), new User(command.toId()),
                new Chat(command.chatId()));
        messageRepository.save(message);

        result.setValue(message.getId());

        return result;
    }

    private void validate(Result<Integer> result, int chatId, String text, int fromId, int toId) {
        if (chatId <= 0) result.addValidation(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);
        if (!stringHasValue.apply(text)) result.addValidation("The text message can't be empty.");
        if (fromId <= 0) result.addValidation(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);
        if (toId <= 0) result.addValidation(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);
    }
}
