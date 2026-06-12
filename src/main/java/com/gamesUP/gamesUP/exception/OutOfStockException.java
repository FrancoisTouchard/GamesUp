package com.gamesUP.gamesUP.exception;

import java.io.Serial;

public class OutOfStockException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public OutOfStockException(String message) { super(message); }
}
