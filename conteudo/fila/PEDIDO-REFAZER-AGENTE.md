# Refazer um edital (pedido para agente)

Você vai montar arquivos de EDITAL (catálogo de concursos). Leia antes `AGENTS.md` e
`conteudo/fila/PEDIDO-EDITAIS.md` (regras e formato JSON). Não é para gerar matéria nem questões.

Seu concurso está em `conteudo/fila/09-refazer-editais.md`, pelo número que recebeu (ex.: **061**):
ali estão o texto oficial (linha "texto") e os arquivos a gravar (linhas "arquivo").

Uma versão anterior desses arquivos foi RECUSADA por resumir o conteúdo programático. Ela está em
`conteudo/rejeitados/editais-resumidos/<mesmo nome>` (ou `editais-sem-fonte/`): aproveite SÓ os
metadados (concurso, orgao, cargo, cargoExibicao, banca, ano, referencia, url, sinonimos), conferindo
com o texto oficial; ignore os tópicos dela. Se não existir, monte os metadados pelo texto oficial.

Para cada arquivo:
1. Ache no texto oficial o conteúdo programático que vale para aquele cargo (conhecimentos gerais
   comuns + específicos). Leia as cláusulas de aplicabilidade. O texto inteiro está no arquivo indicado.
2. Copie TODOS os itens numerados: um tópico por item, com as palavras do edital, sem numeração,
   começando com maiúscula e terminando com ponto. Só separe um item quando ele juntar matérias
   diferentes (ex.: "Pontuação; concordância; regência" vira 3 tópicos). Nunca junte itens nem
   acrescente assunto que não está no texto. Todo tópico com `"topico": null`.
3. Disciplinas com o nome do edital. Nível superior costuma ter 100 a 300+ tópicos; se ficar com
   menos de 6 tópicos por disciplina, você resumiu: releia.
4. Grave com a ferramenta Write (arquivo inteiro de uma vez, UTF-8 sem BOM) em `conteudo/entrada/`
   e confira: `deno run --allow-read conteudo/fila/conferir-edital.ts conteudo/entrada/<arquivo>`.
   Corrija o que ele apontar.
5. Se o texto oficial não tiver o conteúdo programático de um cargo (ex.: "será divulgado"), não
   invente: não grave esse arquivo e explique no relatório.

Não escreva em nenhuma outra pasta. Não faça commit. No fim responda só com a lista: arquivo,
nº de disciplinas, nº de tópicos (e os pulados, com motivo).
