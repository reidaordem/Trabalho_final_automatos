package com.br.unifor.maquina_turing.dto;

import java.util.List;
import java.util.Set;

public class PassoAFDTO {
    private int passo;
    private String simboloLido;
    private Set<String> estadosAnteriores;
    private Set<String> estadosAtuais;
    private List<String> transicoesDisparadas;
    private String descricao;

    public PassoAFDTO(int passo, String simboloLido, Set<String> estadosAnteriores,
                      Set<String> estadosAtuais, List<String> transicoesDisparadas, String descricao) {
        this.passo = passo;
        this.simboloLido = simboloLido;
        this.estadosAnteriores = estadosAnteriores;
        this.estadosAtuais = estadosAtuais;
        this.transicoesDisparadas = transicoesDisparadas;
        this.descricao = descricao;
    }

    public int getPasso() { return passo; }
    public String getSimboloLido() { return simboloLido; }
    public Set<String> getEstadosAnteriores() { return estadosAnteriores; }
    public Set<String> getEstadosAtuais() { return estadosAtuais; }
    public List<String> getTransicoesDisparadas() { return transicoesDisparadas; }
    public String getDescricao() { return descricao; }
}