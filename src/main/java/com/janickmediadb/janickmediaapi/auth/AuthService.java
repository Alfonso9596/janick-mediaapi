package com.janickmediadb.janickmediaapi.auth;

import com.janickmediadb.janickmediaapi.input.admin.PasswordChangeInput;
import com.janickmediadb.janickmediaapi.input.admin.UserInput;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<JwtAuthResponse> login(LoginModel loginModel);

    ResponseEntity<String> register(UserInput userInput);

    ResponseEntity<String> logout(UserDetailsImpl principal);

    ResponseEntity<String> updatePassword(UserDetailsImpl principal, PasswordChangeInput passwordChangeInput);

    ResponseEntity<?> refreshToken(TokenRefreshRequest request);
}
