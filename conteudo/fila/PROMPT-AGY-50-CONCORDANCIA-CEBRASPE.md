Você é professor de Português de um cursinho caro e escreve itens para um app de concursos. Escreva 50 itens
INÉDITOS de CERTO/ERRADO sobre CONCORDÂNCIA VERBAL E NOMINAL no estilo da banca CEBRASPE.

ENTREGA: responda SÓ com um bloco de código ```json contendo um array de 50 objetos, nada fora do bloco.
Não use ferramentas nem crie arquivos; apenas escreva a resposta.

Formato de cada objeto (chaves exatamente assim):
{
  "topico": "Concordância verbal e nominal",
  "estilo": "Cebraspe",
  "statement": "item completo, como na prova",
  "format": "TRUE_FALSE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [ { "key": "C", "text": "Certo", "correct": true|false }, { "key": "E", "text": "Errado", "correct": true|false } ],
  "explanation": "150 a 350 caracteres: a regra, a aplicação ao trecho e, se Errado, o que torna o item errado",
  "section": "título exato do capítulo",
  "errorConceptKey": null,
  "sourceType": "AUTHORIAL", "sourceUrl": null, "board": null, "agency": null, "year": null
}

ESTILO CEBRASPE (obrigatório):
- Pelo menos 30 dos 50 itens partem de um TRECHO de 2 a 5 linhas escrito por você (notícia, relatório, despacho,
  artigo de opinião, texto administrativo), cada um sobre assunto diferente. O item cita o trecho e faz uma
  afirmação sobre ele, nos formatos típicos da banca:
  - "No trecho '...', a forma verbal X concorda com o termo Y."
  - "A substituição de X por Y manteria a correção gramatical do texto."
  - "Se o termo X fosse passado para o plural, a forma verbal Y deveria, obrigatoriamente, ser flexionada no plural."
  - "Seria mantida a correção gramatical do texto caso a forma X fosse substituída por Y."
  - "A forma verbal X está no singular porque concorda com o sujeito oracional."
- Exatamente 25 itens Certo e 25 itens Errado.
- O item Errado troca UM detalhe plausível (o termo com que o verbo concorda, o motivo da flexão, "obrigatoriamente"
  onde a regra é facultativa, a necessidade de alteração). Nada obviamente falso.
- Cuidado com "obrigatoriamente" e "facultativamente": o Cebraspe cobra muito isso. Quando a norma aceita as duas
  formas (sujeito composto posposto, "a maioria de" + plural, porcentagem com especificador, "um dos que", "fui eu
  quem fez/fiz", núcleos ligados por "ou" com ideia de inclusão, adjetivo posposto a vários substantivos), o item
  pode cobrar exatamente essa possibilidade, mas o gabarito tem de estar certo quanto a ser opcional ou obrigatório.

DISTRIBUIÇÃO:
- "1. Fundamentos da concordância e identificação do sujeito" — 10
- "2. Casos especiais de concordância verbal" — 14
- "3. Construções impessoais, locuções verbais e o pronome se" — 10
- "4. Concordância nominal: determinantes, adjetivos e invariáveis" — 10
- "5. Concordância em períodos complexos e revisão estratégica" — 6
Dificuldade: 12 FACIL, 22 MEDIA, 16 DIFICIL.

VARIEDADE: cada caso de concordância é foco de no máximo 2 itens (haver, fazer, se apassivador/indeterminador,
sujeito oracional, sujeito posposto, núcleo com adjunto plural, coletivo, porcentagem, "um dos que", verbo ser,
"é proibido/é necessário", "anexo", "meio", "bastante", "menos", "mesmo", "possível", pronome relativo "que" e
"quem", locuções verbais, verbo concordando com aposto resumidor "tudo", expressões "mais de um", infinitivo).
Nenhum enunciado repetido.

EXPLICAÇÃO: sem citar "Certo"/"Errado" como letra de alternativa; explique a regra e o porquê.

ANTES DE RESPONDER, CONFIRA: 50 itens; 25 Certo e 25 Errado; distribuição por capítulo e dificuldade; nenhum caso
mais de 2 vezes; cada gabarito conferido duas vezes, especialmente obrigatório x facultativo. Corrija se algo falhar.
