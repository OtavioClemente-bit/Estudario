# Atualizar matérias — pedido 14 de 18 (09/10/2026)

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

### 1. `direito-constitucional.advocacia-defensoria` — Advocacia Pública, Advocacia e Defensoria Pública
Arquivo atual: `conteudo/materias/direito-constitucional/advocacia-defensoria.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- trecho sobre história, estrutura e órgãos da AGU conforme a Lei Complementar 73/1993 (Advogado-Geral, Consultoria-Geral, PGU, órgãos de direção e de execução), com 8 questões
- trecho sobre competências do Advogado-Geral da União previstas na lei orgânica (edição de súmulas, pareceres vinculantes, representação perante o STF), com 4 questões
- trecho sobre a Procuradoria-Geral Federal (representação de autarquias e fundações públicas, carreira de procurador federal), com 5 questões
- trecho sobre consultoria jurídica e hipóteses de manifestação obrigatória (parecer obrigatório e vinculante, responsabilidade do parecerista), com 5 questões
- trecho sobre a organização administrativa da AGU (órgãos de direção superior, de execução e vinculados), com 5 questões
- trecho sobre manifestação jurídica obrigatória no controle da administração e responsabilidade do parecerista, com 4 questões
- trecho sobre a atuação da Defensoria Pública da União perante a Justiça Militar da União, com 3 questões
- trecho sobre consultoria, manifestação obrigatória e responsabilidade de pareceristas e administradores (parecer vinculante, dolo ou erro grosseiro), com 5 questões
- trecho sobre a Procuradoria-Geral da Fazenda Nacional além do art. 131, §3º (inscrição em dívida ativa da União, representação em execução fiscal, estrutura), com 5 questões

### 2. `biologia.citologia-genetica` — Citologia e genética
Arquivo atual: `conteudo/materias/biologia/citologia-genetica.json` · 9 tópicos de edital pedem mais conteúdo

Falta:
- bioquímica celular: água, sais minerais, carboidratos, lipídios, proteínas e vitaminas, com 8 questões
- gametogênese (espermatogênese e ovogênese) e formação dos gametas, com 5 questões
- segunda lei de Mendel e poliibridismo (di-hibridismo, proporção 9:3:3:1, cálculos), com 6 questões
- sistema ABO e fator Rh: alelos múltiplos, codominância, eritroblastose fetal, com 6 questões
- herança ligada ao sexo e influenciada pelo sexo (daltonismo, hemofilia, genes holândricos), com 6 questões
- exame de DNA para determinação de paternidade (marcadores, interpretação do resultado), com 4 questões
- mutações gênicas e cromossômicas (numéricas e estruturais) e suas consequências, com 6 questões
- aspectos químicos da célula (composição química e biomoléculas) além da estrutura, com 6 questões
- doenças hereditárias (padrões de herança autossômica e ligada ao sexo, heredogramas, exemplos clínicos) e 6 questões

### 3. `direito-trabalho.acidente-trabalho` — Acidente do trabalho
Arquivo atual: `conteudo/materias/direito-trabalho/acidente-trabalho.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre prevenção de acidentes do trabalho (hierarquia de controles, atos e condições inseguras, papel da CIPA e dos serviços de saúde) e 5 questões
- seção sobre o seguro de acidentes do trabalho (RAT e FAP, benefícios acidentários: auxílio por incapacidade temporária, aposentadoria por incapacidade, auxílio-acidente e pensão) e 6 questões
- seção sobre o seguro de acidentes do trabalho (RAT e FAP, benefícios acidentários e seu financiamento) e 6 questões
- seção sobre causas de acidentes (fator pessoal, condição insegura, ato inseguro) e consequências (afastamento, sequelas, custos), com 5 questões
- seção sobre investigação e análise de acidentes (coleta de dados, árvore de causas, medidas corretivas) e registro, além da CAT, com 5 questões
- seção sobre conceitos médicos de doença ocupacional e nexo técnico (NTEP, listas de doenças relacionadas ao trabalho) e epidemiologia dos acidentes, com 6 questões
- seção sobre indenização material (pensão mensal, despesas de tratamento, lucros cessantes) e moral e estética em acidente de trabalho, com 6 questões
- seção sobre o conceito técnico de acidente (NBR 14280), investigação, taxas de frequência e de gravidade e estatísticas de acidentes, com 8 questões

