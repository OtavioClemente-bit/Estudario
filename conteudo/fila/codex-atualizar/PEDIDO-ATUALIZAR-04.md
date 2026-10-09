# Atualizar matérias — pedido 04 de 18 (09/10/2026)

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

### 1. `direito-previdenciario.beneficios-rgps` — Benefícios do RGPS
Arquivo atual: `conteudo/materias/direito-previdenciario/beneficios-rgps.json` · 53 tópicos de edital pedem mais conteúdo

Falta:
- inscrição do segurado (conceito, prova, regras por categoria) e diferença para filiação, com 4 questões
- prescrição e decadência em matéria previdenciária (prestações, fundo de direito, revisão), com 6 questões
- reajustamento dos benefícios (preservação do valor real, índice, data) e 3 questões
- reajustamento dos benefícios e revisão, com 4 questões
- tempo de contribuição: o que conta, cômputo, indenização, tempo fictício e prova, com 5 questões
- abono anual (13º): quem recebe, base de cálculo e pagamento, com 3 questões
- direito ao melhor benefício e direito adquirido, com 4 questões
- atividade rural e regime de economia familiar (conceito, prova, período) e 5 questões
- contagem recíproca entre regimes e certidão de tempo, com 4 questões
- serviços da previdência: serviço social e reabilitação profissional, com 4 questões
- habilitação e reabilitação profissional (finalidade, obrigatoriedade do segurado, retorno) e 4 questões
- teto e piso dos benefícios e salário mínimo como base, com 3 questões

### 2. `direito-ambiental.licenciamento-responsabilidade` — Licenciamento, EIA/RIMA e responsabilidade ambiental
Arquivo atual: `conteudo/materias/direito-ambiental/licenciamento-responsabilidade.json` · 56 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre o conceito legal e doutrinário de dano ambiental (lesão a recursos ambientais e à sua função ecológica; dano patrimonial e extrapatrimonial; dano individual e coletivo/difuso) e 5 questões
- seção com o conceito de dano ambiental (degradação, lesão ao equilíbrio ecológico, dano ambiental puro, dano reflexo, extrapatrimonial) e 4 questões de identificação
- licenças para fins específicos: licença ambiental simplificada, por adesão e compromisso, corretiva, de operação em fase única e licenças para atividades distintas, e 5 questões
- seção sobre função e natureza jurídica do EIA (instrumento preventivo, prévio ao licenciamento, ato do procedimento e não decisão final, discricionariedade técnica do órgão, publicidade) e 4 questões
- competência legislativa sobre o EIA: competência concorrente (CF, art. 24, VI, VII e VIII), normas gerais da União e suplementar dos estados, e 4 questões
- competência administrativa para exigir o EIA: órgão licenciador conforme a LC 140/2011, possibilidade de estados e municípios exigirem e o papel do CONAMA; 4 questões
- papel de estados e municípios na exigência do EIA e na legislação suplementar local, com a relação com as normas gerais federais; 4 questões
- competência do CONAMA (Lei 6.938/1981, art. 8º) para fixar normas e critérios sobre EIA e licenciamento, limites do poder normativo da resolução e 4 questões
- relação entre licitações/obras públicas e o EIA: exigência de licença ambiental para obras licitadas, projeto básico com impacto ambiental e responsabilidade na Lei 14.133/2021; 4 questões
- seção sobre o dano ambiental (conceito, espécies, nexo causal, teorias do risco, dano futuro e dano residual) e 6 questões
- apuração do dano ambiental: perícia, inquérito civil, termo de ajustamento de conduta, quantificação e prova do nexo; 5 questões
- poder de polícia ambiental: conceito, atributos, fundamento no art. 225 e limites ao uso da propriedade, órgãos do SISNAMA que o exercem; 4 questões

