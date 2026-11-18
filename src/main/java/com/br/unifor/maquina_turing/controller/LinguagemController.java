package com.br.unifor.maquina_turing.controller;

import com.br.unifor.maquina_turing.dto.LinguagemRequest;
import com.br.unifor.maquina_turing.dto.LinguagemResponse;
import com.br.unifor.maquina_turing.linguagens.LinguagemService;
import com.br.unifor.maquina_turing.scanner.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/linguagens")
public class LinguagemController {

    private final LinguagemService service;

    public LinguagemController(LinguagemService service) {
        this.service = service;
    }

    @PostMapping("/analisar")
    public ResponseEntity<?> analisar(@RequestBody LinguagemRequest request) {
        try {
            return ResponseEntity.ok(service.executarOperacoes(request));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}