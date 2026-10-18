# Simulados por edital

Questões **inéditas** escritas no formato da prova real de cada edital (mesma banca, mesmo tipo
de item, mesma distribuição de disciplinas). Servem para:

- **Simulado completo:** todas as questões do arquivo, na ordem, com o tempo da prova.
- **Simulado parcial:** filtre por `disciplina` (ex.: só "Língua Portuguesa").
- **Banco por matéria:** o campo `materia` liga a questão ao id da matéria do Estudário
  (`area.subarea`), então ela pode entrar em outros simulados que cobram o mesmo assunto.

`LISTA-100.md` traz os 100 editais escolhidos, a estrutura da prova e o status de cada simulado.

## Arquivo
`simulados/<arquivo-do-edital>.simulado-1.json` (o nome do edital é o de `editais/ja-temos.txt`).

```json
{
  "edital": "pf-2025--agente",
  "banca": "Cebraspe",
  "estrutura": "120 itens Certo/Errado; um item errado anula um certo",
  "questions": [
    {
      "n": 1,
      "disciplina": "Língua Portuguesa",
      "materia": "portugues.interpretacao-texto",
      "topico": "Compreensão e interpretação de textos",
      "format": "CERTO_ERRADO",          // ou MULTIPLE_CHOICE
      "difficulty": "MEDIA",              // FACIL | MEDIA | DIFICIL
      "textoBase": "opcional: texto que serve a várias questões",
      "statement": "...",
      "options": [ { "key": "C", "text": "Certo", "correct": true }, { "key": "E", "text": "Errado", "correct": false } ],
      "explanation": "...",
      "pegadinha": "..."
    }
  ]
}
```
- Cebraspe: `CERTO_ERRADO` (opções C/E). FGV, FCC, Vunesp, Cesgranrio: 5 alternativas A–E.
  IBFC, AOCP, Idecan e institutos: A–E (ou A–D quando a prova usa 4).
- Em `MULTIPLE_CHOICE` cada alternativa leva `comentario`.

## Prova-base
Cada simulado é escrito sobre o caderno de uma prova anterior do mesmo cargo (`provas-base/<prova>.md`):
dali saem a ordem das disciplinas, a faixa de itens de cada uma e os assuntos cobrados. As questões são
novas — nenhum item é copiado. Simulados ainda sem prova-base estão marcados na `LISTA-100.md`.
