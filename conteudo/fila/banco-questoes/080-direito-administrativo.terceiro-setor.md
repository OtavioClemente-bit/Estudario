Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Terceiro setor, entes de colaboração e convênios** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Terceiro setor e entidades paraestatais; serviços sociais autônomos, fundações de apoio, organizações sociais, OSCIP, parcerias do MROSC (colaboração, fomento e cooperação), convênios e contratos de repasse com referência ao regime federal vigente.
Fica de fora (outras matérias tratam): Consórcios públicos em detalhe, concessões, contratos administrativos em geral, contabilidade pormenorizada de OSCIP e regimes estaduais ou municipais especiais.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Entidades paraestatais.
- Organização administrativa: Entidades paraestatais e terceiro setor.
- Entidades paraestatais e terceiro setor: serviços sociais autônomos, entidades de apoio, organizações sociais, organizações da sociedade civil de interesse público.
- Serviços sociais autônomos, entidades de apoio, organizações sociais, organizações da sociedade civil de interesse público.
- Convênios e consórcios.
- Disposições doutrinárias: Convênios e instrumentos congêneres.
- Organizações sociais.
- Contratos de Gestão.
- Convênios.
- Convênios e consórcios administrativos.
- Entidades paraestatais e terceiro setor.
- Terceiro Setor.
- Contrato de gestão.
- Entidades paraestatais e terceiro setor: serviços sociais autônomos, entidades de apoio, organizações sociais e OSCIPs.
- Entidades paraestatais e o Terceiro Setor.
- Parcerias entre a Administração Pública e o terceiro setor.
- Convênio.
- Convênios administrativos.
- Convênios e termos similares.
- Entidades do Terceiro Setor.
- Entidades paraestatais e terceiro setor: Serviços sociais autônomos.
- Entidades paraestatais e terceiro setor: Entidades de apoio.
- Entidades paraestatais e terceiro setor: Organizações sociais.
- Entidades paraestatais e terceiro setor: Organizações da sociedade civil de interesse público (OSCIP).
- Parcerias entre a Administração Pública e o terceiro setor (Lei nº 13.019/2014 e suas alterações).
- Serviços sociais autônomos.
- Entidades de apoio.
- Organizações da sociedade civil de interesse público.
- Organizações da Sociedade Civil.
- Convênios, acordos, ajustes e instrumentos congêneres.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.terceiro-setor.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.terceiro-setor",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
