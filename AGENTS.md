# Regras para gerar matérias (vale para qualquer agente nesta pasta)

1. **Escreva cada questão, alternativa e explicação você mesmo, no texto da resposta.** É proibido usar
   script, código, modelo ou laço para gerar, completar, embaralhar, reordenar, alongar ou "equilibrar"
   questões e alternativas. O terminal serve só para validar se o JSON abre.
   **Exceção autorizada pelo dono do projeto:** `scripts/banco2/gerar-lote.mjs` pode pedir a um modelo,
   pela API da Anthropic, que escreva um lote inteiro do banco 2 (cada lote é escrito pelo modelo numa
   única resposta, seguindo `conteudo/fila/banco-questoes-2/`). Continua proibido montar, completar,
   embaralhar ou "equilibrar" questões por programa; o script só envia o pedido, grava a resposta e confere.
   **Segunda exceção autorizada:** `scripts/biblioteca/simulados-explicar.mjs` pode pedir a um modelo que
   reescreva a EXPLICAÇÃO (só ela) das questões de `conteudo/simulados/`, mantendo enunciado, alternativas e
   gabarito; se o modelo achar o gabarito errado, ele só aponta, para revisão humana.
   **Terceira exceção autorizada (28/10/2026):** `scripts/agy/gerar.ts` pode pedir pelo Antigravity CLI
   (`agy -p`) que um modelo escreva lotes inteiros de questões para recortes existentes e que outro modelo
   audite cada lote. O script só envia o pedido, grava a resposta, confere, descarta os itens apontados e
   junta os aprovados ao recorte; nunca escreve, completa, embaralha nem "equilibra" questões por conta própria.
2. **Salve os arquivos em `conteudo/entrada/`** (a matéria e o recorte). Nunca grave em
   `conteudo/materias/`, `conteudo/recortes/` nem `conteudo/editais/`: essas pastas são conferidas e
   publicadas por outro processo.
3. Grave o arquivo inteiro de uma vez, só quando ele estiver pronto (nada de arquivo pela metade).
4. Siga o pedido do lote (`conteudo/pedidos/trt3n-lote-NNN.txt` ou outro indicado): mesmos ids, títulos,
   apelidos e escopo. Não junte matérias e não mude o escopo.
5. Se não tiver certeza de um número de lei, artigo, prazo ou súmula, não cite o número: explique o
   conteúdo. Nunca invente norma.
6. Não faça commit, push nem altere nada fora de `conteudo/entrada/`.
