package com.br.unifor.maquina_turing.linguagens;

import com.br.unifor.maquina_turing.dto.LinguagemRequest;
import com.br.unifor.maquina_turing.dto.LinguagemResponse;
import com.br.unifor.maquina_turing.scanner.FormalLexer;
import com.br.unifor.maquina_turing.scanner.ParseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LinguagemService {

    private final FormalLexer formalLexer;

    public LinguagemService(FormalLexer formalLexer) {
        this.formalLexer = formalLexer;
    }

    public LinguagemResponse executarOperacoes(LinguagemRequest req) {
        // 1. Tokeniza Σ usando o Scanner
        Set<Character> sigma = formalLexer.tokenizarAlfabeto(req.getAlfabeto());

        // 2. Instancia o modelo formal (Σ, L)
        LinguagemFormal linguagem = new LinguagemFormal(sigma, req.getDefinicaoLinguagem());

        String w1 = (req.getCadeia1() != null) ? req.getCadeia1().trim() : "";
        String w2 = (req.getCadeia2() != null) ? req.getCadeia2().trim() : "";
        int maxTam = (req.getTamanhoMaximoFecho() != null) ? req.getTamanhoMaximoFecho() : 3;

        List<String> fechoKleene = linguagem.gerarFechoKleene(maxTam);
        List<String> fechoPositivo = linguagem.gerarFechoPositivo(maxTam);

        // 3. Valida w1 em Σ* usando o FormalLexer
        try {
            formalLexer.validarCadeiaNoAlfabeto(w1, sigma);
            if (!w2.isEmpty()) {
                formalLexer.validarCadeiaNoAlfabeto(w2, sigma);
            }
        } catch (ParseException e) {
            return new LinguagemResponse(
                    false,
                    false,
                    "Cadeia inválida para Σ: " + e.getMessage(),
                    linguagem.getAlfabetoFormatado(),
                    linguagem.getDescricaoLinguagem(),
                    linguagem.calcularTamanho(w1),
                    "",
                    0,
                    fechoKleene,
                    fechoPositivo
            );
        }

        int tamanhoW1 = linguagem.calcularTamanho(w1);
        String concatenada = linguagem.concatenar(w1, w2);
        int tamanhoConcat = linguagem.calcularTamanho(concatenada);
        boolean pertenceL = linguagem.pertenceALinguagem(w1);

        String msg = "Cadeia válida para Σ (Tamanho = " + tamanhoW1 + ")"
                + (pertenceL ? " | Pertence a L." : " | Não pertence a L.");

        return new LinguagemResponse(
                true,
                pertenceL,
                msg,
                linguagem.getAlfabetoFormatado(),
                linguagem.getDescricaoLinguagem(),
                tamanhoW1,
                concatenada,
                tamanhoConcat,
                fechoKleene,
                fechoPositivo
        );
    }
}