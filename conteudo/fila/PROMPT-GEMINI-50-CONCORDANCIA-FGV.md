# Gemini: 50 questões de Concordância verbal e nominal, estilo FGV

Por que esta: Concordância cai em cerca de 310 editais da FGV e o recorte da FGV tem só 39 questões. É o mesmo modelo
de pedido que deu 46 de 50 aproveitadas em Regência.

Como pedir: conversa NOVA no Gemini, cole o bloco abaixo e anexe
`conteudo/materias/portugues/concordancia.json` se der. Baixe o bloco de código e me mande o arquivo.
Se cortar, "continue exatamente de onde parou, no mesmo formato".

```
Você é professor de Português de um cursinho caro e escreve questões para um app de concursos. Escreva 50 questões
INÉDITAS sobre CONCORDÂNCIA VERBAL E NOMINAL no estilo da banca FGV.

========== ENTREGA ==========
Um único bloco de código ```json com um array de 50 objetos, nada fora do bloco. Se não couber, feche o array numa
questão completa; eu peço "continue" e você manda outro bloco ```json começando na questão seguinte.

Formato de cada objeto (chaves exatamente assim):
{
  "topico": "Concordância verbal e nominal",
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
- "1. Fundamentos da concordância e identificação do sujeito" — 10
- "2. Casos especiais de concordância verbal" — 14
- "3. Construções impessoais, locuções verbais e o pronome se" — 10
- "4. Concordância nominal: determinantes, adjetivos e invariáveis" — 10
- "5. Concordância em períodos complexos e revisão estratégica" — 6
Dificuldade: 15 FACIL, 20 MEDIA, 15 DIFICIL.
Pelo menos 25 das 50 partem de um TRECHO de 2 a 5 linhas escrito por você (notícia, relatório, despacho, e-mail de
trabalho, crônica, artigo de opinião), cada um sobre um assunto diferente.

========== VARIEDADE ==========
- Cada caso de concordância pode ser o foco de NO MÁXIMO 2 questões. Use ao menos 25 casos diferentes, entre eles:
  sujeito posposto; sujeito distante do verbo; núcleo seguido de adjunto no plural ("a análise dos documentos");
  sujeito oracional; haver existencial e haver de tempo; fazer de tempo; locução com haver/fazer ("deve haver");
  "se" apassivador x "se" índice de indeterminação; verbo + "se" com sujeito plural ("vendem-se casas");
  "existir/ocorrer/faltar/restar/bastar" com sujeito posposto; "um dos que"; "mais de um"; porcentagem com
  especificador; coletivo; pronome relativo "que" e "quem"; verbo ser (sujeito x predicativo, horas, datas,
  distâncias); "é proibido / é necessário / é bom" com e sem determinante; "menos", "pseudo", "alerta" (invariáveis);
  "anexo, incluso, obrigado, mesmo, próprio, quite" (variáveis); "bastante" adjetivo x advérbio; "meio" adjetivo x
  advérbio; adjetivo posposto a dois substantivos; "possível" com "o mais / os mais"; títulos de obras.
- Use pelo menos 6 tipos de comando, nenhum em mais de 10 questões:
  (a) "Assinale a frase em que a concordância está de acordo com a norma-padrão."
  (b) "A frase que apresenta ERRO de concordância é:"
  (c) "No trecho, a forma verbal destacada concorda com qual termo?"
  (d) "Passando-se para o plural o termo destacado, a frase fica corretamente reescrita em:"
  (e) "Assinale a opção que preenche corretamente as lacunas."
  (f) "Mantendo-se o sentido, a substituição do termo destacado está correta quanto à concordância em:"
  (g) "A reescrita do trecho mantém a correção gramatical em:"
- ENUNCIADOS ÚNICOS: nenhum enunciado pode ser idêntico a outro. Quando usar o mesmo comando, mude a redação
  ("Há erro de concordância na frase:", "Uma das frases contraria a norma-padrão quanto à concordância. Trata-se de:").
- PROIBIDO repetir o mesmo esqueleto trocando só o substantivo. Nas questões (a) e (b), as cinco alternativas são
  frases DIFERENTES, cada uma testando um caso diferente.

========== ALTERNATIVAS ERRADAS ==========
Cada alternativa errada traz UM erro que candidatos de verdade cometem: "haviam muitos problemas", "fazem dois
anos", "devem haver soluções", "vende-se casas", "precisam-se de funcionários", "a maioria dos alunos faltaram"
(só se o comando tratar disso), "seguem anexo as notas", "é proibida entrada", "menas pessoas", "meia cansada",
"bastante alunos", "um dos que mais trabalhou" tratado como errado indevidamente, verbo concordando com o adjunto
mais próximo, sujeito posposto no singular. PROIBIDO alternativa que ninguém escreveria ou que dá para eliminar sem
saber concordância.

========== TAMANHO ==========
As cinco alternativas com tamanho parecido. A correta pode ser a MAIS LONGA em no máximo 12 das 50 e a MAIS CURTA
em no máximo 12. Não mude a letra correta para equilibrar; a letra pode ficar como sair (o app embaralha).

========== GABARITO ==========
Exatamente uma correta. NÃO faça o gabarito depender de concordância que a norma aceita de dois jeitos: sujeito
composto posposto ("chegaram/chegou o pai e o filho"), "a maioria de + plural", porcentagem antes de especificador
("30% da turma faltou/faltaram"), "mais de um" repetido, "um dos que" (singular/plural), "fui eu quem fez/fiz",
"nem um nem outro", títulos no plural sem artigo, adjetivo posposto a substantivos (concordar com o mais próximo ou
ir ao plural). Se usar algum desses, o comando deve tratar exatamente da dupla possibilidade.

========== EXEMPLO RUIM (não faça) ==========
"Assinale a frase correta." A) Haviam muitos alunos. B) Havia muitos alunos. C) Haverão muitos alunos. D) Houveram
muitos alunos. E) Hão muitos alunos.
(Ruim: mesma frase variando só o verbo; alternativas que ninguém escreve; um caso só.)

========== EXEMPLO BOM (siga o nível) ==========
"Leia: 'Na reunião de ontem, ___ ser discutidas as metas do semestre, mas ___ dois meses que a diretoria adia a
decisão.' Assinale a opção que preenche corretamente as lacunas."
A) deveriam – faz   B) deveria – fazem   C) deveriam – fazem   D) deveria – faz   E) deveriam – fazia-se
(Bom: dois casos diferentes, erros típicos — locução com sujeito plural, "fazer" de tempo no plural —, uma única
resposta: A.)

========== ANTES DE RESPONDER, CONFIRA ==========
1. São 50 questões, com a distribuição de capítulos e de dificuldade acima?
2. Nenhum caso é foco de mais de 2 questões? Nenhum enunciado é idêntico a outro?
3. Nenhuma alternativa absurda? Nenhum esqueleto repetido?
4. A correta é a mais longa em até 12 e a mais curta em até 12?
5. Nenhum gabarito depende de concordância facultativa? Cada gabarito foi conferido duas vezes?
6. Nenhuma explicação cita letra?
Se algum item falhar, corrija antes de entregar.
```

Ao receber: conferidor, contagem (capítulos, casos, comandos, tamanhos, enunciados repetidos), leitura das 50
conferindo gabarito, e só então junto com as 39 do recorte FGV e publico.
