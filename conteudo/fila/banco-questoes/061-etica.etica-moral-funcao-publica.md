Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Ética: Ética, moral e função pública** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Diferenças entre ética e moral; valores, virtudes e princípios; ética aplicada à função pública; princípios constitucionais; cidadania, democracia, respeito, decoro, honestidade, zelo, organização e prioridade em serviço; integridade, governança, transparência, imparcialidade, controle social e decisão responsável.
Fica de fora (outras matérias tratam): Regras detalhadas de códigos profissionais específicos, disciplina jurídica especial de conflito de interesses e procedimentos disciplinares. Referências gerais a normas de ética e integridade são usadas apenas para explicar conceitos e distinguir seus âmbitos de aplicação.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Ética e função pública.
- Ética e moral.
- Ética, princípios e valores.
- Ética no setor público.
- Ética e democracia: exercício da cidadania.
- Ética e democracia.
- Ética no serviço público.
- Ética e moral: definição e distinção.
- Valores, virtude, honestidade, integridade, decoro e zelo no serviço público: conceitos.
- Ética, democracia, cidadania e o papel do servidor público.
- Aplicação dos princípios éticos na Administração Pública.
- Ética e moral, princípios e valores.
- Ética e função pública e ética no setor público.
- Ética aplicada: ética, moral, valores e virtudes.
- Noções de ética empresarial e profissional.
- Atitudes éticas, respeito, valores e virtudes.
- Atitudes no serviço.
- Ética no setor público e improbidade administrativa.
- Comunicação, redes organizacionais, transparência, integridade e ética pública.
- Ética e conduta do servidor público.
- A gestão da ética nas empresas públicas e privadas.
- Comportamento profissional.
- Princípios e valores éticos do serviço público, seus direitos e deveres à luz do artigo 37 da Constituição Federal de 1988.
- Ética e função pública; ética no setor público.
- Ética no serviço público, comportamento profissional, atitudes, organização e prioridades no trabalho.
- Ética, moral, princípios, valores, democracia, cidadania e função pública.
- Responsabilidade do agente público: sanções éticas e disciplinares.
- Ética no exercício da função pública.
- Ética na administração pública.
- Princípios da Administração Pública aplicados à ética.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo etica.etica-moral-funcao-publica.banco-N.json, onde N é o lote)
```json
{
  "materia": "etica.etica-moral-funcao-publica",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
