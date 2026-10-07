# Fila de matérias (o que falta gerar e consertar)

Atualizado em 06/10/2026. Biblioteca publicada: 373 matérias e 171 recortes.

**Agora (06/10):** `07-editais-200.md` (25 blocos de 8 editais para o Codex montar o catálogo) e
`08-consertar.md` (8 blocos com 38 matérias do Codex que precisam de conserto). Triagem de
`conteudo/entrada`: `deno run --allow-read conteudo/fila/triagem.ts`.

Ordem combinada:
1. `01-pm-acabamento.md` — 10 matérias de PM já geradas, só falta consertar o gabarito (Claude faz à mão).
2. `02-pmmg.md` — 15 matérias que faltam para cobrir o edital da PMMG (Soldado) inteiro (Codex gera).
3. `03-trt-restante.md` — 77 matérias do TRT que ainda não existem (Codex gera).

## Como mandar um bloco para o Codex (copiar e colar, trocando ARQUIVO e BLOCO)

    Leia o AGENTS.md e o pedido C:\Users\otavi\Documents\Codex\2026-09-15\vc-x20\conteudo\fila\ARQUIVO (regras, formato e lista). Faça SÓ o BLOCO, com os ids e escopos exatos do pedido. Grave cada uma em vc-x20/conteudo/entrada/<nome>.json, em UTF-8 SEM BOM. Para conferir cada arquivo rode: deno run --allow-read conteudo/fila/conferir.ts <arquivo>. Ler e editar arquivos à mão é permitido. Uma matéria por vez, usando a ferramenta de editar arquivos (não comando longo do PowerShell); se o conferidor apontar problema, CORRIJA em vez de descartar. Se depois de 2 tentativas o único problema for gabarito concentrado ou certa mais longa, grave assim mesmo e siga. Não pare no meio e não peça confirmação. No fim, diga quais gravou.

## Regras que o Codex deve seguir (já estão no AGENTS.md e nos pedidos)
- Matéria NEUTRA: sem banca, sem órgão ou estado como contexto do aluno (o estilo da banca vai só no recorte).
- 40 a 50 questões: cerca de 25 de múltipla escolha (A–E) e 20 de Certo/Errado; 8+ fáceis, 8+ médias, 8+ difíceis.
- Gabarito espalhado entre A e E; a certa não pode ser sempre a mais longa; uma só correta.
- Não inventar número de lei, artigo, prazo ou pena: se não tiver certeza, explicar sem o número.
- status "PUBLISHED", version 1, theoryTitle preenchido, board/agency null.

## Como publicar (Claude)
1. Copiar de `conteudo/entrada` para uma pasta de trabalho, tirar BOM, rodar `conferir.ts`, procurar explicação que admite "duas certas" e letra da explicação diferente do gabarito.
2. `deno run --allow-read --allow-write scripts/biblioteca/receber.ts <pasta> <arquivos>`
3. `deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts` até não ter nenhum ✗ (apelido repetido: tirar da matéria ANTIGA e subir a versão dela; depois reapontar os editais).
4. Conferir que todas estão "PUBLISHED" (o servidor não entrega "REVIEWED").
5. `bash scripts/biblioteca/publicar.sh` e commit + push.

## Arquivos de EDITAL (catálogo de concursos)

Regras e formato em `PEDIDO-EDITAIS.md`; a lista de concursos fica em `editais-lista.md`. O agente
grava `conteudo/entrada/edital-<nome>.json` com todo tópico em `"topico": null` e confere com
`deno run --allow-read conteudo/fila/conferir-edital.ts <arquivo>`. O Claude confere com o edital
oficial, separa assuntos que ficaram juntos, liga cada tópico à matéria (ou deixa null para gerar),
move para `conteudo/editais/` sem o prefixo `edital-` e publica.
