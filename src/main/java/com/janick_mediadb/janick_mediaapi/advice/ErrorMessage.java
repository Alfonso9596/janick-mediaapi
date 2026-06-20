package com.janick_mediadb.janick_mediaapi.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private int status;
    private String error;
    private String message;
}
