package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.auth.*;
import com.janick_mediadb.janick_mediaapi.input.admin.UserInput;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginModel loginModel) {
        return authService.login(loginModel);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserInput userInput) {
        return authService.register(userInput);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.logout(principal);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }
}
