# Pedido: escrever a MATÉRIA "Gestão Pública: Participação, Resultados e Comunicação"

Você é professor de cursinho para concursos públicos no Brasil. Escreva uma **matéria completa de estudo** (teoria + revisão + questões) para o aplicativo Estudário, no formato JSON do final deste pedido.

## Escopo
- **Cobre:** processos participativos de gestão pública (conselhos, orçamento participativo, audiências e consultas públicas, controle social); gestão de resultados na produção de serviços públicos (contratualização, indicadores, metas, avaliação de desempenho institucional, gestão por resultados); comunicação na gestão pública e gestão de redes organizacionais; transparência e accountability ligadas a esses temas.
- **Não cobre:** modelos de administração pública (patrimonialista, burocrático, gerencial), planejamento estratégico e governo digital, que têm matérias próprias.
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
- Gestão de resultados em serviços públicos e construção de indicadores.
- Processos participativos de gestão pública.
- Educação, comunicação e participação social em saúde.
- Desastres ambientais e participação social.
- Gestão de redes organizacionais.
- Processos participativos de gestão pública: conselhos de gestão, orçamento participativo, parceria entre governo e sociedade.
- Mobilização, organização e participação social nos processos de gestão das instituições estatais: conselhos, conferências e outros fóruns.
- Controle Social e Participação em Políticas Públicas.
- Participação política e controle social na gestão pública.
- Cooperação, Participação e Ação Coletiva: teorias da ação coletiva; cooperação em organizações públicas; participação institucional; engajamento; cidadania; controle social; trabalho colaborativo; equipes multiprofissionais; integração intersetorial.
- Legislação e Normas Aplicáveis: Constituição Federal – direitos sociais, ordem social, cidadania, participação social e organização do Estado.
- Intersetorialidade, controle social, conselhos, participação popular.
- Controle social e cidadania.
- Participação social e cidadania.
- Gestão por resultados.
- Comunicação na gestão pública e na gestão de redes organizacionais.
- Gestão de resultados na produção de serviços públicos.
- Gestão de resultados e solução de conflitos.
- Participação e controle social na Constituição Federal.
- Participação e controle social.
- Participação social.
- Processos participativos de gestão pública. Qualidade na administração pública.
- Gestão por resultados na produção de serviços públicos.
- Atores sociais, controle social, gestão local, cidadania e equidade social.
- Comunicação na gestão pública e gestão de redes organizacionais.
- Gestão por resultados na produção de serviços públicos. Métricas de aferição de resultados. Trabalho híbrido.
- Processos Participativos e Educativos: Participação social e gestão participativa.
- Controle social: participação da sociedade na fiscalização da administração pública.
- Processos participativos de gestão pública: conselhos de gestão, orçamento participativo e parceria entre governo e sociedade.
- Perguntas e fontes: fontes documentais, administrativas, estatísticas e de participação social.
- Participação social nos processos de gestão das políticas públicas: conselhos, conferências, audiências públicas, consultas públicas, PPA participativo, orçamento participativo e outros mecanismos.
- Questões Brasileiras Contemporâneas: mobilização, organização e participação social, conselhos, conferências e outros fóruns.
- Processos participativos: conselhos de gestão, orçamento participativo e parcerias entre governo e sociedade.
- Qualidade na Administração Pública e gestão por resultados.
- Controle social e cidadania; accountability.
- Políticas de segurança pública no Estado Democrático de Direito e participação social.
- Lobby, grupo de pressão, controle social e accountability.
- Amicus Curiae, Audiências Públicas, Consultas Públicas, Fóruns interinstitucionais e Grupos de articulação e comissões de conciliação em políticas públicas. Lobby, grupo de pressão, controle social e accountability.
- Direito à voz e direito a ser ouvido. Participação no Poder Legislativo: comissões parlamentares de participação social.
- Participação junto ao Poder Legislativo: comissões parlamentares de participação social.

