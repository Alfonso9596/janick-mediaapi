package com.janick_mediadb.janick_mediaapi.auth;

import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<JWTAuthResponse> login(RegisterLoginModel loginModel);

    String register(RegisterLoginModel registerModel);

    ResponseEntity<String> logout(UserDetailsImpl principal);

    ResponseEntity<?> refreshToken(TokenRefreshRequest request);
}
