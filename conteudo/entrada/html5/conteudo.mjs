// Matéria informatica.html5, escrita à mão. montar.mjs só monta o JSON (campos fixos) e grava.
export const C1 = "1. A estrutura de um documento";
export const C2 = "2. Semântica: cada coisa com o seu elemento";
export const C3 = "3. Links, imagens, áudio, vídeo e gráficos";
export const C4 = "4. Formulários e validação nativa";
export const C5 = "5. APIs do navegador e acessibilidade";

export const materia = {
  id: "informatica.html5",
  subject: "Informática",
  title: "HTML5: estrutura, semântica, formulários, multimídia, APIs e acessibilidade",
  aliases: [
    "Front-end: HTML5.",
    "Front-end: HTML5",
    "HTML5: estrutura, semântica, formulários e multimídia",
    "Linguagem de marcação HTML5",
    "HTML5 semântico, formulários e multimídia",
  ],
  scope: {
    covers: "Estrutura do documento (doctype, head, body, metadados), elementos, atributos e elementos vazios; semântica (header, nav, main, article, section, aside, footer, figure, títulos, listas e tabelas); links, caminhos, imagens com alt, imagens responsivas, áudio, vídeo, legendas, canvas e SVG; formulários (form, action, method GET/POST, label, tipos de input, validação nativa, select, textarea, button, fieldset); APIs do navegador (data-*, localStorage, sessionStorage, geolocalização, arrastar e soltar, Web Workers) e acessibilidade (alt, rótulos, títulos, lang, ARIA, WCAG e eMAG).",
    excludes: "CSS em profundidade, JavaScript além do necessário para as APIs, frameworks de front-end, HTTP em profundidade e servidores web.",
  },
  theoryTitle: "HTML5: do documento à página acessível",
};

