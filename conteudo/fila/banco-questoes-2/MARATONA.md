# Banco 2 — modo maratona (gerar a noite toda, lote atrás de lote)

Cole o texto abaixo numa conversa nova do projeto "Estudário — Banco 2" (o mesmo que já tem o
00-REGRAS.md e as três partes com as fichas). Troque só a linha COMEÇAR EM, se precisar.

- **Modo agente / tarefa longa:** ele vai salvando um arquivo por lote e segue sozinho.
- **Conversa comum:** ele entrega um lote por resposta e termina com a linha "PRÓXIMO:". Para seguir,
  mande só `continua` (ou deixe uma mensagem pronta para colar).

De manhã: baixe os arquivos `.json` para `conteudo/entrada/banco2/` e me avise. Eu confiro lote por lote.

---

```
MODO MARATONA — BANCO 2

Você vai gerar o Banco 2 em sequência, sem parar, seguindo à risca o arquivo 00-REGRAS.md e as fichas
"BANCO NNN" das partes do projeto.

COMEÇAR EM: BANCO 001, lote 2   (o lote 1 do BANCO 001 já está pronto)

ORDEM (nunca mude):
1. Uma matéria por vez. Termine os lotes 1 a 6 da matéria atual antes de passar para a próxima.
2. Dentro da matéria, lote 1, 2, 3, 4, 5, 6, nessa ordem. O estilo de cada lote é o da tabela das
   regras (1 FGV, 2 FCC, 3 VUNESP, 4 CESGRANRIO, 5 INSTITUTOS, 6 DESAFIO).
3. Depois do lote 6, passe para o próximo número de BANCO (001, 002, 003… até 150), começando do lote 1.
4. Nunca misture matérias: cada lote traz só questões da ficha daquele BANCO, com "topico" copiado
   de um tópico dessa ficha. Nunca junte dois lotes num arquivo nem divida um lote em dois.

CADA LOTE:
- 50 questões de múltipla escolha A–E, no formato JSON das regras, com "materia" igual ao id da ficha,
  "lote" com o número e "estilo" do lote em cada questão.
- 15 MEDIA e 35 DIFICIL (no lote 6, as 50 DIFICIL).
- Gabarito: exatamente 10 de cada letra (A, B, C, D, E), espalhadas sem padrão.
- Tipos aproximados: 22 a 23 CASO, 7 a 8 INCORRETA, 7 a 8 ASSERTIVAS, 5 ASSOCIACAO, 5 CONCEITO,
  2 a 3 INTERPRETACAO.
- Casos, textos, personagens e números novos: não repita nada dos lotes anteriores da mesma matéria
  (antes de começar, releia os lotes que você já entregou dela). Varie o começo dos enunciados e
  evite repetir rótulos como "fictício" ou "autoral".
- Nas ASSERTIVAS, cada item tem que ser claramente certo ou claramente errado. Se um item puder ser
  defendido dos dois jeitos, reescreva o item.
- Nas INCORRETA, a alternativa falsa não pode se entregar só por uma palavra absoluta
  ("necessariamente", "sempre", "garante") quando a questão é DIFICIL.

CONFERÊNCIA ANTES DE ENTREGAR CADA LOTE (faça de verdade, sem pular):
1. Conte: 50 questões, 5 alternativas cada, uma única "correct": true.
2. Conte: 15/35 de dificuldade (ou 0/50 no lote 6) e 10 de cada letra no gabarito.
3. Resolva cada questão como candidato. Se duas alternativas forem defensáveis, reescreva.
4. Confira que todo número, nome ou dado das alternativas e da explicação está no enunciado. Nada de
   lei, súmula, artigo ou número inventado: na dúvida, cobre o conteúdo sem o número.
5. Confira que a "explanation" não cita letras e termina com a dica de como reconhecer a questão na
   prova, que todas as alternativas têm "comentario" e que toda questão tem "pegadinha".
Se algo falhar, corrija antes de entregar. Não entregue lote com erro de conta.

ENTREGA:
- Um arquivo por lote: <id-da-materia>.banco2-<lote>.json
  (ex.: portugues.interpretacao-textual.banco2-2.json). Se não puder criar arquivo, entregue um único
  bloco JSON completo e válido.
- Se a resposta for cortar, pare num ponto limpo, feche o JSON e escreva "LOTE INCOMPLETO: faltam N";
  na mensagem seguinte, entregue só as questões que faltam do mesmo lote, num JSON separado com a
  mesma "materia" e o mesmo "lote".
- No fim de cada entrega, escreva só estas duas linhas (nada de resumo ou comentário):
  FEITO: BANCO NNN, lote L (<id-da-materia>) — 50 questões conferidas
  PRÓXIMO: BANCO NNN, lote L

CONTINUIDADE:
- Não pare para perguntar nada e não peça confirmação entre lotes. Siga para o PRÓXIMO.
- Se eu mandar só "continua", gere o lote da última linha PRÓXIMO.
- Se você perder o fio, procure a última linha PRÓXIMO da conversa e siga dela.
- Qualidade vem antes de velocidade: é melhor um lote a menos do que um lote com erro.
```
