# Teste 2: DeepSeek, 50 questões no padrão Estudário (Regência, estilo FGV)

O teste de 100 deu 71 questões fracas: esqueleto repetido trocando só o substantivo, alternativas erradas absurdas
("chegaram com o estádio"), quase tudo num capítulo só. Este pedido traz tudo explícito, com exemplo bom e ruim e
uma conferência obrigatória antes de responder.

Como pedir: abra uma conversa NOVA na DeepSeek (com DeepThink ligado), cole o bloco abaixo e, se der, anexe
`conteudo/materias/portugues/regencia-verbal-nominal.json`. Baixe o bloco de código pelo botão e me mande o arquivo.
Se cortar, "continue exatamente de onde parou, no mesmo formato" e baixe o segundo bloco.

```
Você é professor de Português de um cursinho caro e escreve questões para um app de concursos. Escreva 50 questões
INÉDITAS sobre REGÊNCIA VERBAL E NOMINAL no estilo da banca FGV.

========== ENTREGA ==========
Um único bloco de código ```json com um array de 50 objetos, nada fora do bloco. Se não couber, feche o array numa
questão completa; eu peço "continue" e você manda outro bloco ```json começando na questão seguinte.

Formato de cada objeto (chaves exatamente assim):
{
  "topico": "Regência verbal e nominal",
  "estilo": "FGV",
  "statement": "...",
  "format": "MULTIPLE_CHOICE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [
    { "key": "A", "text": "...", "correct": false, "comentario": "erro exato desta alternativa, até 140 caracteres" },
    { "key": "B", ... }, { "key": "C", ... }, { "key": "D", ... }, { "key": "E", ... }
  ],
  "explanation": "150 a 350 caracteres: a regra + a aplicação ao caso + o erro típico. Sem citar letra.",
  "section": "título exato do capítulo",
  "errorConceptKey": null,
  "sourceType": "AUTHORIAL", "sourceUrl": null, "board": null, "agency": null, "year": null
}

========== DISTRIBUIÇÃO (obrigatória) ==========
Capítulos ("section" igual, letra por letra):
- "1. Fundamentos da regência e transitividade" — 7
- "2. Regência verbal: sentidos e preposições de alta frequência" — 15
- "3. Regência nominal: substantivos, adjetivos e locuções" — 10
- "4. Pronomes relativos, crase e reescrita" — 10
- "5. Análise avançada e armadilhas de correção" — 8
Dificuldade: 15 FACIL, 20 MEDIA, 15 DIFICIL.
Pelo menos 25 das 50 partem de um TRECHO de 2 a 5 linhas escrito por você (notícia, crônica, artigo de opinião,
e-mail de trabalho), cada um sobre um assunto diferente.

========== VARIEDADE (o teste anterior falhou aqui) ==========
- Cada verbo ou nome pode ser o foco de NO MÁXIMO 2 questões em todo o conjunto. Use ao menos 30 verbos/nomes
  diferentes: assistir, aspirar, visar, preferir, obedecer, desobedecer, implicar, proceder, agradar, chegar, ir,
  esquecer, lembrar, informar, avisar, pagar, perdoar, querer, simpatizar, custar, residir, responder, atender,
  morar, ansioso, apto, compatível, favorável, imune, preferível, propenso, relacionado, satisfeito, acessível,
  alheio, contrário, equivalente, necessário, passível, residente.
- Use pelo menos 6 tipos de comando, nenhum em mais de 10 questões:
  (a) "Assinale a frase em que a regência está de acordo com a norma-padrão."
  (b) "A frase que apresenta ERRO de regência é:"
  (c) "No trecho, a preposição destacada é exigida por qual termo?"
  (d) "Assinale a opção em que a substituição do termo destacado mantém a correção e o sentido."
  (e) "Assinale a opção que preenche corretamente as lacunas."
  (f) "O pronome relativo está corretamente precedido de preposição em:"
  (g) "A reescrita do trecho mantém a correção gramatical em:"
- PROIBIDO repetir o mesmo esqueleto trocando só o substantivo (ex.: "chegaram ao estádio" e depois "chegou à
  estação"). Em questões do tipo (a) e (b), as cinco alternativas devem ser frases DIFERENTES entre si, cada uma
  testando um verbo ou nome diferente — não a mesma frase com cinco preposições.

========== ALTERNATIVAS ERRADAS (o teste anterior falhou aqui) ==========
Cada alternativa errada traz UM erro que candidatos de verdade cometem. Exemplos de erros plausíveis:
"assistir o jogo" (sentido de ver), "aspirar o cargo", "visar o cargo", "prefiro mais X do que Y", "chegou no
local", "o filme que gostei", "a lei que me refiro", "implicar em", "obedecer o regulamento", "informar algo a
alguém de algo", "esqueci do nome", "simpatizo-me com", "o cargo que aspiro", "residente à rua", "compatível a".
PROIBIDO alternativa que ninguém escreveria: "chegou com a estação", "namora em alguém", "custou em mim",
"por a estação", "de a estação". Se uma alternativa pode ser eliminada sem saber regência, reescreva-a.

========== TAMANHO ==========
As cinco alternativas com tamanho parecido. A correta pode ser a MAIS LONGA em no máximo 12 das 50 e a MAIS CURTA
em no máximo 12. Não mude a letra correta para equilibrar; a letra pode ficar como sair (o app embaralha).

========== GABARITO ==========
Exatamente uma correta. NÃO use casos que bons gramáticos aceitam de dois jeitos: "custar a + infinitivo",
"atender o/ao telefone", "esquecer de" sem pronome, "assistir a" no sentido de morar, "obedecer" com pronome "lhe"
para coisas. Na dúvida, troque o exemplo.

========== EXEMPLO RUIM (não faça) ==========
"Assinale a frase em que a regência do verbo chegar está correta." A) Chegaram ao estádio. B) Chegaram no estádio.
C) Chegaram com o estádio. D) Chegaram pelo estádio. E) Chegaram do estádio.
(Ruim: mesma frase com cinco preposições; três erradas são absurdas.)

========== EXEMPLO BOM (siga o nível) ==========
"Leia: 'A comissão, que aspirava ___ uma solução rápida, preferiu adiar a votação ___ aprovar um texto incompleto.'
Assinale a opção que preenche corretamente as lacunas."
A) a – a   B) à – do que   C) — – a   D) a – do que   E) por – que
(Bom: trecho real, erros típicos — "preferir do que", "aspirar sem preposição" —, uma única resposta: A.)

========== ANTES DE RESPONDER, CONFIRA ==========
1. São 50 questões, com a distribuição de capítulos e de dificuldade acima?
2. Nenhum verbo ou nome é foco de mais de 2 questões?
3. Nenhuma alternativa absurda? Nenhum esqueleto repetido?
4. A correta é a mais longa em até 12 e a mais curta em até 12?
5. Cada gabarito foi conferido duas vezes e nenhuma explicação cita letra?
Se algum item falhar, corrija antes de entregar.
```

Ao receber: conferidor de recorte, contagem (capítulos, verbos, comandos, tamanhos, esqueletos), leitura de todas as
50 conferindo gabarito, e só então junto com as 39 do recorte FGV e publico.
