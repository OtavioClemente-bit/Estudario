# Teste: 500 questões de Pontuação no estilo Vunesp

Pontuação cai em 3.644 editais e só perde para interpretação de texto. A Vunesp tem hoje 40 questões de Pontuação no
recorte dela, e o recorte comporta até 600. O teste vale a pena porque, se as 500 ficarem boas, entram direto no app.

Como pedir:
- Cole o bloco abaixo no chat do GPT e anexe (ou cole) `conteudo/materias/portugues/pontuacao.json`, para ele ler a teoria.
- Se ele não aceitar o arquivo, cole só os títulos dos capítulos, que já estão no texto.
- 500 questões não cabem numa resposta. Peça em 10 partes de 50 e diga "continue com a parte 2" e assim por diante.
- Salve cada parte como `parte-01.json` … `parte-10.json` em `conteudo/entrada/teste-gpt-pontuacao/`. Eu junto, confiro e
  publico.

```
Você vai escrever 500 questões INÉDITAS de Língua Portuguesa, assunto PONTUAÇÃO, no estilo da banca VUNESP, para um app de
estudo para concursos. Entregue em 10 partes de 50 questões; em cada resposta, só o JSON da parte (um array), sem comentários
fora do JSON. Ao fim de cada parte, espere eu dizer "continue".

FORMATO de cada questão (copie as chaves exatamente):
{
  "topico": "Pontuação",
  "estilo": "VUNESP",
  "statement": "enunciado completo, com o trecho/frase entre aspas quando houver",
  "format": "MULTIPLE_CHOICE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [
    { "key": "A", "text": "...", "correct": false, "comentario": "por que está errada (até 160 caracteres)" },
    { "key": "B", ... }, { "key": "C", ... }, { "key": "D", ... }, { "key": "E", ... }
  ],
  "explanation": "150 a 400 caracteres explicando a regra e por que a certa está certa, SEM citar letra",
  "section": "título EXATO de um dos capítulos abaixo",
  "errorConceptKey": null,
  "sourceType": "AUTHORIAL", "sourceUrl": null, "board": null, "agency": null, "year": null
}

CAPÍTULOS ("section" tem de ser um destes, copiado igual), com quantas questões em cada:
- "1. Vírgula: termos essenciais, intercalações e coordenação" — 140
- "2. Vírgula entre orações: subordinação, restrição e sentido" — 130
- "3. Ponto final, ponto e vírgula e dois-pontos na estrutura textual" — 80
- "4. Travessão, parênteses, aspas, reticências, interrogação e exclamação" — 80
- "5. Ordem direta e inversa, coesão, clareza e revisão de textos" — 70

REGRAS:
1. Exatamente 5 alternativas (A–E) e UMA correta. A letra correta deve variar e fechar com 100 questões em cada letra no
   total (cerca de 10 por letra em cada parte).
2. A alternativa correta NÃO pode ser a mais longa em mais de 35% das questões. Alternativas com tamanho parecido.
3. Dificuldade no total: 150 FACIL, 200 MEDIA, 150 DIFICIL.
4. Estilo Vunesp de verdade: muitas questões partem de um trecho curto de texto (crônica, notícia, texto opinativo — escreva
   você o trecho, de 2 a 6 linhas) e perguntam sobre a pontuação dele. Varie o comando:
   - "Assinale a alternativa em que a pontuação está de acordo com a norma-padrão."
   - "A vírgula foi empregada para isolar ... em:"
   - "Os dois-pontos, no trecho, introduzem:"
   - "Mantendo o sentido, a frase está corretamente reescrita, quanto à pontuação, em:"
   - "As aspas, no texto, foram usadas para:"
   - "A retirada da vírgula altera o sentido em:"
   Nenhum tipo de comando pode aparecer em mais de 15% das questões.
5. DISTRATORES PLAUSÍVEIS: cada alternativa errada traz um erro que um candidato médio cometeria, como uma vírgula entre
   sujeito e verbo, uma restritiva pontuada como explicativa, um adjunto adverbial longo deslocado sem vírgula, a vírgula
   antes de "e" com sujeitos diferentes, o vocativo sem vírgula ou um ponto e vírgula no lugar errado. PROIBIDO alternativa
   absurda ou que dê para eliminar sem saber a regra.
6. Toda frase das alternativas deve ser natural, em português correto (fora o erro de pontuação proposto quando for o caso).
   Confira cada gabarito duas vezes: em pontuação há casos de vírgula facultativa; NÃO faça questão cujo gabarito dependa de
   vírgula facultativa, a menos que o comando trate exatamente disso.
7. Inéditas: não copie questões de provas reais nem de sites; não repita frase, trecho ou situação entre questões.
8. Explicação: diga a regra, aplique ao caso e aponte o erro típico. Não escreva "letra A", "alternativa C" etc. (o app
   embaralha as alternativas).
9. Cada "comentario" diz o erro exato daquela alternativa (ou, na certa, por que está correta).
```

Ao receber: rodo o conferidor, junto com as 40 que já existem em `conteudo/recortes/vunesp/portugues.pontuacao.json`, leio
uma amostra de pelo menos 50 conferindo o gabarito, e só então publico.
