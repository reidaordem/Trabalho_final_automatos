package com.br.unifor.maquina_turing.automatos;

import com.br.unifor.maquina_turing.dto.PassoAPDTO;
import com.br.unifor.maquina_turing.dto.SimulacaoAPRequest;
import com.br.unifor.maquina_turing.dto.SimulacaoAPResponse;
import com.br.unifor.maquina_turing.dto.TransicaoAPDTO;
import com.br.unifor.maquina_turing.scanner.FormalLexer;
import com.br.unifor.maquina_turing.scanner.ParseException;

import java.util.*;

public class AutomatoPilha {

    private final Set<String> estados;
    private final Set<Character> alfabeto;
    private final String estadoInicial;
    private final Set<String> estadosFinais;
    private final char simboloInicialPilha;
    private final List<TransicaoAP> transicoes;

    public AutomatoPilha(Set<String> estados, Set<Character> alfabeto, String estadoInicial,
                         Set<String> estadosFinais, char simboloInicialPilha, List<TransicaoAP> transicoes) {
        if (estados == null || estados.isEmpty()) {
            throw new ParseException("O conjunto de estados Q do Autômato com Pilha não pode ser vazio.");
        }
        if (estadoInicial == null || !estados.contains(estadoInicial)) {
            throw new ParseException("O estado inicial q0 ('" + estadoInicial + "') deve pertencer a Q.");
        }
        if (estadosFinais == null || estadosFinais.isEmpty()) {
            throw new ParseException("Informe ao menos um estado final em F.");
        }
        for (String f : estadosFinais) {
            if (!estados.contains(f)) {
                throw new ParseException("O estado final '" + f + "' não pertence a Q.");
            }
        }

        this.estados = new LinkedHashSet<>(estados);
        this.alfabeto = new LinkedHashSet<>(alfabeto);
        this.estadoInicial = estadoInicial;
        this.estadosFinais = new LinkedHashSet<>(estadosFinais);
        this.simboloInicialPilha = simboloInicialPilha;
        this.transicoes = new ArrayList<>();

        if (transicoes != null) {
            for (TransicaoAP t : transicoes) {
                if (!this.estados.contains(t.getEstadoAtual())) {
                    throw new ParseException("Estado de origem inválido na transição do AP: " + t.getEstadoAtual());
                }
                if (!this.estados.contains(t.getNovoEstado())) {
                    throw new ParseException("Estado de destino inválido na transição do AP: " + t.getNovoEstado());
                }
                if (!t.isLeituraEpsilon() && !this.alfabeto.contains(t.getSimboloLido())) {
                    throw new ParseException("Símbolo '" + t.getSimboloLido() + "' não pertence ao alfabeto Σ.");
                }
                this.transicoes.add(t);
            }
        }
    }