### 3. `processo-civil.procedimento-comum` — Procedimento comum
Arquivo atual: `conteudo/materias/processo-civil/procedimento-comum.json` · 34 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre a resposta do réu: contestação (prazo, princípio da eventualidade, ônus da impugnação especificada, preliminares do art. 337), reconvenção, revelia e seus efeitos, e 10 questões
- revelia: conceito, efeitos (presunção de veracidade, intimação, intervenção do revel) e hipóteses em que os efeitos não se produzem (arts. 344 a 346), com 6 questões
- contestação (requisitos e prazo), reconvenção (cabimento e processamento) e revelia (efeitos e exceções), com 8 questões
- reconvenção: cabimento, requisitos, conexão, legitimidade e processamento (art. 343), com 5 questões
- revelia: conceito, efeitos, hipóteses de não produção de efeitos e consequências processuais (arts. 344 a 346), com 6 questões
- contestação: prazo, requisitos, princípio da eventualidade, ônus da impugnação especificada, preliminares e defesa de mérito, com 8 questões
- resposta do réu (contestação, reconvenção, revelia) e, após o saneamento, julgamento e sentença com coisa julgada ligados ao rito, com 10 questões
- resposta do réu, revelia e fases seguintes (providências preliminares, provas, audiência, sentença e coisa julgada), com 10 questões
- contestação, reconvenção e revelia, e provas no procedimento comum, com 8 questões
- defesa processual do réu (preliminares do art. 337 e defesa contra o processo), com 6 questões
- defesa de mérito (direta e indireta), ônus da impugnação especificada e fatos novos, com 5 questões
- contestação e seus requisitos: prazo, forma, princípio da eventualidade, preliminares e mérito, com 6 questões

### 4. `direito-eleitoral.partidos-propaganda` — Partidos políticos, eleições, propaganda e prestação de contas
Arquivo atual: `conteudo/materias/direito-eleitoral/partidos-propaganda.json` · 41 tópicos de edital pedem mais conteúdo

Falta:
- condutas vedadas a agentes públicos, captação ilícita de sufrágio em detalhe, sistema eletrônico de votação e totalização e 8 questões sobre a Lei das Eleições
- seção sobre sistemas partidários (bipartidarismo, multipartidarismo, partido dominante, fragmentação) e 4 questões
- órgãos partidários (diretórios e comissões provisórias, convenções, duração, vigência) e 4 questões
- poder de polícia sobre a propaganda (juiz auxiliar, limites, vedação de censura prévia) e 4 questões
- moderação de conteúdo na propaganda (remoção de conteúdo, deveres das plataformas, desinformação) e 4 questões
- permissões e vedações no dia da eleição (boca de urna, aglomeração, manifestação individual, transporte) e 5 questões
- alterações da Lei 13.165/2015 sobre campanha, financiamento e prazos, com 4 questões
- condutas vedadas a agentes públicos (rol, períodos, sanções) e captação ilícita, com 8 questões
- sistemas partidários e eleitorais na ótica da ciência política (distrital, misto, Duverger, fragmentação) e 5 questões
- capítulo sobre condutas vedadas a agentes públicos em campanha (rol, períodos, sanções) e 8 questões
- suplência, sobras eleitorais e cálculo completo das médias, com 6 questões
- anterioridade eleitoral, calendário e substituição de candidaturas, com 5 questões

### 5. `administracao-publica.politicas-publicas` — Políticas públicas: conceitos, ciclo, modelos e avaliação
Arquivo atual: `conteudo/materias/administracao-publica/politicas-publicas.json` · 51 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre as políticas públicas no Estado brasileiro contemporâneo: federalismo cooperativo e descentralização pós-1988, democracia participativa (conselhos, conferências, orçamento participativo), sistemas nacionais (SUS, SUAS) e controle social; 6 questões
- papel das instituições nas políticas públicas (neoinstitucionalismo: regras formais e informais, dependência da trajetória, custos de transação, capacidade estatal) e 5 questões
- descentralização e democracia: municipalização, federalismo cooperativo, participação e controle social, vantagens e riscos da descentralização; 5 questões
- políticas públicas baseadas em evidências: uso de dados e avaliações de impacto, revisões sistemáticas, experimentos e limites da evidência na decisão; 5 questões
- novos arranjos federativos e descentralização das políticas no Brasil (cooperação intergovernamental, consórcios, sistemas nacionais, transferências); 6 questões
- institucionalização das políticas de direitos humanos como políticas de Estado (programas nacionais, marco legal, diferença entre política de governo e de Estado, conselhos); 5 questões
- federalismo e descentralização das políticas no Brasil: repartição de competências, organização dos sistemas nacionais (SUS, SUAS, SNE) e programas nacionais; 6 questões
- teorias e modelos de análise contemporâneos: escolha racional institucional (instituições como regras que reduzem custos de transação e controlam o oportunismo, relação agente-principal); 5 questões
- arranjos institucionais para implementação (coordenação federativa, capacidades estatais, parcerias, consórcios, redes e governança de implementação); 5 questões
- diversidade e inclusão nas políticas públicas: transversalidade, ações afirmativas, interseccionalidade, igualdade e equidade e desenho de políticas para grupos vulneráveis; 5 questões
- instrumentos e alternativas de implementação: fundos, consórcios públicos e transferências obrigatórias, mais instrumentos como regulação, subsídios e parcerias; 6 questões
- análise custo-benefício e custo-efetividade, escala de programas e o uso de custo na avaliação de efetividade e impacto, com exemplos numéricos; 6 questões

