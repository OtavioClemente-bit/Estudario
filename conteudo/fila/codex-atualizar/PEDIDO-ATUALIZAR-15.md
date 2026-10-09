# Atualizar matérias — pedido 15 de 18 (09/10/2026)

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

### 1. `portugues.fonologia-fonemas` — Fonologia, fonemas e divisão silábica
Arquivo atual: `conteudo/materias/portugues/fonologia-fonemas.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- compreensão textual e tonicidade (sílaba tônica, oxítona, paroxítona, proparoxítona), com 8 questões
- tonicidade e prosódia (ortoépia, silabada), com 8 questões
- tonicidade, ortoépia e prosódia, com 8 questões
- compreensão textual, tonicidade e prosódia, com 8 questões
- translineação (regras de separação de sílabas no fim da linha) com 6 questões

### 2. `geografia.cartografia-fusos` — Cartografia e fusos horários
Arquivo atual: `conteudo/materias/geografia/cartografia-fusos.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- sistemas de projeção (UTM, Lambert, Gauss-Krüger) e generalização cartográfica em nível técnico, com 10 questões
- sistemas de coordenadas (UTM, SIRGAS 2000), datum e GNSS/GPS em nível técnico, com 10 questões
- navegação: cartas náuticas e aeronáuticas, rumos, GPS e orientação, com 8 questões
- sensoriamento remoto e interpretação de imagens de satélite e fotografias aéreas, com 8 questões
- didática da cartografia no ensino de geografia (alfabetização cartográfica, leitura de mapas em sala), com 6 questões
- aerofotogrametria, fotointerpretação e sistemas de informação geográfica, com 10 questões
- cartas gráficas analógicas e digitais e produção cartográfica, com 8 questões
- escala e representação do espaço urbano (plantas, cadastro, desenho técnico) com 8 questões

### 3. `ciencias.quimica-organica-isomeria-reacoes` — Química orgânica: isomeria e reações
Arquivo atual: `conteudo/materias/ciencias/quimica-organica-isomeria-reacoes.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- Redução e combustão de compostos orgânicos e reatividade (efeitos eletrônicos, acidez e basicidade de orgânicos), com exemplos e questões; a matéria cobre adição, substituição, eliminação e oxidação.
- Reatividade dos compostos orgânicos (efeito indutivo e mesomérico, acidez e basicidade de álcoois, fenóis e ácidos, polaridade e sítios reativos) com questões.
- Mecanismos de substituição (SN1 e SN2, substituição aromática) e adição nucleofílica a carbonilas, que o escopo exclui como mecanismos avançados; com exemplos e questões.
- Reações de redução de compostos orgânicos (hidrogenação, redução de carbonilas) além da oxidação já tratada, com questões.
- Reações de redução e de combustão orgânica (completa e incompleta), que não aparecem na matéria, com questões.
- Estereoquímica em nível de professor: configuração R/S, diastereoisômeros, compostos meso e projeções de Fischer, com questões.

### 4. `medicina-legal.introducao-pericias-documentos` — Medicina Legal: introdução, perícias e documentos médico-legais
Arquivo atual: `conteudo/materias/medicina-legal/introducao-pericias-documentos.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- prontuário médico: conceito, finalidade, propriedade e guarda, acesso pelo paciente e pela Justiça, sigilo e valor probatório, com 6 questões
- quesitos oficiais por tipo de exame (lesão corporal, conjunção carnal e ato libidinoso, necropsia, sanidade mental, embriaguez), com cada quesito e resposta típica, com 10 questões
- modelos de laudo de lesão corporal, de sexologia forense e necroscópico: roteiro, partes e quesitos de cada um, com 8 questões
- perícia médica administrativa: junta médica oficial, licença para tratamento de saúde, readaptação, aposentadoria por invalidez, isenção de imposto e atestado administrativo, com 8 questões
- conduta ética do perito médico (Código de Ética Médica e normas do CFM): imparcialidade, impedimentos do médico perito, relação com o periciando, sigilo e limites do laudo, com 6 questões
- licenças médicas: atestado para licença, requisitos, CID e sigilo, prazos e controle pela perícia oficial, com 5 questões
- bases legais dos documentos médicos: Código de Ética Médica e resoluções do CFM sobre atestado, prontuário e sigilo, e artigos do Código Penal sobre falsidade de atestado, omissão de notificação e violação de segredo, com 6 questões

