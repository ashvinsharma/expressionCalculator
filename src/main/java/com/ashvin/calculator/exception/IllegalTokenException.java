package com.ashvin.calculator.exception;

public class IllegalTokenException extends RuntimeException {
    public IllegalTokenException(char ch) {
        this(String.valueOf(ch));
    }
    public IllegalTokenException(String s) {
        super("Illegal token: %s".formatted(s));
    }

    public IllegalTokenException() {
        super("Illegal expression");
    }
}