export const chapters = [
  [C1, `### Primeiro, as palavras do assunto

| Sigla ou termo | Significado | Em palavras simples |
| --- | --- | --- |
| **HTML** | HyperText Markup Language (linguagem de marcação de hipertexto) | O **esqueleto** da página: diz o que é título, parágrafo, link, formulário |
| **HTML5** | A versão atual do HTML | Trouxe elementos semânticos, áudio e vídeo nativos, novos tipos de campo e APIs do navegador |
| **WHATWG** | Web Hypertext Application Technology Working Group | Grupo que mantém o **HTML Living Standard**, a especificação viva, atualizada continuamente |
| **W3C** | World Wide Web Consortium | Consórcio que publicou as recomendações do HTML5 e mantém padrões como CSS e WCAG |
| **Elemento** | Marca de abertura, conteúdo e marca de fechamento | \`<p>Texto</p>\` é um elemento parágrafo |
| **Atributo** | Informação extra dentro da marca de abertura | \`href\`, \`src\`, \`id\`, \`class\`, \`lang\` |

HTML **não é linguagem de programação**: não tem variáveis, laços nem decisões. É uma linguagem de **marcação**, que descreve o significado de cada parte do conteúdo. A aparência fica com o CSS e o comportamento com o JavaScript.

### O esqueleto mínimo

\`\`\`html
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Minha página</title>
    <link rel="stylesheet" href="estilo.css">
  </head>
  <body>
    <h1>Olá</h1>
  </body>
</html>
\`\`\`

| Parte | Para que serve |
| --- | --- |
| \`<!DOCTYPE html>\` | Avisa ao navegador que o documento segue o HTML atual (modo padrão). No HTML5 ficou curto assim |
| \`<html lang="pt-BR">\` | Raiz do documento; o **lang** informa o idioma, usado por leitores de tela e tradutores |
| \`<head>\` | **Metadados**: o que descreve a página, mas não aparece no corpo (título da aba, codificação, folhas de estilo) |
| \`<meta charset="utf-8">\` | Codificação dos caracteres; evita acentos quebrados |
| \`<title>\` | Texto da aba e dos resultados de busca; é obrigatório |
| \`<body>\` | Tudo o que aparece na tela |

### Elementos vazios e comentários

Alguns elementos **não têm conteúdo nem marca de fechamento**: são os elementos vazios (void), como \`<br>\`, \`<hr>\`, \`<img>\`, \`<input>\`, \`<meta>\` e \`<link>\`. Comentários usam \`<!-- assim -->\` e não aparecem na tela.

### Atributos globais

Valem para qualquer elemento: \`id\` (identificador **único** na página), \`class\` (grupo, pode repetir), \`style\` (estilo em linha), \`title\` (dica ao passar o mouse), \`lang\`, \`hidden\` (esconde o elemento), \`tabindex\` (ordem de foco pelo teclado) e os atributos personalizados \`data-*\`.

> **Pegadinha:** o HTML5 é tolerante com maiúsculas e com aspas omitidas em atributos simples, mas isso não muda o significado. O que a prova cobra é a função de cada parte, não um estilo de escrita.`],
  [C2, `### Por que semântica importa

Um elemento **semântico** diz o **significado** do conteúdo, não só como ele aparece. Isso ajuda três públicos: **leitores de tela** (que navegam por regiões e títulos), **mecanismos de busca** (que entendem o que é principal) e **quem mantém o código**. \`div\` e \`span\` são genéricos: não dizem nada sobre o conteúdo.

### Os elementos de estrutura do HTML5

| Elemento | Representa | Exemplo de uso |
| --- | --- | --- |
| \`header\` | Introdução de uma página ou seção | Logotipo, título, busca |
| \`nav\` | Bloco de **navegação principal** | Menu do site |
| \`main\` | O **conteúdo principal** da página; deve haver **um só** visível | O artigo ou a lista de produtos |
| \`article\` | Conteúdo **independente**, que faria sentido sozinho | Notícia, post, comentário |
| \`section\` | Agrupamento **temático**, normalmente com título | Capítulo, aba de conteúdo |
| \`aside\` | Conteúdo **tangencial** ao principal | Barra lateral, "veja também" |
| \`footer\` | Rodapé de uma página ou seção | Direitos, contato |
| \`figure\` + \`figcaption\` | Figura com legenda | Gráfico com a fonte embaixo |

> **Como diferenciar article e section:** pergunte "isso pode ser publicado sozinho em outro lugar?". Se sim, \`article\`. Se é só uma parte temática de algo maior, \`section\`.

### Títulos e texto

Os títulos vão de \`h1\` a \`h6\` e formam uma **hierarquia**: não se pula de \`h2\` para \`h5\` só para ter letra menor (tamanho é com CSS). Para ênfase:

| Elemento | Significado | Aparência padrão |
| --- | --- | --- |
| \`strong\` | **Importância** | Negrito |
| \`b\` | Destaque visual sem importância extra | Negrito |
| \`em\` | **Ênfase** (muda o sentido da frase) | Itálico |
| \`i\` | Termo técnico, estrangeiro ou voz diferente | Itálico |
| \`mark\` | Trecho marcado, como o resultado de uma busca | Fundo amarelo |
| \`time datetime="2026-10-25"\` | Data ou hora legível por máquina | Normal |

### Listas e tabelas

- \`ul\`: lista **sem ordem** (marcadores); \`ol\`: lista **ordenada** (números); \`li\`: item. \`dl\`, \`dt\` e \`dd\`: lista de **termos e definições**.
- Tabela: \`table\` com \`caption\` (título), \`thead\`, \`tbody\` e \`tfoot\`; linhas \`tr\`; células de cabeçalho \`th\` e de dados \`td\`. \`colspan\` faz a célula ocupar **colunas**; \`rowspan\`, **linhas**. O atributo \`scope\` no \`th\` (row ou col) ajuda o leitor de tela.

> **Pegadinha:** tabela é para **dados tabulares**, não para montar o layout da página. Layout é com CSS (Flexbox, Grid).`],
  [C3, `### Links

\`<a href="contato.html">Fale conosco</a>\`

| Atributo | Efeito |
| --- | --- |
| \`href\` | Destino: página, arquivo, âncora (\`#secao\`), e-mail (\`mailto:\`) ou telefone (\`tel:\`) |
| \`target="_blank"\` | Abre em nova aba |
| \`rel="noopener"\` | Impede a página aberta de controlar a original pelo \`window.opener\`; os navegadores atuais já aplicam isso ao \`_blank\` |
| \`download\` | Sugere baixar o arquivo em vez de abri-lo |

**Caminhos.** Absoluto traz o endereço completo (\`https://site.com/img/a.png\`). Relativo parte da página atual: \`img/a.png\` (pasta dentro), \`../a.png\` (sobe uma pasta), \`/img/a.png\` (a partir da raiz do site).

### Imagens

\`<img src="mapa.png" alt="Mapa das rodovias federais do Sul" width="600" height="400">\`

- \`alt\` é o **texto alternativo**: lido por leitores de tela e exibido se a imagem falhar. Imagem só decorativa recebe \`alt=""\` (vazio), para ser ignorada.
- \`width\` e \`height\` reservam o espaço e evitam que a página "pule" ao carregar.
- Imagens **responsivas**: \`srcset\` oferece versões de tamanhos diferentes, e \`<picture>\` com \`<source>\` troca a imagem conforme a tela ou o formato suportado.

### Áudio e vídeo nativos

Antes do HTML5, vídeo dependia de plugins como o Flash. Agora:

\`\`\`html
<video controls width="640" poster="capa.jpg">
  <source src="aula.webm" type="video/webm">
  <source src="aula.mp4" type="video/mp4">
  <track src="legenda.vtt" kind="captions" srclang="pt" label="Português">
  Seu navegador não reproduz vídeo.
</video>
\`\`\`

| Atributo | Efeito |
| --- | --- |
| \`controls\` | Mostra play, volume e barra de tempo |
| \`autoplay\` | Começa sozinho; os navegadores costumam exigir **muted** junto |
| \`loop\` / \`muted\` | Repete / começa sem som |
| \`poster\` | Imagem exibida antes de começar (só vídeo) |
| \`<source>\` | Vários formatos: o navegador usa o primeiro que suporta |
| \`<track>\` | Legendas e descrições em arquivo **WebVTT** (.vtt) |

### canvas × SVG

| | \`canvas\` | \`svg\` |
| --- | --- | --- |
| Tipo | **Mapa de bits** (pixels) | **Vetorial** (formas descritas em XML) |
| Desenho | Por **JavaScript**, pixel a pixel | Por elementos (\`circle\`, \`path\`) no próprio código |
| Ampliar | Perde nitidez | Continua nítido |
| Uso típico | Jogos, gráficos dinâmicos, edição de imagem | Ícones, logotipos, mapas e gráficos que precisam escalar |

Outro conteúdo externo entra com \`iframe\` (uma página dentro da outra, como um mapa ou vídeo incorporado).`],
  [C4, `### O formulário e o envio

\`\`\`html
<form action="/inscricao" method="post">
  <label for="email">E-mail</label>
  <input type="email" id="email" name="email" required>
  <button type="submit">Enviar</button>
</form>
\`\`\`

| Atributo do form | Função |
| --- | --- |
| \`action\` | Para **onde** os dados vão |
| \`method="get"\` | Dados vão na **URL** (\`?email=...\`): bom para buscas, ruim para dados sensíveis e grandes |
| \`method="post"\` | Dados vão no **corpo** da requisição: usado para cadastros, senhas e arquivos |
| \`enctype="multipart/form-data"\` | Obrigatório para **enviar arquivos** |
| \`novalidate\` | Desliga a validação nativa do navegador |

### Rótulo, nome e identificador

- \`label for="email"\` liga o rótulo ao campo de \`id="email"\`: clicar no texto foca o campo, e o leitor de tela anuncia o rótulo.
- \`name\` é o nome com que o valor **é enviado** ao servidor. Campo sem \`name\` não é enviado.
- \`placeholder\` é só uma dica dentro do campo, que **some ao digitar**: não substitui o \`label\`.

### Os tipos de input

| Tipo | Para quê | O que o navegador ajuda |
| --- | --- | --- |
| \`text\`, \`password\` | Texto livre / senha oculta | — |
| \`email\`, \`url\`, \`tel\` | E-mail, endereço, telefone | Valida formato (email, url) e mostra o teclado certo no celular |
| \`number\`, \`range\` | Número / controle deslizante | Aceita \`min\`, \`max\` e \`step\` |
| \`date\`, \`time\`, \`color\` | Data, hora, cor | Mostra o seletor próprio |
| \`checkbox\` | Várias escolhas independentes | — |
| \`radio\` | **Uma** escolha entre opções com o **mesmo name** | — |
| \`file\` | Enviar arquivo | \`accept\` e \`multiple\` |
| \`hidden\` | Valor enviado sem aparecer | — |
| \`submit\` | Botão de envio | — |

### Validação nativa

\`required\` (obrigatório), \`minlength\`/\`maxlength\`, \`min\`/\`max\`, \`pattern\` (expressão regular) e os próprios tipos (email, url, number) fazem o navegador **bloquear o envio** com mensagem, sem JavaScript. Ela **não substitui** a validação no servidor: o usuário pode desligá-la ou enviar dados direto.

### readonly × disabled

| Atributo | Pode editar? | O valor é enviado? | Recebe foco? |
| --- | --- | --- | --- |
| \`readonly\` | Não | **Sim** | Sim |
| \`disabled\` | Não | **Não** | Não |

### Outros controles

\`select\` com \`option\` (lista; \`multiple\` permite várias), \`textarea\` (texto longo), \`datalist\` (sugestões para um input), \`fieldset\` com \`legend\` (agrupa campos relacionados, como as opções de um radio) e \`button\`. Dentro de um formulário, \`button\` sem \`type\` é **submit**: para um botão que não envia, use \`type="button"\`.`],
  [C5, `### APIs que vieram com o HTML5

| Recurso | O que faz | Detalhe de prova |
| --- | --- | --- |
| **data-\\*** | Guarda dados próprios no elemento: \`data-id="42"\` | Lidos em JavaScript por \`elemento.dataset.id\` |
| **localStorage** | Guarda pares chave-valor no navegador | **Persiste** depois de fechar o navegador; só texto |
| **sessionStorage** | Igual, mas por aba | **Apaga** ao fechar a aba |
| **Geolocalização** | Localização do aparelho | Exige **permissão** do usuário e contexto seguro (HTTPS) |
| **Arrastar e soltar** | Elementos arrastáveis | Atributo \`draggable="true"\` e eventos dragstart, drop |
| **Web Workers** | Roda JavaScript em segundo plano | Não trava a tela; **não acessa o DOM** diretamente |
| **canvas** | Desenho por script | Ver capítulo 3 |

> **localStorage × cookie:** o cookie vai junto em toda requisição ao servidor; o localStorage fica só no navegador e é lido por JavaScript da mesma origem.

### Acessibilidade: a página para todos

Acessibilidade é permitir que **qualquer pessoa** use a página: quem usa leitor de tela, quem navega só pelo teclado, quem enxerga pouco. O HTML bem escrito já resolve a maior parte:

- \`alt\` em toda imagem que transmite informação;
- \`label\` ligado a cada campo;
- títulos \`h1\`–\`h6\` em ordem lógica;
- \`lang\` no \`html\`;
- elementos nativos (\`button\`, \`a\`, \`nav\`), que já funcionam com teclado e leitor de tela.

### ARIA

**ARIA** (Accessible Rich Internet Applications) acrescenta informação para tecnologias assistivas: \`role="navigation"\`, \`aria-label="Fechar"\`, \`aria-hidden="true"\`, \`aria-expanded\`. A primeira regra é **usar o elemento HTML nativo quando ele existir**: um \`<button>\` real é melhor que uma \`div\` com \`role="button"\`, porque já vem com foco e teclado.

### WCAG e eMAG

| Sigla | O que é |
| --- | --- |
| **WCAG** | Web Content Accessibility Guidelines, as diretrizes de acessibilidade do W3C, com níveis **A**, **AA** e **AAA** |
| **POUR** | Os quatro princípios da WCAG: **Perceptível**, **Operável**, **Compreensível** e **Robusto** |
| **eMAG** | Modelo de Acessibilidade em Governo Eletrônico, a referência brasileira para sites públicos, baseada na WCAG |

> **Pegadinha:** \`tabindex\` positivo (1, 2, 3) muda a ordem natural do foco e costuma atrapalhar. \`tabindex="0"\` coloca na ordem natural, e \`-1\` permite foco só por script.`],
];

