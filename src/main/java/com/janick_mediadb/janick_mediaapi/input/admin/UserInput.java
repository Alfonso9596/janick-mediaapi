package com.janick_mediadb.janick_mediaapi.input.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserInput {
    private String username;
    private String password;
    private List<String> roles = List.of("USER");
    @JsonProperty("isEnabled")
    private boolean enabled = true;
}
