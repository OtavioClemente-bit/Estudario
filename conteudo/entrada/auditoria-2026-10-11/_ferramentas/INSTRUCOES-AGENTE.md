# Correção manual dos arquivos da auditoria

Você corrige, À MÃO, os arquivos listados no seu grupo (`_ferramentas/grupos/grupo-NN.txt`), todos dentro de
`/home/user/Estudario/conteudo/entrada/auditoria-2026-10-11/`.

## Regras do projeto (AGENTS.md — obrigatórias)
- Escreva cada texto você mesmo. Edite com a ferramenta Edit, uma questão/trecho por vez.
- PROIBIDO usar script, código ou laço (Python, Node, sed, jq…) para gerar, alterar, alongar, encurtar, embaralhar
  ou "equilibrar" questões, alternativas, explicações ou textos. O terminal serve só para conferir.
- Não mexa em nenhum arquivo fora do seu grupo. Não faça commit nem push.
- Se não tiver certeza de número de lei, artigo, prazo ou súmula, não cite o número. Nunca invente norma.
- Os arquivos são JSON: dentro de strings use aspas tipográficas (“ ”) ou \" escapado. Nunca quebre o JSON.

## Conferir
```
cd /home/user/Estudario && node --experimental-transform-types --no-warnings conteudo/entrada/auditoria-2026-10-11/_ferramentas/conferir.mts conteudo/entrada/auditoria-2026-10-11/<pasta>/<arquivo>.json
```
Rode antes de começar cada arquivo (para ver o que corrigir) e depois (deve sair "ok" ou só a linha de viés dentro do limite).
Atenção: nas mensagens do conferidor oficial `questions[29]` é base 0 (= questão nº 30). Na linha "viés" os números já são nº (base 1).

## O que corrigir
1. **Viés de tamanho** ("certa mais longa em X/Y [lista]" ou "mais curta", com X acima do limite): escolha questões da lista e
   reescreva as alternativas até X ficar NO MÁXIMO `limite − 2`. Formas aceitas:
   - certa mais longa → alongue 1 ou 2 distratores com conteúdo técnico real e plausível (um detalhe, condição ou consequência
     que um candidato mal preparado aceitaria) e/ou enxugue a certa sem perder precisão;
   - certa mais curta → desenvolva a certa (a condição, o fundamento) e/ou enxugue distratores.
   O gabarito NÃO muda; a certa continua inequivocamente certa e cada distrator continua errado pelo mesmo motivo de antes
   (se o distrator mudar, ajuste o "comentario" dele nos bancos e a explicação, se ela citar o texto). Não crie a pista oposta
   (a certa não pode virar a mais curta/longa), não use rabicho repetido (", nessa ordem", ", conforme o caso"…), nem
   "todas/nenhuma das anteriores", nem absolutos só para errar.
2. **C/E com "alternativa/opção"** na explicação: reescreva só a frase, sem as palavras alternativa(s), opção/opções
   (use escolha, possibilidade, caminho, hipótese…), mantendo o sentido.
3. **Rabicho** ("N alternativas terminam em …"): tire o final repetido e iguale o tamanho com conteúdo próprio.
4. **Trecho colado / rascunho / alternativas repetidas / explicação < 120**: reescreva o item apontado direito.
5. **Banco com "só 30 questões (pedido 40)"**: acrescente 10 questões novas no fim, no mesmo formato, estilo e campos das
   existentes (topico, estilo, statement, format, difficulty, options com comentario, explanation, pegadinha), casos novos,
   gabarito variado entre as letras, explicação ≥ 120 caracteres.
6. **Recorte que "afirma prova real"** ("a banca já explorou/cobrou…", "há prova da banca…"): reescreva a frase dizendo como
   a banca COSTUMA cobrar, sem afirmar que houve prova/questão real. Mantenha howItFalls entre 1.000 e 2.000 caracteres.

## Ao terminar
Rode o conferidor em TODOS os arquivos do grupo de uma vez e responda em até 15 linhas: quantos arquivos ficaram ok,
quantas questões/trechos você reescreveu, e o que (se algo) não conseguiu resolver e por quê.
