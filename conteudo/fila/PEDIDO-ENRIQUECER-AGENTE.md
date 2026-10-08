# Enriquecimento visual de matéria publicada (pedido para agente)

Você é professor de cursinho e designer instrucional. Leia `AGENTS.md` e `conteudo/fila/FORMATO-VISUAL.md`
(o formato que o app desenha; siga à risca). Cada matéria que você recebeu está publicada em
`conteudo/materias/<área>/<nome>.json` (id `<área>.<nome>`). O conteúdo está correto, mas a teoria está
em texto corrido: fórmula solta na frase, nenhum gráfico ou figura.

## O que fazer (à mão, no texto da resposta, sem script gerando conteúdo)
1. Reescreva o `markdown` de CADA capítulo, na mesma ordem e com o MESMO `title`:
   - toda fórmula, equação, unidade composta ou símbolo matemático/lógico vira LaTeX entre `$$`;
   - fórmula principal em bloco (`$$` sozinho na linha antes e depois) + lista do que é cada letra, com unidade;
   - de 1 a 3 blocos ```grafico por capítulo quando o assunto tem função, dado, forma, vetor ou processo
     (capítulo puramente conceitual pode ficar sem); números do gráfico batem com o texto;
   - comparações em tabela; exemplos resolvidos numerados, cada conta em LaTeX, resultado em **negrito**;
     alertas de prova com `>`;
   - mantenha TODOS os fatos, números, exemplos e regras; pode reorganizar e acrescentar explicação curta,
     nunca cortar nem mudar valores. O capítulo não pode encolher.
2. Nas explicações das questões com conta ou fórmula, reescreva só a FORMA: contas em LaTeX, passo a passo
   numerado. Não mude números, resolução, letra do gabarito nem conclusão. Em C/E mantenha "Certo." /
   "Errado." no início. Questões sem conta ficam de fora.
3. Refaça cada conta que aparecer (no node, se quiser) e confira que cada figura de geometria tem as medidas
   verdadeiras e todo ponto citado declarado em `pontos`.

## Entrega
Para cada matéria, grave com Write (arquivo inteiro de uma vez) `conteudo/entrada/<id>.visual.json`:
```
{ "id": "<id>", "chapters": [ { "title": "(igual)", "markdown": "..." } ],
  "explanations": [ { "index": 12, "explanation": "..." } ] }
```
- `index` é a posição da questão no array `questions` da matéria publicada, contando do 0.
- Strings JSON: quebra de linha `\n`, aspas `\"`, barra do LaTeX dobrada (`\\frac`).
- Confira com `deno run --allow-read scripts/biblioteca/enriquecer.ts conteudo/entrada/<id>.visual.json`
  (sem `--gravar`!) e corrija até não haver ✗; avisos "?" de números sumidos: confira se foi só mudança de
  forma (ex.: 0,5 virou \\frac{1}{2}) ou recoloque o número.

Não edite `conteudo/materias`. Não faça commit. No fim responda por matéria: id, nº de gráficos/figuras,
nº de fórmulas em bloco, nº de explicações reformatadas e a saída final do enriquecer.ts.
