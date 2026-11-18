package com.br.unifor.maquina_turing.automatos;

import com.br.unifor.maquina_turing.dto.PassoAFDTO;
import com.br.unifor.maquina_turing.dto.SimulacaoAFRequest;
import com.br.unifor.maquina_turing.dto.SimulacaoAFResponse;
import com.br.unifor.maquina_turing.dto.TransicaoAFDTO;
import com.br.unifor.maquina_turing.scanner.FormalLexer;
import com.br.unifor.maquina_turing.scanner.ParseException;

import java.util.*;

public class AutomatoFinito {

    private final Set<String> estados;
    private final Set<Character> alfabeto;
    private final String estadoInicial;
    private final Set<String> estadosFinais;
    private final List<TransicaoAF> transicoes;
    private final boolean deterministico;

    public AutomatoFinito(Set<String> estados, Set<Character> alfabeto, String estadoInicial,
                          Set<String> estadosFinais, List<TransicaoAF> transicoes) {
        if (estados == null || estados.isEmpty()) {
            throw new ParseException("O conjunto de estados Q não pode ser vazio.");
        }
        if (estadoInicial == null || !estados.contains(estadoInicial)) {
            throw new ParseException("O estado inicial q0 ('" + estadoInicial + "') deve pertencer a Q.");
        }
        if (estadosFinais == null) {
            throw new ParseException("O conjunto de estados finais F não pode ser nulo.");
        }
        for (String f : estadosFinais) {
            if (!estados.contains(f)) {
                throw new ParseException("O estado final '" + f + "' não pertence ao conjunto Q.");
            }
        }

        this.estados = new LinkedHashSet<>(estados);
        this.alfabeto = new LinkedHashSet<>(alfabeto);
        this.estadoInicial = estadoInicial;
        this.estadosFinais = new LinkedHashSet<>(estadosFinais);
        this.transicoes = new ArrayList<>();

        if (transicoes != null) {
            for (TransicaoAF t : transicoes) {
                if (!this.estados.contains(t.getEstadoOrigem())) {
                    throw new ParseException("Estado de origem inválido na transição: " + t.getEstadoOrigem());
                }
                if (!this.estados.contains(t.getEstadoDestino())) {
                    throw new ParseException("Estado de destino inválido na transição: " + t.getEstadoDestino());
                }
                if (!t.isEpsilon() && !this.alfabeto.contains(t.getSimbolo())) {
                    throw new ParseException("Símbolo '" + t.getSimbolo() + "' da transição não pertence ao alfabeto Σ.");
                }
                this.transicoes.add(t);
            }
        }

        this.deterministico = verificarDeterminismo();
    }

    /**
     * Constrói o autômato a partir do payload JSON validando Σ pelo FormalLexer do scanner.
     */
    public static AutomatoFinito deRequest(SimulacaoAFRequest req, FormalLexer formalLexer) {
        if (req.getAlfabeto() == null || req.getAlfabeto().isEmpty()) {
            throw new ParseException("O alfabeto Σ do autômato não pode ser vazio.");
        }
        Set<Character> sigma = formalLexer.tokenizarAlfabeto(String.join(",", req.getAlfabeto()));

        Set<String> conjuntoEstados = new LinkedHashSet<>();
        if (req.getEstados() != null) {
            for (String e : req.getEstados()) {
                if (e != null && !e.trim().isEmpty()) {
                    conjuntoEstados.add(e.trim());
                }
            }
        }

        Set<String> finais = new LinkedHashSet<>();
        if (req.getEstadosFinais() != null) {
            for (String f : req.getEstadosFinais()) {
                if (f != null && !f.trim().isEmpty()) {
                    finais.add(f.trim());
                }
            }
        }

        List<TransicaoAF> listaTransicoes = new ArrayList<>();
        if (req.getTransicoes() != null) {
            for (TransicaoAFDTO dto : req.getTransicoes()) {
                String origem = dto.getEstadoOrigem() != null ? dto.getEstadoOrigem().trim() : "";
                String destino = dto.getEstadoDestino() != null ? dto.getEstadoDestino().trim() : "";
                String symStr = dto.getSimbolo() != null ? dto.getSimbolo().trim() : "";

                Character simbolo = null;
                if (!symStr.isEmpty() && !symStr.equals("ε") && !symStr.equals("&") && !symStr.equalsIgnoreCase("lambda")) {
                    if (symStr.length() != 1) {
                        throw new ParseException("Símbolo inválido na transição: '" + symStr + "'. Use 1 caractere ou 'ε' para vazio.");
                    }
                    simbolo = symStr.charAt(0);
                }

                listaTransicoes.add(new TransicaoAF(origem, simbolo, destino));
            }
        }

        String inicial = req.getEstadoInicial() != null ? req.getEstadoInicial().trim() : "";
        return new AutomatoFinito(conjuntoEstados, sigma, inicial, finais, listaTransicoes);
    }

