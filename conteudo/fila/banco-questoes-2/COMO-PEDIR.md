# Banco 2 — como pedir ao ChatGPT

**O que é:** 150 matérias × 6 lotes × 50 questões = até **45.000 questões** de múltipla escolha A–E, 30% médias e 70% difíceis, nos modelos FGV, FCC, VUNESP, CESGRANRIO, INSTITUTOS (IBFC, AOCP, IDECAN, FUNDATEC, FUMARC) e DESAFIO (sem banca, o mais difícil).

## 1. Montar o projeto no ChatGPT (uma vez)
1. Crie um projeto novo, por exemplo "Estudário — Banco 2".
2. Suba como arquivos do projeto: `00-REGRAS.md`, `projeto-banco2-parte-1.md`, `projeto-banco2-parte-2.md` e `projeto-banco2-parte-3.md` (as três partes têm as fichas das 150 matérias).
3. Cole nas **instruções do projeto**:

> Você cria bancos de questões de concurso para o Estudário. Siga SEMPRE o arquivo 00-REGRAS.md. Quando eu pedir "BANCO NNN, lote L", procure a ficha "BANCO NNN" nas partes do projeto e entregue só aquele lote: 50 questões de múltipla escolha A–E no estilo do lote, 30% MEDIA e 70% DIFICIL (100% DIFICIL no lote 6), no formato JSON das regras, sem repetir casos de lotes anteriores. Pesquise na web para conferir lei e jurisprudência vigentes. Antes de entregar, resolva cada questão e confirme que só uma alternativa é defensável.

## 2. Pedir os lotes
Abra **uma conversa por matéria** (o histórico ajuda a não repetir casos) e mande, um de cada vez:

    BANCO 004, lote 1

Depois `BANCO 004, lote 2` e assim até o lote 6. A lista com os números está em `_lista.txt`.

- **Por que 50 e não 300:** uma questão difícil com comentário em cada alternativa tem uns 600 tokens. Trezentas passam de 180 mil tokens numa resposta só; o modelo corta no meio ou cai de qualidade lá pela questão 80. Com 50 por resposta, a qualidade se mantém e você chega a 300 por matéria com 6 pedidos.
- **Para testar o GPT 6.1 Sol** com mais por vez: `BANCO 004, lotes 1 e 2 juntos (100 questões)`. Se ele entregar 100 completas e boas, pode seguir assim; se cortar ou repetir, volte para 50.
- **Se a resposta cortar:** mande `continue o lote de onde parou, fechando o JSON`.
- **Se vier Certo/Errado ou alternativa faltando:** mande `refaça o lote só com múltipla escolha A–E, 5 alternativas cada`.

## 3. Mandar para conferência
Salve cada resposta como arquivo em `conteudo/entrada/banco2/`, com o nome `<id-da-materia>.banco2-<lote>.json` (exemplo: `direito-administrativo.ato-administrativo.banco2-1.json`).
Depois me avise. Eu confiro cada lote como fiz no banco 1 (gabarito, alternativa defensável, número inventado, distrator absurdo, tamanho das alternativas, nível real de dificuldade), corrijo à mão o que precisar e publico.

## Ordem sugerida
Comece pelas 20 primeiras da `_lista.txt`, que são as que mais aparecem nos editais; com elas, a maioria dos alunos já sente a diferença.
