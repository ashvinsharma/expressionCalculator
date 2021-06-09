package com.ashvin.calculator.simple;

import com.ashvin.calculator.*;
import com.ashvin.calculator.entity.Lexeme;
import com.ashvin.calculator.entity.TokenType;
import com.ashvin.calculator.exception.IllegalTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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
                new Lexeme(TokenType.NUMBER, "3"),
                new Lexeme(TokenType.PLUS),
                new Lexeme(TokenType.NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void goodMinus() {
        final var str = "3-5 ";
        final var expect = List.of(
                new Lexeme(TokenType.NUMBER, "3"),
                new Lexeme(TokenType.MINUS),
                new Lexeme(TokenType.NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void goodMinus_before() {
        final var str = "-3";
        final var expect = List.of(
                new Lexeme(TokenType.MINUS, null),
                new Lexeme(TokenType.NUMBER, "3")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void plusBeforeNumber() {
        final var str = "+3";
        final var expect = List.of(
                new Lexeme(TokenType.PLUS, null),
                new Lexeme(TokenType.NUMBER, "3")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void goodMultiply() {
        final var str = " 3*5";
        final var expect = List.of(
                new Lexeme(TokenType.NUMBER, "3"),
                new Lexeme(TokenType.ASTERISK),
                new Lexeme(TokenType.NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void goodDivision() {
        final var str = "3 /5";
        final var expect = List.of(
                new Lexeme(TokenType.NUMBER, "3"),
                new Lexeme(TokenType.SLASH),
                new Lexeme(TokenType.NUMBER, "5")
        );

        tokenizeTest(str, expect);
    }

    @Test
    void badCharacter() {
        final var str = "3a + 5a";
        Assertions.assertThrows(IllegalTokenException.class, () -> lexer.tokenize(str));
    }
}
