package com.br.unifor.maquina_turing.dto;

public class LinguagemRequest {
    private String alfabeto;            // Ex: "{a, b}" ou "ab"
    private String definicaoLinguagem;  // Ex: "{ab, ba, abba}" ou descrição/regra
    private String cadeia1;             // Ex: "abba"
    private String cadeia2;             // Ex: "ba" (opcional, para concatenação w1.w2)
    private Integer tamanhoMaximoFecho; // Ex: 3 (gera Σ* e Σ+ até tamanho 3)

    public String getAlfabeto() { return alfabeto; }
    public void setAlfabeto(String alfabeto) { this.alfabeto = alfabeto; }

    public String getDefinicaoLinguagem() { return definicaoLinguagem; }
    public void setDefinicaoLinguagem(String definicaoLinguagem) { this.definicaoLinguagem = definicaoLinguagem; }

    public String getCadeia1() { return cadeia1; }
    public void setCadeia1(String cadeia1) { this.cadeia1 = cadeia1; }

    public String getCadeia2() { return cadeia2; }
    public void setCadeia2(String cadeia2) { this.cadeia2 = cadeia2; }

    public Integer getTamanhoMaximoFecho() { return tamanhoMaximoFecho; }
    public void setTamanhoMaximoFecho(Integer tamanhoMaximoFecho) { this.tamanhoMaximoFecho = tamanhoMaximoFecho; }
}