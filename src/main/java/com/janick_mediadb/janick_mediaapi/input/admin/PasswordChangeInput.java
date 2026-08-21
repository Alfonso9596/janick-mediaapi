package com.janick_mediadb.janick_mediaapi.input.admin;

import lombok.Data;

@Data
public class PasswordChangeInput {
    private String currentPassword;
    private String newPassword;
}
