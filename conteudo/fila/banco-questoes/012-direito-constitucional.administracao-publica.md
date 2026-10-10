Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Administração pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Constituição Federal de 1988, arts. 37 a 43: princípios e organização da administração direta e indireta, cargos, empregos e funções, concursos, remuneração, teto, acumulação, mandato eletivo, servidores civis, previdência, estabilidade, responsabilidade estatal, militares estaduais e regiões administrativas.
Fica de fora (outras matérias tratam): Processo legislativo, CPI, estrutura e competências do Judiciário, CNJ, Justiça do Trabalho, CSJT, súmulas vinculantes como procedimento, controle externo dos arts. 31 e 70 a 75 e conteúdo infraconstitucional detalhado de licitações e contratos.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Administração Pública.
- Administração pública: Disposições gerais.
- Administração pública: Disposições gerais, servidores públicos.
- Administração Pública e servidores públicos.
- Da Administração Pública: disposições gerais; dos servidores públicos.
- Administração pública: Servidores públicos.
- Servidores públicos.
- Administração pública: disposições gerais e servidores públicos.
- Administração Pública na CF/88.
- Efetividade, estabilidade e vitaliciedade.
- Da Administração Pública: disposições gerais.
- Servidor efetivo e vitalício: garantias.
- Administração Pública: disposições gerais, servidores públicos civis e militares.
- Constituição Federal de 1988: Administração Pública, artigos 37 ao 41.
- Cargo, emprego e função pública: Efetividade, estabilidade e vitaliciedade.
- Administração Pública na Constituição Federal, arts. 37 a 41.
- Dispositivos pertinentes à administração pública contidos na Constituição Federal de 1988.
- Administração pública na Constituição Federal.
- Constituição Federal: Título III, Capítulo VII, Seções I e II.
- Disposições doutrinárias: Efetividade, estabilidade e vitaliciedade.
- Administração Pública. Disposições gerais, servidores públicos.
- A Administração na Constituição de 1988.
- Regime constitucional dos servidores públicos na Constituição Federal.
- Regime constitucional dos servidores públicos na Constituição Federal de 1988.
- Estabilidade.
- Administração Pública e servidores públicos na Constituição.
- Disposições gerais, servidores públicos.
- Constituição Federal de 1988: organização do Estado, Administração Pública, servidores públicos e organização dos Poderes.
- Dos servidores públicos.
- Princípios constitucionais e normas que regem a administração pública (artigos de 37 a 41 da Constituição Federal de 1988).

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.administracao-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.administracao-publica",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
