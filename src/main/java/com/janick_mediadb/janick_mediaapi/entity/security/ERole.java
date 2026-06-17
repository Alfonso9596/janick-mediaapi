package com.janick_mediadb.janick_mediaapi.entity.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ERole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
