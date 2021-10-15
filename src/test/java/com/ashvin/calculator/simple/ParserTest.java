package com.ashvin.calculator.simple;

import com.ashvin.calculator.Lexer;
import com.ashvin.calculator.Parser;
import com.ashvin.calculator.entity.AbstractSyntaxTree;
import com.ashvin.calculator.entity.Lexeme;
import com.ashvin.calculator.entity.TokenType;
import com.ashvin.calculator.exception.ExpressionParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParserTest {
    private Parser parser;
    private Lexer lexer;

    @BeforeAll
    void init() {
        parser = new SimpleParser();
        lexer = new SimpleLexer();
    }

    private void parse(AbstractSyntaxTree expected, String str) {
        final var lexemes = lexer.tokenize(str);
        final var got = parser.parse(lexemes);
        Assertions.assertEquals(expected, got);
    }

    private void expectThrow(String str, Class<? extends Throwable> e) {
        final var lexemes = lexer.tokenize(str);
        Assertions.assertThrows(e, () -> parser.parse(lexemes));
    }

    @Test
    @DisplayName("Number after number")
    void numberAfterNumber() {
        final var str = "5 5";
        expectThrow(str, ExpressionParseException.class);
    }

    @Test
    @DisplayName("parsePlusBeforeNumber")
    void parsePlusBeforeNumber() throws ExpressionParseException {
        final var expect = new AbstractSyntaxTree(new Lexeme(TokenType.U_PLUS),
                new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "3"))
                , null);
        final var str = "+3";
        final var lexemes = lexer.tokenize(str);
        final var got = parser.parse(lexemes);

        Assertions.assertEquals(expect, got);
    }

    @Test
    @DisplayName("Simple parenthesised expression")
    void simpleParenthesisedExp() throws ExpressionParseException {
        final var expect = new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "5"));
        final var str = "(5)";
        final var lexemes = lexer.tokenize(str);
        final var got = parser.parse(lexemes);

        Assertions.assertEquals(expect, got);
    }

    @Test
    @DisplayName("simple sin test")
    void simpleSinTest() {
        final var str = "s3";
        final var expected = new AbstractSyntaxTree(new Lexeme(TokenType.SIN),
                new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "3")),
                null);

        parse(expected, str);
    }

    @Test
    @DisplayName("Parse simple unary expression")
    void parseSimpleUnaryExpression() throws ExpressionParseException {
        final var expect = new AbstractSyntaxTree(new Lexeme(TokenType.U_MINUS),
                new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "3"))
                , null);
        final var str = "-3";
        final var lexemes = lexer.tokenize(str);
        final var got = parser.parse(lexemes);

        Assertions.assertEquals(expect, got);
    }

    @Test
    @DisplayName("Parse bad expression")
    void parseBadExpression() {
        final var str = "-3-";
        expectThrow(str, ExpressionParseException.class);
    }

    @Test
    @DisplayName("Unary expression with no operand")
    void unaryExpressionEndsWithOperator() {
        final var str = "-";
        expectThrow(str, ExpressionParseException.class);
    }

    @Test
    @DisplayName("Parse bad complex expression")
    void parseBadComplexExpression() {
        final var str = "-3 +5 ** 8 / -2";
        expectThrow(str, ExpressionParseException.class);
    }

    @Test
    @DisplayName("Parse complex expression")
    void parseComplexExpression() throws ExpressionParseException {
        final var expected = new AbstractSyntaxTree(new Lexeme(TokenType.PLUS),
                new AbstractSyntaxTree(new Lexeme(TokenType.U_MINUS),
                        new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "3")),
                        null
                ), new AbstractSyntaxTree(new Lexeme(TokenType.SLASH),
                new AbstractSyntaxTree(new Lexeme(TokenType.ASTERISK),
                        new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "5")),
                        new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "8"))
                ),
                new AbstractSyntaxTree(new Lexeme(TokenType.U_MINUS),
                        new AbstractSyntaxTree(new Lexeme(TokenType.NUMBER, "2")),
                        null
                )
        ));
        final var str = "-3 +5 * 8 / -2";
        parse(expected, str);
    }

    @Test
    @DisplayName("Expression ends with operator")
    void expressionEndsWithOperator() {
        final var str = "-3 +5 * 8 / -2 -";
        expectThrow(str, IndexOutOfBoundsException.class);
    }
}
