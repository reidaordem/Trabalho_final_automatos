package com.br.unifor.maquina_turing.turing;

public class Transicao {
    private final String estadoAtual;
    private final char simboloLido;
    private final String novoEstado;
    private final char simboloEscrito;
    private final char movimento;

    public Transicao(String estadoAtual, char simboloLido, String novoEstado, char simboloEscrito, char movimento) {
        this.estadoAtual = estadoAtual;
        this.simboloLido = simboloLido;
        this.novoEstado = novoEstado;
        this.simboloEscrito = simboloEscrito;
        this.movimento = movimento;
    }

    public String getEstadoAtual() { return estadoAtual; }
    public char getSimboloLido() { return simboloLido; }
    public String getNovoEstado() { return novoEstado; }
    public char getSimboloEscrito() { return simboloEscrito; }
    public char getMovimento() { return movimento; }
}