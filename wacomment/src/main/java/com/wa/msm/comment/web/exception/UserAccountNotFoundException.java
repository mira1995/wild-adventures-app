package com.wa.msm.comment.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserAccountNotFoundException extends RuntimeException {
    UserAccountNotFoundException(String message) {
        super(message);
    }
}