    public static AutomatoPilha deRequest(SimulacaoAPRequest req, FormalLexer formalLexer) {
        if (req.getAlfabeto() == null || req.getAlfabeto().isEmpty()) {
            throw new ParseException("O alfabeto Σ do Autômato com Pilha não pode ser vazio.");
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

        String z0Str = (req.getSimboloInicialPilha() != null && !req.getSimboloInicialPilha().trim().isEmpty())
                ? req.getSimboloInicialPilha().trim()
                : "Z";
        if (z0Str.length() != 1) {
            throw new ParseException("O símbolo inicial da pilha Z0 deve ter exatamente 1 caractere.");
        }

        List<TransicaoAP> lista = new ArrayList<>();
        if (req.getTransicoes() != null) {
            for (TransicaoAPDTO dto : req.getTransicoes()) {
                String origem = dto.getEstadoAtual() != null ? dto.getEstadoAtual().trim() : "";
                String destino = dto.getNovoEstado() != null ? dto.getNovoEstado().trim() : "";
                Character leitura = parseSimboloOpcional(dto.getSimboloLido(), "simboloLido");
                Character topo = parseSimboloOpcional(dto.getTopoPilha(), "topoPilha");
                String empilhar = parseCadeiaPilha(dto.getEmpilhar());

                lista.add(new TransicaoAP(origem, leitura, topo, destino, empilhar));
            }
        }

        String inicial = req.getEstadoInicial() != null ? req.getEstadoInicial().trim() : "";
        return new AutomatoPilha(conjuntoEstados, sigma, inicial, finais, z0Str.charAt(0), lista);
    }

    /**
     * Instancia o Autômato com Pilha padrão para a linguagem L = { a^n b^n | n >= 1 }.
     */
    public static AutomatoPilha criarPadraoAnBn() {
        Set<String> q = new LinkedHashSet<>(List.of("q0", "q1", "q2"));
        Set<Character> sigma = new LinkedHashSet<>(List.of('a', 'b'));
        Set<String> f = Set.of("q2");
        List<TransicaoAP> delta = List.of(
                new TransicaoAP("q0", 'a', 'Z', "q0", "AZ"), // Empilha primeiro 'A' sobre 'Z'
                new TransicaoAP("q0", 'a', 'A', "q0", "AA"), // Empilha demais 'A's
                new TransicaoAP("q0", 'b', 'A', "q1", ""),   // Primeiro 'b' desempilha 'A' e muda para q1
                new TransicaoAP("q1", 'b', 'A', "q1", ""),   // Demais 'b's desempilham 'A'
                new TransicaoAP("q1", null, 'Z', "q2", "Z")  // Fim da cadeia com topo 'Z' -> aceita em q2
        );
        return new AutomatoPilha(q, sigma, "q0", f, 'Z', delta);
    }

    public SimulacaoAPResponse simular(String cadeiaEntrada, FormalLexer formalLexer) {
        String w = (cadeiaEntrada == null) ? "" : cadeiaEntrada.trim();
        formalLexer.validarCadeiaNoAlfabeto(w, alfabeto);

        PilhaAP pilhaInicial = new PilhaAP();
        pilhaInicial.empilhar(simboloInicialPilha);

        List<PassoAPDTO> passosIniciais = new ArrayList<>();
        passosIniciais.add(new PassoAPDTO(
                0,
                "ε (início)",
                estadoInicial,
                pilhaInicial.conteudoFormatado(),
                pilhaInicial.listarDoTopoParaBase(),
                "Início em " + estadoInicial + " com símbolo inicial '" + simboloInicialPilha + "' empilhado."
        ));

        Queue<ConfiguracaoAP> fila = new ArrayDeque<>();
        Set<String> visitados = new HashSet<>();

        ConfiguracaoAP inicio = new ConfiguracaoAP(estadoInicial, 0, pilhaInicial, passosIniciais);
        fila.add(inicio);
        visitados.add(inicio.chave());

        ConfiguracaoAP melhorTentativa = inicio;
        int limiteExploracao = 2000;
        int explorados = 0;

        while (!fila.isEmpty() && explorados < limiteExploracao) {
            ConfiguracaoAP atual = fila.poll();
            explorados++;

            if (atual.passos.size() > melhorTentativa.passos.size()) {
                melhorTentativa = atual;
            }

            // Critério de aceitação por estado final com toda a cadeia consumida
            if (atual.indiceCadeia == w.length() && estadosFinais.contains(atual.estado)) {
                return new SimulacaoAPResponse(
                        true,
                        "ACEITA! Cadeia reconhecida pelo Autômato com Pilha no estado final " + atual.estado + ".",
                        atual.estado,
                        atual.pilha.conteudoFormatado(),
                        atual.passos
                );
            }

            if (atual.passos.size() >= 100) {
                continue;
            }

            for (TransicaoAP t : transicoes) {
                if (!t.getEstadoAtual().equals(atual.estado)) {
                    continue;
                }

                // Verifica leitura da fita (consumindo símbolo ou via ε)
                int proximoIndice = atual.indiceCadeia;
                String leituraStr;
                if (t.isLeituraEpsilon()) {
                    leituraStr = "ε";
                } else {
                    if (atual.indiceCadeia >= w.length() || w.charAt(atual.indiceCadeia) != t.getSimboloLido()) {
                        continue;
                    }
                    proximoIndice++;
                    leituraStr = String.valueOf(t.getSimboloLido());
                }

                // Verifica topo da pilha
                if (!t.isPopEpsilon()) {
                    Character topoAtual = atual.pilha.topo();
                    if (topoAtual == null || topoAtual != t.getTopoPilha()) {
                        continue;
                    }
                }

                // Executa clonagem da pilha e aplica desempilhar / empilhar
                PilhaAP novaPilha = atual.pilha.copiar();
                StringBuilder descOperacao = new StringBuilder();
                descOperacao.append("Leu '").append(leituraStr).append("'");

                if (!t.isPopEpsilon()) {
                    char removido = novaPilha.desempilhar();
                    descOperacao.append(", desempilhou '").append(removido).append("'");
                }

                if (!t.isPushEpsilon()) {
                    // Empilha da direita para a esquerda para que o 1º caractere fique no topo
                    String cadeiaEmpilhar = t.getEmpilhar();
                    for (int i = cadeiaEmpilhar.length() - 1; i >= 0; i--) {
                        novaPilha.empilhar(cadeiaEmpilhar.charAt(i));
                    }
                    descOperacao.append(", empilhou '").append(cadeiaEmpilhar).append("'");
                } else {
                    descOperacao.append(", não empilhou (ε)");
                }

                descOperacao.append(" -> estado ").append(t.getNovoEstado());

                List<PassoAPDTO> novosPassos = new ArrayList<>(atual.passos);
                novosPassos.add(new PassoAPDTO(
                        novosPassos.size(),
                        leituraStr,
                        t.getNovoEstado(),
                        novaPilha.conteudoFormatado(),
                        novaPilha.listarDoTopoParaBase(),
                        descOperacao.toString()
                ));

                ConfiguracaoAP proxima = new ConfiguracaoAP(t.getNovoEstado(), proximoIndice, novaPilha, novosPassos);
                if (visitados.add(proxima.chave())) {
                    fila.add(proxima);
                }
            }
        }

        // Caso rejeitada, anexa o motivo final ao rastro mais profundo encontrado
        List<PassoAPDTO> passosRejeicao = new ArrayList<>(melhorTentativa.passos);
        passosRejeicao.add(new PassoAPDTO(
                passosRejeicao.size(),
                melhorTentativa.indiceCadeia < w.length() ? String.valueOf(w.charAt(melhorTentativa.indiceCadeia)) : "EOF",
                melhorTentativa.estado,
                melhorTentativa.pilha.conteudoFormatado(),
                melhorTentativa.pilha.listarDoTopoParaBase(),
                "Cadeia rejeitada: sem transição válida para atingir estado final."
        ));

        return new SimulacaoAPResponse(
                false,
                "REJEITA! A cadeia não pertence à linguagem aceita pelo Autômato com Pilha.",
                melhorTentativa.estado,
                melhorTentativa.pilha.conteudoFormatado(),
                passosRejeicao
        );
    }

    private static Character parseSimboloOpcional(String valor, String campo) {
        if (valor == null) return null;
        String limpo = valor.trim();
        if (limpo.isEmpty() || limpo.equals("ε") || limpo.equals("&") || limpo.equalsIgnoreCase("lambda")) {
            return null;
        }
        if (limpo.length() != 1) {
            throw new ParseException("Campo '" + campo + "' inválido ('" + limpo + "'). Use 1 caractere ou 'ε'.");
        }
        return limpo.charAt(0);
    }

    private static String parseCadeiaPilha(String valor) {
        if (valor == null) return "";
        String limpo = valor.trim();
        if (limpo.isEmpty() || limpo.equals("ε") || limpo.equals("&") || limpo.equalsIgnoreCase("lambda")) {
            return "";
        }
        return limpo;
    }

    private static class ConfiguracaoAP {
        final String estado;
        final int indiceCadeia;
        final PilhaAP pilha;
        final List<PassoAPDTO> passos;

        ConfiguracaoAP(String estado, int indiceCadeia, PilhaAP pilha, List<PassoAPDTO> passos) {
            this.estado = estado;
            this.indiceCadeia = indiceCadeia;
            this.pilha = pilha;
            this.passos = passos;
        }

        String chave() {
            return estado + "|" + indiceCadeia + "|" + pilha.conteudoFormatado();
        }
    }
}