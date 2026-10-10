Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Correio eletrônico (Outlook 2016) e navegação (Google Chrome)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceitos de email, endereços, campos Para/Cc/Cco, mensagens, anexos, envio, resposta e encaminhamento; Outlook 2016 clássico, pastas, pesquisa, regras e organização; Chrome 103 ou superior, abas, histórico, downloads, favoritos, privacidade e segurança.
Fica de fora (outras matérias tratam): Administração de servidores de correio, Exchange avançado, desenvolvimento web, protocolos em profundidade e funcionalidades exclusivas de edições posteriores do Outlook.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Microsoft Outlook 2016: Correio Eletrônico.
- Google Chrome 103.x ou superior: Navegação na Internet.
- Programas de correio eletrônico (Outlook Express e Mozilla Thunderbird).
- Correio Eletrônico.
- Programas de correio eletrônico (Outlook Express, Mozilla Thunderbird e similares).
- Uso de correio eletrônico.
- Preparo e envio de mensagens.
- Anexação de arquivos.
- Programas de correio eletrônico (Outlook Express, e Mozilla Thunderbird).
- Redes de computadores: programas de correio eletrônico (Microsoft Outlook, Outlook Express).
- Navegadores Microsoft Edge e Google Chrome e correio eletrônico Microsoft Outlook.
- Programas de correio eletrônico (Outlook Express).
- Ferramentas de comunicação e colaboração: correio eletrônico (webmail, cliente de e-mail).
- Correio eletrônico - Gmail, Outlook (envio, recebimento, anexos, segurança e etiqueta digital).
- Outlook, Internet, intranet, busca na web e navegadores.
- Redes de Computadores: Correio eletrônico: endereços, utilização e recursos típicos.
- Conceitos e serviços relacionados à Internet e a correio eletrônico.
- Cliente de E-mail e protocolos (SMTP e IMAP) – Correio Eletrônico: uso de correio eletrônico, preparo e envio de mensagens, anexação de arquivos.
- Redes de computadores: Correio eletrônico: endereços, utilização de recursos típicos.
- Programas de correio eletrônico (Microsoft Outlook).
- Google Chrome.
- E-mail: utilização e configurações usuais.
- Correio eletrônico: conceito e segurança para usuário.
- Microsoft Outlook.
- Navegadores de Internet, serviços de busca na Web e uso do correio eletrônico.
- Serviços de correio eletrônico.
- Conceitos e modos de utilização de ferramentas e aplicativos de correio eletrônico.
- Correio eletrônico institucional.
- Navegadores Microsoft Edge e Google Chrome, correio eletrônico Microsoft Outlook, busca na Internet e grupos de discussão.
- Programas de correio eletrônico (Microsoft Outlook)

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.correio-eletronico-outlook-2016-navegacao.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.correio-eletronico-outlook-2016-navegacao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
