package com.br.unifor.maquina_turing.gramaticas;

import com.br.unifor.maquina_turing.dto.GLCRequest;
import com.br.unifor.maquina_turing.dto.PassoGLCDTO;
import com.br.unifor.maquina_turing.dto.SimulacaoGLCResponse;
import com.br.unifor.maquina_turing.scanner.FormalLexer;
import com.br.unifor.maquina_turing.scanner.ParseException;
import com.br.unifor.maquina_turing.scanner.Token;
import com.br.unifor.maquina_turing.scanner.TokenType;

import java.util.*;

public class GramaticaLivreContexto {

    private final Set<String> variaveis;
    private final Set<Character> terminais;
    private final List<Producao> producoes;
    private final String simboloInicial;

    public GramaticaLivreContexto(Set<String> variaveis, Set<Character> terminais,
                                  List<Producao> producoes, String simboloInicial) {
        if (variaveis == null || variaveis.isEmpty()) {
            throw new ParseException("O conjunto de variáveis V não pode ser vazio.");
        }
        if (simboloInicial == null || !variaveis.contains(simboloInicial)) {
            throw new ParseException("O símbolo inicial S ('" + simboloInicial + "') deve pertencer a V.");
        }
        if (producoes == null || producoes.isEmpty()) {
            throw new ParseException("A gramática deve possuir pelo menos uma produção em R.");
        }

        for (String v : variaveis) {
            if (v.length() == 1 && terminais.contains(v.charAt(0))) {
                throw new ParseException("Disjunção violada: '" + v + "' não pode pertencer a V e Σ simultaneamente (V ∩ Σ = ∅).");
            }
        }

        this.variaveis = new LinkedHashSet<>(variaveis);
        this.terminais = new LinkedHashSet<>(terminais);
        this.producoes = new ArrayList<>(producoes);
        this.simboloInicial = simboloInicial;
    }

    /**
     * Constrói a GLC usando o FormalLexer do scanner para tokenizar Σ e as produções R.
     */
    public static GramaticaLivreContexto deRequest(GLCRequest req, FormalLexer formalLexer) {
        if (req.getTerminais() == null || req.getTerminais().isEmpty()) {
            throw new ParseException("O conjunto de terminais Σ não pode ser vazio.");
        }
        Set<Character> sigma = formalLexer.tokenizarAlfabeto(String.join(",", req.getTerminais()));

        Set<String> vars = new LinkedHashSet<>();
        if (req.getVariaveis() != null) {
            for (String v : req.getVariaveis()) {
                if (v != null && !v.trim().isEmpty()) {
                    String varLimpa = v.trim();
                    if (varLimpa.length() != 1 || !Character.isUpperCase(varLimpa.charAt(0))) {
                        throw new ParseException("Variável inválida '" + varLimpa + "'. Use letras maiúsculas (ex: S, A, B).");
                    }
                    vars.add(varLimpa);
                }
            }
        }

        String inicial = req.getSimboloInicial() != null ? req.getSimboloInicial().trim() : "";

        List<Producao> listaProducoes = new ArrayList<>();
        if (req.getProducoes() != null) {
            for (String linhaRegra : req.getProducoes()) {
                if (linhaRegra == null || linhaRegra.trim().isEmpty()) {
                    continue;
                }
                List<Token> tokens = formalLexer.tokenizarProducao(linhaRegra);
                extrairProducoesDosTokens(tokens, vars, sigma, listaProducoes);
            }
        }

        return new GramaticaLivreContexto(vars, sigma, listaProducoes, inicial);
    }

