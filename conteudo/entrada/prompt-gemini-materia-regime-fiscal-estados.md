# Pedido: escrever a MATÉRIA "Regime de Recuperação Fiscal e Equilíbrio Fiscal dos Estados (LC 159/2017 e LC 178/2021)"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** LC 159/2017 (Regime de Recuperação Fiscal dos Estados e do DF): requisitos de adesão, plano de recuperação, vedações, conselho de supervisão; LC 178/2021 (Programa de Acompanhamento e Transparência Fiscal e Plano de Promoção do Equilíbrio Fiscal) e as alterações que fez na LRF e na LC 159; relação com a LRF e com os limites de despesa com pessoal e endividamento. Pesquise alterações posteriores (inclusive leis de 2024 e 2025 sobre renegociação de dívidas dos estados) e só cite números de que tiver certeza.
- **Não cobre:** a LRF em geral e orçamento público (matérias próprias).
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
- Regime de Recuperação Fiscal dos Estados e do Distrito Federal.
- Lei Complementar federal nº 159/2017.
- Lei Complementar Federal nº 159/2017.
- Lei Complementar Federal nº 178/2021.
- Lei Complementar nº 178/2021.
- Regime de Recuperação Fiscal.
- Regime de Recuperação Fiscal (Lei Complementar n. 159/2017).
- Lei complementar nº 159/2017.
- Lei complementar nº 178/2021.
- Programas de Recuperação Fiscal Federal e do Distrito Federal.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "direito-financeiro.regime-recuperacao-fiscal-estados",
 "subject": "Direito Financeiro",
 "title": "Regime de Recuperação Fiscal e Equilíbrio Fiscal dos Estados (LC 159/2017 e LC 178/2021)",
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
   "title": "1. Fundamentos, abrangência e planejamento fiscal",
   "markdown": "## Responsabilidade fiscal: obrigação de prevenir desequilíbrios\nA **Lei Complementar nº 101/2000 (LRF)** estabelece normas de finanças públicas voltadas à responsabilidade na gestão fiscal. No **art. 1º, § 1º**, responsabilidade significa **ação planejada e transparente**, com prevenção de riscos, correção de desvios, cumprimento de metas de resultados entre receitas e despesas e respeito a limites e condições sobre renúncia de receitas, geração de despesas, endividamento, garantias e Restos a  [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Qual o núcleo do art. 1º da LRF?",
   "back": "Ação planejada e transparente, prevenção de riscos, correção de desvios, metas e limites (art. 1º, § 1º)."
  },
  {
   "front": "Quando uma estatal é dependente?",
   "back": "Quando a empresa controlada recebe recursos nas hipóteses do art. 2º, III, ressalvado aporte de capital para aumento de participação acionária."
  }
 ],
 "tips": [
  "Leia cada prazo junto de sua unidade: RREO (bimestre), RGF (quadrimestre), RCL (doze meses móveis), programação financeira (30 dias da publicação).",
  "Faça um quadro de três colunas para AMF, ARF e reserva de contingência: meta, risco e dotação para risco, respectivamente."
 ],
 "traps": [
  "**Como aparece:** “Todo benefício fiscal exige simultaneamente absorção na LOA e compensação.” **Erro:** soma os incisos I e II do art. 14. **Correto:** exigências gerais e pelo menos uma das duas condições.",
  "**Como aparece:** “O AMF examina passivos contingentes e determina providências.” **Erro:** confunde anexos. **Correto:** isso pertence ao ARF (art. 4º, § 3º)."
 ],
 "activeRecall": [
  {
   "question": "Quais são os três instrumentos que conectam planejamento plurianual, diretrizes e orçamento anual?",
   "answer": "PPA, LDO e LOA; a LRF detalha metas e riscos na LDO, e compatibilidade e reserva de contingência na LOA (arts. 4º e 5º)."
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
   "statement": "Um ente detecta queda persistente de arrecadação e decide manter integralmente as despesas, apesar de risco concreto às metas fiscais. Qual conduta melhor reflete o art. 1º da LRF?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "Manter o programa original e justificar o resultado apenas ao fim do exercício.",
     "correct": false
    },
    {
     "key": "B",
     "text": "Identificar o risco e ajustar a gestão para proteger as metas fiscais.",
     "correct": true
    },
    {
     "key": "C",
     "text": "Substituir as metas fiscais por estimativas informais durante o exercício.",
     "correct": false
    },
    {
     "key": "D",
     "text": "Suspender toda despesa corrente até o exercício seguinte.",
     "correct": false
    },
    {
     "key": "E",
     "text": "Tratar a receita realizada como irrelevante após aprovada a peça orçamentária.",
     "correct": false
    }
   ],
   "explanation": "Gabarito B. B: O art. 1º, § 1º, exige ação planejada e transparente, prevenção de riscos e correção de desvios. A: Adia a correção que a LRF exige durante a execução. C: Metas e seus instrumentos não podem ser trocados por estimativas informais. D: A LRF prevê medidas proporcionais, com despesas legalmente protegidas. E: A execução é monitorada continuamente e importa à avaliação das metas.",
   "section": "1. Fundamentos, abrangência e planejamento fiscal",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Um ente pode tratar a responsabilidade fiscal como simples proibição de déficits, sem demonstrar prevenção de riscos ou correção de desvios durante o exercício.",
   "format": "TRUE_FALSE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "C",
     "text": "Certo",
     "correct": false
    },
    {
     "key": "E",
     "text": "Errado",
     "correct": true
    }
   ],
   "explanation": "Errado. O art. 1º, § 1º, da LC nº 101/2000 exige ação planejada e transparente, prevenção de riscos, correção de desvios, metas e observância de limites. A obrigação é de gestão fiscal responsável, não uma única regra abstrata sobre déficit.",
   "section": "1. Fundamentos, abrangência e planejamento fiscal",
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
   "title": "Lei Complementar nº 101, de 4 de maio de 2000 — texto compilado",
   "publisher": "Portal da Legislação",
   "reference": "LRF, arts. 1º a 65, texto consolidado consultado em 08/10/2026",
   "url": "https://www.planalto.gov.br/ccivil_03/leis/lcp/lcp101.htm",
   "accessedAt": "2026-10-08"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "direito-financeiro.regime-recuperacao-fiscal-estados").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de lei, artigo ou meta inventado.
