package com.ashvin.calculator.simple;

import com.ashvin.calculator.Evaluator;
import com.ashvin.calculator.Lexer;
import com.ashvin.calculator.Parser;
import com.ashvin.calculator.exception.EvalException;
import com.ashvin.calculator.exception.ExpressionParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.math.RoundingMode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EvaluatorTest {
    private Lexer lexer;
    private Parser parser;
    private Evaluator evaluator;
    private int scale;

    @BeforeAll
    void setUp() {
        scale = Integer.parseInt(System.getProperty("decimal.scale", "3"));
        lexer = new SimpleLexer();
        parser = new SimpleParser();
        evaluator = new SimpleEvaluator();
    }

    private BigDecimal evaluate(String str) throws ExpressionParseException, EvalException {
        final var lexemes = lexer.tokenize(str);
        final var tree = parser.parse(lexemes);
        return evaluator.evaluate(tree);
    }

    @Test
    @DisplayName("Simple eval")
    void simpleEval() throws ExpressionParseException, EvalException {
        final var expected = new BigDecimal(8);
        final var str = "3 + 5";
        final var got = evaluate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Complex eval")
    void complexEval() throws ExpressionParseException, EvalException {
        final var expected = new BigDecimal(-23).setScale(scale, RoundingMode.HALF_EVEN);
        final var str = "-3 +5 * 8 / -2";
        final var got = evaluate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Simple Parenthesised Expression")
    void simpleParenthesisedExpression() throws EvalException {
        final var str = "(5 + 2) / 7";
        final var expected = new BigDecimal(1).setScale(scale, RoundingMode.HALF_EVEN);
        final var got = evaluate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Complex Parenthesised Expression")
    void complexParenthesisedExpression() throws EvalException {
        final var str = "(5 + (2 * 2)) / (11 + (-2))";
        final var expected = new BigDecimal(1).setScale(scale, RoundingMode.HALF_EVEN);
        final var got = evaluate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Simple sin Expression")
    void simpleSinExpression() throws EvalException {
        final var str = "s(11/7)";
        final var expected = new BigDecimal(1).setScale(scale, RoundingMode.HALF_EVEN);

        final var got = evaluate(str);

        Assertions.assertEquals(expected, got);
    }
}