export const summary = `## Guia de revisão
**Estrutura:** \`<!DOCTYPE html>\`, \`html lang\`, \`head\` (metadados: charset, viewport, title, link) e \`body\` (o que aparece). Elementos vazios: br, hr, img, input, meta, link. id é único; class repete.

**Semântica:** header, nav, main (um só, conteúdo principal), article (independente), section (agrupamento temático), aside (tangencial), footer, figure/figcaption. strong = importância; em = ênfase. Títulos em hierarquia; tabela só para dados (th, td, colspan, rowspan, scope).

**Mídia:** a href (target, rel, download), caminhos relativos e absolutos; img com alt (decorativa: alt vazio); video/audio com controls, source e track (WebVTT); canvas é bitmap por script, SVG é vetorial.

**Formulários:** action e method (GET na URL, POST no corpo; arquivos pedem multipart/form-data). label for = id; name é o que vai ao servidor; placeholder não é rótulo. Tipos email, url, number, date, range, checkbox, radio (mesmo name). Validação nativa com required, pattern, min/max, que não substitui a do servidor. readonly envia, disabled não. button dentro de form é submit por padrão.

**APIs e acessibilidade:** data-* (dataset), localStorage persiste, sessionStorage some ao fechar a aba, geolocalização pede permissão, Web Workers não acessam o DOM. Acessibilidade com alt, label, títulos, lang e elementos nativos; ARIA complementa. WCAG (A, AA, AAA; POUR) e eMAG no governo.`;

