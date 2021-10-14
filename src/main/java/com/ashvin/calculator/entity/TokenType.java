package com.ashvin.calculator.entity;

import com.ashvin.calculator.entity.TokenClass.Asterisk;
import com.ashvin.calculator.entity.TokenClass.Caret;
import com.ashvin.calculator.entity.TokenClass.Minus;
import com.ashvin.calculator.entity.TokenClass.Number;
import com.ashvin.calculator.entity.TokenClass.Plus;
import com.ashvin.calculator.entity.TokenClass.Slash;

import java.math.BigDecimal;

public enum TokenType {
    NUMBER(new Number(Integer.MAX_VALUE, 1, 'x')),
    PLUS(new Plus(10, 2, '+')),
    MINUS(new Minus(10, 2, '-')),
    ASTERISK(new Asterisk(20, 2, '*')),
    SLASH(new Slash(20, 2, '/')),
    CARET(new Caret(30, 2, '^'));

    public TokenClass tokenClass;

    TokenType(TokenClass tokenClass) {
        this.tokenClass = tokenClass;
    }

    BigDecimal eval(BigDecimal... operands) {
        return tokenClass.eval(operands);
    }
}

// 1 + 9 * 8 + ( 8 * 9 + ( 8 / 1 ))
// 8 * 9 + ( 8 / 1 )
// 8 / 1
