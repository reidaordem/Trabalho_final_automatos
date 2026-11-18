package com.br.unifor.maquina_turing.gramaticas;

public class Producao {
    private final String variavel;
    private final String corpo; // "" representa ε

    public Producao(String variavel, String corpo) {
        this.variavel = variavel;
        this.corpo = corpo;
    }

    public String getVariavel() { return variavel; }
    public String getCorpo() { return corpo; }

    public boolean isEpsilon() {
        return corpo == null || corpo.isEmpty();
    }

    @Override
    public String toString() {
        return variavel + " -> " + (isEpsilon() ? "ε" : corpo);
    }
}