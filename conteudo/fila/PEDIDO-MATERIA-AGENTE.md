# Escrever uma matéria nova (pedido para agente)

Você é professor de cursinho e autor de questões de concurso. Leia antes `AGENTS.md` e
`conteudo/fila/LEIA-ME.md` (regras do formato). Sua matéria está em `conteudo/fila/11-cobertura.md`,
pelo id que recebeu: ali estão o título, o escopo e o que NÃO entra (já existe em outra matéria).

## Modelo de qualidade
Abra e siga o padrão de `conteudo/materias/informatica/java-fundamentos.json` (formato JSON, campos,
tom) e de uma matéria jurídica parecida da mesma pasta. O aluno pode estar começando do zero: explique
o porquê, dê exemplo concreto em cada conceito, avise as pegadinhas.

## O que escrever (tudo à mão, no texto, sem script, sem laço, sem gerar por código)
1. **Teoria**: 5 capítulos, do básico ao avançado, cobrindo o escopo inteiro. Cada capítulo com
   explicação, exemplo resolvido e "pegadinhas". Último capítulo: "Como o tema costuma ser cobrado".
2. **Resumo, flashcards (15+), dicas, recordação ativa** — no mesmo formato do modelo.
3. **45 questões**: 25 de múltipla escolha (A–E) e 20 de Certo/Errado; 15 fáceis, 15 médias,
   15 difíceis. Gabarito espalhado (nenhuma letra acima de 40%); a certa NÃO pode ser a mais longa em
   mais de 40% das questões (alongue distratores à mão). Uma única alternativa correta; distratores
   errados de verdade, mas plausíveis (erro típico do aluno). Explicação de 120+ caracteres que diga
   por que a certa está certa e por que cada errada erra; em C/E começa com "Certo." ou "Errado.".
   Enunciado de múltipla escolha termina em pergunta ou comando; item C/E é uma afirmação.
4. **Neutra**: sem banca, órgão, tribunal regional, PM/bombeiros como contexto; board/agency null;
   `status` "PUBLISHED", `version` 1, questões autorais com campos de fonte null.
5. **Lei**: só cite número de lei, artigo, súmula, prazo ou tema se tiver certeza absoluta e for
   regra vigente. Na dúvida, explique o conteúdo sem o número. Nunca invente.
6. **Programação**: todo código precisa estar correto e rodar; saída de exemplo exata.
7. **Apelidos (aliases)**: procure em `conteudo/editais/*.json` tópicos com `"topico": null` que
   sejam exatamente do seu escopo e copie até 20 textos exatos (10+ caracteres, específicos, nada
   genérico como "Recursos." ou "Prazos."). Não use texto que já seja apelido de outra matéria.

## Gravar e conferir
- Grave com Write, arquivo inteiro de uma vez, em `conteudo/entrada/<id>.json` (UTF-8 sem BOM).
- Rode `deno run --allow-read conteudo/fila/conferir.ts conteudo/entrada/<id>.json` e corrija à mão
  (com Edit) tudo o que ele apontar, sem descartar questões, até dar "validador: OK".
- Antes de terminar, releia TODAS as questões como se fosse o aluno: refaça contas, confira que só
  uma alternativa está certa e que a explicação bate com o gabarito.

Não escreva em outra pasta. Não faça commit. No fim responda só: id, nº de questões, gabarito por
letra, e qualquer ponto de lei que você evitou citar por dúvida.
