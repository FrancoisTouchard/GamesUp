package com.gamesUP.gamesUP.exception;

import org.springframework.dao.DataIntegrityViolationException;

import java.io.Serial;

public class ResourceInUseException extends DataIntegrityViolationException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceInUseException(String message) { super(message); }
}
