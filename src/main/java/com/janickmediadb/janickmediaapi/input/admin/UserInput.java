package com.janickmediadb.janickmediaapi.input.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class UserInput {
    private String username;
    private String password;
    private List<String> roles = List.of("USER");
    @JsonProperty("isEnabled")
    private boolean enabled = true;
}
