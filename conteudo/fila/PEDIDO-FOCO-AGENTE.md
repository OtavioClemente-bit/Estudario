# Foco de cada tópico de edital dentro da matéria (pedido para agente)

Cada tópico de edital ("Legítima defesa.") está ligado a uma matéria inteira ("Teoria do crime", 45
questões). Para tópico estreito, o app vai abrir a matéria **no foco**: primeiro os capítulos e as
questões daquele tópico, depois o resto. Seu trabalho é dizer, tópico por tópico, qual é esse foco.

## Entrada
Para cada matéria do seu lote há um arquivo `<id>.json` (na pasta que você recebeu) com: `titulo`,
`escopo`, `capitulos` (índice e título), `questoes` (id, capítulo, começo do enunciado) e `topicos`
(os textos de edital ligados a ela). Se precisar, leia a matéria completa em
`conteudo/materias/<área>/<nome>.json` (id `<área>.<nome>`).

## Decisão, para CADA texto de `topicos` (julgamento seu, tópico por tópico; não use regra automática)
- `"foco": "inteira"` quando o tópico cobre a matéria toda ou a maior parte dela (ex.: "Teoria geral
  do crime", "Ato administrativo: conceito, requisitos, atributos, classificação, espécies, extinção"),
  ou quando é genérico demais para recortar.
- Senão, o recorte:
  - `"capitulos"`: os índices (começando em 0) dos capítulos que ensinam aquele tópico (1 a 3);
  - `"questoes"`: os ids das questões que cobram **diretamente** aquele tópico (todas as que cobram;
    pode ser lista vazia se nenhuma cobra — aí fica só a teoria).
  Uma questão que só menciona o assunto de passagem não entra.

## Saída
Para cada matéria, grave com Write (arquivo inteiro de uma vez) `conteudo/entrada/foco/<id>.json`:
```
{ "materia": "<id>", "topicos": [
  { "texto": "<texto exato do tópico>", "foco": "inteira" },
  { "texto": "<texto exato>", "capitulos": [3], "questoes": ["q12", "q31", "q40"] } ] }
```
- Todos os textos de `topicos` aparecem, uma vez cada, copiados exatamente.
- Só ids de capítulo e de questão que existem na matéria.
- Não edite `conteudo/materias` nem `conteudo/editais`. Não faça commit.

No fim responda só: quantas matérias, quantos tópicos "inteira" e quantos com recorte.
