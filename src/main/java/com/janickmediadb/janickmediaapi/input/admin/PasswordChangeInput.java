package com.janickmediadb.janickmediaapi.input.admin;

import lombok.Data;

@Data
public class PasswordChangeInput {
    private String currentPassword;
    private String newPassword;
}
