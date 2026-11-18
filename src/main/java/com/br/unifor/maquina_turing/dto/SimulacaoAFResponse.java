package com.br.unifor.maquina_turing.dto;

import java.util.List;
import java.util.Set;

public class SimulacaoAFResponse {
    private boolean aceita;
    private String tipoAutomato;
    private String mensagem;
    private String caminhoResumo;
    private Set<String> estados;
    private String estadoInicial;
    private Set<String> estadosFinais;
    private List<TransicaoAFDTO> transicoes;
    private List<PassoAFDTO> passos;

    public SimulacaoAFResponse(boolean aceita, String tipoAutomato, String mensagem, String caminhoResumo,
                               Set<String> estados, String estadoInicial, Set<String> estadosFinais,
                               List<TransicaoAFDTO> transicoes, List<PassoAFDTO> passos) {
        this.aceita = aceita;
        this.tipoAutomato = tipoAutomato;
        this.mensagem = mensagem;
        this.caminhoResumo = caminhoResumo;
        this.estados = estados;
        this.estadoInicial = estadoInicial;
        this.estadosFinais = estadosFinais;
        this.transicoes = transicoes;
        this.passos = passos;
    }

    public boolean isAceita() { return aceita; }
    public String getTipoAutomato() { return tipoAutomato; }
    public String getMensagem() { return mensagem; }
    public String getCaminhoResumo() { return caminhoResumo; }
    public Set<String> getEstados() { return estados; }
    public String getEstadoInicial() { return estadoInicial; }
    public Set<String> getEstadosFinais() { return estadosFinais; }
    public List<TransicaoAFDTO> getTransicoes() { return transicoes; }
    public List<PassoAFDTO> getPassos() { return passos; }
}