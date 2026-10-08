# Fórmulas, gráficos e figuras que o app desenha

Vale para matéria nova (`PEDIDO-MATERIA-AGENTE.md`) e para enriquecimento (`PEDIDO-ENRIQUECER-AGENTE.md`).
O app (Android e site) desenha exatamente isto; qualquer outra forma aparece como texto cru.

## Fórmulas (LaTeX)
- Toda fórmula, equação, unidade composta ou símbolo matemático vai em LaTeX entre cifrões DUPLOS.
- Fórmula principal em bloco, com `$$` sozinho na linha antes e depois (o app centraliza), e logo abaixo
  uma lista dizendo o que é cada letra, com unidade:

  ```
  $$
  pV = nRT
  $$
  ```
- Fórmula curta dentro da frase: `$$U = R \cdot i$$` na própria linha.
- **Nunca** cifrão simples nem `\( \)` (o app confunde com R$). Valor em reais fica em texto: "R$ 1.050,00".
- Dentro do JSON, a barra do LaTeX é dobrada: `\\frac`, `\\cdot`, `\\sqrt`.

## Gráficos e figuras
Um bloco de código com a linguagem `grafico` e um JSON de UMA linha dentro, sozinho no parágrafo.
No JSON da matéria fica: `"texto\n\n```grafico\n{\"tipo\":\"funcao\", ...}\n```\n\nmais texto"`.

| tipo | serve para | exemplo |
|---|---|---|
| pizza | partes de um todo | `{"tipo":"pizza","titulo":"...","itens":[{"rotulo":"A","valor":40},{"rotulo":"B","valor":60}],"legenda":"dados ilustrativos"}` |
| barras | comparar quantidades | `{"tipo":"barras","titulo":"...","unidade":"%","itens":[{"rotulo":"...","valor":12.5}]}` |
| linha | evolução no tempo | `{"tipo":"linha","titulo":"...","eixoX":"ano","eixoY":"R$ mil","series":[{"nome":"...","pontos":[[2020,10],[2021,12]]}]}` |
| funcao | função matemática | `{"tipo":"funcao","titulo":"...","funcoes":[{"expr":"x^2-4","nome":"f(x) = x² − 4"}],"xmin":-4,"xmax":4,"pontos":[{"x":2,"y":0,"rotulo":"raiz"}]}` |
| geometria | figuras de geometria e física | ver abaixo |

- **funcao:** em `expr` use x, números com ponto, `+ - * / ^`, parênteses e sen, cos, tg, ln, log, raiz, abs, exp, pi.
- **geometria:** coordenadas reais com escala igual nos dois eixos (triângulo 3-4-5 tem catetos 3 e 4).
  `{"tipo":"geometria","titulo":"Triângulo retângulo","pontos":[{"nome":"A","x":0,"y":0},{"nome":"B","x":4,"y":0},{"nome":"C","x":0,"y":3}],"poligonos":[["A","B","C"]],"segmentos":[{"de":"B","ate":"C","rotulo":"a = 5"},{"de":"A","ate":"B","rotulo":"c = 4"},{"de":"A","ate":"C","rotulo":"b = 3"}],"angulos":[{"vertice":"A","de":"B","ate":"C","reto":true}]}`
  Também aceita `"circulos":[{"centro":"O","raio":2,"rotulo":"r = 2"}]` e
  `"vetores":[{"de":"O","ate":"F","rotulo":"F = 10 N"}]` (forças, velocidades, decomposição).
  Todo nome citado em segmentos, ângulos, círculos e vetores existe em `pontos`; rótulo de lado traz letra e valor.
- De 1 a 3 por capítulo, só quando ajuda: funções (afim, quadrática, exponencial, log), juros simples × compostos,
  distribuição de dados, porcentagem de um todo, evolução no tempo, Pitágoras, trigonometria, áreas, polígonos,
  círculos, Venn, forças e vetores, MRU/MRUV (gráfico de posição ou velocidade × tempo), oferta e demanda.
- Dado real só com fonte; dado inventado leva "dados ilustrativos" na legenda. Os números do gráfico batem com os do texto.

## Resto do texto
- Comparações e classificações em tabela Markdown.
- Exemplos resolvidos em lista numerada, cada conta em LaTeX, fechando com o resultado em **negrito**.
- Alertas de prova com `>` no início da linha.