export const flashcards = [
  ["O que significa a sigla HTML?", "HyperText Markup Language, linguagem de marcação de hipertexto: descreve a estrutura e o significado do conteúdo."],
  ["HTML é linguagem de programação?", "Não. É linguagem de marcação: não tem variáveis, laços nem decisões. A lógica fica com o JavaScript."],
  ["Para que serve o <!DOCTYPE html>?", "Avisa ao navegador que o documento segue o HTML atual, para renderizar no modo padrão."],
  ["O que vai no head e o que vai no body?", "No head, metadados (title, meta charset, viewport, link para CSS); no body, o que aparece na tela."],
  ["Cite elementos vazios (sem fechamento).", "br, hr, img, input, meta e link."],
  ["Qual a diferença entre id e class?", "id identifica um único elemento da página; class pode ser repetida em vários elementos."],
  ["Quando usar main?", "Para o conteúdo principal da página; deve haver um só main visível."],
  ["article ou section?", "article é conteúdo independente, que faz sentido sozinho (notícia, post); section é agrupamento temático de algo maior."],
  ["Para que serve aside?", "Para conteúdo tangencial ao principal, como barras laterais e links relacionados."],
  ["Qual a diferença entre strong e b?", "strong indica importância (tem significado); b só destaca visualmente."],
  ["Para que servem colspan e rowspan?", "colspan faz a célula ocupar várias colunas; rowspan, várias linhas."],
  ["O que fazer com o alt de uma imagem decorativa?", "Deixá-lo vazio (alt=\"\"), para o leitor de tela ignorar a imagem."],
  ["O que o elemento track adiciona a um vídeo?", "Legendas, legendas descritivas ou capítulos, em arquivo WebVTT (.vtt)."],
  ["Por que usar vários source num video?", "Para oferecer formatos diferentes; o navegador usa o primeiro que suporta."],
  ["canvas ou SVG: qual é vetorial?", "SVG, que descreve formas em XML e escala sem perder nitidez; canvas é mapa de pixels desenhado por JavaScript."],
  ["Qual a diferença entre GET e POST num formulário?", "GET envia os dados na URL; POST, no corpo da requisição, mais adequado para dados sensíveis e arquivos."],
  ["O que é obrigatório para enviar arquivos por formulário?", "method=\"post\" e enctype=\"multipart/form-data\"."],
  ["Para que serve o atributo name de um campo?", "É o nome com que o valor é enviado ao servidor; campo sem name não é enviado."],
  ["placeholder substitui o label?", "Não: some ao digitar e não é um rótulo acessível; o label continua necessário."],
  ["Como fazer radios serem do mesmo grupo?", "Dando o mesmo name a todos; assim só um pode ser marcado."],
  ["readonly ou disabled: qual valor é enviado?", "readonly é enviado; disabled não é enviado nem recebe foco."],
  ["Qual o type padrão de um button dentro de um form?", "submit: para um botão que não envia, usa-se type=\"button\"."],
  ["localStorage ou sessionStorage: qual persiste ao fechar o navegador?", "localStorage; o sessionStorage é apagado ao fechar a aba."],
  ["Qual a primeira regra do uso de ARIA?", "Usar o elemento HTML nativo quando ele existir; ARIA complementa, não substitui."],
  ["Quais são os princípios da WCAG?", "Perceptível, Operável, Compreensível e Robusto (POUR)."],
];

export const tips = [
  "Em questões de semântica, pergunte qual é a função do conteúdo: navegação (nav), principal (main), independente (article), temático (section) ou lateral (aside).",
  "head guarda metadados e não aparece na tela; tudo o que o usuário vê fica no body.",
  "Formulário: action diz para onde, method diz como; GET põe os dados na URL e POST no corpo.",
  "Para acessibilidade de formulários, procure a ligação label for = id do campo.",
  "Lembre a dupla readonly envia, disabled não envia.",
  "canvas é pixel desenhado por script; SVG é vetor descrito no código. Ampliar sem perder qualidade é SVG.",
  "Na dúvida entre ARIA e elemento nativo, a alternativa certa costuma ser o elemento nativo.",
  "localStorage persiste; sessionStorage dura a aba; cookie viaja com as requisições.",
];

export const traps = [
  "A alternativa chama o HTML de linguagem de programação. Errado: é linguagem de marcação.",
  "A alternativa diz que o placeholder é o rótulo acessível do campo. Errado: o rótulo é o label ligado pelo for e pelo id.",
  "A alternativa afirma que POST é mais seguro porque criptografa os dados. Errado: POST só tira os dados da URL; quem protege a transmissão é o HTTPS.",
  "A alternativa diz que campo disabled é enviado com o formulário. Errado: só readonly é enviado.",
  "A alternativa recomenda tabela para organizar o layout da página. Errado: tabela é para dados tabulares; layout é com CSS.",
  "A alternativa diz que a validação nativa dispensa a do servidor. Errado: o usuário pode contorná-la; o servidor precisa validar de novo.",
  "A alternativa afirma que uma página pode ter vários main visíveis. Errado: deve haver um só main visível.",
  "A alternativa diz que Web Workers manipulam o DOM. Errado: eles rodam em segundo plano sem acesso direto ao DOM.",
];

export const activeRecall = [
  ["Monte de memória o esqueleto mínimo de um documento HTML5 e diga a função de cada parte.", "DOCTYPE (modo padrão), html com lang (raiz e idioma), head com meta charset, viewport e title (metadados) e body (o que aparece)."],
  ["Como decidir entre article, section e div?", "article se o conteúdo faz sentido sozinho; section se é um agrupamento temático com título; div quando não há significado, só necessidade de estilo ou script."],
  ["Explique a diferença entre strong/em e b/i.", "strong e em carregam significado (importância e ênfase); b e i só marcam visualmente ou indicam termo técnico, estrangeiro ou voz diferente."],
  ["Quando o alt de uma imagem deve ficar vazio e quando deve descrever?", "Vazio quando a imagem é decorativa; descritivo quando transmite informação, dizendo o que ela comunica."],
  ["Compare canvas e SVG em três pontos.", "canvas: pixels, desenho por JavaScript, perde nitidez ao ampliar. SVG: vetores em XML, elementos no código, escala sem perder qualidade."],
  ["O que acontece com os dados num formulário com method GET e com POST?", "GET: viram parâmetros na URL, aparecem no histórico e têm limite de tamanho. POST: vão no corpo da requisição; para arquivos usa-se multipart/form-data."],
  ["Por que o label é importante e como ligá-lo ao campo?", "Ele é o nome acessível do campo e amplia a área de clique; liga-se com label for igual ao id do input."],
  ["Diferencie readonly e disabled quanto a edição, foco e envio.", "Nenhum dos dois deixa editar. readonly recebe foco e é enviado; disabled não recebe foco e não é enviado."],
  ["Compare localStorage, sessionStorage e cookie.", "localStorage persiste no navegador; sessionStorage some ao fechar a aba; cookie também é guardado, mas vai junto em cada requisição ao servidor."],
  ["Cite três práticas de HTML que garantem acessibilidade.", "alt nas imagens informativas, label ligado a cada campo e títulos em ordem lógica (além de lang e elementos nativos)."],
];

