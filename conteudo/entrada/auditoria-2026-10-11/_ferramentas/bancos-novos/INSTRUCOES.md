# Escrever um lote de banco de questões

Você escreve, À MÃO, UM lote de questões, seguindo o pedido original em `pedido-<materia>.md` (leia inteiro:
lista de tópicos, regras "Em todos os lotes", formato JSON e a descrição do SEU lote).

## Regras do projeto (AGENTS.md — obrigatórias)
- Escreva cada questão, alternativa, comentário e explicação você mesmo, uma a uma. PROIBIDO usar script, código,
  laço ou modelo para gerar, completar, embaralhar, reordenar, alongar ou equilibrar questões. O terminal só confere.
- Grave o arquivo inteiro de uma vez (Write), só quando estiver pronto. Depois, ajustes pontuais com Edit.
- Se não tiver certeza de número de lei, artigo, prazo ou súmula, não cite o número; explique o conteúdo. Nunca invente.
- Sem commit; não mexa em nada além do seu arquivo.

## Antes de escrever
- Leia os outros lotes já existentes da mesma matéria em
  `/home/user/Estudario/conteudo/entrada/auditoria-2026-10-11/bancos/<materia>.banco-*.json` para copiar o formato
  exato e NÃO repetir casos/enunciados. Leia também a matéria em `.../materias/<materia>.json`.

## Qualidade (o conferidor e a revisão recusam)
- Exatamente a quantidade e o estilo do seu lote (CEBRASPE = Certo/Errado; FGV, FCC, CESGRANRIO = A–E).
- Exatamente uma correta; gabarito de múltipla escolha distribuído (8 de cada letra A–E em 40); C/E equilibrado.
- Dificuldade variada (cerca de um terço de cada). Casos concretos, nível de prova real.
- Distratores plausíveis (conceito vizinho, exceção esquecida, regra certa no caso errado); nada absurdo, nada de
  "todas/nenhuma das anteriores".
- Tamanho das alternativas parecido: a certa NÃO pode ser a mais longa nem a mais curta em mais de 35% das questões.
- Explicação ≥ 120 caracteres (C/E começa com "Certo." ou "Errado."); "comentario" em cada alternativa; "pegadinha"
  e "topico"/"estilo" como no formato do pedido.

## Gravar em
/home/user/Estudario/conteudo/entrada/auditoria-2026-10-11/bancos/<materia>.banco-<N>.json

## Conferir
cd /home/user/Estudario && node --experimental-transform-types --no-warnings conteudo/entrada/auditoria-2026-10-11/_ferramentas/conferir.mts conteudo/entrada/auditoria-2026-10-11/bancos/<materia>.banco-<N>.json
Deve mostrar o número certo de questões, nenhum erro e viés abaixo do limite.

## Ao terminar
Responda em até 5 linhas: arquivo gravado, nº de questões, resultado do conferidor, dúvidas de conteúdo.
