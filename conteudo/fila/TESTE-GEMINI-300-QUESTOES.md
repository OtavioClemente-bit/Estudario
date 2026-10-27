# Teste: 300 questões de Concordância no estilo Vunesp (Gemini)

Concordância é uma das matérias de Português mais cobradas. A Vunesp tem hoje 40 questões no recorte dela, e o recorte
comporta até 600.

Como pedir:
- Cole o bloco abaixo no Gemini e anexe `conteudo/materias/portugues/concordancia.json` para ele ler a teoria.
- Se a resposta for cortada no meio, diga "continue exatamente de onde parou, sem repetir".
- Salve tudo em `conteudo/entrada/teste-gemini-concordancia/` (um arquivo ou vários, tanto faz). Eu junto, confiro e publico.

```
Escreva 300 questões INÉDITAS de Língua Portuguesa sobre CONCORDÂNCIA (verbal e nominal), no estilo da banca VUNESP, para
um app de estudo para concursos. Responda SÓ com JSON: um array com as 300 questões, sem nenhum texto fora do JSON. Se não
couber numa resposta, pare no fim de uma questão completa (feche o array) e eu peço "continue"; na continuação, mande outro
array começando na questão seguinte, sem repetir nenhuma.

FORMATO de cada questão (copie as chaves exatamente):
{
  "statement": "enunciado completo, com o trecho ou a frase entre aspas quando houver",
  "format": "MULTIPLE_CHOICE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [
    { "key": "A", "text": "...", "correct": false, "comentario": "erro exato desta alternativa (até 140 caracteres)" },
    { "key": "B", ... }, { "key": "C", ... }, { "key": "D", ... }, { "key": "E", ... }
  ],
  "explanation": "150 a 350 caracteres: a regra, a aplicação ao caso e o erro típico, SEM citar letra",
  "section": "título EXATO de um dos capítulos abaixo",
  "errorConceptKey": null,
  "sourceType": "AUTHORIAL", "sourceUrl": null, "board": null, "agency": null, "year": null
}

CAPÍTULOS ("section" tem de ser um destes, copiado igual), com quantas questões em cada:
- "1. Fundamentos da concordância e identificação do sujeito" — 60
- "2. Casos especiais de concordância verbal" — 80
- "3. Construções impessoais, locuções verbais e o pronome se" — 60
- "4. Concordância nominal: determinantes, adjetivos e invariáveis" — 60
- "5. Concordância em períodos complexos e revisão estratégica" — 40

REGRAS:
1. Exatamente 5 alternativas (A–E) e UMA correta. A letra correta fecha com 60 questões em cada letra.
2. A alternativa correta NÃO pode ser a mais longa em mais de 30% das questões. Alternativas com tamanho parecido.
3. Dificuldade: 90 FACIL, 120 MEDIA, 90 DIFICIL.
4. Estilo Vunesp: pelo menos metade das questões parte de um trecho de 2 a 6 linhas que você escreve (crônica, notícia,
   texto opinativo, sobre assuntos variados e realistas, nada de texto genérico de enchimento). Varie o comando:
   - "Assinale a alternativa em que a concordância está de acordo com a norma-padrão."
   - "Mantendo-se o sentido, a substituição do trecho destacado está correta quanto à concordância em:"
   - "Passando-se para o plural o termo ..., a frase fica corretamente reescrita em:"
   - "Assinale a alternativa que preenche corretamente as lacunas."
   - "O verbo destacado concorda com o termo:"
   - "A frase em que a concordância foi feita de forma INCORRETA é:"
   Nenhum tipo de comando em mais de 50 questões. Nenhuma ideia ou situação repetida mais de duas vezes.
5. DISTRATORES PLAUSÍVEIS, com erros que um candidato médio cometeria: verbo concordando com o termo mais próximo e não com
   o núcleo, "haver" existencial no plural, "fazer" de tempo no plural, "-se" apassivador com verbo no singular, "menos"
   flexionado, "anexo"/"obrigado"/"mesmo" sem concordar, "é proibido"/"é necessário" com determinante, sujeito posposto no
   singular, "um dos que", porcentagem, coletivo com especificador. PROIBIDO alternativa absurda.
6. Confira cada gabarito duas vezes. NÃO faça questão cujo gabarito dependa de concordância facultativa (ex.: sujeito
   composto posposto, "a maioria dos" + verbo), a não ser que o comando trate exatamente disso e diga que as duas formas
   são aceitas.
7. Inéditas: não copie questões de provas reais nem de sites; não repita frase entre questões.
8. Não escreva "letra A", "alternativa C" etc. na explicação (o app embaralha as alternativas).
```

Ao receber: rodo o conferidor, conto letras, tamanhos e comandos, leio uma amostra conferindo gabaritos, junto com as 40
de `conteudo/recortes/vunesp/portugues.concordancia.json` e só então publico.
