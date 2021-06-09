package com.ashvin.calculator;

import com.ashvin.calculator.entity.AbstractSyntaxTree;
import com.ashvin.calculator.exception.EvalException;

import java.math.BigDecimal;

public interface Evaluator {
    BigDecimal evaluate(AbstractSyntaxTree root) throws EvalException;
}
