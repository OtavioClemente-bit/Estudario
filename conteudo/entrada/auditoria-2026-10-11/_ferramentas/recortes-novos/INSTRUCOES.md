# Escrever recortes "Como a prova cobra"

Você escreve, À MÃO, os recortes do seu grupo (`grupo-NN.txt`). Para cada `recorte-NNN` da lista:
1. Leia o pedido em `pedidos/recorte-NNN.md` (matéria, escopo, bancas na ordem).
2. Leia a matéria correspondente para conhecer o conteúdo:
   `/home/user/Estudario/conteudo/entrada/auditoria-2026-10-11/materias/<id>.json` (capítulos, dicas, pegadinhas).
3. Escreva o arquivo inteiro de uma vez, com a ferramenta Write, em
   `/home/user/Estudario/conteudo/entrada/auditoria-2026-10-11/recortes/recorte-NNN.json`.
4. Confira (comando abaixo) e corrija o que aparecer.

## Regras do projeto (AGENTS.md — obrigatórias)
- Escreva cada texto você mesmo. PROIBIDO usar script, código, laço ou modelo para gerar, completar ou variar textos.
  O terminal serve só para conferir.
- Grave o arquivo inteiro de uma vez, só quando estiver pronto. Não mexa em nada fora dos seus arquivos. Sem commit.
- Se não tiver certeza de número de lei, artigo, prazo ou súmula, NÃO cite o número: explique o conteúdo. Nunca invente norma.

## O que é
É a última parte da matéria no app: o professor de cursinho explica como a banca DO ALUNO cobra exatamente este assunto.
O recorte "GERAL" (Padrão das provas) é usado quando o concurso não tem banca ou a banca não tem recorte.

## Para CADA banca da lista, na ordem, e o GERAL por último
- `incidence`: ALTA, MEDIA ou BAIXA (na dúvida, MEDIA; sem estatística nem percentual).
- `howItFalls` (1.000 a 2.000 caracteres): texto corrido do professor — formato do item (Certo/Errado, A–E, A–D), jeito do
  enunciado (caso concreto, letra de lei, texto-base, cálculo), pontos do assunto mais explorados, profundidade, o que derruba
  o candidato e o que pode ter menos peso. Inclua 1 ou 2 exemplos curtos de COMO o item aparece, com o raciocínio
  ("um item típico descreve … e afirma que …, o que está errado porque …"), sem montar questão com alternativas.
- `tips` (4 a 6): dicas acionáveis e específicas deste assunto nessa banca.
- `traps` (4 a 6): "como a prova escreve → por que está errado → versão certa". SEM letra/número no começo.
- `questions`: sempre `[]`.

## Qualidade
- Específico do assunto; nada que serviria para qualquer matéria ("exige atenção", "leia com calma").
- Cada banca com cara própria (formato, profundidade, pontos preferidos). Se dois recortes ficaram trocáveis, reescreva.
  Não repita a mesma pegadinha, dica ou exemplo em duas bancas; o que é comum vai só no GERAL.
- GERAL não cita nome de banca.
- HONESTIDADE: nunca diga que a banca "já cobrou", "há prova/questão da banca", nem cite prova, ano ou estatística.
  Diga como ela COSTUMA cobrar. Banca pequena que você não conhece bem: baseie-se no formato e perfil dela, sem fingir
  certeza, e use incidence MEDIA ou BAIXA.
- Não cite nome de concurso, órgão ou cargo (os exemplos de público no pedido são só para calibrar).

## Formato (JSON válido; use aspas tipográficas “ ” dentro de textos)
[
  { "topic": "<id da matéria>", "board": "<nome exatamente como na lista; no padrão: GERAL>", "role": null, "version": 1,
    "incidence": "MEDIA", "howItFalls": "...", "tips": ["..."], "traps": ["..."], "questions": [] }
]

## Conferir
cd /home/user/Estudario && node --experimental-transform-types --no-warnings conteudo/entrada/auditoria-2026-10-11/_ferramentas/conferir.mts conteudo/entrada/auditoria-2026-10-11/recortes/recorte-NNN.json
Além do "ok", confira você mesmo: bancas na ordem do pedido + GERAL, howItFalls 1.000–2.000, 4–6 tips e traps.

## Ao terminar
Responda em até 8 linhas: quais recortes gravou, se todos passaram no conferidor e qualquer dúvida de conteúdo.
