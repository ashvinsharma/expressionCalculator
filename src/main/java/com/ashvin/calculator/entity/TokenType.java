package com.ashvin.calculator.entity;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum TokenType {
    NUMBER((left) -> left),
    PLUS((left, right) -> right == null ? left : left.add(right)),
    MINUS((left, right) -> right == null ? left.negate() : left.subtract(right)),
    ASTERISK((left, right) -> right == null ? left : left.multiply(right)),
    SLASH((left, right) -> {
        if (right == null || right.equals(BigDecimal.ZERO))
            throw new IllegalStateException("Unexpected value: " + right);
        //noinspection BigDecimalMethodWithoutRoundingCalled
        return left.divide(right);
    });

    private BiFunction<BigDecimal, BigDecimal, BigDecimal> biFunction;
    private Function<BigDecimal, BigDecimal> function;

    TokenType(BiFunction<BigDecimal, BigDecimal, BigDecimal> function) {
        this.biFunction = function;
    }

    TokenType(Function<BigDecimal, BigDecimal> f) {
        this.function = f;
    }

    BigDecimal eval(BigDecimal left, BigDecimal right) {
        return biFunction.apply(left, right);
    }

    BigDecimal eval(BigDecimal left) {
        return function.apply(left);
    }
}
