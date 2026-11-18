package com.br.unifor.maquina_turing.scanner;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class FormalLexer {

    /**
     * Lê uma definição de alfabeto como "{a, b}", "a, b" ou "ab"
     * e retorna o conjunto ordenado de símbolos únicos Σ.
     */
    public Set<Character> tokenizarAlfabeto(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new ParseException("O alfabeto Σ não pode ser vazio.");
        }

        String limpo = input.trim();

        // Remove chaves opcionais { ... }
        if (limpo.startsWith("{") || limpo.endsWith("}")) {
            if (!limpo.startsWith("{") || !limpo.endsWith("}")) {
                throw new ParseException("Chaves desbalanceadas na definição do alfabeto Σ.");
            }
            limpo = limpo.substring(1, limpo.length() - 1).trim();
        }

        if (limpo.isEmpty()) {
            throw new ParseException("O alfabeto Σ deve conter pelo menos um símbolo.");
        }

        Set<Character> sigma = new LinkedHashSet<>();

        // Caso o usuário separe por vírgula: ex. "a, b, 0, 1"
        if (limpo.contains(",")) {
            String[] partes = limpo.split(",");
            for (int i = 0; i < partes.length; i++) {
                String simbolo = partes[i].trim();
                if (simbolo.length() != 1) {
                    throw new ParseException("Símbolo inválido '" + simbolo + "' na posição " + i + " do alfabeto. Cada símbolo de Σ deve ter 1 caractere.");
                }
                sigma.add(simbolo.charAt(0));
            }
        } else {
            // Caso o usuário digite direto: ex. "ab" ou "01"
            for (int i = 0; i < limpo.length(); i++) {
                char c = limpo.charAt(i);
                if (!Character.isWhitespace(c)) {
                    sigma.add(c);
                }
            }
        }

        return sigma;
    }

    /**
     * Verifica caractere por caractere se w ∈ Σ*.
     * Lança ParseException informando exatamente qual símbolo falhou e sua posição.
     */
    public void validarCadeiaNoAlfabeto(String cadeia, Set<Character> sigma) {
        if (cadeia == null || cadeia.isEmpty()) {
            return; // Cadeia vazia (ε) pertence a Σ* para qualquer alfabeto Σ
        }

        for (int i = 0; i < cadeia.length(); i++) {
            char c = cadeia.charAt(i);
            if (!sigma.contains(c)) {
                throw new ParseException("Símbolo '" + c + "' na posição " + i + " não pertence ao alfabeto Σ = " + sigma);
            }
        }
    }

    /**
     * Tokeniza uma regra de produção GLC no formato "S -> aSb | lambda" ou "S -> aSb | ε".
     */
    public List<Token> tokenizarProducao(String regra) {
        if (regra == null || regra.trim().isEmpty()) {
            throw new ParseException("A regra de produção não pode ser vazia.");
        }

        List<Token> tokens = new ArrayList<>();
        int i = 0;

        while (i < regra.length()) {
            char current = regra.charAt(i);

            if (Character.isWhitespace(current)) {
                i++;
                continue;
            }

            // Seta de produção: ->
            if (current == '-' && i + 1 < regra.length() && regra.charAt(i + 1) == '>') {
                tokens.add(new Token(TokenType.ARROW, "->", i));
                i += 2;
                continue;
            }

            // Alternativa de produção: |
            if (current == '|') {
                tokens.add(new Token(TokenType.PIPE, "|", i));
                i++;
                continue;
            }

            // Representações de cadeia vazia: ε ou &
            if (current == 'ε' || current == '&') {
                tokens.add(new Token(TokenType.LAMBDA, String.valueOf(current), i));
                i++;
                continue;
            }

            // Palavra reservada "lambda"
            if (regra.startsWith("lambda", i)) {
                int fim = i + 6;
                boolean fimDePalavra = (fim == regra.length()) || !Character.isLetterOrDigit(regra.charAt(fim));
                if (fimDePalavra) {
                    tokens.add(new Token(TokenType.LAMBDA, "lambda", i));
                    i = fim;
                    continue;
                }
            }

            // Variável não-terminal (letra maiúscula)
            if (Character.isUpperCase(current)) {
                tokens.add(new Token(TokenType.ID, String.valueOf(current), i));
                i++;
                continue;
            }

            // Qualquer outro caractere válido é tratado como símbolo terminal
            tokens.add(new Token(TokenType.SYMBOL, String.valueOf(current), i));
            i++;
        }

        tokens.add(new Token(TokenType.EOF, "", regra.length()));
        return tokens;
    }
}