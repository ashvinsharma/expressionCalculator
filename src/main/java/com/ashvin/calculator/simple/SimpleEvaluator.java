package com.ashvin.calculator.simple;

import com.ashvin.calculator.*;
import com.ashvin.calculator.entity.AbstractSyntaxTree;
import com.ashvin.calculator.entity.Lexeme;
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

            // noinspection ConstantConditions
            return evaluate(left, lexeme, right);
        } catch (NumberFormatException e) {
            throw new EvalException("Invalid number specified");
        } catch (EvalException e) {
            throw e;
        } catch (Exception e) {
            throw new EvalException("Evaluation of expression failed");
        }
    }

    private BigDecimal evaluate(BigDecimal left, Lexeme lexeme, BigDecimal right) {
        // if left is null, right is guaranteed to be null
        // so only check for right
        return lexeme.eval(left, right);
    }
}
