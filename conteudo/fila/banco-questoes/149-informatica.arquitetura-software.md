Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Arquitetura de software: camadas, MVC, microsserviços e padrões de projeto** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito de arquitetura de software, componentes, conectores e atributos de qualidade; estilos arquiteturais (cliente-servidor, ponto a ponto, duto e filtro, orientada a eventos, serverless em noções); arquitetura em camadas (apresentação, negócio e dados), camadas lógicas x físicas, camadas estritas x relaxadas, vantagens e desvantagens; MVC, fluxo entre Model, View e Controller, MVC na web e front controller, MVC x três camadas, MVP e MVVM; noções de arquitetura hexagonal e Clean Architecture; monólito, SOA e microsserviços (características, comunicação síncrona e assíncrona, banco por serviço, API gateway, service discovery, circuit breaker, saga, consistência eventual, strangler fig, lei de Conway); padrões de projeto GoF (propósito, escopo, os 23 padrões de criação, estruturais e comportamentais, com exemplos em Java) e noções de GRASP.
Fica de fora (outras matérias tratam): Fundamentos e pilares da orientação a objetos e princípios SOLID (em informatica.orientacao-objetos); UML, modelos de processo e métodos ágeis (em informatica.engenharia-software-ageis); HTTP, métodos, códigos de resposta e APIs em detalhe (em informatica.python-r-api); SOAP, WSDL e UDDI em detalhe; Docker, Kubernetes e ferramentas de nuvem; frameworks específicos (Spring, Java EE/Jakarta EE) e padrões Java EE; DDD em profundidade.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Arquitetura de software.
- Arquitetura de software: Interoperabilidade de sistemas.
- Arquitetura de software: Arquitetura orientada a serviços.
- Arquitetura orientada a serviços: Web services.
- Arquitetura de software: Arquitetura orientada a objetos.
- Arquitetura de software: Arquitetura de aplicações para ambiente web.
- Arquitetura de aplicações para ambiente web: Servidor de aplicações.
- Noções de Arquitetura SOA (Service Oriented Architecture).
- Padrões de projeto.
- Noções de Arquitetura Cliente-Servidor.
- Engenharia de software: Padrões de projeto e SOLID.
- SOA e Web Services: UDDI, WSDL e SOAP.
- Desenho de arquitetura de soluções.
- Camadas de Aplicação, processos, frontend, backend.
- Arquitetura cliente-servidor multicamadas.
- Design Patterns.
- Arquitetura MVC.
- SOA e web services: conceitos básicos e aplicações.
- Padrões de desenvolvimento e reuso.
- Arquitetura de software. Interoperabilidade de sistemas.
- Design de software: arquitetura hexagonal, microsserviços (orquestração de serviços e API gateway) e containers.
- Arquitetura.
- Arquiteturas de integração, SOA, Webservices e REST.
- Padrão MVC.
- Arquitetura de desenvolvimento da Plataforma Digital do Poder Judiciário (PDPJ-Br): a) Arquitetura distribuída de microsserviços: API RESTful;
- Arquitetura Limpa (Clean Architecture).
- Padrão MVC (Model-View-Controller) aplicado à web.
- Arquiteturas em camadas, baseada em serviços, microsserviços (orquestração de serviços e API gateway), orientação a eventos, cliente-servidor, serverless.
- Padrões: GoF.
- Padrões: GRASP.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.arquitetura-software.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.arquitetura-software",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