### 4. `portugues.variacao-linguistica-norma-linguistica` — Variação linguística e norma linguística
Arquivo atual: `conteudo/materias/portugues/variacao-linguistica-norma-linguistica.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- análise de atos oficiais (ofício, requerimento, portaria: impessoalidade, formalidade, padronização) com exemplos e cerca de 5 questões; níveis de linguagem e adequação já estão cobertos
- implicações pedagógicas da variação para o ensino de Língua Portuguesa (BNCC, pedagogia da variação, ensino de gramática, avaliação, preconceito linguístico na escola) com questões de nível docente
- relação entre oralidade e escrita: continuum fala-escrita, marcas de oralidade, planejamento, retextualização e mitos da dicotomia, com cerca de 5 questões; hoje só a variação diamésica é citada
- linguagem da Internet: internetês, abreviações, emojis, memes, gêneros digitais e seus efeitos na escrita, com cerca de 5 questões
- linguagem publicitária: adequação ao público-alvo, desvios da norma como recurso estilístico, apelo e persuasão, com exemplos e cerca de 5 questões
- uso literário de coloquialismo e regionalismo na fala de personagens (discurso direto, regionalismo literário, marcas de oralidade) com trechos comentados e cerca de 5 questões
- gírias, neologismos, abreviações digitais e escrita espontânea interpessoal (inovações lexicais expressivas) com exemplos e cerca de 5 questões

### 5. `informatica.sistemas-informacao` — Sistemas de informação
Arquivo atual: `conteudo/materias/informatica/sistemas-informacao.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- capítulo sobre inteligência de negócios (data warehouse, OLAP, dashboards) e gestão de conteúdo/ECM, com 6 questões
- capítulo sobre fases e ciclo de vida de um sistema de informação (levantamento, análise, projeto, implementação, testes, implantação, manutenção), com 6 questões
- capítulo sobre componentes de um SI (hardware, software, redes, dados e pessoas) com 5 questões
- capítulo sobre arquitetura e tecnologias de SI (camadas, cliente-servidor, web, nuvem) com 6 questões
- capítulo sobre ciclo de vida de SI (cascata, iterativo, fases) e tipos de SI, com 6 questões

### 6. `ciencias.quimica-organica-funcoes-nomenclatura` — Química orgânica: funções e nomenclatura
Arquivo atual: `conteudo/materias/ciencias/quimica-organica-funcoes-nomenclatura.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- propriedades físicas (ponto de ebulição, solubilidade, polaridade) e químicas (acidez de fenóis e ácidos, basicidade de aminas) de cada função, com 10 questões
- propriedades físicas e químicas das funções (ponto de ebulição, solubilidade, acidez/basicidade) para compostos de 1 a 8 carbonos, com 10 questões
- hibridação sp3/sp2/sp, estados de oxidação do carbono, ligações sigma e pi e geometria molecular, com 8 questões
- estrutura e ligações em compostos orgânicos (hibridação, sigma/pi, ressonância, polaridade), 6 questões
- principais reações orgânicas (substituição, adição, eliminação, oxidação, esterificação) e sínteses de cada função, com 10 questões; ver ciencias.quimica-organica-isomeria-reacoes
- propriedades físicas e químicas dos compostos orgânicos (ponto de ebulição, solubilidade, acidez), com 8 questões
- hibridação, estados de oxidação, ligações sigma e pi e geometria molecular, com 8 questões

### 7. `ciencias.ligacoes-quimicas` — Ligações químicas
Arquivo atual: `conteudo/materias/ciencias/ligacoes-quimicas.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- forças intermoleculares (dipolo, ligação de hidrogênio, London) e propriedades físicas, com 10 questões
- ligação metálica (mar de elétrons, ligas, propriedades dos metais) com 8 questões
- geometria molecular: teoria da repulsão dos pares eletrônicos (VSEPR), hibridação e teoria da ligação de valência, com 12 questões
- geometria molecular e forças intermoleculares, com 12 questões
- ligações intermoleculares (dipolo-dipolo, ligação de hidrogênio, London) e metálica, com 10 questões
- ligações e forças intermoleculares nos estados sólido, líquido e gasoso, com 10 questões

