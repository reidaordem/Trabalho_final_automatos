package com.br.unifor.maquina_turing.automatos;

public class TransicaoAP {
    private final String estadoAtual;
    private final Character simboloLido; // null representa leitura ε
    private final Character topoPilha;   // null representa ε (não exige/remove topo)
    private final String novoEstado;
    private final String empilhar;       // "" representa ε (não empilha nada)

    public TransicaoAP(String estadoAtual, Character simboloLido, Character topoPilha,
                       String novoEstado, String empilhar) {
        this.estadoAtual = estadoAtual;
        this.simboloLido = simboloLido;
        this.topoPilha = topoPilha;
        this.novoEstado = novoEstado;
        this.empilhar = (empilhar == null) ? "" : empilhar;
    }

    public String getEstadoAtual() { return estadoAtual; }
    public Character getSimboloLido() { return simboloLido; }
    public Character getTopoPilha() { return topoPilha; }
    public String getNovoEstado() { return novoEstado; }
    public String getEmpilhar() { return empilhar; }

    public boolean isLeituraEpsilon() { return simboloLido == null; }
    public boolean isPopEpsilon() { return topoPilha == null; }
    public boolean isPushEpsilon() { return empilhar.isEmpty(); }
}