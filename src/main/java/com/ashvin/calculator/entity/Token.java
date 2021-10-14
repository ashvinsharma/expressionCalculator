package com.ashvin.calculator.entity;

import java.math.BigDecimal;

public interface Token {
    BigDecimal eval(BigDecimal... bd);
}
