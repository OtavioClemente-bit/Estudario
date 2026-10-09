# Atualizar matérias — pedido 17 de 18 (09/10/2026)

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

### 1. `processo-trabalho.nulidades-excecoes` — Nulidades e exceções no processo do trabalho
Arquivo atual: `conteudo/materias/processo-trabalho/nulidades-excecoes.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre poderes do juiz do trabalho (direção do processo, art. 765 da CLT, poderes instrutórios, conciliação, polícia de audiência) e questões; hoje a matéria só cobre impedimento e suspeição.
- Capítulo sobre honorários advocatícios no processo do trabalho (art. 791-A da CLT, sucumbência recíproca, beneficiário da justiça gratuita, honorários contratuais) e questões que os liguem às nulidades e à preclusão.

### 2. `ciencias.estequiometria` — Estequiometria
Arquivo atual: `conteudo/materias/ciencias/estequiometria.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Unidade de massa atômica (u), massa atômica média e volume molar (22,4 L/mol nas CNTP, lei de Avogadro), com questões; a matéria cobre mol e massa molar mas não u nem volume molar.
- Capítulo sobre as leis ponderais (Lavoisier, Proust, Dalton e Richter-Wenzel) com exemplos e questões; hoje só há conservação de massa de passagem.
- Fórmulas químicas: mínima, molecular e percentual (composição centesimal), determinação a partir de massas, com questões.

### 3. `direito-trabalho.fgts` — FGTS
Arquivo atual: `conteudo/materias/direito-trabalho/fgts.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- Certificado de Regularidade do FGTS (CRF): finalidade, exigência para contratos e financiamentos, validade e casos de emissão, com questões.
- Guia de Recolhimento do FGTS (GRF) e GFIP/eSocial: quem recolhe, prazo, campos e retificação, com questões.
- Relação entre FGTS e PIS/PASEP (cotas, abono salarial, saque do PIS) com questões; a matéria não trata de PIS/PASEP.
- Incidência do FGTS sobre o 13º salário e demais parcelas remuneratórias (gratificação natal, horas extras, aviso prévio), com questões.

### 4. `etica.conflito-interesses-nepotismo` — Conflito de interesses e nepotismo na administração federal
Arquivo atual: `conteudo/materias/etica/conflito-interesses-nepotismo.json` · 2 tópicos de edital pedem mais conteúdo

Falta:
- Código de Conduta da Alta Administração Federal (finalidade, normas de conduta, Comissão de Ética Pública, sanções) e as quarentenas, com questões.
- Código de Conduta da Alta Administração Federal e ética pública (princípios, Comissão de Ética Pública, sanção de censura) com questões.

### 5. `direito.regras-minimas-tratamento-presos` — Regras Mínimas da ONU para o Tratamento de Pessoas Presas
Arquivo atual: `conteudo/materias/direito/regras-minimas-tratamento-presos.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- Regras de Bangkok, princípios da ONU para o tratamento de presos e Convenção Americana (garantias da pessoa presa), com questões.
- Regras de Bangkok: tratamento de mulheres presas, gestantes, filhos, revista e medidas não privativas de liberdade, com questões.
- Regras de Bangkok (Resolução 2010/16 da ONU): conteúdo e aplicação, com questões.
- Regras de Bangkok e Regras de Tóquio (medidas não privativas de liberdade), com questões.

### 6. `minas-gerais.barragens` — Barragens
Arquivo atual: `conteudo/materias/minas-gerais/barragens.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Política Nacional de Segurança de Barragens (Lei 12.334/2010 e alterações): classificação, plano de segurança, responsabilidades e sanções, em nível de juiz, com questões.
- Política Nacional de Segurança de Barragens em detalhe (objetivos, fundamentos, instrumentos, SNISB, classificação por risco e dano potencial) com questões.
- Tipos construtivos e componentes de barragens (terra, enrocamento, concreto), noções de engenharia e fiscalização de obras, com questões.

### 7. `direito-trabalho.estabilidade-garantias` — Estabilidade e garantias provisórias de emprego
Arquivo atual: `conteudo/materias/direito-trabalho/estabilidade-garantias.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- força maior (arts. 501 a 503 da CLT: conceito, imprevisibilidade, redução proporcional do salário, indenização reduzida pela metade) com cerca de 6 questões; estabilidade e garantias já estão cobertas
- força maior (arts. 501 a 503 da CLT: conceito, imprevisibilidade, redução proporcional do salário, indenização reduzida pela metade e extinção da empresa) com cerca de 6 questões; estabilidade e garantias já estão cobertas e o aviso prévio tem matéria própria

### 8. `direito-constitucional.trf-juizes-federais` — Tribunais Regionais Federais e juízes federais
Arquivo atual: `conteudo/materias/direito-constitucional/trf-juizes-federais.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- organização judiciária de Minas Gerais (TJMG, comarcas, juízes de direito) em comparação com a Justiça Federal, com 8 questões
- Justiça Militar e conflitos de competência com a Justiça Federal, com 6 questões; ver direito-constitucional.tribunais-juizes-militares-estados
- juízes estaduais e competência delegada/residual em relação aos juízes federais, com 6 questões

### 9. `processo-trabalho.embargos-execucao` — Embargos à execução, impugnação e embargos de terceiro
Arquivo atual: `conteudo/materias/processo-trabalho/embargos-execucao.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- liquidação de sentença (cálculo, artigos, arbitramento, impugnação à conta) integrada aos embargos, com 8 questões; ver processo-trabalho.liquidacao-sentenca
- praça, leilão, arrematação e custas na execução, com 10 questões; ver processo-trabalho.praca-leilao-arrematacao
- praça, leilão, arrematação e custas na execução, com 10 questões

