package com.techgn.whisper.commands.users;

import com.techgn.whisper.model.entities.User;
import com.techgn.whisper.model.interfaces.Command;
import com.techgn.whisper.repositories.UserRepository;
import com.techgn.whisper.shared.ErrorMessages;
import com.techgn.whisper.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.techgn.whisper.utils.Cryptography.encodePassword;
import static com.techgn.whisper.utils.StringUtils.stringHasValue;
import static com.techgn.whisper.utils.StringUtils.stringsAreDifferent;

@Service
public class UpdateUserCommandHandler implements Command<Result<Void>, UpdateUserCommand> {

    @Autowired
    private UserRepository userRepository;

    public Result<Void> handle(UpdateUserCommand command) {
        Result<Void> result = new Result<>();

        Result<String> resultValidate = validateUpdateUser(command.getId(), command.getNewName(), command.getNewEmail(),
                command.getNewPassword(), command.getNewPicture());
        if (resultValidate.isInvalid()) return result.setFromAnother(resultValidate);

        Result<User> resultFindUser = findById(command.getId());
        if (resultFindUser.isInvalid()) return result.setFromAnother(resultFindUser);

        User user = resultFindUser.getValue();

        if (stringHasValue.apply(command.getNewName()) && stringsAreDifferent.apply(user.getName(), command.getNewName()))
            user.setName(command.getNewName());
        if (stringHasValue.apply(command.getNewEmail()) && stringsAreDifferent.apply(user.getEmail(), command.getNewEmail()))
            user.setEmail(command.getNewEmail());
        // Encoded passwords are always different
        if (stringHasValue.apply(command.getNewPassword()))
            user.setPassword(encodePassword.apply(command.getNewPassword()));
        if (stringHasValue.apply(command.getNewPicture()) && stringsAreDifferent.apply(user.getPicture(), command.getNewPicture()))
            user.setPassword(command.getNewPicture());

        userRepository.save(user);

        return result;
    }

    public Result<User> findById(int id) {
        Result<User> result = new Result<>();

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return result.setBusinessMessage(String.format("User(%s) not found.", id));

        result.setValue(user.get());
        return result;
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

    public Result<Optional<User>> findByEmail(String email) {
        return new Result<>(userRepository.findByEmail(email));
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

    private Result<String> validateUpdateUser(int id, String newName, String newEmail, String newPassword, String newPicture) {
        Result<String> result = new Result<>();

        if (id <= 0) result.addValidation(ErrorMessages.ID_MUST_BE_GREATER_THAN_ZERO);
        if (stringHasValue.apply(newName)) validateName(result, newName);
        if (stringHasValue.apply(newEmail)) validateEmail(result, newEmail);
        if (stringHasValue.apply(newPassword)) validatePassword(result, newPassword);
        if (stringHasValue.apply(newPicture)) validatePicture(result, newPicture);

        return result;
    }
}
