# Direção artística - primeiro acesso

## O que mudou

A primeira versão tentava desenhar a arte à mão, em Compose Canvas, com formas planas em
"isometria suave" - módulos-cápsula, trilhas curvas, nós. Depois de duas rodadas de ajuste ela
continuou parecendo o que é: vetor achatado, não 3D. Não tem quantidade de sombra, grade de pontos
ou detalhe que resolva isso - 2D desenhado à mão não vira 3D profissional, e insistir só produzia
uma "quase-arte" que competia mal com o resto do app.

A decisão agora: **a arte final é um render 3D de verdade, gerado fora do app**, e o Compose só
precisa saber trocar o placeholder por ela quando o arquivo existir - o que já funciona, sem
mudança de código. Enquanto a arte final não chega, a tela mostra só o glifo da marca sobre um
painel neutro (`OnboardingPlaceholderArt` em `ui/onboarding/OnboardingIllustrations.kt`) - um
espaço de espera honesto, não uma tentativa de ilustração completa.

Segunda mudança, de conteúdo: o produto não pressupõe mais um edital formal. A pessoa pode importar
um edital completo **ou** só listar as matérias que sabe que precisa estudar (o app já suporta isso
- método "Montar manualmente" na tela de configuração). A direção de arte abaixo trata as duas
coisas como a mesma ideia: conteúdo estruturado, de qualquer origem, virando um caminho.

## A ideia

A marca do Estudário conta **uma transformação**: o que você precisa estudar - vindo de um edital
ou só de uma lista de matérias - vira um caminho controlável. E um segundo tema, tão central quanto
o primeiro: você não estuda sozinho contra esse conteúdo. Pode levar qualquer pedaço dele para a IA
que já usa - a que for - e o que ela responde volta a fazer parte do plano.

## Estilo - render 3D profissional

Pense em ilustração de produto de SaaS de ponta (o registro visual de Stripe, Linear, Notion):
objetos sólidos com material de verdade - plástico fosco, vidro fosco - sob luz de estúdio, nunca
um ícone plano, nunca um desenho de linha, nunca uma cena fotográfica com pessoas.

**Câmera.** Isométrica suave, o mesmo ângulo em todas as cenas (~30°, olhando de cima e da
esquerda). Trocar de ângulo entre cenas é o que mais rápido denuncia um conjunto que não foi
pensado como família.

**Luz.** Estúdio, principal vindo de cima à esquerda, preenchimento suave do lado oposto. Sombra de
contato curta e macia sob cada objeto - nunca objetos flutuando sem ancoragem no chão da cena.

**Material.** Os blocos que representam conteúdo são ora plástico fosco (cor sólida, sem brilho
forte), ora vidro fosco translúcido (para o que ainda não foi tocado) - nunca metal polido, nunca
holograma, nunca textura de papel ou tecido.

**Fundo.** Cor sólida e chapada, sem gradiente, exatamente a cor de fundo do tema - `#FAF9F5` na
versão clara, `#131311` na escura. **Cada cena precisa das duas versões**, geradas ou coloridas
separadamente: uma imagem 3D não se adapta ao tema em tempo real como um desenho vetorial. Sem
esse par, a arte só funciona em um dos dois modos, o que é pior que não ter arte nenhuma (o
placeholder pelo menos funciona nos dois).

**Paleta.** As mesmas cores da marca, agora como cor de material, não de preenchimento chapado:

| Papel | Claro | Escuro |
|---|---|---|
| Marca / decidido | `#4F46E5` | `#C5C0FF` |
| Dominado | `#0B7A56` | `#7EE0B8` |
| Pendente (vidro fosco) | `#79776D` | `#938F85` |
| Fundo | `#FAF9F5` | `#131311` |

Sem cor fora dessa paleta, sem gradiente arco-íris, sem neon.

## O que está proibido

Pessoa, mão, rosto. Notebook, livro físico, lâmpada, checklist, lápis, cérebro, engrenagem, robô ou
qualquer assistente antropomorfizado, balão de chat, foguete de produtividade. Foto ou textura
fotorrealista de objeto do mundo real (a linguagem é abstrata, não um "escritório em miniatura").
Logotipo ou nome de qualquer IA específica - a cena de "sua IA" é sobre escolha, não sobre uma
marca. Gráfico corporativo genérico (barra, rosca, linha de tendência). Texto, letra ou número
dentro da imagem.

## As cinco cenas

Proporção de todas: **16:10**, gerada a 1600×1000 (cobre 3x num aparelho de 400dp de largura).
Cada cena existe como dois arquivos - claro e escuro - com fundo chapado já embutido na imagem, sem
transparência: ver "Como entregar os arquivos" abaixo para os nomes exatos.

### 1. Abertura - `art_onboarding_welcome`

Content solto virando caminho. Um grupo de blocos de vidro fosco (pendente), tamanhos e alturas
irregulares, espalhados sem ordem na metade esquerda da cena. Os mesmos blocos, agora sólidos e na
cor da marca, formam uma escada/rampa 3D ascendente que sobe até o canto superior direito, onde um
bloco com um leve brilho (halo suave, sem exagero) marca a chegada. A transformação é uma única
composição contínua, não um "antes/depois" com linha divisória.

