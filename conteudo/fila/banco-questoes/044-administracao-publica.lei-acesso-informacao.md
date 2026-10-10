Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Pública: Lei de Acesso à Informação** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Fundamentos e abrangência da Lei nº 12.527/2011; diretrizes; transparência ativa e passiva; direitos de acesso e pedidos; prazos, recursos e omissão; acesso parcial; informações pessoais; responsabilidades; aspectos essenciais dos Decretos nº 7.724/2012 e nº 8.777/2016; alterações da Lei nº 15.141/2025.
Fica de fora (outras matérias tratam): Transparência fiscal detalhada pela LRF; classificação exaustiva por todas as autoridades; regime técnico integral da LGPD; procedimentos singulares de outros entes e plataformas.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei nº 12.527/2011 (Lei de Acesso à Informação).
- Lei de acesso à informação.
- Acesso à Informação.
- Lei de Acesso à Informação (Lei nº 12.527/2011).
- Lei nº 12.527/2011: acesso à informação.
- Acesso à informação e proteção de dados pessoais.
- Lei Federal nº 12.527, de 18 de novembro de 2011 – Lei de Acesso à Informação (LAI).
- Lei de Acesso à Informação e regulamentação indicada no edital.
- Lei nº 12.527/2011 (Lei de Acesso à Informação): capítulos I, II, III, IV e V.
- Dec. nº 7.724 e no 7845.
- Lei de acesso à informação: Lei nº 12.527/2011.
- Acesso à Informação: Lei n.º 12.527/2011 (Lei de Acesso à Informação).
- Acesso à Informação: Lei nº 12.527/2011 (Lei de Acesso à Informação).
- Acesso à Informação: Lei nº 12.527/2011, Decreto nº 7.724/2012 e Política de Dados Abertos do Poder Executivo Federal, instituída pelo Decreto nº 8.777/2016.
- Lei nº 12.527/2011: Lei de Acesso à Informação.
- Direito de acesso à informação: normas constitucionais, Lei nº 12.527/2011, Decreto nº 7.724/2012 e Política de Dados Abertos do Poder Executivo Federal.
- Lei nº 12.527/2011 e suas alterações (Lei de Acesso à Informação).
- Lei nº 12.527/2011.
- Canais de acesso à informação.
- Lei de Acesso à Informação (Lei nº 12.527/2011 e suas alterações).
- Direito de acesso à informação no Brasil: normas constitucionais, Lei nº 12.527/2011, Decreto nº 7.724/2012 (Regulamenta a Lei nº 12.527/2011) e Decreto nº 8.777/2016 (Institui a Política de Dados Abertos do Poder Executivo federal).
- Acesso à informação e proteção de dados.
- Acesso à informação e proteção de dados: Legislação pertinente.
- Legislação pertinente: Lei nº 12.527/2011 (Lei de Acesso à Informação).
- Transparência, Acesso à Informação e Integridade: Lei nº 12.527/2011 (Lei de Acesso à Informação – LAI).
- Acesso à informação e proteção de dados: Leis nº 12.527/2011 e nº 13.709/2018.
- Lei de acesso a informações (Lei nº 12.527/2011 e suas alterações).
- Lei Federal nº 12.527/2011 (Lei de Acesso à Informação).
- Acesso à informação. Lei nº 12.527/2011.
- Lei de Acesso à Informação (Lei nº 12.527/2011 e suas alterações): direito de acesso à informação no Brasil, negativas de acesso, informações classificadas e dados abertos.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-publica.lei-acesso-informacao.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-publica.lei-acesso-informacao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
