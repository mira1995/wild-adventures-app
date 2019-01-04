package com.wa.msm.comment.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CommentNotValidException extends RuntimeException {
    public CommentNotValidException(String message) {
        super(message);
    }
}