### 2. Conteúdo - `art_onboarding_syllabus`

Estrutura, não pilha. Um bloco central maior se ramifica em blocos médios, que se ramificam em
blocos menores - uma árvore 3D, conectores finos e sólidos (não linhas 2D soltas: pequenos trilhos
ou hastes com a mesma linguagem de material dos blocos). Parte dos blocos menores já é sólida
(cor "dominado" - verde), outra parte ainda é vidro fosco. A composição deixa claro que o conteúdo
chega com estrutura própria, não como uma pilha achatada - funciona tanto para "isto é um edital"
quanto para "isto são as matérias que eu escolhi".

### 3. Plano - `art_onboarding_plan`

Cada coisa no seu lugar. Uma fileira/trilho horizontal com encaixes marcados (como slots discretos
de um trilho técnico). Blocos descendo e se encaixando: os da esquerda já pousados e sólidos (cor
"dominado"), um bloco central na cor da marca, ligeiramente maior, com um brilho suave marcando
"hoje", os da direita ainda em vidro fosco ou parcialmente transparentes, um ou dois ainda "no ar"
sobre o próprio slot, como se estivessem prestes a encaixar.

### 4. Sua IA - `art_onboarding_ai`

O tema central. O mesmo trilho da cena anterior continua visível. Um bloco se eleva do trilho até um
pequeno agrupamento solto de três formas de vidro - nenhuma delas com cor de marca, tamanhos e
alturas diferentes, "a IA que você já usa", no plural, nunca uma identidade fixa. Uma dessas três
formas ganha luz e cor de marca - a escolhida. Um feixe de luz fino e sólido (não um raio genérico
de energia - uma linha de luz discreta, na cor da marca) conecta só essa forma de volta ao bloco no
trilho, que volta maior, sólido, com o mesmo brilho de "decidido" da cena 3.

Nada de robô, tela de chat ou logotipo - a ideia inteira é contada por três formas, uma escolha, e
um feixe de luz voltando.

### 5. Evolução - `art_onboarding_progress`

**A mesma rampa da cena 1**, agora ocupando a cena inteira e vista quase completa: a mesma
composição, o mesmo ângulo, a mesma curva - é a história fechando. Cerca de 60% do início da rampa é
sólido e colorido (dominado); o restante, em direção ao topo, é vidro fosco, translúcido, blocos um
pouco menores. Um bloco com brilho marca exatamente a fronteira entre os dois. Nunca um gráfico de
barra ou linha - a leitura vem do próprio caminho.

### Extra - `art_empty_no_exam`

Estado vazio: o trilho existe, os encaixes existem, só o primeiro bloco tem cor - os outros três são
vidro fosco vazio, esperando.

## Prompts para geração

Cada cena precisa de duas gerações - clara e escura - mudando só a cor de fundo e a paleta de
material (ver tabela acima). Gerar a claro e a escuro na mesma sessão, logo em sequência, ajuda a
manter luz e composição idênticas entre as duas.

### Bloco de estilo - versão clara (repetir em todas, trocando só a cena)

```
Professional 3D render, soft isometric camera angle (~30°, looking down and to
the left), consistent across the whole set. Studio lighting: key light from the
upper left, soft fill from the opposite side, short soft contact shadows under
every object - nothing floats without grounding.

Objects are simple geometric blocks (rounded rectangular slabs), rendered in two
materials only: matte solid plastic in indigo #4F46E5 or green #0B7A56 for
"decided" or "mastered" content, and frosted translucent glass in warm grey
#79776D for content not yet touched. No metal, no chrome, no holographic
surfaces, no photographic textures.

Flat solid background, color #FAF9F5 (warm off-white), no gradient, no
vignette, no floor grid, no horizon line.

No text, no letters, no numbers, no people, no hands, no faces, no laptops, no
books, no lightbulbs, no robots, no chat bubbles, no logos or brand marks of
any kind, no bar/pie/line charts, no photographic elements.

Composition: 16:10 landscape aspect ratio, generous negative space, calm and
precise, editorial product-illustration sensibility - think a high-end SaaS
launch page (Stripe, Linear, Notion), not a stock-icon pack.
```

### Bloco de estilo - versão escura (mesma cena, só troca isto)

```
[mesmo bloco acima, com estas substituições:]
Materials: matte solid plastic in indigo #C5C0FF or green #7EE0B8 for "decided"
or "mastered" content, frosted translucent glass in warm grey #938F85 for
content not yet touched.
Flat solid background, color #131311 (near-black), no gradient.
```

### Cena 1 - abertura

```
[bloco de estilo]

Scene: a loose, disordered cluster of frosted warm-grey glass blocks of varying
sizes on the left side of the frame. The same blocks, now solid indigo and
uniform, form a continuous 3D ascending ramp/staircase rising toward the upper
right corner of the frame, where the topmost block has a subtle soft glow
marking arrival. The disordered cluster and the ordered ramp are one single
continuous composition - no dividing line, no arrow, no before/after split.
```

### Cena 2 - conteúdo

