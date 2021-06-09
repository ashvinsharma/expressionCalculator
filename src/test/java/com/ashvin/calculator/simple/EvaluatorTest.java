package com.ashvin.calculator.simple;

import com.ashvin.calculator.*;
import com.ashvin.calculator.exception.EvalException;
import com.ashvin.calculator.exception.ExpressionParseException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EvaluatorTest {
    private Lexer lexer;
    private Parser parser;
    private Evaluator evaluator;

    @BeforeAll
    void setUp() {
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
        final var expected = new BigDecimal(-23);
        final var str = "-3 +5 * 8 / -2";
        final var got = evaluate(str);

        Assertions.assertEquals(expected, got);
    }
}