### 6. `direito-constitucional.stf-cnj-stj` — STF, CNJ e STJ
Arquivo atual: `conteudo/materias/direito-constitucional/stf-cnj-stj.json` · 23 tópicos de edital pedem mais conteúdo

Falta:
- seções sobre disposições gerais do Judiciário (Estatuto da Magistratura, garantias) e sobre o TST, a Justiça do Trabalho e o CSJT, com 12 questões
- seção sobre os demais órgãos do Judiciário (art. 92) e suas competências, além de CNJ, com 6 questões
- seção sobre os demais órgãos do Judiciário (art. 92: TST, TRFs, TRTs, TREs, STM, TJs) e 6 questões
- seção sobre as funções essenciais à Justiça (Ministério Público, advocacia pública, advocacia e defensoria) com 6 questões
- seção sobre corregedorias, ouvidorias e conselhos superiores (CSJT e Conselho da Justiça Federal) como controle interno do Judiciário, com 6 questões
- seção sobre organização geral do Poder Judiciário (art. 92 e disposições gerais) com 6 questões
- seção sobre corregedorias, ouvidorias e conselhos superiores (CSJT e CJF) como controle interno do Judiciário, com 6 questões
- seção sobre o Conselho Nacional do Ministério Público (composição e competências, art. 130-A) com 5 questões
- seção sobre competências originárias e recursais dos tribunais de justiça e dos demais tribunais superiores (TST, TSE, STM), com 6 questões
- seção sobre os TRFs e os juízes federais (composição e competências) com 6 questões
- seção sobre a história do CNJ (criação pela EC 45/2004) e seu funcionamento (Plenário, regimento interno, Presidência, Corregedoria) além da composição por categorias, com 5 questões
- seção sobre a Corregedoria Nacional de Justiça (atribuições do Corregedor Nacional, inspeções, reclamações disciplinares) com 5 questões

### 7. `administracao-geral.comportamento-organizacional` — Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança
Arquivo atual: `conteudo/materias/administracao-geral/comportamento-organizacional.json` · 45 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre relações indivíduo-organização (contrato psicológico, comprometimento, socialização, expectativas recíprocas) e 5 questões
- capítulo sobre atitudes e satisfação no trabalho (componentes da atitude, dissonância cognitiva, determinantes e efeitos da satisfação) e 6 questões
- relacionamento interpessoal e empatia no trabalho em equipe (conceitos, barreiras, feedback) e 4 questões
- capítulo sobre relações indivíduo-organização (contrato psicológico, comprometimento, socialização) e 5 questões
- inovação organizacional (tipos de inovação, criatividade, barreiras e fatores de inovação) e 4 questões
- autoliderança e liderança de equipes (liderança compartilhada, equipes autogeridas) e 4 questões
- competência interpessoal (habilidades, feedback, assertividade) integrada ao gerenciamento de conflitos, com 4 questões
- capítulo sobre o nível individual do comportamento (personalidade, percepção, atitudes, valores) e as variáveis individuais, grupais e organizacionais, com 6 questões
- satisfação e comprometimento no trabalho (conceito, determinantes, consequências, modelo de três componentes) e 5 questões
- competência interpessoal (habilidades, feedback, assertividade) e 4 questões
- capítulo sobre variáveis individuais (personalidade, percepção, atitudes, valores) no contexto organizacional e 6 questões
- competência interpessoal (habilidades, feedback, assertividade) ligada a conflitos e mudança, com 4 questões

