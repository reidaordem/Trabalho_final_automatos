package com.br.unifor.maquina_turing.dto;

public class TransicaoAFDTO {
    private String estadoOrigem;
    private String simbolo;
    private String estadoDestino;

    public TransicaoAFDTO() {}

    public TransicaoAFDTO(String estadoOrigem, String simbolo, String estadoDestino) {
        this.estadoOrigem = estadoOrigem;
        this.simbolo = simbolo;
        this.estadoDestino = estadoDestino;
    }

    public String getEstadoOrigem() { return estadoOrigem; }
    public void setEstadoOrigem(String estadoOrigem) { this.estadoOrigem = estadoOrigem; }

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public String getEstadoDestino() { return estadoDestino; }
    public void setEstadoDestino(String estadoDestino) { this.estadoDestino = estadoDestino; }
}