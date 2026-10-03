# Biblioteca de matérias — instruções para o gerador (Codex)

Esta pasta é a fonte da biblioteca do Estudário. Tudo aqui vira matéria pronta no app, entregue
sem custo de IA. Leia este arquivo inteiro antes de gerar qualquer coisa.

## As três peças

| Pasta | O que é | Pode citar concurso/banca? |
|---|---|---|
| `materias/<disciplina>/<topico>.json` | A matéria canônica de um tópico, igual para todo concurso | **Não** |
| `recortes/<banca>/<topico>[--<cargo>].json` | "Como cai" aquele tópico numa banca (e, se preciso, num cargo) | Sim, só a dela |
| `editais/<concurso>-<ano>.json` | Os tópicos de um edital, cada um ligado a uma matéria | Sim |

Assim, quem estuda para bombeiro recebe a matéria canônica mais o recorte **dele**. Nunca o da PM.

## Fluxo quando chegar um edital

1. Leia o edital e escreva `editais/<concurso>-<ano>.json` (formato abaixo).
2. Para cada tópico, procure em `materias/` uma matéria que já cubra aquele conteúdo (veja `title`
   e `aliases`). Se existir, ligue o tópico a ela. **Não crie uma matéria duplicada.** Um tópico do
   edital que junta vários assuntos pode ser ligado à matéria principal.
3. Se não existir, deixe `"topico": null` por enquanto e crie a matéria em `materias/` depois.
4. Crie os recortes da banca do concurso para os tópicos (`recortes/`), se ainda não existirem.
5. Rode o conferidor (abaixo). Corrija tudo o que ele apontar.

## Regras de qualidade (o conferidor recusa o que fugir delas)

- **Nada genérico.** Explique o conteúdo de verdade, com exemplos resolvidos, fórmulas, artigos de
  lei citados pelo número, exceções e casos que a banca adora. Proibido "TODO", "a ser preenchido",
  "[inserir]", "lorem ipsum".
- **Teoria:** 2 a 6 capítulos, cada um com pelo menos 800 caracteres e, somando, pelo menos 6.000.
  Markdown simples: títulos `###`, listas, **negrito**, tabelas e exemplos.
- **Resumo:** pelo menos 400 caracteres.
- **Flashcards:** 12 a 30, um conceito por cartão, frente curta, sem frentes repetidas.
- **Dicas e pegadinhas:** 3 a 8 de cada. **Recordação ativa:** 5 a 10 perguntas com resposta.
- **Conceitos de erro (`errorConcepts`):** 2 a 6, chaves `e1`…`e6`. São os erros típicos de quem
  estuda o tópico; as questões apontam para eles.
- **Banco de questões: pelo menos 40** (o ideal é 60 a 100), com:
  - pelo menos 15 Certo/Errado e 15 de cinco alternativas (A–E); quatro alternativas (A–D) é opcional;
  - pelo menos 8 fáceis, 8 médias e 8 difíceis;
  - exatamente uma alternativa correta e gabarito bem distribuído entre as letras (nenhuma letra
    com mais de 40% das respostas);
  - distratores plausíveis, que pegam quem tem o erro típico, nunca absurdos;
  - explicação de pelo menos 120 caracteres que diz por que a certa está certa **e** por que as
    outras estão erradas;
  - `section` igual ao título exato de um capítulo.
- **Nível de prova, não de exercício.** Compare cada questão com provas reais da banca. Enunciado
  de uma linha com distrator óbvio ("o ônibus causou a chuva") não serve.
  - **Interpretação de texto** sempre traz um texto-base de verdade (de 8 a 25 linhas, no
    enunciado), e de 3 a 5 questões trabalham o mesmo texto: ideia central, inferência, sentido de
    palavra no contexto, referência de pronome, reescrita que mantém o sentido.
  - Distrator bom é o que um candidato preparado quase marca: extrapola um pouco, troca
    "alguns" por "todos", inverte causa e efeito, usa palavra do texto com sentido errado.
- **Sem pista no tamanho.** A alternativa certa não pode ser quase sempre a mais longa (nem a mais
  curta). Escreva as alternativas com tamanho parecido; o conferidor recusa se a certa for a mais
  longa ou a mais curta em mais de 40% das questões.
