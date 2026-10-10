Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Organização político-administrativa e entes federativos** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Forma federativa brasileira; soberania e autonomia; arts. 18 e 19; União e bens federais; bens estaduais; auto-organização dos Estados; reorganização territorial; Municípios e lei orgânica; alterações municipais com LC nº 230/2026; Distrito Federal; Territórios; noções pontuais de fiscalização e organização.
Fica de fora (outras matérias tratam): Repartição detalhada das competências dos arts. 21 a 30; intervenção federal e estadual; regime geral dos bens públicos. As referências pontuais a competências são apenas as indispensáveis à compreensão dos entes.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Organização político-administrativa do Estado.
- Estado federal brasileiro, União, Estados, Distrito Federal, municípios e territórios.
- Da organização do Estado: da organização político-administrativa; da União, dos Estados Federados, dos Municípios, do Distrito Federal e dos Territórios.
- Organização do Estado.
- Organização político-administrativa do Estado: Estado federal brasileiro, União, estados, Distrito Federal, municípios e territórios.
- Organização do Estado: organização político-administrativa.
- Organização do Estado: Estado federal brasileiro.
- Organização do Estado: Estados federados.
- Organização do Estado: Municípios.
- Organização do Estado: Territórios.
- Municípios.
- Organização político-administrativa da União, estados, Distrito Federal, municípios e territórios.
- Organização político-administrativa da União, Estados, Distrito Federal e Municípios.
- Organização político-administrativa: União, estados, Distrito Federal, municípios e territórios.
- Organização político-administrativa.
- Da organização do Estado: da organização político administrativa: da União.
- Estados federados.
- Organização do Estado: organização político-administrativa, União, Estados, Municípios, Distrito Federal e Territórios.
- Organização do Estado: A União.
- Organização do Estado: O Distrito Federal.
- União.
- Territórios.
- Constituição Federal de 1988: organização do Estado, artigos 18 ao 43.
- Organização do Estado: União.
- Organização do Estado: Distrito Federal.
- Organização político‐administrativa.
- Organização político-administrativa do Estado federal brasileiro: União, Estados, Distrito Federal, Municípios e Territórios.
- Distrito Federal.
- Constituição da República Federativa do Brasil de 1988: Organização político-administrativa do Estado.
- Organização do Estado: União e Administração Pública.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, literalidade da lei com o detalhe que a prova troca, jurisprudência e súmulas, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Cite artigo, lei, súmula e prazo com o número, conferidos no texto oficial vigente (planalto.gov.br, STF, STJ). Se não tiver certeza de um número, explique sem ele — nunca invente.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.organizacao-politico-administrativa.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.organizacao-politico-administrativa",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
