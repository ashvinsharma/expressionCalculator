package com.ashvin.calculator.simple;

import com.ashvin.calculator.Calculator;
import com.ashvin.calculator.exception.EvalException;
import com.ashvin.calculator.exception.ExpressionParseException;
import com.ashvin.calculator.exception.IllegalTokenException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleCalculatorTest {
    private Calculator calculator;

    @BeforeAll
    void init() {
        calculator = new SimpleCalculator();
    }

    @Test
    @DisplayName("Simple number input")
    void simpleNumberInput() throws ExpressionParseException, EvalException {
        final var expected = new BigDecimal(42);
        final var str = "42";
        final var got = calculator.calculate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Simple negative number input")
    void simpleNegativeNumberInput() throws ExpressionParseException, EvalException {
        final var expected = new BigDecimal(-42);
        final var str = "-42";
        final var got = calculator.calculate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Simple decimal number input")
    void simpleDecimalNumberInput() throws ExpressionParseException, EvalException {
        final var expected = new BigDecimal("42.0");
        final var str = "42.0";
        final var got = calculator.calculate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Simple negative decimal number input")
    void simpleNegativeDecimalNumberInput() throws ExpressionParseException, EvalException {
        final var expected = new BigDecimal("-42.0");
        final var str = "-42.0";
        final var got = calculator.calculate(str);

        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Eval good expression")
    void evalGoodExpression() throws ExpressionParseException, EvalException {
        final var expected = new BigDecimal(-1);
        final var str = "+3 - 4";

        final var got = calculator.calculate(str);
        Assertions.assertEquals(expected, got);
    }

    @Test
    @DisplayName("Invalid token")
    void invalidToken() {
        final var str = "+3 - 4a";
        Assertions.assertThrows(IllegalTokenException.class, () -> calculator.calculate(str));
    }

    @Test
    @DisplayName("Invalid operator")
    void invalidOperator() {
        final var str = "+3 - 4*";
        Assertions.assertThrows(ExpressionParseException.class, () -> calculator.calculate(str));
    }

    @Test
    @DisplayName("Divide by zero")
    void divideByZero() {
        final var str = "-3 / 0";
        Assertions.assertThrows(EvalException.class, () -> calculator.calculate(str));
    }
}
