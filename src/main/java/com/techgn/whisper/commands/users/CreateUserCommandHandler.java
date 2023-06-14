package com.techgn.whisper.commands.users;

import com.techgn.whisper.model.entities.User;
import com.techgn.whisper.model.interfaces.Command;
import com.techgn.whisper.repositories.UserRepository;
import com.techgn.whisper.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.techgn.whisper.utils.Cryptography.encodePassword;
import static com.techgn.whisper.utils.StringUtils.stringHasValue;

@Service
public class CreateUserCommandHandler implements Command<Result<Integer>, CreateUserCommand> {

    @Autowired
    private UserRepository userRepository;

    public Result<Integer> handle(CreateUserCommand command) {
        Result<Integer> resultCreate = new Result<>();

        Result<String> resultValidate = validateCreateUser(command.name(), command.email(), command.password(),
                command.picture());
        if (resultValidate.isInvalid()) return resultCreate.setFromAnother(resultValidate);

        User user = new User(command.name(), command.email(), encodePassword.apply(command.password()),
                LocalDateTime.now(), command.picture());
        resultCreate.setValue(userRepository.save(user).getId());

        return resultCreate;
    }

    public Result<Optional<User>> findByEmail(String email) {
        return new Result<>(userRepository.findByEmail(email));
    }

    private void validateName(Result<String> result, String name) {
        if (!stringHasValue.apply(name))
            result.addValidation("Name can't be empty.");
        else if (name.length() > User.NAME_MAX_LENGTH)
            result.addValidation(String.format("Name can't have more than %s characters.", User.NAME_MAX_LENGTH));
    }

    private void validateEmail(Result<String> result, String email) {
        if (!stringHasValue.apply(email))
            result.addValidation("Email can't be empty.");
        else if (email.length() > User.EMAIL_MAX_LENGTH)
            result.addValidation(String.format("Email can't have more than %s characters.", User.EMAIL_MAX_LENGTH));
        else if (findByEmail(email).getValue().isPresent())
            result.addValidation("There's already an user for this email.");
    }

    private void validatePassword(Result<String> result, String password) {
        if (!stringHasValue.apply(password))
            result.addValidation("Password can't be empty.");
        else if (password.length() > User.PASSWORD_MAX_LENGTH)
            result.addValidation(String.format("Password can't have more than %s characters.", User.PASSWORD_MAX_LENGTH));
    }

    private void validatePicture(Result<String> result, String picture) {
        if (!stringHasValue.apply(picture))
            result.addValidation("Picture can't be empty.");
        else if (picture.length() > User.PICTURE_MAX_LENGTH)
            result.addValidation(String.format("Picture can't have more than %s characters.", User.PICTURE_MAX_LENGTH));
    }

    private Result<String> validateCreateUser(String name, String email, String password, String picture) {
        Result<String> result = new Result<>();

        validateName(result, name);
        validateEmail(result, email);
        validatePassword(result, password);
        validatePicture(result, picture);

        return result;
    }
}