### 8. `estatistica.inferencia-testes-hipoteses` — Inferência estatística e testes de hipóteses
Arquivo atual: `conteudo/materias/estatistica/inferencia-testes-hipoteses.json` · 33 tópicos de edital pedem mais conteúdo

Falta:
- teste z para proporção e roteiro de teste para uma média (variância conhecida ou não), comparação de duas médias e de duas proporções, com 6 questões de proporção
- teste z para uma proporção (estatística, região crítica, p-valor) e teste para diferença de médias e proporções entre dois grupos, com 6 questões
- intervalos de credibilidade (inferência bayesiana: priori, posteriori, credibilidade x confiança) e 4 questões; o IC frequentista já está coberto
- hipóteses simples e compostas (definição, lema de Neyman-Pearson em noções) e 4 questões; nível, potência, teste t e qui-quadrado já cobertos
- distribuições amostrais da média e da proporção (média, erro padrão, TCL) e características de um bom estimador (eficiência, suficiência), com 6 questões
- cálculo do tamanho da amostra para estimar média e proporção (n=(zσ/E)², n=z²p(1-p)/E², pior caso p=0,5) e 6 questões; o IC para média e proporção já está coberto
- teste z para proporção populacional (hipóteses, estatística, decisão) e 5 questões; erros, significância e potência já cobertos
- métodos de estimação (momentos, máxima verossimilhança, mínimos quadrados), suficiência e propriedades (eficiência, consistência, EQM), com exemplos e 8 questões
- comparação de duas médias (amostras independentes com variâncias iguais ou diferentes; pareadas) e de duas proporções, com estatística de teste e 6 questões; o pareado só aparece em exemplos
- testes não paramétricos (sinais, Wilcoxon, Mann-Whitney, Kruskal-Wallis, Kolmogorov-Smirnov), quando usar paramétrico ou não, e 6 questões
- estimador de máxima verossimilhança (função de verossimilhança, log-verossimilhança, derivação para Bernoulli, Poisson e normal, propriedades assintóticas) e 6 questões
- estimação por momentos, mínimos quadrados ordinários e máxima verossimilhança, com exemplos e 8 questões

### 9. `medicina-legal.traumatologia-tanatologia` — Medicina Legal: traumatologia, tanatologia e sexologia forense
Arquivo atual: `conteudo/materias/medicina-legal/traumatologia-tanatologia.json` · 43 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre inumação, exumação, cremação e embalsamamento: conceitos, finalidades, regras do CPP para a exumação (autoridade, auto, presença de interessados) e prazos de sepultamento; hoje exumação tem um parágrafo e as demais não aparecem; 5 questões
- seção sobre baropatias: aumento e diminuição da pressão atmosférica, barotrauma, doença descompressiva, e efeitos de explosões (onda de choque, blast); 4 questões; calor, eletricidade e química já estão cobertos
- seção sobre projéteis de arma de fogo de alta energia (fuzis): cavitação temporária, transferência de energia, lesões de entrada e saída, trajetória, diferença para armas comuns; 4 questões
- seção sobre necropsia médico-legal: indicações, requisitos (prazo, obrigatoriedade, quem realiza), técnicas (exame externo e interno) e sua diferença para a necropsia clínica; hoje só há uma linha sobre o prazo de seis horas; 6 questões
- seção sobre as classificações da morte (aparente, relativa, intermediária, real, anatômica, histológica): conceito e critérios de cada uma; 4 questões
- seção sobre aborto médico-legal: conceito, espécies (espontâneo, acidental, provocado, legal), sinais periciais do aborto provocado e perícia na gestante e no produto da concepção; 5 questões; incluir abandono de recém-nascido
- incluir trajetória do projétil e efeitos lesionais da energia cinética (cavitação temporária), além do que já existe sobre entrada, saída e distância de disparo; 4 questões
- tipos de violência sexual (estupro, estupro de vulnerável, importunação sexual, assédio) sob a ótica médico-legal e coleta, acondicionamento e preservação de vestígios (cadeia de custódia, DNA, swabs), com atendimento à vítima; 6 questões
- aspectos médico-legais da violência doméstica e familiar (mulher, criança, adolescente, idoso e pessoa com deficiência): padrões de lesões, notificação compulsória, laudo e proteção; hoje só crianças e idosos têm trecho; 6 questões
- seção sobre exame perinecroscópico: exame do local e do cadáver in situ antes da remoção, posição, vestígios, sinais de morte, e registro; 4 questões
- seção sobre armas, instrumentos e munições: arma branca e de fogo, partes do cartucho (estojo, projétil, carga de projeção, espoleta), calibres e tipos de projétil; hoje a matéria exclui balística; 4 questões
- seção sobre ação explosiva (explosões, lesões por onda de choque, queimaduras e estilhaços), pois ação termoquímica e elétrica já estão cobertas; 3 questões

