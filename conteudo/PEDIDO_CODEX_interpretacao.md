# Pedido para o Codex: corrigir os itens Certo/Errado (rodada 2)

Arquivos: `conteudo/materias/portugues/interpretacao-textual.json` e
`conteudo/materias/portugues/generos-textuais.json`. Leia `conteudo/COMO_GERAR.md` antes.

A rodada 1 ficou boa nas questões de múltipla escolha: comandos variados, sem rabichos, distratores
melhores. Mantenha isso. Os problemas agora estão nos itens Certo/Errado e em uma explicação trocada.

## O que está errado

O conferidor (`deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts`)
recusa 44 itens (36 em interpretação, 8 em gêneros) com "a explicação copia o próprio item".

1. **Explicação que só repete o item, com aspas falsas.** Nos 36 itens Certo/Errado de
   interpretação, a explicação copia a frase do item e a põe entre aspas como se fosse um trecho do
   texto-base, depois fecha com frase genérica ("Assim, não converte um dado parcial em conclusão
   universal."). Isso não explica nada e engana o aluno, que acha que aquilo está no texto.
   → Cada explicação deve citar o que **realmente está no texto-base** (trecho literal, entre
   aspas, só se existir) e dizer por que o item está certo ou errado.
2. **Gabarito errado.** Exemplo: no texto do alfinete (Machado), o item 4, "A comparação com
   Bonaparte indica que a mudança foi rápida, mas não prova que a prosperidade durará", está marcado
   como **Errado**, mas a própria explicação confirma que ele está certo. → Confira o gabarito de
   **todos** os itens Certo/Errado relendo o texto-base, não o modelo.
3. **Explicação de outra questão.** Na questão do conectivo "Ao mesmo tempo" (pesquisa ambiental),
   a explicação fala de "faixa etária", "indicador medido" e "conclusão escolar", que são da
   questão da Pnad Educação. → Revise se cada explicação fala da própria questão.
4. **Proporção de formatos.** São 36 Certo/Errado e só 24 de múltipla escolha. A IDECAN cobra
   múltipla escolha com 5 alternativas (A–E). → Inverta a proporção: pelo menos 40 de múltipla
   escolha e no máximo 20 Certo/Errado em interpretação.

## O que manter

- Os 12 textos-base e as fontes.
- O que já está bom nas questões de múltipla escolha.
- Gabarito equilibrado entre as letras e tamanho parecido entre as alternativas.

## Como entregar

- Rode o conferidor e só termine com "Tudo certo." e nenhum ✗.
- Mantenha o status `REVIEWED`. Não faça commit.
