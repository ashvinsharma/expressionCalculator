package com.ashvin.calculator.simple;

import com.ashvin.calculator.Parser;
import com.ashvin.calculator.entity.AbstractSyntaxTree;
import com.ashvin.calculator.entity.Lexeme;
import com.ashvin.calculator.entity.TokenType;
import com.ashvin.calculator.exception.ExpressionParseException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleParser implements Parser {
    @Override
    public AbstractSyntaxTree parse(List<Lexeme> lexemes) throws ExpressionParseException {
        final var tokens = lexemes
                .stream()
                .map(TokenWrapper::new)
                .collect(Collectors.toList());

        parseTokens(tokens);
        if (tokens.size() != 1) throw new ExpressionParseException("Failed to parse all or some parts of expression");
        final var wrapper = tokens.get(0);

        return wrapper.getNode();
    }

    private List<TokenWrapper> parseTokens(List<TokenWrapper> tokens) {
        processParens(tokens);

        processUnaryOperators(tokens, EnumSet.allOf(TokenType.class)
                .stream()
                .filter(x -> x != TokenType.NUMBER)
                .filter(x -> x.getTokenClass().getNumOperands() == 1)
                .collect(Collectors.toMap(x -> x.getTokenClass().getSymbol(), Function.identity())));

//        filter binary operators then sort then group them by priority in a set
//        20 -> [ASTERISK, SLASH]
//        10 -> [PLUS, MINUS]
//        then process
        EnumSet.allOf(TokenType.class)
                .stream()
                .filter(x -> x.getTokenClass().getNumOperands() == 2)
                .sorted((o1, o2) -> o2.compare(o1))
                .collect(
                        Collectors.groupingBy((x -> x.getTokenClass().getPriority()),
                                LinkedHashMap::new,
                                Collectors.toSet()))
                .forEach((key, val) -> processBinaryOperators(tokens, val));

        return tokens;
    }

    // recursively solves inner parenthesised expression first
    // 1 + 9 * 8 + ( 8 * 9 + ( 8 / 1 ))
    // 8 * 9 + ( 8 / 1 )
    // 8 / 1
    private void processParens(List<TokenWrapper> tokens) {
        final var tmp = new ArrayList<TokenWrapper>(tokens.size() * 2);
        for (int i = 0; i < tokens.size(); i++) {
            final var token = tokens.get(i);
            if (!token.isLexeme()) {
                tmp.add(token);
                continue;
            }
            if (token.getLexeme().getType().equals(TokenType.OPEN_PARENS)) {
                final var idx = findMatchingParen(tokens, i);
                tmp.addAll(parseTokens(new ArrayList<>(tokens.subList(i + 1, idx))));
                i = idx;
            } else {
                tmp.add(token);
            }
        }
        tokens.clear();
        tokens.addAll(tmp);
    }

    /**
     * Returns idx of matching parens
     *
     * @param tokens - list of {@link TokenWrapper}
     * @param idx - index of the matching closing parenthesis
     */
    private int findMatchingParen(List<TokenWrapper> tokens, int idx) {
        if (!tokens.get(idx).isLexeme())
            throw new IllegalArgumentException("Token at idx %s is not Lexeme".formatted(idx));
        if (tokens.get(idx).getLexeme().getType() != TokenType.OPEN_PARENS)
            throw new IllegalArgumentException("Token at idx %s is not open parenthesis".formatted(idx));

        var balance = 0;
        for (int i = idx + 1; i < tokens.size(); i++) {
            final var token = tokens.get(i);
            final var lexeme = token.getLexeme();
            if (lexeme.getType().equals(TokenType.OPEN_PARENS)) {
                balance++;
            } else if (lexeme.getType().equals(TokenType.CLOSE_PARENS)) {
                if (balance == 0) return i;
                else balance--;
            }
        }
        throw new IllegalArgumentException("No matching parenthesis found");
    }

    private void processBinaryOperators(List<TokenWrapper> tokens, Set<TokenType> types)
            throws ExpressionParseException {
        final var result = new ArrayList<TokenWrapper>();
        AbstractSyntaxTree node = null;

        for (int i = 0; i < tokens.size(); i++) {
            final var wrapper = tokens.get(i);
            // its a node, don't touch it
            if (!wrapper.isLexeme()) {
                result.add(wrapper);
                continue;
            }

            final var lexeme = wrapper.getLexeme();
            // add it straightaway if not in types
            if (!types.contains(lexeme.getType())) {
                if (node != null) {
                    result.add(new TokenWrapper(node));
                    node = null;
                }

                result.add(wrapper);
                continue;
            }

            if (node != null) {
                var next = tokens.get(i + 1);
                node = new AbstractSyntaxTree(lexeme, node, next.getNode());
                i++;
                continue;
            }

            try {
                // this token is a binary operator which means we need to get
                // previous and next tokens to create AST
                result.remove(result.size() - 1);
                final var prev = tokens.get(i - 1);

                if (prev.isLexeme()) {
                    final var prevLexeme = prev.getLexeme();
                    if (prevLexeme.getType() != TokenType.NUMBER) {
                        throw new ExpressionParseException("Expected a number");
                    }
                }

                final var next = tokens.get(i + 1);
                if (next.isLexeme()) {
                    final var nextLexeme = next.getLexeme();
                    if (nextLexeme.getType() != TokenType.NUMBER) {
                        throw new ExpressionParseException("Expected a number");
                    }
                }

                node = new AbstractSyntaxTree(lexeme, prev.getNode(), next.getNode());
            } catch (IndexOutOfBoundsException e) {
                throw new ExpressionParseException("Operator without operand");
            }

            i++;
        }

        // add any residual node
        if (node != null) {
            result.add(new TokenWrapper(node));
        }

        tokens.clear();
        tokens.addAll(result);
    }

    private void processUnaryOperators(List<TokenWrapper> tokens, Map<Character, TokenType> types)
            throws ExpressionParseException {
        final var result = new ArrayList<TokenWrapper>();

        for (int i = 0; i < tokens.size(); i++) {
            final var current = tokens.get(i);
            // dont touch AST
            if (!current.isLexeme()) {
                result.add(current);
                continue;
            }

            final var lexeme = current.getLexeme();
            if (!types.containsKey(lexeme.getType().getTokenClass().getSymbol())) {
                result.add(current);
                continue;
            }

            if (i > 0) {
                var prev = tokens.get(i - 1);
                if (prev.isLexeme()) {
                    final var prevLexeme = prev.getLexeme();
                    // if previous element is a number then its a binary operator
                    if (prevLexeme.getType() == TokenType.NUMBER) {
                        result.add(current);
                        continue;
                    }
                } else {
                    result.add(current);
                    continue;
                }
            }

            // check if next token is number
            try {
                TokenWrapper next = tokens.get(i + 1);
                if (next.isLexeme()) {
                    final var nextLexeme = next.getLexeme();
                    if (nextLexeme.getType() != TokenType.NUMBER)
                        throw new ExpressionParseException("Expected a number");
                }

                final var newLexeme = new Lexeme(types.get(lexeme.getType().getTokenClass().getSymbol()));
                var node = new AbstractSyntaxTree(newLexeme, next.getNode(), null);
                result.add(new TokenWrapper(node));
            } catch (IndexOutOfBoundsException e) {
                throw new ExpressionParseException("Operand not found");
            }

            i++;
        }
        tokens.clear();
        tokens.addAll(result);
    }

    private static class TokenWrapper {
        private AbstractSyntaxTree node;
        private Lexeme lexeme;

        public TokenWrapper(AbstractSyntaxTree node) {
            this.node = node;
        }

        public TokenWrapper(Lexeme lexeme) {
            this.lexeme = lexeme;
        }

        public boolean isLexeme() {
            return node == null;
        }

        public AbstractSyntaxTree getNode() {
            return node != null ? node : new AbstractSyntaxTree(lexeme);
        }

        public Lexeme getLexeme() {
            return lexeme;
        }
    }
}
