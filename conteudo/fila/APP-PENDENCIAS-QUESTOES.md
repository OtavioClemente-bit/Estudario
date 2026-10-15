# Pendências do app para o banco de questões (anotado em 10/10/2026, revisto em 14/10/2026)

1. [x] **A–D derivado de A–E**: feito. `library.ts` (fitsStyle/deliverQuestion) aceita A–E com
   `comentario` em todas as alternativas, tira uma errada (escolha fixa pela semente), refaz as letras e
   monta a explicação com os comentários restantes. Vale para as 14.510 questões A–E dos recortes.
   Limite: as questões próprias das matérias (sem `comentario`) não entram em A–D; esgotado o recorte,
   o pedido A–D cai na IA.
2. [x] **Filtro por banca**: as questões do recorte da banca entram antes das da matéria; quando a banca não tem
   recorte próprio e vem o "Padrão das provas", as questões são ordenadas pelo `estilo` mais parecido com a banca
   (`preferredStyles`, ex.: Vunesp → VUNESP, FCC). Feito em 15/10.
3. [x] **Tópico por questão**: feito em 15/10. `assembleFromLibrary` recebe o `topicPath` do pedido e, entre as
   questões do recorte da banca, põe primeiro as cujo `topico` cobre ao menos metade das palavras do tópico
   pedido (`topicWords`/`topicMatches`), depois as demais da banca e as da matéria. Precisa de deploy do ai-text-worker.
4. [x] **Nota estilo Cebraspe**: feito (commit 218c8919, nota líquida no simulado).
5. [x] **Assertivas I–IV em linhas separadas**: feito (commit 218c8919, servidor quebra em parágrafos).
6. [x] **Normalizar nomes de banca**: feito em 15/10 (19 editais sem banca preenchidos; FAFIPA e DECEx/Exército
   unificados; os demais já batiam com `normalizeBoard`).
