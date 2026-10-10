Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Criptografia, certificação digital e segurança de redes** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Criptografia simétrica e assimétrica, funções hash, assinatura digital, certificado digital e infraestrutura de chaves públicas, noções de ICP-Brasil, VPN, firewall e sistemas de detecção e prevenção de intrusão (IDS/IPS).
Fica de fora (outras matérias tratam): Tipos de malware, phishing, antivírus e cuidados básicos do usuário (ficam em informatica.seguranca); matemática dos algoritmos, configuração de equipamentos por fabricante e perícia forense.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Assinatura e certificação digital.
- Certificação digital.
- Princípios de segurança, confidencialidade e assinatura digital.
- Criptografia.
- Redes de computadores: Redes privadas virtuais (VPN).
- Criptografia: Conceitos básicos e aplicações.
- Criptografia simétrica e assimétrica.
- Segurança da informação: certificação digital, conceito e funcionalidades.
- Infraestrutura de chaves públicas e certificação digital.
- Protocolos e mecanismos de segurança: VPN, SSL/TLS.
- Noções de criptografia e proteção de dados: hash criptográfico (MD5, SHA-1, SHA-256), assinaturas digitais.
- Infraestrutura de chaves públicas — public key infrastructure (PKI).
- Segurança de redes de computadores.
- IDS, IPS e SIEM.
- Autenticação, criptografia, certificado digital e assinatura digital.
- Criptografia e proteção de dados em trânsito e em repouso; sistemas criptográficos simétricos e assimétricos e principais protocolos.
- Tokens e outros dispositivos de segurança.
- Comunicação segura com SSL e TLS.
- Criptografia simétrica e assimétrica;
- Certificação digital;
- Mecanismos de segurança: firewall, detecção de intrusão e autenticação.
- Criptografia, assinatura e certificação digital.
- Protocolos SSL, TLS e IPsec.
- Conceitos de Firewall.
- Ambiente de rede seguro.
- Segurança física e lógica, criptografia, protocolos, assinatura, certificação digital, hashes e esteganografia.
- Infraestrutura de chaves públicas e ICP-Brasil.
- Protocolos criptográficos.
- Hashes e algoritmos de hash.
- Esteganografia e criptoanálise.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.criptografia-certificacao.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.criptografia-certificacao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
