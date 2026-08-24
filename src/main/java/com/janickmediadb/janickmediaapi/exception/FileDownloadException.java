package com.janickmediadb.janickmediaapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDownloadException extends Exception {

    private final String message;

    public FileDownloadException(String message) {
        this.message = message;
    }
}
