Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Direito Processual Civil: Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção e ação popular** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Habeas corpus, mandado de segurança individual e coletivo, habeas data, mandado de injunção, ação popular, ação civil pública, recursos cíveis em geral e meios extrajudiciais de solução de conflitos na Administração Pública.
Fica de fora (outras matérias tratam): Procedimentos penais específicos, recursos de legislação especial não indicados e estudo exaustivo de arbitragem privada.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ação civil pública.
- Habeas corpus, mandado de segurança, mandado de injunção e habeas data.
- Mandado de segurança individual e coletivo.
- Mandado de segurança.
- Procedimentos especiais: mandado de segurança, ação popular, ação civil pública, ação de improbidade administrativa.
- Habeas Corpus.
- Mandado de Segurança Coletivo.
- Mandado de segurança, mandado de injunção, ação popular, habeas data e habeas corpus.
- Remédios constitucionais: habeas data, habeas corpus, mandado de segurança, ação popular e mandado de injunção.
- Ações diversas: Mandado de segurança.
- Ações diversas: Ação civil pública.
- Direitos e garantias fundamentais: habeas corpus, mandado de segurança, mandado de injunção e habeas data.
- Processo coletivo e tutela de direitos difusos, coletivos e individuais homogêneos.
- Ações coletivas.
- Mandado de segurança, ação popular, ação civil pública, improbidade, mandado de injunção e habeas data.
- Remédios constitucionais, mandado de segurança, habeas data, mandado de injunção, Ação popular.
- Procedimentos extrajudiciais de solução de conflitos na Administração Pública.
- Ação civil pública (Lei nº 7.347/1985 e alterações).
- Ação civil pública (Lei nº 7.347/1985 e suas alterações).
- Processos nos tribunais, recursos, mandado de segurança, ação popular, ação civil pública e improbidade.
- Remédios constitucionais: habeas-corpus, mandado de segurança.
- Ações específicas: Ação civil pública.
- – Legislação Extravagante: Lei nº 7.347/85 (Ação civil pública).
- Lei nº 12.016/2009 (Mandado de Segurança).
- Ação civil pública, ação popular e ação de improbidade administrativa.
- Habeas corpus, habeas data e mandado de injunção.
- Mandado de segurança, mandado de injunção, habeas data e mandado de segurança coletivo.
- Processo civil no controle de constitucionalidade, ações constitucionais e declaração incidental de inconstitucionalidade.
- Tutela judicial ambiental, ação popular, ação civil pública, mandados constitucionais e tutela de urgência.
- O processo civil e o controle judicial dos atos administrativos: mandado de segurança; Ação popular; Ação civil pública; Ação de improbidade administrativa.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo processo-civil.remedios-constitucionais-mandado-seguranca-habeas.banco-N.json, onde N é o lote)
```json
{
  "materia": "processo-civil.remedios-constitucionais-mandado-seguranca-habeas",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
