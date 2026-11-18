package com.br.unifor.maquina_turing.scanner;

public enum TokenType {
    // Tokens originais do scanner do professor
    ID,
    NUM,
    ASSIGN,
    PLUS,
    MINUS,
    MULTIPLE,
    DIVIDE,
    LPAREN,
    RPAREN,

    // Tokens estendidos para Linguagens Formais e Gramáticas (GLC)
    ARROW,   // ->
    PIPE,    // |
    LAMBDA,  // ε, & ou lambda (cadeia vazia)
    SYMBOL,  // Símbolo terminal ou caractere do alfabeto

    EOF
}