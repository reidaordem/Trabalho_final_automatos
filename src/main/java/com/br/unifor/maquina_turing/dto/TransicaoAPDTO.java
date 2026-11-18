package com.br.unifor.maquina_turing.dto;

public class TransicaoAPDTO {
    private String estadoAtual;
    private String simboloLido; // Ex: "a", "b" ou "ε"
    private String topoPilha;   // Ex: "Z", "A" ou "ε" (não desempilha nada)
    private String novoEstado;
    private String empilhar;    // Ex: "AZ", "AA", "Z" ou "ε" (não empilha nada)

    public TransicaoAPDTO() {}

    public TransicaoAPDTO(String estadoAtual, String simboloLido, String topoPilha,
                          String novoEstado, String empilhar) {
        this.estadoAtual = estadoAtual;
        this.simboloLido = simboloLido;
        this.topoPilha = topoPilha;
        this.novoEstado = novoEstado;
        this.empilhar = empilhar;
    }

    public String getEstadoAtual() { return estadoAtual; }
    public void setEstadoAtual(String estadoAtual) { this.estadoAtual = estadoAtual; }

    public String getSimboloLido() { return simboloLido; }
    public void setSimboloLido(String simboloLido) { this.simboloLido = simboloLido; }

    public String getTopoPilha() { return topoPilha; }
    public void setTopoPilha(String topoPilha) { this.topoPilha = topoPilha; }

    public String getNovoEstado() { return novoEstado; }
    public void setNovoEstado(String novoEstado) { this.novoEstado = novoEstado; }

    public String getEmpilhar() { return empilhar; }
    public void setEmpilhar(String empilhar) { this.empilhar = empilhar; }
}