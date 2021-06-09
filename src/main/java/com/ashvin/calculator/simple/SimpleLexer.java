package com.ashvin.calculator.simple;

import com.ashvin.calculator.Lexer;
import com.ashvin.calculator.entity.Lexeme;
import com.ashvin.calculator.entity.TokenType;
import com.ashvin.calculator.exception.IllegalTokenException;

import java.util.ArrayList;
import java.util.List;

public class SimpleLexer implements Lexer {

    @Override
    public List<Lexeme> tokenize(String str) {
        var currToken = new StringBuilder();
        var lexemes = new ArrayList<Lexeme>();
        for (int i = 0; i < str.length(); i++) {
            var curr = str.charAt(i);
            if (Character.isDigit(curr) || curr == '.') {
                currToken.append(curr);
                continue;
            }
            // we have encountered a symbol so time to put the currToken
            // inside the list and start afresh
            else if (currToken.length() > 0) {
                getLexeme(currToken, lexemes);
                currToken = new StringBuilder();
            }

            if (curr == ' ') continue;

            Lexeme lexeme = switch (curr) {
                case '+' -> new Lexeme(TokenType.PLUS);
                case '-' -> new Lexeme(TokenType.MINUS);
                case '*' -> new Lexeme(TokenType.ASTERISK);
                case '/' -> new Lexeme(TokenType.SLASH);
                default -> throw new IllegalTokenException(curr);
            };

            lexemes.add(lexeme);
        }

        // for the last token
        if (currToken.length() > 0) getLexeme(currToken, lexemes);

        return lexemes;
    }

    private void getLexeme(StringBuilder currToken, ArrayList<Lexeme> lexemes) {
        Lexeme lexeme;
        var token = currToken.toString();
        if (token.matches("[0-9]+|[0-9]+\\.[0-9]+")) {
            lexeme = new Lexeme(TokenType.NUMBER, token);
            lexemes.add(lexeme);
        } else {
            throw new IllegalTokenException();
        }
    }
}
