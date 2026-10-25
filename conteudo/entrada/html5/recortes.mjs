// Recortes "como a banca cobra" da matéria informatica.html5, escritos à mão.
import { C1, C2, C3, C4, C5 } from "./conteudo.mjs";

// [enunciado, dificuldade, capítulo, conceito, alternativas [texto, certa?, comentário?][], explicação]
export const recortes = [
  {
    pasta: "geral", board: "GERAL", incidence: "ALTA", estilo: "GERAL",
    howItFalls: "Nas provas de tecnologia, HTML5 é cobrado de forma conceitual e quase sempre junto com CSS e JavaScript. Os temas mais frequentes são a função dos elementos semânticos (header, nav, main, article, section, aside, footer), a estrutura do documento (DOCTYPE, head e body), os formulários (GET e POST, label, tipos de input, validação nativa, readonly e disabled) e os recursos que surgiram no HTML5: áudio e vídeo nativos, canvas, SVG e armazenamento local. Acessibilidade aparece cada vez mais, com texto alternativo, rótulos de formulário, ARIA e a WCAG. A melhor forma de estudar é associar cada elemento à sua finalidade e decorar as poucas diferenças que as bancas adoram trocar: article e section, strong e b, localStorage e sessionStorage, readonly e disabled, canvas e SVG.",
    tips: [
      "Leia o elemento pela finalidade: navegação, conteúdo principal, conteúdo independente, agrupamento temático ou conteúdo lateral.",
      "Em formulários, separe as três coisas: para onde vai (action), como vai (method) e com que nome (name).",
      "Recurso novo do HTML5 em prova costuma ser áudio e vídeo nativos, canvas, armazenamento local, geolocalização e novos tipos de input.",
      "Em acessibilidade, a resposta certa tende a usar o próprio HTML (alt, label, títulos), e não ARIA no lugar dele."
    ],
    traps: [
      "A alternativa diz que o HTML5 depende de plugin para reproduzir vídeo. Errado: video e audio são nativos.",
      "A alternativa troca localStorage e sessionStorage. Errado: o primeiro persiste; o segundo some ao fechar a aba.",
      "A alternativa diz que section e article são sinônimos. Errado: article é independente; section é temático.",
      "A alternativa coloca o title dentro do body. Errado: title é metadado e fica no head."
    ],
    questoes: [
      ["Entre os recursos introduzidos pelo HTML5, encontram-se:", "FACIL", C3, "e3",
        [["Os elementos video e audio, que reproduzem mídia sem plugins.", true], ["O elemento font, para definir a fonte do texto.", false], ["O elemento frameset, para dividir a janela em quadros.", false], ["O elemento center, para centralizar o conteúdo.", false], ["O elemento applet, para executar programas Java.", false]],
        "Gabarito A. O HTML5 trouxe áudio e vídeo nativos, dispensando plugins como o Flash. font e center eram elementos de apresentação abandonados, frameset foi retirado em favor de layouts com CSS, e applet também deixou de existir."],
      ["O elemento aside representa um conteúdo tangencialmente relacionado ao conteúdo principal, como barras laterais e blocos de links relacionados.", "FACIL", C2, "e2",
        [["Certo", true], ["Errado", false]],
        "Certo. aside marca o que complementa o conteúdo ao redor sem fazer parte do fluxo principal, como uma barra lateral, uma caixa de 'veja também' ou uma nota explicativa."],
      ["Em um formulário, os campos com o atributo readonly não são enviados ao servidor quando o formulário é submetido.", "MEDIA", C4, "e4",
        [["Certo", false], ["Errado", true]],
        "Errado. readonly impede a edição, mas mantém o foco e envia o valor normalmente. Quem fica de fora do envio é o campo com disabled."]
    ]
  },
  {
    pasta: "fumarc", board: "FUMARC", incidence: "MEDIA", estilo: "FUMARC",
    howItFalls: "A FUMARC usa múltipla escolha com quatro alternativas (A a D) e cobra HTML de forma objetiva, pedindo a afirmativa correta ou a incorreta sobre elementos, atributos e formulários. O padrão esperado é reconhecer a função de cada elemento e a sintaxe certa de atributos comuns, sem exigir código longo. As alternativas erradas trocam elementos de função vizinha (header por head, article por section, b por strong) ou atributos parecidos (id por name, readonly por disabled). Como há menos questões públicas específicas de HTML5 desta banca, estude o núcleo com segurança: estrutura do documento, elementos semânticos, links e imagens com alt, formulários com GET e POST e os recursos nativos de áudio e vídeo. Atenção redobrada aos enunciados que pedem a alternativa INCORRETA.",
    tips: [
      "Não confunda head (metadados do documento) com header (cabeçalho visível de uma página ou seção).",
      "Em 'assinale a INCORRETA', avalie cada frase separadamente e marque a única que você riscou.",
      "Associe name ao envio dos dados e id à identificação do elemento na página.",
      "Lembre o trio do vídeo: controls mostra os botões, source oferece formatos e track traz as legendas."
    ],
    traps: [
      "A alternativa diz que o elemento header guarda os metadados da página. Errado: metadados ficam no head.",
      "A alternativa afirma que o atributo id define o nome enviado ao servidor. Errado: quem define é o name.",
      "A alternativa diz que o atributo alt é opcional e não tem relação com acessibilidade. Errado: alt é o texto alternativo lido por leitores de tela.",
      "A alternativa afirma que method=\"get\" envia os dados no corpo da requisição. Errado: GET envia na URL."
    ],
    questoes: [
      // Cinco alternativas, cada uma com comentário: no pedido "4 alternativas" o servidor tira uma
      // errada e refaz as letras; por isso as explicações não citam letras.
      ["Sobre a estrutura de um documento HTML, assinale a alternativa CORRETA.", "FACIL", C1, "e1",
        [["O elemento header reúne os metadados, como title e meta.", false, "Metadados ficam no head; header é o cabeçalho visível."],
         ["O elemento head reúne metadados que não aparecem no corpo da página.", true, "Correta: title, meta e link ficam no head."],
         ["O elemento title é exibido como o primeiro título dentro do body.", false, "O title aparece na aba do navegador, não no corpo."],
         ["O DOCTYPE é um elemento que deve ser fechado no fim do documento.", false, "O DOCTYPE é uma declaração e não tem fechamento."],
         ["O elemento body é opcional quando a página tem um main.", false, "O main fica dentro do body; o body continua sendo o corpo do documento."]],
        "O head guarda os metadados que descrevem o documento, como title, meta charset e link para o CSS, e nada disso aparece no corpo. O conteúdo visível fica no body, e o DOCTYPE é só uma declaração inicial, sem fechamento."],
      ["Em relação aos elementos semânticos do HTML5, assinale a alternativa INCORRETA.", "MEDIA", C2, "e2",
        [["O nav marca blocos principais de navegação.", false, "Afirmação correta: nav é para menus e sumários importantes."],
         ["O main marca o conteúdo principal da página.", false, "Afirmação correta: deve haver um só main visível."],
         ["O article marca conteúdo independente, como uma notícia.", false, "Afirmação correta: article faz sentido sozinho."],
         ["O aside marca o conteúdo principal e obrigatório da página.", true, "É a incorreta: aside é conteúdo tangencial, lateral ao principal."],
         ["O footer marca o rodapé de uma página ou de uma seção.", false, "Afirmação correta: pode haver footer de página e de seção."]],
        "O aside é para conteúdo tangencial, como barras laterais e links relacionados; o conteúdo principal é marcado com main. As demais frases descrevem corretamente nav, main, article e footer."],
      ["Para que um campo de texto seja preenchido obrigatoriamente antes do envio do formulário, sem uso de JavaScript, utiliza-se o atributo:", "FACIL", C4, "e5",
        [["required.", true, "Correta: ativa a validação nativa de preenchimento obrigatório."],
         ["readonly.", false, "readonly só impede a edição."],
         ["disabled.", false, "disabled desativa o campo e o tira do envio."],
         ["placeholder.", false, "placeholder é apenas uma dica dentro do campo."],
         ["autofocus.", false, "autofocus só coloca o cursor no campo ao abrir a página."]],
        "O atributo required faz o navegador bloquear o envio enquanto o campo estiver vazio, mostrando uma mensagem, sem nenhuma linha de JavaScript. Os demais atributos tratam de edição, envio, dica ou foco."],
      ["Considere o trecho `<img src=\"mapa.png\" alt=\"Mapa das rodovias do estado\">`. O atributo alt serve para:", "FACIL", C3, "e3",
        [["Definir o endereço do arquivo da imagem.", false, "O endereço é definido pelo src."],
         ["Oferecer um texto alternativo, lido por leitores de tela.", true, "Correta: alt descreve a imagem e aparece se ela falhar."],
         ["Definir a largura e a altura da imagem.", false, "Dimensões são width e height."],
         ["Abrir a imagem em uma nova aba ao clicar.", false, "Abrir em nova aba é target=\"_blank\" num link."],
         ["Mostrar uma legenda visível embaixo da imagem.", false, "Legenda visível é figcaption dentro de figure."]],
        "O alt é o texto alternativo: leitores de tela o anunciam e o navegador o mostra se a imagem não carregar. Para imagens apenas decorativas, deve ficar vazio, para ser ignorado."],
      ["Um formulário de busca deve permitir que o usuário copie e compartilhe o endereço com os termos pesquisados. O método adequado é:", "MEDIA", C4, "e4",
        [["post, que envia os dados no corpo da requisição.", false, "Com POST os termos não aparecem na URL."],
         ["get, que envia os dados como parâmetros na URL.", true, "Correta: os termos ficam no endereço e podem ser compartilhados."],
         ["put, que substitui o recurso no servidor.", false, "PUT não é valor de method em formulários HTML."],
         ["head, que envia apenas os cabeçalhos.", false, "HEAD não é usado para enviar formulários."],
         ["dialog, que envia os dados ao servidor por um modal.", false, "method=\"dialog\" só fecha um diálogo, sem enviar ao servidor."]],
        "Com GET, os campos viram parâmetros na URL (por exemplo, ?q=rodovia), o que permite salvar e compartilhar a busca. Para senhas e cadastros usa-se POST, com os dados no corpo da requisição."],
      ["Sobre os elementos de mídia do HTML5, assinale a alternativa CORRETA.", "MEDIA", C3, "e3",
        [["O elemento video exige um plugin instalado no navegador.", false, "video é nativo; não precisa de plugin."],
         ["O atributo controls exibe os botões de reprodução do vídeo.", true, "Correta: mostra play, volume e barra de tempo."],
         ["O elemento track define o arquivo principal do vídeo.", false, "track traz legendas; o arquivo vem em src ou source."],
         ["O atributo poster define o volume inicial do vídeo.", false, "poster é a imagem exibida antes de o vídeo começar."],
         ["O elemento audio aceita apenas arquivos no formato MP3.", false, "audio aceita vários formatos, oferecidos com source."]],
        "O atributo controls exibe os controles nativos do navegador. O vídeo é reproduzido sem plugin, track acrescenta legendas em WebVTT, poster é a imagem de capa, e audio aceita vários formatos."]
    ]
  },
  {
    pasta: "cebraspe", board: "Cebraspe", incidence: "ALTA", estilo: "CEBRASPE",
    howItFalls: "O Cebraspe cobra HTML5 em itens de Certo ou Errado curtos, que juntam um conceito correto a um detalhe trocado. Os alvos preferidos são as generalizações e as confusões entre pares: section e article, strong e b, canvas e SVG, localStorage e sessionStorage, GET e POST, readonly e disabled. Também aparecem itens sobre a finalidade da semântica (acessibilidade e indexação), a validação nativa de formulários e a acessibilidade com alt, label e WCAG. Como um erro anula um acerto, deixe em branco quando não souber o detalhe. Leia o item procurando a palavra que o torna errado (sempre, apenas, obrigatoriamente, dispensa) e, em itens com código, confira o atributo exato.",
    tips: [
      "Palavras como 'dispensa', 'sempre' e 'apenas' costumam marcar o item errado em HTML5.",
      "Em item com código, confira o atributo exato: name, id, for, alt, method.",
      "Validação nativa nunca dispensa a validação no servidor: item que diz o contrário está errado.",
      "Na dúvida real sobre um detalhe, deixe em branco: no Cebraspe o erro desconta um acerto."
    ],
    traps: [
      "O item diz que a validação nativa do HTML5 dispensa validação no servidor. Errado.",
      "O item diz que canvas gera gráficos vetoriais. Errado: canvas é mapa de bits; SVG é vetorial.",
      "O item diz que POST criptografa os dados. Errado: só os tira da URL; quem protege é o HTTPS.",
      "O item diz que o uso de ARIA substitui elementos nativos. Errado: ARIA complementa; o nativo vem primeiro."
    ],
    questoes: [
      ["O uso de elementos semânticos do HTML5, como nav, main e article, favorece a acessibilidade, pois permite que leitores de tela identifiquem as regiões da página.", "FACIL", C2, "e2",
        [["Certo", true], ["Errado", false]],
        "Certo. Elementos semânticos informam o papel de cada região, e leitores de tela permitem pular direto para a navegação, o conteúdo principal ou um artigo. Os buscadores também se beneficiam dessa estrutura."],
      ["A validação nativa de formulários do HTML5, com atributos como required e pattern, torna desnecessária a validação dos dados no servidor.", "MEDIA", C4, "e5",
        [["Certo", false], ["Errado", true]],
        "Errado. A validação no navegador melhora a experiência, mas pode ser desligada ou contornada por quem envia a requisição diretamente. O servidor precisa validar de novo tudo o que recebe."],
      ["Os dados armazenados com localStorage permanecem no navegador mesmo após ele ser fechado, enquanto os de sessionStorage são descartados ao fechar a aba.", "MEDIA", C5, "e6",
        [["Certo", true], ["Errado", false]],
        "Certo. Os dois guardam pares chave-valor por origem, mas o localStorage é persistente e o sessionStorage dura apenas enquanto a aba está aberta."],
      ["Para que leitores de tela anunciem corretamente o rótulo de um campo, o atributo for do elemento label deve conter o mesmo valor do atributo name do campo.", "DIFICIL", C4, "e5",
        [["Certo", false], ["Errado", true]],
        "Errado. O for do label deve corresponder ao id do campo, não ao name. O name define a chave com que o valor é enviado; a ligação do rótulo é feita pelo id."]
    ]
  },
  {
    pasta: "fcc", board: "FCC", incidence: "MEDIA", estilo: "FCC",
    howItFalls: "A FCC cobra HTML em múltipla escolha de cinco alternativas, muitas vezes com um trecho de código para completar ou interpretar. A banca valoriza a sintaxe exata: nome do elemento, nome do atributo e valor válido. Alternativas erradas trazem atributos inexistentes ou trocados, como alt no lugar de title, ou elementos com nome parecido. Os temas recorrentes são formulários (tipos de input, GET e POST, label), tabelas (colspan, rowspan, th), links e imagens, e os elementos semânticos e de mídia do HTML5. Estude decorando os nomes exatos e lendo trechos de código até reconhecer de imediato o que cada linha faz.",
    tips: [
      "Em lacuna de código, a alternativa certa precisa ter elemento e atributo válidos ao mesmo tempo.",
      "Tabela: th é cabeçalho, td é dado, colspan junta colunas e rowspan junta linhas.",
      "Desconfie de atributos com nome 'intuitivo' que não existem, como text, link ou image.",
      "Envio de arquivo: method post e enctype multipart/form-data."
    ],
    traps: [
      "A alternativa usa title para o texto alternativo da imagem. Errado: o texto alternativo é alt.",
      "A alternativa usa rowspan para juntar colunas. Errado: colunas é colspan.",
      "A alternativa diz que o link abre em nova aba com target=\"new\". Errado: o valor é _blank.",
      "A alternativa envia arquivo sem enctype multipart/form-data. Errado: sem ele o arquivo não é enviado."
    ],
    questoes: [
      ["Considere `<a href=\"edital.pdf\" ___>Baixar edital</a>`. Para sugerir ao navegador que baixe o arquivo em vez de abri-lo, a lacuna deve ser preenchida com:", "MEDIA", C3, "e3",
        [["target=\"_blank\"", false], ["rel=\"download\"", false], ["download", true], ["type=\"save\"", false], ["href=\"save\"", false]],
        "Gabarito C. O atributo download indica que o destino deve ser baixado, e pode até sugerir o nome do arquivo. target=\"_blank\" só abre em nova aba; rel=\"download\" e type=\"save\" não têm esse efeito, e href já está definido."],
      ["Em uma tabela, para que a célula de cabeçalho Total ocupe duas linhas, a marcação correta é:", "MEDIA", C2, "e2",
        [["<td colspan=\"2\">Total</td>", false], ["<th rowspan=\"2\">Total</th>", true], ["<th colspan=\"2\">Total</th>", false], ["<tr span=\"2\">Total</tr>", false], ["<caption rows=\"2\">Total</caption>", false]],
        "Gabarito B. th marca célula de cabeçalho, e rowspan=\"2\" faz a célula ocupar duas linhas. colspan juntaria colunas, td é célula de dado, tr é linha e não aceita span, e caption é o título da tabela."],
      ["O tipo de input que exibe um controle deslizante para escolher um número entre um mínimo e um máximo é:", "FACIL", C4, "e4",
        [["type=\"number\"", false], ["type=\"slider\"", false], ["type=\"scroll\"", false], ["type=\"level\"", false], ["type=\"range\"", true]],
        "Gabarito E. type=\"range\" mostra um controle deslizante e aceita min, max e step. number é uma caixa numérica com setas, e slider, scroll e level não existem como tipos de input."]
    ]
  },
  {
    pasta: "fgv", board: "FGV", incidence: "MEDIA", estilo: "FGV",
    howItFalls: "A FGV cobra HTML5 em questões de cinco alternativas com cenários de desenvolvimento: uma necessidade do sistema e a pergunta sobre a marcação mais adequada, ou um trecho de código e a pergunta sobre o comportamento. É comum exigir raciocínio sobre semântica e acessibilidade (qual elemento comunica melhor o papel do conteúdo), sobre formulários (envio, validação, campos que não vão ao servidor) e sobre armazenamento no navegador. As alternativas erradas são plausíveis e longas. Resolva o cenário antes de ler as opções e prefira sempre a solução nativa do HTML à que depende de scripts ou de ARIA.",
    tips: [
      "Leia o cenário e decida a resposta antes de olhar as alternativas.",
      "Entre uma solução nativa (button, label, nav) e uma improvisada (div com ARIA), a nativa tende a ser a correta.",
      "Em formulários, pergunte se o valor precisa ir ao servidor: readonly envia, disabled não.",
      "Armazenamento: persistente (localStorage), por aba (sessionStorage) ou enviado ao servidor (cookie)."
    ],
    traps: [
      "A alternativa resolve um botão com div e onclick. Errado quando o cenário pede acessibilidade: o correto é o button nativo.",
      "A alternativa usa disabled num valor que precisa ser enviado. Errado: o valor some do envio.",
      "A alternativa guarda um carrinho que deve sobreviver ao fechamento do navegador em sessionStorage. Errado: o adequado é localStorage.",
      "A alternativa usa h4 logo após h1 só para ter letra menor. Errado: hierarquia de títulos não é tamanho."
    ],
    questoes: [
      ["Um sistema exibe o número do protocolo em um campo de formulário que o usuário não pode alterar, mas que precisa chegar ao servidor no envio, e deve continuar acessível pelo teclado. A marcação adequada é:", "DIFICIL", C4, "e4",
        [["<input name=\"protocolo\" value=\"123\" disabled>", false], ["<input name=\"protocolo\" value=\"123\" readonly>", true], ["<input id=\"protocolo\" value=\"123\" hidden>", false], ["<span name=\"protocolo\">123</span>", false], ["<input value=\"123\" readonly>", false]],
        "Gabarito B. readonly impede a edição, mantém o foco pelo teclado e envia o valor com a chave protocolo. disabled tiraria o valor do envio e do foco; hidden esconderia o campo; span não é campo de formulário; e o input sem name não seria enviado."],
      ["Uma loja virtual quer que os itens do carrinho continuem salvos no navegador mesmo que o usuário feche e volte ao site dias depois, sem enviar esses dados a cada requisição. O recurso adequado é:", "MEDIA", C5, "e6",
        [["sessionStorage, que guarda os dados por aba.", false], ["Um cookie de sessão, que expira ao fechar o navegador.", false], ["localStorage, que guarda os dados de forma persistente.", true], ["Um atributo data-* no elemento body da página.", false], ["Um Web Worker rodando em segundo plano.", false]],
        "Gabarito C. localStorage guarda os dados de forma persistente na origem do site e não os envia nas requisições. sessionStorage some ao fechar a aba, o cookie de sessão expira e ainda vai ao servidor, data-* não sobrevive ao recarregar e Web Workers não armazenam dados."],
      ["Uma equipe criou um menu clicável com `<div class=\"botao\" onclick=\"abrir()\">Menu</div>`, e usuários de teclado não conseguem acioná-lo. A correção mais adequada é:", "DIFICIL", C5, "e6",
        [["Acrescentar aria-hidden=\"true\" à div.", false], ["Trocar a div por um elemento span.", false], ["Aumentar o tamanho da fonte da div.", false], ["Usar `<button type=\"button\" onclick=\"abrir()\">Menu</button>`.", true], ["Mover a div para dentro do elemento footer.", false]],
        "Gabarito D. O button nativo já recebe foco, responde ao Enter e ao Espaço e é anunciado como botão pelos leitores de tela; type=\"button\" evita que ele envie formulários. aria-hidden esconderia o controle, span tem o mesmo problema da div, e fonte ou posição não resolvem o acesso pelo teclado."]
    ]
  },
  {
    pasta: "cesgranrio", board: "Cesgranrio", incidence: "MEDIA", estilo: "CESGRANRIO",
    howItFalls: "A Cesgranrio cobra HTML5 em múltipla escolha de cinco alternativas, com enunciados que descrevem uma necessidade da página e pedem o elemento ou atributo que a atende. O foco é aplicado: escolher entre elementos parecidos e saber o efeito de cada atributo. As alternativas erradas costumam ser elementos reais, mas com outro propósito, o que exige conhecer a função de cada um. Estude a correspondência entre necessidade e marcação: estruturar a página, mostrar mídia, coletar dados, guardar informação no navegador e tornar o conteúdo acessível.",
    tips: [
      "Traduza o enunciado em necessidade: estruturar, exibir mídia, coletar dados, guardar dados ou dar acessibilidade.",
      "Alternativas reais com outro propósito são a armadilha principal: pergunte se aquilo resolve o que foi pedido.",
      "Para agrupar campos relacionados de um formulário com um título, pense em fieldset e legend.",
      "Para sugestões enquanto o usuário digita, sem limitar a escolha, pense em datalist."
    ],
    traps: [
      "A alternativa usa select para sugerir valores sem limitar a digitação. Errado: select limita às opções; datalist apenas sugere.",
      "A alternativa usa figcaption como título de tabela. Errado: título de tabela é caption.",
      "A alternativa usa iframe para exibir um vídeo do próprio site com controles nativos. Errado: o adequado é video.",
      "A alternativa usa legend fora de fieldset para rotular um campo. Errado: o campo é rotulado por label."
    ],
    questoes: [
      ["Um formulário de endereço tem os campos de rua, número e cidade, que devem ser agrupados visual e semanticamente sob o título Endereço. A marcação adequada é:", "MEDIA", C4, "e5",
        [["fieldset com legend Endereço envolvendo os campos.", true], ["section com caption Endereço envolvendo os campos.", false], ["div com title Endereço envolvendo os campos.", false], ["table com th Endereço envolvendo os campos.", false], ["label Endereço envolvendo os três campos juntos.", false]],
        "Gabarito A. fieldset agrupa campos relacionados e legend dá o título do grupo, que leitores de tela anunciam junto com os campos. caption é título de tabela, title é só dica do mouse, table é para dados e um label deve rotular um único campo."],
      ["Um campo de cidade deve sugerir nomes enquanto o usuário digita, mas permitir digitar uma cidade que não esteja na lista. O recurso adequado é:", "DIFICIL", C4, "e5",
        [["select com option para cada cidade.", false], ["input com list ligado a um datalist.", true], ["textarea com placeholder listando as cidades.", false], ["input type=\"radio\" para cada cidade.", false], ["input type=\"search\" com o atributo pattern.", false]],
        "Gabarito B. datalist oferece sugestões a um input ligado pelo atributo list, sem impedir outro valor. select limita às opções existentes, placeholder é só dica, radio obriga a escolher uma opção da lista e pattern apenas valida o formato."],
      ["Um curso online quer exibir as aulas em vídeo hospedadas no próprio servidor, com botões de reprodução e legendas em português. A marcação adequada envolve:", "MEDIA", C3, "e3",
        [["O elemento iframe com um arquivo .vtt como src.", false], ["O elemento canvas desenhando cada quadro do vídeo.", false], ["O elemento audio com o atributo poster.", false], ["O elemento object com um plugin de vídeo.", false], ["O elemento video com controls, source e track.", true]],
        "Gabarito E. video com controls mostra os botões nativos, source aponta o arquivo e track liga a legenda em WebVTT. iframe incorpora outra página, canvas é desenho por script, audio não tem poster nem vídeo, e object com plugin é a forma antiga, anterior ao HTML5."]
    ]
  },
  {
    pasta: "ibfc", board: "IBFC", incidence: "BAIXA", estilo: "IBFC",
    howItFalls: "O IBFC cobra HTML de forma básica e conceitual, em múltipla escolha, pedindo definições e a identificação da afirmativa correta. São frequentes as perguntas sobre o significado da sigla, a estrutura do documento, a função de elementos comuns (títulos, parágrafos, listas, links e imagens) e o que o HTML5 trouxe de novo. Em editais de nível superior aparecem formulários e acessibilidade. Prepare definições curtas e precisas e a lista dos elementos mais usados com a sua função, porque as alternativas erradas trocam termos parecidos.",
    tips: [
      "HTML descreve a estrutura; CSS, a aparência; JavaScript, o comportamento.",
      "Saiba o que cada elemento básico faz: h1 a h6, p, a, img, ul, ol, li, table.",
      "Lembre o que veio com o HTML5: elementos semânticos, áudio e vídeo nativos, canvas e novos tipos de input.",
      "Hiperlink usa o elemento a com o atributo href."
    ],
    traps: [
      "A alternativa expande HTML como High Text Machine Language. Errado: é HyperText Markup Language.",
      "A alternativa diz que o elemento img usa href para o arquivo. Errado: usa src; href é do link.",
      "A alternativa diz que ul cria lista numerada. Errado: numerada é ol; ul usa marcadores.",
      "A alternativa diz que o HTML define cores e fontes. Errado: aparência é papel do CSS."
    ],
    questoes: [
      ["A sigla HTML significa:", "FACIL", C1, "e1",
        [["High Text Machine Language.", false], ["HyperText Markup Language.", true], ["Hyperlink and Text Management Logic.", false], ["Home Tool Markup Language.", false], ["Hyper Transfer Media Language.", false]],
        "Gabarito B. HTML significa HyperText Markup Language, linguagem de marcação de hipertexto. As outras expansões não existem; a confusão com HTTP (HyperText Transfer Protocol) é comum, mas esse é o protocolo de transferência."],
      ["Para criar um link para a página contato.html, a marcação correta é:", "FACIL", C3, "e3",
        [["<link src=\"contato.html\">Contato</link>", false], ["<a src=\"contato.html\">Contato</a>", false], ["<a href=\"contato.html\">Contato</a>", true], ["<href a=\"contato.html\">Contato</href>", false], ["<url to=\"contato.html\">Contato</url>", false]],
        "Gabarito C. O link usa o elemento a, com o destino no atributo href. O elemento link fica no head e liga recursos como folhas de estilo; src é usado por img, script e mídia; href e url não são elementos."],
      ["Sobre listas em HTML, assinale a alternativa correta.", "FACIL", C2, "e2",
        [["ul cria uma lista numerada.", false], ["ol cria uma lista com marcadores.", false], ["li só pode ser usado dentro de tabelas.", false], ["ol cria uma lista ordenada, numerada.", true], ["dl cria uma lista de links.", false]],
        "Gabarito D. ol é a lista ordenada, numerada; ul é a lista sem ordem, com marcadores; li é o item das duas; e dl é a lista de termos e definições, não de links."]
    ]
  },
];
