Você é professor de cursinho preparatório e autor de questões de concurso público no Brasil. Escreva uma matéria completa de **Informática: Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco (Cebraspe, FGV, FCC, Vunesp, IBFC). Pesquise na web e use fontes oficiais.

## Escopo
Conceito de arquitetura de software, componentes, conectores e atributos de qualidade; estilos arquiteturais (cliente-servidor, ponto a ponto, duto e filtro, orientada a eventos, serverless em noções); arquitetura em camadas (apresentação, negócio e dados), camadas lógicas x físicas, camadas estritas x relaxadas, vantagens e desvantagens; MVC, fluxo entre Model, View e Controller, MVC na web e front controller, MVC x três camadas, MVP e MVVM; noções de arquitetura hexagonal e Clean Architecture; monólito, SOA e microsserviços (características, comunicação síncrona e assíncrona, banco por serviço, API gateway, service discovery, circuit breaker, saga, consistência eventual, strangler fig, lei de Conway); padrões de projeto GoF (propósito, escopo, os 23 padrões de criação, estruturais e comportamentais, com exemplos em Java) e noções de GRASP.

Fica de fora (outras matérias tratam): Fundamentos e pilares da orientação a objetos e princípios SOLID (em informatica.orientacao-objetos); UML, modelos de processo e métodos ágeis (em informatica.engenharia-software-ageis); HTTP, métodos, códigos de resposta e APIs em detalhe (em informatica.python-r-api); SOAP, WSDL e UDDI em detalhe; Docker, Kubernetes e ferramentas de nuvem; frameworks específicos (Spring, Java EE/Jakarta EE) e padrões Java EE; DDD em profundidade.

Os editais pedem este assunto assim (cubra todos os pontos que pertencem ao escopo acima). **Atenção:** se algum item da lista for claramente de outra matéria (fora do escopo — ex.: regime de servidores numa matéria de controle judicial), IGNORE esse item; nunca crie capítulo ou questões para assunto fora do escopo.
- Arquitetura de software.
- Arquitetura cliente-servidor multicamadas.
- Padrões de projeto.
- Arquitetura cliente-servidor.
- Arquitetura de software: Interoperabilidade de sistemas.
- Arquitetura de software: Arquitetura orientada a serviços.
- Arquitetura orientada a serviços: Web services.
- Arquitetura de software: Arquitetura orientada a objetos.
- Arquitetura de software: Arquitetura de aplicações para ambiente web.
- Arquitetura de aplicações para ambiente web: Servidor de aplicações.
- Noções de Arquitetura SOA (Service Oriented Architecture).
- Noções de Arquitetura Cliente-Servidor.
- Engenharia de software: Padrões de projeto e SOLID.
- Design Patterns.
- SOA e Web services: conceitos básicos e aplicações.
- Desenho de arquitetura de soluções.
- SOA e Web Services: UDDI, WSDL e SOAP.
- Arquitetura de aplicações para ambiente web.
- Camadas de Aplicação, processos, frontend, backend.
- Microsserviços.
- Arquitetura MVC.
- Arquitetura orientada a serviços.
- Padrões de projeto e análise e projeto orientados a objetos.
- Arquitetura de software e interoperabilidade de sistemas.
- Padrões de desenvolvimento e reuso.

## O que a versão atual não cobre (revisão contra os editais — inclua, se for do escopo)
- capítulo sobre interoperabilidade de sistemas: integração por APIs, web services, mensageria, formatos XML/JSON e padrões de integração, com 6 questões
- capítulo sobre web services (SOAP/WSDL/UDDI x REST, contrato, XML/JSON) e 6 questões
- capítulo sobre arquitetura de aplicações web: navegador, servidor web, servidor de aplicações, requisição/resposta HTTP, sessão e balanceamento, com 6 questões
- seção sobre servidor de aplicações x servidor web (contêiner de servlets, pool de conexões, transações, exemplos Tomcat/JBoss) e 4 questões
- capítulo sobre os cinco princípios SOLID (SRP, OCP, LSP, ISP, DIP) com exemplos em Java e 6 questões
- capítulo detalhando SOAP (envelope, header, body, fault), WSDL (types, message, portType, binding, service) e UDDI (registro e descoberta) e 6 questões
- capítulo sobre desenho de arquitetura de solução: visões arquiteturais, decisões e trade-offs entre atributos de qualidade, documentação (C4/ADR) e 5 questões
- capítulo sobre web services (conceitos, SOAP/REST, WSDL, aplicações) e 6 questões
- capítulo sobre interoperabilidade de sistemas (níveis técnico, sintático e semântico, integração por serviços e APIs) e 5 questões
- capítulo sobre padrões de integração (EIP, mensageria), web services SOAP e REST (verbos, recursos, códigos de resposta) e 8 questões

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
  "id": "informatica.arquitetura-software",
  "subject": "Informática",
  "title": "Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto",
  "version": 2,
  "status": "PUBLISHED",
  "aliases": ["Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto"],
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
- Antes de entregar, resolva cada questão como candidato: só uma alternativa defensável, gabarito certo, explicação batendo, tamanhos das alternativas equilibrados. Se a resposta ficar longa demais para uma mensagem, entregue em partes, continuando exatamente de onde parou, sem repetir. Se puder, entregue como arquivo .json para download com o nome informatica.arquitetura-software.json.
