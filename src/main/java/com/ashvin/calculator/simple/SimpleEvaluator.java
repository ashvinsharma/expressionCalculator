package com.ashvin.calculator.simple;

import com.ashvin.calculator.Evaluator;
import com.ashvin.calculator.entity.AbstractSyntaxTree;
import com.ashvin.calculator.entity.TokenType;
import com.ashvin.calculator.exception.EvalException;

import java.math.BigDecimal;

public class SimpleEvaluator implements Evaluator {
    @Override
    public BigDecimal evaluate(AbstractSyntaxTree node) throws EvalException {
        try {
            final var lexeme = node.getLexeme();
            if (lexeme.getType() == TokenType.NUMBER) {
                return new BigDecimal(lexeme.getValue());
            }

            BigDecimal left = node.getLeft() != null ? evaluate(node.getLeft()) : null;
            BigDecimal right = node.getRight() != null ? evaluate(node.getRight()) : null;

            return lexeme.eval(left, right);
        } catch (Exception e) {
            throw new EvalException("Evaluation of expression failed");
        }
    }
}
