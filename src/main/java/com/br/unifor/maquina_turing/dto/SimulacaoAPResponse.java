package com.br.unifor.maquina_turing.dto;

import java.util.List;

public class SimulacaoAPResponse {
    private boolean aceita;
    private String mensagem;
    private String estadoFinal;
    private String pilhaFinal;
    private List<PassoAPDTO> passos;

    public SimulacaoAPResponse(boolean aceita, String mensagem, String estadoFinal,
                               String pilhaFinal, List<PassoAPDTO> passos) {
        this.aceita = aceita;
        this.mensagem = mensagem;
        this.estadoFinal = estadoFinal;
        this.pilhaFinal = pilhaFinal;
        this.passos = passos;
    }

    public boolean isAceita() { return aceita; }
    public String getMensagem() { return mensagem; }
    public String getEstadoFinal() { return estadoFinal; }
    public String getPilhaFinal() { return pilhaFinal; }
    public List<PassoAPDTO> getPassos() { return passos; }
}