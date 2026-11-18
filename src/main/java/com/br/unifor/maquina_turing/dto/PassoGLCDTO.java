package com.br.unifor.maquina_turing.dto;

public class PassoGLCDTO {
    private int passo;
    private String formaSentencial;
    private String regraAplicada;

    public PassoGLCDTO(int passo, String formaSentencial, String regraAplicada) {
        this.passo = passo;
        this.formaSentencial = formaSentencial;
        this.regraAplicada = regraAplicada;
    }

    public int getPasso() { return passo; }
    public String getFormaSentencial() { return formaSentencial; }
    public String getRegraAplicada() { return regraAplicada; }
}