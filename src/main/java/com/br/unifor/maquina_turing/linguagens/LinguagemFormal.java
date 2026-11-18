package com.br.unifor.maquina_turing.linguagens;

import com.br.unifor.maquina_turing.scanner.ParseException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class LinguagemFormal {

    private final Set<Character> alfabeto;
    private final String descricaoLinguagem;
    private final Set<String> palavrasExplicitas; // Preenchido se L for definida por enumeração: {a, ab, ba}

    public LinguagemFormal(Set<Character> alfabeto, String descricaoLinguagem) {
        if (alfabeto == null || alfabeto.isEmpty()) {
            throw new ParseException("O alfabeto Σ não pode ser vazio.");
        }
        this.alfabeto = Collections.unmodifiableSet(new LinkedHashSet<>(alfabeto));
        this.descricaoLinguagem = (descricaoLinguagem == null || descricaoLinguagem.trim().isEmpty())
                ? "L = Σ*"
                : descricaoLinguagem.trim();
        this.palavrasExplicitas = extrairPalavrasSeEnumerada(this.descricaoLinguagem);
    }

    /**
     * Se o usuário definir L por extensão, ex: "{a, ab, ba}",
     * valida teoricamente se L ⊆ Σ* (toda palavra de L usa apenas símbolos de Σ).
     */
    private Set<String> extrairPalavrasSeEnumerada(String def) {
        if (def.startsWith("{") && def.endsWith("}")) {
            String miolo = def.substring(1, def.length() - 1).trim();
            Set<String> palavras = new LinkedHashSet<>();
            if (miolo.isEmpty()) {
                return palavras; // Linguagem vazia: L = ∅
            }
            for (String item : miolo.split(",")) {
                String w = item.trim();
                if (w.equals("ε") || w.equals("lambda") || w.equals("&")) {
                    palavras.add("");
                } else {
                    if (!validarCadeia(w)) {
                        throw new ParseException("Definição inválida: a palavra '" + w + "' de L não pertence a Σ* (" + alfabeto + ").");
                    }
                    palavras.add(w);
                }
            }
            return palavras;
        }
        return null; // Linguagem definida por regra/descrição intensional
    }

    /**
     * Verifica se w ∈ Σ*
     */
    public boolean validarCadeia(String cadeia) {
        if (cadeia == null || cadeia.isEmpty()) {
            return true; // ε ∈ Σ*
        }
        for (int i = 0; i < cadeia.length(); i++) {
            if (!alfabeto.contains(cadeia.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se w ∈ L
     */
    public boolean pertenceALinguagem(String cadeia) {
        String w = (cadeia == null) ? "" : cadeia;
        if (!validarCadeia(w)) {
            return false;
        }
        // Se L foi definida como conjunto finito {w1, w2, ...}, verifica pertinência exata
        if (palavrasExplicitas != null) {
            return palavrasExplicitas.contains(w);
        }
        // Caso contrário, L = Σ* (ou descrição livre sobre Σ*)
        return true;
    }

    /**
     * Calcula |w|
     */
    public int calcularTamanho(String cadeia) {
        return (cadeia == null) ? 0 : cadeia.length();
    }

    /**
     * Calcula w1 · w2
     */
    public String concatenar(String w1, String w2) {
        String s1 = (w1 == null) ? "" : w1;
        String s2 = (w2 == null) ? "" : w2;
        return s1 + s2;
    }

    /**
     * Gera Σ* = {ε} ∪ Σ^1 ∪ Σ^2 ∪ ... ∪ Σ^maxTam em ordem lexicográfica (via BFS).
     */
    public List<String> gerarFechoKleene(int maxTam) {
        int limite = Math.max(0, Math.min(maxTam, 5)); // Trava de segurança (crescimento exponencial |Σ|^k)
        List<String> resultado = new ArrayList<>();
        resultado.add("ε");

        if (limite == 0) {
            return resultado;
        }

        Queue<String> fila = new ArrayDeque<>();
        fila.add("");

        while (!fila.isEmpty()) {
            String atual = fila.poll();
            if (atual.length() >= limite) {
                continue;
            }
            for (char simbolo : alfabeto) {
                String novaCadeia = atual + simbolo;
                resultado.add(novaCadeia);
                fila.add(novaCadeia);
                if (resultado.size() >= 500) {
                    return resultado;
                }
            }
        }
        return resultado;
    }

    /**
     * Gera Σ+ = Σ* - {ε} = Σ^1 ∪ Σ^2 ∪ ... ∪ Σ^maxTam.
     */
    public List<String> gerarFechoPositivo(int maxTam) {
        List<String> kleene = gerarFechoKleene(Math.max(1, maxTam));
        if (!kleene.isEmpty() && "ε".equals(kleene.get(0))) {
            return new ArrayList<>(kleene.subList(1, kleene.size()));
        }
        return kleene;
    }

    public String getAlfabetoFormatado() {
        StringBuilder sb = new StringBuilder("{");
        int count = 0;
        for (char c : alfabeto) {
            sb.append(c);
            if (++count < alfabeto.size()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public String getDescricaoLinguagem() {
        return descricaoLinguagem;
    }
}