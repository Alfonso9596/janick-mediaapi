package com.janickmediadb.janickmediaapi.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;
}
