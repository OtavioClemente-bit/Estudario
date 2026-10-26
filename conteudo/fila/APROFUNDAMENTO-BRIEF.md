# Aprofundar matérias de tópico grande (lista: aprofundamento-lista.txt)

Problema: estas matérias atendem tópicos de edital amplos (vários assuntos num tópico só) e foram espremidas em ~30 mil
caracteres. Alguns subassuntos ficaram rasos ou sem nome. Objetivo: o aluno que estudar a matéria aprende TODO o tópico e vai
bem na prova. Leia AGENTS.md (tudo à mão, nunca invente número de lei/artigo/prazo, não faça commit).

Para CADA matéria sua:
1. Veja os tópicos de edital ligados a ela:
   `node scripts/biblioteca/topicos-de-materia.js <id> 60`
   Monte a lista de TODOS os subassuntos que esses tópicos pedem (use os nomes do edital: ex. "nexos", "operadores
   sequenciais", "expressões referenciais").
2. Leia a matéria inteira e marque, para cada subassunto: bem explicado / raso / ausente / presente com outro nome.
3. Se tudo estiver bem explicado, não mexa (diga no relatório). Se não:
   - Reorganize em até 6 capítulos, teoria total de 30.000 a 50.000 caracteres (não passe de 50 mil).
   - Use os termos do edital nos títulos e no texto; abra o capítulo 1 com um "**Mapa do edital**" (tabela: termo do edital →
     o que é → capítulo).
   - Padrão de aula: problema que o assunto resolve, toda sigla explicada, exemplo passo a passo, tabelas comparativas,
     **Pegadinha:** e "**Resumo para a prova**" ao fim de cada capítulo.
   - Pode renomear/criar capítulos, MAS então atualize o "section" de TODAS as questões para o título exato do capítulo onde
     o conteúdo delas ficou (nenhuma questão pode ficar apontando para título inexistente).
   - Acrescente 10 a 20 questões novas sobre os subassuntos que estavam rasos/ausentes (formato e regras do arquivo; distratores
     plausíveis; explicação 150–400 caracteres sem citar letra; certa não pode ser a mais longa em mais de 40%).
   - Ajuste flashcards/resumo/dicas para cobrir os subassuntos novos; versão +1.
   - Edite o JSON com um script que só COPIA o texto que você escreveu (c1.md... para chapters[i].markdown) ou com Edit.
     Nunca PowerShell Set-Content.
4. Valide: `deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/materias/<dir>/<arq>.json` → OK.

Relatório por matéria: subassuntos que estavam rasos/ausentes, o que fez, tamanho antes→depois, nº de questões novas.
