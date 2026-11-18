package com.br.unifor.maquina_turing.dto;

import java.util.List;

public class PassoAPDTO {
    private int passo;
    private String leitura;             // Símbolo lido da fita de entrada
    private String estado;              // Estado atual do controle finito
    private String conteudoPilha;       // Representação textual da pilha
    private List<String> elementosPilha;// Lista do topo para a base (para desenho visual)
    private String acao;

    public PassoAPDTO(int passo, String leitura, String estado, String conteudoPilha,
                      List<String> elementosPilha, String acao) {
        this.passo = passo;
        this.leitura = leitura;
        this.estado = estado;
        this.conteudoPilha = conteudoPilha;
        this.elementosPilha = elementosPilha;
        this.acao = acao;
    }

    public int getPasso() { return passo; }
    public String getLeitura() { return leitura; }
    public String getEstado() { return estado; }
    public String getConteudoPilha() { return conteudoPilha; }
    public List<String> getElementosPilha() { return elementosPilha; }
    public String getAcao() { return acao; }
}