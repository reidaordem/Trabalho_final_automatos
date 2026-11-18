package com.br.unifor.maquina_turing.dto;

public class TransicaoDTO {
    private String estadoAtual;
    private char simboloLido;
    private String novoEstado;
    private char simboloEscrito;
    private char movimento;

    // Getters e Setters
    public String getEstadoAtual() { return estadoAtual; }
    public void setEstadoAtual(String estadoAtual) { this.estadoAtual = estadoAtual; }

    public char getSimboloLido() { return simboloLido; }
    public void setSimboloLido(char simboloLido) { this.simboloLido = simboloLido; }

    public String getNovoEstado() { return novoEstado; }
    public void setNovoEstado(String novoEstado) { this.novoEstado = novoEstado; }

    public char getSimboloEscrito() { return simboloEscrito; }
    public void setSimboloEscrito(char simboloEscrito) { this.simboloEscrito = simboloEscrito; }

    public char getMovimento() { return movimento; }
    public void setMovimento(char movimento) { this.movimento = movimento; }
}