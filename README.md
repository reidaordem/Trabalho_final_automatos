# Simulador de Linguagens Formais e Modelos Computacionais

Aplicação web educacional desenvolvida em **Java (Spring Boot)** para modelagem, validação léxica/sintática e simulação passo a passo dos principais formalismos da Teoria da Computação: **Linguagens Formais**, **Autômatos Finitos (AFD e AFN)**, **Gramáticas Livres de Contexto (GLC)**, **Autômatos com Pilha (AP)**, **Máquinas de Turing (MT)** e **Análise de Complexidade Assintótica ($O, \Omega, \Theta$)**.

---

## Arquitetura do Projeto

```text
src/main/java/com/br/unifor/maquina_turing/
├── scanner/       # Lexer, Parser descendente recursivo e FormalLexer
├── linguagens/    # Operações formais sobre Σ, L, Σ*, Σ+, |w| e concatenação
├── automatos/     # Simuladores unificados de AFD/AFN e Autômato com Pilha (PilhaAP)
├── gramaticas/    # Motor de derivação mais à esquerda, árvore de derivação e integração nativa
├── turing/        # Fita dinâmica, transições e SimuladorMaquinaTuring
├── dto/           # Objetos de requisição, resposta e registro passo a passo
└── controller/    # Endpoints REST (/api/linguagens, /api/automatos, /api/gramaticas, /api/simular)
```

---

## Como Executar

### Pré-requisitos
- **Java JDK 17** ou superior instalado.
- **Gradle** (ou utilize o Gradle Wrapper `./gradlew` já incluso no repositório).

### Passos para Execução

1. Clone o repositório e acesse a pasta raiz do projeto:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd maquina-turing
   ```

2. Execute a aplicação via Gradle Wrapper:
   - **Linux / macOS:**
     ```bash
     ./gradlew bootRun
     ```
   - **Windows (CMD / PowerShell):**
     ```cmd
     gradlew.bat bootRun
     ```

3. Abra o navegador e acesse:
   ```text
   http://localhost:8080
   ```

---

## Exemplos de Entrada e Saída

### 1. Módulo 1 — Linguagens Formais (`POST /api/linguagens/analisar`)
- **Entrada:**
  - Alfabeto $\Sigma$: `{a, b}`
  - Linguagem $L$: `{ab, ba, abba}`
  - Cadeia 1 ($w_1$): `abba`
  - Cadeia 2 ($w_2$): `ba`
  - Tamanho máximo de fecho: `3`
- **Saída:**
  - **Validação:** Cadeia válida para $\Sigma$ (Tamanho = 4) | Pertence a $L$.
  - **Concatenação ($w_1 \cdot w_2$):** `abbaba` (Tamanho = 6)
  - **Fecho de Kleene ($\Sigma^*$):** `ε, a, b, aa, ab, ba, bb, aaa, aab, aba, abb, baa, bab, bba, bbb`
  - **Fecho Positivo ($\Sigma^+$):** `a, b, aa, ab, ba, bb, aaa, aab, aba, abb, baa, bab, bba, bbb`

### 2. Módulo 2 — Autômato Finito (`POST /api/automatos/finito/simular`)
- **Entrada (JSON — AFD):**
  ```json
  {
    "estados": ["q0", "q1", "q2"],
    "alfabeto": ["a", "b"],
    "estadoInicial": "q0",
    "estadosFinais": ["q2"],
    "transicoes": [
      { "estadoOrigem": "q0", "simbolo": "a", "estadoDestino": "q1" },
      { "estadoOrigem": "q1", "simbolo": "b", "estadoDestino": "q2" }
    ],
    "cadeia": "ab"
  }
  ```
- **Saída:**
  - **Veredito:** `ACEITA (AFD)` | Estados finais ativos: `q2`
  - **Caminho:** `q0 -> q1 -> q2` (acompanhado do grafo SVG interativo passo a passo).

### 3. Módulo 3 — Gramática Livre de Contexto (`POST /api/gramaticas/simular` e `/nativa`)
- **Exemplo A — GLC Customizada:**
  - Variáveis ($V$): `S` | Terminais ($\Sigma$): `a, b` | Inicial: `S`
  - Produções ($R$): `S -> aSb` e `S -> ε`
  - Cadeia: `aaabbb`
  - **Saída:** Aceita! Cadeia derivada em 4 passo(s): `S => aSb => aaSbb => aaaSbbb => aaabbb` + Árvore de derivação simplificada.
- **Exemplo B — Scanner / Parser Nativo ($S \to \text{ID} = E$):**
  - Entrada: `z = (x + y) * 2`
  - **Saída:** `EXPRESSÃO VÁLIDA` pelo Lexer/Parser nativo (9 tokens reconhecidos).

### 4. Módulo 4 — Autômato com Pilha (`POST /api/automatos/pilha/simular`)
- **Entrada:**
  - Linguagem: $L = \{a^n b^n \mid n \ge 1\}$ ($Q = \{q_0, q_1, q_2\}$, $\Sigma = \{a, b\}$, $Z_0 = Z$, $F = \{q_2\}$)
  - Cadeia: `aaabbb`
- **Saída:**
  - **Veredito:** `ACEITA!` Cadeia reconhecida pelo Autômato com Pilha no estado final $q_2$.
  - **Rastreamento:** Exibe para cada passo a leitura (`a`, `b`, `ε`), o estado atual e o conteúdo da pilha LIFO (`[Z] -> [A, Z] -> [A, A, Z] -> [A, A, A, Z] -> ... -> [Z]`).

### 5. Módulo 5 — Máquina de Turing (`POST /api/simular`)
- **Opção B — Soma Unária ($3 + 2 = 5$):**
  - Fita Inicial: `000100` (`estadoInicial = q0`, `estadoFinal = q3`)
  - Fita Final: `B00000BB` (Cadeia aceita com sucesso!)
- **Opção A — Reconhecimento de $L = \{w\#w\}$:**
  - Fita Inicial: `ab#ab` (`estadoInicial = q0`, `estadoFinal = q8`)
  - Fita Final: `BXY#XYBB` (Cadeia aceita com sucesso!)