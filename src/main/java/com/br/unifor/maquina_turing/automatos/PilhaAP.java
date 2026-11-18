package com.br.unifor.maquina_turing.automatos;

import com.br.unifor.maquina_turing.scanner.ParseException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PilhaAP {

    private final Deque<Character> dados = new ArrayDeque<>();

    public PilhaAP() {}

    private PilhaAP(Deque<Character> origem) {
        this.dados.addAll(origem);
    }

    /**
     * Operação PUSH: insere um símbolo no topo da pilha.
     */
    public void empilhar(char simbolo) {
        dados.push(simbolo);
    }

    /**
     * Operação POP: remove e retorna o símbolo do topo da pilha.
     */
    public char desempilhar() {
        if (dados.isEmpty()) {
            throw new ParseException("Erro de execução: tentativa de desempilhar de uma pilha vazia.");
        }
        return dados.pop();
    }

    public Character topo() {
        return dados.peek();
    }

    public boolean estaVazia() {
        return dados.isEmpty();
    }

    public PilhaAP copiar() {
        return new PilhaAP(this.dados);
    }

    /**
     * Retorna os elementos ordenados do topo para a base (para renderização visual).
     */
    public List<String> listarDoTopoParaBase() {
        List<String> lista = new ArrayList<>();
        for (Character c : dados) {
            lista.add(String.valueOf(c));
        }
        return lista;
    }

    public String conteudoFormatado() {
        if (dados.isEmpty()) {
            return "∅ (vazia)";
        }
        List<String> itens = listarDoTopoParaBase();
        return "[" + String.join(", ", itens) + "] (topo: " + dados.peek() + ")";
    }
}