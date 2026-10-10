Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Crimes contra a Administração Pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.crimes-administracao-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.crimes-administracao-publica",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
