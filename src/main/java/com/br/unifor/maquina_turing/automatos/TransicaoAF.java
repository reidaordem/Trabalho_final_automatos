package com.br.unifor.maquina_turing.automatos;

public class TransicaoAF {
    private final String estadoOrigem;
    private final Character simbolo; // null representa transição ε (vazia)
    private final String estadoDestino;

    public TransicaoAF(String estadoOrigem, Character simbolo, String estadoDestino) {
        this.estadoOrigem = estadoOrigem;
        this.simbolo = simbolo;
        this.estadoDestino = estadoDestino;
    }

    public String getEstadoOrigem() { return estadoOrigem; }
    public Character getSimbolo() { return simbolo; }
    public String getEstadoDestino() { return estadoDestino; }

    public boolean isEpsilon() {
        return simbolo == null;
    }

    @Override
    public String toString() {
        String sym = isEpsilon() ? "ε" : String.valueOf(simbolo);
        return estadoOrigem + " - " + sym + " -> " + estadoDestino;
    }
}