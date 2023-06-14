package com.techgn.whisper.queries;

import com.techgn.whisper.model.dtos.FriendshipDTO;
import com.techgn.whisper.model.dtos.FriendshipKeyDTO;
import com.techgn.whisper.model.dtos.UserDTO;
import com.techgn.whisper.model.entities.Friendship;
import com.techgn.whisper.repositories.FriendshipRepository;
import com.techgn.whisper.shared.ErrorMessages;
import com.techgn.whisper.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipQueries {

    @Autowired
    private FriendshipRepository friendshipRepository;

    public Result<List<Friendship>> findByUserId(int userId) {
        Result<List<Friendship>> result = new Result<>();

        if (userId <= 0) result.addValidation(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);
        if (result.isInvalid()) return result;

        Optional<List<Friendship>> resultFindFriendships = friendshipRepository.findByUserOneOrTwoIdEqualsTo(userId);
        resultFindFriendships.ifPresent(result::setValue);

        return result;
    }

    public Result<List<FriendshipDTO>> findDtosByUserId(int userId) {
        Result<List<FriendshipDTO>> result = new Result<>();

        if (userId <= 0) result.addValidation(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);
        if (result.isInvalid()) return result;

        Optional<List<Friendship>> resultFindFriendships = friendshipRepository.findByUserOneOrTwoIdEqualsTo(userId);

        List<FriendshipDTO> friendships = new ArrayList<>();
        if (resultFindFriendships.isPresent()) {
            friendships = resultFindFriendships
                    .get().stream()
                    .map(f -> {
                        var keyDTO = new FriendshipKeyDTO(f.getUserTwo().getId(), f.getUserTwo().getId());

                        var userOne = f.getUserOne();
                        var userOneDTO = new UserDTO(userOne.getId(), userOne.getName(), userOne.getEmail(),
                                userOne.getPicture(), userOne.getCreatedAt(), userOne.getUpdatedAt());

                        var userTwo = f.getUserTwo();
                        var userTwoDTO = new UserDTO(userTwo.getId(), userTwo.getName(), userTwo.getEmail(),
                                userTwo.getPicture(), userTwo.getCreatedAt(), userTwo.getUpdatedAt());

                        return new FriendshipDTO(keyDTO, userOneDTO, userTwoDTO, f.getCreatedAt());
                    }).toList();
        }
        result.setValue(friendships);

        return result;
    }
}