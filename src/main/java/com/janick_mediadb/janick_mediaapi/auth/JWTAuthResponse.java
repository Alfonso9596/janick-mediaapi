package com.janick_mediadb.janick_mediaapi.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
    private int id;
    private String username;
    private List<String> roles;
}
