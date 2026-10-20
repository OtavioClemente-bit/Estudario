# Pedido: escrever a MATÉRIA "Plano Nacional de Educação (PNE)"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** o Plano Nacional de Educação: base constitucional (art. 214), diretrizes, metas e estratégias (educação infantil, alfabetização, ensino fundamental e médio, EJA, educação especial, educação profissional, ensino superior, valorização dos profissionais, gestão democrática, financiamento e percentual do PIB), monitoramento e avaliação, planos estaduais e municipais. Pesquise e informe a situação atual do PNE vigente (lei e período); se não tiver certeza de algum número de meta, explique o conteúdo sem o número.
- **Não cobre:** LDB, BNCC, Fundeb e didática (matérias próprias).
- **Atualização:** pesquise na legislação oficial (Planalto, MEC, STF/STJ conforme o tema) para garantir que tudo está vigente. Hoje é 2026-10-18.
- **Nunca invente número de lei, artigo, meta, súmula ou tema.** Se não tiver certeza do número, explique o conteúdo sem o número.

## O que escrever (tudo escrito por você, questão por questão; nada de montar por programa)
1. **Teoria: 5 capítulos**, do básico ao avançado, cobrindo o escopo. Cada capítulo em Markdown, com explicação, exemplo concreto (caso), quadro-resumo do tema e uma seção "Pegadinhas". Último capítulo: "Como o tema costuma ser cobrado" (sem citar nome de banca: diga "provas de Certo/Errado", "provas com casos longos").
2. **summary** (resumo de 1 tela), **flashcards** (pelo menos 20; frente = súmula/tese ou pergunta curta, verso = resposta), **tips** (7), **traps** (7), **activeRecall** (8 perguntas com resposta), **errorConcepts** (5 confusões típicas, com key "e1", "e2"... "e5", título e resumo).
3. **45 questões**: 25 de múltipla escolha (A–E) e 20 de Certo/Errado (format "TRUE_FALSE", com duas opções: key "C" texto "Certo" e key "E" texto "Errado"); 15 fáceis (FACIL), 15 médias (MEDIA), 15 difíceis (DIFICIL).
   - Uma única alternativa correta; distratores plausíveis (erro típico de quem estudou mal). **Proibido distrator absurdo ou cômico.**
   - Gabarito espalhado (nenhuma letra acima de 40% das de múltipla escolha); a correta NÃO pode ser a alternativa mais longa em mais de 40% das questões.
   - Explicação de 120+ caracteres dizendo por que a certa está certa e por que as erradas erram; em Certo/Errado começa com "Certo." ou "Errado.".
   - Cada questão com "section" = título EXATO (copiado) de um capítulo e "errorConceptKey" = uma key de errorConcepts.
   - Questões autorais: sourceType "AUTHORIAL", board/agency/year/sourceUrl = null.
4. **Neutra:** sem nome de banca, órgão ou tribunal regional como contexto das questões.
5. **sources:** liste as fontes que você realmente consultou (STF, STJ, Planalto), com url e data de acesso.
6. **aliases:** copie até 20 textos EXATOS da lista "Tópicos de edital" abaixo (os mais específicos; nada genérico).

