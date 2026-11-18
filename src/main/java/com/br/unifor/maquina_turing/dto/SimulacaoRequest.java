package com.br.unifor.maquina_turing.dto;

import java.util.List;

public class SimulacaoRequest {
    private String estadoInicial;
    private String estadoFinal;
    private String fita;
    private List<TransicaoDTO> transacoes;

    // Getters e Setters
    public String getEstadoInicial() { return estadoInicial; }
    public void setEstadoInicial(String estadoInicial) { this.estadoInicial = estadoInicial; }

    public String getEstadoFinal() { return estadoFinal; }
    public void setEstadoFinal(String estadoFinal) { this.estadoFinal = estadoFinal; }

    public String getFita() { return fita; }
    public void setFita(String fita) { this.fita = fita; }

    public List<TransicaoDTO> getTransacoes() { return transacoes; }
    public void setTransacoes(List<TransicaoDTO> transacoes) { this.transacoes = transacoes; }
}