### 5. `direito-trabalho.direitos-constitucionais-trabalhadores` — Direitos constitucionais dos trabalhadores (art. 7º)
Arquivo atual: `conteudo/materias/direito-trabalho/direitos-constitucionais-trabalhadores.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- regra transitória do art. 10 do ADCT (indenização de 40% do FGTS enquanto não houver lei complementar) e vedação de dispensa arbitrária do dirigente da CIPA e da gestante, com 5 questões
- estabilidade provisória da gestante (art. 10, II, b, do ADCT): início, confirmação da gravidez, reintegração e indenização, e proteção do dirigente da CIPA, com 6 questões
- aposentadoria especial (art. 201, § 1º): ligação com atividades insalubres e perigosas e relação com os adicionais, com 4 questões

### 6. `informatica.java-fundamentos` — Java: fundamentos da linguagem
Arquivo atual: `conteudo/materias/informatica/java-fundamentos.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre os recursos do Java 8 em diante (expressões lambda, interfaces funcionais, Stream API, Optional, var, records e switch expressions) e 8 questões
- capítulo sobre novidades do Java SE 11 (var em lambdas, novos métodos de String, HttpClient) e 5 questões; a parte Java EE 8 fica fora desta matéria
- capítulo sobre recursos do Java 17 a 21 (records, sealed classes, pattern matching, text blocks, switch com padrões, virtual threads) e 6 questões
- capítulo sobre estrutura da JVM (heap, stack, metaspace), coleta de lixo (gerações e coletores) e monitoramento com JConsole, jps e jstack, mais 6 questões
- capítulo sobre arquitetura da JVM (áreas de memória, heap, garbage collection) e ferramentas de monitoramento JConsole, jps e jstack, mais 6 questões
- seção sobre literais (inteiros, ponto flutuante, caractere, booleano, null, text blocks), imutabilidade e pool de Strings e 4 questões
- seção sobre categorias de operadores (aritméticos, relacionais, lógicos, bit a bit, atribuição composta, ternário), tabela de precedência e associatividade e 5 questões de avaliação de expressões

### 7. `direito-constitucional.nacionalidade` — Direitos de nacionalidade (arts. 12 e 13)
Arquivo atual: `conteudo/materias/direito-constitucional/nacionalidade.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre direitos e deveres de nacionais no exterior (proteção diplomática, assistência consular, serviço militar e deveres do nacional) e 4 questões
- capítulo sobre dupla e múltipla nacionalidade (conflito positivo, polipatria, tratados de reconhecimento) e 5 questões
- seção sobre conceito e natureza jurídica da nacionalidade e sobre direitos e condição jurídica do estrangeiro (extradição, expulsão, deportação, asilo) e 6 questões
- seção sobre apatridia e polipatria (conflitos negativo e positivo, convenções internacionais sobre apátridas) e 5 questões
- seção sobre condição jurídica do estrangeiro e das pessoas no direito internacional (direitos do estrangeiro, vistos, expulsão) e 6 questões
- capítulo sobre múltipla nacionalidade e conflitos entre ordenamentos, com 5 questões
- seção sobre apatridia (causas, convenções da ONU, proteção do apátrida) e 5 questões

### 8. `processo-trabalho.audiencias` — Audiências, arquivamento, revelia e confissão
Arquivo atual: `conteudo/materias/processo-trabalho/audiencias.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre exceções (suspeição, impedimento e incompetência relativa: forma, prazo e efeitos) e 5 questões
- seção sobre perempção trabalhista (arquivamento repetido e perda do direito de reclamar por seis meses) e 4 questões
- seção sobre contestação e reconvenção no processo do trabalho (cabimento, momento, julgamento conjunto) e 5 questões
- capítulo sobre produção de provas na audiência (ônus, testemunhas, número, contradita, perícias e assistentes técnicos) e 8 questões
- capítulo sobre exceções e provas testemunhal, documental e pericial, com 10 questões
- capítulo sobre provas e decisões (razões finais, sentença, acordo homologado, prazos de publicação) e 8 questões

