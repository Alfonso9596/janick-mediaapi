package com.janickmediadb.janickmediaapi.controller;

import com.janickmediadb.janickmediaapi.auth.AuthService;
import com.janickmediadb.janickmediaapi.auth.JwtAuthResponse;
import com.janickmediadb.janickmediaapi.auth.LoginModel;
import com.janickmediadb.janickmediaapi.auth.TokenRefreshRequest;
import com.janickmediadb.janickmediaapi.auth.UserDetailsImpl;
import com.janickmediadb.janickmediaapi.input.admin.PasswordChangeInput;
import com.janickmediadb.janickmediaapi.input.admin.UserInput;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginModel loginModel) {
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

    @PutMapping("/changepassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeInput passwordChangeInput) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.updatePassword(principal, passwordChangeInput);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }
}