- **Nada de rabicho de molde nas alternativas.** Não cole a mesma expressão no fim de várias
  alternativas para igualar tamanho (", no contexto do relato", ", segundo a passagem
  apresentada"…). Iguale o tamanho com conteúdo de verdade. O conferidor recusa a expressão final
  que se repetir em mais de 4 alternativas (ou 3% delas).
- **Varie a pergunta.** A mesma pergunta ("Qual síntese preserva a ideia central…") em mais de 20%
  das questões é recusada.
- **Revise o português depois de qualquer troca automática.** Frase quebrada como "qualquer o
  desmatamento" é recusada.
- **Explicação própria em cada questão.** Nada de frase de molde colada em todas ("A alternativa
  indicada é a única compatível…"). O conferidor recusa a frase que se repetir em mais de 15% das
  explicações.
- **Questões reais de prova:** prefira questões autorais no estilo das bancas (`"sourceType":
  "AUTHORIAL"`, com `board`, `agency`, `year` e `sourceUrl` em `null`). Só use `"REAL"` com link,
  banca e ano verdadeiros e conferidos.
- **Fontes:** pelo menos uma, com link real (lei no Planalto, norma oficial, livro de referência).
  `accessedAt` no formato `AAAA-MM-DD`.
- **Confira tudo.** Resolva cada questão você mesmo antes de gravar. Confira números de artigos e
  a redação vigente da lei. Em Português, cuidado redobrado com gabarito ambíguo.

## Formato da matéria (`materias/matematica/funcao-1-grau.json`)

```json
{
  "id": "matematica.funcao-1-grau",
  "subject": "Matemática",
  "title": "Função do 1º grau",
  "version": 1,
  "status": "PUBLISHED",
  "aliases": ["Função afim", "Funções de 1º grau", "Função polinomial do 1º grau"],
  "scope": { "covers": "Definição, gráfico, coeficientes, raiz, estudo do sinal e problemas.", "excludes": "Função quadrática." },
  "theoryTitle": "Função do 1º grau",
  "chapters": [{ "title": "Definição e coeficientes", "markdown": "..." }],
  "summary": "...",
  "flashcards": [{ "front": "...", "back": "..." }],
  "tips": ["..."],
  "traps": ["..."],
  "activeRecall": [{ "question": "...", "answer": "..." }],
  "errorConcepts": [{ "key": "e1", "title": "...", "summary": "..." }],
  "questions": [
    {
      "statement": "...",
      "format": "MULTIPLE_CHOICE",
      "difficulty": "MEDIA",
      "options": [
        { "key": "A", "text": "...", "correct": false },
        { "key": "B", "text": "...", "correct": true },
        { "key": "C", "text": "...", "correct": false },
        { "key": "D", "text": "...", "correct": false },
        { "key": "E", "text": "...", "correct": false }
      ],
      "explanation": "...",
      "section": "Definição e coeficientes",
      "errorConceptKey": "e1",
      "sourceType": "AUTHORIAL",
      "board": null,
      "agency": null,
      "year": null,
      "sourceUrl": null
    }
  ],
  "sources": [{ "kind": "OFICIAL", "title": "...", "publisher": "...", "reference": "", "url": "https://...", "accessedAt": "2026-10-03" }]
}
```

- `id`: `disciplina.topico`, minúsculas, sem acento, palavras separadas por hífen. Nunca mude o id
  de uma matéria publicada.
- Certo/Errado: `"format": "TRUE_FALSE"` e opções `C` (`"Certo"`) e `E` (`"Errado"`).
- `aliases`: outros nomes com que editais chamam este tópico. Um apelido pertence a uma matéria só.
- `status`: `DRAFT` (rascunho, não aparece no app), `REVIEWED` ou `PUBLISHED` (aparece no app).
- **Ao melhorar uma matéria publicada, aumente `version`.**

## Formato do recorte (`recortes/vunesp/matematica.funcao-1-grau.json`)

```json
{
  "topic": "matematica.funcao-1-grau",
  "board": "VUNESP",
  "role": null,
  "version": 1,
  "incidence": "ALTA",
  "howItFalls": "Como esta banca cobra o tópico: tipo de enunciado, o que mais aparece, pegadinhas recorrentes, nível de profundidade e o que dá para deixar de lado. Mínimo de 300 caracteres.",
  "tips": ["..."],
  "traps": ["..."],
  "questions": []
}
```

- `role`: `null` vale para qualquer cargo da banca. Use um cargo (`"Soldado PM"`) só quando a
  cobrança muda de verdade para ele; aí o nome do arquivo ganha `--soldado-pm`.
- `incidence`: `ALTA`, `MEDIA` ou `BAIXA`.
- `questions`: opcional, questões no estilo da banca, nas mesmas regras das da matéria. Elas são
  entregues antes das da matéria.

## Formato do edital (`editais/pm-sp-soldado-2026.json`)

```json
{
  "concurso": "PM-SP",
  "cargo": "Soldado",
  "banca": "VUNESP",
  "ano": 2026,
  "url": "https://...",
  "disciplinas": [
    {
      "nome": "Matemática",
      "topicos": [
        { "texto": "Função do 1º grau: gráfico e aplicações", "topico": "matematica.funcao-1-grau" },
        { "texto": "Geometria espacial", "topico": null }
      ]
    }
  ]
}
```

- `texto`: copie o tópico **exatamente como está no edital**. Ele vira apelido da matéria, e é
  assim que o app reconhece o tópico quando alguém importa esse edital.

## Conferir e publicar

```bash
deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts
```

O comando lista os problemas por arquivo e os tópicos de edital ainda sem matéria. Só publique
quando ele disser "Tudo certo". A publicação é feita pelo dono do projeto, com `--publicar` e as
chaves do Supabase no ambiente.

Arquivos que começam com `_` são ignorados (use para rascunhos).
