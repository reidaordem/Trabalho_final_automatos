package com.br.unifor.maquina_turing.gramaticas;

import java.util.ArrayList;
import java.util.List;

public class NoArvore {
    private final String simbolo;
    private final List<NoArvore> filhos = new ArrayList<>();

    public NoArvore(String simbolo) {
        this.simbolo = simbolo;
    }

    public void adicionarFilho(NoArvore no) {
        this.filhos.add(no);
    }

    public String getSimbolo() { return simbolo; }
    public List<NoArvore> getFilhos() { return filhos; }
}