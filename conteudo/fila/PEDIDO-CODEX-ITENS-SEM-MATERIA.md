# Pedido ao Codex: itens de edital que ainda não têm matéria (tarefa longa, em sequência)

## Para que serve

Quando o aluno escolhe um edital, cada item do conteúdo programático precisa apontar para uma matéria da biblioteca.
Hoje 8,6% dos itens estão sem matéria. A lista `conteudo/fila/itens-sem-materia.tsv` traz os **700 itens sem matéria
que mais se repetem** (cada um aparece em 8 a 166 editais), do mais comum para o menos comum.

## Mensagem para colar no Codex

```
Leia AGENTS.md e conteudo/fila/PEDIDO-CODEX-ITENS-SEM-MATERIA.md e trabalhe a lista conteudo/fila/itens-sem-materia.tsv
em sequência, da linha 1 até o fim. Para cada item, grave a decisão (e a matéria nova, se for o caso) antes de passar
ao próximo. Pule itens que já tenham decisão gravada. Não pare para perguntar; se o limite acabar, eu peço para continuar.
```

## Para cada item da lista (linha n = 1, 2, 3...)

Decida UMA de três coisas e grave `conteudo/entrada/itens-sem-materia/<n>.json` (n com 3 dígitos: 001, 002...):

```json
{ "n": 1, "texto": "texto do item, igual à lista", "decisao": "ligar | materia-nova | ignorar",
  "id": "id da matéria (para ligar ou materia-nova; null para ignorar)", "motivo": "uma frase" }
```

1. **ligar** — já existe matéria que cobre o item. Procure em `conteudo/fila/indice-materias.tsv` (id, título e o que cobre)
   e, se precisar, abra a matéria em `conteudo/materias/<area>/<arquivo>.json` para confirmar. Só ligue se a matéria
   realmente ensina aquilo que o item pede. Item amplo (ex.: "Constituição Federal de 1988") liga na matéria mais geral
   que existir do assunto; se não houver uma geral, vire matéria nova.
2. **materia-nova** — nenhuma matéria cobre o item e ele é conteúdo que se estuda (ex.: "Tuberculose", "Macros" do Excel,
   "Regime pluviométrico", "Gestão democrática" na educação). Escreva a matéria completa (abaixo) e use o id dela.
   Se um item posterior da lista for o mesmo assunto, ligue-o à matéria que você criou.
3. **ignorar** — não é conteúdo de estudo: referência bibliográfica solta, nome de autor, título de capítulo vazio
   ("Capítulo I - Das Disposições Gerais"), pedaço de frase cortada, "Notícias do site da Prefeitura", atualidades do dia.

## Matéria nova (quando for o caso)

- Grave em `conteudo/entrada/materias-codex-2/<area>.<arquivo>.json`, com a MESMA estrutura de uma matéria pronta
  (veja `conteudo/materias/informatica/flyway-migracao-banco.json` como modelo): id, subject, title, version 1,
  status "PUBLISHED", aliases (8 a 15, cada um com 10+ caracteres, incluindo o texto do item), scope, theoryTitle,
  4 a 6 chapters (estilo didático: "### O problema que este assunto resolve", quadro "> **Siglas e termos deste capítulo**"
  com lista, tabela quando houver comparação, "### Como cai na prova"), summary, 20+ flashcards, 6 tips, 6 traps,
  8 activeRecall, 4 a 5 errorConcepts, pelo menos 45 questões (15+ Certo/Errado com metade Certo e metade Errado,
  25+ de 5 alternativas; 10+ FACIL e 10+ DIFICIL) e sources oficiais reais.
- Escreva em pedaços se precisar (capítulos num arquivo, questões em blocos de 10) e junte no arquivo final; arquivo
  pequeno e completo é permitido pelo AGENTS.md.
- Questões: uma única correta; as cinco alternativas no mesmo formato e tamanho parecido (a correta nem a mais longa
  nem a mais curta em mais de 30%); nada de "conforme a matéria"; explicação sem citar letra; erros plausíveis.
- Saúde, lei e números: se não tiver certeza de dose, prazo, artigo ou valor, explique o conceito sem o número.
  Nunca invente norma.
- Valide até dar OK: `deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts <arquivo>`.
- Antes de criar, confira se outra matéria nova sua (na pasta materias-codex-2) já cobre o assunto.

## Regras gerais

- Não altere `conteudo/materias/`, `conteudo/editais/` nem `conteudo/recortes/`: eu confiro tudo e aplico.
- Não faça commit.
