package com.techgn.whisper.commands.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserCommand {
    private int id;
    private String newName;
    private String newEmail;
    private String newPassword;
    private String newPicture;

    public UpdateUserCommand() {
    }
}
