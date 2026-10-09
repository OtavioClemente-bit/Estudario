# Atualizar matérias — pedido 18 de 18 (09/10/2026)

Uma revisão de todas as ligações edital → matéria mostrou que estas matérias **pertencem** aos tópicos
dos editais, mas estão rasas ou incompletas para o nível cobrado. Reescreva cada uma, mantendo o mesmo id,
título e escopo central, e **acrescente** o que falta (lista abaixo, tirada dos tópicos reais dos editais,
em ordem de importância).

Leia antes (só leitura): `AGENTS.md`, `conteudo/fila/PEDIDO-MATERIA-AGENTE.md`,
`conteudo/fila/FORMATO-VISUAL.md` e o modelo `conteudo/materias/direito-penal/teoria-do-crime.json`.

## Regras
- Abra a matéria publicada (caminho ao lado do id) e parta dela: aproveite o que está certo, corrija o
  que estiver raso, e cubra os itens de "falta". Se um item for de outra matéria, ignore-o.
- "version" = versão publicada + 1. Mesmo "id".
- 5 a 7 capítulos com 2.500+ caracteres úteis cada (tabelas comparativas, artigos de lei citados quando
  tiver certeza); 45+ questões (A–E e Certo/Errado, dificuldades equilibradas, gabaritos equilibrados),
  cobrindo também os itens novos; 15+ flashcards, dicas, pegadinhas, recordação ativa, fontes reais.
- Distrator bom = erro de quem estudou mal (instituto vizinho, prazo trocado, exceção esquecida). Nada de
  "sempre/nunca/exclusivamente" caricato; a certa não pode ser sempre a mais longa.
- Lei/súmula/tema que você não tem certeza: explique o conteúdo sem o número. Nunca invente norma.
- Grave cada matéria como `<id>.json` **na pasta desta conversa**, uma por vez, inteira, à mão.
- Rode `deno run --allow-read C:/Users/otavi/Documents/Codex/2026-09-15/vc-x20/conteudo/fila/conferir.ts <id>.json`
  até "validador: OK". Não faça commit nem push. Não escreva no projeto.

## Matérias (nesta ordem)

### 1. `geografia.geologia-relevo-solos` — Geologia, relevo e solos
Arquivo atual: `conteudo/materias/geografia/geologia-relevo-solos.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Pedologia: classificação de solos (SiBCS: latossolo, argissolo etc.), horizontes e ciclagem de nutrientes, com questões.
- Hidrologia (bacias hidrográficas, ciclo da água, águas subterrâneas), que a matéria não cobre, com questões.
- Tipos de solos e suas características (latossolos, argissolos, neossolos, etc.), com questões.

### 2. `biologia.seres-vivos-classificacao` — Seres vivos e classificação
Arquivo atual: `conteudo/materias/biologia/seres-vivos-classificacao.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Evolução (Darwin e Lamarck, seleção natural, especiação, filogenia e cladística) ligada à classificação, com questões.
- Ciclos biológicos de invertebrados causadores de doenças (esquistossomose, teníase, ascaridíase, malária, doença de Chagas, filariose etc.), com questões.
- Regras de nomenclatura biológica (binomial, gênero e espécie, grafia, código de nomenclatura), com questões.

### 3. `fisica.magnetismo-inducao` — Magnetismo e indução eletromagnética
Arquivo atual: `conteudo/materias/fisica/magnetismo-inducao.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- propriedades magnéticas dos materiais (paramagnetismo, diamagnetismo, ferromagnetismo, histerese), com 8 questões
- propriedades elétricas e magnéticas dos materiais (dielétricos, condutores, para/dia/ferromagnetismo), com 8 questões

### 4. `matematica.trigonometria` — Trigonometria
Arquivo atual: `conteudo/materias/matematica/trigonometria.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- inequações trigonométricas (resolução no ciclo, intervalos) com 8 questões
- operações com arcos: seno, cosseno e tangente da soma e diferença, arco duplo e metade, transformação em produto, com 12 questões

### 5. `ciencias.solucoes` — Soluções
Arquivo atual: `conteudo/materias/ciencias/solucoes.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Acrescentar fração em quantidade de matéria (fração molar), fração em massa (título), fração em volume e densidade da solução, com conversões e questões; hoje só há g/L, mol/L, molalidade e % m/m.
- Acrescentar fração em quantidade de matéria (fração molar), fração em massa (título) e fração em volume, com conversões e questões.

### 6. `matematica.funcao-exponencial-logaritmica` — Funções exponencial e logarítmica
Arquivo atual: `conteudo/materias/matematica/funcao-exponencial-logaritmica.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Inequações exponenciais (sentido da desigualdade conforme a base) com resolução e questões; a matéria só traz equações simples.
- Inequações logarítmicas (condição de existência e sentido da desigualdade) com resolução e questões.

### 7. `portugues.figuras-de-linguagem` — Figuras de linguagem
Arquivo atual: `conteudo/materias/portugues/figuras-de-linguagem.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Adjetivação e metáfora como estratégia persuasiva em publicidade, com textos de anúncios e questões.
- Rima, métrica, aliteração, assonância e disposição gráfica em poemas (versos livres e estruturados), com questões.

### 8. `geografia.brasil-territorio-estado` — Brasil: território, Estado e divisão regional
Arquivo atual: `conteudo/materias/geografia/brasil-territorio-estado.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Desigualdades regionais e planejamento regional no Brasil (PNDR, SUDENE, SUDAM, polos de desenvolvimento, concentração industrial), com questões.
- Formação territorial do Brasil: ocupação, expansão, tratados de limites e organização do território ao longo da história, com questões.

### 9. `literatura.barroco-arcadismo` — Barroco e Arcadismo
Arquivo atual: `conteudo/materias/literatura/barroco-arcadismo.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Barroco no Brasil: Gregório de Matos, Padre Vieira e Botelho de Oliveira, com leitura de trechos e questões.
- Arcadismo no Brasil: Cláudio Manuel da Costa, Tomás Antônio Gonzaga, Basílio da Gama e Santa Rita Durão, com leitura de trechos e questões.

### 10. `literatura.modernismo-contemporanea` — Modernismo e literatura contemporânea
Arquivo atual: `conteudo/materias/literatura/modernismo-contemporanea.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Terceira geração modernista e tendências contemporâneas (Clarice Lispector, Guimarães Rosa, João Cabral, Geração de 45) com questões; a matéria cobre antecedentes e as duas primeiras fases.
- Pós-modernismo e produção posterior a 1945 (Geração de 45, concretismo, poesia marginal), com questões.