## Formato (exemplo de estrutura com trechos de outra matéria; siga os mesmos campos e tipos)
```json
{
 "id": "administracao-publica.gestao-participativa-resultados-comunicacao",
 "subject": "Administração Pública",
 "title": "Gestão Pública: Participação, Resultados e Comunicação",
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
   "title": "1. Patrimonialismo, burocracia e evolução das formas de Estado",
   "markdown": "## Patrimonialismo e Estado oligárquico\nO **patrimonialismo** ocorre quando a direção estatal é exercida como extensão dos interesses pessoais de quem governa, sem separação institucional suficiente entre patrimônio público e privado. Nomeações por parentesco, distribuição de favores, troca de proteção por lealdade pessoal e uso seletivo de recursos coletivos são manifestações típicas. Em um caso concreto, a autoridade que distribui funções administrativas a aliados para manter apoio político, i [...]"
  }
 ],
 "summary": "...",
 "flashcards": [
  {
   "front": "Patrimonialismo?",
   "back": "Confusão entre patrimônio/interesses do governante e recursos e funções públicas."
  },
  {
   "front": "Burocracia de Weber?",
   "back": "Modelo racional-legal, baseado em regras impessoais, hierarquia, especialização e profissionalização."
  }
 ],
 "tips": [
  "Separe conceito ideal de burocracia das suas disfunções; regras e impessoalidade não significam automaticamente ineficiência.",
  "Associe cada reforma à data correta: DASP 1938, Decreto-Lei 200/1967, Plano Diretor 1995 e EC 19/1998."
 ],
 "traps": [
  "A prova descreve hierarquia e normas como disfunções burocráticas. Elas pertencem ao tipo ideal; disfunção é o exagero que desvia a finalidade.",
  "A prova afirma que descentralização e delegação são a mesma operação. São princípios distintos no art. 6º do Decreto-Lei 200/1967."
 ],
 "activeRecall": [
  {
   "question": "Como diferenciar patrimonialismo da burocracia racional-legal?",
   "answer": "No patrimonialismo, relações pessoais e interesses privados condicionam o Estado; na burocracia, competências e decisões são regidas por regras impessoais."
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
   "statement": "Uma administração distribui funções a aliados pessoais, independentemente de critérios técnicos, e utiliza recursos públicos para assegurar fidelidade política. Qual modelo melhor caracteriza essa prática?",
   "format": "MULTIPLE_CHOICE",
   "difficulty": "FACIL",
   "options": [
    {
     "key": "A",
     "text": "Patrimonialista, pela confusão entre interesses particulares e funções públicas.",
     "correct": true
    },
    {
     "key": "B",
     "text": "Burocrático racional-legal, pela hierarquia institucional das escolhas.",
     "correct": false
    },
    {
     "key": "C",
     "text": "Gerencial, pela autonomia na seleção de agentes de execução.",
     "correct": false
    },
    {
     "key": "D",
     "text": "Pós-gerencial, pela participação de atores sociais na direção.",
     "correct": false
    },
    {
     "key": "E",
     "text": "Regulador, pela organização de interesses em categorias econômicas.",
     "correct": false
    }
   ],
   "explanation": "Gabarito A. A troca pessoal de favores e o emprego privado do aparelho estatal caracterizam patrimonialismo. B requer impessoalidade e competências formais, ausentes no caso; C confunde discricionariedade gerencial com favorecimento; D pressupõe redes responsáveis, não lealdades pessoais; E trata da função regulatória do Estado.",
   "section": "1. Patrimonialismo, burocracia e evolução das formas de Estado",
   "errorConceptKey": "e1",
   "sourceType": "AUTHORIAL",
   "board": null,
   "agency": null,
   "year": null,
   "sourceUrl": null
  },
  {
   "statement": "Uma administração que separa as decisões de interesses pessoais, mantém atribuições por regras impessoais e documenta procedimentos apresenta características associadas à burocracia racional-legal.",
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
   "explanation": "Certo. Competência institucional, impessoalidade, hierarquia de funções e documentação são elementos do modelo racional-legal de Weber. A existência de normas não equivale à disfunção burocrática; essa surge quando formalismo e rigidez deixam de servir ao interesse público.",
   "section": "1. Patrimonialismo, burocracia e evolução das formas de Estado",
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
   "title": "Decreto nº 9.203, de 22 de novembro de 2017 (texto atualizado)",
   "publisher": "Presidência da República",
   "reference": "Arts. 1º a 6º, 7º-A, 8º-B, 9º-A, 13-A, 15-A, 16, 17 e 19",
   "url": "https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2017/decreto/d9203.htm",
   "accessedAt": "2026-10-10"
  }
 ]
}
```

## Entrega
- Um único arquivo/bloco JSON válido com a matéria inteira (id "administracao-publica.gestao-participativa-resultados-comunicacao").
- Se não couber numa resposta: entregue a teoria e a revisão primeiro (chapters, summary, flashcards, tips, traps, activeRecall, errorConcepts) num JSON fechado, escreva "continua", e na próxima resposta entregue só {"questions":[...], "sources":[...]}.
- Antes de entregar, confira: 45 questões, 25 MC + 20 C/E, 15/15/15 de dificuldade, nenhum número de lei, artigo ou meta inventado.
