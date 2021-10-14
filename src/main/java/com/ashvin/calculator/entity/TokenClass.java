package com.ashvin.calculator.entity;

import java.math.BigDecimal;

public abstract class TokenClass implements Token {
    public int priority;
    public int numOperands;
    public char symbol;

    public TokenClass(int priority, int numOperands, char symbol) {
        this.priority = priority;
        this.numOperands = numOperands;
        this.symbol = symbol;
    }
    public void validate(BigDecimal... bigDecimals) {
        if (bigDecimals.length != numOperands) {
            throw new IllegalArgumentException(String.format("Only %s arguments needed", numOperands));
        }
    }

    static class Number extends TokenClass implements Token {
        public Number(int priority, int numOperands, char symbol) {
            super(priority, numOperands, symbol);
        }

        @Override
        public BigDecimal eval(final BigDecimal... bigDecimals) {
            super.validate(bigDecimals);
            return bigDecimals[0];
        }
    }

    static class Plus extends TokenClass implements Token {
        public Plus(int priority, int numOperands, char symbol) {
            super(priority, numOperands, symbol);
        }

        @Override
        public BigDecimal eval(final BigDecimal... bigDecimals) {
            super.validate(bigDecimals);
            final var left = bigDecimals[0];
            final var right = bigDecimals[1];
            return right == null ? left : left.add(right);
        }
    }

    static class Minus extends TokenClass implements Token {
        public Minus(int priority, int numOperands, char symbol) {
            super(priority, numOperands, symbol);
        }

        @Override
        public BigDecimal eval(final BigDecimal... bigDecimals) {
            super.validate(bigDecimals);
            final var left = bigDecimals[0];
            final var right = bigDecimals[1];
            return right == null ? left.negate() : left.subtract(right);
        }
    }

    static class Asterisk extends TokenClass implements Token {
        public Asterisk(int priority, int numOperands, char symbol) {
            super(priority, numOperands, symbol);
        }

        @Override
        public BigDecimal eval(final BigDecimal... bigDecimals) {
            super.validate(bigDecimals);
            final var left = bigDecimals[0];
            final var right = bigDecimals[1];
            return right == null ? left : left.multiply(right);
        }
    }

    static class Slash extends TokenClass implements Token {
        public Slash(int priority, int numOperands, char symbol) {
            super(priority, numOperands, symbol);
        }

        @Override
        public BigDecimal eval(final BigDecimal... bigDecimals) {
            super.validate(bigDecimals);
            final var left = bigDecimals[0];
            final var right = bigDecimals[1];
            if (right == null || right.equals(BigDecimal.ZERO))
                throw new IllegalStateException("Unexpected value: " + right);
            //noinspection BigDecimalMethodWithoutRoundingCalled
            return left.divide(right);
        }
    }

    static class Caret extends TokenClass implements Token {
        public Caret(int priority, int numOperands, char symbol) {
            super(priority, numOperands, symbol);
        }

        @Override
        public BigDecimal eval(final BigDecimal... bigDecimals) {
            super.validate(bigDecimals);
            final var left = bigDecimals[0];
            final var right = bigDecimals[1];
            final var rightInt = Integer.parseInt(right.toString());
            return left.pow(rightInt);
        }
    }
}