    private static void extrairProducoesDosTokens(List<Token> tokens, Set<String> vars,
                                                  Set<Character> sigma, List<Producao> destino) {
        if (tokens.size() < 3 || tokens.get(0).getType() != TokenType.ID || tokens.get(1).getType() != TokenType.ARROW) {
            throw new ParseException("Formato de produção inválido. Use 'Variavel -> corpo' (ex: S -> aSb | ε).");
        }

        String lhs = tokens.get(0).getLexeme();
        if (!vars.contains(lhs)) {
            throw new ParseException("Variável '" + lhs + "' no lado esquerdo da produção não foi declarada em V.");
        }

        StringBuilder corpoAtual = new StringBuilder();
        boolean viuLambda = false;

        for (int i = 2; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.getType() == TokenType.EOF || t.getType() == TokenType.PIPE) {
                if (!viuLambda && corpoAtual.isEmpty()) {
                    throw new ParseException("Produção vazia sem símbolo ε/lambda para '" + lhs + "'.");
                }
                destino.add(new Producao(lhs, viuLambda ? "" : corpoAtual.toString()));
                corpoAtual.setLength(0);
                viuLambda = false;
            } else if (t.getType() == TokenType.LAMBDA) {
                viuLambda = true;
            } else if (t.getType() == TokenType.ID) {
                if (!vars.contains(t.getLexeme())) {
                    throw new ParseException("Variável '" + t.getLexeme() + "' não declarada em V.");
                }
                corpoAtual.append(t.getLexeme());
            } else if (t.getType() == TokenType.SYMBOL) {
                char c = t.getLexeme().charAt(0);
                if (!sigma.contains(c)) {
                    throw new ParseException("Terminal '" + c + "' não declarado em Σ.");
                }
                corpoAtual.append(c);
            }
        }
    }

    /**
     * Executa derivação mais à esquerda (BFS com poda de prefixo e comprimento)
     * e reconstrói a árvore de derivação simplificada.
     */
    public SimulacaoGLCResponse derivar(String cadeiaAlvo, int maxProfundidade, FormalLexer formalLexer) {
        String w = (cadeiaAlvo == null) ? "" : cadeiaAlvo.trim();
        formalLexer.validarCadeiaNoAlfabeto(w, terminais);

        Queue<EstadoBusca> fila = new ArrayDeque<>();
        Set<String> visitados = new HashSet<>();

        List<String> formasIniciais = List.of(simboloInicial);
        fila.add(new EstadoBusca(simboloInicial, formasIniciais, Collections.emptyList()));
        visitados.add(simboloInicial);

        while (!fila.isEmpty()) {
            EstadoBusca atual = fila.poll();

            // Encontrou derivação exata para w
            if (atual.forma.equals(w)) {
                List<PassoGLCDTO> passos = new ArrayList<>();
                passos.add(new PassoGLCDTO(0, atual.formas.get(0), "Símbolo inicial"));
                for (int i = 0; i < atual.regras.size(); i++) {
                    String f = atual.formas.get(i + 1);
                    passos.add(new PassoGLCDTO(i + 1, f.isEmpty() ? "ε" : f, atual.regras.get(i).toString()));
                }

                List<String> formasFormatadas = new ArrayList<>();
                for (String f : atual.formas) {
                    formasFormatadas.add(f.isEmpty() ? "ε" : f);
                }

                NoArvore arvore = montarArvoreSimplificada(atual.regras);
                String resumo = String.join(" => ", formasFormatadas);

                return new SimulacaoGLCResponse(
                        true,
                        "Aceita! Cadeia derivada em " + atual.regras.size() + " passo(s).",
                        resumo,
                        passos,
                        arvore
                );
            }

            if (atual.regras.size() >= maxProfundidade) {
                continue;
            }

            // Encontra o índice da variável mais à esquerda
            int idxVar = encontrarVariavelMaisAEsquerda(atual.forma);
            if (idxVar == -1) {
                continue; // Apenas terminais, mas diferente de w
            }

            // Poda 1: prefixo terminal antes da variável mais à esquerda já deve bater com w
            String prefixoTerminal = atual.forma.substring(0, idxVar);
            if (!w.startsWith(prefixoTerminal)) {
                continue;
            }

            String varEsquerda = String.valueOf(atual.forma.charAt(idxVar));

            for (Producao p : producoes) {
                if (!p.getVariavel().equals(varEsquerda)) {
                    continue;
                }

                String novaForma = atual.forma.substring(0, idxVar) + p.getCorpo() + atual.forma.substring(idxVar + 1);

                // Poda 2: número de terminais já presentes não pode superar |w|
                if (contarTerminais(novaForma) > w.length()) {
                    continue;
                }

                if (visitados.add(novaForma)) {
                    List<String> novasFormas = new ArrayList<>(atual.formas);
                    novasFormas.add(novaForma);

                    List<Producao> novasRegras = new ArrayList<>(atual.regras);
                    novasRegras.add(p);

                    fila.add(new EstadoBusca(novaForma, novasFormas, novasRegras));
                }
            }
        }

        return new SimulacaoGLCResponse(
                false,
                "Rejeitada: não foi possível derivar '" + (w.isEmpty() ? "ε" : w) + "' a partir de " + simboloInicial + ".",
                "",
                Collections.emptyList(),
                null
        );
    }

    private int encontrarVariavelMaisAEsquerda(String forma) {
        for (int i = 0; i < forma.length(); i++) {
            if (variaveis.contains(String.valueOf(forma.charAt(i)))) {
                return i;
            }
        }
        return -1;
    }

    private int contarTerminais(String forma) {
        int count = 0;
        for (int i = 0; i < forma.length(); i++) {
            if (!variaveis.contains(String.valueOf(forma.charAt(i)))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Reconstrói a árvore de derivação seguindo a ordem das regras aplicadas mais à esquerda.
     */
    public NoArvore montarArvoreSimplificada(List<Producao> regrasAplicadas) {
        NoArvore raiz = new NoArvore(simboloInicial);
        Deque<NoArvore> pilhaNaoTerminais = new ArrayDeque<>();
        pilhaNaoTerminais.push(raiz);

        for (Producao p : regrasAplicadas) {
            if (pilhaNaoTerminais.isEmpty()) {
                break;
            }
            NoArvore noAtual = pilhaNaoTerminais.pop();

            if (p.isEpsilon()) {
                noAtual.adicionarFilho(new NoArvore("ε"));
            } else {
                List<NoArvore> filhosCriados = new ArrayList<>();
                for (int i = 0; i < p.getCorpo().length(); i++) {
                    String sym = String.valueOf(p.getCorpo().charAt(i));
                    NoArvore filho = new NoArvore(sym);
                    noAtual.adicionarFilho(filho);
                    filhosCriados.add(filho);
                }
                // Empilha em ordem reversa para que o filho mais à esquerda fique no topo
                for (int i = filhosCriados.size() - 1; i >= 0; i--) {
                    NoArvore f = filhosCriados.get(i);
                    if (variaveis.contains(f.getSimbolo())) {
                        pilhaNaoTerminais.push(f);
                    }
                }
            }
        }

        return raiz;
    }

    private static class EstadoBusca {
        final String forma;
        final List<String> formas;
        final List<Producao> regras;

        EstadoBusca(String forma, List<String> formas, List<Producao> regras) {
            this.forma = forma;
            this.formas = formas;
            this.regras = regras;
        }
    }
}