### 10. `direito-administrativo.poderes-administrativos` — Poderes administrativos e abuso de poder
Arquivo atual: `conteudo/materias/direito-administrativo/poderes-administrativos.json` · 34 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre os principais setores de atuação da polícia administrativa (sanitária, ambiental, edilícia, de trânsito, de costumes, de atividades econômicas) e 5 questões
- seção sobre liberdades públicas e seus limites frente ao poder de polícia (direitos fundamentais restringíveis, reserva de lei, proporcionalidade) e 4 questões
- seção sobre liberdades públicas e poder de polícia (restrição de direitos fundamentais, reserva legal, limites) e 4 questões
- seção sobre polícia edilícia (controle de construções e edificações, alvará, embargo e demolição, competência municipal) e 4 questões
- capítulo curto sobre os princípios administrativos do art. 37 e da Lei 9.784/1999, com 5 questões
- seção própria sobre o dever de eficiência (conceito, art. 37 da Constituição, avaliação de desempenho, relação com o poder-dever) e 4 questões
- seção própria sobre o dever de prestação de contas (parágrafo único do art. 70 da Constituição, controle, transparência) e 4 questões
- seção com jurisprudência aplicada dos tribunais superiores sobre poderes e deveres (poder de polícia, delegação, controle do mérito, regulamentos) e 6 questões
- seção sobre limites, extensão e controle do poder regulamentar (controle legislativo por sustação, controle judicial, ADI e ilegalidade x inconstitucionalidade) e 4 questões
- seção sobre poder de polícia e regulação (agências reguladoras, poder normativo setorial, regulação econômica x polícia) e 5 questões
- capítulo curto sobre os princípios do art. 37 da Constituição e do art. 2º da Lei 9.784/1999 ligados ao exercício dos poderes, com 5 questões
- seção sobre liberdades públicas e setores de atuação da polícia administrativa e 6 questões

### 11. `direito-administrativo.bens-publicos` — Bens públicos
Arquivo atual: `conteudo/materias/direito-administrativo/bens-publicos.json` · 40 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre administração, aquisição (compra, doação, desapropriação, usucapião, acessão) e alienação de bens públicos, aforamento e concessão de domínio pleno, e legislação patrimonial da União, com 6 questões
- seção sobre regime de vias públicas, cemitérios públicos e portos (natureza, uso, poder de polícia e concessão), com 5 questões
- seção sobre terrenos de marinha e acrescidos (conceito, linha do preamar médio de 1831, domínio da União, foro, taxa de ocupação e laudêmio), com 5 questões
- seção sobre modos de aquisição de bens públicos (contrato, desapropriação, usucapião de bens privados, acessão, herança vacante, arrematação) com 4 questões
- seção sobre administração e gestão dos bens públicos (competência, inventário, cadastro, zeladoria) e disciplina da utilização, com 4 questões
- seção sobre a não oneração (vedação de hipoteca, penhor e anticrese), regime de execução por precatório e sobre a Súmula do STF sobre usucapião, com 5 questões
- seção sobre ocupação, aforamento (enfiteuse), concessão de direito real de uso e concessão de domínio pleno, com 5 questões
- seção sobre aquisição de bens públicos (contrato, desapropriação, doação, usucapião, acessão) com 4 questões
- seção sobre administração e aquisição de bens públicos, com 4 questões
- seção sobre ocupação, aforamento e concessão de domínio pleno, além de concessão de direito real de uso, com 5 questões
- seção sobre aquisição de bens públicos, com 4 questões
- seção sobre gestão do patrimônio público (inventário, registro, cessão, conservação, SPU), com 4 questões