### 9. `legislacao.lei-8112-1990-direitos-vantagens` — Lei 8.112/1990: direitos e vantagens
Arquivo atual: `conteudo/materias/legislacao/lei-8112-1990-direitos-vantagens.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o regime disciplinar (deveres, proibições, acumulação, penalidades) ou ligar também à matéria de regime disciplinar; hoje só direitos e vantagens estão cobertos
- jornada de trabalho, progressão e promoção (carreira) e auxílios (alimentação, transporte, creche), hoje ausentes; férias, licenças e remuneração já estão cobertas
- capítulo sobre deveres e proibições do servidor (arts. 116 a 117) com cerca de 8 questões; a matéria cobre só direitos
- capítulo de regime disciplinar (deveres, proibições, penalidades, prescrição disciplinar) e questões, pois a matéria trata só de direitos e vantagens
- capítulo sobre aposentadoria do servidor federal (regras do art. 40 da CF aplicáveis à Lei 8.112) e questões; licenças já estão cobertas

### 10. `portugues.acentuacao` — Acentuação gráfica
Arquivo atual: `conteudo/materias/portugues/acentuacao.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- vocabulário e uso de dicionário (verbetes, abreviaturas, sinônimos) e crase/ortografia em geral, com 6 questões
- prosódia (ortoépia e silabada) e estrutura e formação das palavras (radical, afixos, derivação, composição), com 8 questões
- prosódia e estrutura e formação das palavras, com 8 questões
- estrutura e formação das palavras (radical, afixos, derivação, composição, hibridismo) com 8 questões
- classes de palavras (substantivo, adjetivo, verbo, advérbio, pronome etc.) e seu emprego, com 10 questões

### 11. `ciencias.equilibrio-quimico` — Equilíbrio químico
Arquivo atual: `conteudo/materias/ciencias/equilibrio-quimico.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- hidrólise de sais, soluções tampão (Henderson-Hasselbalch), Kh e graus de dissociação e hidrólise, com 12 questões
- lei da diluição de Ostwald (grau de ionização e Ka) com 6 questões
- efeito do íon comum no equilíbrio iônico, com 6 questões
- teorias ácido-base de Arrhenius e de Lewis (a matéria só trata de Brønsted-Lowry) com 6 questões

### 12. `direito-civil.prescricao-decadencia` — Prescrição e decadência no Código Civil
Arquivo atual: `conteudo/materias/direito-civil/prescricao-decadencia.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- prescrição contra a Fazenda Pública (prazo quinquenal do Decreto 20.910/1932, interrupção, prescrição intercorrente, pretensões de ressarcimento ao erário) e 6 questões
- hipóteses de imprescritibilidade (direitos da personalidade, estado das pessoas, pretensões declaratórias e ressarcimento ao erário conforme o STF) e 5 questões
- prazos decadenciais do Código Civil (anulação do negócio jurídico por vício, vícios redibitórios, retrovenda e outros) e 5 questões

