# Regras para gerar matérias (vale para qualquer agente nesta pasta)

1. **Escreva cada questão, alternativa e explicação você mesmo, no texto da resposta.** É proibido usar
   script, código, modelo ou laço para gerar, completar, embaralhar, reordenar, alongar ou "equilibrar"
   questões e alternativas. O terminal serve só para validar se o JSON abre.
2. **Salve os arquivos em `conteudo/entrada/`** (a matéria e o recorte). Nunca grave em
   `conteudo/materias/`, `conteudo/recortes/` nem `conteudo/editais/`: essas pastas são conferidas e
   publicadas por outro processo.
3. Grave o arquivo inteiro de uma vez, só quando ele estiver pronto (nada de arquivo pela metade).
4. Siga o pedido do lote (`conteudo/pedidos/trt3n-lote-NNN.txt` ou outro indicado): mesmos ids, títulos,
   apelidos e escopo. Não junte matérias e não mude o escopo.
5. Se não tiver certeza de um número de lei, artigo, prazo ou súmula, não cite o número: explique o
   conteúdo. Nunca invente norma.
6. Não faça commit, push nem altere nada fora de `conteudo/entrada/`.
