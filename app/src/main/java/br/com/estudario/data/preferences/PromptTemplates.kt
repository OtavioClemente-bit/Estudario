package br.com.estudario.data.preferences

object PromptTemplates {
    const val PLANO = """COMO ENTREGAR A RESPOSTA, ISTO VEM ANTES DE QUALQUER OUTRA INSTRUÇÃO:
1. Entregue UM ARQUIVO para download, com a extensão indicada. Se você tiver ferramenta de gerar arquivos (interpretador de código, análise de dados, canvas ou documento), use-a e me devolva o arquivo pronto para baixar.
2. Não escreva NADA fora do arquivo: sem introdução, sem explicação do que você fez, sem resumo, sem aviso e sem pergunta no final. A resposta é o arquivo e nada mais.
3. Não cole o conteúdo do arquivo na conversa. Quem vai ler esse JSON é o aplicativo, não uma pessoa.
4. Somente se você realmente não conseguir gerar arquivo: responda com o JSON puro, começando em "{" e terminando em "}", sem crases, sem bloco de código e sem uma única palavra fora dele.
5. Se o conteúdo for longo, vá até o fim dentro do mesmo arquivo. Não corte no meio, não resuma para caber e não pergunte se deve continuar.

Crie um arquivo .plano compatível com o aplicativo Estudário para organizar meus estudos com base nas informações abaixo.

INFORMAÇÕES PARA O PLANO:
- Concurso: [NOME DO CONCURSO]
- ID externo do concurso (externalId): [EXATAMENTE COMO CADASTRADO NO APP]
- Nome do plano: [NOME]
- Objetivo: [OBJETIVO]
- Data de início: [AAAA-MM-DD]
- Data da prova: [AAAA-MM-DD OU null]
- Disponibilidade: [MINUTOS LÍQUIDOS POR DIA, SEGUNDA A DOMINGO; informe dias indisponíveis]
- Meta semanal de questões: [NÚMERO]
- Meta mensal de discursivas: [NÚMERO]
- Matérias e respectivos externalId: [LISTA EXATA DO APP]
- Prioridades por matéria: [CRITICAL, HIGH, MEDIUM ou LOW]
- Manutenção mínima semanal por matéria em minutos: [VALORES]
- Edital e tópicos com seus externalId: [COLE/ANEXE O EDITAL E OS IDs DISPONÍVEIS]
- Preferências ou restrições: [DIAS DE DESCANSO, FOCO, MÉTODO, ETC.]

REGRAS DO PLANEJAMENTO:
- Monte um plano realista com fases anuais, focos mensais, metas semanais e tarefas datadas.
- Respeite rigorosamente os minutos disponíveis; não crie capacidade nem horários fictícios. Distribua teoria, questões, revisão e recuperação ativa, priorizando matérias críticas e revisões vencidas.
- Planeje todas as datas entre dataInicio e dataProva, se houver. Datas devem usar AAAA-MM-DD.
- Vincule matéria e tópico somente usando externalId fornecido. Se não houver ID confirmado, use null no vínculo e informe o nome em materiaNome ou topicoNome; nunca invente IDs.
- IDs de fases, meses, semanas e tarefas devem ser únicos e estáveis dentro do arquivo. Não crie histórico de execução.
- Defina active e masterPlan como false. Deixe baseRevision como null.
- Use SIMPLE se a disponibilidade for uniforme; ADVANCED se variar por dia. SIMPLE ainda exige os sete dias na lista dias.
- Enumeradores permitidos: modo SIMPLE ou ADVANCED; prioridade CRITICAL, HIGH, MEDIUM ou LOW; tipo de tarefa THEORY, QUESTIONS, REVIEW, ACTIVE_RECALL, FLASHCARDS, SIMULATION ou DISCURSIVE; status PLANEJADA; origem IMPORTED.
- tarefas[].dependencias deve conter IDs de tarefas existentes e não pode formar ciclos. Use lista vazia quando não houver dependências.
- Não invente dados ausentes. Se faltar informação essencial para cumprir o formato, use valores neutros válidos e registre a premissa em metadata.

FORMATO OBRIGATÓRIO:
- Responda somente com JSON válido, sem Markdown, sem bloco de código e sem texto fora do JSON.
- Use format "estudario-plano" e version 1.
- Inclua todos os campos abaixo; arrays podem ficar vazios quando não houver dados suficientes.

{
  "format": "estudario-plano",
  "version": 1,
  "planId": "UUID",
  "concurso": { "externalId": "ID-EXATO", "nome": "NOME DO CONCURSO" },
  "nome": "NOME DO PLANO",
  "objetivo": "OBJETIVO",
  "active": false,
  "masterPlan": false,
  "dataInicio": "AAAA-MM-DD",
  "dataProva": null,
  "baseRevision": null,
  "configuracao": {
    "modo": "ADVANCED",
    "dias": [
      { "dia": 1, "minutos": 120, "indisponivel": false },
      { "dia": 2, "minutos": 120, "indisponivel": false },
      { "dia": 3, "minutos": 120, "indisponivel": false },
      { "dia": 4, "minutos": 120, "indisponivel": false },
      { "dia": 5, "minutos": 120, "indisponivel": false },
      { "dia": 6, "minutos": 0, "indisponivel": true },
      { "dia": 7, "minutos": 0, "indisponivel": true }
    ],
    "questoesSemanais": 100,
    "discursivasMensais": 2
  },
  "prioridades": [
    { "externalId": "ID-EXATO-DA-MATERIA", "nome": "Matéria", "prioridade": "HIGH", "manutencaoMinutos": 60, "pausada": false, "posicao": 0 }
  ],
  "fasesAnuais": [
    { "id": "fase-01", "nome": "Fundamentos", "objetivo": "...", "criterioConclusao": "...", "inicio": "AAAA-MM-DD", "fim": "AAAA-MM-DD", "metaMinutos": 0, "metaQuestoes": 0, "metaDiscursivas": 0, "metaPercentual": 0 }
  ],
  "planosMensais": [
    { "id": "mes-01", "mes": "AAAA-MM", "foco": "...", "metaMinutos": 0, "metaQuestoes": 0, "metaDiscursivas": 0, "metaPercentual": 0 }
  ],
  "planosSemanais": [
    { "id": "semana-01", "inicio": "AAAA-MM-DD", "objetivo": "...", "metaMinutos": 0, "metaQuestoes": 0, "metaDiscursivas": 0 }
  ],
  "tarefas": [
    { "id": "tarefa-01", "materiaExternalId": "ID-EXATO-OU-NULL", "topicoExternalId": "ID-EXATO-OU-NULL", "materiaNome": "Matéria", "topicoNome": "Tópico", "data": "AAAA-MM-DD", "tipo": "THEORY", "minutos": 60, "questoes": 0, "prioridade": "HIGH", "status": "PLANEJADA", "origem": "IMPORTED", "locked": false, "observacoes": "...", "dependencias": [] }
  ],
  "metadata": { "premissas": "..." }
}

Antes de responder, valide JSON puro, formato e versão, datas, enumeradores, IDs únicos, externalIds não inventados, dependências válidas e capacidade diária/semanal sem exceder a disponibilidade informada."""

