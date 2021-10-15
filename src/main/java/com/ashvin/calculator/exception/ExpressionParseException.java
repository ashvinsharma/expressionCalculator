package com.ashvin.calculator.exception;

public class ExpressionParseException extends RuntimeException {
    public ExpressionParseException(String s) {
        super("Failed to parse: %s".formatted(s));
    }
}
