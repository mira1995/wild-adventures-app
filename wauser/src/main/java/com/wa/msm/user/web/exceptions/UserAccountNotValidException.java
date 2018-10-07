package com.wa.msm.user.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UserAccountNotValidException extends RuntimeException {
    public UserAccountNotValidException(String message) {
        super(message);
    }
}
