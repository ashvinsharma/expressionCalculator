package com.ashvin.calculator.simple;

import com.ashvin.calculator.*;
import com.ashvin.calculator.entity.AbstractSyntaxTree;
import com.ashvin.calculator.exception.EvalException;
import com.ashvin.calculator.exception.ExpressionParseException;

import java.math.BigDecimal;

public class SimpleCalculator implements Calculator {
    private final Lexer lexer;
    private final Parser parser;
    private final Evaluator evaluator;
    private AbstractSyntaxTree syntaxTree;

    public SimpleCalculator() {
        lexer = new SimpleLexer();
        parser = new SimpleParser();
        evaluator = new SimpleEvaluator();
    }

    @Override
    public void compile(String expr) throws ExpressionParseException {
        final var lexemes = lexer.tokenize(expr); // store it in some place for evaluator's input;
        syntaxTree = parser.parse(lexemes);
    }

    @Override
    public BigDecimal calculate() throws EvalException {
        return evaluator.evaluate(syntaxTree);
    }

    @Override
    public BigDecimal calculate(String expr) throws EvalException, ExpressionParseException {
        compile(expr);
        return calculate();
    }
}
