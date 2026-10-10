Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Organização administrativa: direta, indireta e órgãos públicos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Administração direta e indireta; centralização, descentralização por outorga e colaboração, concentração, desconcentração; órgãos públicos, teoria do órgão e suas classificações; entidades indiretas em visão geral; art. 37, XIX e XX, da Constituição; vinculação, hierarquia e organização por decreto nos limites constitucionais.
Fica de fora (outras matérias tratam): Regime detalhado das autarquias, agências, fundações, empresas estatais e consórcios; contratos de concessão e permissão; serviços públicos em profundidade; entidades do terceiro setor.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Autarquias, fundações, empresas públicas e sociedades de economia mista.
- Administração direta e indireta.
- Noções de organização administrativa.
- Noções de organização administrativa: Centralização, descentralização, concentração e desconcentração.
- Noções de organização administrativa: Administração direta e indireta.
- Organização administrativa.
- Organização administrativa: Centralização, descentralização, concentração e desconcentração.
- Organização administrativa: administração direta e indireta; centralizada e descentralizada; autarquias, fundações, empresas públicas, sociedades de economia mista.
- Centralização, descentralização, concentração e desconcentração.
- Administração direta e indireta, centralizada e descentralizada.
- Organização administrativa: Administração direta e indireta.
- Órgãos públicos.
- Órgão público: conceito e classificação.
- Organização administrativa: noções gerais.
- Administração pública direta e indireta.
- Conceito de administração pública sob os aspectos orgânico, formal e material.
- Organização administrativa: administração direta e indireta, centralizada e descentralizada.
- Organização administrativa: centralização, descentralização, concentração, desconcentração, administração direta e indireta.
- Organização administrativa da União; administração direta e indireta.
- Administração pública: Organização, descentralização, desconcentração, órgãos públicos.
- Administração pública sob os aspectos orgânico, formal e material.
- Centralização e descentralização da atividade administrativa; administração direta e indireta.
- Órgãos públicos: conceito, natureza e classificação.
- Organização administrativa: centralização, descentralização, concentração e desconcentração; administração direta e indireta.
- Administração direta e indireta, centralizada e descentralizada: autarquias, fundações, empresas públicas e sociedades de economia mista.
- Organização administrativa, Administração direta e indireta, centralização e descentralização.
- Organização administrativa da União: administração direta e indireta.
- Administração indireta e entidades paralelas.
- Administração direta e indireta, autarquias, fundações, empresas públicas e sociedades de economia mista.
- Organização administrativa. Centralização, descentralização, concentração e desconcentração.

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.organizacao-administrativa.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.organizacao-administrativa",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
