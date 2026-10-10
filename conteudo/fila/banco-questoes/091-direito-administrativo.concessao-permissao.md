Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Administrativo: Concessão e permissão de serviço público** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Lei 8.987/1995 vigente: concessões e permissões, licitação, tarifa, encargos, intervenção, extinção, reversão de bens e distinções pertinentes sobre autorização.
Fica de fora (outras matérias tratam): Teoria geral completa de serviços públicos, direitos do usuário fora do necessário à adequação e regimes setoriais especiais não previstos na Lei 8.987.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Delegação: concessão, permissão e autorização.
- Concessão, permissão e autorização de serviços públicos.
- Lei nº 8.987/1995.
- Serviços públicos: Delegação.
- Formas de delegação de serviço público.
- Extinção, reversão dos bens.
- Concessão, permissão e autorização.
- Extinção da concessão de serviço público e reversão dos bens.
- Lei nº 8.987/1995 (Lei de Concessões).
- Serviços públicos: regulação, concessão, permissão e autorização do serviço público.
- Formas de prestação e meios de execução: Delegação.
- Permissão e autorização.
- Serviços públicos: concessão, permissão, autorização e delegação.
- Delegação de serviços públicos: concessão, permissão, autorização.
- Regime jurídico da concessão e da permissão de serviço público.
- Delegação.
- Delegação de serviços públicos: concessão, permissão e autorização.
- Serviços delegados.
- Serviços públicos: serviços delegados.
- Serviços públicos: permissão e autorização.
- Parceria Público-Privada: Lei nº 8.987/1995, que dispõe sobre o regime de concessão e permissão da prestação de serviços públicos e Lei nº 11.079/2004, que institui normas gerais para licitação e contratação de parceria público-privada no âmbito da administração pública.
- Delegação por concessão, permissão e autorização.
- Lei nº 8.987/1995 e alterações.
- Concessão e permissão de serviços públicos (Lei nº 8.987/1995 e suas alterações).
- Concessão, permissão, autorização e delegação.
- Delegação de serviços públicos por autorização, permissão e concessão.
- Permissão, concessão e autorização de serviços públicos.
- Serviços públicos: extinção da concessão de serviço público e reversão dos bens.
- Concessões e permissões de serviços públicos.
- Conceito, características.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo direito-administrativo.concessao-permissao.banco-N.json, onde N é o lote)
```json
{
  "materia": "direito-administrativo.concessao-permissao",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