## Tópicos de edital (textos reais, para os aliases)
- Plano Nacional de Educação.
- Plano Nacional de Educação (PNE).
- Plano Nacional de Educação (PNE – Lei nº 13.005/2014): Metas e responsabilidades dos municípios.
- Lei nº 13.005/2014 - Plano Nacional de Educação (PNE).
- Lei nº 13.005/2014 - Plano Nacional de Educação (PNE). 11.
- Lei nº 13.005/2014 - Plano Nacional de Educação (PNE). 4.
- Lei nº 13.005/2014 - Plano Nacional de Educação – PNE.
- Tutela coletiva do direito educacional, Lei de Diretrizes e Bases da Educação e Plano Nacional de Educação.
- Plano Nacional de Educação – PNE.
- Plano Nacional de Educação (Lei nº 13.005/2014).
- Plano Nacional de Educação - PNE: Lei Federal nº 15.388/2026.
- Plano Nacional de Educação (PNE): Lei Federal nº 15.388/2026.
- Plano Nacional de Educação em Direitos Humanos (PNEDH).
- Lei Federal nº 15.388/2026 - Plano Nacional de Educação (PNE).
- Conceitos, classificação e escopo de atendimento: definições de PNE, avaliação do grau de dependência e planejamento centrado no paciente e no cuidador.
- Odontologia preventiva e educação em saúde para PNE e cuidadores: escovação adaptada, dispositivos auxiliares, fluorterapia e controle químico do biofilme, manejo de disfagia e risco de aspiração.
- Endodontia e cirurgia em PNE (noções): indicações em ambiente ambulatorial, exodontias simples, hemostasia, suturas e cuidados pós-operatórios com cuidador.
- Odontopediatria para PNE: cronologia de erupção alterada, hipoplasias, trauma dental recorrente, protetores bucais e estratégias de reforço positivo.
- Plano Nacional de Educação em Direitos Humanos – 2018.
- Plano Nacional de Educação em Direitos Humanos.
- Plano Nacional de Educação: Lei Federal nº 15.388, de 14 de abril de 2026.
- Plano Nacional de Educação (PNE) e políticas educacionais para a educação básica e superior.
- Plano Nacional de Educação – Lei nº 13.005/2014. Diretrizes, metas e estratégias. Expansão e qualidade da educação superior. Monitoramento e avaliação do Plano Nacional de Educação.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "educacao.plano-nacional-educacao",
 "subject": "Educação",
 "title": "Plano Nacional de Educação (PNE)",
 "version": 1,
 "status": "PUBLISHED",
 "aliases": [
  "(até 20 textos exatos da lista abaixo)"
 ],
 "scope": {
  "covers": "...",
  "excludes": "..."
 },
 "theoryTitle": "...",
 "chapters": [
  {
   "title": "1. Direito à educação, organização do sistema e LDB",
   "markdown": "## Educação como direito e dever compartilhado\nO art. **205 da Constituição Federal** estabelece educação como direito de todos e dever do Estado e da família, promovida com colaboração da sociedade, visando pleno desenvolvimento da pessoa, preparo para exercício da cidadania e qualificação para o trabalho. O art. **206** orienta o ensino por igualdade de condições de acesso e permanência, liberdade de aprender e ensinar, pluralismo de ideias e concepções pedagógicas, gratuidade do ensino públic [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Qual é a finalidade constitucional da educação?",
   "back": "Pleno desenvolvimento, preparo para cidadania e qualificação para o trabalho, CF art. 205."
  },
  {
   "front": "Qual a idade da obrigatoriedade escolar?",
   "back": "Educação básica obrigatória e gratuita dos 4 aos 17 anos, CF art. 208, I."
  }
 ],
 "tips": [
  "Associe Constituição a direito, LDB a organização, BNCC às aprendizagens essenciais e PPP ao projeto concreto da escola.",
  "Não use o PNE da década anterior como vigente: em outubro de 2026 a referência é Lei 15.388/2026."
 ],
 "traps": [
  "A prova atribui ao município atuação prioritária no ensino médio. Errado: o art. 211 prioriza municípios na infantil e fundamental, estados/DF no fundamental e médio.",
  "A prova afirma que a BNCC esgota o currículo e impede adaptação territorial. Errado: LDB art. 26 exige parte diversificada."
 ],
 "activeRecall": [
  {
   "question": "Como a Constituição distribui prioridades educacionais?",
   "answer": "Municípios: infantil e fundamental; estados/DF: fundamental e médio; União: coordenação, sistema federal e apoio redistributivo/supletivo, CF art. 211."
  }
 ],
 "errorConcepts": [
  {
   "key": "e1",
   "title": "...",
   "summary": "..."
  }
 ],
 "questions": [
  {
   "statement": "Uma escola revisa a finalidade constitucional do ensino e inclui desenvolvimento da pessoa, cidadania e qualificação para o trabalho. Qual artigo fundamenta esse conjunto?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "O art. 205 da Constituição, que apresenta a finalidade geral da educação.",
     "correct": true
    },
    {
     "key": "B",
     "text": "O art. 211 da Constituição, que delimita os resultados individuais do ensino.",
     "correct": false
    },
    {
     "key": "C",
     "text": "O art. 212-A da Constituição, que descreve competências essenciais da BNCC.",
     "correct": false
    },
    {
     "key": "D",
     "text": "O art. 214 da Constituição, que estabelece a finalidade de cada aula escolar.",
     "correct": false
    },
    {
     "key": "E",
     "text": "O art. 208 da Constituição, que dispõe sobre o currículo oculto nas redes.",
     "correct": false
    }
   ],
   "explanation": "Gabarito A. O art. 205 da Constituição reúne pleno desenvolvimento, cidadania e qualificação para o trabalho. B organiza colaboração federativa; C disciplina financiamento pelo Fundeb; D trata do plano nacional de educação; E estabelece deveres e garantias de oferta, e não a finalidade geral indicada.",
   "section": "1. Direito à educação, organização do sistema e LDB",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Uma proposta escolar combina formação cidadã, desenvolvimento integral e preparação para o trabalho, reconhecendo educação como direito de todos e dever do Estado e da família, conforme art. 205 da Constituição.",
   "format": "TRUE_FALSE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "C",
     "text": "Certo",
     "correct": true
    },
    {
     "key": "E",
     "text": "Errado",
     "correct": false
    }
   ],
   "explanation": "Certo. O art. 205 define a educação como direito de todos, dever do Estado e da família, promovida com colaboração social, para pleno desenvolvimento, cidadania e qualificação para o trabalho. Uma das finalidades não elimina as outras, que se integram.",
   "section": "1. Direito à educação, organização do sistema e LDB",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  }
 ],
 "sources": [
  {
   "kind": "OFICIAL",
   "title": "Constituição da República Federativa do Brasil de 1988",
   "publisher": "Presidência da República",
   "reference": "Arts. 205 a 214, especialmente 205, 206, 208, 211, 212-A e 214",
   "url": "https://planalto.gov.br/ccivil_03/constituicao/constituicaocompilado.htm",
   "accessedAt": "2026-10-10"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "educacao.plano-nacional-educacao").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de lei, artigo ou meta inventado.
