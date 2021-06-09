package com.ashvin.calculator.entity;

import java.util.Objects;

public class AbstractSyntaxTree {
    private final Lexeme lexeme;
    private final AbstractSyntaxTree left;
    private final AbstractSyntaxTree right;

    public AbstractSyntaxTree(Lexeme lexeme) {
        this(lexeme, null, null);
    }

    public AbstractSyntaxTree(Lexeme lexeme, AbstractSyntaxTree left, AbstractSyntaxTree right) {
        this.lexeme = lexeme;
        this.left = left;
        this.right = right;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    public AbstractSyntaxTree getLeft() {
        return left;
    }

    public AbstractSyntaxTree getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractSyntaxTree that)) return false;

        if (!lexeme.equals(that.lexeme)) return false;
        if (!Objects.equals(left, that.left)) return false;
        return Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        int result = lexeme.hashCode();
        result = 31 * result + (left != null ? left.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
