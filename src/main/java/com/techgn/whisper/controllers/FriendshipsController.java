package com.techgn.whisper.controllers;

import com.techgn.whisper.commands.friendships.CreateFriendshipCommand;
import com.techgn.whisper.commands.friendships.CreateFriendshipCommandHandler;
import com.techgn.whisper.model.dtos.FriendshipDTO;
import com.techgn.whisper.model.responses.GetFriendshipResponse;
import com.techgn.whisper.model.responses.GetUserResponse;
import com.techgn.whisper.queries.FriendshipQueries;
import com.techgn.whisper.shared.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipsController {

    private final CreateFriendshipCommandHandler createFriendshipCommandHandler;
    private final FriendshipQueries friendshipQueries;

    public FriendshipsController(CreateFriendshipCommandHandler createFriendshipCommandHandler, FriendshipQueries friendshipQueries) {
        this.createFriendshipCommandHandler = createFriendshipCommandHandler;
        this.friendshipQueries = friendshipQueries;
    }

    @PostMapping
    public ResponseEntity<Result<Void>> create(@RequestBody CreateFriendshipCommand command) {
        Result<Void> resultCreateFriendship = createFriendshipCommandHandler.handle(command);
        if (resultCreateFriendship.isInvalid()) return new ResponseEntity<>(resultCreateFriendship,
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(resultCreateFriendship, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Result<List<GetFriendshipResponse>>> findById(@PathVariable int userId) {
        Result<List<GetFriendshipResponse>> result = new Result<>();

        Result<List<FriendshipDTO>> resultFindFriendships = friendshipQueries.findDtosByUserId(userId);
        if (resultFindFriendships.isInvalid()) return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        List<GetFriendshipResponse> friendships = resultFindFriendships.getValue()
                .stream().map(f -> {
                    var userOne = f.userOne();
                    var userOneResponse = new GetUserResponse(userOne.id(), userOne.name(), userOne.email(),
                            userOne.picture(), userOne.createdAt(), userOne.updatedAt());

                    var userTwo = f.userTwo();
                    var userTwoResponse = new GetUserResponse(userTwo.id(), userTwo.name(), userTwo.email(),
                            userTwo.picture(), userTwo.createdAt(), userTwo.updatedAt());

                    return new GetFriendshipResponse(userOneResponse, userTwoResponse, f.createdAt());
                }).toList();
        result.setValue(friendships);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
