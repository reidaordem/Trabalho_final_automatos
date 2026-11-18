package com.br.unifor.maquina_turing.turing;

import com.br.unifor.maquina_turing.dto.PassoDTO;
import com.br.unifor.maquina_turing.dto.SimulacaoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimuladorMaquinaTuring {

    private String estadoAtual;
    private final String estadoFinal;
    private final Fita fita;
    private final List<Transicao> transicaos = new ArrayList<>();

    public SimuladorMaquinaTuring(String estadoAtual, String estadoFinal, Fita fita) {
        this.estadoAtual = estadoAtual;
        this.estadoFinal = estadoFinal;
        this.fita = fita;
    }

    public void adicionarTransicao(Transicao transicao) {
        transicaos.add(transicao);
    }

    public SimulacaoResponse executar() {
        List<PassoDTO> passos = new ArrayList<>();
        int passo = 0;
        int limitePassos = 1000; // Trava teórica contra loops (Problema da Parada)

        while (!estadoAtual.equals(estadoFinal) && passo < limitePassos) {
            char simbolo = fita.ler();

            Optional<Transicao> transicaoEntrada = transicaos.stream()
                    .filter(f -> f.getEstadoAtual().equals(estadoAtual) && f.getSimboloLido() == simbolo)
                    .findFirst();

            if (transicaoEntrada.isEmpty()) {
                passos.add(new PassoDTO(passo, estadoAtual, simbolo, fita.conteudo(), fita.getCabecote(), "Sem transição válida. Máquina parou."));
                return new SimulacaoResponse(false, "Máquina parou: sem transição definida para o estado/símbolo atual.", estadoAtual, fita.conteudo(), passos);
            }

            Transicao t = transicaoEntrada.get();

            String acao = "Escreveu '" + t.getSimboloEscrito() + "', moveu " + t.getMovimento() + ", ir para " + t.getNovoEstado();
            passos.add(new PassoDTO(passo, estadoAtual, simbolo, fita.conteudo(), fita.getCabecote(), acao));

            fita.escrever(t.getSimboloEscrito());
            fita.mover(t.getMovimento());
            estadoAtual = t.getNovoEstado();

            passo++;
        }

        if (estadoAtual.equals(estadoFinal)) {
            passos.add(new PassoDTO(passo, estadoAtual, fita.ler(), fita.conteudo(), fita.getCabecote(), "Estado final atingido!"));
            return new SimulacaoResponse(true, "Cadeia aceita com sucesso!", estadoAtual, fita.conteudo(), passos);
        } else {
            return new SimulacaoResponse(false, "Limite de passos excedido (Loop detectado).", estadoAtual, fita.conteudo(), passos);
        }
    }
}