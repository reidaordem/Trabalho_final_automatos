package com.br.unifor.maquina_turing.dto;

import java.util.List;

public class SimulacaoResponse {
    private boolean aceita;
    private String mensagem;
    private String estadoFinal;
    private String fitaFinal;
    private List<PassoDTO> passos;

    public SimulacaoResponse(boolean aceita, String mensagem, String estadoFinal, String fitaFinal, List<PassoDTO> passos) {
        this.aceita = aceita;
        this.mensagem = mensagem;
        this.estadoFinal = estadoFinal;
        this.fitaFinal = fitaFinal;
        this.passos = passos;
    }

    public boolean isAceita() { return aceita; }
    public String getMensagem() { return mensagem; }
    public String getEstadoFinal() { return estadoFinal; }
    public String getFitaFinal() { return fitaFinal; }
    public List<PassoDTO> getPassos() { return passos; }
}