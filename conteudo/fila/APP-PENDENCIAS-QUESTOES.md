# Pendências do app para o banco de questões (anotado em 10/10/2026)

1. **A–D derivado de A–E**: quando o aluno pede A a D, tirar uma alternativa errada (a mais fraca) e
   reorganizar as letras. Questões do banco trazem `comentario` por alternativa e `explanation` sem
   letras: montar a explicação com os comentários que sobraram. As 45 questões antigas de cada matéria
   citam letras na explicação: precisam de ajuste próprio (remapear letras ou só usar as do banco).
   Hoje `supabase/functions/_shared/library.ts` (fitsStyle) só serve A–D se a questão tiver 4 opções,
   então A–D sempre cai na IA paga.
2. **Filtro por banca**: questões do banco têm `estilo` (CEBRASPE, FGV, FCC, CESGRANRIO). Quando o
   aluno estuda um edital, priorizar o estilo da banca dele (campo `banca` do edital) e completar com os
   outros se faltar.
3. **Tópico por questão**: questões do banco têm `topico` (texto do edital); usar em "gerar questões"
   por tópico e nos recortes.
4. **Nota estilo Cebraspe** nos simulados/listas Certo/Errado: certo +1, errado −1, branco 0; botão
   "deixar em branco"; no fim mostrar quanto o aluno perdeu chutando.
5. **Tipos de comando**: o banco traz "assinale a INCORRETA" e assertivas I–IV ("Apenas I e II.").
   Conferir se a tela de questão exibe bem os itens I, II, III, IV em linhas separadas.
6. **Normalizar nomes de banca** nos editais (Cebraspe / CESPE/UnB / Cespe; FGV / Fundação Getulio
   Vargas; FCC / Fundação Carlos Chagas; Cesgranrio / Fundação Cesgranrio; IBFC) — 18 editais sem banca.