    const val EDITAL = """COMO ENTREGAR A RESPOSTA, ISTO VEM ANTES DE QUALQUER OUTRA INSTRUÇÃO:
1. Entregue UM ARQUIVO para download, com a extensão indicada. Se você tiver ferramenta de gerar arquivos (interpretador de código, análise de dados, canvas ou documento), use-a e me devolva o arquivo pronto para baixar.
2. Não escreva NADA fora do arquivo: sem introdução, sem explicação do que você fez, sem resumo, sem aviso e sem pergunta no final. A resposta é o arquivo e nada mais.
3. Não cole o conteúdo do arquivo na conversa. Quem vai ler esse JSON é o aplicativo, não uma pessoa.
4. Somente se você realmente não conseguir gerar arquivo: responda com o JSON puro, começando em "{" e terminando em "}", sem crases, sem bloco de código e sem uma única palavra fora dele.
5. Se o conteúdo for longo, vá até o fim dentro do mesmo arquivo. Não corte no meio, não resuma para caber e não pergunte se deve continuar.

Vou enviar o conteúdo programático de um edital. Gere um arquivo .estudo compatível com o aplicativo Estudário.

OBJETIVO: montar a estrutura completa, preservando todas as matérias, tópicos, subtópicos e a ordem oficial. Não omita, resuma, una nem invente itens.

PROIBIDO INVENTAR MATÉRIA OU TÓPICO:
- Use APENAS o que está escrito no edital. Não acrescente matéria, tópico ou assunto que não esteja lá, nem para "completar o que falta", nem porque "costuma cair".
- Copie o nome de cada matéria e tópico como está escrito, com a mesma grafia, numeração e ordem. Não troque pelo nome "padrão de mercado".
- Matéria listada sem conteúdo programático fica com topicos vazio. Não preencha por conta própria.
- Item ilegível ou ambíguo: reproduza como conseguir ler e registre a dúvida em observacoes. Não chute.
- Dividir um item longo em subtopicos é desejável (contentOriginType DIDACTIC_SUBDIVISION). Criar assunto que não aparece no edital, não.

REGRAS OBRIGATÓRIAS:
- Responda somente com JSON válido, sem bloco Markdown e sem texto adicional.
- Use version 2 e packageId único.
- IDs minúsculos, únicos, sem acentos e separados por hífen.
- Nesta etapa deixe teorias, resumos e questoes como listas vazias.
- Represente subdivisões recursivamente em subtopicos.
- Mantenha os mesmos IDs em todos os pacotes futuros. O ID é a identidade permanente do item no Plano Mestre.
- Use contentOriginType: EDITAL para texto literal do edital, DIDACTIC_SUBDIVISION para divisão didática e AUXILIARY_CONTENT para fundamento complementar.
- Em priorityAssessment, avalie a importância para a prova com base, nesta ordem, em quantidade oficial de questões, peso oficial, pontuação oficial, critério eliminatório, distribuição oficial, histórico fornecido de banca/cargo, recorrência demonstrável e relevância estrutural. Use inferência somente na falta de evidência superior.
- priorityAssessment exige score inteiro de 0 a 100, source permitido, confidence de 0.0 a 1.0, rationale curto e evidence verificável. Sem evidência, use score 50, source DEFAULT, confidence 0.0 e não invente estatísticas, percentuais ou frequências.

ESTRUTURA DE CADA TÓPICO:
{
  "id": "materia-topico",
  "titulo": "Título conforme o edital",
  "descricao": "Escopo curto",
  "ordem": 0,
  "prioridade": "NORMAL",
  "contentOriginType": "EDITAL",
  "observacoes": "",
  "teorias": [],
  "resumos": [],
  "questoes": [],
  "subtopicos": []
}

RAIZ DO ARQUIVO:
{
  "version": 2,
  "packageId": "edital-ORGAO-CARGO-ANO-v1",
  "concurso": { "id": "concurso-orgao-cargo-ano", "nome": "NOME DO CONCURSO, CARGO", "principal": true },
  "padroesQuestao": { "banca": "BANCA", "orgao": "ORGAO", "ano": 2026, "origem": "Material de estudo gerado" },
  "materias": [{ "id": "materia", "nome": "Matéria", "ordem": 0, "topicos": [] }]
}

Antes de responder, confira internamente: JSON válido, todos os itens do edital presentes, IDs sem repetição e nenhuma vírgula sobrando.

TEXTO DO EDITAL:
[COLE O EDITAL AQUI]"""

