package com.wa.msm.adventure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class SessionNotValidException extends RuntimeException {
    public SessionNotValidException(String message) {
        super(message);
    }
}