### 12. `direito-administrativo.licitacao-principios-modalidades` — Licitação na Lei 14.133/2021: conceito, princípios e modalidades
Arquivo atual: `conteudo/materias/direito-administrativo/licitacao-principios-modalidades.json` · 55 tópicos de edital pedem mais conteúdo

Falta:
- execução e fiscalização contratual, sanções, anulação e revogação, convênios e consórcios, com questões
- contratações de TIC, sustentabilidade, contratações digitais/inovadoras e jurisprudência dos tribunais, com questões
- capítulo sobre sistema de registro de preços (ata, adesão, vigência) e decreto regulamentador, com questões
- detalhamento do rito: fases, edital, habilitação, julgamento e recursos, com mais questões
- capítulo sobre pregão eletrônico (fase de lances, desenrolar) e questões

### 13. `informatica.criptografia-certificacao` — Criptografia, certificação digital e segurança de redes
Arquivo atual: `conteudo/materias/informatica/criptografia-certificacao.json` · 58 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre TLS/SSL (handshake, certificado do servidor, versões, HTTPS) com 4 questões
- seção sobre SIEM (coleta e correlação de logs, alertas) e 3 questões que o distingam de IDS/IPS
- seção sobre criptografia em repouso (disco, banco, backup) versus em trânsito, protocolos de cada caso e 4 questões
- seção sobre tokens, smartcards e senhas de uso único (OTP) e 3 questões
- capítulo sobre SSL/TLS: handshake, cifras negociadas, certificado do servidor, HTTPS e 4 questões
- seção sobre autenticação (fatores, MFA, biometria) ao lado de firewall e IDS, e 3 questões
- capítulo sobre SSL/TLS (handshake, versões) e aprofundamento do IPsec (IKE, modos transporte e túnel) com 4 questões
- seções sobre segurança física e lógica e sobre esteganografia (técnicas, LSB) com exemplos e 4 questões
- seção sobre protocolos criptográficos (TLS, IPsec/IKE, SSH, S/MIME, PGP) com o papel de cada um e 4 questões
- seção sobre esteganografia (técnicas, LSB) e criptoanálise (força bruta, texto claro conhecido, análise de frequência) e 4 questões
- seção sobre ACL, NAT/PAT e roteamento no perímetro de segurança e 4 questões
- IKE/ISAKMP e associações de segurança, modos transporte e túnel do IPsec, PPTP e L2TP em detalhe, e 4 questões

### 14. `contabilidade.estrutura-conceitual-cpc` — Estrutura conceitual, DMPL e pronunciamentos do CPC
Arquivo atual: `conteudo/materias/contabilidade/estrutura-conceitual-cpc.json` · 38 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre princípios contábeis (Resolução CFC 750: entidade, continuidade, oportunidade, registro pelo valor original, competência, prudência) e sobre a estrutura das NBC (NBC TG, ITG, PG, TSP) com 6 questões
- capítulo sobre a Lei das S.A. (estrutura e elaboração das demonstrações, convergência pelas Leis 11.638/2007 e 11.941/2009) e relação com os CPCs, com 6 questões
- capítulo sobre a legislação societária (Lei 6.404/76 e alterações) e sobre os princípios fundamentais de contabilidade, com 6 questões
- seção sobre os ramos aplicados da contabilidade (financeira, gerencial, de custos, pública, auditoria, perícia) e 3 questões; objetivos e usuários já existem
- capítulo sobre princípios contábeis (Resolução CFC 750) e estrutura das NBC, com 6 questões
- capítulo sobre a Lei das S.A. (demonstrações, convergência aos padrões internacionais) e sua relação com os CPCs, com 6 questões
- seção sobre as Normas Brasileiras de Contabilidade (estrutura do CFC, NBC TG, NBC TSP, ITG, NBC PG e PA, hierarquia) e 5 questões
- seção sobre princípios de contabilidade (Resolução CFC 750 e sua incorporação à Estrutura Conceitual) com 5 questões
- seção sobre as NBC emitidas pelo CFC (NBC TG, TSP, ITG, PG, PA) e sua relação com os CPCs, com 5 questões
- seção sobre a legislação societária (Leis 11.638/2007 e 11.941/2009 e Lei 6.404/76) e a convergência às IFRS, com 5 questões
- seção sobre as NBC (estrutura do CFC, NBC TG, TSP, ITG, PG, PA) e 5 questões
- capítulo sobre a legislação societária (Lei 6.404/76) e sobre os princípios de contabilidade, com 6 questões

