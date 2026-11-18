package com.br.unifor.maquina_turing.controller;

import com.br.unifor.maquina_turing.automatos.AutomatoFinito;
import com.br.unifor.maquina_turing.automatos.AutomatoPilha;
import com.br.unifor.maquina_turing.dto.SimulacaoAFRequest;
import com.br.unifor.maquina_turing.dto.SimulacaoAPRequest;
import com.br.unifor.maquina_turing.scanner.FormalLexer;
import com.br.unifor.maquina_turing.scanner.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/automatos")
public class AutomatoController {

    private final FormalLexer formalLexer;

    public AutomatoController(FormalLexer formalLexer) {
        this.formalLexer = formalLexer;
    }

    @PostMapping("/finito/simular")
    public ResponseEntity<?> simularAF(@RequestBody SimulacaoAFRequest request) {
        try {
            AutomatoFinito af = AutomatoFinito.deRequest(request, formalLexer);
            return ResponseEntity.ok(af.simular(request.getCadeia(), formalLexer));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/pilha/simular")
    public ResponseEntity<?> simularAP(@RequestBody SimulacaoAPRequest request) {
        try {
            AutomatoPilha ap = AutomatoPilha.deRequest(request, formalLexer);
            return ResponseEntity.ok(ap.simular(request.getCadeia(), formalLexer));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}