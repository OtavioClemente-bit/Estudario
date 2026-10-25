// Recortes "como a banca cobra" da matéria informatica.css3, escritos à mão.
// montar.mjs só acrescenta os campos fixos e grava um arquivo por banca em conteudo/recortes/.
const C1 = "1. O que é CSS e como ele chega à página";
const C2 = "2. Seletores, cascata, especificidade e herança";
const C3 = "3. Modelo de caixa, display e posicionamento";
const C4 = "4. Layout moderno: Flexbox e Grid";
const C5 = "5. Responsividade e recursos visuais modernos";

// [enunciado, dificuldade, capítulo, conceito, alternativas [texto, certa?][], explicação]
export const recortes = [
  {
    pasta: "geral", board: "GERAL", incidence: "ALTA", estilo: "GERAL",
    howItFalls: "Nas provas de tecnologia, CSS aparece quase sempre ao lado de HTML, em questões conceituais curtas, e raramente pede que o candidato escreva código. O que mais se cobra é saber qual regra vence: a especificidade dos seletores (ID, classe, elemento), a ordem de aparição e o efeito do !important. Em seguida vêm o modelo de caixa (conteúdo, padding, borda e margem, com a conta do box-sizing), a diferença entre display none e visibility hidden, e os valores de position, principalmente a referência do absolute. Editais mais recentes trazem Flexbox, Grid e responsividade: espere perguntas sobre eixo principal e transversal, unidade fr, media queries com min-width e max-width e a meta viewport. Unidades relativas (em, rem, %, vw) entram como pegadinha de cálculo. A forma de estudar é montar o placar de especificidade sem pressa, desenhar a caixa com números e decorar o que cada propriedade de Flexbox faz em cada eixo.",
    tips: [
      "Questão de 'qual cor aparece' é sempre cascata: confira especificidade antes de olhar a ordem das regras.",
      "Em cálculo de largura, procure o box-sizing no enunciado; sem ele, vale content-box e padding e borda somam por fora.",
      "Se a alternativa fala em 'eixo', identifique o flex-direction antes de decidir entre justify-content e align-items.",
      "Unidade relativa em prova: em olha o pai, rem olha a raiz, % olha o bloco que contém, vw e vh olham a janela."
    ],
    traps: [
      "A alternativa diz que a folha externa sempre vence a interna. Errado: com especificidade igual, vence a regra que aparece por último.",
      "A alternativa soma as margens verticais de dois parágrafos. Errado: elas colapsam e fica só a maior.",
      "A alternativa afirma que Grid é unidimensional. Errado: Grid trabalha linhas e colunas; Flexbox é que é unidimensional.",
      "A alternativa coloca a meta viewport dentro do CSS. Errado: ela é um elemento meta do HTML, no head."
    ],
    questoes: [
      ["Uma página aplica `.botao { color: white; }` e `button.botao { color: yellow; }` ao mesmo botão, nessa ordem. Qual cor o texto exibe e por quê?", "MEDIA", C2, "e1",
        [["Branco, porque a primeira regra declarada tem prioridade.", false], ["Amarelo, porque button.botao soma classe e elemento.", true], ["Branco, porque a classe sozinha é mais específica que o elemento.", false], ["Amarelo, porque elementos sempre vencem classes.", false], ["Nenhuma, porque regras em conflito se anulam.", false]],
        "Gabarito B. .botao tem placar (0,0,1,0) e button.botao tem (0,0,1,1): a mesma classe mais um elemento. A segunda é mais específica e vence, independentemente da ordem. Elementos sozinhos perdem para classes, e regras em conflito não se anulam: uma delas sempre prevalece."],
      ["Em um layout com `display: flex` e `flex-direction: row`, qual combinação centraliza os itens na horizontal e na vertical?", "FACIL", C4, "e5",
        [["justify-content: center e align-items: center.", true], ["text-align: center e vertical-align: middle.", false], ["align-content: center e flex-wrap: wrap.", false], ["margin: center e padding: center.", false], ["order: center e flex-basis: center.", false]],
        "Gabarito A. Em row, justify-content atua no eixo principal (horizontal) e align-items no transversal (vertical); com center nos dois, o conteúdo fica no meio da caixa. text-align e vertical-align não valem para itens flex, align-content distribui linhas quebradas, e margin, padding, order e flex-basis não aceitam center."],
      ["Com `box-sizing: border-box`, um elemento com `width: 300px`, `padding: 15px` e `border: 5px` ocupa 340px de largura.", "MEDIA", C3, "e3",
        [["Certo", false], ["Errado", true]],
        "Errado. Com border-box, o width já inclui padding e borda, então o elemento ocupa 300px e o conteúdo encolhe para 260px. Os 340px seriam o resultado com content-box, o padrão, em que 15 + 15 de padding e 5 + 5 de borda somam por fora."],
      ["A abordagem mobile-first escreve primeiro os estilos para telas pequenas e usa media queries com min-width para acrescentar regras em telas maiores.", "FACIL", C5, "e6",
        [["Certo", true], ["Errado", false]],
        "Certo. No mobile-first, o estilo base é o do celular e as media queries com min-width valem a partir de certa largura, acrescentando colunas e ajustes para tablets e computadores. O desktop-first faz o caminho inverso, com max-width."]
    ]
  },
  {
    pasta: "fumarc", board: "FUMARC", incidence: "MEDIA", estilo: "FUMARC",
    howItFalls: "A FUMARC usa múltipla escolha com quatro alternativas (A a D) e enunciados objetivos, muitas vezes pedindo a afirmativa correta ou a incorreta entre quatro frases sobre o mesmo assunto. Em CSS, o padrão esperado é conceitual: função de cada propriedade, diferença entre conceitos próximos e qual regra prevalece, sem código extenso. Alternativas erradas costumam trocar funções vizinhas, como atribuir ao padding o papel da margem, ao display none o comportamento do visibility hidden ou ao seletor de classe o símbolo do ID. Por haver menos questões públicas específicas de CSS desta banca, estude o núcleo com segurança: sintaxe da regra, formas de inclusão, seletores (#, ., combinadores), especificidade, modelo de caixa e position. Depois avance para Flexbox, Grid e media queries, que aparecem nos editais mais novos. Leia com atenção os enunciados que pedem a alternativa INCORRETA, comuns nesta banca.",
    tips: [
      "Em 'assinale a INCORRETA', marque ao lado de cada frase se ela está certa; a resposta é a única que você riscou.",
      "Associe os símbolos sem hesitar: # é ID, ponto é classe, > é filho direto, espaço é descendente.",
      "Se duas alternativas parecem certas, uma delas provavelmente troca padding (dentro da borda) por margin (fora da borda).",
      "Em questões de position, procure a palavra 'referência': relative, absolute e fixed se distinguem por ela."
    ],
    traps: [
      "A alternativa diz que o seletor .menu seleciona o elemento de id menu. Errado: o ponto indica classe; o ID usa #.",
      "A alternativa afirma que margin é o espaço interno entre conteúdo e borda. Errado: esse é o padding; a margin fica fora da borda.",
      "A alternativa diz que display: none mantém o espaço do elemento. Errado: quem mantém é visibility: hidden.",
      "A alternativa afirma que comentários em CSS usam <!-- -->. Errado: em CSS o comentário é /* */."
    ],
    questoes: [
      // Cinco alternativas, cada uma com comentário: no pedido "4 alternativas" (o formato da FUMARC)
      // o servidor tira uma errada e refaz as letras; por isso as explicações não citam letras.
      ["Sobre seletores CSS, assinale a alternativa CORRETA.", "FACIL", C2, "e1",
        [["O seletor #topo seleciona os elementos com a classe topo.", false, "O # indica ID; a classe topo seria selecionada com .topo."],
         ["O seletor .aviso seleciona os elementos com a classe aviso.", true, "Correta: o ponto indica classe, então pega todos os elementos com class=\"aviso\"."],
         ["O seletor div p seleciona apenas os p filhos diretos de div.", false, "O espaço pega descendentes em qualquer nível; só filhos diretos seria div > p."],
         ["O seletor h1, h2 seleciona apenas os h2 dentro de h1.", false, "A vírgula agrupa: seleciona todos os h1 e todos os h2, independentes."],
         ["O seletor * aumenta a especificidade da regra em que aparece.", false, "O seletor universal vale zero na especificidade."]],
        "O ponto marca classe e o # marca ID. Os combinadores mudam a relação: espaço é descendente em qualquer nível, > é filho direto, e a vírgula apenas junta seletores independentes na mesma regra."],
      ["Em relação ao modelo de caixa do CSS, assinale a alternativa INCORRETA.", "MEDIA", C3, "e3",
        [["O padding é o espaço interno entre o conteúdo e a borda.", false, "Afirmação correta: o padding fica dentro da borda e recebe a cor de fundo."],
         ["A margin é o espaço externo, fora da borda do elemento.", false, "Afirmação correta: a margem separa a caixa das vizinhas."],
         ["No padrão content-box, o width não inclui padding nem borda.", false, "Afirmação correta: no content-box padding e borda são somados por fora."],
         ["A margin recebe a cor de fundo definida em background.", true, "É a incorreta: a margem é sempre transparente; o fundo vai só até a borda."],
         ["Com border-box, o width passa a incluir padding e borda.", false, "Afirmação correta: no border-box o conteúdo encolhe para caber no width."]],
        "A cor de fundo pinta o conteúdo e o padding, até a borda, e a margem fica sempre transparente. As demais afirmações descrevem corretamente o padding, a margem e as duas formas de calcular a largura (content-box e border-box)."],
      ["Um desenvolvedor quer que um aviso suma da tela sem que os elementos de baixo subam para ocupar o lugar. A declaração adequada é:", "FACIL", C3, "e4",
        [["display: none.", false, "Remove o elemento do fluxo, e os elementos de baixo sobem."],
         ["position: absolute.", false, "Tira o elemento do fluxo, e o espaço dele é liberado."],
         ["visibility: hidden.", true, "Correta: esconde o elemento e mantém o espaço reservado."],
         ["height: auto.", false, "Só faz a altura acompanhar o conteúdo; não esconde nada."],
         ["display: inline.", false, "Muda o comportamento na linha, mas o aviso continua visível."]],
        "visibility: hidden é a única opção que esconde o aviso e mantém o buraco onde ele estava. display: none e position: absolute tiram o elemento do fluxo, e as outras declarações não escondem o conteúdo."],
      ["Considere as regras `p { color: blue; }` e `#texto { color: green; }`, aplicadas ao parágrafo `<p id=\"texto\">`. A cor exibida será:", "FACIL", C2, "e1",
        [["Azul, porque a regra do elemento p foi escrita primeiro.", false, "A ordem só desempata quando a especificidade é igual."],
         ["Verde, porque o seletor de ID é mais específico.", true, "Correta: ID (0,1,0,0) vence elemento (0,0,0,1)."],
         ["Azul, porque seletores de elemento têm prioridade.", false, "É o contrário: elemento tem a menor especificidade."],
         ["Preta, porque regras em conflito voltam ao padrão.", false, "Regras em conflito não se anulam; uma delas sempre prevalece."],
         ["Verde-azulado, porque as duas cores se combinam.", false, "Cores de regras diferentes não se misturam; vale uma só."]],
        "O seletor de ID é mais específico que o de elemento, então o verde prevalece. A ordem de escrita só decide quando a especificidade empata, e o CSS nunca mistura nem anula declarações em conflito."],
      ["Sobre o posicionamento com a propriedade position, assinale a alternativa CORRETA.", "MEDIA", C3, "e4",
        [["Com relative, o elemento sai do fluxo e libera o seu espaço.", false, "relative mantém o espaço original; só desloca o desenho."],
         ["Com fixed, o elemento se posiciona em relação à janela.", true, "Correta: fixed usa a viewport e fica parado ao rolar."],
         ["Com static, as propriedades top e left deslocam o elemento.", false, "Em static, top e left são ignorados."],
         ["Com absolute, a referência é sempre o elemento body.", false, "A referência é o ancestral posicionado mais próximo."],
         ["Com sticky, o elemento fica fixo desde o carregamento da página.", false, "sticky só gruda depois de atingir o limite de rolagem."]],
        "Cada valor de position tem uma referência própria: relative usa a posição original, absolute o ancestral posicionado, fixed a janela, e sticky alterna entre relative e fixed conforme a rolagem."],
      ["Sobre o Flexbox, assinale a alternativa CORRETA.", "MEDIA", C4, "e5",
        [["É um modelo bidimensional que controla linhas e colunas.", false, "Flexbox é unidimensional; o bidimensional é o Grid."],
         ["A propriedade justify-content atua no eixo principal.", true, "Correta: o eixo principal é definido por flex-direction."],
         ["O valor padrão de flex-direction é column.", false, "O padrão é row, horizontal."],
         ["A propriedade order altera a ordem dos elementos no HTML.", false, "order muda só a ordem visual; o HTML continua igual."],
         ["O valor padrão de flex-wrap é wrap.", false, "O padrão é nowrap: tudo numa linha só."]],
        "No Flexbox, justify-content trabalha no eixo principal e align-items no transversal. O modelo organiza uma dimensão por vez, começa em row e nowrap por padrão, e order não mexe no código HTML."],
      ["Para que o conteúdo de uma página se adapte à largura de celulares, é necessário, no HTML, o elemento:", "FACIL", C5, "e6",
        [["<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">.", true, "Correta: faz o celular usar a largura real do aparelho."],
         ["<link rel=\"responsive\" href=\"mobile.css\">.", false, "Não existe rel=\"responsive\"; folhas usam rel=\"stylesheet\"."],
         ["<style media=\"phone\"> com as regras do celular.", false, "Não existe o tipo de mídia phone."],
         ["<meta name=\"mobile\" content=\"true\">.", false, "Essa meta não tem efeito no layout."],
         ["<body responsive=\"true\">.", false, "Não existe atributo responsive no body."]],
        "Sem a meta viewport com width=device-width, o navegador do celular desenha a página como num monitor largo e a reduz, e as media queries não enxergam a largura real. As outras marcações não existem com essa função."],
      ["Em uma folha de estilo, a unidade rem é calculada em relação:", "MEDIA", C1, "e6",
        [["Ao tamanho da fonte do elemento-pai.", false, "Essa é a unidade em, quando usada em font-size."],
         ["À largura da janela do navegador.", false, "Relativas à janela são vw e vh."],
         ["Ao tamanho da fonte do elemento raiz (html).", true, "Correta: rem significa root em."],
         ["À resolução em pixels da tela do aparelho.", false, "Nenhuma dessas unidades usa a resolução física."],
         ["À largura do bloco que contém o elemento.", false, "Relativa ao bloco que contém é a porcentagem."]],
        "rem toma o font-size do elemento html e, por isso, não se acumula em elementos aninhados. em depende do pai, a porcentagem do bloco que contém, e vw e vh da janela do navegador."]
    ]
  },
  {
    pasta: "cebraspe", board: "Cebraspe", incidence: "ALTA", estilo: "CEBRASPE",
    howItFalls: "O Cebraspe cobra CSS em itens de Certo ou Errado, quase sempre uma afirmação técnica curta que mistura um conceito correto com um detalhe trocado. Como um erro anula um acerto, vale mais deixar em branco do que chutar quando o detalhe é desconhecido. Os alvos preferidos são as generalizações: dizer que um seletor sempre vence, que uma propriedade sempre herda, que absolute se posiciona sempre em relação à página, ou que Flexbox e Grid fazem a mesma coisa. Também aparecem itens sobre a função de media queries, a separação entre conteúdo (HTML) e apresentação (CSS), o efeito de !important e o cálculo de especificidade. Leia cada item procurando a palavra que o torna errado: sempre, apenas, nunca, todos. Itens longos com um trecho de código pedem atenção ao símbolo usado no seletor.",
    tips: [
      "Palavras absolutas (sempre, apenas, nunca) costumam marcar o item errado em CSS; procure a exceção.",
      "Item com código: confira primeiro os símbolos do seletor (#, ., >, espaço) antes de julgar a frase.",
      "Na dúvida real sobre um detalhe, deixe em branco: no Cebraspe o erro desconta um acerto.",
      "Separe os papéis: HTML estrutura, CSS apresenta, JavaScript dá comportamento. Itens que trocam isso estão errados."
    ],
    traps: [
      "O item diz que propriedades CSS são sempre herdadas pelos filhos. Errado: margin, padding, border e background não herdam.",
      "O item diz que position absolute sempre toma a página como referência. Errado: a referência é o ancestral posicionado mais próximo.",
      "O item diz que o seletor universal aumenta a especificidade. Errado: ele vale zero.",
      "O item diz que media queries só testam a largura da tela. Errado: também testam orientação, tipo de mídia e preferências como tema escuro."
    ],
    questoes: [
      ["Em CSS, a declaração !important faz com que uma propriedade prevaleça sobre declarações normais conflitantes, ainda que estas tenham seletores mais específicos.", "MEDIA", C2, "e1",
        [["Certo", true], ["Errado", false]],
        "Certo. Na cascata, declarações marcadas com !important passam à frente das normais antes mesmo de se comparar a especificidade. Entre duas declarações importantes, aí sim a especificidade e a ordem voltam a decidir."],
      ["As media queries do CSS restringem-se a verificar a largura da janela do navegador, não sendo possível testar outras características do dispositivo.", "MEDIA", C5, "e6",
        [["Certo", false], ["Errado", true]],
        "Errado. Além da largura, media queries testam altura, orientação (portrait ou landscape), tipo de mídia, como print, e preferências do usuário, como prefers-color-scheme para o tema escuro e prefers-reduced-motion para reduzir animações."],
      ["Considere o trecho `nav > a { color: red; }` e um menu em que os links estão dentro de itens li de uma lista colocada no nav. Essa regra deixa vermelhos todos esses links.", "DIFICIL", C2, "e1",
        [["Certo", false], ["Errado", true]],
        "Errado. O combinador > seleciona apenas os filhos diretos de nav. Os links que estão dentro de li, dentro de ul, são descendentes de nav, mas não filhos diretos, e não são atingidos. Para alcançá-los em qualquer nível, o seletor seria nav a, com espaço."],
      ["Um elemento com position absolute é posicionado em relação ao seu ancestral posicionado mais próximo, isto é, ao primeiro ancestral cuja propriedade position seja diferente de static.", "MEDIA", C3, "e4",
        [["Certo", true], ["Errado", false]],
        "Certo. absolute retira o elemento do fluxo e usa como referência o ancestral posicionado mais próximo (relative, absolute, fixed ou sticky). Se nenhum ancestral estiver posicionado, a referência passa a ser o bloco inicial da página."],
      ["No CSS Grid, a unidade fr representa uma fração do espaço livre disponível no contêiner, permitindo distribuir colunas de forma proporcional.", "FACIL", C4, "e5",
        [["Certo", true], ["Errado", false]],
        "Certo. Em grid-template-columns: 1fr 2fr, o espaço livre é dividido em três partes, e a segunda coluna fica com o dobro da primeira. A unidade fr é própria do Grid e trabalha sobre o espaço que sobra depois das trilhas de tamanho fixo."],
      ["O seletor de ID possui especificidade maior que a de qualquer combinação de seletores de classe, de modo que dez classes encadeadas não superam um único ID.", "DIFICIL", C2, "e1",
        [["Certo", true], ["Errado", false]],
        "Certo. A especificidade é comparada casa a casa: a dos IDs vem antes da das classes, sem transbordo. Assim, (0,1,0,0) vence (0,0,10,0). Só um estilo em linha ou uma declaração !important passaria à frente do ID."]
    ]
  },
  {
    pasta: "fcc", board: "FCC", incidence: "MEDIA", estilo: "FCC",
    howItFalls: "A FCC cobra CSS em múltipla escolha com cinco alternativas, em geral com um pequeno trecho de código e a pergunta sobre o efeito produzido ou sobre a propriedade que completa a lacuna. É uma banca que valoriza a sintaxe exata: nome correto da propriedade, valor válido e símbolo certo do seletor. Por isso, alternativas erradas costumam trazer propriedades que não existem ou estão escritas de outro jeito, como text-color no lugar de color, ou valores inválidos para a propriedade. Os temas recorrentes são seletores e especificidade, modelo de caixa, display, position, unidades e, nos editais recentes, Flexbox e media queries. Estude decorando os nomes exatos das propriedades mais comuns e o que cada valor faz, e treine ler um bloco de CSS e prever o resultado na tela.",
    tips: [
      "Desconfie de propriedades com nome 'intuitivo' que não existem, como text-color ou font-colour.",
      "Em lacuna de código, a alternativa certa precisa ter propriedade e valor válidos juntos; cheque os dois.",
      "Lembre a ordem dos atalhos de quatro valores: topo, direita, baixo, esquerda (sentido horário).",
      "Em Flexbox, decore os nomes completos: justify-content, align-items, flex-direction, flex-wrap."
    ],
    traps: [
      "A alternativa usa text-color: red para colorir o texto. Errado: a propriedade é color.",
      "A alternativa lê margin: 10px 20px como 10px nas laterais. Errado: o primeiro valor é topo e base; o segundo, as laterais.",
      "A alternativa diz que inline aceita width e height. Errado: quem aceita, sem quebrar a linha, é inline-block.",
      "A alternativa usa @media (width-max: 600px). Errado: a sintaxe é max-width."
    ],
    questoes: [
      ["Para deixar vermelho o texto de todos os parágrafos de uma página, a regra CSS correta é:", "FACIL", C1, "e1",
        [["p { text-color: red; }", false], ["p { font-color: red; }", false], ["#p { color: red; }", false], ["p { color: red; }", true], [".p { colour: red; }", false]],
        "Gabarito D. A propriedade que define a cor do texto é color, e o seletor de tipo p pega todos os parágrafos. text-color e font-color não existem, colour é a grafia britânica não aceita, e #p e .p selecionariam um ID e uma classe chamados p, não os parágrafos."],
      ["Considere a declaração `margin: 5px 10px 15px 20px;`. A margem esquerda do elemento será de:", "MEDIA", C3, "e3",
        [["5px.", false], ["10px.", false], ["15px.", false], ["20px.", true], ["50px.", false]],
        "Gabarito D. Com quatro valores, a ordem é horária a partir do topo: topo 5px, direita 10px, base 15px e esquerda 20px. Os valores não se somam, por isso 50px não faz sentido; 5, 10 e 15 são as outras três margens."],
      ["Um contêiner deve exibir seus filhos em uma linha horizontal, com espaço igual entre eles e sem espaço nas pontas. As declarações adequadas são:", "MEDIA", C4, "e5",
        [["display: flex; justify-content: space-between;", true], ["display: block; text-align: justify;", false], ["display: grid; align-items: space-between;", false], ["display: inline; margin: auto;", false], ["display: flex; align-items: space-around;", false]],
        "Gabarito A. Com display: flex, os filhos ficam em linha (row é o padrão), e space-between distribui o espaço livre entre os itens, deixando o primeiro e o último encostados nas pontas. space-around poria espaço também nas pontas e, além disso, align-items não aceita esses valores. block com text-align alinha texto, não caixas."],
      ["Em `<link rel=\"stylesheet\" href=\"site.css\">`, o elemento link é usado para:", "FACIL", C1, "e1",
        [["Inserir uma regra CSS em linha no próprio elemento.", false], ["Criar um hiperlink para outra página do site.", false], ["Importar um script que aplica os estilos via JavaScript.", false], ["Referenciar uma folha de estilo externa ao documento.", true], ["Declarar uma variável CSS global para a página.", false]],
        "Gabarito D. link com rel=\"stylesheet\" carrega uma folha de estilo externa, que pode ser compartilhada por várias páginas e guardada em cache. Estilo em linha usa o atributo style, hiperlinks usam o elemento a, scripts usam o elemento script e variáveis CSS são declaradas com --nome."],
      ["A media query que aplica estilos somente quando a janela tem até 600px de largura é:", "MEDIA", C5, "e6",
        [["@media (min-width: 600px) { … }", false], ["@media (max-width: 600px) { … }", true], ["@media (width-max: 600px) { … }", false], ["@media screen-600 { … }", false], ["@viewport (600px) { … }", false]],
        "Gabarito B. max-width: 600px significa 'até 600px de largura'. min-width faria o contrário, valendo a partir de 600px; width-max e screen-600 não são sintaxes válidas, e @viewport não é a forma de escrever media queries."]
    ]
  },
  {
    pasta: "fgv", board: "FGV", incidence: "MEDIA", estilo: "FGV",
    howItFalls: "A FGV costuma cobrar CSS em questões de cinco alternativas contextualizadas: um cenário de desenvolvimento, um trecho de código e a pergunta sobre o resultado ou sobre a melhor solução. É comum exigir raciocínio sobre a cascata com várias regras ao mesmo tempo, o cálculo do tamanho final de uma caixa e o comportamento de Flexbox e Grid em situações concretas. As alternativas erradas são plausíveis e longas, cada uma com uma justificativa técnica que parece correta. Por isso, resolva o caso antes de ler as opções: calcule a especificidade, desenhe a caixa com os números ou identifique o eixo do Flexbox. Os editais mais recentes da banca para TI incluem responsividade, acessibilidade e boas práticas de front-end, então espere perguntas sobre mobile-first e unidades relativas.",
    tips: [
      "Resolva o cenário no rascunho antes de ler as alternativas; as erradas da FGV são convincentes.",
      "Em várias regras sobre o mesmo elemento, liste o placar de cada uma e só depois aplique a ordem.",
      "Em questões de largura, desenhe a caixa: conteúdo, padding, borda e margem, com os números do enunciado.",
      "Quando o cenário fala em 'manutenção' ou 'reaproveitamento', a resposta tende a envolver folha externa, classes ou variáveis CSS."
    ],
    traps: [
      "A alternativa justifica a cor final pela ordem das regras, ignorando que a primeira tem um ID. Errado: especificidade vem antes da ordem.",
      "A alternativa soma a margem ao width com border-box. Errado: a margem nunca entra no width; com border-box só padding e borda entram.",
      "A alternativa diz que z-index alto sempre traz o elemento à frente. Errado: ele só compete dentro do mesmo contexto de empilhamento.",
      "A alternativa usa em e rem como sinônimos. Errado: em depende do pai, rem da raiz."
    ],
    questoes: [
      ["Uma equipe tem as regras `#menu .item { color: red; }`, `.menu-lateral .item { color: blue; }` e `li.item { color: green; }`, todas atingindo o mesmo elemento, nessa ordem. O texto aparece:", "DIFICIL", C2, "e1",
        [["Verde, pois é a última regra declarada.", false], ["Azul, pois combina duas classes.", false], ["Vermelho, pois a regra com ID tem maior especificidade.", true], ["Verde, pois combina elemento e classe.", false], ["Azul, pois classes encadeadas somam mais que um ID.", false]],
        "Gabarito C. Os placares são (0,1,1,0) para #menu .item, (0,0,2,0) para .menu-lateral .item e (0,0,1,1) para li.item. A regra com ID vence, pois a casa dos IDs é comparada primeiro. A ordem só desempataria placares iguais, e duas classes não superam um ID."],
      ["Um card tem `box-sizing: border-box`, `width: 320px`, `padding: 16px`, `border: 2px solid` e `margin: 10px`. O espaço horizontal total que ele ocupa na linha, incluindo margens, é:", "DIFICIL", C3, "e3",
        [["320px.", false], ["340px.", true], ["356px.", false], ["376px.", false], ["388px.", false]],
        "Gabarito B. Com border-box, a caixa visível tem 320px, já incluindo padding e borda. Somando as margens dos dois lados (10 + 10), o espaço ocupado é 340px. Com content-box, a caixa teria 356px (320 + 32 de padding + 4 de borda) e ocuparia 376px com as margens; 320px esquece as margens."],
      ["Em uma galeria, deseja-se que o número de colunas se ajuste sozinho à largura da tela, com cada coluna tendo no mínimo 180px, sem usar media queries. A solução mais adequada é:", "DIFICIL", C4, "e5",
        [["grid-template-columns: repeat(4, 180px);", false], ["grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));", true], ["display: flex; flex-direction: column; width: 180px;", false], ["grid-template-columns: 180px 1fr;", false], ["display: inline-block; width: 25%;", false]],
        "Gabarito B. auto-fit cria quantas colunas couberem e minmax(180px, 1fr) garante o mínimo de 180px, deixando as colunas crescerem para ocupar o espaço. repeat(4, 180px) fixa quatro colunas, column empilha os itens, 180px 1fr fixa duas colunas e inline-block com 25% fixa quatro por linha."],
      ["Ao criar estilos que se adaptem a quem aumenta o tamanho padrão da fonte no navegador, sem efeito cascata em elementos aninhados, a unidade mais indicada para font-size é:", "MEDIA", C1, "e6",
        [["px, por ser uma medida fixa e previsível.", false], ["em, por acompanhar o tamanho do elemento-pai.", false], ["rem, por depender só do tamanho da raiz.", true], ["vw, por acompanhar a largura da janela.", false], ["pt, por ser a unidade usada na impressão.", false]],
        "Gabarito C. rem acompanha o font-size da raiz, que reflete a preferência do usuário, e não se multiplica em elementos aninhados. px ignora a preferência, em se acumula de pai para filho, vw depende da largura da janela e não da fonte escolhida, e pt é uma unidade de impressão."],
      ["Um modal tem `position: fixed; z-index: 1000` e mesmo assim aparece atrás do cabeçalho da página, que tem z-index 10. Uma explicação compatível é:", "DIFICIL", C3, "e4",
        [["O z-index só funciona com display: block.", false], ["O modal está dentro de um ancestral que cria um contexto de empilhamento abaixo do cabeçalho.", true], ["Valores de z-index acima de 999 são ignorados pelo navegador.", false], ["position: fixed impede o uso de z-index.", false], ["O cabeçalho vence porque foi declarado antes no HTML.", false]],
        "Gabarito B. O z-index compete dentro do contexto de empilhamento. Se um ancestral do modal cria um contexto (por exemplo, com position e z-index próprios, opacity menor que 1 ou transform) que fica abaixo do cabeçalho, o 1000 só vale lá dentro. As demais explicações não existem: fixed aceita z-index, não há limite de 999 e a ordem do HTML não supera valores definidos."]
    ]
  },
  {
    pasta: "cesgranrio", board: "Cesgranrio", incidence: "MEDIA", estilo: "CESGRANRIO",
    howItFalls: "A Cesgranrio cobra CSS em múltipla escolha de cinco alternativas, com enunciados que descrevem uma necessidade do sistema e perguntam qual propriedade, seletor ou técnica a atende. O foco é aplicado e direto: escolher entre propriedades parecidas, entender o efeito de um valor e reconhecer a ferramenta adequada (Flexbox para alinhar itens em linha, Grid para malha, media query para adaptar à tela). As alternativas erradas costumam ser tecnicamente reais, mas servem a outro propósito, o que exige saber a função de cada recurso, não só reconhecê-lo. Estude a correspondência entre necessidade e propriedade: alinhar, espaçar, esconder, posicionar, adaptar e animar, cada uma com as suas propriedades típicas.",
    tips: [
      "Traduza o enunciado em verbo: alinhar, espaçar, esconder, posicionar, adaptar ou animar; cada verbo tem suas propriedades.",
      "Alternativas reais com propósito diferente são a armadilha principal: pergunte 'isso resolve o que foi pedido?'.",
      "Para animação disparada por interação (hover), pense em transition; para animação contínua, @keyframes.",
      "Para espaço entre itens de Flexbox ou Grid, a solução moderna é gap, não margens em cada item."
    ],
    traps: [
      "A alternativa usa transform para mudar a cor suavemente. Errado: transform gira, move e escala; a suavização da troca é transition.",
      "A alternativa usa float para centralizar verticalmente. Errado: float não centraliza na vertical; Flexbox resolve com align-items.",
      "A alternativa usa opacity: 0 para remover o elemento do fluxo. Errado: ele continua ocupando espaço e recebendo cliques.",
      "A alternativa usa position: relative para fixar um botão na tela. Errado: quem fica parado ao rolar é fixed."
    ],
    questoes: [
      ["Em um painel, os botões de ação devem aparecer lado a lado, com 12px de espaço entre eles, sem margem nas pontas. A forma mais direta de fazer isso no contêiner é:", "MEDIA", C4, "e5",
        [["display: flex; gap: 12px;", true], ["display: block; padding: 12px;", false], ["display: grid; margin: 12px;", false], ["float: left; border-spacing: 12px;", false], ["display: inline; line-height: 12px;", false]],
        "Gabarito A. Com display: flex, os botões ficam em linha, e gap cria exatamente 12px entre os itens, sem espaço nas pontas. padding e margin aplicados ao contêiner criam espaço em volta dele, border-spacing é de tabelas e line-height ajusta a altura da linha de texto."],
      ["Um link deve mudar de cor gradualmente, em 0,2 segundo, quando o mouse passar sobre ele. O recurso indicado é:", "FACIL", C5, "e6",
        [["transform: scale(0.2);", false], ["animation-delay: 0.2s;", false], ["transition: color 0.2s;", true], ["opacity: 0.2;", false], ["z-index: 2;", false]],
        "Gabarito C. transition: color 0.2s faz a troca de cor provocada pelo :hover acontecer de forma gradual. transform muda forma e posição, animation-delay só atrasa uma animação de @keyframes, opacity torna o elemento transparente e z-index trata da ordem de empilhamento."],
      ["Um selo de 'Novo' deve ficar preso no canto superior direito de cada card, mesmo com cards de alturas diferentes. A combinação adequada é:", "MEDIA", C3, "e4",
        [["Card com position: absolute e selo com position: relative.", false], ["Card com position: relative e selo com position: absolute; top: 0; right: 0.", true], ["Card com float: right e selo com display: inline.", false], ["Card com position: fixed e selo com position: static.", false], ["Card com display: none e selo com visibility: visible.", false]],
        "Gabarito B. O card com position: relative vira a referência, e o selo absolute com top: 0 e right: 0 vai para o canto superior direito de cada card. Inverter os papéis faria o card sair do fluxo; fixed prenderia o card à janela; float e display none não posicionam o selo no canto."],
      ["Uma página deve usar duas colunas em telas a partir de 900px e uma coluna em telas menores. Seguindo mobile-first, a estrutura correta é:", "MEDIA", C5, "e6",
        [["Regra base com duas colunas e @media (min-width: 900px) com uma coluna.", false], ["Regra base com uma coluna e @media (max-width: 900px) com duas colunas.", false], ["Regra base com uma coluna e @media (min-width: 900px) com duas colunas.", true], ["Somente @media print com as duas versões do layout.", false], ["Regra base com duas colunas e @media (orientation: portrait) com uma.", false]],
        "Gabarito C. No mobile-first, o estilo base é o do celular, com uma coluna, e a media query com min-width: 900px acrescenta as duas colunas a partir dessa largura. As outras opções invertem a lógica, testam a orientação em vez da largura ou usam a mídia de impressão."]
    ]
  },
  {
    pasta: "ibfc", board: "IBFC", incidence: "BAIXA", estilo: "IBFC",
    howItFalls: "O IBFC cobra CSS de forma mais básica e conceitual, em múltipla escolha, com enunciados que pedem a definição de um termo ou a identificação da afirmativa correta sobre um recurso. São frequentes as perguntas sobre o significado da sigla, a função do CSS em relação ao HTML, as formas de inclusão (externa, interna e em linha), a sintaxe de uma regra e os seletores de ID e de classe. Em editais de nível superior aparecem também modelo de caixa e noções de responsividade. A melhor preparação é ter definições curtas e precisas na ponta da língua e reconhecer a sintaxe correta de uma regra, porque as alternativas erradas costumam trocar termos parecidos ou inverter a função de HTML e CSS.",
    tips: [
      "Tenha prontas as definições curtas: CSS define a apresentação; HTML define a estrutura.",
      "Saiba as três formas de inclusão pelo nome: externa (link), interna (style) e em linha (atributo style).",
      "Em sintaxe, a regra é seletor seguido de chaves com propriedade: valor; e ponto e vírgula.",
      "Lembre o significado da sigla: Cascading Style Sheets, folhas de estilo em cascata."
    ],
    traps: [
      "A alternativa diz que CSS é uma linguagem de programação para criar a estrutura da página. Errado: CSS é linguagem de estilo; a estrutura é do HTML.",
      "A alternativa diz que CSS significa Computer Style Sheets. Errado: é Cascading Style Sheets.",
      "A alternativa diz que o ID pode se repetir em vários elementos da página. Errado: o ID deve ser único; a classe é que se repete.",
      "A alternativa diz que a regra usa sinal de igual entre propriedade e valor. Errado: usa dois-pontos."
    ],
    questoes: [
      ["A sigla CSS, usada no desenvolvimento web, significa:", "FACIL", C1, "e1",
        [["Computer Style Sheets.", false], ["Creative Style System.", false], ["Cascading Style Sheets.", true], ["Colorful Style Syntax.", false], ["Content Styling Service.", false]],
        "Gabarito C. CSS significa Cascading Style Sheets, folhas de estilo em cascata. O 'cascata' se refere à forma como o navegador decide entre regras em conflito (origem, especificidade e ordem). As demais expansões não existem."],
      ["Sobre a relação entre HTML e CSS, é correto afirmar que:", "FACIL", C1, "e1",
        [["O CSS define a estrutura e o HTML define a aparência.", false], ["O HTML estrutura o conteúdo e o CSS define a apresentação.", true], ["O CSS substitui o HTML em páginas modernas.", false], ["O HTML e o CSS são a mesma linguagem com nomes diferentes.", false], ["O CSS é usado apenas para criar animações.", false]],
        "Gabarito B. O HTML diz o que cada parte é (título, parágrafo, lista) e o CSS diz como ela aparece (cores, tamanhos, posições). Essa separação permite trocar o visual sem mexer no conteúdo. O CSS não substitui o HTML, não são a mesma linguagem e animação é só um dos recursos do CSS."],
      ["A sintaxe correta de uma regra CSS que define fundo cinza para o elemento body é:", "FACIL", C1, "e1",
        [["body = background: gray", false], ["body { background = gray }", false], ["{ body: background gray }", false], ["body { background: gray; }", true], ["background(body) { gray }", false]],
        "Gabarito D. A regra tem o seletor (body), seguido de chaves com a declaração no formato propriedade: valor; (background: gray;). As outras opções usam sinal de igual, invertem a posição do seletor ou inventam uma sintaxe de função que o CSS não tem."],
      ["Sobre os seletores de ID e de classe, assinale a alternativa correta.", "MEDIA", C2, "e1",
        [["O ID usa ponto (.) e pode se repetir na página.", false], ["A classe usa cerquilha (#) e deve ser única.", false], ["O ID usa cerquilha (#) e deve ser único na página.", true], ["Classe e ID têm a mesma especificidade.", false], ["Um elemento não pode ter classe e ID ao mesmo tempo.", false]],
        "Gabarito C. O ID é selecionado com # e deve identificar um único elemento; a classe é selecionada com ponto e pode se repetir em vários elementos. O ID tem especificidade maior que a classe, e um mesmo elemento pode ter as duas coisas ao mesmo tempo."]
    ]
  },
];
