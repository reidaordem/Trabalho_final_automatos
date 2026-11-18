package com.br.unifor.maquina_turing.controller;

import com.br.unifor.maquina_turing.dto.SimulacaoRequest;
import com.br.unifor.maquina_turing.dto.SimulacaoResponse;
import com.br.unifor.maquina_turing.dto.TransicaoDTO;
import com.br.unifor.maquina_turing.turing.Fita;
import com.br.unifor.maquina_turing.turing.SimuladorMaquinaTuring;
import com.br.unifor.maquina_turing.turing.Transicao;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TuringController {

    @PostMapping("/simular")
    public SimulacaoResponse simular(@RequestBody SimulacaoRequest request) {
        Fita fita = new Fita(request.getFita() != null ? request.getFita() : "");
        SimuladorMaquinaTuring simulador = new SimuladorMaquinaTuring(
                request.getEstadoInicial(),
                request.getEstadoFinal(),
                fita
        );

        if (request.getTransacoes() != null) {
            for (TransicaoDTO t : request.getTransacoes()) {
                simulador.adicionarTransicao(new Transicao(
                        t.getEstadoAtual(),
                        t.getSimboloLido(),
                        t.getNovoEstado(),
                        t.getSimboloEscrito(),
                        t.getMovimento()
                ));
            }
        }

        return simulador.executar();
    }
}