Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Constitucional: Defesa do Estado e das Instituições Democráticas** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Sistema constitucional de crises; Conselhos da República e de Defesa Nacional; estados de defesa e sítio (pressupostos, consulta, autorização, duração, restrições, controles e efeitos); noções institucionais das Forças Armadas; organização constitucional da segurança pública, atribuições, subordinação, guardas municipais e segurança viária.
Fica de fora (outras matérias tratam): Intervenção federal em profundidade; regime detalhado das Forças Armadas e serviço militar; regime dos militares estaduais e normas infraconstitucionais de segurança pública.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Defesa do Estado e das instituições democráticas.
- Defesa do Estado e das instituições democráticas: Segurança pública.
- Poder executivo: Conselho da República e de Defesa Nacional.
- Defesa do Estado e das instituições democráticas: estado de defesa, estado de sítio, Forças Armadas e segurança pública.
- Conselho da República e de Defesa Nacional.
- Estado de defesa e estado de sítio.
- Poder Executivo: Do Conselho da República e do Conselho de Defesa Nacional.
- Conselho da república.
- Conselho de defesa nacional.
- Defesa do Estado e das instituições democráticas: Organização da segurança pública.
- Conselho da República e Conselho de Defesa Nacional.
- Defesa do Estado e das instituições democráticas: segurança pública; organização da segurança pública.
- Estado de Exceção.
- Estado de defesa.
- Estado de sítio.
- Defesa do Estado e das instituições democráticas, segurança pública e organização da segurança pública.
- Defesa do Estado e das instituições democráticas: Estado de defesa e estado de sítio.
- Conselho da República e Conselho de Defesa.
- Segurança pública: Organização da segurança pública.
- Constituição Federal: defesa do Estado e das instituições democráticas.
- Segurança Pública conforme o artigo 144 da Constituição Federal.
- Funções essenciais à Justiça, defesa do Estado e instituições democráticas.
- Sistema constitucional das crises.
- Organização da Segurança Pública.
- Defesa do Estado e das instituições democráticas: Atribuições constitucionais da Polícia Judiciária.
- Atribuições constitucionais da Polícia Judiciária.
- Funções essenciais à justiça: Defesa do Estado e das instituições democráticas.
- Segurança pública e artigo 144 da Constituição Federal.
- Constituição Federal: defesa do Estado, instituições democráticas e segurança pública.
- Defesa do Estado e das instituições democráticas; segurança pública; forças armadas.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-constitucional.defesa-estado-instituicoes.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-constitucional.defesa-estado-instituicoes",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
