Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Redes de computadores** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.redes-computadores.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.redes-computadores",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
