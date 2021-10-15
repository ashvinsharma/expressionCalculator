package com.ashvin.calculator.exception;

public class EvalException extends Exception{
    public EvalException(String s) {
        super("Failed to eval: %s".formatted(s));
    }

    public EvalException(Throwable e) {
        this(e.getMessage());
    }
}
