package com.br.unifor.maquina_turing.dto;

import java.util.List;

public class SimulacaoAPRequest {
    private List<String> estados;             // Q
    private List<String> alfabeto;            // Σ
    private String estadoInicial;             // q0
    private List<String> estadosFinais;       // F
    private String simboloInicialPilha;       // Z0 (ex: "Z")
    private List<TransicaoAPDTO> transicoes;  // δ
    private String cadeia;                    // w

    public List<String> getEstados() { return estados; }
    public void setEstados(List<String> estados) { this.estados = estados; }

    public List<String> getAlfabeto() { return alfabeto; }
    public void setAlfabeto(List<String> alfabeto) { this.alfabeto = alfabeto; }

    public String getEstadoInicial() { return estadoInicial; }
    public void setEstadoInicial(String estadoInicial) { this.estadoInicial = estadoInicial; }

    public List<String> getEstadosFinais() { return estadosFinais; }
    public void setEstadosFinais(List<String> estadosFinais) { this.estadosFinais = estadosFinais; }

    public String getSimboloInicialPilha() { return simboloInicialPilha; }
    public void setSimboloInicialPilha(String simboloInicialPilha) { this.simboloInicialPilha = simboloInicialPilha; }

    public List<TransicaoAPDTO> getTransicoes() { return transicoes; }
    public void setTransicoes(List<TransicaoAPDTO> transicoes) { this.transicoes = transicoes; }

    public String getCadeia() { return cadeia; }
    public void setCadeia(String cadeia) { this.cadeia = cadeia; }
}