### 13. `processo-trabalho.organizacao-competencia` — Justiça do Trabalho: organização e competência
Arquivo atual: `conteudo/materias/processo-trabalho/organizacao-competencia.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre composição e competência do TST (27 ministros, Pleno, Órgão Especial, Seções de Dissídios Individuais e Coletivos e Turmas) e 6 questões
- seção sobre responsabilidade por dano processual (litigância de má-fé: hipóteses, multa, indenização e legitimados) e 5 questões
- seção sobre o juiz do trabalho (investidura, atribuições do juiz e da Vara, impedimentos e suspeição, substituição) e 6 questões
- seção sobre poderes do juiz do trabalho (direção do processo, poder instrutório, conciliação, tutela de urgência e poder de polícia na audiência) e 6 questões

### 14. `fisica.eletrostatica` — Eletrostática
Arquivo atual: `conteudo/materias/fisica/eletrostatica.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- Lei de Gauss: fluxo do campo elétrico, superfície gaussiana, aplicação a esfera, fio e plano infinitos e ao condutor, com exemplos resolvidos e cerca de 8 questões; a lei não é citada na matéria
- princípios gerais do eletromagnetismo para perito de engenharia: equações de Maxwell, grandezas de campo e integrais de linha/superfície, com cerca de 8 questões; a matéria cobre só eletrostática elementar, sem magnetismo
- campos magnetostático e eletromagnetostático (lei de Biot-Savart, lei de Ampère, força magnética, indução) e a abordagem vetorial do campo eletrostático, com cerca de 10 questões; o magnetismo é excluído da matéria
- campos elétricos em meios materiais: dielétricos, polarização, vetor D, permissividade e condições de fronteira entre meios diferentes, com cerca de 8 questões; a matéria só cita a dependência da permissividade
- Lei de Gauss: fluxo elétrico, superfície gaussiana, aplicação a esfera, fio e plano infinitos e ao condutor, com cerca de 8 questões; a lei não é citada na matéria

### 15. `matematica.sistemas-lineares` — Sistemas lineares
Arquivo atual: `conteudo/materias/matematica/sistemas-lineares.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- sistemas com equação do 2º grau (substituição levando a equação quadrática) com 6 questões
- sistema linear homogêneo (solução trivial, determinante nulo e soluções não triviais) com 5 questões
- representação matricial de sistemas (AX=B), regra de Cramer e interpretação gráfica de três planos, com 8 questões
- sistemas com equação do 2º grau e métodos de resolução, com 6 questões

### 16. `matematica.sequencias-logicas` — Sequências lógicas
Arquivo atual: `conteudo/materias/matematica/sequencias-logicas.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- equivalências lógicas e raciocínio crítico, com 8 questões; ver matematica.equivalencias-negacoes-logicas
- progressões aritméticas e geométricas (termo geral, soma) como sequências, com 10 questões
- sequência de Fibonacci e outras recorrências clássicas, com 6 questões
- equivalências lógicas ao lado das sequências, com 8 questões
- equivalências lógicas e raciocínio crítico, com 8 questões

### 17. `fisica.optica-geometrica` — Óptica geométrica
Arquivo atual: `conteudo/materias/fisica/optica-geometrica.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- natureza da luz: modelo ondulatório, espectro eletromagnético, velocidade e cor, polarização e dualidade, com 8 questões
- instrumentos ópticos (lupa, microscópio, luneta, câmera, projetor) com 10 questões
- olho humano e defeitos visuais (miopia, hipermetropia, astigmatismo, presbiopia) e correção por lentes, com 8 questões
- instrumentos ópticos (lupa, microscópio, luneta, câmera) e suas características, com 10 questões
- instrumentos ópticos (lupa, microscópio, luneta, câmera) e suas aplicações, com 10 questões
- instrumentos ópticos construídos com lentes (lupa, microscópio, luneta) com 10 questões

### 18. `direito-trabalho.trabalho-menor` — Proteção ao trabalho do menor
Arquivo atual: `conteudo/materias/direito-trabalho/trabalho-menor.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- Direito à profissionalização e proteção no trabalho do ECA (arts. 60 a 69: aprendizagem, trabalho educativo, vedações, bolsa de aprendizagem) com questões.
- Salário do menor e do aprendiz (salário mínimo-hora, direito ao salário mínimo integral, FGTS a 2%), com questões.
- Trabalho infantil: conceito, características, causas e piores formas (Convenção 182 da OIT, lista TIP) com questões.
- Trabalho infantil artístico: autorização judicial, competência (ADI 5326), Convenção 138 da OIT e requisitos, com questões.
- Direito à profissionalização e proteção no trabalho do ECA (arts. 60 a 69) com questões.