### 15. `contabilidade.balanco-dre` — Balanço patrimonial e demonstração do resultado
Arquivo atual: `conteudo/materias/contabilidade/balanco-dre.json` · 43 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre o conteúdo e a estrutura dos grupos do ativo, passivo e patrimônio líquido (art. 178 da Lei das S.A.), com critérios de avaliação de cada grupo e 8 questões
- seção sobre contas a receber: conceito, conteúdo, ajuste a valor presente, perdas estimadas e critérios de avaliação, com 5 questões
- seção sobre realizável a longo prazo / ativo não circulante realizável: conceito, classificação e contas típicas, com 5 questões
- capítulo sobre as mudanças das Leis 11.638/2007 e 11.941/2009 (novos grupos do ativo, ajuste a valor presente, avaliação a valor justo, extinção da reavaliação) e 8 questões
- seção sobre disponibilidades: caixa, bancos e equivalentes de caixa, conteúdo, classificação e critérios de avaliação, com 5 questões
- seção sobre reconhecimento de receita (cinco etapas do CPC 47, obrigações de desempenho, momento do reconhecimento) e 6 questões
- capítulo sobre a elaboração das demonstrações pela Lei das S.A. e pelo CPC 26 (demonstrações obrigatórias, características qualitativas, princípios) e 6 questões
- capítulo sobre a elaboração das demonstrações pela legislação societária e pelas normas brasileiras de contabilidade (CPC 26 e ITG) e 6 questões
- capítulo sobre critérios de avaliação e contabilização de ativos e passivos (custo, valor justo, valor presente, valor realizável) e 8 questões
- capítulo sobre noções de legislação societária (Lei das S.A. e alterações das Leis 11.638/2007 e 11.941/2009) aplicadas às demonstrações, e 8 questões
- capítulo sobre grupos e subgrupos do balanço pela Lei das S.A., classificação de contas e critérios de avaliação do ativo e passivo, com 8 questões
- seção sobre provisão para IR e CSLL, participações de empregados e administradores, lucro líquido e lucro por ação, com 5 questões

### 16. `conhecimentos-bancarios.produtos-servicos-bancarios` — Produtos e serviços bancários
Arquivo atual: `conteudo/materias/conhecimentos-bancarios/produtos-servicos-bancarios.json` · 34 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre crédito direto ao consumidor (CDC): finalidade, financeira ou banco, taxas, IOF, alienação fiduciária do bem, diferença para crédito pessoal, e 5 questões
- capítulo sobre depósitos à vista, a prazo (CDB e RDB: diferenças, liquidez) e letra de câmbio (aceite, emissor financeira), e 6 questões
- capítulo sobre cobrança bancária e pagamento de títulos e carnês (boleto, compensação, cobrança simples e caucionada, tarifas) e 5 questões
- capítulo sobre transferências de fundos (TED, DOC, Pix, débito automático): regras, horários, limites, e 5 questões
- capítulo sobre canais de atendimento: home e office banking, remote banking, banco virtual e dinheiro de plástico (cartões), com 5 questões
- capítulo sobre financiamento de capital de giro (finalidade, prazo, garantias, custos, diferença para capital fixo) e 5 questões
- capítulo sobre leasing: arrendamento financeiro e operacional, opção de compra, VRG, bens, sociedade arrendadora, e 6 questões
- capítulo sobre financiamento de capital fixo (investimentos em máquinas e imóveis, prazos longos, BNDES, garantias) e 5 questões
- seção sobre conta garantida (limite rotativo para capital de giro, contrato, encargos sobre saldo devedor) e 4 questões
- seções sobre crédito direto ao consumidor, crédito rural e investimentos em fundos além dos produtos já tratados, e 6 questões
- seção sobre hot money (capital de giro de curtíssimo prazo, lastro em notas promissórias, taxa de mercado) e 3 questões
- seção sobre desconto de títulos (duplicatas e cheques pré-datados, taxa de desconto, direito de regresso) e 4 questões

