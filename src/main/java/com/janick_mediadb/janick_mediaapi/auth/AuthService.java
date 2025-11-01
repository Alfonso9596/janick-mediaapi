package com.janick_mediadb.janick_mediaapi.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<JWTAuthResponse> login(RegisterLoginModel loginModel);

    String register(RegisterLoginModel registerModel);

    ResponseEntity<String> logout(Object principal);

    ResponseEntity<String> refreshToken(HttpServletRequest request);
}
