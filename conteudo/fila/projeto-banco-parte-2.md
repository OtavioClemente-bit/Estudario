==================================================
# BANCO 051 — informatica.redes-computadores
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Redes de computadores** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceitos e topologias de LAN, MAN e WAN; comutação e roteamento; modelos OSI e TCP/IP; encapsulamento; protocolos de aplicação, transporte, rede e enlace; endereços MAC e IP; IPv4, IPv6, DNS, DHCP, ARP/NDP; redes Wi-Fi e segurança básica.
Fica de fora (outras matérias tratam): Configuração de equipamentos de fabricante específico, cálculo avançado de sub-redes e administração detalhada de redes de grande porte.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Redes de computadores.
- Redes de Computadores: Conceitos básicos, ferramentas, aplicativos e procedimentos de internet e intranet.
- Redes de Computadores: conceitos básicos.
- Tipos de redes: locais (LAN), metropolitanas (MAN) e de longa distância (WAN).
- Redes de computadores: fundamentos.
- Tecnologias ethernet, Fibre Channel, iSCSI, padrão wi-fi IEEE 802.11x.
- Dispositivos: repetidores, bridges, switches e roteadores.
- Técnicas de comutação de circuitos, pacotes e células.
- Noções de redes de computadores.
- Conceitos de redes de computadores: meios de transmissão, classificação, topologia de redes, redes de longa distância, redes locais e redes sem fio.
- Elementos de interconexão de redes de computadores (hubs repetidores, switches, roteadores).
- Redes de comunicação.
- Introdução a redes (computação/telecomunicações).
- Noções básicas de transmissão de dados: tipos de enlace, códigos, modos e meios de transmissão.
- Redes de computadores: locais, metropolitanas e de longa distância.
- Terminologia e aplicações, topologias, modelos de arquitetura (OSI/ISO e TCP/IP) e protocolos.
- Noções de Redes e Comunicação.
- Noções de arquitetura e princípios de funcionamento das redes.
- Redes de computadores: Fundamentos de comunicação de dados.
- Redes de computadores: Estações e servidores.
- Redes de computadores: Tecnologias de redes locais e de longa distância.
- Redes de computadores: Arquitetura cliente-servidor.
- Tipos e meios de transmissão.
- Tecnologias e tipos de redes locais e de longa distância (PAN, LAN, MAN, WAN, WPAN, WLAN, WMAN e WWAN).
- Elementos de interconexão de redes de computadores (gateways, hubs, repetidores, bridges, switches e roteadores).
- Redes de computadores e procedimentos de Internet e intranet.
- Fundamentos de comunicação de dados.
- Meios de transmissão, classificação e topologias de redes locais, sem fio e de longa distância.
- Elementos de interconexão: hubs, repetidores, switches e roteadores.
- Modelos de referência OSI e padrões IEEE 802.1, 802.3 e 802.11 a/b/g/n/ac.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.redes-computadores.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.redes-computadores",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 052 — direito-administrativo.servico-publico
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Serviço público: conceito, princípios e classificação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito e titularidade do serviço público; princípios da continuidade, adequação e modicidade; classificação uti singuli e uti universi; Lei 13.460/2017 e direitos, deveres, manifestações, ouvidorias e Carta de Serviços do Usuário.
Fica de fora (outras matérias tratam): Concessão, permissão, autorização como formas de delegação, política tarifária detalhada, intervenção e extinção de contratos, que pertencem a tópico próprio.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Serviços públicos.
- Serviços públicos: conceito, classificação, regulamentação, formas e competência de prestação.
- Serviços públicos: Conceito.
- Classificação.
- Princípios.
- Serviços públicos: Elementos constitutivos.
- Serviços públicos: Formas de prestação e meios de execução.
- Serviços públicos: Classificação.
- Serviços públicos: Princípios.
- Serviço público.
- Direitos dos usuários de serviço público.
- Formas de prestação e meios de execução.
- Formas de prestação e meios de execução dos serviços públicos.
- Serviços públicos: conceito, elementos, prestação, delegação, classificação e princípios.
- Serviços públicos e direitos dos usuários: Lei nº 13.460/2017.
- Usuário do serviço público.
- Serviços públicos: conceito e princípios.
- Serviços públicos. Conceito. Elementos constitutivos.
- Classificação dos serviços públicos. Princípios.
- Serviços Públicos: conceito, classificação, regulamentação e controle; forma, meios e requisitos.
- Elementos constitutivos.
- Conceito de serviço público.
- Serviços públicos: conceito de serviço público.
- Serviços públicos: caracteres jurídicos.
- Serviços públicos: classificação e garantias.
- Serviços públicos: usuário do serviço público.
- Serviços públicos: conceito, elementos, prestação, execução, delegação, classificação, princípios e Lei nº 13.460/2018.
- Serviços públicos: conceito e elementos constitutivos.
- Serviços Públicos: conceitos, princípios, classificação, formas de prestação e extinção.
- Serviços públicos: conceito, elementos, formas de prestação, delegação, classificação e princípios.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.servico-publico.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.servico-publico",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 053 — direito-administrativo.extincao-ato-administrativo
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Extinção do ato: anulação, revogação, convalidação, cassação e caducidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Formas de extinção dos atos administrativos; anulação e prazo decadencial do art. 54 da Lei 9.784/1999; revogação e limites; convalidação; cassação; caducidade; contraposição; Súmula 473 do STF.
Fica de fora (outras matérias tratam): Elementos, atributos e classificações gerais do ato, tratados em tópico próprio; processo disciplinar e invalidação judicial de atos não administrativos, salvo contraste indispensável.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Decadência administrativa.
- Extinção do ato administrativo: cassação, anulação, revogação e convalidação.
- Revogação e anulação.
- Revogação, anulação e convalidação do ato administrativo.
- Cassação.
- Teoria das nulidades no direito administrativo.
- Atos administrativos nulos e anuláveis.
- Extinção, revogação, invalidação e convalidação.
- Cassação e caducidade.
- Cassação, anulação, revogação e convalidação.
- Ato administrativo: Extinção do ato administrativo.
- Ato administrativo: Decadência administrativa.
- Atos administrativos: revogação, anulação e convalidação do ato administrativo.
- Extinção do ato administrativo.
- Invalidação, anulação e revogação.
- Anulação e revogação.
- Atos administrativos: cassação.
- Atos administrativos: revogação e anulação.
- Anulação, revogação e convalidação dos atos administrativos.
- Ato administrativo: Prescrição.
- Ato administrativo: invalidação, anulação e revogação.
- Ato administrativo: extinção, desfazimento e sanatória.
- Atos administrativos: Extinção dos atos administrativos.
- Extinção dos atos administrativos: Revogação, anulação e cassação.
- Atos administrativos: Convalidação.
- Atos administrativos: Decadência administrativa.
- Ato administrativo: extinção, nulidades e revogação.
- Ato administrativo: Invalidação, extinção, anulação e revogação.
- Anulação e revogação do ato administrativo.
- Extinção do ato administrativo: cassação, anulação, revogação e convalidação; decadência administrativa.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.extincao-ato-administrativo.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.extincao-ato-administrativo",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 054 — afo.lei-responsabilidade-fiscal
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Financeira e Orçamentária: Responsabilidade na gestão fiscal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
LC nº 101/2000 e atualizações; princípios; planejamento fiscal, AMF, ARF e execução; previsão e arrecadação, RCL e renúncia; geração de despesas, DOCC e pessoal; transferências voluntárias; dívida, operações de crédito, regra de ouro e vedações; transparência, RREO, RGF e fiscalização, sem listar os percentuais máximos de limites de cada ente.
Fica de fora (outras matérias tratam): Créditos adicionais em detalhe, Plano de Contas Aplicado ao Setor Público (PCASP), competências específicas de tribunais de contas e tabelas de percentuais de limites por esfera ou Poder.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei Complementar nº 101/2000 (Lei de Responsabilidade Fiscal).
- Lei de responsabilidade fiscal.
- Lei de Responsabilidade Fiscal: planejamento.
- Lei de Responsabilidade Fiscal: transparência, controle e fiscalização.
- Lei de Responsabilidade Fiscal: receita pública.
- Lei de Responsabilidade Fiscal: despesa pública.
- Lei de Responsabilidade Fiscal: dívida e endividamento.
- Lei de Responsabilidade Fiscal: conceitos e objetivos.
- Lei Complementar nº 101/2000.
- Lei de Responsabilidade Fiscal (Lei Complementar nº 101/2000).
- Relatório de Gestão Fiscal.
- Relatório Resumido de Execução Orçamentária.
- Responsabilidade Fiscal.
- Limitações das Despesas.
- Despesa com pessoal.
- Endividamento Público.
- Capítulo III – Da Receita Pública.
- Capítulo IV – Da Despesa Pública.
- Capítulo VII – Da Dívida e do Endividamento.
- Lei de Responsabilidade Fiscal (Lei Complementar nº 101/2000): Planejamento.
- Lei de Responsabilidade Fiscal: princípios, objetivos e efeitos no planejamento e processo orçamentário; regra de ouro, anexos de metas e riscos fiscais, receita corrente líquida, renúncia de receita, geração da despesa, despesa obrigatória continuada, vedações e transparência.
- Manual de Demonstrativos Fiscais, 12ª edição.
- Transparência.
- Transferências voluntárias.
- Renúncia de receitas.
- Lei Complementar nº 101/2000 e suas alterações (Lei de Responsabilidade Fiscal).
- Relatório de gestão fiscal: estrutura, composição.
- Renúncia de receitas tributárias.
- Destinação de recursos públicos para o setor privado.
- Lei Complementar nº 101/2000 e Lei nº 4.320/1964.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo afo.lei-responsabilidade-fiscal.banco-N.json, onde N é o lote)
```json
{
  "materia": "afo.lei-responsabilidade-fiscal",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 055 — afo.ppa-ldo-loa-ciclo
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Financeira e Orçamentária: Planejamento e ciclo orçamentário** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
PPA, LDO e LOA; integração dos instrumentos; ciclo orçamentário; estrutura geral dos orçamentos; créditos suplementares, especiais e extraordinários, sem prazos ou percentuais não necessários.
Fica de fora (outras matérias tratam): Estágios detalhados da receita e despesa, limites fiscais e demonstrações contábeis.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Plano plurianual.
- Diretrizes orçamentárias.
- Ciclo orçamentário.
- Sistema e processo de orçamentação.
- Processo orçamentário.
- Orçamento público: Ciclo orçamentário.
- Orçamento anual.
- Plano Plurianual, Lei de Diretrizes Orçamentárias e Lei Orçamentária Anual.
- Lei de Diretrizes Orçamentárias (LDO): objetivos, estrutura, base legal e conteúdo, Anexos de Metas Fiscais, Anexos de Riscos Fiscais, critérios para limitação de empenho.
- Orçamento público: Processo orçamentário.
- Créditos ordinários e adicionais.
- Alterações orçamentárias.
- O orçamento público no Brasil: Sistema de planejamento e de orçamento federal.
- Lei de Diretrizes Orçamentárias.
- Lei Orçamentária Anual.
- Leis orçamentárias: espécies e tramitação legislativa.
- Plano Plurianual (PPA): estrutura, base legal, objetivos, conteúdo, tipos de programas.
- O orçamento público no Brasil: Plano plurianual.
- O orçamento público no Brasil: Diretrizes orçamentárias.
- O orçamento público no Brasil: Orçamento anual.
- O orçamento público no Brasil: Sistema e processo de orçamentação.
- Matérias orçamentárias e noções de processo legislativo orçamentário: Projeto de Lei Orçamentária, Projeto de Lei do Plano Plurianual; Projeto de Lei de Diretrizes Orçamentárias; Projeto de Lei de Crédito Adicional.
- O ciclo orçamentário.
- Sistema de planejamento e de orçamento federal.
- Lei de Diretrizes Orçamentárias (LDO).
- Lei Orçamentária Anual (LOA).
- Projeto de Lei Orçamentária Anual: elaboração, acompanhamento e aprovação.
- Sistema e processo de orçamentação, classificações, estrutura programática e créditos ordinários e adicionais.
- Emendas parlamentares ao Orçamento.
- Leis orçamentárias.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo afo.ppa-ldo-loa-ciclo.banco-N.json, onde N é o lote)
```json
{
  "materia": "afo.ppa-ldo-loa-ciclo",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 056 — informatica.sistema-operacional-windows
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Sistema Operacional Windows 10** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Uso do Windows 10 em estação de trabalho: interface, janelas, Explorador de Arquivos, caminhos, busca, operações com arquivos e pastas, configurações usuais, extensões, compartilhamento e permissões.
Fica de fora (outras matérias tratam): Administração avançada de domínio, comandos de terminal, registro do Windows, políticas corporativas e recursos exclusivos do Windows 11.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos de organização e de gerenciamento de informações, arquivos, pastas e programas.
- Sistema Operacional Windows 10: manipulação de arquivos e pastas, configurações, permissões etc.
- Sistema operacional Windows.
- Noções de sistema operacional (ambiente Windows).
- Organização e gerenciamento de informações, arquivos, pastas e programas.
- Sistema operacional e ambiente Windows.
- Extensões e arquivos.
- Sistemas Operacionais Windows/Linux: conceito de pastas, diretórios, arquivos e atalhos.
- Área de trabalho e área de transferência.
- Manipulação de arquivos e pastas.
- Organização de informações, arquivos, pastas e programas.
- Organização de arquivos, pastas e programas.
- Noções de sistema operacional Windows.
- Noções de organização e de gerenciamento de informações, arquivos, pastas e programas.
- Identificação e manipulação de arquivos.
- Windows 10: janelas, menus, barra de tarefas, área de trabalho e gerenciamento de arquivos e pastas.
- Compartilhamento, área de transferência, configurações de tela, cores, fontes e impressoras; Windows Explorer.
- Sistema operacional Windows 10.
- Gerenciamento de arquivos, pastas e programas.
- Noções de sistema operacional no ambiente Windows.
- Identificação e manipulação de arquivos e backup.
- Noções de sistema operacional (Windows e Linux).
- Sistema Operacional: Windows/Linux: conceito de pastas, diretórios, arquivos e atalhos, área de trabalho, área de transferência, manipulação de arquivos e pastas, uso dos menus, programas e aplicativos, interação com o conjunto de aplicativos.
- Noções de sistema operacional (Linux e Windows).
- MS-Windows 10: arquivos, pastas, atalhos, área de trabalho, menus, programas e aplicativos.
- MS-Windows 10: pastas, diretórios, arquivos, atalhos, áreas de trabalho e transferência, manipulação de arquivos, menus, programas e aplicativos.
- Conceitos básicos do sistema operacional Windows.
- Principais aplicativos e acessórios do Windows 10.
- Conceitos de organização de pastas e arquivos.
- Principais extensões de arquivos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.sistema-operacional-windows.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.sistema-operacional-windows",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 057 — legislacao.lei-8112-1990-regime-disciplinar
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei 8.112/1990: regime disciplinar** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Deveres do art. 116; proibições do art. 117; acumulação dos arts. 118 a 120; responsabilidades dos arts. 121 a 126-A; penalidades e critérios dos arts. 127 a 142; prescrição disciplinar.
Fica de fora (outras matérias tratam): Procedimento de sindicância, processo disciplinar, afastamento preventivo, comissão, defesa e julgamento processual, tratados em matéria própria; regimes estaduais, municipais e militares.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Do Regime Disciplinar: Dos Deveres, Das Proibições, Da Acumulação, Das Responsabilidades e Das Penalidades.
- Direitos, deveres e responsabilidades dos servidores públicos civis.
- Regime disciplinar e processo administrativo-disciplinar.
- Direitos e deveres.
- Regime disciplinar na Lei nº 8.112/1990: deveres e proibições, acumulação, responsabilidades, penalidades.
- Direitos e deveres dos funcionários públicos.
- Lei nº 8.112/1990 e alterações (Regime Jurídico dos Servidores Públicos Civis da União): Regime disciplinar (deveres e proibições, acumulação, responsabilidades, penalidades).
- Agentes públicos: Direitos e deveres.
- Lei nº 8.112/1990 e alterações: regime disciplinar, deveres, proibições, acumulação, responsabilidades, penalidades e processo administrativo disciplinar.
- Do regime disciplinar: dos deveres, das proibições, da acumulação, das responsabilidades, das penalidades.
- Regime e processo administrativo disciplinar.
- Lei nº 8.112/1990 e suas alterações: regime disciplinar.
- Regime disciplinar da Lei nº 8.112/1990.
- Disposições doutrinárias: Direitos e deveres.
- Regime disciplinar.
- Ética no setor público: Regime Jurídico dos Servidores Públicos Civis da União (Lei nº 8.112/1990), regime disciplinar, deveres e proibições, acumulação, responsabilidade e penalidades.
- Lei nº 8.112/1990 - regime disciplinar: deveres e proibições, acumulação de cargos, responsabilidades, penalidades.
- Responsabilidade civil, criminal e administrativa dos agentes públicos.
- Deveres.
- Proibições.
- Acumulação.
- Penalidades.
- Dos Servidores Públicos – Lei n.º 8.112/90: do regime disciplinar, dos deveres, das proibições, da acumulação, das responsabilidades, das penalidades.
- Direitos e deveres dos servidores públicos.
- Lei nº 8.112/1990 e alterações: deveres, proibições, acumulação, responsabilidades e penalidades disciplinares.
- Lei nº 8.112/1990: direitos, deveres e responsabilidades dos servidores públicos civis.
- Regime disciplinar: deveres, proibições, acumulação, responsabilidades e penalidades.
- Lei nº 8.112/1990 e alterações: regime disciplinar (deveres e proibições, acumulação, responsabilidades, penalidades).
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): regime disciplinar.
- Agentes públicos e servidores públicos: direitos, deveres e responsabilidades dos servidores públicos civis.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8112-1990-regime-disciplinar.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8112-1990-regime-disciplinar",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 058 — matematica.estatistica-descritiva
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática e Raciocínio Lógico: Estatística descritiva** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
População, amostra, variáveis, frequências absoluta e relativa, tabelas, gráficos de barras, setores, linhas e histogramas, leitura e interpretação, média aritmética e ponderada, mediana, moda, amplitude, variância e desvio padrão.
Fica de fora (outras matérias tratam): Inferência estatística, estimação, testes de hipóteses, distribuições de probabilidade, regressão e medidas para dados agrupados que exijam interpolação além de estimativas por ponto médio.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Noções de estatística: média, moda, mediana e desvio padrão.
- Noções de Estatísticas: medidas de tendência central (moda, mediana, média aritmética simples e ponderada) e de dispersão (desvio médio, amplitude, variância, desvio padrão).
- Histogramas e curvas de frequência.
- Estatística descritiva e análise exploratória de dados: gráficos, diagramas, tabelas, medidas descritivas (posição, dispersão, assimetria e curtose).
- Estatística: média, moda, mediana e desvio padrão.
- Estatística descritiva.
- Medidas de posição: média, moda, mediana e quartis.
- Gráficos, diagramas, tabelas, medidas descritivas (posição, dispersão, assimetria e curtose).
- Medidas de dispersão: amplitude, variância, desvio-padrão, coeficiente de variação, amplitude interquartil.
- Análise exploratória de dados.
- Descrição univariada: população e amostra; estatística descritiva e inferencial; variáveis estatísticas e níveis de mensuração.
- Dados em série e agrupados; distribuições de frequência, histogramas e polígonos de frequências.
- Medidas de tendência central, variabilidade absoluta e relativa, assimetria e curtose.
- Métodos para sumarização e análise exploratória de dados.
- Variáveis quantitativas e qualitativas.
- Estatística descritiva e análise exploratória de dados.
- Organização e apresentação de variáveis.
- Estatística descritiva, medidas de tendência central e dispersão, histogramas e gráficos.
- Conceitos gerais: variável, tipos de variáveis.
- Frequências: absoluta e relativa, frequências acumuladas.
- Medidas de tendência central (em dados brutos ou agrupados em classes): média aritmética, média geométrica, média ponderada, moda e mediana.
- Medidas de Posição: quartis e percentis.
- Medidas de dispersão (em dados brutos ou agrupados em classes): amplitude, variância, desvio padrão e coeficiente de variação.
- Representação tabular e gráfica.
- Medidas de dispersão: amplitude, amplitude interquartil, variância, desvio padrão e coeficiente de variação.
- Medidas de posição: média, moda, mediana e separatrizes.
- Distribuição de frequências: absoluta, relativa, acumulada.
- Noções de estatística: média, moda, mediana e desvio-padrão.
- Média, moda, mediana e desvio padrão.
- Medidas de Tendência Central.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.estatistica-descritiva.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.estatistica-descritiva",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 059 — afo.receita-despesa-publica
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Financeira e Orçamentária: Receita e despesa pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Classificações e estágios da receita e despesa pública; restos a pagar processados e não processados; despesas de exercícios anteriores; suprimento de fundos e controles básicos.
Fica de fora (outras matérias tratam): Princípios e ciclo orçamentário em geral, limites detalhados da LRF e registros contábeis do PCASP.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Restos a pagar.
- Despesas de exercícios anteriores.
- Suprimento de fundos.
- Classificações orçamentárias.
- Despesa pública.
- Receita pública.
- Despesa pública: Conceito e classificações.
- Programação e execução orçamentária e financeira.
- Receita pública: Estágios.
- Receita pública: Fontes.
- Despesa pública: Estágios.
- Despesa pública: categorias, estágios.
- Receita pública: Conceito, ingresso e receitas.
- Programação e execução orçamentária e financeira: Descentralização orçamentária e financeira.
- Despesa pública: Restos a pagar.
- Despesa pública: Despesas de exercícios anteriores.
- Despesa pública: Suprimento de fundos.
- Receita pública: Conceito e classificações.
- Receita pública: categorias, fontes, estágios; dívida ativa.
- Classificação econômica da Receita e da Despesa pública.
- Conceito e estágios da Receita e da Despesa pública.
- O orçamento público no Brasil: Classificações orçamentárias.
- O orçamento público no Brasil: Estrutura programática.
- Programação e execução orçamentária e financeira: Acompanhamento da execução.
- Receita pública: Dívida ativa.
- Receita pública: Classificação das receitas públicas.
- Receita e despesa pública.
- Classificação das receitas públicas.
- Receita e despesa pública: Conceito, etapas, estágios e categorias econômicas.
- Receita pública: categorias, fontes, estágios e dívida ativa.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo afo.receita-despesa-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "afo.receita-despesa-publica",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 060 — direito-constitucional.stf-cnj-stj
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: STF, CNJ e STJ** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Composição e competências constitucionais do STF; súmula vinculante; repercussão geral; composição e atribuições do CNJ; composição e competências do STJ.
Fica de fora (outras matérias tratam): Demais órgãos do Judiciário, organização da Justiça do Trabalho, CSJT, quinto constitucional fora da composição do STJ e detalhes processuais não necessários à compreensão constitucional.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Do Poder Judiciário: disposições gerais; do Supremo Tribunal Federal; do Conselho Nacional de Justiça: organização e competência; do Superior Tribunal de Justiça; dos Tribunais e Juízes do Trabalho; do Conselho Superior da Justiça do Trabalho: organização e competência.
- Conselho Nacional de Justiça: composição e competências.
- Conselho Nacional de Justiça.
- Poder Judiciário: Órgãos do Poder Judiciário.
- Poder Judiciário: Conselho Nacional de Justiça (CNJ).
- Súmula Vinculante.
- Órgãos do poder Judiciário: Organização e competências, Conselho Nacional de Justiça.
- Supremo Tribunal Federal.
- Superior Tribunal de Justiça.
- Poder judiciário: Conselho Nacional de Justiça.
- Do Conselho Nacional de Justiça: organização e competência.
- Do Superior Tribunal de Justiça.
- Conselho Nacional de Justiça (CNJ): composição e competências.
- Conselho Nacional de Justiça (CNJ).
- Órgãos do Poder Judiciário: organização e competências.
- Organização e competências, Conselho Nacional de Justiça (CNJ).
- Conselho Nacional de Justiça e funções essenciais à Justiça.
- Composição e competências do Conselho Nacional de Justiça.
- Organização e competências, Conselho Nacional de Justiça: Composição e competências.
- Controle de constitucionalidade: Súmula vinculante.
- Conselho Nacional de Justiça (CNJ): Composição e competência.
- Sistemas de controle interno do Poder Judiciário: Corregedorias, Ouvidorias, Conselhos Superiores e Conselho Nacional de Justiça.
- Do Supremo Tribunal Federal.
- Superior Tribunal de Justiça: organização e competência.
- Supremo Tribunal Federal: Composição, estrutura e competências.
- Superior Tribunal de Justiça: Composição, estrutura e competências.
- Poder Judiciário, STF e STJ.
- Organização e competências, Conselho Nacional de Justiça.
- Conselho Nacional de Justiça: composição e competência.
- Corregedorias, ouvidorias, conselhos superiores e Conselho Nacional de Justiça.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.stf-cnj-stj.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.stf-cnj-stj",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 061 — etica.etica-moral-funcao-publica
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Ética: Ética, moral e função pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Diferenças entre ética e moral; valores, virtudes e princípios; ética aplicada à função pública; princípios constitucionais; cidadania, democracia, respeito, decoro, honestidade, zelo, organização e prioridade em serviço; integridade, governança, transparência, imparcialidade, controle social e decisão responsável.
Fica de fora (outras matérias tratam): Regras detalhadas de códigos profissionais específicos, disciplina jurídica especial de conflito de interesses e procedimentos disciplinares. Referências gerais a normas de ética e integridade são usadas apenas para explicar conceitos e distinguir seus âmbitos de aplicação.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ética e função pública.
- Ética e moral.
- Ética, princípios e valores.
- Ética no setor público.
- Ética e democracia: exercício da cidadania.
- Ética e democracia.
- Ética no serviço público.
- Ética e moral: definição e distinção.
- Valores, virtude, honestidade, integridade, decoro e zelo no serviço público: conceitos.
- Ética, democracia, cidadania e o papel do servidor público.
- Aplicação dos princípios éticos na Administração Pública.
- Ética e moral, princípios e valores.
- Ética e função pública e ética no setor público.
- Ética aplicada: ética, moral, valores e virtudes.
- Noções de ética empresarial e profissional.
- Atitudes éticas, respeito, valores e virtudes.
- Atitudes no serviço.
- Ética no setor público e improbidade administrativa.
- Comunicação, redes organizacionais, transparência, integridade e ética pública.
- Ética e conduta do servidor público.
- A gestão da ética nas empresas públicas e privadas.
- Comportamento profissional.
- Princípios e valores éticos do serviço público, seus direitos e deveres à luz do artigo 37 da Constituição Federal de 1988.
- Ética e função pública; ética no setor público.
- Ética no serviço público, comportamento profissional, atitudes, organização e prioridades no trabalho.
- Ética, moral, princípios, valores, democracia, cidadania e função pública.
- Responsabilidade do agente público: sanções éticas e disciplinares.
- Ética no exercício da função pública.
- Ética na administração pública.
- Princípios da Administração Pública aplicados à ética.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo etica.etica-moral-funcao-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "etica.etica-moral-funcao-publica",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 062 — direito-constitucional.organizacao-poderes
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Organização dos Poderes e separação das funções** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Unidade do poder estatal; separação e coordenação dos Poderes Legislativo, Executivo e Judiciário; funções típicas e atípicas; freios e contrapesos; independência, autonomia e garantias institucionais.
Fica de fora (outras matérias tratam): Organização detalhada do processo legislativo e das Casas legislativas; atribuições pormenorizadas da Chefia do Executivo; estrutura judiciária; técnicas de controle de constitucionalidade; tratados e competências materiais específicas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Da organização dos Poderes.
- Organização dos poderes no Estado: Mecanismos de freios e contrapesos.
- Organização dos poderes no Estado.
- Organização dos Poderes.
- Poderes do Estado e respectivas funções.
- Organização dos Poderes: Poder Legislativo, Poder Executivo e Poder Judiciário.
- Os poderes do Estado e as respectivas funções.
- Organização dos Poderes: Poder Legislativo e Poder Executivo.
- Mecanismos de freios e contrapesos.
- Poderes Executivo, Legislativo e Judiciário.
- Poderes Executivo, Legislativo e Judiciário, processo legislativo e fiscalização.
- Poderes do Estado: executivo, legislativo e judiciário.
- Organização dos Poderes e mecanismos de freios e contrapesos.
- Separação de Poderes.
- Divisão e coordenação de Poderes da República.
- Organização dos Poderes: mecanismos de freios e contrapesos.
- Organização dos Poderes e funções essenciais à Justiça.
- Poder e divisão de poderes.
- Poderes da União.
- Poder executivo, legislativo e Judiciário.
- Teoria Geral do Estado e poderes do Estado.
- Organização e separação dos Poderes.
- Organização dos Poderes, freios e contrapesos e funções essenciais à Justiça.
- Separação Poderes.
- Mecanismo de freios e contrapesos.
- A unidade do poder estatal e a separação de poderes.
- Estado Democrático de Direito e organização dos Poderes.
- Poderes Legislativo, Executivo e Judiciário na CF/88.
- Organização do Estado e organização dos Poderes.
- Divisão e coordenação dos Poderes da República.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.organizacao-poderes.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.organizacao-poderes",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 063 — direito-constitucional.conceito-classificacao-constituicao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Conceito e classificação das constituições** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito, natureza, objeto, estrutura, elementos, supremacia e interpretação da Constituição; concepções sociológica, política e jurídica; classificações e ciclos constitucionais; princípios fundamentais dos arts. 1º a 4º; natureza e classificação da Constituição de 1988; poder constituinte originário e derivado, reforma e revisão.
Fica de fora (outras matérias tratam): Estudo específico dos direitos fundamentais, controle de constitucionalidade em seus procedimentos e processo legislativo geral, exceto regras estritamente necessárias à reforma constitucional do art. 60.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceito de Constituição.
- Constituição: Conceito, objeto, elementos e classificações.
- Constituição: conceito, classificações e princípios fundamentais.
- Constituição.
- Supremacia da Constituição.
- Constituição: conceito e classificação.
- Constituição Federal de 1988: conceito, contexto histórico, características, estrutura do texto.
- Constituição: conceito, classificações, princípios fundamentais.
- Classificações das constituições.
- Conceito, objeto, elementos e classificações da Constituição.
- Constituição: conceito, objeto, elementos, classificações e supremacia.
- Constituição da República Federativa do Brasil de 1988 e emendas: Conceito, classificações, princípios fundamentais.
- Constituição: supremacia da Constituição.
- Classificação.
- Neoconstitucionalismo.
- Direito Constitucional: conceito, objeto, elementos e classificações.
- Conceito, classificações, princípios fundamentais, emendas constitucionais.
- Conceito, natureza, classificação e estrutura da Constituição Federal de 1988.
- Constitucionalismo.
- A ordem constitucional vigente.
- Estado e Constituição.
- Classificação das Constituições.
- Constituições material e formal, Constituição-garantia e Constituição dirigente.
- Direito Constitucional: natureza, conceito e objeto.
- Direito constitucional: Noções gerais, ciclos constitucionais.
- História das constituições.
- Teoria geral da Constituição: conceito, origens, conteúdo, estrutura e classificação.
- Constituição de 1988: conceito, contexto histórico, características, estrutura do texto.
- Teoria da Constituição, poder constituinte, princípios fundamentais, interpretação e eficácia das normas constitucionais.
- Constituição: conceito, objeto, elementos, classificação, supremacia e interpretação.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.conceito-classificacao-constituicao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.conceito-classificacao-constituicao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 064 — direito-constitucional.ministerio-publico
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Ministério Público e MPT** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Arts. 127 a 130-A da Constituição: perfil e princípios do Ministério Público, autonomia, ramos do MPU e MPT, chefia, ingresso, garantias, vedações, funções, CNMP e atuação constitucional trabalhista (art. 114, § 3º), com LC nº 75/1993 como apoio.
Fica de fora (outras matérias tratam): Organização infraconstitucional detalhada das carreiras; ritos processuais; atribuições de ramos e órgãos que ultrapassem a compreensão constitucional do MP/MPT; estudo aprofundado de Advocacia Pública, advocacia e Defensoria Pública, citadas apenas em contraste.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ministério Público.
- Funções essenciais à justiça: Ministério Público.
- Funções essenciais à justiça: Ministério Público, Advocacia Pública.
- Funções essenciais à justiça: Ministério Público, Advocacia Pública e Defensoria Pública.
- Funções essenciais à Justiça: Ministério Público e advocacia pública.
- Das funções essenciais à Justiça: do Ministério Público.
- Conselho Nacional do Ministério Público.
- Funções essenciais à Justiça: Ministério Público, advocacia e defensoria públicas.
- Ministério Público, Ministério Público junto aos tribunais de contas e advocacia pública.
- Constituição da República Federativa do Brasil de 1988: Funções essenciais à justiça.
- Funções essenciais à Justiça: Ministério Público, advocacia e Defensoria Pública.
- Constituição Federal: Título IV, Capítulo IV, Seção I.
- Lei Complementar nº 75/1993.
- Funções essenciais à Justiça, Ministério Público, Advocacia Pública, advocacia e Defensoria Pública.
- Funções essenciais à justiça. Ministério Público e Advocacia Pública.
- Ministério Público: Princípios, garantias, vedações, organização e competências.
- Ministério Público: Conselho Nacional do Ministério Público.
- Ramos do Ministério Público e funções exclusivas e concorrentes.
- Membros do MPU: ingresso, promoção, aposentadoria, garantias, prerrogativas e vedações.
- Conselho Nacional do Ministério Público: composição e atribuições constitucionais.
- Funções essenciais à Justiça: Ministério Público, princípios, garantias, vedações, organização e competências.
- Funções essenciais à Justiça: Ministério Público: princípios, garantias, vedações, organização e competências.
- Funções essenciais à Justiça: Ministério Público, Advocacia Pública, advocacia e Defensoria Pública.
- Ministério Público e defesa do Estado e das instituições democráticas.
- Ministério Público, advocacia pública.
- Funções essenciais à Justiça: Ministério Público, Advocacia, Advocacia Pública e Defensoria Pública.
- Funções essenciais à Justiça: Ministério Público;
- Ministério Público do Trabalho: atribuições, garantias e vedações;
- Ministério Público e funções essenciais à justiça.
- Ministério Público: princípios constitucionais.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.ministerio-publico.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.ministerio-publico",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 065 — direito-constitucional.advocacia-defensoria
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Advocacia Pública, Advocacia e Defensoria Pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Arts. 131 a 135 da Constituição: representação da União, consultoria e assessoramento do Executivo, procuradorias estaduais e distrital, peculiaridade municipal, advocacia privada e Defensoria Pública, seus princípios, autonomia e garantias.
Fica de fora (outras matérias tratam): Organização detalhada de outras funções essenciais à justiça, organização judiciária e questões processuais específicas que extrapolem o núcleo constitucional.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Funções essenciais à Justiça.
- Defensoria Pública.
- Funções essenciais à Justiça: defensoria pública.
- Advocacia pública.
- Funções essenciais à justiça: Advocacia Pública.
- Da Advocacia e da Defensoria Pública.
- Advocacia e Defensoria Pública.
- Funções essenciais à justiça: Advocacia e Defensoria Pública.
- Advocacia pública consultiva.
- Advocacia e Defensoria Públicas.
- Funções essenciais à Justiça: Ministério Público, advocacia e defensorias públicas.
- Funções essenciais à justiça: Ministério Público, advocacia pública, advocacia privada e Defensoria Pública.
- Funções essenciais à Justiça: Ministério Público, advocacia e defensoria públicas.
- Da Advocacia Pública.
- Das Funções Essenciais à Justiça.
- Funções essenciais da justiça.
- Advocacia Pública (arts. 44 a 132).
- Advocacia Pública;
- Advocacia-Geral da União: representação, consultoria e assessoramento jurídico.
- Competências constitucionais, história, estrutura, órgãos e organização da Advocacia-Geral da União.
- Advogado-Geral da União: provimento e competências.
- Consultoria e assessoramento jurídico ao Poder Executivo federal.
- Representação judicial e extrajudicial da União e de agentes públicos.
- Advocacia-Geral da União: perfil constitucional e funções institucionais.
- As funções essenciais à Justiça.
- A Advocacia-Geral da União na CF/88.
- Advocacia-Geral da União.
- Procuradoria-Geral Federal.
- Advocacia pública consultiva e hipóteses de manifestação obrigatória.
- Advocacia Pública: representação judicial e extrajudicial das pessoas jurídicas de direito público.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.advocacia-defensoria.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.advocacia-defensoria",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 066 — direito-administrativo.autarquias-agencias-reguladoras-executivas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Autarquias, Agências reguladoras e executivas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Administração indireta; autarquias; fundações públicas; empresas públicas; sociedades de economia mista; entidades privadas de colaboração; agências reguladoras e executivas, sua formação, autonomia, poderes, controles e prazos; atos administrativos, elementos, atributos, espécies, discricionariedade, anulação, revogação, convalidação e decadência.
Fica de fora (outras matérias tratam): Licitações e contratos em geral; concessões e permissões como regime autônomo; regimes de servidores em profundidade; processo administrativo além das regras indispensáveis aos atos e sua invalidação; organização interna da Administração direta.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Autarquias, fundações, empresas públicas e sociedades de economia mista.
- Fundações públicas.
- Autarquias.
- Sociedades de economia mista.
- Empresas públicas e privadas.
- Agências reguladoras.
- Empresas estatais.
- Empresa pública.
- Sociedade de economia mista.
- Empresas estatais: normas constitucionais, Lei nº 13.303/2016 e Decreto nº 8.945/2016.
- Administração indireta: Autarquias.
- Administração indireta: Agências reguladoras.
- Administração indireta: Agências executivas.
- Administração indireta: Fundações públicas.
- Administração indireta: Empresas públicas.
- Administração indireta: Sociedades de economia mista.
- Empresas públicas e sociedades de economia mista.
- Autarquias, Agências reguladoras e executivas.
- Agências reguladoras: histórico, conceito, características e controle.
- Reforma do Estado e papel das agências reguladoras.
- Administração indireta e entidades paralelas.
- Autarquias e fundações.
- Lei nº 13.303/2016 (Lei das Estatais).
- Atos administrativos: elementos e atributos.
- Agências reguladoras e agências executivas.
- Entidades reguladoras federais: estrutura jurídica, funções, autonomia administrativa e poder normativo.
- Autarquias, fundações, empresas públicas, sociedades de economia mista, entidades paraestatais e terceiro setor.
- Agências reguladoras: história, conceito, características, controle, estrutura, funções, autonomia e poder normativo.
- Órgãos reguladores no Brasil: histórico e características das autarquias.
- Autarquias, agências reguladoras e executivas, fundações públicas, empresas públicas e sociedades de economia mista.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.autarquias-agencias-reguladoras-executivas.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.autarquias-agencias-reguladoras-executivas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 067 — administracao-geral.projetos-qualidade
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Geral: Gestão de projetos e gestão da qualidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito de projeto, programa, portfólio e operação; ciclo de vida do projeto e estruturas organizacionais; escritório de projetos; noções do Guia PMBOK (grupos de processos e áreas de conhecimento da 6ª edição, princípios e domínios da 7ª); escopo e EAP; cronograma, caminho crítico e folga; custos e valor agregado; riscos e respostas; partes interessadas; evolução da qualidade e seus teóricos (Deming, Juran, Crosby, Ishikawa, Feigenbaum); qualidade total; ciclo PDCA; ferramentas da qualidade (Pareto, Ishikawa, histograma, folha de verificação, dispersão, carta de controle, fluxograma, 5W2H, GUT, brainstorming, benchmarking); custos da qualidade; programa 5S; qualidade no serviço público.
Fica de fora (outras matérias tratam): Métodos ágeis em detalhe (Scrum, Kanban, XP), PRINCE2, certificação ISO 9001 em detalhe, Seis Sigma estatístico, modelos de excelência em gestão pública e reformas administrativas (tratados em matérias próprias).

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Gestão da qualidade e modelo de excelência gerencial.
- Gestão de projetos.
- Gestão de projetos: elaboração, análise e avaliação de projetos.
- Processos, grupos de processos e áreas de conhecimento.
- Ferramentas de gestão da qualidade.
- Gestão da qualidade.
- Processos, grupos de processos e área de conhecimento.
- Gerenciamento de projetos (PMBOK 7ª edição).
- Principais teóricos e suas contribuições para a gestão da qualidade.
- Gestão por Projetos.
- Gestão da qualidade e modelo de excelência gerencial: principais teóricos e suas contribuições para a gestão da qualidade.
- Principais características dos modelos de gestão de projetos.
- Projetos e suas etapas.
- Projetos e a organização.
- Gestão de projetos: Principais características dos modelos de gestão de projetos.
- Gestão de projetos: Projetos e suas etapas.
- Ciclo de vida de projeto e ciclo de vida do produto.
- Ciclo PDCA.
- Gestão de riscos.
- Elaboração, análise e avaliação de projetos.
- Gestão da qualidade e modelo de excelência gerencial: Ferramentas de gestão da qualidade.
- Gestão de projetos: Noções de elaboração, análise, avaliação e gerenciamento de projetos.
- Gerência de projetos: conceitos.
- Gestão da qualidade em serviços.
- Gestão de projetos: elaboração, análise, avaliação, modelos e etapas.
- Gestão de projetos: elaboração, análise, avaliação e gerenciamento.
- Evolução da administração: Qualidade na Administração Pública.
- Formulação de programas e projetos.
- Integração de escopo, prazos, custos, riscos, qualidade, documentação e comunicação.
- Gestão de programas e portfólio de projetos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-geral.projetos-qualidade.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-geral.projetos-qualidade",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 068 — direito-constitucional.processo-legislativo
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Processo legislativo** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Espécies normativas do art. 59; emendas constitucionais e limites; iniciativa e emendas; tramitação, deliberação, sanção, veto, promulgação e publicação; medidas provisórias; leis delegadas; leis complementares e ordinárias; decretos legislativos, resoluções e técnica legislativa básica.
Fica de fora (outras matérias tratam): Organização e estatuto dos congressistas; competências institucionais detalhadas; controle financeiro e orçamentário aprofundado; processos estaduais e municipais além de referência indispensável.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Processo legislativo.
- Do Processo Legislativo.
- Poder legislativo: Processo legislativo.
- Do Poder Legislativo: do processo legislativo.
- Procedimento Legislativo: definição; tipos: normal ou ordinário, abreviado, sumário, sumaríssimo, especial, concentrado.
- Processo Legislativo: definição, natureza jurídica, princípios gerais.
- Noções básicas: anteprojeto, autógrafos, unicameralismo e bicameralismo, blocos parlamentares, comissões, correção de erro, deliberação, destaque, emendas, iniciativa, legislatura.
- Noções básicas: sanção, sessões legislativas, turnos, urgência, veto, votação, voto vencido em separado.
- Iniciativa do processo de elaboração das leis: concorrente, reservada ou exclusiva, vinculada, popular.
- Tramitação de proposições: projeto de lei ordinária; projeto de lei complementar; projeto de decreto legislativo, projeto de resolução, indicação, parecer, emenda, requerimentos.
- Processo legislativo federal: conceito, espécies normativas, modalidades, fases.
- Processo legislativo estadual, distrital e municipal: normas constitucionais federais aplicáveis.
- Poder Legislativo: Do Processo Legislativo.
- Espécies normativas.
- Processo legislativo estadual.
- Poder Legislativo, processo legislativo e fiscalização.
- Do Processo Legislativo, da fiscalização contábil, financeira e orçamentária.
- Processo Legislativo: fundamentos e garantias de independência, conceito, objetos, atos e procedimentos.
- Processo legislativo: fundamento e garantias de independência, conceito, objetos, atos e procedimentos.
- Processo legislativo: conceito, objeto, espécies de atos normativos e procedimentos.
- Iniciativa.
- Emendas.
- Votação, sanção, veto, promulgação e publicação da lei.
- Medida Provisória.
- Direito Constitucional Aplicado ao Processo Legislativo.
- Processo legislativo constitucional: iniciativa, discussão, votação, sanção, veto, promulgação e publicação.
- Processo Legislativo Estadual e Organização do Poder Legislativo.
- Iniciativa legislativa: iniciativa parlamentar, iniciativa do Governador do Estado, iniciativa do Poder Judiciário, iniciativa do Ministério Público, iniciativa da Defensoria Pública, iniciativa popular, iniciativa privativa, iniciativa compartilhada.
- Limites constitucionais à iniciativa legislativa estadual.
- Tramitação de proposições legislativas.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.processo-legislativo.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.processo-legislativo",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 069 — matematica.probabilidade
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Probabilidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Experimentos aleatórios, espaço amostral, eventos, axiomas e propriedades, probabilidade clássica e empírica, complemento, união, interseção, diagramas de Venn, probabilidade condicional, independência, contagem, com e sem reposição, probabilidade total e regra de Bayes.
Fica de fora (outras matérias tratam): Variáveis aleatórias contínuas, distribuições inferenciais, testes estatísticos e estatística avançada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Probabilidade.
- Problemas de contagem e noções de probabilidade.
- Probabilidade: Definições básicas e axiomas.
- Princípios de probabilidade.
- Noções de probabilidade: espaço amostral.
- Eventos, união, intersecção e complementar de eventos, probabilidade condicional e independência.
- Definições básicas e axiomas.
- Probabilidade, eventos dependentes e independentes, união, interseção e probabilidade condicional.
- Conceitos básicos de probabilidade.
- Probabilidades: definições básicas, axiomas e propriedades.
- Conceitos fundamentais de probabilidade.
- Noções de probabilidade: experimento aleatório, espaços amostrais finitos e equiprováveis e eventos aleatórios.
- Cálculo de probabilidades.
- Probabilidade condicional.
- Probabilidade: experimento aleatório, espaço amostral, evento.
- Espaços equiprováveis.
- Probabilidade de Laplace.
- Espaços não equiprováveis.
- Teorema do produto.
- Teorema de Bayes.
- Probabilidades: conceito e axiomas.
- Probabilidade, certeza, impossibilidade, fenômenos aleatórios, espaço amostral e evento.
- Probabilidade: definição e propriedades.
- Cálculo de probabilidade.
- Probabilidades.
- Probabilidade, axiomas, probabilidade condicional e independência.
- Probabilidade clássica, contagem, diagrama de Venn e probabilidade condicional.
- Conceitos de probabilidade.
- Modelo de probabilidade.
- Probabilidade: conceitos básicos, eventos independentes e mutuamente exclusivos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.probabilidade.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.probabilidade",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 070 — informatica.correio-eletronico-outlook-2016-navegacao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Correio eletrônico (Outlook 2016) e navegação (Google Chrome)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

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

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

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
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 071 — direito-constitucional.direitos-politicos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Direitos políticos e partidos (arts. 14 a 17)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Soberania popular, participação direta, sufrágio, cidadania, alistamento e voto, elegibilidade, inelegibilidades, reeleição, suspensão e perda de direitos políticos, anterioridade eleitoral e regime constitucional dos partidos políticos.
Fica de fora (outras matérias tratam): Procedimento eleitoral infraconstitucional aprofundado, crimes eleitorais, propaganda eleitoral, financiamento em detalhes e organização administrativa do processo eleitoral.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Partidos políticos.
- Direitos políticos.
- Cidadania e direitos políticos.
- Direitos e garantias fundamentais: Direitos políticos.
- Cidadania.
- Dos direitos políticos.
- Lei nº 9.709/1998 (regulamenta a execução do disposto nos incisos I, II e III do art. 14 da CRFB/88 – plebiscito, referendo e iniciativa popular).
- Direitos sociais, nacionalidade, cidadania, direitos políticos e partidos políticos.
- Direitos políticos, partidos políticos.
- Direitos e garantias fundamentais: Dos Direitos Políticos.
- Direitos políticos: Direitos fundamentais e direitos políticos.
- Direitos políticos: Privação dos direitos políticos.
- Poder representativo: Sufrágio.
- Sufrágio: Natureza.
- Sufrágio: Extensão do sufrágio.
- Sufrágio: Valor do sufrágio.
- Sufrágio: Modo de sufrágio.
- Sufrágio: Formas de sufrágio.
- Direitos políticos e partidos políticos.
- Nacionalidade, direitos políticos e partidos políticos.
- Nacionalidade, cidadania, direitos políticos e partidos políticos.
- Plebiscito.
- Nacionalidade, cidadania e direitos políticos.
- Dos direitos e garantias fundamentais: dos direitos políticos.
- Alistamento.
- Suspensão e perda dos direitos políticos.
- Referendo.
- Iniciativa popular.
- Direitos e deveres fundamentais: Partidos políticos.
- Direitos e garantias fundamentais: partidos políticos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.direitos-politicos.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.direitos-politicos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 072 — matematica.analise-combinatoria
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Análise combinatória** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Princípios aditivo e multiplicativo, fatorial, permutações simples e com repetição, arranjos, combinações e contagens em etapas.
Fica de fora (outras matérias tratam): Probabilidade, análise assintótica e combinatória avançada de grafos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Princípios de contagem e probabilidade.
- Princípios de contagem.
- Análise combinatória.
- Análise combinatória, conjuntos numéricos e sistemas de equações do primeiro e segundo graus.
- Combinações.
- Arranjos e permutações.
- Análise combinatória e probabilidade: arranjos, combinações, permutações simples, probabilidade de um evento e resolução de problemas.
- Análise combinatória: princípio fundamental da contagem, arranjos, permutações e combinações.
- Binômio de Newton.
- Problemas de contagem.
- Técnicas de Contagem e Análise Combinatória: Combinações Simples, Arranjos e Permutação com e sem repetição.
- Princípios fundamentais de contagem.
- Arranjos, permutações, combinações.
- Combinações, arranjos e permutação.
- Contagem, probabilidade e geometria básica.
- Problemas de contagem e probabilidade.
- Contagem e análise combinatória; binômio de Newton.
- Análise combinatória: princípio fundamental da contagem.
- Permutação simples e com repetição.
- Arranjos e combinações.
- Fatorial.
- Permutação.
- Combinação.
- Arranjo.
- Noções básicas de contagem.
- Contagem: princípio fundamental da contagem, permutações, arranjos e combinações.
- Matrizes: Análise combinatória.
- Matrizes: Binômio de Newton.
- Princípios simples de contagem e probabilidade.
- Análise combinatória e conjuntos numéricos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.analise-combinatoria.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.analise-combinatoria",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 073 — informatica.banco-dados-sql
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Banco de dados e SQL** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceitos de banco de dados e SGBD; modelo relacional, tabelas, atributos, chaves e integridade; normalização; SQL para definição, manipulação e consulta, junções, agregação e transações; noções de data warehouse, data lake e ETL.
Fica de fora (outras matérias tratam): Administração avançada de produtos específicos, sintaxe proprietária extensa, ajuste de desempenho em larga escala e implementação física de mecanismos de armazenamento.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Linguagem de consulta estruturada (SQL).
- Linguagem de definição de dados (DDL).
- Linguagem de manipulação de dados (DML).
- Banco de dados.
- Propriedades de banco de dados.
- Banco de dados: Conceitos básicos.
- Integridade referencial.
- Avaliação de modelos de dados.
- Banco de dados: Arquitetura.
- SGBD.
- Normalização das estruturas de dados.
- Banco de dados: Estrutura de dados.
- Banco de dados: Modelagem e normalização de dados.
- Banco de dados: Noções de administração de dados e de banco de dados.
- Modelagem de dados (conceitual, lógica e física).
- Chaves e relacionamentos.
- Modelagem e normalização de dados.
- Abordagem relacional.
- Banco de dados: organização de arquivos, métodos de acesso, abstração e modelos de dados.
- Integridade referencial e metadados.
- SQL, DDL e DML.
- Noções de administração de dados e de banco de dados.
- Organização e gerenciamento de bancos de dados.
- Arquivos, modelos de dados e sistemas gerenciadores de banco de dados.
- Linguagens de definição e manipulação de dados e SQL.
- Controle de proteção, segurança e integridade de bancos de dados.
- SQL.
- Projeto e modelagem de banco de dados relacional.
- Linguagem SQL.
- Noções de bancos de dados.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.banco-dados-sql.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.banco-dados-sql",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 074 — administracao-publica.modelos-governanca
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Pública: Modelos de administração pública e governança** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Patrimonialismo, burocracia weberiana e suas disfunções, administração pública gerencial (nova gestão pública), reformas do Estado no Brasil (reforma burocrática, Decreto-Lei 200/1967, Plano Diretor de 1995 e Emenda 19/1998), governança pública e seus princípios e mecanismos, accountability (vertical, horizontal e social), gestão por resultados e indicadores de desempenho, excelência nos serviços públicos e pós-gerencialismo.
Fica de fora (outras matérias tratam): Princípios e regras detalhadas do Direito Administrativo, licitações e contratos, Lei de Acesso à Informação, atendimento ao público, planejamento e orçamento (PPA, LDO, LOA) e gestão de projetos e da qualidade em geral, que estão em outras matérias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Gestão de resultados na produção de serviços públicos.
- Convergências e diferenças entre a gestão pública e a gestão privada.
- Empreendedorismo governamental e novas lideranças no setor público.
- O paradigma do cliente na gestão pública.
- Gestão da Qualidade: excelência nos serviços públicos.
- A nova gestão pública.
- Governabilidade, governança e accountability.
- Governança e gestão pública.
- Administração pública: do modelo racional-legal ao paradigma pós-burocrático.
- Gestão por resultados na produção de serviços públicos.
- Reformas administrativas.
- Evolução da administração pública no Brasil (após 1930).
- Evolução da Administração Pública brasileira e reformas administrativas: princípios, objetivos, resultados, patrimonialismo, burocracia e gerencialismo.
- Estruturação da máquina administrativa no Brasil desde 1930: dimensões estruturais e culturais.
- Governança pública.
- Evolução dos modelos da administração pública (patrimonialista, burocrática e gerencial).
- Modelos de gestão pública (patrimonialista, burocrática e gerencial), com destaque para a Reforma do Estado e a Nova Gestão Pública.
- Gestão pública contemporânea, abordando temas como governança, accountability, transparência, participação social, planejamento governamental (PPA, LDO e LOA), gestão por resultados e indicadores de desempenho.
- Accountability.
- Evolução dos modelos de gestão pública: patrimonialismo, administração burocrática e nova gestão pública ( New Public Management ).
- Nova Governança Pública ( New Public Governance ): conceito, fundamentos e evolução.
- Ação pública em redes de cooperação, articulação interinstitucional e parcerias com o setor privado e terceiro setor.
- Valor Público, cadeia de valor público.
- Governança na Administração Pública.
- Governança pública: conceito, distinção entre governança e governabilidade e alinhamento estratégico.
- Governança na Administração Pública: mecanismos de governança: liderança, estratégia e controle.
- Conceito de valor público: geração de valor para a sociedade, entrega de resultados, sustentabilidade e avaliação de impactos de políticas públicas.
- Decreto Federal nº 9.203/2017 e suas alterações (política de governança da administração pública federal direta, autárquica e fundacional).
- Governabilidade e governança; intermediação de interesses: clientelismo, corporativismo e neocorporativismo.
- Evolução da Administração Pública no Brasil após 1930, reformas administrativas e nova gestão pública.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-publica.modelos-governanca.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-publica.modelos-governanca",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 075 — legislacao.lei-8112-1990-processo-disciplinar
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei 8.112/1990: sindicância e processo administrativo disciplinar** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Sindicâncias investigativa, patrimonial e acusatória; afastamento preventivo; PAD ordinário e sumário; instauração, comissão, inquérito, defesa, relatório, julgamento, nulidades, recursos, revisão e Súmula Vinculante 5.
Fica de fora (outras matérias tratam): Descrição aprofundada de deveres, proibições, espécies de penalidades e prescrição material; regimes disciplinares estaduais, municipais, militares ou de empregados celetistas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Processo administrativo disciplinar.
- Espécies de Procedimento Disciplinar: sindicâncias investigativa, patrimonial e acusatória; processo administrativo disciplinar (ritos ordinário e sumário).
- Fases do processo administrativo disciplinar: instauração, inquérito e julgamento.
- Comissão Disciplinar: requisitos, suspeição, impedimento e prazo para conclusão dos trabalhos (prorrogação e recondução).
- Agentes públicos: Processo administrativo disciplinar.
- Do processo administrativo disciplinar.
- Processo administrativo disciplinar e Lei nº 8.112/1990.
- Direito administrativo disciplinar: Procedimentos disciplinares da administração pública.
- Disposições doutrinárias: Processo administrativo disciplinar.
- Lei nº 8.112/1990 e processo disciplinar.
- Processo administrativo disciplinar e sua revisão.
- Responsabilidade do agente público: sindicância.
- Responsabilidade do agente público: processo administrativo disciplinar e termo de ajustamento de conduta.
- Regime e processo disciplinar.
- Processo administrativo disciplinar – noções.
- Processo administrativo sancionador e disciplinar.
- Responsabilidade e processo administrativo disciplinar dos servidores.
- Sistema de correição do poder executivo federal (Decreto nº 5.480/2005, Decreto nº 5.683/2006, Decreto nº 7.128/2010, Portaria CGU nº 335/2006).
- Responsabilidade e processo administrativo disciplinar; Lei nº 8.112/1990.
- Processo disciplinar e Lei nº 8.112/1990.
- Processo administrativo disciplinar e sindicância.
- Regime disciplinar e processo administrativo disciplinar.
- Processo administrativo disciplinar e responsabilização de agentes públicos.
- Procedimentos disciplinares da administração pública.
- Processos administrativos em espécie e processo administrativo disciplinar.
- Processo Administrativo Disciplinar (PAD).
- Responsabilidade, penalidades e processo administrativo disciplinar.
- Sindicância e processo administrativo disciplinar.
- A sindicância.
- Processo administrativo disciplinar. Processo sumário. Sindicância. Verdade sabida.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8112-1990-processo-disciplinar.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8112-1990-processo-disciplinar",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 076 — etica.codigo-etica-servidor-federal
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Ética: Código de Ética do servidor público civil federal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Decreto nº 1.171/1994, Anexo (regras deontológicas, deveres, vedações e censura ética); conceitos convergentes da Lei nº 8.027/1990, arts. 1º e 2º; Decreto nº 6.029/2007 (sistema, comissões, garantias, apuração e consequências); e rito aplicável da Resolução CEP nº 10/2008.
Fica de fora (outras matérias tratam): Regime disciplinar geral, procedimentos sancionadores de outros regimes, improbidade, legislação específica de conflito de interesses e códigos particulares de carreiras ou entidades.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Decreto nº 1.171/1994: Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal.
- Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal.
- Código de Ética Profissional do Serviço Público (Decreto nº 1.171/1994).
- Código de Ética Profissional do Serviço Público - Decreto nº 1.171/1994.
- Decreto nº 1.171/1994 (Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal).
- Lei nº 8.027/1990 e Decreto nº 1.171/1994: Código de Ética dos Servidores Públicos.
- Decreto Federal nº 1.171/1994: Código de Ética Profissional do Servidor Público, princípios éticos, deveres e penalidades.
- Código de Ética Profissional do Servidor Civil do Poder Executivo Federal e Código de Ética da Secretaria do Tesouro Nacional.
- Ética Pública: Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal e Sistema de Gestão da Ética.
- Ética no setor público: Código de Ética Profissional do Serviço Público (Decreto nº 1.171/1994).
- Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal (Decreto nº 1.171/1994).
- Decreto nº 1.171/1994: Código de Ética Profissional do Serviço Público.
- Decreto nº 6.029/2007 (Institui Sistema de Gestão da Ética do Poder Executivo Federal).
- Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal: Decreto nº 1.171/1994.
- Código de Ética Profissional do Serviço Público.
- Ética e função pública e Código de Ética Profissional do Serviço Público.
- Decreto nº 1.171/1994 (Código de Ética Profissional do Serviço Público).
- Decreto Federal nº 1.171/1994: Código de Ética Profissional do Servidor Público, princípios éticos, deveres do servidor e penalidades em caso de descumprimento do código.
- Ética no setor público e Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal.
- Código de Ética Profissional do Serviço Público — Decreto nº 1.171, de 22 de junho de 1999.
- Decreto nº 6.029, de 1º de fevereiro de 2007.
- Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal e Código de Ética da ANM.
- Decreto nº 1.171/1994 (Código de Ética Profissional do Servidor Público Civil).
- Código de Ética Profissional do Serviço Público: Decreto nº 1.171/1994.
- Decreto nº 1.171/1994 e Código de Ética da ANTT aprovado pela Deliberação nº 284/2009.
- Decreto nº 1.171/1994 e Código de Ética da ANTT (Deliberação nº 284/2009).
- Código de Ética Profissional do Serviço Público e Código de Ética da ANTT.
- Ética no serviço público: Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal (Decreto n.º 1.171/1994).
- Ética Pública e Código de Ética Profissional do Servidor Público Civil do Poder Executivo Federal.
- Sistema de Gestão da Ética do Poder Executivo Federal e conflito de interesses.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo etica.codigo-etica-servidor-federal.banco-N.json, onde N é o lote)
```json
{
  "materia": "etica.codigo-etica-servidor-federal",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 077 — legislacao.lei-13146-2015-inclusao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Estatuto da Pessoa com Deficiência (Lei 13.146/2015)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Lei Brasileira de Inclusão: conceito de pessoa com deficiência e avaliação biopsicossocial; igualdade e não discriminação; capacidade civil, curatela e tomada de decisão apoiada; direitos à saúde, à educação, ao trabalho e à acessibilidade; crimes previstos na lei.
Fica de fora (outras matérias tratam): Regimes completos de saúde, educação e trabalho previstos em legislação própria, regras processuais detalhadas da curatela, políticas públicas estranhas à Lei Brasileira de Inclusão e crimes que não estejam tipificados nela.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei 13.146/2015 (Institui a Lei Brasileira de Inclusão da Pessoa com Deficiência).
- Lei Brasileira de Inclusão Lei Federal nº13.146/2015 e suas alterações.
- Lei Brasileira de Inclusão da Pessoa com Deficiência (Lei federal nº 13.146/2015 e suas alterações).
- Estatuto da Pessoa com Deficiência (Lei nº 13.146/2015).
- Inclusão, direitos e garantias legais e constitucionais das pessoas com deficiência.
- Lei nº 13.146/2015 institui a Lei Brasileira de Inclusão da Pessoa com Deficiência (Estatuto da Pessoa com Deficiência).
- Lei n.º 13.146/2015 e suas alterações (Lei Brasileira de Inclusão da Pessoa com Deficiência – Estatuto da Pessoa com Deficiência).
- Lei nº 13.146/2015: Lei Brasileira de Inclusão da Pessoa com Deficiência.
- Inclusão de pessoas com deficiência.
- Leis nº 12.764/2012 e nº 13.146/2015: direitos da pessoa com transtorno do espectro autista e inclusão da pessoa com deficiência.
- Lei Brasileira de Inclusão da Pessoa com Deficiência – Estatuto da Pessoa com Deficiência (Lei nº 13.146/2015 e suas alterações).
- Acessibilidade.
- Lei Brasileira de Inclusão da Pessoa com Deficiência.
- Lei Brasileira de Inclusão da Pessoa com Deficiência (Estatuto da Pessoa com Deficiência).
- Estatuto da pessoa com deficiência.
- Lei nº 13.146/2015 (Estatuto da Pessoa com Deficiência).
- Lei Brasileira de Inclusão da Pessoa com Deficiência – Estatuto da Pessoa com Deficiência (Lei nº 13.146/2015).
- Fontes dos Direitos Humanos: Lei nº 13.146/2015 (Estatuto da pessoa com deficiência).
- Inclusão, direitos e garantias legais e constitucionais das pessoas com deficiência: Constituição Federal, Lei nº 11.126/2005 e Lei nº 13.146/2015.
- Leis Federais n. 13.146/2015.
- Crimes contra a pessoa com deficiência (Lei nº 13.146/2015).
- Lei Federal nº 13.146/2015 - Estatuto da Pessoa com Deficiência (especialmente quanto aos crimes previstos em seu texto).
- Lei nº 13.146/2015 e suas alterações (Crimes previstos no Estatuto da Pessoa com Deficiência).
- Legislação Especial: Lei nº 13.146/2015 (Estatuto da Pessoa com Deficiência).
- Lei N. 7.853, de 24 de outubro de 1989 e Lei Federal n. 13.146/2015 - Estatuto da pessoa com Deficiência (livro I);
- Estatuto da Pessoa com Deficiência e legislação correlata.
- Diversidade e inclusão e atendimento à pessoa com deficiência.
- Acessibilidade e prioridade de atendimento às pessoas com deficiência ou mobilidade reduzida.
- Diversidade e Inclusão: Lei Brasileira de Inclusão da Pessoa com Deficiência (Estatuto da Pessoa com Deficiência): Lei nº 13.146, de 06 de julho de 2015.
- Proteção e garantias das pessoas com deficiência: Leis Federais n. 7.853/1989, 10.048/2000, 10.098/2000 e 13.146/2015 e Decretos Federais n. 3.298/1999 e 5.296/2004.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-13146-2015-inclusao.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-13146-2015-inclusao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 078 — direito-administrativo.crimes-administracao-publica
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Crimes contra a Administração Pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Código Penal, arts. 312 a 337, com foco em peculato e espécies, concussão, corrupção passiva e ativa, prevaricação, condescendência criminosa, advocacia administrativa e conceito penal de funcionário público do art. 327.
Fica de fora (outras matérias tratam): Demais crimes contra a Administração não indicados no escopo, crimes licitatórios, improbidade administrativa, infrações disciplinares e processo penal.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Crimes contra a Administração Pública.
- Crimes praticados por funcionário público contra a Administração em geral.
- Dos crimes contra a Administração Pública: Crimes praticados por funcionário público contra a administração em geral, Crimes praticados por particular contra a administração em geral, Crimes contra a administração da justiça.
- Crimes contra a Administração Pública: crimes praticados por funcionário público e por particular contra a Administração Pública.
- Crimes praticados por particular contra a Administração em geral.
- Crimes contra a fé pública e contra a Administração Pública.
- Crimes contra a administração pública. Crimes praticados por funcionário público e por particular contra a administração pública.
- Crimes contra a Administração Pública: crimes praticados por funcionário público e por particular contra a Administração em geral.
- Crimes contra a Administração pública: resistência, desobediência e desacato.
- Crimes em espécie previstos no Código Penal: Crimes contra a Administração Pública.
- Código Penal: crimes contra a Administração Pública, crimes de funcionário público e de particular contra a Administração, crimes em licitações e contratos e crimes contra as finanças públicas.
- Dos crimes contra a administração pública: dos crimes praticados por funcionário público contra a administração em geral e dos crimes contra as Finanças Públicas.
- Crime: crimes contra a Administração Pública.
- Crimes contra a Administração Pública praticados por agentes públicos ou particulares e crimes contra a Administração da Justiça.
- Crimes contra a Administração Pública e contra as finanças públicas.
- Crimes contra a administração pública e Lei nº 8.429, de 2/6/1992.
- Excesso de Exação. Violação de sigilo.
- Penas: Crimes contra a administração pública.
- Crimes contra a Administração Pública praticados por funcionário público ou por particular, inclusive contra a Administração Pública estrangeira.
- Dos crimes contra as finanças públicas.
- Crimes contra a administração pública em detrimento do INSS.
- Parte Especial: crimes contra a Administração Pública.
- Crimes contra a Administração Pública (art. 312 a 359 do Decreto-Lei nº 2848 de 07 de dezembro de 1940 – Código Penal Brasileiro).
- Crimes contra a Administração Pública praticados por funcionário público ou por particular.
- Crimes contra a Administração Pública previstos no Código Penal.
- Decreto-Lei no 2.848/1940 - Crimes contra a Administração Pública.
- Crimes contra a administração da justiça.
- Crimes em espécie do Código Penal: Crimes contra a administração pública.
- Crimes contra a Administração Pública praticados por funcionário público e crimes contra as finanças públicas.
- Crimes contra a fé pública, a Administração Pública e o meio ambiente.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.crimes-administracao-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.crimes-administracao-publica",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 079 — informatica.internet-busca-redes-sociais
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Internet: busca, redes sociais, grupos de discussão e wikis** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Como funcionam os mecanismos de busca (rastreamento, indexação e ranqueamento); operadores de pesquisa (aspas, sinal de menos, OR, site:, filetype:, intitle:, inurl:, curinga *); redes sociais e suas finalidades; grupos de discussão, listas de e-mail, fóruns e wikis; comunicação síncrona e assíncrona; segurança e privacidade no uso: engenharia social, phishing em redes sociais, configurações de privacidade, autenticação em dois fatores, notícias falsas e checagem de informação.
Fica de fora (outras matérias tratam): Configuração detalhada de navegadores, correio eletrônico (clientes e protocolos), computação em nuvem, protocolos de rede em profundidade e criptografia, que têm matérias próprias; responsabilidade civil de provedores e direito digital em detalhe.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Sítios de busca e pesquisa na Internet.
- Grupos de discussão.
- Redes sociais.
- Redes de computadores: sítios de busca e pesquisa na Internet.
- Redes de computadores: redes sociais.
- Sítios de busca e pesquisa na internet: Utilização de mecanismos de busca como Google, Bing.
- Redes de computadores: Mídias sociais.
- Redes de computadores: grupos de discussão.
- Ferramentas e aplicativos de navegação, de correio eletrônico, de grupos de discussão, de busca e pesquisa.
- Ferramentas e aplicativos comerciais de navegação, de correio eletrônico, de grupos de discussão, de busca, de pesquisa e de redes sociais.
- Utilização de mecanismos de busca (Google, Bing); pesquisas e filtros.
- Internet e Intranet, pesquisa na web e mecanismos de busca.
- Ferramentas de busca.
- Sítios de busca, grupos de discussão e computação em nuvem.
- Busca na Internet, grupos de discussão e computação em nuvem.
- Correio eletrônico, grupos de discussão, fóruns e wikis.
- Ferramentas e aplicativos comerciais de navegação, de correio eletrônico, de grupos de discussão, de busca, de pesquisa, de redes sociais e ferramentas colaborativas.
- Conceitos básicos, ferramentas, aplicativos e procedimentos de Internet e intranet, grupos de discussão, redes sociais, computação na nuvem, programas de navegação, deep web, dark web.
- Redes de computadores: Navegadores, sítios de busca e pesquisa na Internet.
- Pesquisa na internet, redes sociais e computação em nuvem.
- Internet e Intranet, busca e pesquisa na web e mecanismos de busca.
- Conceitos e modos de utilização de ferramentas e aplicativos de grupos de discussão.
- Conceitos e modos de utilização de ferramentas e aplicativos de busca e pesquisa.
- Correio eletrônico, pesquisa na internet, grupos de discussão e redes sociais.
- Redes, internet, intranet, navegadores, correio eletrônico, busca, grupos e redes sociais.
- Redes sociais: Twitter, Facebook, LinkedIn, WhatsApp, YouTube, Instagram e Telegram.
- Grupos de discussão, fóruns e wikis.
- Redes Sociais: X (ex -Twitter), Facebook, Linkedin, WhatsApp, YouTube, Instagram e Telegram.
- Ferramentas e aplicativos comerciais de navegação, correio eletrônico, grupos de discussão, busca, pesquisa e redes sociais.
- Navegadores Internet Explorer e Mozilla Firefox; Outlook Express; busca e pesquisa na Internet e grupos de discussão.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.internet-busca-redes-sociais.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.internet-busca-redes-sociais",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 080 — direito-administrativo.terceiro-setor
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Terceiro setor, entes de colaboração e convênios** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Terceiro setor e entidades paraestatais; serviços sociais autônomos, fundações de apoio, organizações sociais, OSCIP, parcerias do MROSC (colaboração, fomento e cooperação), convênios e contratos de repasse com referência ao regime federal vigente.
Fica de fora (outras matérias tratam): Consórcios públicos em detalhe, concessões, contratos administrativos em geral, contabilidade pormenorizada de OSCIP e regimes estaduais ou municipais especiais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Entidades paraestatais.
- Organização administrativa: Entidades paraestatais e terceiro setor.
- Entidades paraestatais e terceiro setor: serviços sociais autônomos, entidades de apoio, organizações sociais, organizações da sociedade civil de interesse público.
- Serviços sociais autônomos, entidades de apoio, organizações sociais, organizações da sociedade civil de interesse público.
- Convênios e consórcios.
- Disposições doutrinárias: Convênios e instrumentos congêneres.
- Organizações sociais.
- Contratos de Gestão.
- Convênios.
- Convênios e consórcios administrativos.
- Entidades paraestatais e terceiro setor.
- Terceiro Setor.
- Contrato de gestão.
- Entidades paraestatais e terceiro setor: serviços sociais autônomos, entidades de apoio, organizações sociais e OSCIPs.
- Entidades paraestatais e o Terceiro Setor.
- Parcerias entre a Administração Pública e o terceiro setor.
- Convênio.
- Convênios administrativos.
- Convênios e termos similares.
- Entidades do Terceiro Setor.
- Entidades paraestatais e terceiro setor: Serviços sociais autônomos.
- Entidades paraestatais e terceiro setor: Entidades de apoio.
- Entidades paraestatais e terceiro setor: Organizações sociais.
- Entidades paraestatais e terceiro setor: Organizações da sociedade civil de interesse público (OSCIP).
- Parcerias entre a Administração Pública e o terceiro setor (Lei nº 13.019/2014 e suas alterações).
- Serviços sociais autônomos.
- Entidades de apoio.
- Organizações da sociedade civil de interesse público.
- Organizações da Sociedade Civil.
- Convênios, acordos, ajustes e instrumentos congêneres.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.terceiro-setor.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.terceiro-setor",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 081 — administracao-publica.planejamento-estrategico
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Pública: Planejamento estratégico, BSC, SWOT e indicadores** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito e características do planejamento estratégico; etapas do processo (referencial estratégico, diagnóstico, formulação, implementação e controle); negócio, missão, visão e valores; análise dos ambientes interno e externo, matriz SWOT e estratégias cruzadas; análise de cenários; matriz GUT e plano 5W2H como apoio; estratégias genéricas de Porter, cinco forças, redes e alianças; desdobramento em objetivos, metas, indicadores e iniciativas; Balanced Scorecard: perspectivas, mapa estratégico, relações de causa e efeito, temas, ativos intangíveis e adaptação ao setor público; OKR (noções); indicadores de desempenho: conceito, atributos, componentes, tipos (insumo, processo, produto, resultado e impacto), eficiência, eficácia, efetividade e economicidade; particularidades do planejamento estratégico na administração pública.
Fica de fora (outras matérias tratam): Teorias da administração, funções planejar, organizar, dirigir e controlar e a noção introdutória dos níveis estratégico, tático e operacional (matéria administracao-geral.funcoes-administrativas); ferramentas da qualidade como PDCA, Ishikawa e Pareto; gestão de projetos; PPA, LDO e LOA; normas específicas de planejamento do Poder Judiciário; planejamento de TI (PETI e PDTI).

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Planejamento estratégico.
- Balanced Scorecard.
- Indicadores de desempenho.
- Gestão estratégica.
- Planejamento estratégico: Conceitos, métodos e técnicas.
- Negócio, missão, visão de futuro, valores.
- Variáveis componentes dos indicadores.
- Ferramentas de análise de ambiente: análise SWOT, análise de cenários, matriz GUT.
- Tipos de indicadores.
- Análise competitiva e estratégias genéricas.
- Sistema de medição de desempenho organizacional.
- Indicadores de desempenho: conceito, formulação e análise.
- Referencial Estratégico das Organizações.
- Planejamento estratégico de negócio.
- Balanced Scorecard e processo decisório.
- Processo de planejamento: Análise competitiva e estratégias genéricas.
- Processo de planejamento: Redes e alianças.
- Processo de planejamento: Balanced scorecard.
- Controle: Sistema de medição de desempenho organizacional.
- Planejamento e gestão estratégica: conceitos, princípios, etapas, níveis, métodos e ferramentas.
- Balanced Scorecard (BSC).
- Estabelecimento de objetivos e metas organizacionais.
- Implementação de estratégias.
- Análise de cenários.
- Metodologias para medição de desempenho.
- Planejamento estratégico: conceitos, princípios, etapas, níveis, métodos e ferramentas.
- Planejamento nas organizações públicas: análise do ambiente, objetivos estratégicos, missão, visão e valores; ciclo PDCA.
- Gerenciamento de indicadores, metas e resultados.
- Planejamento estratégico: visão, missão e análise SWOT, matriz GUT e ferramenta 5W2H.
- Formulação e construção de indicadores.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-publica.planejamento-estrategico.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-publica.planejamento-estrategico",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 082 — portugues.tipologia-textual
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Tipologia textual** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Tipos e sequências textuais narrativas, descritivas, expositivas, explicativas, argumentativas, injuntivas e dialogais; finalidade, organização, marcas linguísticas, predominância e combinação em textos reais, com distinção introdutória entre tipo e gênero e entre literatura e não literatura.
Fica de fora (outras matérias tratam): Classificação aprofundada de gêneros textuais, interpretação global desvinculada da tipologia e análise sintática extensa.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Tipologia textual.
- Modos de organização discursiva: descrição, narração, exposição, argumentação e injunção.
- Os modos de organização discursiva: a descrição, a narração, a exposição informativa e a exposição argumentativa.
- Modos de organização discursiva: descrição, narração, exposição, argumentação e injunção; características específicas de cada modo.
- Tipos textuais: informativo, publicitário, propagandístico, normativo, didático e divinatório; características específicas de cada tipo.
- Organização estrutural dos textos.
- Características específicas de cada modo.
- Características específicas de cada tipo.
- Modos de organização discursiva e tipos textuais.
- Modos de organização discursiva: descrição, narração, exposição informativa e exposição argumentativa.
- Textos literários e não literários e tipologia da frase portuguesa.
- Sequências textuais descritiva, narrativa, argumentativa, injuntiva e dialogal.
- Características específicas de cada tipo textual.
- Características específicas dos modos de organização discursiva.
- Tipos textuais e características dos textos literários e não literários.
- Tipos textuais e suas características; textos literários e não literários.
- Modos discursivos, tipos textuais e textos literários e não literários.
- Tipos textuais, textos literários e não literários e tipologia da frase portuguesa.
- Modos discursivos: descrição, narração, exposição, argumentação e injunção.
- Modos de organização discursiva e tipos textuais previstos no edital.
- Modos discursivos e tipos textuais indicados no edital.
- Tipos textuais, textos literários e não literários.
- Tipos textuais, tipologia e estrutura da frase portuguesa.
- Tipos textuais e textos literários e não literários.
- Modos de organização discursiva: descrição, narração, exposição, argumentação e injunção e suas características.
- Organização retórica: generalização, exemplificação, descrição, definição, exemplificação/especificação, explanação, classificação, elaboração.
- Tipologia textual e gênero textual: narração, descrição, dissertação, carta (argumentativa, familiar, comercial, convite etc.).
- Modos discursivos, tipos textuais, textos literários e não literários e tipologia da frase.
- Modos discursivos, tipos textuais, textos literários e não literários e estrutura da frase.
- Características específicas de cada modo de organização discursiva.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.tipologia-textual.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.tipologia-textual",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 083 — informatica.computacao-nuvem
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Computação em nuvem** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Noções de computação em nuvem, características do modelo, implantação pública, privada, comunitária e híbrida, IaaS, PaaS e SaaS, armazenamento remoto, Google Drive, OneDrive, sincronização, compartilhamento, acesso offline, proteção, backup e restauração.
Fica de fora (outras matérias tratam): Administração de infraestrutura, desenho de redes, desenvolvimento de serviços e configuração avançada de segurança organizacional.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Computação na nuvem (cloud computing).
- Procedimentos de backup.
- Armazenamento de dados na nuvem (cloud storage).
- Segurança da informação: armazenamento de dados na nuvem (cloud storage).
- Computação em nuvem.
- Redes de computadores: Computação na nuvem (cloud computing).
- Computação em nuvem e redes sociais.
- Segurança da informação e segurança cibernética: Armazenamento de dados na nuvem (cloud storage).
- Arquitetura em nuvem (SaaS, IaaS e Paas).
- Armazenamento de dados na nuvem.
- Redes de Computadores: Conceitos básicos de computação em nuvem.
- Armazenamento de dados em nuvem.
- Noções de Computação em Nuvem.
- Definição e características das nuvens privadas e públicas.
- Modelos de Serviço em Nuvem: Infraestrutura como Serviço (IaaS), Plataforma como Serviço (PaaS) e Software como Serviço (SaaS).
- Backup e armazenamento de dados na nuvem.
- Conceitos de computação em nuvem: conceitos básicos.
- Tipologia (IaaS, PaaS, SaaS).
- Modelo: privada, pública, híbrida.
- Benefícios, alta disponibilidade, escalabilidade, elasticidade, agilidade, recuperação de desastres.
- Características gerais de identidade, privacidade, conformidade e segurança na nuvem.
- Computação em nuvem e gerenciamento de arquivos, pastas e programas.
- Computação em nuvem: conceitos envolvidos, vantagens e desvantagens.
- Conceitos de computação e armazenamento de dados em nuvem (cloud computing).
- Software de Backup.
- Computação e armazenamento de dados na nuvem.
- Computação e armazenamento na nuvem.
- Backup e armazenamento de dados em nuvem.
- Computação na nuvem (cloud computing)
- Armazenamento de dados na nuvem (cloud storage.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.computacao-nuvem.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.computacao-nuvem",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 084 — afo.orcamento-publico-principios
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Financeira e Orçamentária: Orçamento público e princípios orçamentários** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito e funções do orçamento; tipos e técnicas orçamentárias; ciclo; princípios de unidade, universalidade, anualidade, exclusividade, especificação, publicidade, transparência e não afetação; integração entre planejamento e orçamento.
Fica de fora (outras matérias tratam): Detalhamento de PPA, LDO e LOA, estágios da receita e despesa, créditos adicionais, contabilidade pública e limites fiscais específicos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Orçamento público.
- O orçamento público no Brasil.
- Orçamento público: conceitos e princípios orçamentários.
- Orçamento público: Conceito.
- Orçamento público: Princípios orçamentários.
- Técnicas orçamentárias.
- Orçamento-Programa: fundamentos e técnicas.
- Orçamento público: Técnicas orçamentárias.
- Orçamento público: conceito, técnicas, princípios, ciclo e processo orçamentário.
- Orçamento público: conceitos e princípios.
- Orçamento público: Conceito, espécies e natureza jurídica.
- Orçamento público no Brasil.
- Noções de orçamento público.
- Orçamento público e sua evolução.
- Princípios orçamentários e contábeis aplicados à administração pública.
- Orçamento público: História, evolução e natureza jurídica.
- Orçamento público: conceito e princípios orçamentários.
- O papel do Estado e a atuação do governo nas finanças públicas: Funções do orçamento público.
- Orçamento público: conceito, espécies, natureza jurídica e princípios.
- Orçamento Público: evolução, conceitos, espécies e natureza jurídica.
- Orçamento público e sua evolução: Princípios orçamentários.
- Orçamento.
- Funções do orçamento público.
- Características e elementos básicos do orçamento tradicional, orçamento de base zero, orçamento de desempenho e orçamento-programa.
- Métodos, técnicas e instrumentos do orçamento público; normas legais aplicáveis.
- Princípios de planejamento e de orçamento público.
- Orçamento público: conceitos e princípios. Evolução conceitual do orçamento público.
- Métodos, técnicas e instrumentos do Orçamento Público.
- Orçamento Público: conceitos, espécies e natureza jurídica.
- Evolução do orçamento público: orçamento tradicional, orçamento- programa, orçamento base zero e orçamento por resultados.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo afo.orcamento-publico-principios.banco-N.json, onde N é o lote)
```json
{
  "materia": "afo.orcamento-publico-principios",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 085 — direito-constitucional.tributacao-orcamento
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Tributação e orçamento na Constituição** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Sistema Tributário Nacional: espécies comuns aos entes, capacidade contributiva, lei complementar e reforma do consumo; repartição direta e indireta das receitas, finanças públicas e orçamentos constitucionais — PPA, LDO, LOA, emendas, créditos, vedações, duodécimos e pessoal.
Fica de fora (outras matérias tratam): Competência tributária individualizada e limitações em profundidade; controle externo e tribunais de contas em detalhe; técnica operacional de AFO e Lei de Responsabilidade Fiscal em profundidade.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Repartição das receitas tributárias.
- Sistema Tributário Nacional.
- Finanças públicas.
- Sistema Tributário Nacional: Princípios gerais.
- Sistema Tributário Nacional: Repartição das receitas tributárias.
- Sistema Tributário Nacional. Princípios gerais.
- Orçamento na Constituição de 1988: Plano Plurianual (PPA), Lei de Diretrizes Orçamentárias (LDO), Lei Orçamentária Anual (LOA).
- Finanças públicas: normas gerais e orçamento público.
- Finanças públicas: Normas gerais.
- Orçamentos.
- Tributação e orçamento.
- Orçamento na Constituição Federal de 1988.
- Da Repartição das Receitas Tributárias.
- Dos princípios gerais.
- Direito financeiro na Constituição Federal de 1988.
- Finanças públicas na Constituição de 1988.
- Finanças públicas: Orçamentos.
- Sistema Tributário Nacional na Constituição Federal: Da Tributação e do Orçamento.
- Do sistema tributário nacional.
- Direito Financeiro na Constituição Federal: das Finanças Públicas (arts. 165 a 169 da CF/88).
- Dívida ativa, repartição de receitas e federalismo fiscal.
- Vinculação e desvinculação de receitas.
- Orçamento na Constituição de 1988.
- Repartição de receitas.
- Orçamento.
- Noções sobre o Sistema Tributário Nacional.
- Tributação, orçamento, Sistema Tributário Nacional e finanças públicas.
- Sistema tributário nacional e repartição das receitas tributárias.
- Direito Financeiro Orçamento na Constituição de 1988.
- Receitas de transferências constitucionais e legais.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.tributacao-orcamento.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.tributacao-orcamento",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 086 — orcamento-publico.320-1964
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Orçamento Público: Lei n. 4.320/1964** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Lei n. 4.320/1964: estrutura e princípios da Lei de Orçamento; receita pública; classificação e execução da despesa; créditos adicionais; programação e execução do orçamento, com casos aplicados à administração judiciária.
Fica de fora (outras matérias tratam): Lei Complementar n. 101/2000 em profundidade, regras gerais de licitações e contratos, classificação completa da receita e da despesa conforme manuais atuais e contabilidade patrimonial não necessária à compreensão dos artigos do edital.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei nº 4.320/1964.
- Créditos adicionais.
- Programação e execução orçamentária e financeira.
- Execução orçamentária e financeira.
- Orçamento público no Brasil: Títulos I, IV, V e VI da Lei nº 4.320/1964.
- Créditos ordinários e adicionais.
- Lei nº 4.320/1964 e alterações.
- O orçamento público no Brasil: Créditos ordinários e adicionais.
- Programação e execução orçamentária e financeira: Alterações orçamentárias.
- Despesa pública: Dívida flutuante e fundada.
- Alterações orçamentárias.
- Lei Federal nº 4.320/1964 e suas alterações.
- Lei nº 4.320/64.
- Acompanhamento da execução.
- Dívida flutuante e fundada.
- Créditos adicionais, especiais, extraordinários, ilimitados e suplementares.
- Programação de desembolso e mecanismos retificadores do orçamento.
- Leis de Créditos Adicionais.
- Fundos Especiais de Despesa.
- Normas gerais de Direito Financeiro (Lei federal nº 4.320/1964).
- Lei nº 4.320/1964: Lei de Orçamento.
- Lei nº 4.320/1964: proposta orçamentária.
- Lei nº 4.320/1964: elaboração da Lei de Orçamento.
- Lei nº 4.320/1964: exercício financeiro.
- Lei nº 4.320/1964: créditos adicionais.
- Lei nº 4.320/1964: execução do Orçamento.
- Lei nº 4.320/1964: fundos especiais.
- Lei nº 4.320/1964: controle da execução orçamentária.
- Lei nº 4.320/1964: autarquias e outras entidades.
- Lei nº 4.320/1964: disposições finais.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo orcamento-publico.320-1964.banco-N.json, onde N é o lote)
```json
{
  "materia": "orcamento-publico.320-1964",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 087 — direito-constitucional.controle-constitucionalidade
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Controle de constitucionalidade** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Inconstitucionalidade formal e material; controle preventivo e repressivo; controle difuso, cláusula de reserva de plenário e papel do Senado; controle concentrado por ADI, ADC, ADO e ADPF, com legitimados, efeitos e modulação.
Fica de fora (outras matérias tratam): Controle de constitucionalidade estadual em face de constituições estaduais, controle de convencionalidade e ações constitucionais que não integrem o escopo do lote.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ação direta de inconstitucionalidade.
- Controle de Constitucionalidade.
- Ação declaratória de constitucionalidade.
- Arguição de descumprimento de preceito fundamental.
- Controle de constitucionalidade: sistemas difuso e concentrado.
- Ação Direta de Inconstitucionalidade por Omissão.
- Ação declaratória de constitucionalidade e arguição de descumprimento de preceito fundamental.
- Controle de constitucionalidade de atos normativos.
- Controle da constitucionalidade: Sistemas gerais e sistema brasileiro.
- Controle abstrato de constitucionalidade.
- Controle concreto e abstrato de constitucionalidade do direito municipal.
- Controle de constitucionalidade: sistemas gerais e sistema brasileiro.
- Controle de constitucionalidade nos estados e no Distrito Federal.
- Constituição: Supremacia da Constituição.
- Controle da constitucionalidade: Controle incidental ou concreto.
- Controle da constitucionalidade: Controle abstrato de constitucionalidade.
- Controle da constitucionalidade: Ação declaratória de constitucionalidade.
- Controle da constitucionalidade: Ação direta de inconstitucionalidade.
- Controle judicial de constitucionalidade: conceito, histórico, sistemas, pressupostos, modalidades, órgãos competentes, sujeitos legitimados, objetos de controle, tipos de inconstitucionalidade, parâmetros de controle, formalidades, procedimentos, julgamentos, decisões, efeitos das decisões, técnicas de decisão, segurança e estabilidade das decisões.
- Controle não judicial de constitucionalidade: órgãos, institutos e procedimentos.
- Interpretação da Constituição e controle de constitucionalidade; normas constitucionais e inconstitucionais.
- Incidente de arguição de inconstitucionalidade.
- Controle incidental ou concreto.
- Exame in abstractu da constitucionalidade de proposições legislativas.
- Ação direta de inconstitucionalidade por ação e por omissão.
- Controle de constitucionalidade: ação direta de inconstitucionalidade.
- Controle da constitucionalidade: Exame in abstractu da constitucionalidade de proposições legislativas.
- Controle da constitucionalidade: Arguição de descumprimento de preceito fundamental.
- Controle da constitucionalidade: Ação direta de inconstitucionalidade por omissão.
- Controle da constitucionalidade: Ação direta de inconstitucionalidade interventiva.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.controle-constitucionalidade.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.controle-constitucionalidade",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 088 — legislacao.lei-8112-1990-provimento
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Legislação: Lei 8.112/1990: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Lei nº 8.112/1990, arts. 1º a 39: disposições preliminares, requisitos de investidura, concurso, nomeação, posse, exercício, estágio probatório e estabilidade, formas de provimento, vacância, remoção, redistribuição e substituição; leitura constitucional e regulamentar indispensável.
Fica de fora (outras matérias tratam): Vencimentos e remuneração em geral, vantagens, férias, licenças e afastamentos em detalhe, deveres, responsabilidades e processo disciplinar, ressalvadas referências estritamente necessárias à interpretação dos arts. 1º a 39.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei n. 8.112/1990 e alterações: Das Disposições Preliminares;
- Do Provimento, Da Vacância, Da Remoção, Da Redistribuição e Da Substituição;
- Formas de provimento e vacância dos cargos públicos.
- Vacância.
- Estágio probatório.
- Provimento, vacância, remoção, redistribuição e substituição.
- Cargo, emprego e função pública: Vacância.
- Lei nº 8.112/1990 e alterações: provimento, vacância, remoção, redistribuição e substituição.
- Lei nº 8.112/1990 e alterações: Das disposições preliminares.
- Do Provimento, Vacância, Remoção, Redistribuição e Substituição: do provimento, da vacância, da remoção, da redistribuição e da substituição.
- Lei nº 8.112/1990 (Regime Jurídico dos Servidores Públicos Civis da União): disposições preliminares.
- Disposições doutrinárias: Vacância.
- Provimento.
- Efetividade, estabilidade e vitaliciedade.
- Regime jurídico: provimento, vacância, remoção, redistribuição e substituição.
- Dos Servidores Públicos – Lei n.º 8.112/90: disposições preliminares, provimento, vacância.
- Regime Jurídico dos Servidores Públicos Civis da União: disposições preliminares, provimento, vacância, remoção, redistribuição e substituição.
- Regime jurídico único: provimento, vacância, remoção, redistribuição e substituição.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): disposições preliminares.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): provimento.
- Regime jurídico dos servidores públicos civis da União (Lei nº 8.112/1990 e alterações): vacância.
- Agentes públicos e servidores públicos: estágio probatório.
- Agentes públicos e servidores públicos: formas de provimento e vacância dos cargos públicos.
- Agentes públicos e servidores públicos: exigência constitucional de concurso público para investidura em cargo ou emprego público.
- Lei nº 8.112/1990 (Regime Jurídico dos Servidores Públicos Civis da União e alterações): disposições preliminares.
- Lei nº 8.112/1990: provimento, vacância, remoção, redistribuição e substituição.
- Provimento e vacância dos cargos públicos e exigência de concurso público.
- Provimento e movimentação.
- Lei nº 8.112/1990, provimento, vacância e concurso público.
- Funcionário efetivo e vitalício: garantias e estágio probatório.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo legislacao.lei-8112-1990-provimento.banco-N.json, onde N é o lote)
```json
{
  "materia": "legislacao.lei-8112-1990-provimento",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 089 — administracao-publica.gestao-processos
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Pública: Gestão de processos (BPM): mapeamento, análise e melhoria** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Conceito e elementos de processo; hierarquia (macroprocesso, processo, subprocesso, atividade, tarefa); processos finalísticos, de suporte e gerenciais; cadeia de valor; visão funcional x visão por processos; gestão de processos x gestão por processos; estruturas funcional, matricial e por processos; BPM como disciplina gerencial; papéis (dono do processo, escritório de processos, patrocinador, analistas e executores); ciclo de vida de BPM segundo o Guia BPM CBOK; mapeamento e modelagem (AS-IS e TO-BE, técnicas de levantamento, diagrama, mapa e modelo); SIPOC, fluxograma e noções de BPMN (eventos, atividades, gateways, piscinas, raias, fluxos e artefatos); análise de valor agregado, gargalo, lead time, eficiência do ciclo e indicadores de processo; abordagens de melhoria (kaizen, redesenho, reengenharia, lean, Seis Sigma e DMAIC, benchmarking, padronização); automação com BPMS, workflow e RPA; gestão da mudança na melhoria de processos.
Fica de fora (outras matérias tratam): Funções administrativas, planejamento estratégico e a visão geral de gestão por projetos do processo organizacional (matéria própria); ciclo PDCA e ferramentas da qualidade em detalhe, como Pareto, histograma e carta de controle (gestão da qualidade); gerenciamento de projetos e PMBOK; BPMN avançado para desenvolvimento de software; certificação ISO; normas e resoluções específicas de órgãos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Gestão de processos.
- Técnicas de mapeamento, análise e melhoria de processos.
- Gestão de processos: conceitos da abordagem por processos.
- Conceitos da abordagem por processos.
- BPM.
- Conceitos de gestão de processos e modelagem de processos de negócio usando BPMN.
- Gestão de processos: conceitos, fundamentos, técnicas de mapeamento, análise e melhoria de processos.
- Gestão de processos: Técnicas de mapeamento, análise e melhoria de processos.
- Gestão por processos.
- Construção e mensuração de indicadores de processos.
- Gestão de processos: mapeamento, análise e melhoria.
- Gestão por processos: Ferramentas clássicas para o gerenciamento de processos.
- Identificação e delimitação de processos de negócio.
- Ciclo PDCA e macroprocessos finalísticos, gerenciais e de suporte.
- Gestão de processos e modelagem de processos de negócio com BPMN.
- Modelagem de processos de negócio.
- Gestão de processos: BPM.
- Gestão por processos e gestão funcional.
- Notação BPMN.
- Gerenciamento de processos de negócio (BPM CBOK v.4.0).
- Conceitos, modelagem de processos, análise de processos, desenho de processos, gerenciamento de desempenho de processos, transformação de processos, tecnologias de BPM.
- Hierarquia do processo: macroprocesso, processo, subprocesso, atividades e tarefa.
- Ferramentas e tecnologias de gerenciamento de processos.
- Técnicas de mapeamento de processos (modelos as-is).
- Técnicas de análise e simulação de processos.
- Técnicas de modelagem de processos (modelos to-be).
- Modelagem de processos de negócio: conceitos básicos.
- Gerenciamento de processos de negócio (BPM).
- Gestão de processos: Construção e mensuração de indicadores de processos.
- Gestão de processos: conceitos, diferença entre gestão de processos e por processos, mapeamento e modelagem.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-publica.gestao-processos.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-publica.gestao-processos",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 090 — informatica.bi-mineracao-dados
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: BI, data warehouse, OLAP, ETL e mineração de dados** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Sistemas transacionais (OLTP) e analíticos (OLAP); características do data warehouse (orientado a assunto, integrado, não volátil e variante no tempo); data mart, ODS, data lake e lakehouse; abordagens de Inmon e de Kimball; modelagem dimensional: fatos, dimensões, granularidade, medidas aditivas, semiaditivas e não aditivas, chave substituta, hierarquias, esquemas estrela, floco de neve e constelação, dimensões que mudam lentamente; ETL e ELT, área de preparação e qualidade dos dados; operações OLAP (drill down, roll up, slice, dice, pivot, drill across e drill through) e arquiteturas ROLAP, MOLAP e HOLAP; KDD e CRISP-DM; tarefas de mineração (classificação, regressão, agrupamento, associação com suporte, confiança e lift, detecção de anomalias); algoritmos clássicos (árvore de decisão, k-NN, Naive Bayes, k-means, Apriori) e tipos de aprendizado de máquina.
Fica de fora (outras matérias tratam): Visão geral do ciclo de análise, big data, ética, viés e explicabilidade de IA e transformação digital (estão em informatica.analise-dados-ia); SQL e normalização de bancos relacionais; programação de modelos, redes neurais em detalhe e uso passo a passo de ferramentas comerciais de BI.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Modelagem dimensional.
- Técnicas de Integração e Ingestão de Dados (ETL/ELT, Transferência de Arquivos e Integração via Base de Dados).
- Técnicas de modelagem e otimização de bases de dados multidimensionais.
- Business Intelligence.
- Mineração de dados: conceituação e características.
- Técnicas e tarefas de mineração de dados.
- Noções de modelagem dimensional. Conceito e aplicações.
- Noções de mineração de dados. Conceituação e características.
- Técnicas e tarefas de mineração de dados. Classificação. Regras de associação.
- Business Intelligence (BI).
- ETL, tratamento, manipulação e visualização de dados.
- ETL, manipulação, tratamento e visualização de dados.
- Arquitetura e aplicações de data warehouse com ETL e OLAP.
- Arquitetura de business intelligence.
- Conceitos, fundamentos, características, técnicas e métodos de business intelligence (BI).
- Regras de associação.
- Noções de mineração de dados: conceituação e características.
- Conceito de DataWarehouse, DataMart, DataLake, DataMesh.
- Noções de Business Intelligence: Ferramentas e aplicabilidade.
- ETL/ELT (Extract, Transform, Load).
- Pipeline de Dados.
- Modelagem dimensional e aplicações.
- Arquitetura de Inteligência de Negócio.
- OLAP.
- Sistemas transacionais.
- Sistemas de suporte à decisão.
- Conceitos básicos, arquiteturas e aplicações de datawarehousing, ETL, Olap e data mining.
- Data warehouse e data mining.
- Mineração de Dados.
- Definições e conceitos de data warehouse e data mining.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.bi-mineracao-dados.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.bi-mineracao-dados",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 091 — direito-administrativo.concessao-permissao
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Concessão e permissão de serviço público** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Lei 8.987/1995 vigente: concessões e permissões, licitação, tarifa, encargos, intervenção, extinção, reversão de bens e distinções pertinentes sobre autorização.
Fica de fora (outras matérias tratam): Teoria geral completa de serviços públicos, direitos do usuário fora do necessário à adequação e regimes setoriais especiais não previstos na Lei 8.987.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Delegação: concessão, permissão e autorização.
- Concessão, permissão e autorização de serviços públicos.
- Lei nº 8.987/1995.
- Serviços públicos: Delegação.
- Formas de delegação de serviço público.
- Extinção, reversão dos bens.
- Concessão, permissão e autorização.
- Extinção da concessão de serviço público e reversão dos bens.
- Lei nº 8.987/1995 (Lei de Concessões).
- Serviços públicos: regulação, concessão, permissão e autorização do serviço público.
- Formas de prestação e meios de execução: Delegação.
- Permissão e autorização.
- Serviços públicos: concessão, permissão, autorização e delegação.
- Delegação de serviços públicos: concessão, permissão, autorização.
- Regime jurídico da concessão e da permissão de serviço público.
- Delegação.
- Delegação de serviços públicos: concessão, permissão e autorização.
- Serviços delegados.
- Serviços públicos: serviços delegados.
- Serviços públicos: permissão e autorização.
- Parceria Público-Privada: Lei nº 8.987/1995, que dispõe sobre o regime de concessão e permissão da prestação de serviços públicos e Lei nº 11.079/2004, que institui normas gerais para licitação e contratação de parceria público-privada no âmbito da administração pública.
- Delegação por concessão, permissão e autorização.
- Lei nº 8.987/1995 e alterações.
- Concessão e permissão de serviços públicos (Lei nº 8.987/1995 e suas alterações).
- Concessão, permissão, autorização e delegação.
- Delegação de serviços públicos por autorização, permissão e concessão.
- Permissão, concessão e autorização de serviços públicos.
- Serviços públicos: extinção da concessão de serviço público e reversão dos bens.
- Concessões e permissões de serviços públicos.
- Conceito, características.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.concessao-permissao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.concessao-permissao",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 092 — direito-constitucional.direitos-sociais
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Direitos sociais (arts. 6º a 11)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Direitos sociais do art. 6º; direitos dos trabalhadores do art. 7º em visão geral; liberdade sindical, greve e participação laboral dos arts. 8º a 11; reserva do possível e mínimo existencial.
Fica de fora (outras matérias tratam): Direitos e garantias individuais do art. 5º, regimes legais trabalhistas detalhados e controvérsias processuais sobre dissídios coletivos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Direitos sociais.
- Direitos e garantias fundamentais: Direitos sociais.
- Dos direitos sociais.
- Direitos e garantias fundamentais: Dos Direitos Sociais.
- Princípios constitucionais do trabalho.
- Direitos sociais e sua efetivação.
- Dos direitos e garantias fundamentais: dos direitos sociais.
- Direitos e deveres fundamentais: Direitos sociais, nacionalidade, cidadania e direitos políticos.
- Direitos e deveres fundamentais: Direitos sociais e sua efetivação.
- Direitos sociais, dignidade do trabalhador e proteção constitucional ao trabalho.
- Liberdade e organização sindical, contribuição sindical e direito de greve.
- Direitos sociais e sua efetivação, direito à saúde e Sistema Único de Saúde.
- Poder constituinte: Direitos sociais.
- Direitos e garantias fundamentais: direitos sociais e direitos políticos.
- Direitos sociais, nacionalidade e cidadania.
- Direitos sociais em espécie.
- Direitos sociais: conceito e classificação.
- Direitos dos trabalhadores (individuais e coletivos).
- Fundamentos constitucionais dos direitos sociais. Ordem social e direitos sociais. Direitos sociais e direitos econômicos. Conceito e classificação dos direitos sociais.
- Direitos sociais: direito à alimentação, à moradia, à saúde, à educação, ao trabalho, direitos constitucionais trabalhistas, do direito ao futuro.
- Organização sindical.
- Os direitos constitucionais dos trabalhadores.
- A Saúde e a Teoria dos Direitos Sociais.
- Saúde e a Teoria dos Direitos Sociais.
- Direitos sociais, econômicos, culturais e ambientais.
- Direitos sociais e sua relação com a dignidade do trabalhador.
- Proteção constitucional ao trabalho e princípios da dignidade humana e valorização do trabalho.
- Liberdade sindical, organização dos trabalhadores, contribuição sindical e autonomia das entidades sindicais.
- Direito de greve de trabalhadores e servidores públicos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.direitos-sociais.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.direitos-sociais",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 093 — portugues.formacao-de-palavras
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Formação de palavras** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Morfemas e estrutura lexical (radical, afixos, vogal temática, desinência e elementos de ligação); derivação e suas modalidades; composição por justaposição e aglutinação; hibridismo; onomatopeias; siglas, acrônimos, abreviaturas, redução vocabular e outros processos produtivos; elementos greco-latinos e critérios de reconhecimento.
Fica de fora (outras matérias tratam): Paradigmas completos de flexão nominal e verbal, análise sintática, modalização discursiva em profundidade e reconstrução histórica exaustiva de etimologias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Estrutura e formação de palavras.
- Elementos estruturais e processos de formação de palavras.
- Formas de abreviação.
- Estrutura e formação de palavras e formas de abreviação.
- Morfologia e estrutura das palavras.
- Formação de palavras.
- Morfologia: estrutura e formação de palavras.
- Processos de formação de palavras.
- Processos de formação palavras.
- Processo de formação de palavras.
- Estrutura e formação de palavras, abreviações e classes de palavras.
- Estrutura e formação de palavras e abreviações.
- Morfologia: estrutura e formação de palavras e vozes verbais e sua conversão.
- Processos de formação das palavras.
- Estrutura e formação de palavras, abreviação e classes de palavras.
- Estrutura e formação de palavras; formas de abreviação.
- Formação de palavras, abreviação e classes de palavras.
- Estrutura e formação de palavras, classes de palavras e modalizadores.
- Estrutura, formação e abreviação de palavras.
- Morfologia: estrutura e formação das palavras; radicais gregos e latinos.
- Formação, estrutura e classes de palavras e modalizadores.
- Morfologia: estrutura e formação das palavras (radical, afixos, desinências, vogais e consoantes de ligação).
- Processos de formação de palavras: derivação, composição, hibridismo, onomatopeia, siglas e abreviações.
- Morfologia: formação e classe de palavras.
- Estrutura do vocábulo: flexão dos vocábulos, seu valor e significação dentro de frases.
- Descrição linguística: unidades linguísticas: orações, sintagmas, palavras, morfemas.
- Estrutura de palavras.
- Formação de palavras, abreviações e classes gramaticais.
- Textos da esfera científica/acadêmica — Análise linguística: Identifique os processos corriqueiros de formação de palavras (afixos, derivações e composições) essenciais ao léxico técnico.
- Processos de formação de palavras e emprego das classes de palavras.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.formacao-de-palavras.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.formacao-de-palavras",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 094 — matematica.quantificadores-diagramas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Matemática: Quantificadores e diagramas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Leitura de proposições categóricas com todo, algum e nenhum; relações de inclusão, interseção e disjunção entre conjuntos; tradução entre linguagem natural e diagramas de Venn; negação de enunciados quantificados; inferências válidas e inválidas.
Fica de fora (outras matérias tratam): Lógica de predicados com símbolos formais, demonstrações avançadas, quantificadores múltiplos com dependência de variáveis e cálculos de cardinalidade além de conjuntos finitos simples.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Diagramas lógicos.
- Lógica sentencial (ou proposicional): Diagramas lógicos.
- Diagramas lógicos e resolução de problemas.
- Lógica sentencial ou proposicional: diagramas lógicos.
- Diagramas lógicos (silogismos e relações de inclusão/exclusão).
- Diagramas de Venn e aplicações em lógica.
- Quantificadores lógicos: todo, algum, nenhum.
- Diagramas lógicos e lógica de primeira ordem.
- Diagramas lógicos, envolvendo as proposições categóricas.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, cálculo com todos os dados no enunciado, interpretação de tabela ou gráfico, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo matematica.quantificadores-diagramas.banco-N.json, onde N é o lote)
```json
{
  "materia": "matematica.quantificadores-diagramas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 095 — administracao-geral.funcoes-administrativas
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Geral: Funções administrativas: planejar, organizar, dirigir e controlar** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Noções das teorias da administração (clássica, científica, relações humanas, burocrática, neoclássica, sistêmica e contingencial); processo administrativo e funções planejar, organizar, dirigir e controlar; planejamento estratégico, tático e operacional, missão, visão e análise SWOT; organização: estrutura formal e informal, organograma, departamentalização, centralização, descentralização, delegação, amplitude de controle, estruturas linear, funcional, linha-staff, matricial e em rede; direção: liderança, motivação (Maslow, Herzberg, McGregor, expectativa) e comunicação; controle: tipos, níveis e etapas; tomada de decisão: tipos, modelo racional, racionalidade limitada e heurísticas; cultura organizacional: níveis e elementos.
Fica de fora (outras matérias tratam): Gestão de pessoas (recrutamento, treinamento, avaliação de desempenho, competências), gestão de projetos e da qualidade, modelos de administração pública e governança, e organização da administração pública no direito administrativo, tratados em matérias próprias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Cultura organizacional.
- Características básicas das organizações formais modernas: tipos de estrutura organizacional, natureza, finalidades e critérios de departamentalização.
- Funções da Administração: planejamento, organização, direção e controle.
- Funções de administração: planejamento, organização, direção e controle.
- Tipos de departamentalização: características, vantagens e desvantagens de cada tipo.
- Organização informal.
- Administração por objetivos.
- Evolução da administração: principais abordagens da administração (clássica até contingencial).
- Processo decisório.
- Controle: tipos, vantagens e desvantagens.
- Estrutura organizacional.
- Conceitos básicos em administração: eficiência, eficácia, efetividade, qualidade.
- Teorias da administração.
- Planejamento tático.
- Planejamento operacional.
- Controle: características.
- Motivação, liderança, comunicação, descentralização e delegação.
- Processo administrativo e funções de planejamento, organização, direção e controle.
- Planejamento: princípios e conceitos básicos, níveis estratégico, tático e operacional.
- Processo administrativo: Processo de planejamento.
- Evolução da administração.
- Comunicação.
- Processo Administrativo: planejamento, organização, direção e controle.
- Organização.
- Processo decisório: tipos de decisões.
- Processo de planejamento: Planejamento tático.
- Processo de planejamento: Planejamento operacional.
- Processo de planejamento: Administração por objetivos.
- Processo de planejamento: Processo decisório.
- Características básicas das organizações formais modernas: tipos de estrutura organizacional, natureza e finalidades.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-geral.funcoes-administrativas.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-geral.funcoes-administrativas",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 096 — direito-civil.pessoas-naturais
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Civil: Pessoas naturais: personalidade, capacidade e domicílio** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Início e término da personalidade civil, nascituro, comoriência, capacidade e emancipação, autonomia de pessoas com deficiência após Lei 13.146/2015, direitos da personalidade, ausência e morte presumida, domicílio das pessoas naturais (Código Civil, arts. 1º a 39 e 70 a 78).
Fica de fora (outras matérias tratam): Pessoas jurídicas, capacidade processual, detalhamento de sucessões além das etapas patrimoniais da ausência e regras especiais de competência processual.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Domicílio.
- Direitos da Personalidade.
- Pessoas naturais.
- Pessoas naturais: Domicílio.
- Personalidade.
- Pessoas naturais: Personalidade.
- Pessoas naturais: Capacidade.
- Pessoas naturais: Direitos da personalidade.
- Pessoas naturais: Conceito.
- Capacidade.
- Pessoas naturais: Ausência.
- Início da pessoa natural.
- Pessoas naturais: Início da pessoa natural.
- Ausência.
- Pessoas naturais: Nome civil.
- Pessoas naturais: Estado civil.
- Pessoas naturais: Existência.
- Personalidade e Capacidade.
- Pessoas naturais: nome.
- Domicílio Civil.
- Do domicílio.
- Pessoa natural.
- Nome.
- Pessoas naturais: personalidade, capacidade, direitos de personalidade.
- Da Pessoa Natural.
- Das Pessoas.
- Das pessoas naturais.
- Domicílio e residência.
- Pessoas.
- Pessoas naturais. Conceito. Início da pessoa natural. Personalidade. Capacidade.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-civil.pessoas-naturais.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-civil.pessoas-naturais",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 097 — informatica.analise-dados-ia
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Análise de dados e inteligência artificial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Ciclo de análise; mineração de dados; inteligência de negócios e visualização; big data; aprendizado de máquina supervisionado e não supervisionado; avaliação, sobreajuste, viés, explicabilidade e usos de inteligência artificial; transformação digital orientada por dados.
Fica de fora (outras matérias tratam): Programação detalhada de modelos, demonstrações matemáticas avançadas, treinamento de redes neurais em código e implantação em produto específico.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos de Inteligência Artificial, Análise de Dados e Big Data.
- Técnicas para pré-processamento de dados.
- Visualização e análise exploratória de dados.
- Regressão linear e logística, árvores de decisão, random forests, SVM e K-NN.
- Visualização de dados e storytelling.
- Ciência de dados.
- Modelo de referência CRISP-DM.
- Modelagem preditiva. Aprendizado de máquina. Mineração de texto.
- Manipulação, tratamento e visualização de dados.
- Aprendizado supervisionado: regressão, classificação, métricas, regularização e validação de modelos.
- Aprendizado não supervisionado, PCA, K-Means, misturas gaussianas e regras de associação.
- Aprendizado supervisionado: Regressão e Classificação.
- Métodos e etapas de análise de dados.
- Big Data.
- Conceitos básicos de inteligência artificial: aprendizado supervisionado, não supervisionado e por reforço.
- Tendências, projeções e analytics.
- Inteligência artificial, análise de dados e big data.
- Noções de análise de dados.
- Análise de Agrupamentos (Clusterização).
- Análise de agrupamentos (clusterização). Detecção de anomalias.
- Modelagem estatística e aprendizado de máquina: visualização e comunicação de resultados.
- Aprendizado supervisionado e não supervisionado.
- Processos de treinamento, validação e teste.
- Noções de Análise e Mineração de Dados: Estrutura e Organização dos Dados (dados estruturados e não estruturados), Coleta, Tratamento, Armazenamento e Visualização de dados.
- Noções de Inteligência Artificial e Aprendizado de Máquina: Compreensão básica das principais técnicas de aprendizado de máquina, como agrupamento (clustering), classificação, detecção de anomalias.
- Aprendizado não supervisionado: redução de dimensionalidade e PCA.
- K-Means, mistura de Gaussianas e regras de associação.
- Técnicas de classificação, regressão, agrupamento, redução de dimensionalidade e associação.
- Aprendizado não supervisionado, PCA, K-Means, mistura de Gaussianas e regras de associação.
- Métricas de avaliação, overfitting, underfitting, regularização e seleção de modelos.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.analise-dados-ia.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.analise-dados-ia",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 098 — portugues.funcoes-linguagem-teoria
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Português: História da língua, níveis e funções da linguagem** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Formação histórica do português e contato linguístico; língua, linguagem, fala e norma; variações e registros; elementos da comunicação; seis funções da linguagem; noções de discurso relatado e intertextualidade na leitura funcional de textos.
Fica de fora (outras matérias tratam): Morfologia e sintaxe detalhadas, redação oficial, regras ortográficas pormenorizadas e análise aprofundada de gêneros textuais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Funções da linguagem.
- Elementos dos atos de comunicação.
- Os diversos níveis de linguagem.
- As funções da linguagem.
- Registros de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas.
- Linguagem e comunicação: situação comunicativa e variações linguísticas.
- Níveis de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas: Os diversos níveis de linguagem.
- As estruturas linguísticas no processo de construção de mensagens adequadas: As funções da linguagem.
- Discurso, registros, funções da linguagem e atos de comunicação.
- Funções da linguagem: Elementos dos atos de comunicação.
- Tipos de discurso, registros e funções da linguagem e elementos dos atos de comunicação.
- Elementos da comunicação e funções da linguagem.
- Elementos de comunicação.
- Comunicação, linguagem e variações linguísticas.
- Comunicação, linguagem, variações linguísticas, gêneros e tipologias textuais.
- Funções da linguagem e elementos dos atos de comunicação.
- Registros e funções da linguagem.
- Registros e funções da linguagem e elementos dos atos de comunicação.
- Tipos de discurso, registros e funções da linguagem.
- Tipos de discurso, registros e funções da linguagem e elementos da comunicação.
- Registros e funções da linguagem e atos de comunicação.
- Registros, funções da linguagem, discurso e comunicação.
- Registros, funções da linguagem e elementos dos atos de comunicação.
- Ordem direta e inversa, tipos de discurso, registros e funções da linguagem.
- Discurso, registros, funções da linguagem e elementos da comunicação.
- Teoria da linguagem: história da língua, níveis de linguagem e funções da linguagem.
- Níveis de linguagem, funções da linguagem e intertextualidade.
- Pragmática, significado contextual, níveis e funções da linguagem e intertextualidade.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.funcoes-linguagem-teoria.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.funcoes-linguagem-teoria",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 099 — direito-constitucional.ordem-social
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Ordem social na Constituição** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Princípios da ordem social; seguridade social (financiamento, saúde, previdência e assistência); educação, cultura e desporto; ciência, tecnologia e inovação; meio ambiente; família, criança, adolescente, jovem, pessoa idosa e direitos indígenas na Constituição (arts. 193 a 232).
Fica de fora (outras matérias tratam): Cálculos previdenciários, exame integral da legislação infraconstitucional, políticas setoriais fora da matéria e questões de processo orçamentário ou controle judicial não necessárias para distinguir direitos constitucionais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ordem social.
- Seguridade social.
- Meio ambiente.
- Ordem social: Seguridade Social.
- Ordem social: meio ambiente.
- Educação, cultura e desporto.
- Ordem social: base e objetivos da ordem social.
- Constituição Federal de 1988 (Artigos n° 205 a n° 214).
- Constituição Federal de 1988 (do art. 205 ao art. 214).
- Comunicação social.
- Família, criança, adolescente, idoso, indígenas.
- Ordem social: ciência e tecnologia.
- Ordem social: índios.
- Da Previdência Social.
- Ordem social e Previdência Social.
- Direitos dos Povos indígenas e das comunidades tradicionais.
- Direitos e interesses das populações indígenas.
- Da Ordem Social.
- Ciência, tecnologia e inovação.
- Família, criança, adolescente, jovem e idoso.
- Família, criança, adolescente e idoso.
- Ordem social: Disposições Gerais.
- Ordem social: Previdência Social.
- Ordem social: família, criança, adolescente, jovem e idoso.
- Índios.
- Da Ordem Social: Seguridade Social (Disposição Geral).
- Da Ordem Social: Base e objetivos da ordem social.
- Ordem social, base e objetivos.
- Base e objetivos da ordem social.
- Ciência e tecnologia.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.ordem-social.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.ordem-social",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

==================================================
# BANCO 100 — direito-penal.aplicacao-lei-penal
==================================================
Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Penal: Aplicação da lei penal** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 4 lotes, um por resposta. Quando eu pedir 'lote N', entregue só aquele lote, sem repetir casos dos lotes anteriores:
- **Lote 1 (CEBRASPE, 50 questões):** 50 itens de Certo/Errado no estilo Cebraspe: afirmação curta e técnica, com o detalhe trocado (prazo, competência, exceção, conceito vizinho); explanation começa com Certo. ou Errado.
- **Lote 2 (FGV, 30 questões):** 30 de múltipla escolha A a E no estilo FGV: caso concreto mais longo, aplicação e interpretação; as erradas são aplicações plausíveis mas equivocadas.
- **Lote 3 (FCC, 30 questões):** 30 de múltipla escolha A a E no estilo FCC: mais literal, cobra o texto da norma/regra com o detalhe de prazo, número, competência ou exceção.
- **Lote 4 (VUNESP, 30 questões):** 30 de múltipla escolha A a E no estilo Vunesp e bancas de nível médio: enunciado curto e direto, uma regra por questão.

## Escopo
Legalidade e anterioridade; interpretação e integração; sucessão de leis penais; tempo e lugar do crime; territorialidade, extraterritorialidade e contagem de prazo penal.
Fica de fora (outras matérias tratam): Teoria geral do crime, espécies de pena e crimes em espécie.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Aplicação da lei penal.
- Tempo e lugar do crime.
- Territorialidade e extraterritorialidade da lei penal.
- Lei penal no tempo e no espaço.
- Aplicação da lei penal: Tempo e lugar do crime.
- Aplicação da lei penal: Irretroatividade da lei penal.
- A lei penal no tempo e no espaço.
- Analogia.
- Contagem de prazo.
- Irretroatividade da lei penal.
- Aplicação da lei penal: Interpretação da lei penal.
- Aplicação da lei penal: Analogia.
- Interpretação da lei penal.
- Lei penal excepcional, especial e temporária.
- Pena cumprida no estrangeiro.
- Princípios básicos.
- Aplicação da lei penal: A lei penal no tempo e no espaço.
- Aplicação da lei penal: Conflito aparente de normas penais.
- Aplicação da lei penal: territorialidade e extraterritorialidade da lei penal.
- Princípios da legalidade e da anterioridade.
- Aplicação da lei penal: princípios da legalidade e da anterioridade.
- Aplicação da lei penal: contagem de prazo.
- Princípios aplicáveis ao Direito Penal.
- Eficácia da sentença estrangeira.
- Aplicação da lei penal: lei penal excepcional, especial e temporária.
- Princípios básicos do Direito Penal.
- Aplicação da lei penal: pena cumprida no estrangeiro.
- Aplicação da lei penal: eficácia da sentença estrangeira.
- Aplicação da lei penal: frações não computáveis da pena.
- Conflito aparente de normas penais.

## Em todos os lotes
- Dificuldade: 30% fáceis, 40% médias, 30% difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Casos DIFERENTES entre si e entre lotes; nada de repetir o mesmo caso com outras palavras.
- Cada questão leva "estilo" com o nome do estilo do lote (CEBRASPE, FGV, FCC, VUNESP).

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-penal.aplicacao-lei-penal.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-penal.aplicacao-lei-penal",
  "lote": 1,
  "estilo": "CEBRASPE",
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "estilo": "CEBRASPE", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".

