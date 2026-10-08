# Auditoria de matérias publicadas (pedido para agente)

Você é revisor de conteúdo de concurso. Leia `AGENTS.md` antes. Seu lote está em
`conteudo/fila/10-auditoria.md`, pelo número que recebeu: são algumas matérias de `conteudo/materias/`.

Para CADA matéria do lote:

1. Copie o arquivo para `conteudo/entrada/<id da matéria>.json` (ex.: `cp conteudo/materias/direito-civil/bens.json conteudo/entrada/direito-civil.bens.json`). Nunca edite `conteudo/materias/`.
2. Leia a matéria inteira (teoria, resumo, flashcards, dicas, pegadinhas e TODAS as questões).
3. Em cada questão, confira:
   - a alternativa marcada como correta está mesmo certa (refaça contas; confira a regra jurídica vigente);
   - nenhuma outra alternativa também está correta, nem por sinônimo nem por ser uma afirmação mais ampla e verdadeira;
   - a explicação bate com o gabarito (em Certo/Errado, a primeira palavra "Certo."/"Errado." bate com a opção marcada);
   - o enunciado faz sentido, não se contradiz e é uma pergunta (ou afirmação a julgar, em C/E);
   - nada cita número de lei, artigo, prazo ou súmula de que você não tenha certeza; se tiver dúvida, reescreva explicando o conteúdo sem o número.
4. Na teoria e nos flashcards, corrija só erros de conteúdo (regra errada, número inventado, informação desatualizada). Não reescreva estilo.
5. Corrija À MÃO, com a ferramenta Edit, na cópia em `conteudo/entrada/`. Ao trocar uma alternativa errada, mantenha-a errada e plausível. Proibido usar script para gerar, embaralhar ou reordenar questões e alternativas.
6. Se corrigiu algo, aumente `"version"` em 1 e mantenha `"status": "PUBLISHED"`. Confira: `deno run --allow-read conteudo/fila/conferir.ts conteudo/entrada/<id>.json` e resolva o que ele apontar (sem descartar questões).
7. Se a matéria não tiver nenhum erro, APAGUE a cópia de `conteudo/entrada/` (não deixe cópia igual lá).

Não escreva em outra pasta. Não faça commit. No fim responda só com: para cada matéria, "sem erros" ou a lista curta do que corrigiu (índice da questão + o problema em poucas palavras).
