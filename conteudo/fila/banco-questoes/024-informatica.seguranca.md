Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Segurança** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceitos e distinções entre malware, vírus, cavalo de Troia, worm, spyware, phishing, pharming, ransomware e spam; formas comuns de propagação, impactos e medidas de prevenção e resposta em ambiente de tribunal.
Fica de fora (outras matérias tratam): Criptografia avançada, configuração de firewall por fabricante, resposta forense especializada, exploração de vulnerabilidades e administração de infraestrutura de segurança.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Segurança da informação: Procedimentos de segurança.
- Noções de vírus, worms e pragas virtuais.
- Segurança: Tipos de vírus, Cavalos de Tróia, Malwares, Worms, Spyware, Phishing, Pharming, Ransomwares, Spam.
- Aplicativos para segurança (antivírus, firewall, antispyware etc.).
- Aplicativos para segurança (antivírus, firewall, anti-spyware etc.).
- Segurança da informação.
- Procedimentos de segurança.
- Noções de vírus, worms e outras pragas virtuais.
- Boas práticas de segurança cibernética, incluindo autenticação de dois fatores e gestão de senhas.
- Segurança da informação: noções de vírus, worms e pragas virtuais.
- Segurança da informação: Aplicativos para segurança (antivírus, firewall, anti-spyware etc.).
- Segurança da informação: Procedimentos de backup.
- Segurança da informação: fundamentos, conceitos e mecanismos de segurança.
- Segurança da informação e segurança cibernética.
- Segurança da informação: Noções de malware.
- Segurança da informação (Noções de vírus e pragas virtuais, Procedimentos de backup).
- Procedimentos de segurança e backup.
- Ferramentas de segurança (antivírus e firewalls).
- Malwares e ataques.
- Conceitos gerais de segurança da informação: proteção contra vírus e outras formas de softwares ou ações intrusivas.
- Conceitos de proteção e segurança.
- Segurança da informação e segurança cibernética: Procedimentos de segurança.
- Segurança da informação e segurança cibernética: Mecanismos de autenticação.
- Usuário e senha, autenticação em dois fatores, senhas de uso único e tokens.
- Segurança da informação e segurança cibernética: Procedimentos de backup.
- Segurança da informação e segurança cibernética: Códigos maliciosos.
- Códigos maliciosos: Vírus, worms e pragas virtuais.
- Segurança da informação e segurança cibernética: Aplicativos para segurança (antivírus, firewall, anti-spyware etc.).
- Segurança da informação e segurança cibernética: Incidentes em redes computacionais.
- Incidentes em redes computacionais: Tipos, tratamento e resposta.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.seguranca.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.seguranca",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
