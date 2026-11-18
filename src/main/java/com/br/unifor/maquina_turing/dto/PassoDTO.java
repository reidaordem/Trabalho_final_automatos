package com.br.unifor.maquina_turing.dto;

public class PassoDTO {
    private int passo;
    private String estadoAtual;
    private char simboloLido;
    private String fitaConteudo;
    private int cabecote;
    private String acao;

    public PassoDTO(int passo, String estadoAtual, char simboloLido, String fitaConteudo, int cabecote, String acao) {
        this.passo = passo;
        this.estadoAtual = estadoAtual;
        this.simboloLido = simboloLido;
        this.fitaConteudo = fitaConteudo;
        this.cabecote = cabecote;
        this.acao = acao;
    }

    public int getPasso() { return passo; }
    public String getEstadoAtual() { return estadoAtual; }
    public char getSimboloLido() { return simboloLido; }
    public String getFitaConteudo() { return fitaConteudo; }
    public int getCabecote() { return cabecote; }
    public String getAcao() { return acao; }
}