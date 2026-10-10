Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Princípios da Administração Pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
LIMPE do art. 37, caput, da Constituição; art. 37, § 1º; princípios e critérios do art. 2º da Lei nº 9.784/1999; razoabilidade, proporcionalidade, motivação, autotutela, continuidade e segurança jurídica; Súmulas 346 e 473, Súmula Vinculante 13, Tema 138 e referência cautelosa ao Tema 1.000.
Fica de fora (outras matérias tratam): Regimes disciplinares, crimes, procedimentos completos de licitações ou de processos administrativos, atos e poderes administrativos em profundidade, e nepotismo fora da compreensão dos fundamentos e limites da SV 13.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Princípios expressos e implícitos da administração pública.
- Princípios básicos da Administração Pública.
- Princípios básicos da administração.
- Regime jurídico-administrativo: Princípios expressos e implícitos da administração pública.
- Princípios da Administração Pública.
- Princípios constitucionais e legais da Administração Pública.
- Princípios administrativos implícitos.
- Princípios.
- Poderes e deveres da administração pública: Dever de eficiência.
- Poderes e deveres da administração pública: Dever de probidade.
- Princípios da legalidade, impessoalidade, moralidade, publicidade e eficiência.
- Princípios da Administração Pública aplicados à ética.
- Princípios constitucionais da Administração Pública: legalidade, impessoalidade, moralidade, publicidade e eficiência.
- Princípios constitucionais da Administração Pública.
- Administração pública: princípios básicos.
- Regime jurídico‐administrativo: Princípios expressos e implícitos da administração pública.
- Princípios de Direito Administrativo.
- Princípios constitucionais e doutrinários da Administração Pública.
- Administração pública: Princípios expressos e implícitos da administração pública.
- Regime jurídico‐ administrativo: Princípios expressos e implícitos da administração pública.
- Supremacia do interesse público sobre o privado e indisponibilidade, pela administração, dos interesses púbicos.
- Princípios da Supremacia do Interesse Público e da Indisponibilidade.
- Administração pública: princípios constitucionais.
- Princípios da administração pública e responsabilidade civil do Estado.
- Administração pública: princípios básicos, administração direta e indireta.
- Artigo 37 da Constituição Federal (Princípios constitucionais da Administração Pública: Princípios da legalidade, impessoalidade, moralidade, publicidade e eficiência).
- Noções de Direito Administrativo: Princípios de Direito Administrativo.
- Direito Administrativo: Princípios de Direito Administrativo.
- Natureza, fins e princípios da Administração Pública.
- Probidade administrativa e princípios da Administração Pública.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.principios-administracao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.principios-administracao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
