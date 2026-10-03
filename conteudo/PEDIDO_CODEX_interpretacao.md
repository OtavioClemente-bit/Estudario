# Pedido para o Codex: refazer as questões de interpretação textual

Arquivo: `conteudo/materias/portugues/interpretacao-textual.json` (só ele; `generos-textuais.json`
passou no conferidor e não deve ser mexido).

Antes de começar, leia `conteudo/COMO_GERAR.md` inteiro: as regras novas estão em "Nível de prova".

## O que está errado

O conferidor (`deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts`)
recusa o arquivo com 14 problemas:

1. **Rabichos de molde no fim das alternativas.** Mais de 100 alternativas terminam com a mesma
   expressão colada só para igualar o tamanho: ", no contexto do relato", ", segundo a passagem
   apresentada", ", diante das condições informadas", ", considerando a ressalva textual", ", no
   recorte temporal citado". Isso vira pista: a certa costuma ser a única sem rabicho.
   → Tire todos. Iguale o tamanho das alternativas com conteúdo real (um detalhe do texto, uma
   condição, um quantificador), nunca com frase genérica.
2. **"qualquer" inserido por troca automática**, quebrando a frase. Exemplos: "medição definitiva
   de qualquer o desmatamento" (texto-base 8); "não permite concluir que qualquer problema
   educacional foi resolvido" (o sentido certo é "todos os problemas"). O conferidor aponta as
   questões 35 a 39, 55 e 57, mas releia todos os textos-base e explicações procurando trocas
   parecidas.
3. **Pergunta sempre igual.** Cada texto-base abre com "Qual síntese preserva a ideia central e os
   limites do texto?". Varie: ideia central, inferência, sentido de palavra ou expressão no
   contexto, referência de pronome, reescrita que mantém o sentido, intenção do autor, efeito de
   sentido de um conectivo ou da pontuação. Siga o estilo IDECAN.
4. **Distrator absurdo.** Ex.: questão do alfinete (Machado), alternativa E, "Clarinha conserva o
   alfinete ao entregar a rosa ao pretendente": não existe pretendente no texto. Distrator bom é o
   que um candidato preparado quase marca (extrapola um pouco, troca "alguns" por "todos", inverte
   causa e efeito, usa palavra do texto com sentido errado). Revise todas as alternativas erradas
   com esse critério.

## O que manter

- Os textos-base com fonte real (Machado de Assis em domínio público, Agência Brasil etc.) e os
  links.
- Gabaritos corretos e explicações próprias de cada questão.
- A distribuição equilibrada das letras do gabarito e o tamanho parecido entre alternativas
  (a certa não pode ser a mais longa nem a mais curta em mais de 40% das questões).

## Como entregar

- Rode o conferidor e só termine quando ele disser "Tudo certo." sem nenhum ✗.
- Mantenha o status `REVIEWED`.
- Não faça commit; avise que terminou para ser revisado.
