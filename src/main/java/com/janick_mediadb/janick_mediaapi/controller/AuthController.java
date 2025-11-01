package com.janick_mediadb.janick_mediaapi.controller;

import com.janick_mediadb.janick_mediaapi.auth.AuthService;
import com.janick_mediadb.janick_mediaapi.auth.JWTAuthResponse;
import com.janick_mediadb.janick_mediaapi.auth.RegisterLoginModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody RegisterLoginModel loginModel) {
        return authService.login(loginModel);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterLoginModel registerModel) {
        String response = authService.register(registerModel);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.logout(principal);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }
}
