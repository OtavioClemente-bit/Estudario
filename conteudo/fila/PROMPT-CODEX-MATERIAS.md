# Prompt para o Codex criar matérias no padrão do Estudário

Cole o texto do bloco abaixo no Codex e troque só a linha **MATÉRIA**. Peça **uma matéria por tarefa**, porque o limite acaba
rápido. O Codex grava um JSON pronto, e eu só confiro e publico.

```
Você vai escrever UMA matéria de estudo para o app Estudário, neste repositório. Leia antes:
- AGENTS.md (regras do projeto);
- conteudo/fila/PEDIDOS-MATERIAS-LOCAIS-2026-10-21.md, seção "Mensagem 0" (formato JSON e quantidades: siga TODOS os campos);
- um modelo pronto, para copiar o estilo e a estrutura: conteudo/materias/roraima/lc-53-2001-servidores.json
  (e conteudo/materias/distrito-federal/lc-840-2011-servidores.json).

MATÉRIA: <título> | id: "<prefixo>.<nome>" | tópicos do edital (viram aliases, copie exatamente): "<tópico 1>" | "<tópico 2>"

COMO FAZER (nesta ordem):
1. FONTE OFICIAL PRIMEIRO. Abra o texto oficial e compilado da norma ou do documento (Planalto, portal da Assembleia ou Câmara,
   Diário Oficial, site do órgão, IBGE). Leia o texto INTEIRO e as alterações até hoje. Se não conseguir abrir uma fonte oficial,
   PARE e só escreva um arquivo PENDENCIA.md explicando o que tentou. Não escreva a matéria com base em memória ou em site de
   apostila.
2. NUNCA invente número de artigo, prazo, percentual, valor, data ou composição. Se não confirmou, não cite o número: explique a
   ideia. Não escreva "a confirmar", "pendente" ou "revisão editorial" dentro da matéria: o que não foi confirmado fica de fora,
   e você avisa no relatório final.
3. Escreva o JSON INTEIRO e grave UMA vez em conteudo/entrada/materias-codex/<id>.json. Não grave Markdown, nem rascunho, nem
   arquivos de controle, auditoria ou scripts.
4. Valide e corrija à mão até dar OK:
   deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/entrada/materias-codex/<id>.json

PADRÃO DE QUALIDADE (aula de cursinho caro, não resumo de lei):
- 4 a 6 capítulos, com teoria somada de 15.000 a 30.000 caracteres. Cada capítulo começa explicando o problema que a regra resolve.
  Explique cada sigla e termo técnico na primeira vez que aparecer, dê exemplo concreto com personagem ("Ana tomou posse e..."),
  use tabela comparativa onde houver institutos parecidos, mostre como a banca cobra, marque **Pegadinha:** e feche com
  "**Resumo para a prova**".
- summary: resumo geral em tópicos, com os números de prova, de no mínimo 400 caracteres.
- flashcards (12 a 30): frente com pergunta objetiva e verso curto com o dado exato. Sem frente repetida.
- tips (3 a 8) e traps (3 a 8): dicas e pegadinhas ESPECÍFICAS desta norma, nada genérico como "leia com atenção".
- activeRecall (5 a 10) e errorConcepts (2 a 6, com chaves e1, e2...): os erros de raciocínio típicos deste assunto.
- questions: 40 a 50, com pelo menos 20 Certo/Errado e 18 de múltipla escolha A–E, e pelo menos 8 de cada dificuldade.
  - Cada explicação tem de 150 a 400 caracteres e diz POR QUE está certo ou errado, citando o artigo só se tiver certeza.
  - "section" é o título EXATO de um capítulo.
  - Na múltipla escolha, a certa não pode ser a mais longa em mais de 40% das questões. Varie a letra certa.
  - Não cite letras de alternativa na explicação (o app embaralha as alternativas).
  - DISTRATORES PLAUSÍVEIS: toda alternativa errada precisa parecer certa para quem não estudou. Troque um prazo, um órgão, um
    percentual, uma exceção, ou use a regra de uma lei parecida. PROIBIDO alternativa absurda ou caricata ("divulgar sua senha",
    "custear toda a unidade", "o princípio da eficiência elimina a boa-fé"). Em Certo/Errado, o item errado deve ter UM erro
    sutil de prova, e não um exagero óbvio.
  - Use enunciados no estilo da banca do edital (FGV, Cebraspe, FCC, Vunesp...), não só "Fulano fez X. Qual é o dever?".
- sources: só as fontes que você realmente abriu, com url e accessedAt da data de hoje.

RELATÓRIO FINAL (curto): o arquivo gravado, o tamanho da teoria, a quantidade de questões, as fontes abertas e o que ficou de fora
por falta de confirmação. Não faça commit em main: abra PR só com o arquivo da matéria.
```

## Prefixos de id

Use o prefixo do lugar: `distrito-federal.`, `roraima.`, `rondonia.`, `espirito-santo.`, `santa-catarina.`, `minas-gerais.`,
`ceara.`, `alagoas.`, `mato-grosso.`, `paraiba.` etc. Para município, junte o estado e a cidade, como em
`roraima.boa-vista-pccr-geral`. Para norma nacional, use `saude-publica.`, `educacao.` ou `legislacao.`.

## Pendentes do PR 3 (precisam da fonte oficial)

- Decreto 2.760/2009 de Santa Catarina (CONESAN e CRESANs). Tópico: "Decreto Estadual nº 2.760/2009."
- Legislação de Chapada dos Guimarães: LCs 62/2014 e 97/2023, Leis 1.447/2011 e 1.570/2014.
- Processo administrativo de Santa Luzia/MG (diploma 4.055/2019).
- CEE/AL: Resoluções 6 e 7/2014 e Resolução 70/2022.