    const val CONTEUDO = """COMO ENTREGAR A RESPOSTA, ISTO VEM ANTES DE QUALQUER OUTRA INSTRUÇÃO:
1. Entregue UM ARQUIVO para download, com a extensão indicada. Se você tiver ferramenta de gerar arquivos (interpretador de código, análise de dados, canvas ou documento), use-a e me devolva o arquivo pronto para baixar.
2. Não escreva NADA fora do arquivo: sem introdução, sem explicação do que você fez, sem resumo, sem aviso e sem pergunta no final. A resposta é o arquivo e nada mais.
3. Não cole o conteúdo do arquivo na conversa. Quem vai ler esse JSON é o aplicativo, não uma pessoa.
4. Somente se você realmente não conseguir gerar arquivo: responda com o JSON puro, começando em "{" e terminando em "}", sem crases, sem bloco de código e sem uma única palavra fora dele.
5. Se o conteúdo for longo, vá até o fim dentro do mesmo arquivo. Não corte no meio, não resuma para caber e não pergunte se deve continuar.

Crie um arquivo .estudo version 2 para o aplicativo Estudário com LIVRO COMPLETO, RESUMO COMPLETO, REVISÃO RÁPIDA, MEMORIZAÇÃO ATIVA e QUESTÕES para o tópico indicado.

DADOS:
- Concurso: [NOME EXATO NO APP]
- Matéria: [NOME EXATO NO APP]
- Caminho do tópico: [TÓPICO PAI > SUBTÓPICO]
- Banca/órgão/ano: [PREENCHA]
- Quantidade de questões: [10]
- Fontes ou texto-base: [COLE OU ANEXE AQUI]
- O priorityAssessment já salvo é contexto do tópico. preserve esse bloco quando ele aparecer no modelo e não recalcule a importância genérica; o app mantém a avaliação local quando o pacote não o trouxer.

TEORIA COMPLETA, TRATE COMO UM LIVRO:
- Escreva material longo, didático e autossuficiente, não um resumo ampliado.
- Divida em capítulos e seções numa sequência pedagógica: fundamentos, desenvolvimento, exemplos, aplicações, pegadinhas da banca e revisão do capítulo.
- Explique termos na primeira ocorrência, use exemplos concretos, comparações, tabelas Markdown quando ajudarem e conecte os conceitos.
- Cada capítulo deve possuir vários parágrafos substanciais. Não use frases soltas para simular profundidade.
- Cubra integralmente o escopo informado, sem inventar leis, números, jurisprudência ou versões. Indique a data de referência quando o conteúdo puder mudar.
- O campo markdown de cada capítulo aceita títulos, subtítulos, listas, negrito, tabelas e citações.

FONTES (campo "fontes"), OBRIGATÓRIO:
- Liste as fontes que você realmente abriu para escrever este material. Cada item: tipo ("OFICIAL" ou "COMPLEMENTAR"), titulo, publicador, referencia (artigo/seção/página), url exata e acessadoEm (AAAA-MM-DD).
- É essa lista que o app guarda e mostra para a pessoa conferir depois. Fonte sem título não entra.
- Não liste fonte que não abriu, não invente URL, título, órgão, página ou data, e não chame fonte complementar de oficial.

RESUMO COMPLETO E REVISÃO RÁPIDA:
- O summary consolida toda a teoria de modo estruturado, ainda detalhado o bastante para estudar.
- O quickReview é uma revisão de poucos minutos: conceitos-chave, diferenças, fórmulas e regras.
- Em tips, escreva bizus objetivos. Em traps, erros e confusões típicas de prova.
- Em activeRecall, escreva perguntas curtas que obriguem o aluno a lembrar sem olhar a resposta.
- Em errorConcepts, agrupe conceitos que provavelmente originam erros, com título e explicação corretiva.

PROIBIDO INVENTAR:
- Escreva apenas sobre o tópico informado nos DADOS. Não amplie para assuntos vizinhos nem crie tópico novo.
- Copie concurso, matéria e tópico exatamente como estão escritos nos DADOS. Um caractere diferente e o app não acha onde encaixar.
- Não invente lei, artigo, súmula, número, prazo, percentual, jurisprudência ou versão de norma. Sem certeza, escreva o conceito sem o número.

QUESTÕES:
- PRIORIZE QUESTÕES QUE JÁ CAÍRAM EM PROVA. Antes de criar qualquer questão autoral, procure questões reais de concursos anteriores sobre este tópico, preferindo a banca informada. Confira o enunciado e o gabarito na fonte oficial.
  • Questão real verificada: questionSourceType "REAL", com banca, orgao, ano, sourceId e sourceUrl reais.
  • Questão real reescrita com outras palavras: "REAL_ADAPTED", mantendo banca/orgao/ano da original.
  • Só quando não existir questão real sobre o ponto: "AUTHORIAL", no estilo da banca, com banca/orgao/ano/sourceId em null.
  • NUNCA marque como REAL uma questão que você criou, e nunca invente banca, órgão, ano ou fonte para parecer real. Isso é pior que não ter questão real nenhuma.
- FORMATO MISTO, obrigatório: cerca de 70% de múltipla escolha com 5 alternativas (chaves A, B, C, D, E) e 30% no estilo Certo/Errado. Múltipla escolha SEMPRE em maior número. Com 20 questões: 14 de A a E e 6 de Certo/Errado.
  • Certo/Errado: o enunciado é uma afirmação a ser julgada e há exatamente 2 alternativas, chave "C" com texto "Certo" e chave "E" com texto "Errado", uma delas correta. Não escreva "(Certo ou Errado)" no enunciado.
  • Alterne os dois formatos ao longo da lista em vez de agrupar por tipo.
- NÍVEL: prova difícil de verdade. Pelo menos metade DIFICIL, o resto MEDIA, no máximo uma FACIL.
  • Difícil é caso concreto em vez de definição, exceção à regra, prazo/competência/requisito parecido com outro, comparação entre institutos vizinhos, alternativa correta que exige descartar duas quase certas.
  • Difícil NÃO é texto confuso, enunciado ambíguo nem pegadinha de português.
  • Cada distrator precisa ser o erro que alguém que ESTUDOU cometeria. Distrator descartável de bate-pronto deve ser trocado.
- Explicação: diga por que a correta está certa E por que cada errada está errada, citando a fonte da resposta.
- COBERTURA: cada questão cobra um ponto DIFERENTE do tópico. Não reformule o mesmo conceito várias vezes. Priorize o que a banca cobra de verdade, prazo, competência, exceção, quórum, requisito, hipótese de cabimento, em vez de definição de manual.
- PROIBIDO (entregam o gabarito de graça): alternativa "todas as anteriores" ou "nenhuma das anteriores"; absolutos como "sempre", "nunca", "em nenhuma hipótese" usados só para marcar o distrator errado; e a correta ser visivelmente a mais longa ou detalhada. Todas as alternativas com tamanho e detalhe parecidos.
- GABARITO DISTRIBUÍDO: espalhe a letra correta entre A, B, C, D e E, não concentre em B e C. Nas de Certo/Errado, aproxime metade de certos e metade de errados.
- ENUNCIADO no estilo da banca informada: use o verbo de comando dela ("julgue o item", "assinale a alternativa correta", "é correto afirmar") e o tamanho típico de enunciado dela.
- CONCEITO DO ERRO (campo "conceitoErro"): em cada questão, informe o id de um item de errorConcepts deste mesmo arquivo, o conceito que a pessoa não domina quando erra essa questão. É assim que o caderno de erros mostra o padrão em vez de uma lista solta. Se o conceito não existir na lista, crie-o em errorConcepts.
- VÍNCULO COM O MATERIAL (campo "secao"): em TODA questão, preencha "secao" com o título EXATO de um capítulo da teoria ou de uma seção do resumo deste mesmo arquivo, o trecho que responde a questão. Copie o título caractere por caractere, sem acrescentar numeração nem reescrever. É esse campo que faz o app abrir a revisão no ponto certo quando a pessoa erra. Se nenhuma seção explica o ponto, corrija o material para cobri-lo em vez de deixar vazio.
- Use dificuldade FACIL, MEDIA ou DIFICIL e tags específicas.

FORMATO OBRIGATÓRIO (modelo simples recomendado para um tópico):
- Responda somente com JSON válido, sem ``` e sem qualquer texto fora do JSON.
- Use packageId estável no padrão conteudo-MATERIA-TOPICO. Para corrigir ou ampliar depois, mantenha o mesmo packageId; o app oferecerá “Atualizar”.
- Repita exatamente concurso, matéria e tópico existentes no aplicativo.
- IDs devem ser únicos em todo o pacote.

USE EXATAMENTE ESTA ESTRUTURA:
{
"version": 2,
"packageId": "conteudo-materia-topico",
"competition": "NOME EXATO DO CONCURSO",
"subject": "NOME EXATO DA MATÉRIA",
"topic": "NOME EXATO DO TÓPICO",
"teorias": [
  {
    "id": "teoria-topico-v1",
    "titulo": "Teoria completa, Nome do tópico",
    "capitulos": [
      { "id": "cap-01", "titulo": "1. Fundamentos", "markdown": "Texto longo em Markdown..." },
      { "id": "cap-02", "titulo": "2. Desenvolvimento", "markdown": "Texto longo em Markdown..." }
    ]
  }
],
"summary": "# Resumo completo\n\nConteúdo estruturado...",
"quickReview": "# Revisão rápida\n\nConteúdo para poucos minutos...",
"tips": ["Bizu objetivo 1", "Bizu objetivo 2"],
"traps": ["Pegadinha ou confusão frequente 1"],
"activeRecall": ["Pergunta de recuperação ativa 1?", "Pergunta 2?"],
"questoes": [
  {
    "id": "q-topico-001",
    "questionSourceType": "AUTHORIAL",
    "sourceId": null,
    "sourceUrl": null,
    "enunciado": "...",
    "alternativas": [
      { "chave": "A", "texto": "...", "correta": false },
      { "chave": "B", "texto": "...", "correta": true },
      { "chave": "C", "texto": "...", "correta": false },
      { "chave": "D", "texto": "...", "correta": false },
      { "chave": "E", "texto": "...", "correta": false }
    ],
    "explicacao": "...",
    "dificuldade": "DIFICIL",
    "secao": "1. Fundamentos",
    "conceitoErro": "erro-conceito-01",
    "banca": null,
    "orgao": null,
    "ano": null,
    "tags": ["tag"]
  },
  {
    "id": "q-topico-002",
    "questionSourceType": "REAL",
    "sourceId": "banca-orgao-ano-q42",
    "sourceUrl": "https://url-oficial-realmente-consultada",
    "banca": "BANCA",
    "orgao": "ORGAO",
    "ano": 2024,
    "enunciado": "Afirmação a ser julgada...",
    "alternativas": [
      { "chave": "C", "texto": "Certo", "correta": true },
      { "chave": "E", "texto": "Errado", "correta": false }
    ],
    "explicacao": "...",
    "dificuldade": "DIFICIL",
    "secao": "2. Desenvolvimento",
    "conceitoErro": "erro-conceito-01",
    "tags": ["tag"]
  }
],
"errorConcepts": [
  { "id": "erro-conceito-01", "title": "Conceito que gera confusão", "summary": "Correção clara e curta." }
],
"fontes": [
  { "id": "fonte-01", "tipo": "OFICIAL", "titulo": "Lei 0.000/0000", "publicador": "Órgão responsável", "referencia": "Art. 1º a 5º", "url": "https://...", "acessadoEm": "AAAA-MM-DD" },
  { "id": "fonte-02", "tipo": "COMPLEMENTAR", "titulo": "Título do material", "publicador": "Instituição", "referencia": "Capítulo 3", "url": "https://...", "acessadoEm": "AAAA-MM-DD" }
],
"tags": ["tema", "subtema"]
}

Antes de responder, valide internamente: JSON puro e válido; livro realmente completo; summary diferente de quickReview; arrays presentes; quantidade solicitada de questões; exatamente uma correta por questão; nenhuma vírgula sobrando."""
}
