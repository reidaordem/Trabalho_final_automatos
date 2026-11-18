package com.br.unifor.maquina_turing.scanner;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public List<Token> tokenSize(String input) {
        List<Token> tokens = new ArrayList<>();

        int i = 0;
        while (i < input.length()) {
            char current = input.charAt(i);

            // Ignorar espaço
            if (Character.isWhitespace(current)) {
                i++;
                continue;
            }

            // Identificadores: letras seguidas de letras/dígitos
            if (Character.isLetter(current)) {
                int start = i;
                StringBuilder sb = new StringBuilder();

                while (i < input.length()) {
                    char c = input.charAt(i);
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        sb.append(c);
                        i++;
                    } else {
                        break;
                    }
                }

                tokens.add(new Token(TokenType.ID, sb.toString(), start));
                continue;
            }

            // Números inteiros
            if (Character.isDigit(current)) {
                int start = i;
                StringBuilder sb = new StringBuilder();

                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    sb.append(input.charAt(i));
                    i++;
                }
                tokens.add(new Token(TokenType.NUM, sb.toString(), start));
                continue;
            }

            // Símbolos
            switch (current) {
                case '=':
                    tokens.add(new Token(TokenType.ASSIGN, "=", i));
                    break;
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+", i));
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-", i));
                    break;
                case '*':
                    tokens.add(new Token(TokenType.MULTIPLE, "*", i));
                    break;
                case '/':
                    tokens.add(new Token(TokenType.DIVIDE, "/", i));
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "(", i));
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")", i));
                    break;
                default:
                    throw new IllegalArgumentException("Caractere inválido '" + current + "' na posição " + i);
            }
            i++;
        }
        tokens.add(new Token(TokenType.EOF, "", input.length()));
        return tokens;
    }
}