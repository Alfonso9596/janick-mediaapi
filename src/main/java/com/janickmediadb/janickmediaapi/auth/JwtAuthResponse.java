package com.janickmediadb.janickmediaapi.auth;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private int id;
    private String username;
    private List<String> roles;

    public JwtAuthResponse(String accessToken, String refreshToken, int id, String username, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
