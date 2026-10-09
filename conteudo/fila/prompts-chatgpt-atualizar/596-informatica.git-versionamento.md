Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Informática: Git e controle de versão** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Para que serve o controle de versão; modelos centralizado (CVS, SVN) e distribuído (Git, Mercurial); conceitos do Git: repositório, working tree, staging area (index), commit, hash, HEAD e estados dos arquivos; comandos init, clone, status, add, commit, log, diff, branch, checkout e switch, merge (fast-forward, three-way, --no-ff, --squash), rebase, stash, restore, reset (soft, mixed, hard) x revert, commit --amend, reflog, tag (leve e anotada), remote, fetch, pull e push; conflitos de merge e como resolvê-los; .gitignore; fluxos de trabalho Git Flow, GitHub Flow e trunk-based; pull request e merge request, fork e revisão de código; noções de GitHub e GitLab.

Fica de fora (outras matérias tratam): Pipelines de CI/CD em detalhe (GitLab CI, GitHub Actions, Jenkins), GitOps e DevOps em geral, métodos ágeis, internals do Git (formato dos objetos, packfiles), submódulos, hooks, cherry-pick e bisect em profundidade, e administração de servidores Git.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Ferramenta de versionamento Git.
- Gestão de configuração.
- Engenharia de software: Ferramentas de controle de versão.
- Git.
- Ferramentas de versionamento Git.
- Gitlab.
- Ferramenta de Gestão da configuração GIT.
- Modelo de versionamento, merge, branch, pipeline.
- Git;
- Gitlab;
- Gitflow;
- Versionamento de dados e código (Git).
- Versionamento de código: conceitos.
- SVN.
- Github.
- Controle de versões e repositórios.
- Gerência de configuração: conceitos, práticas e ferramentas.
- Gerência de configuração: Conceitos e práticas.
- Uso de ferramentas de gerência de configuração.
- Versionamento Git.
- Ferramentas de controle de versão, automação de build e integração contínua.
- Repositórios de código, versionamento, GitHub e SVN.
- Gestão de configuração, controle de versão e mudança e integração contínua.
- DevOps e Git para gestão de configuração.
- Gerência de configuração, controle de versão e mudanças e integração contínua.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- Capítulo sobre gerência de configuração de software: item de configuração, baseline, controle de mudanças, auditoria e relatórios de configuração, releases e relação com o controle de versão, com 8 questões
- Seção sobre Subversion (SVN): modelo centralizado, trunk/branches/tags, commit atômico, numeração de revisões, comandos checkout/update/commit e comparação com Git, com 6 questões
- Seção sobre versionamento semântico (MAJOR.MINOR.PATCH) e automação de build/integração contínua ligada ao fluxo Git, com 6 questões
- Seção sobre gerência de configuração aplicada a branches, tags, trunk, builds e pacotes de liberação (release), com 6 questões
- Capítulo sobre gerência de configuração de software: item de configuração, baseline, controle de mudanças, auditoria e relatórios de configuração, releases e relação com o controle de versão, com 8 questões e entrega contínua
- Seção sobre Subversion (SVN): modelo centralizado, trunk/branches/tags, commit atômico, numeração de revisões, comandos checkout/update/commit e comparação com Git, com 6 questões, além de pipelines GitLab CI/CD (stages, jobs, runners, .gitlab-ci.yml) e Jira

