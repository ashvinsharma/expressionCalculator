package com.ashvin.calculator;

import com.ashvin.calculator.entity.AbstractSyntaxTree;
import com.ashvin.calculator.entity.Lexeme;
import com.ashvin.calculator.exception.ExpressionParseException;

import java.util.List;

public interface Parser {
    AbstractSyntaxTree parse(List<Lexeme> lexemes) throws ExpressionParseException;
}
