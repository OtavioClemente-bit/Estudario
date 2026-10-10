Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Lei de Abuso de Autoridade (Lei 13.869/2019)** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Lei nº 13.869/2019: conceito e finalidade específica; sujeitos ativos; divergência interpretativa; ação penal; efeitos da condenação; penas restritivas; independência de instâncias; tipos penais dos arts. 9º a 38, incluindo violência institucional do art. 15-A, e regras gerais de procedimento.
Fica de fora (outras matérias tratam): Exame autônomo dos crimes funcionais de outras leis, improbidade administrativa, procedimento disciplinar geral e responsabilidade civil do Estado; apenas os pontos de interface expressos nos arts. 4º e 6º a 8º são abordados.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Lei nº 13.869/2019.
- Crimes de abuso de autoridade.
- Crimes de abuso de autoridade (Lei nº 13.869/2019 e suas alterações).
- Abuso de autoridade.
- Lei nº 4.898/1965 (abuso de autoridade).
- Lei nº 13.869/2019, e suas alterações (abuso de autoridade).
- Lei nº 13.869/2019: Crimes de abuso de autoridade.
- Lei nº 13.869/2019 (Crimes de abuso de autoridade).
- Lei nº 13.869/2019 (abuso de autoridade).
- Lei 13.869/2019 Lei de abuso de autoridade.
- Abuso de autoridade: Lei nº 13.869/2019.
- Legislação penal especial: abuso de autoridade.
- Crimes de abuso de autoridade (Lei nº 13.869/2019).
- Lei Federal nº 13.869/2019, que dispõe sobre os crimes de abuso de autoridade.
- Lei Federal nº 13.869/2019 (Lei de Abuso de Autoridade).
- Lei de Abuso de Autoridade (Lei nº 13.869/2019).
- Lei nº 13.869/2019: abuso de autoridade.
- Lei nº 13.869/2019 e suas alterações (abuso de autoridade).
- Lei nº 13.869/2019 (Lei de Abuso de Autoridade).
- Lei Federal nº 13.869/2019 - Lei de Abuso de Autoridade.
- Abuso de autoridade (Lei nº 13.869/2019).
- Lei de abuso de autoridade (Lei nº 13.869/2019 e suas alterações).
- Lei de abuso de autoridade (Lei nº 13.869/2019 e alterações).
- Direito de representação e processo de responsabilidade administrativa, civil e penal nos casos de abuso de autoridade.
- Processo nos crimes: de abuso de autoridade, de responsabilidade dos funcionários públicos.
- Leis Federais n. 13.869/2019.
- Abuso de autoridade (Lei nº 13.869/2019 e suas alterações).
- Lei nº 13.869/2019 e suas alterações: Crimes de abuso de autoridade.
- Lei Federal nº 13.869/2019 - Crimes de Abuso de Autoridade.
- Penais especiais: Lei nº 13.869/19, Abuso de Autoridade.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.abuso-autoridade.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.abuso-autoridade",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