### 8. `informatica.orientacao-objetos` — Orientação a objetos
Arquivo atual: `conteudo/materias/informatica/orientacao-objetos.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- Seção sobre programação estruturada e procedural (sequência, decisão e repetição, modularização com funções, dados globais x locais) e sua comparação com a orientação a objetos, com 5 questões.
- Seção comparando linguagens orientadas a objetos e procedurais (estado e dados globais x encapsulados, funções x métodos, reuso por herança), com exemplos em C e Java e 5 questões.
- Seção comparando linguagens orientadas a objetos e procedurais (modularização, reuso, estado, exemplos em C e Java), com 5 questões.
- Seção sobre os paradigmas estruturado e funcional (funções puras, imutabilidade, funções de ordem superior, recursão) em comparação com o orientado a objetos, com 6 questões.
- Seção sobre GRASP (especialista na informação, criador, controlador, baixo acoplamento, alta coesão, polimorfismo, variações protegidas, indireção, fabricação pura) relacionada ao SOLID, com 7 questões.
- Seção sobre o paradigma estruturado (sequência, seleção, iteração, modularização, ausência de encapsulamento) comparado ao orientado a objetos, com 5 questões.
- Seção sobre programação funcional (funções puras, imutabilidade, funções de primeira classe, ordem superior, recursão, lambda) em contraste com a orientação a objetos, com 6 questões.
- Seção sobre programação estruturada (sequência, decisão, repetição, funções, passagem de parâmetros) comparada à orientação a objetos, com 5 questões.

### 9. `informatica.linux` — Linux: conceitos, diretórios, permissões e comandos
Arquivo atual: `conteudo/materias/informatica/linux.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- Seção sobre software livre e código aberto: as quatro liberdades, licenças (GPL, LGPL, BSD, MIT, Apache), copyleft, modelos de negócio, padrões abertos e projetos, com 10 questões
- Capítulo de operação e configuração do Linux (serviços e systemd básico, configuração de rede, discos e particionamento, montagem, logs, agendamento com cron) com 12 questões
- Capítulo de operação e configuração do Linux (serviços e systemd básico, configuração de rede, discos e particionamento, montagem, logs, agendamento com cron) com 12 questões, específico de Red Hat
- Seção sobre sistemas de arquivos (ext4, Btrfs, XFS: journaling, limites), conceitos de LVM (PV, VG, LV) e gerenciamento de processos (prioridade, nice, jobs), com 10 questões
- Seção sobre instalação do Linux (particionamento, boot, gerenciador GRUB) e administração básica (usuários, serviços, atualização), com 10 questões
- Seção sobre o Red Hat Enterprise Linux: subscription, yum/dnf e repositórios, SELinux, firewalld e serviços, com 10 questões
- Seção sobre systemd (unidades, systemctl, targets, journalctl) e scripts em Bash e Python, com 10 questões
- Seção sobre CentOS, Red Hat e Oracle Linux: diferenças entre as distribuições da família Red Hat, gerenciador de pacotes, SELinux e ciclo de suporte, com 8 questões

### 10. `direito-trabalho.contrato-individual-trabalho` — Contrato individual de trabalho
Arquivo atual: `conteudo/materias/direito-trabalho/contrato-individual-trabalho.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre renúncia, transação, invalidade dos atos e efeitos do contrato (nulidades, princípio da irrenunciabilidade), com 6 questões
- seção sobre contratos afins (empreitada, representação comercial, estágio, trabalho autônomo, cooperativa, terceirização) e 6 questões
- capítulo sobre estabilidades e garantias provisórias de emprego (dirigente sindical, cipeiro, gestante, acidentado) e 8 questões
- capítulos sobre remuneração, férias e duração do trabalho com questões
- seção sobre o contrato de trabalho dos empregados de serventias extrajudiciais (regime celetista, Lei dos Cartórios) e 5 questões
- capítulo sobre estabilidade e garantias provisórias, profissões regulamentadas e transferência, com 10 questões
- seção sobre profissões regulamentadas (jornalista, médico, advogado, bancário, professor, etc.) e 6 questões
- capítulos sobre salários especiais, organização sindical, convenções e acordos coletivos e mediação e arbitragem, com 10 questões

### 11. `fisica.ondas-som` — Ondas e som
Arquivo atual: `conteudo/materias/fisica/ondas-som.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- seção sobre ultrassom e sobre lentes (que pertencem à óptica geométrica), com 5 questões
- capítulo de acústica e áudio em nível de engenharia eletrônica (nível sonoro em decibéis, espectro, filtros, microfones), com 8 questões
- capítulo sobre espectro eletromagnético e propagação das ondas eletromagnéticas, com 6 questões
- capítulo de óptica física (natureza ondulatória da luz, polarização, interferência e difração da luz) e 8 questões
- seção sobre interferência da luz (experimento de Young, interferência em películas finas) e 5 questões
- seção sobre difração da luz (fenda simples, rede de difração) e 5 questões
- capítulo sobre conforto acústico (reverberação, absorção, isolamento, nível de intensidade sonora em decibéis) e 8 questões
- capítulo sobre noções de acústica (reverberação, absorção, isolamento, nível de intensidade sonora) e 8 questões

