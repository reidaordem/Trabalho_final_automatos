package com.br.unifor.maquina_turing.dto;

import java.util.List;

public class GLCRequest {
    private List<String> variaveis;      // V (ex: ["S"])
    private List<String> terminais;      // Σ (ex: ["a", "b"])
    private String simboloInicial;       // S (ex: "S")
    private List<String> producoes;      // R (ex: ["S -> aSb | ε"] ou ["S -> aSb", "S -> ε"])
    private String cadeia;               // w (ex: "aaabbb")

    public List<String> getVariaveis() { return variaveis; }
    public void setVariaveis(List<String> variaveis) { this.variaveis = variaveis; }

    public List<String> getTerminais() { return terminais; }
    public void setTerminais(List<String> terminais) { this.terminais = terminais; }

    public String getSimboloInicial() { return simboloInicial; }
    public void setSimboloInicial(String simboloInicial) { this.simboloInicial = simboloInicial; }

    public List<String> getProducoes() { return producoes; }
    public void setProducoes(List<String> producoes) { this.producoes = producoes; }

    public String getCadeia() { return cadeia; }
    public void setCadeia(String cadeia) { this.cadeia = cadeia; }
}