# Prompt para continuar a reescrita didática (outro Claude)

Cole o bloco abaixo numa sessão nova do Claude Code com este repositório.

```
Continue a REESCRITA DIDÁTICA das matérias do Estudário, no mesmo padrão das que já foram feitas (veja os commits
"reescrita didática completa: ..." e, como modelos, conteudo/materias/processo-civil/acao-popular-injuncao-habeas-data.json
e conteudo/materias/direito-penal/crimes-contra-pessoa.json, se existir).

FILA: conteudo/fila/reescrita-didatica-faltam.txt (já em ordem de prioridade; a 2ª coluna é o nº de editais).
Pegue do topo para baixo. Antes de cada uma, confira que o id NÃO está em conteudo/fila/reescrita-didatica-feitas.txt
(outra sessão pode ter feito). Não mexa nas matérias criadas recentemente em conteudo/entrada/materias-pr3,
materias-codex e materias-codex2: elas já nasceram no padrão novo.

REGRAS (AGENTS.md vale inteiro):
- Escreva cada capítulo À MÃO. Nenhum script gera, completa ou reescreve texto; script só copia o markdown dos capítulos
  para o JSON e sobe a versão.
- Mantenha os TÍTULOS dos capítulos exatamente iguais (as questões apontam para eles em "section"). Não mexa nas questões.
- Nunca invente número de lei, artigo, prazo, valor ou súmula: use só os que já estão no texto ou que você conferiu na fonte
  oficial. Na dúvida, explique a ideia sem o número.

PADRÃO DE CADA CAPÍTULO:
- Começa explicando o problema que o assunto resolve, em linguagem simples.
- Subtítulos "###" (o título do capítulo é o H2). Toda sigla e todo termo técnico explicado na primeira vez.
- Exemplo concreto passo a passo (com personagem: "Ana tomou posse e..."), tabela comparativa quando houver institutos
  parecidos, **Pegadinha:** com o erro que a banca explora, e "**Resumo para a prova**" no fim.
- Teoria total da matéria entre 15.000 e 30.000 caracteres.

FLUXO POR MATÉRIA:
1. Escreva c1.md ... cN.md numa pasta temporária.
2. Copie para o JSON (só o campo markdown de cada capítulo; versão +1).
3. Valide: deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts conteudo/materias/<dir>/<arq>.json → OK.
4. Acrescente o id em conteudo/fila/reescrita-didatica-feitas.txt.
5. Commit: "reescrita didática completa: <título> com texto inteiro reescrito" e push na main (git pull antes).
Publicação no app eu faço depois (não rode publicar.sh se não tiver a chave).

Faça quantas couber, uma por vez, sem pular a validação. No fim, diga quais fez e qual é a próxima da fila.
```