## Regras de qualidade (as mais importantes)
1. **Regra precisa, não vaga.** Dê a regra exata com exemplos concretos do tipo que cai (frases, casos, contas), as exceções e os casos de dúvida real que a prova explora. Quando houver norma oficial (ex.: Acordo Ortográfico, VOLP, Manual de Redação da Presidência), cite-a com precisão. Nunca invente regra.
2. **Teoria:** 5 capítulos, do básico ao avançado, cada um com pelo menos 2.500 caracteres de conteúdo útil (sem enchimento, sem "neste capítulo veremos"). Cada conceito: ideia simples → regra precisa → exemplo concreto do tipo que cai → exceção que a prova usa → como reconhecer. Tabelas para comparar institutos ou casos parecidos. O último capítulo termina com "Como o tema costuma ser cobrado".
3. **Questões: 45** — 25 de múltipla escolha (A a E) e 20 de Certo/Errado; 15 fáceis, 15 médias, 15 difíceis. Gabarito da múltipla escolha com 5 de cada letra; Certo/Errado com 10 de cada. Enunciados com caso concreto, como nas provas.
4. **Tamanho das alternativas:** a certa NÃO pode ser a mais longa nem a mais curta em mais de 10 das 25 questões de múltipla escolha. Escreva as cinco alternativas com tamanho parecido; a certa nunca é uma frase seca de 3 palavras ao lado de distratores longos.
5. **Distratores (o ponto que mais derruba qualidade):** cada alternativa errada é o que um candidato que estudou MAL marcaria — instituto ou conceito vizinho trocado, número, prazo, autor ou súmula trocados, regra certa aplicada ao caso errado, exceção esquecida. Proibido distrator que se elimina sem saber a matéria: absurdo ("toda remoção é impossível", "depende de autorização judicial prévia"), de outro assunto, que contradiz o enunciado, "todas/nenhuma das anteriores", absolutos ("sempre", "nunca", "exclusivamente") só para marcar o errado.
6. **Explicação** de cada questão (mínimo 120 caracteres): por que a certa está certa, com o dispositivo ou a regra, e por que CADA errada erra. Em Certo/Errado começa com "Certo." ou "Errado.".
7. Também: resumo completo; 18 a 25 flashcards (uma ideia por cartão, frente curta, verso de 1 a 3 frases); 5 a 8 dicas práticas; 5 a 8 pegadinhas (como a prova escreve, por que está errado, versão certa); 6 a 10 perguntas de recordação ativa com resposta; **de 4 a 6** "conceitos que geram erro" (chaves e1 a e6); fontes reais que você abriu.
8. Texto neutro: não escreva o nome de nenhuma banca, órgão ou cargo, nem a palavra "banca" (use "a prova").

## Formato de entrega
Um único bloco de código JSON válido, exatamente neste formato (campos e grafia iguais):

```json
{
  "id": "informatica.git-versionamento",
  "subject": "Informática",
  "title": "Git e controle de versão",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Git e controle de versão"],
  "scope": { "covers": "o que a matéria cobre", "excludes": "o que fica de fora" },
  "theoryTitle": "título completo da teoria",
  "chapters": [ { "title": "1. ...", "markdown": "texto em Markdown (## e ###, listas, **negrito**, tabelas | a | b |, alertas com >)" } ],
  "summary": "resumo em Markdown",
  "flashcards": [ { "front": "pergunta curta", "back": "resposta curta" } ],
  "tips": ["dica 1"],
  "traps": ["pegadinha 1"],
  "activeRecall": [ { "question": "pergunta", "answer": "resposta" } ],
  "errorConcepts": [ { "key": "e1", "title": "nome do erro", "summary": "explicação corretiva" } ],
  "questions": [
    {
      "statement": "enunciado (pergunta ou comando)",
      "format": "MULTIPLE_CHOICE",
      "difficulty": "FACIL",
      "options": [
        { "key": "A", "text": "...", "correct": false },
        { "key": "B", "text": "...", "correct": true },
        { "key": "C", "text": "...", "correct": false },
        { "key": "D", "text": "...", "correct": false },
        { "key": "E", "text": "...", "correct": false }
      ],
      "explanation": "Gabarito B. ...",
      "section": "título EXATO de um dos capítulos",
      "errorConceptKey": "e1",
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
    },
    {
      "statement": "afirmação para julgar",
      "format": "TRUE_FALSE",
      "difficulty": "MEDIA",
      "options": [ { "key": "C", "text": "Certo", "correct": false }, { "key": "E", "text": "Errado", "correct": true } ],
      "explanation": "Errado. ...",
      "section": "título EXATO de um dos capítulos",
      "errorConceptKey": null,
      "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
    }
  ],
  "sources": [ { "kind": "OFICIAL", "title": "...", "publisher": "...", "reference": "...", "url": "https://...", "accessedAt": "AAAA-MM-DD" } ]
}
```
- difficulty: "FACIL", "MEDIA" ou "DIFICIL". format: "MULTIPLE_CHOICE" ou "TRUE_FALSE". kind das fontes: "OFICIAL" ou "COMPLEMENTAR".
- IMPORTANTE: escreva cada explicação de forma própria, à mão. Proibido usar frases-molde repetidas entre questões (ex.: "não descreve o critério aplicável", "A análise deve distinguir..."). Em múltipla escolha, explique por que CADA alternativa errada está errada com o conceito específico dela. Não estique alternativas com enfeites como ", segundo o caso apresentado".
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome informatica.git-versionamento.json.
