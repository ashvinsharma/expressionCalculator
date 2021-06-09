package com.ashvin.calculator;

import com.ashvin.calculator.entity.Lexeme;

import java.util.List;

public interface Lexer {
    List<Lexeme> tokenize(String expr);
}