export const errorConcepts = [
  ["e1", "Estrutura e metadados", "head guarda metadados e não aparece; body é o conteúdo visível. DOCTYPE ativa o modo padrão; id é único e class repete."],
  ["e2", "Semântica dos elementos", "main é o conteúdo principal único; article é independente; section é temático; aside é tangencial. strong e em têm significado; b e i não."],
  ["e3", "Mídia e gráficos", "alt descreve a imagem (vazio se decorativa); video e audio usam source e track; canvas é bitmap por script e SVG é vetorial."],
  ["e4", "Envio de formulários", "GET põe os dados na URL e POST no corpo; arquivos pedem multipart/form-data; name é o que vai ao servidor; disabled não é enviado."],
  ["e5", "Rótulos e validação", "label for = id é o rótulo acessível; placeholder não é rótulo; validação nativa não substitui a do servidor."],
  ["e6", "APIs e acessibilidade", "localStorage persiste e sessionStorage dura a aba; Web Workers não acessam o DOM; ARIA complementa elementos nativos; WCAG tem os princípios POUR."],
];

// [enunciado, dificuldade, capítulo, conceito, alternativas [texto, certa?][], explicação]
export const questoes = [
  ["Em um documento HTML, qual elemento reúne os metadados da página, como a codificação de caracteres, o título da aba e as folhas de estilo?", "FACIL", C1, "e1",
    [["O elemento head.", true], ["O elemento body.", false], ["O elemento header.", false], ["O elemento footer.", false], ["O elemento main.", false]],
    "Gabarito A. O head contém metadados que descrevem o documento e não aparecem no corpo: meta charset, title, viewport, link para CSS. O body traz o conteúdo visível, e header, footer e main são elementos semânticos dentro do body, sem relação com metadados."],
  ["Qual elemento do HTML5 deve marcar o conteúdo principal e dominante de uma página, distinto de menus, barras laterais e rodapé?", "FACIL", C2, "e2",
    [["O elemento section.", false], ["O elemento main.", true], ["O elemento article.", false], ["O elemento aside.", false], ["O elemento div.", false]],
    "Gabarito B. main representa o conteúdo principal da página, e deve haver um só main visível. section agrupa um tema, article é conteúdo independente, aside é tangencial ao principal e div é genérico, sem significado."],
  ["Um portal publica notícias que podem ser compartilhadas e lidas isoladamente em outros sites. Qual elemento é o mais adequado para envolver cada notícia?", "MEDIA", C2, "e2",
    [["section, por agrupar o tema da página.", false], ["aside, por ficar ao lado do conteúdo.", false], ["article, por ser conteúdo independente.", true], ["nav, por levar a outras páginas do portal.", false], ["span, por ser um contêiner de texto.", false]],
    "Gabarito C. article representa uma composição independente, que faz sentido fora do contexto, como notícia, post ou comentário. section é um agrupamento temático de algo maior, aside é tangencial, nav é navegação e span é um contêiner genérico em linha."],
  ["Uma imagem apenas decorativa, sem informação para o usuário, deve receber qual tratamento no atributo alt?", "MEDIA", C3, "e3",
    [["Repetir o nome do arquivo da imagem.", false], ["Descrever cores e formas da decoração.", false], ["Remover o atributo alt por completo.", false], ["Deixar o alt vazio, com aspas sem texto.", true], ["Usar a palavra imagem como texto.", false]],
    "Gabarito D. alt vazio (alt=\"\") informa que a imagem é decorativa, e o leitor de tela a ignora. Remover o alt faz alguns leitores anunciarem o nome do arquivo; descrever a decoração ou escrever 'imagem' só gera ruído para quem usa tecnologia assistiva."],
  ["Em um vídeo incorporado com o elemento video, qual elemento filho acrescenta legendas a partir de um arquivo WebVTT?", "MEDIA", C3, "e3",
    [["O elemento source.", false], ["O elemento caption.", false], ["O elemento figcaption.", false], ["O elemento param.", false], ["O elemento track.", true]],
    "Gabarito E. track liga arquivos WebVTT (.vtt) ao vídeo, com kind como captions ou subtitles. source oferece formatos alternativos do próprio vídeo, caption é título de tabela, figcaption é legenda de figura e param pertencia a object."],
  ["Um site precisa de um logotipo que fique nítido em qualquer tamanho de tela, sem perda de qualidade ao ampliar. Qual tecnologia é a mais indicada?", "MEDIA", C3, "e3",
    [["SVG, por descrever formas vetoriais.", true], ["canvas, por desenhar pixels via script.", false], ["iframe, por carregar outra página.", false], ["audio, por permitir mídia incorporada.", false], ["table, por organizar a imagem em células.", false]],
    "Gabarito A. SVG descreve formas geométricas em XML e escala sem perder nitidez. canvas é mapa de bits: ao ampliar, perde qualidade. iframe incorpora outra página, audio é para som e table é para dados tabulares."],
  ["Em um formulário de cadastro com senha, qual método de envio evita que os dados apareçam na barra de endereços e no histórico do navegador?", "FACIL", C4, "e4",
    [["GET, que envia os dados no corpo da requisição.", false], ["POST, que envia os dados no corpo da requisição.", true], ["GET, que criptografa os dados antes do envio.", false], ["POST, que dispensa o uso de HTTPS.", false], ["HEAD, que envia só os cabeçalhos dos campos.", false]],
    "Gabarito B. Com POST, os dados vão no corpo da requisição e não aparecem na URL nem no histórico. GET coloca os dados na URL. Nenhum dos dois criptografa: a proteção na transmissão vem do HTTPS, que continua necessário. HEAD não é usado para enviar formulários."],
  ["Para que um formulário envie arquivos escolhidos com input type=file, além de method=post, qual atributo do form é necessário?", "DIFICIL", C4, "e4",
    [["accept=\"file\".", false], ["action=\"upload\".", false], ["enctype=\"multipart/form-data\".", true], ["novalidate.", false], ["target=\"_blank\".", false]],
    "Gabarito C. O envio de arquivos exige enctype=\"multipart/form-data\", que divide o corpo da requisição em partes. accept fica no input e filtra os tipos de arquivo, action é só o destino, novalidate desliga a validação e target define onde abre a resposta."],
  ["Qual atributo define o nome com que o valor de um campo é enviado ao servidor quando o formulário é submetido?", "FACIL", C4, "e4",
    [["id.", false], ["class.", false], ["placeholder.", false], ["name.", true], ["title.", false]],
    "Gabarito D. name é a chave enviada com o valor (por exemplo, email=fulano@x.com); campo sem name não é enviado. id identifica o elemento na página e liga o label, class agrupa para estilo, placeholder é dica visual e title é a dica do mouse."],
  ["Um campo de CPF já preenchido deve aparecer para o usuário sem permitir edição, mas o valor precisa chegar ao servidor no envio. O atributo adequado é:", "MEDIA", C4, "e4",
    [["disabled.", false], ["hidden.", false], ["required.", false], ["autocomplete=\"off\".", false], ["readonly.", true]],
    "Gabarito E. readonly impede a edição, mantém o foco e envia o valor. disabled também impede a edição, mas não envia o valor nem recebe foco. hidden esconde o elemento, required torna o preenchimento obrigatório e autocomplete apenas controla sugestões do navegador."],
  ["Um grupo de opções de sexo, em que só uma pode ser marcada, foi feito com input type=radio. O que garante que marcar uma opção desmarque as outras?", "MEDIA", C4, "e4",
    [["Todos os radios terem o mesmo name.", true], ["Todos os radios terem o mesmo id.", false], ["Cada radio ter um value igual.", false], ["O form ter o atributo novalidate.", false], ["Cada radio estar dentro de um label.", false]],
    "Gabarito A. Radios com o mesmo name formam um grupo, e só um pode ficar marcado. O id deve ser único, então não pode repetir; o value deve ser diferente para identificar a escolha; novalidate e label não criam grupo."],
  ["Dentro de um formulário, um button escrito sem o atributo type é tratado pelo navegador como qual tipo?", "DIFICIL", C4, "e4",
    [["button, que não executa nenhuma ação.", false], ["submit, que envia o formulário.", true], ["reset, que limpa os campos.", false], ["menu, que abre uma lista de opções.", false], ["image, que envia as coordenadas do clique.", false]],
    "Gabarito B. O tipo padrão de button é submit: clicar nele envia o formulário. Por isso, botões que só abrem algo ou rodam script devem ter type=\"button\". reset limpa os campos, e menu e image não são o padrão."],
  ["Sobre a validação nativa de formulários do HTML5, com atributos como required e pattern, é correto afirmar que ela:", "MEDIA", C4, "e5",
    [["Dispensa qualquer validação no servidor.", false], ["Só funciona com JavaScript habilitado.", false], ["Impede o envio no navegador, mas não substitui a do servidor.", true], ["Funciona apenas em campos do tipo text.", false], ["É ativada pelo atributo novalidate.", false]],
    "Gabarito C. A validação nativa bloqueia o envio no navegador e mostra mensagens, sem JavaScript, mas pode ser contornada; o servidor precisa validar de novo. Ela vale para vários tipos (email, number, url) e é justamente desligada por novalidate."],
  ["Qual elemento HTML liga um texto descritivo a um campo de formulário, de modo que leitores de tela anunciem o rótulo ao focar o campo?", "FACIL", C4, "e5",
    [["fieldset, que agrupa campos relacionados.", false], ["legend, que dá título ao grupo de campos.", false], ["placeholder, que mostra a dica dentro do campo.", false], ["label, associado ao id do campo pelo for.", true], ["datalist, que oferece sugestões ao campo.", false]],
    "Gabarito D. label com for igual ao id do input cria o nome acessível do campo e amplia a área de clique. fieldset e legend agrupam e titulam conjuntos de campos, placeholder é atributo de dica que some ao digitar e datalist apenas sugere valores."],
  ["Qual mecanismo de armazenamento do navegador mantém os dados mesmo depois que o usuário fecha e reabre o navegador, sem enviá-los ao servidor a cada requisição?", "MEDIA", C5, "e6",
    [["sessionStorage.", false], ["Cookie de sessão.", false], ["Variável global de JavaScript.", false], ["Atributo data-* do elemento.", false], ["localStorage.", true]],
    "Gabarito E. localStorage guarda pares chave-valor de forma persistente na origem do site e não viaja nas requisições. sessionStorage é apagado ao fechar a aba, cookies vão junto em cada requisição, variáveis somem ao recarregar e data-* fica só no HTML da página."],
  ["Um script pesado de cálculo trava a interface enquanto roda. Qual recurso do HTML5 permite executá-lo em segundo plano, sem bloquear a tela?", "DIFICIL", C5, "e6",
    [["Web Workers.", true], ["sessionStorage.", false], ["Geolocalização.", false], ["Atributos data-*.", false], ["Elemento canvas.", false]],
    "Gabarito A. Web Workers executam JavaScript em outra linha de execução, sem travar a interface, e se comunicam com a página por mensagens; não acessam o DOM diretamente. sessionStorage guarda dados, geolocalização dá a posição, data-* guarda atributos e canvas desenha."],
  ["Um desenvolvedor criou um botão com `<div role=\"button\">`. Segundo a primeira regra de uso do ARIA, a melhor prática seria:", "DIFICIL", C5, "e6",
    [["Acrescentar aria-hidden=\"true\" à div.", false], ["Usar o elemento nativo button.", true], ["Trocar role por tabindex=\"5\".", false], ["Envolver a div em um elemento nav.", false], ["Adicionar title com o texto do botão.", false]],
    "Gabarito B. A primeira regra do ARIA é usar o elemento HTML nativo quando ele existe: button já vem com foco, teclado e papel acessível. aria-hidden esconderia o botão do leitor de tela, tabindex positivo bagunça a ordem do foco, e nav e title não resolvem a função."],
  ["Os quatro princípios das diretrizes de acessibilidade WCAG, conhecidos pela sigla POUR, são:", "MEDIA", C5, "e6",
    [["Público, Organizado, Útil e Rápido.", false], ["Padronizado, Otimizado, Unificado e Responsivo.", false], ["Perceptível, Operável, Compreensível e Robusto.", true], ["Permanente, Objetivo, Universal e Rastreável.", false], ["Prático, Ordenado, Usável e Reutilizável.", false]],
    "Gabarito C. A WCAG organiza os critérios em quatro princípios: Perceptível, Operável, Compreensível e Robusto (POUR, do inglês Perceivable, Operable, Understandable, Robust). As demais combinações não fazem parte das diretrizes."],
  ["Em uma tabela de dados, uma célula de cabeçalho deve ocupar o espaço de três colunas. O atributo adequado é:", "FACIL", C2, "e2",
    [["rowspan=\"3\".", false], ["span=\"3\".", false], ["cols=\"3\".", false], ["colspan=\"3\".", true], ["width=\"3\".", false]],
    "Gabarito D. colspan define quantas colunas a célula ocupa. rowspan faz o mesmo para linhas, cols pertence ao textarea, span é usado em col e colgroup, e width define largura, não mescla células."],
  ["Qual atributo, colocado no elemento html, informa o idioma do conteúdo, ajudando leitores de tela a usarem a pronúncia correta?", "FACIL", C1, "e1",
    [["charset.", false], ["lang.", true], ["dir.", false], ["translate.", false], ["locale.", false]],
    "Gabarito B. lang=\"pt-BR\" no html define o idioma principal, usado por leitores de tela, tradutores e buscadores. charset define a codificação (no meta), dir define a direção do texto, translate diz se algo deve ser traduzido e locale não é atributo HTML."],
  ["Qual das alternativas lista apenas elementos vazios, isto é, que não têm conteúdo nem marca de fechamento?", "MEDIA", C1, "e1",
    [["img, input, br e meta.", true], ["p, div, span e a.", false], ["ul, li, ol e dl.", false], ["head, body, title e html.", false], ["form, label, select e option.", false]],
    "Gabarito A. img, input, br e meta são elementos vazios: não envolvem conteúdo e não têm marca de fechamento. Os demais grupos são elementos com conteúdo, que abrem e fecham, como parágrafos, listas, a estrutura do documento e controles de formulário."],
  ["Em uma lista de etapas de um processo, em que a ordem importa, qual elemento é o mais adequado?", "FACIL", C2, "e2",
    [["ul, com marcadores sem ordem.", false], ["dl, com termos e definições.", false], ["menu, com itens de comando.", false], ["table, com uma coluna de etapas.", false], ["ol, com itens numerados em ordem.", true]],
    "Gabarito E. ol é a lista ordenada, em que a sequência tem significado, e os itens aparecem numerados. ul é lista sem ordem, dl associa termos a definições, menu é para comandos e table é para dados com linhas e colunas."],

  ["O HTML é uma linguagem de marcação, sem estruturas de decisão ou repetição, e por isso não é considerado uma linguagem de programação.", "FACIL", C1, "e1",
    [["Certo", true], ["Errado", false]],
    "Certo. O HTML descreve a estrutura e o significado do conteúdo por meio de elementos. Ele não tem variáveis, condicionais nem laços; a lógica da página fica a cargo do JavaScript."],
  ["Os elementos colocados dentro do head, como title e meta, são exibidos no corpo da página, acima do conteúdo do body.", "FACIL", C1, "e1",
    [["Certo", false], ["Errado", true]],
    "Errado. O head guarda metadados que descrevem o documento e não aparecem no corpo. O title aparece na aba do navegador e nos resultados de busca, mas não dentro da página; o conteúdo visível fica no body."],
  ["O atributo id deve ter valor único na página, enquanto o mesmo valor de class pode ser aplicado a vários elementos.", "FACIL", C1, "e1",
    [["Certo", true], ["Errado", false]],
    "Certo. O id identifica um único elemento e serve, por exemplo, para âncoras e para ligar o label ao campo. A class agrupa elementos que recebem o mesmo estilo ou comportamento e pode se repetir quantas vezes for preciso."],
  ["A declaração <!DOCTYPE html> é um elemento HTML que deve ser fechado com </DOCTYPE> no final do documento.", "MEDIA", C1, "e1",
    [["Certo", false], ["Errado", true]],
    "Errado. O DOCTYPE não é um elemento, é uma declaração que fica na primeira linha e avisa ao navegador para usar o modo padrão. Ela não tem marca de fechamento."],
  ["Uma página pode ter vários elementos main visíveis, um para cada seção de conteúdo importante.", "MEDIA", C2, "e2",
    [["Certo", false], ["Errado", true]],
    "Errado. O main representa o conteúdo principal e dominante da página, e só pode haver um main visível por vez. Para dividir o conteúdo em partes, usam-se section e article dentro dele."],
  ["O elemento strong indica que o trecho tem importância, enquanto b apenas destaca visualmente, sem acrescentar significado.", "MEDIA", C2, "e2",
    [["Certo", true], ["Errado", false]],
    "Certo. Os dois aparecem em negrito por padrão, mas strong carrega significado (importância) e pode ser tratado de forma diferente por tecnologias assistivas. b é um destaque apenas visual."],
  ["Tabelas HTML são a forma recomendada de organizar colunas e áreas do layout de uma página.", "FACIL", C2, "e2",
    [["Certo", false], ["Errado", true]],
    "Errado. Tabela é para dados tabulares, como planilhas e horários. Usar tabela para layout prejudica a acessibilidade e a manutenção; a distribuição visual da página é feita com CSS, por exemplo com Flexbox e Grid."],
  ["O elemento nav deve ser usado para os blocos principais de navegação, e não necessariamente para todo grupo de links da página.", "DIFICIL", C2, "e2",
    [["Certo", true], ["Errado", false]],
    "Certo. nav marca blocos de navegação importantes, como o menu principal ou um sumário. Um punhado de links no rodapé ou dentro de um texto não precisa de nav, para não poluir a navegação por regiões do leitor de tela."],
  ["O elemento canvas desenha gráficos vetoriais descritos em XML, que podem ser ampliados sem perda de qualidade.", "MEDIA", C3, "e3",
    [["Certo", false], ["Errado", true]],
    "Errado. canvas é uma área de mapa de bits desenhada por JavaScript, pixel a pixel, que perde nitidez ao ampliar. O formato vetorial descrito em XML, que escala sem perder qualidade, é o SVG."],
  ["No elemento video, vários elementos source permitem oferecer o mesmo conteúdo em formatos diferentes, e o navegador usa o primeiro que conseguir reproduzir.", "MEDIA", C3, "e3",
    [["Certo", true], ["Errado", false]],
    "Certo. Cada source traz um arquivo e o seu tipo (como video/webm e video/mp4). O navegador percorre a lista e escolhe o primeiro formato suportado, o que aumenta a compatibilidade."],
  ["Em um link, o atributo target=\"_blank\" faz o destino abrir em uma nova aba ou janela do navegador.", "FACIL", C3, "e3",
    [["Certo", true], ["Errado", false]],
    "Certo. _blank abre o destino em um novo contexto de navegação. Por segurança, recomenda-se rel=\"noopener\", que impede a página aberta de controlar a original; os navegadores atuais já aplicam esse comportamento por padrão."],
  ["O caminho relativo ../imagens/logo.png aponta para a pasta imagens localizada dentro da pasta da página atual.", "DIFICIL", C3, "e3",
    [["Certo", false], ["Errado", true]],
    "Errado. Os dois pontos (..) sobem um nível: o caminho aponta para a pasta imagens que está ao lado da pasta atual, no nível de cima. Dentro da pasta da página seria imagens/logo.png."],
  ["Em um formulário enviado com method=\"get\", os dados preenchidos aparecem na URL como parâmetros, depois do sinal de interrogação.", "FACIL", C4, "e4",
    [["Certo", true], ["Errado", false]],
    "Certo. Com GET, os campos viram parâmetros na URL (por exemplo, ?busca=rodovia). Por isso o GET é adequado a buscas e filtros, mas não a senhas e dados sensíveis, que ficariam no histórico e nos registros."],
  ["O método POST criptografa os dados do formulário, o que dispensa o uso de HTTPS no envio de senhas.", "MEDIA", C4, "e4",
    [["Certo", false], ["Errado", true]],
    "Errado. POST apenas leva os dados no corpo da requisição, fora da URL; não há criptografia. Quem protege a transmissão é o HTTPS, que continua indispensável para senhas e dados pessoais."],
  ["Um campo com o atributo disabled não pode ser editado e o seu valor não é enviado com o formulário.", "MEDIA", C4, "e4",
    [["Certo", true], ["Errado", false]],
    "Certo. disabled desativa o controle: ele não recebe foco, não pode ser editado e fica fora dos dados enviados. Se a intenção é só impedir a edição e ainda enviar o valor, o atributo é readonly."],
  ["O atributo placeholder funciona como rótulo acessível do campo e pode substituir o elemento label sem prejuízo.", "MEDIA", C4, "e5",
    [["Certo", false], ["Errado", true]],
    "Errado. O placeholder é uma dica que some quando o usuário começa a digitar e não é tratado como rótulo confiável pelas tecnologias assistivas. O rótulo acessível é o label ligado ao campo pelo for e pelo id."],
  ["O input type=\"email\" faz o navegador verificar se o valor tem formato de endereço de e-mail antes de permitir o envio do formulário.", "FACIL", C4, "e5",
    [["Certo", true], ["Errado", false]],
    "Certo. Esse tipo ativa a validação nativa do formato (algo com @ e domínio) e, no celular, costuma abrir um teclado com o @ à mão. A validação pode ser desligada com novalidate e precisa ser repetida no servidor."],
  ["Os dados guardados com sessionStorage permanecem disponíveis mesmo depois que o usuário fecha a aba e abre o site de novo.", "MEDIA", C5, "e6",
    [["Certo", false], ["Errado", true]],
    "Errado. O sessionStorage vale só enquanto a aba está aberta e é apagado ao fechá-la. Para manter os dados entre visitas, usa-se o localStorage, que persiste no navegador."],
  ["A API de geolocalização do navegador só fornece a posição do usuário depois que ele concede permissão.", "FACIL", C5, "e6",
    [["Certo", true], ["Errado", false]],
    "Certo. Por privacidade, o navegador pede autorização antes de entregar a localização ao site, e os navegadores atuais exigem contexto seguro (HTTPS). Sem permissão, a chamada termina em erro."],
  ["Atributos personalizados no formato data-*, como data-id=\"42\", podem ser lidos em JavaScript pela propriedade dataset do elemento.", "DIFICIL", C5, "e6",
    [["Certo", true], ["Errado", false]],
    "Certo. O HTML5 permite guardar dados próprios em atributos data-*; o valor de data-id aparece em elemento.dataset.id. É uma forma padronizada de passar informações do HTML para o script sem inventar atributos fora do padrão."],
  ["O eMAG é o modelo de acessibilidade adotado pelo governo brasileiro e foi elaborado com base nas diretrizes internacionais da WCAG.", "DIFICIL", C5, "e6",
    [["Certo", true], ["Errado", false]],
    "Certo. O Modelo de Acessibilidade em Governo Eletrônico orienta os sites públicos brasileiros e se baseia na WCAG, adaptando as recomendações à realidade nacional, com orientações como atalhos de teclado e descrição de imagens."],
];

export const sources = [
  ["OFICIAL", "HTML Living Standard", "WHATWG", "Estrutura do documento, semântica, formulários, mídia e APIs", "https://html.spec.whatwg.org/multipage/"],
  ["OFICIAL", "Web Content Accessibility Guidelines (WCAG) 2.2", "W3C", "Princípios e critérios de acessibilidade", "https://www.w3.org/TR/WCAG22/"],
  ["OFICIAL", "Modelo de Acessibilidade em Governo Eletrônico (eMAG)", "Governo Federal", "Recomendações de acessibilidade para sites públicos", "https://emag.governoeletronico.gov.br/"],
  ["OFICIAL", "WAI-ARIA 1.2", "W3C", "Papéis, estados e propriedades ARIA", "https://www.w3.org/TR/wai-aria-1.2/"],
  ["COMPLEMENTAR", "MDN Web Docs: HTML", "Mozilla", "Elementos, atributos, formulários e Web Storage", "https://developer.mozilla.org/pt-BR/docs/Web/HTML"],
];