### 10. `fisica.trabalho-energia-potencia` — Trabalho, energia e potência
Arquivo atual: `conteudo/materias/fisica/trabalho-energia-potencia.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- impulso e quantidade de movimento (teorema do impulso) com 8 questões; ver fisica.impulso-quantidade-movimento-colisoes
- trabalho da força elástica e energia potencial elástica (mola) com 8 questões
- impulso e quantidade de movimento com 8 questões

### 11. `ciencias.gases` — Gases
Arquivo atual: `conteudo/materias/ciencias/gases.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- difusão e efusão dos gases (lei de Graham) com 6 questões
- gases perfeitos na termodinâmica: energia interna, primeira lei, trabalho em transformações e ciclos, com 10 questões
- transformação adiabática (relação entre P, V e T, expoente de Poisson) com 6 questões

### 12. `matematica.polinomios-equacoes-polinomiais` — Polinômios e equações polinomiais
Arquivo atual: `conteudo/materias/matematica/polinomios-equacoes-polinomiais.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- frações polinomiais e identidade de frações (decomposição e simplificação) com 6 questões
- funções polinomiais (afim, quadrática, cúbica): gráficos, raízes e sinais, com 8 questões
- produtos notáveis (quadrado da soma, diferença de quadrados, cubo) com 8 questões

### 13. `ciencias.radioatividade` — Radioatividade e transformações nucleares
Arquivo atual: `conteudo/materias/ciencias/radioatividade.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Capítulo sobre espectro eletromagnético, radiações ionizantes e não ionizantes e fontes naturais e artificiais de radiação (radônio, raios cósmicos, equipamentos), com questões.
- Capítulo sobre produção de raios X (tubo, bremsstrahlung, raios X característicos), irradiadores gama e comparação de poder de penetração de alfa, beta, gama e X, com questões.
- Aprofundar proteção radiológica: grandezas dosimétricas (dose absorvida, equivalente, efetiva), limites de dose, princípios de justificação, otimização (ALARA) e limitação, e normas de radioproteção, com questões; hoje só nível introdutório.

### 14. `geografia.demografia-urbanizacao-mundo` — Demografia e urbanização mundial
Arquivo atual: `conteudo/materias/geografia/demografia-urbanizacao-mundo.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Distribuição espacial da população no Brasil (regiões, densidade, concentração litorânea, rede urbana nacional) com questões; a matéria é de escala mundial.
- Indicadores de qualidade de vida (IDH, expectativa de vida, mortalidade infantil, analfabetismo, Gini) com questões.
- Capítulo sobre teorias demográficas (malthusiana, neomalthusiana, reformista) e teoria da transição, com questões.

### 15. `ciencias.principios-basicos` — Princípios básicos
Arquivo atual: `conteudo/materias/ciencias/principios-basicos.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Hidrodinâmica e mecânica dos fluidos de engenharia (escoamento, equação da continuidade, Bernoulli, perda de carga), que o escopo exclui, com questões.
- Propriedades dos fluidos: viscosidade, compressibilidade, tensão superficial, fluido newtoniano e massa específica, com questões.
- Forças hidrostáticas sobre superfícies planas e curvas submersas (empuxo resultante, centro de pressão) para engenharia, com questões.

### 16. `processo-trabalho.custas-emolumentos` — Custas, emolumentos e isenções
Arquivo atual: `conteudo/materias/processo-trabalho/custas-emolumentos.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Valor da causa no processo do trabalho (art. 840, § 1º, e rito sumaríssimo) e sua ligação com custas, com questões.

### 17. `portugues.estilistica-vicios-linguagem` — Estilística, vícios de linguagem e qualidades da expressão
Arquivo atual: `conteudo/materias/portugues/estilistica-vicios-linguagem.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Ruptura de registro, coloquialismo, anacronismo, rebuscamento e linguagem estereotipada (clichê), com exemplos e questões.
- Convenções da escrita e recursos estruturais do texto (paralelismo, sequência, estrutura do período), com questões.
- Problemas estruturais de frases (ambiguidade estrutural, paralelismo, truncamento, ordem das palavras), com reescritas e questões.

### 18. `fisica.leis-newton-atrito` — Leis de Newton, forças e atrito
Arquivo atual: `conteudo/materias/fisica/leis-newton-atrito.json` · 3 tópicos de edital pedem mais conteúdo

Falta:
- Sistemas de referência inerciais e não inerciais e a relação com a primeira lei, com questões.
- Força elástica (lei de Hooke, molas) e força peso em diferentes referenciais, com questões.
- Força centrífuga (referencial não inercial), superelevação e superlargura de curvas em rodovias e ferrovias, com questões.