### 17. `informatica.html-css-web` — HTML, CSS, HTTP e APIs REST
Arquivo atual: `conteudo/materias/informatica/html-css-web.json` · 49 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre WebSocket (comunicação bidirecional, handshake, diferença para HTTP e polling) e 4 questões
- capítulo sobre usabilidade e acessibilidade na internet (heurísticas, WCAG, eMAG) e padrões W3C, e 6 questões
- capítulos sobre XML (sintaxe, DTD, XSD), DHTML e web services SOAP/WSDL, e 6 questões
- capítulo sobre XML (sintaxe, bem-formado, XSD) e comparação com JSON, e 5 questões
- capítulo sobre web services SOAP (envelope, WSDL, UDDI) comparados ao REST, e 5 questões
- capítulo sobre arquitetura web: HTTP/2, gRPC, WebSockets, TLS, proxy, DNS, balanceamento de carga, tolerância a falhas e escalabilidade, e 8 questões
- capítulo sobre GraphQL (consultas, esquema, resolvers, diferenças para REST) e 5 questões
- seção sobre DHTML (HTML, CSS e JavaScript combinados para páginas dinâmicas) e 3 questões
- capítulos sobre XML, XSD e XSLT (estrutura, validação e transformação) e 6 questões
- capítulos sobre SOAP, AngularJS e microsserviços (conceito, comunicação, vantagens e desafios), e 6 questões
- capítulo sobre arquitetura web: HTTP/2, gRPC, WebSockets, TLS, proxy, cache, DNS e balanceamento de carga, e 8 questões
- capítulo sobre GraphQL (esquema, queries e mutations, comparação com REST) e 5 questões

### 18. `conhecimentos-bancarios.sistema-financeiro-nacional` — Sistema Financeiro Nacional
Arquivo atual: `conteudo/materias/conhecimentos-bancarios/sistema-financeiro-nacional.json` · 29 tópicos de edital pedem mais conteúdo

Falta:
- conceito legal de instituição financeira (art. 17 da Lei 4.595/1964) e equiparações; instituições financeiras públicas (BB, Caixa, BNDES, BNB, BASA) x privadas; autorização e fiscalização, com 5 questões
- bancos comerciais cooperativos (bancos cooperativos): natureza, relação com as cooperativas centrais, atuação e supervisão, com 3 questões
- bancos de investimento: operações permitidas (subscrição, underwriting, fusões e aquisições), vedação de depósitos à vista, captação, com 4 questões
- bancos de desenvolvimento e bancos regionais (BNDES, BNB, BASA): finalidade, fontes de recursos e atuação no fomento, com 4 questões
- sociedades de crédito, financiamento e investimento (financeiras): operações permitidas, captação por letras de câmbio e atuação no crédito ao consumidor, com 3 questões
- sociedades de arrendamento mercantil (leasing): conceito, modalidades, atuação e supervisão, com 3 questões
- sociedades corretoras de títulos e valores mobiliários: atividades, intermediação em bolsa, regulação e fiscalização, com 3 questões
- sociedades distribuidoras de títulos e valores mobiliários: atividades, diferenças em relação às corretoras e fiscalização, com 3 questões
- sociedades de crédito imobiliário: finalidade, captação e operações de financiamento habitacional dentro do SFH/SFI, com 3 questões
- associações de poupança e empréstimo: natureza mutualista, captação em poupança e financiamento imobiliário, com 3 questões
- intervenção, liquidação extrajudicial e regime de administração especial temporária (Lei 6.024/1974 e correlatas): hipóteses, efeitos, administrador, prazos e indisponibilidade de bens, com 6 questões
- sistema de bancos-sombra (shadow banking): conceito, entidades e atividades fora da regulação bancária, riscos sistêmicos e debate regulatório (inclusive fintechs e fundos), com 5 questões
