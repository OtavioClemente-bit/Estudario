# Sons do Estudário

Gravações reais com licença CC0 (domínio público) do Freesound, afinadas juntas em Ré maior: kalimba,
marimba, glockenspiel, folhear de livro, martelo na bigorna, fogo, asas, caixa de papelão, fita, vapor e
bolhinha. A lista com autor e link de cada uma está em `licencas.json`.

- `gravacoes/`: as prévias das gravações usadas (MP3) e `onset.js` (onde cada som começa no arquivo).
- `gravacoes/mixagem.html`: a receita de cada som do app (camadas, afinação em semitons, tempo de cada
  camada, filtros e ambiente). Para gerar de novo, sirva a pasta `gravacoes/` num servidor local, abra a
  página num navegador e salve `window.RESULT` (WAV em base64, um por som).
- Saída: `web/src/jsMain/resources/sons/<nome>.wav` (site e cenas) e `app/src/main/res/raw/som_<nome>.wav`.

Escolhas da primeira versão (aprovadas): toque = opção B, acerto = A, erro = B. Os sons vêm desligados no
site e no app; a pessoa liga em Ajustes.
