package com.br.unifor.maquina_turing.dto;

import java.util.List;

public class LinguagemResponse {
    private boolean validaNoAlfabeto;
    private boolean pertenceALinguagem;
    private String mensagem;
    private String alfabetoFormatado;
    private String linguagemDefinida;
    private int tamanhoCadeia1;
    private String concatenacao;
    private int tamanhoConcatenacao;
    private List<String> fechoKleene;   // Σ*
    private List<String> fechoPositivo; // Σ+

    public LinguagemResponse(boolean validaNoAlfabeto, boolean pertenceALinguagem, String mensagem,
                             String alfabetoFormatado, String linguagemDefinida, int tamanhoCadeia1,
                             String concatenacao, int tamanhoConcatenacao,
                             List<String> fechoKleene, List<String> fechoPositivo) {
        this.validaNoAlfabeto = validaNoAlfabeto;
        this.pertenceALinguagem = pertenceALinguagem;
        this.mensagem = mensagem;
        this.alfabetoFormatado = alfabetoFormatado;
        this.linguagemDefinida = linguagemDefinida;
        this.tamanhoCadeia1 = tamanhoCadeia1;
        this.concatenacao = concatenacao;
        this.tamanhoConcatenacao = tamanhoConcatenacao;
        this.fechoKleene = fechoKleene;
        this.fechoPositivo = fechoPositivo;
    }

    public boolean isValidaNoAlfabeto() { return validaNoAlfabeto; }
    public boolean isPertenceALinguagem() { return pertenceALinguagem; }
    public String getMensagem() { return mensagem; }
    public String getAlfabetoFormatado() { return alfabetoFormatado; }
    public String getLinguagemDefinida() { return linguagemDefinida; }
    public int getTamanhoCadeia1() { return tamanhoCadeia1; }
    public String getConcatenacao() { return concatenacao; }
    public int getTamanhoConcatenacao() { return tamanhoConcatenacao; }
    public List<String> getFechoKleene() { return fechoKleene; }
    public List<String> getFechoPositivo() { return fechoPositivo; }
}