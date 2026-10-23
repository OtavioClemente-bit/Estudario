# Sons do Estudário

A família de sons é **papel e piano** (o Folha é um livro), toda em Ré maior, na escola do Duolingo:
no dia a dia, sons curtos e discretos (acerto = duas notas de piano subindo com o tique do lápis; erro =
duas notas graves e abafadas, mais baixas); o festivo é mais cheio. As comemorações juntam os sons físicos
da cena (martelo na bigorna, fogo, asas, caixa) com o "som do Folha" no fim: páginas acelerando, a palma
de um livro fechando ("achou!"), o carimbo, um baque grave e o acorde de piano. Quando o Folha fica tonto,
tocam passarinhos de desenho animado.

Todas as gravações são reais, com licença CC0 (domínio público) no Freesound; autor e número de cada uma em
`licencas.json`.

- `gravacoes/`: as prévias das gravações (MP3), `familia.js` (a receita de cada som: camadas, notas,
  tempos, volumes) e `exportar.html` (gera os WAV). Para gerar de novo: sirva a pasta `gravacoes/` num
  servidor local, abra `exportar.html` num navegador e salve cada item de `window.RESULT` (WAV em base64).
  `?s=acerto,erro` gera só alguns.
- Saída: `web/src/jsMain/resources/sons/<nome>.wav` (site e cenas) e `app/src/main/res/raw/som_<nome>.wav`.
- Os sons vêm desligados no site e no app; a pessoa liga em Ajustes. No celular não tocam no silencioso.
