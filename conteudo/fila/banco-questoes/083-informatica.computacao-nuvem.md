Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Computação em nuvem** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.computacao-nuvem.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.computacao-nuvem",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
