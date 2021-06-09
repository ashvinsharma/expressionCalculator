package com.ashvin.calculator.entity;

import java.util.Objects;

public class Lexeme {

    private final TokenType type;
    private final String value;

    public Lexeme(TokenType type) {
        this(type, null);
    }

    public Lexeme(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lexeme that)) return false;

        if (type != that.type) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lexeme{type=%s, value='%s'}".formatted(type, value);
    }
}

