package com.janick_mediadb.janick_mediaapi.auth;

import com.janick_mediadb.janick_mediaapi.input.admin.PasswordChangeInput;
import com.janick_mediadb.janick_mediaapi.input.admin.UserInput;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<JWTAuthResponse> login(LoginModel loginModel);

    ResponseEntity<String> register(UserInput userInput);

    ResponseEntity<String> logout(UserDetailsImpl principal);

    ResponseEntity<String> updatePassword(UserDetailsImpl principal, PasswordChangeInput passwordChangeInput);

    ResponseEntity<?> refreshToken(TokenRefreshRequest request);
}
