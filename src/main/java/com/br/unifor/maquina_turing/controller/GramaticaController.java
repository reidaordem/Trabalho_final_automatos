package com.br.unifor.maquina_turing.controller;

import com.br.unifor.maquina_turing.dto.GLCRequest;
import com.br.unifor.maquina_turing.dto.SimulacaoGLCResponse;
import com.br.unifor.maquina_turing.gramaticas.GramaticaService;
import com.br.unifor.maquina_turing.scanner.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gramaticas")
public class GramaticaController {

    private final GramaticaService service;

    public GramaticaController(GramaticaService service) {
        this.service = service;
    }

    @PostMapping("/simular")
    public ResponseEntity<?> simularGLC(@RequestBody GLCRequest request) {
        try {
            return ResponseEntity.ok(service.simularGLC(request));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/nativa")
    public ResponseEntity<?> validarNativa(@RequestBody Map<String, String> payload) {
        try {
            String expressao = payload.get("expressao");
            return ResponseEntity.ok(service.validarExpressaoNativa(expressao));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}