### 12. `informatica.python` — Python: fundamentos da linguagem
Arquivo atual: `conteudo/materias/informatica/python.json` · 8 tópicos de edital pedem mais conteúdo

Falta:
- capítulo introdutório de NumPy, Pandas (DataFrame/Series), Matplotlib e Scikit-learn aplicados a análise de dados e aprendizado de máquina, com cerca de 8 questões; a matéria exclui bibliotecas científicas
- capítulo sobre Pandas, NumPy, SciPy, Matplotlib, Scikit-learn e noções de TensorFlow/PyTorch (tensores, treino de modelos) e questões; a matéria cobre só a linguagem pura
- DataFrames (criação, seleção com loc/iloc, filtros, agrupamento) e matrizes/arrays NumPy; listas, dicionários e conjuntos já estão cobertos
- capítulo sobre Jupyter Notebook (células, kernel, markdown, execução) e questões; a orientação a objetos já está coberta
- Shell Script (bash), Ruby, Groovy e comparação com JavaScript; só Python é ensinado nesta matéria
- capítulo sobre scripts de automação: módulos os/sys/pathlib, subprocess, manipulação de arquivos CSV/JSON, argparse e agendamento, com cerca de 6 questões
- aplicação de Python em ciência de dados e IA: NumPy, Pandas, Scikit-learn, fluxo de treino/avaliação de modelos e questões
- noções de TensorFlow e PyTorch (tensores, camadas, treino, diferenciação automática) e questões; a matéria cobre só Python puro

### 13. `matematica.equacoes-1-2-grau` — Equações do 1º e do 2º grau
Arquivo atual: `conteudo/materias/matematica/equacoes-1-2-grau.json` · 5 tópicos de edital pedem mais conteúdo

Falta:
- inequações do 1º e do 2º grau (resolução, sinal da função, conjunto-solução em intervalos) com cerca de 10 questões; as equações já estão cobertas
- inequações do 1º e do 2º grau com estudo de sinal e conjunto-solução, e cerca de 8 questões; só as equações estão cobertas
- inequações do 1º e do 2º grau (estudo do sinal, representação gráfica, intervalos) e problemas com inequações, com cerca de 10 questões; as equações e a modelagem já estão cobertas
- inequações do 1º e do 2º grau (a parte de sistemas lineares tem matéria própria), com cerca de 8 questões

