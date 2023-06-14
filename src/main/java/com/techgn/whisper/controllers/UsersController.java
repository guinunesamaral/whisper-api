package com.techgn.whisper.controllers;

import com.techgn.whisper.commands.users.*;
import com.techgn.whisper.model.dtos.UserDTO;
import com.techgn.whisper.model.responses.GetUserResponse;
import com.techgn.whisper.queries.UserQueries;
import com.techgn.whisper.shared.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserQueries userQueries;
    private final LoginCommandHandler loginCommandHandle;
    private final CreateUserCommandHandler createUserCommandHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;

    public UsersController(UserQueries userQueries, LoginCommandHandler loginCommandHandle, CreateUserCommandHandler createUserCommandHandler, UpdateUserCommandHandler updateUserCommandHandler) {
        this.userQueries = userQueries;
        this.loginCommandHandle = loginCommandHandle;
        this.createUserCommandHandler = createUserCommandHandler;
        this.updateUserCommandHandler = updateUserCommandHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<Result<Void>> login(@RequestBody LoginCommand command) {
        Result<Void> resultLogin = loginCommandHandle.handle(command);
        if (resultLogin.isInvalid()) return new ResponseEntity<>(new Result<>(resultLogin), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new Result<>(resultLogin), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Result<List<GetUserResponse>>> findByNameOrEmail(@RequestParam String name, String email) {
        Result<List<GetUserResponse>> result = new Result<>();

        Result<List<UserDTO>> resultFindByNameOrEmail = userQueries.findByNameOrEmail(name, email);
        if (resultFindByNameOrEmail.isInvalid())
            return new ResponseEntity<>(result.setBusinessMessage(resultFindByNameOrEmail.getMessage()), HttpStatus.BAD_REQUEST);

        List<GetUserResponse> users = resultFindByNameOrEmail.getValue().stream().map(u -> new GetUserResponse(u.id(),
                u.name(), u.email(), u.picture(), u.createdAt(), u.updatedAt())).toList();
        result.setValue(users);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Result<GetUserResponse>> findById(@PathVariable int userId) {
        Result<GetUserResponse> result = new Result<>();

        Result<UserDTO> resultFindDtoById = userQueries.findDtoById(userId);
        if (resultFindDtoById.isInvalid())
            return new ResponseEntity<>(result.setBusinessMessage(resultFindDtoById.getMessage()), HttpStatus.BAD_REQUEST);

        GetUserResponse getUserResponse = new GetUserResponse(resultFindDtoById.getValue().id(), resultFindDtoById.getValue().name(),
                resultFindDtoById.getValue().email(), resultFindDtoById.getValue().picture(),
                resultFindDtoById.getValue().createdAt(), resultFindDtoById.getValue().updatedAt());
        result.setValue(getUserResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result<Integer>> create(@RequestBody CreateUserCommand command) {
        Result<Integer> resultCreate = createUserCommandHandler.handle(command);
        if (resultCreate.isInvalid()) return new ResponseEntity<>(resultCreate, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(resultCreate, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Result<Void>> update(@PathVariable int userId, @RequestBody UpdateUserCommand command) {
        command.setId(userId);
        Result<Void> resultUpdate = updateUserCommandHandler.handle(command);
        if (resultUpdate.isInvalid()) return new ResponseEntity<>(resultUpdate, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(resultUpdate, HttpStatus.OK);
    }
}