    /**
     * Um autômato é AFD se não possui transições ε e se para cada par (estado, símbolo)
     * existe no máximo 1 estado de destino distinto.
     */
    private boolean verificarDeterminismo() {
        Map<String, Set<String>> mapa = new HashMap<>();
        for (TransicaoAF t : transicoes) {
            if (t.isEpsilon()) {
                return false;
            }
            String chave = t.getEstadoOrigem() + "::" + t.getSimbolo();
            mapa.computeIfAbsent(chave, k -> new HashSet<>()).add(t.getEstadoDestino());
            if (mapa.get(chave).size() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calcula o fecho-ε(S): todos os estados alcançáveis a partir de S usando zero ou mais transições ε.
     */
    private Set<String> fechoEpsilon(Set<String> origem) {
        Set<String> fecho = new LinkedHashSet<>(origem);
        Queue<String> fila = new ArrayDeque<>(origem);

        while (!fila.isEmpty()) {
            String atual = fila.poll();
            for (TransicaoAF t : transicoes) {
                if (t.isEpsilon() && t.getEstadoOrigem().equals(atual)) {
                    if (fecho.add(t.getEstadoDestino())) {
                        fila.add(t.getEstadoDestino());
                    }
                }
            }
        }
        return fecho;
    }

    public SimulacaoAFResponse simular(String cadeiaEntrada, FormalLexer formalLexer) {
        String w = (cadeiaEntrada == null) ? "" : cadeiaEntrada.trim();

        // Valida a cadeia contra Σ usando o scanner do projeto
        formalLexer.validarCadeiaNoAlfabeto(w, alfabeto);

        List<PassoAFDTO> passos = new ArrayList<>();
        List<String> trilhaCaminho = new ArrayList<>();

        // Passo 0: Estado inicial + fecho-ε
        Set<String> iniciais = new LinkedHashSet<>();
        iniciais.add(estadoInicial);
        Set<String> estadosAtuais = fechoEpsilon(iniciais);

        trilhaCaminho.add(formatarConjuntoEstados(estadosAtuais));
        passos.add(new PassoAFDTO(
                0,
                "ε (início)",
                Collections.emptySet(),
                new LinkedHashSet<>(estadosAtuais),
                Collections.emptyList(),
                "Estado(s) inicial(is) ativo(s): " + formatarConjuntoEstados(estadosAtuais)
        ));

        // Processa cada símbolo da cadeia w
        for (int i = 0; i < w.length(); i++) {
            char simbolo = w.charAt(i);
            Set<String> anteriores = new LinkedHashSet<>(estadosAtuais);
            Set<String> alcancadosDiretos = new LinkedHashSet<>();
            List<String> disparadas = new ArrayList<>();

            for (String est : anteriores) {
                for (TransicaoAF t : transicoes) {
                    if (!t.isEpsilon() && t.getEstadoOrigem().equals(est) && t.getSimbolo() == simbolo) {
                        alcancadosDiretos.add(t.getEstadoDestino());
                        disparadas.add(t.toString());
                    }
                }
            }

            estadosAtuais = fechoEpsilon(alcancadosDiretos);
            trilhaCaminho.add(formatarConjuntoEstados(estadosAtuais));

            if (estadosAtuais.isEmpty()) {
                passos.add(new PassoAFDTO(
                        i + 1,
                        String.valueOf(simbolo),
                        anteriores,
                        Collections.emptySet(),
                        disparadas,
                        "Sem transição para '" + simbolo + "' a partir de " + formatarConjuntoEstados(anteriores) + ". Cadeia rejeitada."
                ));
                break;
            }

            passos.add(new PassoAFDTO(
                    i + 1,
                    String.valueOf(simbolo),
                    anteriores,
                    new LinkedHashSet<>(estadosAtuais),
                    disparadas,
                    "Leu '" + simbolo + "' -> estados ativos: " + formatarConjuntoEstados(estadosAtuais)
            ));
        }

        // Critério de aceitação: interseção não vazia entre estados atuais e F (S_final ∩ F ≠ ∅)
        boolean aceita = false;
        for (String est : estadosAtuais) {
            if (estadosFinais.contains(est)) {
                aceita = true;
                break;
            }
        }

        String tipo = deterministico ? "AFD" : "AFN";
        String resumo = String.join(" -> ", trilhaCaminho);
        String msg = (aceita ? "ACEITA" : "REJEITA")
                + " (" + tipo + ") | Estados finais ativos: " + formatarConjuntoEstados(estadosAtuais);

        List<TransicaoAFDTO> transicoesDTO = new ArrayList<>();
        for (TransicaoAF t : transicoes) {
            transicoesDTO.add(new TransicaoAFDTO(
                    t.getEstadoOrigem(),
                    t.isEpsilon() ? "ε" : String.valueOf(t.getSimbolo()),
                    t.getEstadoDestino()
            ));
        }

        return new SimulacaoAFResponse(
                aceita,
                tipo,
                msg,
                resumo,
                estados,
                estadoInicial,
                estadosFinais,
                transicoesDTO,
                passos
        );
    }

    private String formatarConjuntoEstados(Set<String> conjunto) {
        if (conjunto.isEmpty()) {
            return "∅";
        }
        if (conjunto.size() == 1) {
            return conjunto.iterator().next();
        }
        return "{" + String.join(", ", conjunto) + "}";
    }
}