package com.br.unifor.maquina_turing.gramaticas;

import com.br.unifor.maquina_turing.dto.GLCRequest;
import com.br.unifor.maquina_turing.dto.PassoGLCDTO;
import com.br.unifor.maquina_turing.dto.SimulacaoGLCResponse;
import com.br.unifor.maquina_turing.scanner.FormalLexer;
import com.br.unifor.maquina_turing.scanner.Lexer;
import com.br.unifor.maquina_turing.scanner.ParseException;
import com.br.unifor.maquina_turing.scanner.Parser;
import com.br.unifor.maquina_turing.scanner.Token;
import com.br.unifor.maquina_turing.scanner.TokenType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GramaticaService {

    private final FormalLexer formalLexer;
    private final Lexer lexerNativo = new Lexer();

    public GramaticaService(FormalLexer formalLexer) {
        this.formalLexer = formalLexer;
    }

    public SimulacaoGLCResponse simularGLC(GLCRequest req) {
        GramaticaLivreContexto glc = GramaticaLivreContexto.deRequest(req, formalLexer);
        return glc.derivar(req.getCadeia(), 40, formalLexer);
    }

    /**
     * Executa diretamente o Lexer e o Parser descendente recursivo do professor
     * para expressões de atribuição: S -> ID = E
     */
    public SimulacaoGLCResponse validarExpressaoNativa(String expressao) {
        if (expressao == null || expressao.trim().isEmpty()) {
            throw new ParseException("Informe uma expressão aritmética (ex: z = (x + y) * 2).");
        }

        List<Token> tokens;
        try {
            tokens = lexerNativo.tokenSize(expressao);
        } catch (IllegalArgumentException e) {
            return new SimulacaoGLCResponse(
                    false,
                    "Erro Léxico (Scanner): " + e.getMessage(),
                    "",
                    Collections.emptyList(),
                    null
            );
        }

        try {
            Parser parser = new Parser(tokens);
            parser.parse();
        } catch (ParseException e) {
            return new SimulacaoGLCResponse(
                    false,
                    "Erro Sintático (Parser): " + e.getMessage(),
                    "",
                    Collections.emptyList(),
                    null
            );
        }

        // Monta passos de análise e árvore sintática simplificada da expressão validada
        List<PassoGLCDTO> passos = new ArrayList<>();
        passos.add(new PassoGLCDTO(0, "S", "Início da descida recursiva (Parser do Professor)"));
        passos.add(new PassoGLCDTO(1, "ID = E", "Regra S -> ID = E (" + tokens.get(0).getLexeme() + " =)"));

        NoArvore raiz = new NoArvore("S");
        raiz.adicionarFilho(new NoArvore("ID (" + tokens.get(0).getLexeme() + ")"));
        raiz.adicionarFilho(new NoArvore("ASSIGN (=)"));

        NoArvore noExpr = new NoArvore("E");
        StringBuilder sbTokens = new StringBuilder();
        for (int i = 2; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.getType() == TokenType.EOF) break;
            noExpr.adicionarFilho(new NoArvore(t.getType() + " ('" + t.getLexeme() + "')"));
            sbTokens.append(t.getLexeme()).append(" ");
        }
        raiz.adicionarFilho(noExpr);

        String exprLimpa = sbTokens.toString().trim();
        passos.add(new PassoGLCDTO(2, tokens.get(0).getLexeme() + " = " + exprLimpa, "E -> T ((+|-) T)* | T -> F ((*|/) F)* | F -> (E)|ID|NUM"));

        return new SimulacaoGLCResponse(
                true,
                "EXPRESSÃO VÁLIDA pelo Lexer/Parser nativo (" + (tokens.size() - 1) + " tokens reconhecidos).",
                "S => ID = E => " + tokens.get(0).getLexeme() + " = " + exprLimpa,
                passos,
                raiz
        );
    }
}