### 14. `processo-trabalho.principios-processo` — Princípios do processo do trabalho e aplicação do CPC
Arquivo atual: `conteudo/materias/processo-trabalho/principios-processo.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- princípios específicos do processo do trabalho que faltam: jus postulandi, concentração, imediatidade, irrecorribilidade imediata das decisões interlocutórias, impulso oficial, extrapetição em casos legais, finalidade social e simplicidade, com cerca de 10 questões; proteção, oralidade, conciliação, celeridade e informalidade já estão cobertas
- princípios gerais do processo trabalhista além dos cinco já ensinados: jus postulandi, concentração, imediatidade, irrecorribilidade das interlocutórias, impulso oficial e inquisitoriedade na execução, com cerca de 10 questões
- fontes do direito processual do trabalho (CLT, Lei 5.584/70, Lei 7.701/88, CPC, regimentos, súmulas) e os princípios que faltam (jus postulandi, concentração, imediatidade, irrecorribilidade das interlocutórias), com cerca de 10 questões
- princípios do processo do trabalho que faltam: jus postulandi, concentração, imediatidade, irrecorribilidade imediata das interlocutórias, impulso oficial e finalidade social, com cerca de 10 questões
- fontes do direito processual do trabalho (CLT, Lei 5.584/70, Lei 7.701/88, CPC, regimentos internos, súmulas e orientações do TST) com hierarquia e exemplos e cerca de 6 questões
- princípios específicos que faltam: jus postulandi, concentração, imediatidade, irrecorribilidade imediata das interlocutórias, impulso oficial e finalidade social, com cerca de 10 questões
- princípios gerais do processo trabalhista além dos cinco já ensinados: jus postulandi, concentração, imediatidade, irrecorribilidade das interlocutórias e impulso oficial, com cerca de 10 questões

### 15. `processo-trabalho.servicos-auxiliares-peritos` — Serviços auxiliares da Justiça do Trabalho e peritos
Arquivo atual: `conteudo/materias/processo-trabalho/servicos-auxiliares-peritos.json` · 4 tópicos de edital pedem mais conteúdo

Falta:
- gratuidade da justiça no processo do trabalho (art. 790, §§ 3º e 4º: benefício a quem recebe até 40% do teto do RGPS, comprovação da insuficiência, momento do pedido, efeitos sobre custas e perícia) com cerca de 8 questões; honorários periciais e ADI 5766 já estão cobertos
- gratuidade da justiça no processo do trabalho (art. 790, §§ 3º e 4º, critérios de renda, comprovação, pessoa jurídica, efeitos sobre custas e perícia) com cerca de 8 questões; honorários periciais já estão cobertos
- gratuidade da justiça no processo do trabalho (art. 790, §§ 3º e 4º, critérios de renda, comprovação, efeitos sobre custas e perícia) com cerca de 8 questões; honorários periciais já estão cobertos
- regras gerais sobre o perito (CPC, arts. 156 a 158 e 465 a 480: nomeação, escusa, impedimento e suspeição, assistente técnico, quesitos, laudo) aplicadas ao processo do trabalho, com cerca de 8 questões; só os honorários do art. 790-B são tratados

### 16. `matematica.geometria-espacial-volumes` — Geometria espacial: áreas e volumes
Arquivo atual: `conteudo/materias/matematica/geometria-espacial-volumes.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- poliedros (relação de Euler, poliedros de Platão e regulares), posições relativas de retas e planos, troncos de pirâmide e de cone e princípio de Cavalieri, com cerca de 10 questões; hoje só áreas e volumes de seis sólidos
- poliedros regulares e de Platão, relação de Euler (V − A + F = 2), contagem de vértices, arestas e faces e o cubo e o paralelepípedo com diagonal, com cerca de 8 questões
- poliedros convexos (relação de Euler, poliedros regulares), cubo e paralelepípedo com diagonais, troncos de pirâmide e de cone e inscrição de sólidos, com cerca de 10 questões de nível de colégio militar
- cubo e paralelepípedo explicitamente, com área total, volume e diagonal do espaço, com cerca de 5 questões; o cubo hoje aparece só como prisma
- área, volume e diagonal do cubo e do paralelepípedo retângulo (a = √(a²+b²+c²)), com exemplos e cerca de 5 questões
- poliedros: definição, convexidade, relação de Euler, poliedros de Platão e regulares, soma dos ângulos das faces, com cerca de 8 questões

### 17. `matematica.porcentagem` — Porcentagem e variação percentual
Arquivo atual: `conteudo/materias/matematica/porcentagem.json` · 6 tópicos de edital pedem mais conteúdo

Falta:
- capítulo curto de juros simples e compostos como aplicação de porcentagem, com 5 questões
- operações com números, juros e proporcionalidade (regra de três, grandezas proporcionais) com 10 questões
- números reais e operações e juros simples e compostos, com 10 questões
- conjuntos, números, juros e proporcionalidade, com 10 questões
- proporcionalidade (grandezas direta e inversamente proporcionais, regra de três) com 8 questões
- divisão proporcional (partes diretamente e inversamente proporcionais) com 8 questões

### 18. `informatica.forense-pericia-digital` — Forense e perícia digital
Arquivo atual: `conteudo/materias/informatica/forense-pericia-digital.json` · 7 tópicos de edital pedem mais conteúdo

Falta:
- aspectos jurídicos da prova digital: validade, admissibilidade, ata notarial, provas por meios eletrônicos e LGPD, com 6 questões
- recuperação de arquivos apagados: sistemas de arquivos (FAT, NTFS, ext), tabela de arquivos, file carving e slack space, com 8 questões
- log de eventos do Windows (Event Viewer, tipos de eventos, IDs de logon, análise cronológica) com 6 questões
- técnicas antiforense: criptografia de disco, esteganografia, ocultação e destruição de rastros, com 6 questões
- sanitização de discos e wipe (sobrescrita, degauss, destruição física, secure erase), com 6 questões
- dados armazenados x dados de tráfego, dispositivos, plataformas e nuvem sob a ótica processual penal (requisição, marco civil, cooperação internacional), com 6 questões
- aspectos jurídicos da prova digital (validade, admissibilidade, produção) com 6 questões