```
[bloco de estilo]

Scene: one larger solid indigo block at the left, connecting via thin solid
rod-like connectors (same material language as the blocks, not flat 2D lines)
to three medium blocks, each of which connects to two or three smaller blocks
further right - a branching 3D tree structure read left to right. Some of the
smallest blocks are solid green (mastered), others are frosted translucent
glass (not yet studied). Connectors get thinner as the structure branches.
```

### Cena 3 - plano

```
[bloco de estilo]

Scene: a horizontal 3D track/rail spanning most of the frame width, with
visible discrete slot markings along it. Six blocks seated along the track:
the first two solid green and fully seated, the third slightly larger, solid
indigo, with a soft glow marking "today", the last three either frosted glass
or partially translucent, one or two of them still slightly above their slot
as if about to settle into place.
```

### Cena 4 - sua IA

```
[bloco de estilo]

Scene: the same horizontal track as the previous scene, partially visible,
with a green solid block on one side. From one block near the center, the
block rises slightly and a thin soft indigo light beam extends upward toward a
small loose cluster of three frosted-glass shapes of different sizes and
heights, floating close together but not touching - plain, colorless, no two
identical, suggesting options rather than one fixed choice. One of the three -
not the largest - glows softly and turns solid indigo. Only from that one, a
single thin solid indigo light beam returns down to the block on the track,
which is now slightly larger and solid indigo with a soft glow, clearly
"decided". No hub or junction point - direct beams only. No robot, no screen,
no chat interface, no brand logo anywhere in the scene.
```

### Cena 5 - evolução

```
[bloco de estilo]

Scene: the same ascending 3D ramp/staircase as scene 1, now shown as the full
composition. The first roughly 60% of the ramp (from the lower area) is solid
indigo and green, fully rendered blocks with real material and shadow. Beyond
that point, the ramp continues as frosted translucent glass, slightly smaller
blocks, fading toward the upper right. Exactly at the boundary between the two
sections, one block has a soft glow marking the current position. Reads as a
path being filled in - never as a bar chart, line chart, or progress ring.
```

## Como entregar os arquivos

Os nomes abaixo são definitivos e já existem no código (`OnboardingArt` em
`ui/onboarding/OnboardingIllustrations.kt`) - não precisam mudar quando a arte chegar.

| Cena | Nome do arquivo |
|---|---|
| Abertura | `art_onboarding_welcome` |
| Conteúdo | `art_onboarding_syllabus` |
| Plano | `art_onboarding_plan` |
| Sua IA | `art_onboarding_ai` |
| Evolução | `art_onboarding_progress` |
| Estado vazio | `art_empty_no_exam` |

Para cada nome: a versão **clara** vai em `app/src/main/res/drawable/<nome>.png` (ou `.webp`); a
versão **escura**, com o mesmo nome, vai em `app/src/main/res/drawable-night/<nome>.png`. O Android
escolhe sozinho pelo tema do aparelho - não existe flag nem import para configurar isso.

Formato: PNG ou WebP, 1600×1000 (16:10), fundo **chapado** já embutido na imagem (a cor exata da
tabela acima) - sem transparência, porque a imagem precisa cobrir o quadro inteiro sem deixar ver o
que está atrás.

Se só existir a versão clara, **não entregue nada ainda** - o placeholder (glifo sobre painel
neutro) funciona nos dois temas; uma arte que só funciona num dos dois é pior que o placeholder.

## Continuidade visual

Esse vocabulário (bloco 3D, material fosco/vidro, feixe de luz) é a nova linguagem de toda arte de
produto do Estudário, não só do onboarding - mas ele só existe fora do Compose, como imagem gerada.
Dentro do app, as primitivas 2D antigas (`artModule`, `artTrack`, `artNode`, `artSpoke`,
`artDotField`, `artTicks`, em `ui/components/EstudarioArt.kt`) continuam existindo e servem para
elementos funcionais pequenos - barra de cobertura, indicador de progresso, um ícone de estado -
onde uma forma desenhada no código ainda faz sentido. A diferença agora é clara: **ilustração de
tela cheia é sempre imagem gerada; elemento funcional pequeno pode continuar sendo Compose.**

## Movimento

A entrada continua sutil e uma vez só - 640ms, com um leve crescimento de escala (94% → 100%) e
aparecimento gradual, tanto para o placeholder quanto para a arte final assim que ela for colocada
(`OnboardingArtSlot` aplica isso automaticamente aos dois, não é preciso configurar nada por
imagem). Nada pulsa, nada gira, nada se repete em loop.

Com redução de movimento ligada no sistema, a arte nasce montada e nenhuma animação roda - lido de
`Settings.Global.ANIMATOR_DURATION_SCALE`, verificado em `rememberReducedMotion()`.

Se, além da imagem estática, você quiser movimento de verdade dentro da própria cena (os blocos se
formando, o feixe de luz viajando), isso é um projeto separado - uma animação Lottie exportada de
After Effects ou gerada por uma ferramenta de IA - e precisa de uma dependência nova no projeto
(`lottie-compose`) que ainda não foi adicionada. Avise antes de investir nisso: a imagem estática
com a transição de entrada acima já é um salto grande sobre o que existia.
