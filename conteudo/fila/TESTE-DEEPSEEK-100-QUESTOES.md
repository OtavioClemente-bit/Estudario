# Teste: DeepSeek, 100 questões de uma vez (Regência verbal e nominal, estilo FGV)

Por que esta: Regência cai em cerca de 300 editais da FGV, e o recorte da FGV tem hoje só 39 questões (o recorte
comporta até 600). Se as 100 ficarem boas, entram direto no app.

Sem copiar e colar: o prompt pede a resposta inteira num único bloco de código JSON. No chat da DeepSeek, o bloco
de código tem um botão de baixar (foi assim que veio o `deepseek_json_....json` da matéria dos Juizados). Baixe e
mande o arquivo para mim. Se ele cortar no meio, escreva "continue exatamente de onde parou, no mesmo formato" e
baixe o segundo bloco também; eu junto os dois.

Anexe (ou cole) também `conteudo/materias/portugues/regencia-verbal-nominal.json` se o chat aceitar, para ele seguir
a mesma teoria.

```
Escreva 100 questões INÉDITAS de Língua Portuguesa sobre REGÊNCIA VERBAL E NOMINAL, no estilo da banca FGV, para um
app de estudo para concursos.

ENTREGA: responda com UM ÚNICO bloco de código ```json contendo um array com as 100 questões, e nada fora do bloco.
Se não couber, feche o array numa questão completa e eu peço "continue"; na continuação, outro bloco ```json com um
array que começa na questão seguinte, sem repetir nenhuma.

FORMATO de cada questão (copie as chaves exatamente):
{
  "topico": "Regência verbal e nominal",
  "estilo": "FGV",
  "statement": "enunciado; pode trazer um trecho entre aspas",
  "format": "MULTIPLE_CHOICE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [
    { "key": "A", "text": "...", "correct": false, "comentario": "o erro exato desta alternativa (até 140 caracteres)" },
    { "key": "B", ... }, { "key": "C", ... }, { "key": "D", ... }, { "key": "E", ... }
  ],
  "explanation": "150 a 350 caracteres: a regra, aplicada ao caso, SEM citar letra de alternativa",
  "section": "título EXATO de um dos capítulos abaixo",
  "errorConceptKey": null,
  "sourceType": "AUTHORIAL", "sourceUrl": null, "board": null, "agency": null, "year": null
}

CAPÍTULOS ("section" igual, letra por letra) e quantas questões em cada:
- "1. Fundamentos da regência e transitividade" — 15
- "2. Regência verbal: sentidos e preposições de alta frequência" — 30
- "3. Regência nominal: substantivos, adjetivos e locuções" — 20
- "4. Pronomes relativos, crase e reescrita" — 20
- "5. Análise avançada e armadilhas de correção" — 15

REGRAS:
1. Exatamente 5 alternativas e UMA correta. Dificuldade: 30 FACIL, 40 MEDIA, 30 DIFICIL.
2. TAMANHO: a alternativa correta pode ser a mais longa em no máximo 25 das 100, e a mais curta em no máximo 25.
   Escreva as alternativas com tamanho e nível de detalhe parecidos. NÃO mude a letra correta para "equilibrar";
   a letra pode ficar como sair (o app embaralha).
3. Estilo FGV: muitas questões partem de um trecho de 2 a 5 linhas que você escreve (notícia, crônica, texto
   opinativo, com assuntos variados e realistas). Varie o comando:
   - "Assinale a frase em que a regência do verbo destacado está de acordo com a norma-padrão."
   - "A preposição sublinhada é exigida por qual termo?"
   - "Assinale a opção em que a substituição do termo sublinhado mantém a correção e o sentido."
   - "A frase que apresenta erro de regência é:"
   - "Assinale a opção que preenche corretamente as lacunas."
   - "O pronome relativo está corretamente precedido de preposição em:"
   Nenhum comando em mais de 20 questões. Nenhuma ideia repetida mais de duas vezes.
4. Use os verbos e nomes que mais caem: assistir, aspirar, visar, preferir, obedecer, implicar, proceder, agradar,
   chegar/ir, esquecer/lembrar(-se), informar, pagar/perdoar, querer, simpatizar, namorar, custar, residir, ansioso,
   apto, compatível, favorável, imune, preferível, propenso, relacionado, satisfeito, entre outros.
5. Distratores plausíveis: erros que um candidato médio cometeria ("assistir o jogo", "prefiro mais X do que Y",
   "chegou no local", "o filme que gostei", "a lei que me refiro", "implicar em", "aspirar o cargo"). Proibido
   alternativa absurda.
6. Confira cada gabarito duas vezes. NÃO faça questão cujo gabarito dependa de regência que os gramáticos aceitam
   de dois jeitos (ex.: "esquecer de" sem pronome no registro coloquial, "atender o/ao telefone"), a não ser que o
   comando trate exatamente disso.
7. Explicação sem letra de alternativa ("a letra B", "alternativa C" são proibidos). Inéditas: não copie provas.
```

Ao receber: rodo o conferidor de recorte, conto tamanhos, comandos e repetições, leio uma amostra conferindo
gabaritos, junto com as 39 de `conteudo/recortes/fgv/portugues.regencia-verbal-nominal.json` e só então publico.
