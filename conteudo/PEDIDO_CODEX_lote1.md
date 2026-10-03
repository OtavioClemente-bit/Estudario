# Pedido para o Codex: lote 1 de Português (CBMMG 2027, IDECAN)

Leia `conteudo/COMO_GERAR.md` inteiro antes de começar. As matérias
`portugues/interpretacao-textual.json` e `portugues/generos-textuais.json` são o **modelo de
qualidade** aprovado: siga o padrão delas.

> **Uma matéria por vez.** Se o pedido disser qual (ex.: "só crase"), faça só ela, com o recorte e
> o vínculo no edital, rode o conferidor e entregue. As outras ficam para os próximos pedidos.

## O que criar

Quatro matérias novas, cada uma com o recorte IDECAN e o tópico ligado no edital:

| Matéria (`materias/portugues/`) | Tópico do edital (`editais/cbmmg-cfsd-bm-2027.json`) |
|---|---|
| `concordancia.json` | "concordância verbal e nominal;" |
| `crase.json` | "emprego do sinal indicativo de crase;" |
| `pontuacao.json` | "emprego dos sinais de pontuação;" |
| `coesao-textual.json` | "Domínio dos mecanismos de coesão textual: …" |

- Recortes em `recortes/idecan/portugues.<topico>.json`.
- No edital, troque `"topico": null` pelo id da matéria nesses 4 tópicos. Não mexa nos outros.
- Não altere as matérias que já existem.

## Lições das rodadas anteriores (o conferidor já recusa tudo isto)

1. **Formato IDECAN:** a banca cobra cinco alternativas (A–E). Em cada matéria, **pelo menos 2/3
   das questões em A–E**; Certo/Errado só como complemento (mínimo exigido pelo manual).
2. **Nada de rabicho de molde** no fim das alternativas para igualar tamanho. Iguale com
   conteúdo.
3. **A certa não pode ser a mais longa** (nem a mais curta) em mais de 40% das questões.
4. **Varie o que se pergunta**: identificar erro, corrigir frase, justificar regra, reescrita que
   mantém o sentido e a correção, efeito de sentido da pontuação, referente de um termo coesivo.
5. **Explicação própria**: diga a regra aplicada àquela frase e por que cada distrator falha. Nunca
   copie o item na explicação nem coloque entre aspas algo que não está no enunciado.
6. **Confira cada gabarito** aplicando a regra à frase, não confiando no texto que você gerou.
   Atenção às exceções que a IDECAN adora: crase facultativa (possessivos, nomes de mulher,
   "até"), crase proibida (antes de verbo, masculino, "a" singular + plural); concordância com
   sujeito posposto, coletivo, "haver/fazer" impessoais, "a maioria de", "um dos que";
   vírgula entre sujeito e verbo, oração adjetiva explicativa × restritiva.
7. **Revise o português** depois de qualquer troca automática de palavras.
8. **Frases-exemplo próprias** ou de fonte real em domínio público (com link). Coesão textual
   precisa de textos-base (de 8 a 25 linhas), como em interpretação.

## Como entregar

- Rode `deno run --allow-read --allow-env --allow-net scripts/biblioteca/importar.ts` e só termine
  com "Tudo certo." e nenhum ✗.
- Status `REVIEWED`. Não faça commit.
- No fim, diga para cada matéria: número de questões por formato, distribuição das letras e em
  quantas a certa é a mais longa.
