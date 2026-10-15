# Pendências do app para o banco de questões (anotado em 10/10/2026, revisto em 14/10/2026)

1. [x] **A–D derivado de A–E**: feito. `library.ts` (fitsStyle/deliverQuestion) aceita A–E com
   `comentario` em todas as alternativas, tira uma errada (escolha fixa pela semente), refaz as letras e
   monta a explicação com os comentários restantes. Vale para as 14.510 questões A–E dos recortes.
   Limite: as questões próprias das matérias (sem `comentario`) não entram em A–D; esgotado o recorte,
   o pedido A–D cai na IA.
2. [~] **Filtro por banca**: em parte — as questões do recorte da banca entram antes das da matéria.
   Falta, se valer a pena, priorizar o `estilo` quando o recorte da banca do aluno não existir.
3. [ ] **Tópico por questão**: questões do banco têm `topico` (texto do edital); usar em "gerar questões"
   por tópico e nos recortes.
4. [x] **Nota estilo Cebraspe**: feito (commit 218c8919, nota líquida no simulado).
5. [x] **Assertivas I–IV em linhas separadas**: feito (commit 218c8919, servidor quebra em parágrafos).
6. [ ] **Normalizar nomes de banca** nos editais (Cebraspe / CESPE/UnB / Cespe; FGV / Fundação Getulio
   Vargas; FCC / Fundação Carlos Chagas; Cesgranrio / Fundação Cesgranrio; IBFC) — 18 editais sem banca.
