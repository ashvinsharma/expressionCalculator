package com.ashvin.calculator.simple;

import com.ashvin.calculator.*;
import com.ashvin.calculator.entity.Lexeme;
import com.ashvin.calculator.entity.TokenType;
import com.ashvin.calculator.exception.IllegalTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ashvin.calculator.entity.TokenType.*;

class LexerTest {
    final static Lexer lexer = new SimpleLexer();

    private void tokenizeTest(final String str, final List<Lexeme> expect) {
        final var got = lexer.tokenize(str);
        Assertions.assertEquals(expect, got);
    }

    @Test
    void goodAdd() {
        final var str = "3+ 5";
        final var expect = List.of(
                new Lexeme(NUMBER, "3"),
                new Lexeme(PLUS),
                new Lexeme(NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void goodMinus() {
        final var str = "3-5 ";
        final var expect = List.of(
                new Lexeme(NUMBER, "3"),
                new Lexeme(MINUS),
                new Lexeme(NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void unaryMinus() {
        final var str = "-3";
        final var expect = List.of(
                new Lexeme(MINUS, null),
                new Lexeme(NUMBER, "3")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void unaryPlus() {
        final var str = "+3";
        final var expect = List.of(
                new Lexeme(PLUS, null),
                new Lexeme(NUMBER, "3")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void mathSin() {
        final var str = "s3";
        final var expect = List.of(
                new Lexeme(SIN),
                new Lexeme(NUMBER, "3")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void goodMultiply() {
        final var str = " 3*5";
        final var expect = List.of(
                new Lexeme(NUMBER, "3"),
                new Lexeme(ASTERISK),
                new Lexeme(NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void goodDivision() {
        final var str = "3 /5";
        final var expect = List.of(
                new Lexeme(NUMBER, "3"),
                new Lexeme(SLASH),
                new Lexeme(NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void badCharacter() {
        final var str = "3a + 5a";
        Assertions.assertThrows(IllegalTokenException.class, () -> lexer.tokenize(str));
    }

    @Test
    void multipleDecimal() {
        final var str = "3.23.4";
        Assertions.assertThrows(IllegalTokenException.class, () -> lexer.tokenize(str));
    }

    @Test
    void goodCaret() {
        final var str = "3 ^ 2";
        final var expected = List.of(
                new Lexeme(NUMBER, "3"),
                new Lexeme(CARET),
                new Lexeme(NUMBER, "2")
        );
        tokenizeTest(str, expected);
    }

    @Test
    void goodParens() {
        final var str = "(3 ^ 2)";
        final var expected = List.of(
                new Lexeme(OPEN_PARENS),
                new Lexeme(NUMBER, "3"),
                new Lexeme(CARET),
                new Lexeme(NUMBER, "2"),
                new Lexeme(CLOSE_PARENS)
        );
        tokenizeTest(str, expected);
    }
}
