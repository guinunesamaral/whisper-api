package com.techgn.whisper.queries;

import com.techgn.whisper.model.dtos.UserDTO;
import com.techgn.whisper.model.entities.User;
import com.techgn.whisper.repositories.UserRepository;
import com.techgn.whisper.shared.ErrorMessages;
import com.techgn.whisper.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.techgn.whisper.utils.Cryptography.passwordsMatch;

@Service
public class UserQueries {

    @Autowired
    private UserRepository userRepository;

    public Result<User> findById(int id) {
        Result<User> result = new Result<>();

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return result.setBusinessMessage(String.format("User(%s) not found.", id));

        result.setValue(user.get());
        return result;
    }

    public Result<UserDTO> findDtoById(int id) {
        Result<UserDTO> result = new Result<>();

        Optional<UserDTO> userDTO = userRepository.findById(id).map((u) -> new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getPicture(), u.getCreatedAt(), u.getUpdatedAt()));
        if (userDTO.isEmpty()) return result.setBusinessMessage(ErrorMessages.USER_NOT_FOUND);

        result.setValue(userDTO.get());
        return result;
    }

    private Result<Optional<User>> findByEmail(String email) {
        return new Result<>(userRepository.findByEmail(email));
    }

    public Result<List<UserDTO>> findByNameOrEmail(String name, String email) {
        Result<List<UserDTO>> result = new Result<>();

        Optional<List<User>> users = userRepository.findByNameOrEmailContaining(name, email);
        List<UserDTO> userDTOS = new ArrayList<>();
        if (users.isPresent()) {
            userDTOS = users.get()
                    .stream()
                    .map(u -> new UserDTO(
                            u.getId(),
                            u.getName(),
                            u.getEmail(),
                            u.getPassword(),
                            u.getPicture(),
                            u.getCreatedAt(),
                            u.getUpdatedAt()))
                    .toList();
        }
        result.setValue(userDTOS);

        return result;
    }

    public Result<User> findByEmailAndPassword(String email, String password) {
        Result<User> result = new Result<>();

        Result<Optional<User>> resultFindByEmail = findByEmail(email);
        if (resultFindByEmail.isInvalid()) return result.setFromAnother(resultFindByEmail);
        else if (resultFindByEmail.getValue().isEmpty()) return result.setBusinessMessage(ErrorMessages.USER_NOT_FOUND);

        User user = resultFindByEmail.getValue().get();
        if (!passwordsMatch.apply(password, user.getPassword()))
            return result.setBusinessMessage("User found but passwords doesn't match.");

        result.setValue(user);

        return result;
    }
}
