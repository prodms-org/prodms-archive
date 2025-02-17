package com.hydroyura.prodms.archive.server.exception.model;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ValidationException extends RuntimeException {

    private final Errors errors;

    public ValidationException(Errors errors, String msg) {
        super(msg);
        this.errors = errors;
    }

    public ValidationException(Errors errors, String msg, Throwable t) {
        super(msg, t);
        this.errors = errors;
    }

}