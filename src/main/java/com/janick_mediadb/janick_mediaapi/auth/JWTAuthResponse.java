package com.janick_mediadb.janick_mediaapi.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JWTAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private int id;
    private String username;
    private List<String> roles;

    public JWTAuthResponse(String accessToken, String refreshToken, int id, String username, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
