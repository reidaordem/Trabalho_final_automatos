package com.br.unifor.maquina_turing.dto;

import com.br.unifor.maquina_turing.gramaticas.NoArvore;
import java.util.List;

public class SimulacaoGLCResponse {
    private boolean aceita;
    private String mensagem;
    private String derivacaoResumo;
    private List<PassoGLCDTO> passos;
    private NoArvore arvoreDerivacao;

    public SimulacaoGLCResponse(boolean aceita, String mensagem, String derivacaoResumo,
                                List<PassoGLCDTO> passos, NoArvore arvoreDerivacao) {
        this.aceita = aceita;
        this.mensagem = mensagem;
        this.derivacaoResumo = derivacaoResumo;
        this.passos = passos;
        this.arvoreDerivacao = arvoreDerivacao;
    }

    public boolean isAceita() { return aceita; }
    public String getMensagem() { return mensagem; }
    public String getDerivacaoResumo() { return derivacaoResumo; }
    public List<PassoGLCDTO> getPassos() { return passos; }
    public NoArvore getArvoreDerivacao() { return arvoreDerivacao; }
}