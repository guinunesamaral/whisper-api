package com.techgn.whisper.commands.friendships;

import com.techgn.whisper.model.entities.Chat;
import com.techgn.whisper.model.entities.Friendship;
import com.techgn.whisper.model.entities.FriendshipKey;
import com.techgn.whisper.model.entities.User;
import com.techgn.whisper.model.interfaces.Command;
import com.techgn.whisper.queries.UserQueries;
import com.techgn.whisper.repositories.ChatRepository;
import com.techgn.whisper.repositories.FriendshipRepository;
import com.techgn.whisper.shared.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CreateFriendshipCommandHandler implements Command<Result<Void>, CreateFriendshipCommand> {

    private final UserQueries userService;
    private final ChatRepository chatRepository;
    private final FriendshipRepository friendshipRepository;

    public CreateFriendshipCommandHandler(UserQueries userService, ChatRepository chatRepository, FriendshipRepository friendshipRepository) {
        this.userService = userService;
        this.chatRepository = chatRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Result<Void> handle(CreateFriendshipCommand command) {
        Result<Void> result = new Result<>();

        Result<User> resultFindUserOne = userService.findById(command.userOneId());
        if (resultFindUserOne.isInvalid()) result.addValidation(resultFindUserOne.getMessage());

        Result<User> resultFindUserTwo = userService.findById(command.userTwoId());
        if (resultFindUserTwo.isInvalid()) result.addValidation(resultFindUserTwo.getMessage());

        if (result.isInvalid()) return result;

        Optional<Friendship> friendship = friendshipRepository.findByUserOneAndUserTwoIds(command.userOneId(), command.userTwoId());
        if (friendship.isPresent()) return result.setBusinessMessage("These users are already friends.");

        FriendshipKey friendshipKey = new FriendshipKey(command.userOneId(), command.userTwoId());
        Friendship f = new Friendship(friendshipKey, resultFindUserOne.getValue(), resultFindUserTwo.getValue(),
                LocalDateTime.now());

        friendshipRepository.save(f);
        chatRepository.save(new Chat(LocalDateTime.now(), f));

        return result;
    }
}
