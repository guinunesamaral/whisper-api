package com.techgn.whisper.commands.users;

import com.techgn.whisper.model.entities.User;
import com.techgn.whisper.model.interfaces.Command;
import com.techgn.whisper.queries.UserQueries;
import com.techgn.whisper.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginCommandHandler implements Command<Result<Void>, LoginCommand> {

    @Autowired
    private UserQueries userQueries;

    public Result<Void> handle(LoginCommand command) {
        Result<Void> result = new Result<>();

        Result<User> resultFindByEmailAndPassword = userQueries.findByEmailAndPassword(command.email(), command.password());
        if (resultFindByEmailAndPassword.isInvalid()) return result.setFromAnother(resultFindByEmailAndPassword);

        return result;
    }
}
