package com.janickmediadb.janickmediaapi.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.exception.ForbiddenException;
import com.janickmediadb.janickmediaapi.exception.InternalServerException;
import com.janickmediadb.janickmediaapi.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                NOT_FOUND.value(),
                NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );
        return new ResponseEntity<>(errorMessage, NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                exception.getMessage()
        );
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ErrorMessage> handleForbiddenException(ForbiddenException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                FORBIDDEN.value(),
                FORBIDDEN.getReasonPhrase(),
                exception.getMessage()
        );
        return new ResponseEntity<>(errorMessage, FORBIDDEN);
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleInternalServerException(InternalServerException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getMessage()
        );
        return new ResponseEntity<>(errorMessage, INTERNAL_SERVER_ERROR);
    }
}
