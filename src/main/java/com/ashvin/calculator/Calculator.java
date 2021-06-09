package com.ashvin.calculator;

import com.ashvin.calculator.exception.EvalException;
import com.ashvin.calculator.exception.ExpressionParseException;

import java.math.BigDecimal;

public interface Calculator {
    void compile(String expr) throws ExpressionParseException;

    BigDecimal calculate() throws EvalException;

    BigDecimal calculate(String expr) throws EvalException, ExpressionParseException;
}
