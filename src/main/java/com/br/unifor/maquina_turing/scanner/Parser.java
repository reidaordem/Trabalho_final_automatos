package com.br.unifor.maquina_turing.scanner;

import java.util.List;
import java.util.Stack;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;
    private final Stack<Token> parenteseStack = new Stack<>();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        statement();
        if (!isAtEnd()) {
            Token token = peek();
            throw new ParseException("Token inesperado após fim da instrução: " + token);
        }

        if (!parenteseStack.isEmpty()) {
            Token token = parenteseStack.peek();
            throw new ParseException("Parêntese aberto sem fechamento na posição " + token.getPosition());
        }
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    // S -> ID = E
    private void statement() {
        consumer(TokenType.ID, "Era esperado um identificador no início da atribuição.");
        consumer(TokenType.ASSIGN, "Era esperado '=' após o identificador.");
        expression();
    }

    // E -> T ((+ | -) T)*
    private void expression() {
        term();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            term();
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return type == TokenType.EOF;
        }
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    // T -> F ((* | /) F)*
    private void term() {
        factor();
        while (match(TokenType.MULTIPLE, TokenType.DIVIDE)) {
            factor();
        }
    }

    // F -> (E) | ID | NUM
    private void factor() {
        if (match(TokenType.NUM)) {
            return;
        }
        if (match(TokenType.ID)) {
            return;
        }
        if (match(TokenType.LPAREN)) {
            Token open = previous();
            parenteseStack.push(open);

            expression();
            consumer(TokenType.RPAREN, "Era esperado ')' para fechar a expressão.");
            parenteseStack.pop();
            return;
        }
        throw new ParseException("Era esperado Número, Identificador ou '(' na posição " + peek().getPosition());
    }

    private Token consumer(TokenType tokenType, String message) {
        if (check(tokenType)) {
            return advance();
        }
        throw new ParseException(message + " Encontrado: " + peek());
    }
}