# Matérias novas para o Gemini (18/10/2026)

**Base:** os 3.677 editais de `conteudo/editais`.
- 83% dos tópicos já têm matéria que cobre o tópico inteiro.
- Sobram cerca de 60 mil tópicos sem matéria.
- Uma segunda passada pela API em 12 mil desses textos confirmou que não falta ligação: falta matéria. Só 57 ligaram com confiança alta.

**Como a lista está ordenada:** pelo número de **concursos diferentes** que pedem o assunto. Um concurso com 40 cargos conta uma vez.
- "Tópicos" é quantas linhas de edital a matéria passaria a cobrir.
- A contagem foi feita por palavra-chave, então é uma estimativa.

**Regras para cada matéria (as mesmas das últimas do Gemini):**
- **Formato:** o mesmo das matérias já publicadas, com `id`, `title`, `subject`, `scope.covers`, capítulos em Markdown e questões com explicação.
- **Sem duplicar:** escreva só o que falta. A coluna "Já existe (não repetir)" diz o que a biblioteca já tem.
- **Lei sem certeza:** se não tiver certeza de um número de lei, resolução ou portaria, explique o conteúdo sem citar o número.
- **Tamanho:** matéria grande vira 2 ou 3 matérias. Gineco-obstetrícia e cirurgia já estão divididas abaixo.
- **Exemplos:** os tópicos de exemplo são as palavras reais dos editais. A matéria precisa cobrir esses itens.

## Prioridade 1: saúde (é onde está o maior buraco)

| # | Matéria proposta | id sugerido | Concursos | Tópicos | Já existe (não repetir) | Tópicos de exemplo |
|---|---|---|---|---|---|---|
| 1 | Odontologia clínica: dentística, cirurgia oral menor, materiais dentários, radiologia, patologia bucal, urgências e pacientes especiais | `odontologia.clinica-cirurgia-materiais-radiologia` | 110 | 670 | endodontia, odontopediatria, periodontia, prótese/oclusão/DTM, saúde coletiva e biossegurança | Materiais dentários; Radiologia odontológica; Patologia e diagnóstico bucal; Urgências em Odontologia; Cirurgia oral menor; Atendimento a pacientes com necessidades especiais; Código de Ética Odontológica |
| 2 | Nutrição: nutrição normal, avaliação nutricional, dietoterapia e políticas de alimentação (PNAN, alimentação escolar) | `nutricao.dietoterapia-avaliacao-politicas` | 77 | 503 | (nada de nutrição humana) | Nutrição normal e leis da alimentação; Avaliação do estado nutricional; Dietoterapia cardiovascular, renal, diabetes, obesidade; Alimentação da criança; Política Nacional de Alimentação e Nutrição; Alimentação escolar |
| 3 | Urologia e nefrologia, com transplante renal e a Lei de Transplantes | `medicina.urologia-nefrologia-transplante` | 63 | 590 | clínica endócrino/renal/gastro/infecto (só noções) | Litíase e infecções do trato geniturinário; Neoplasias do sistema geniturinário; Urgências e traumatismo geniturinário; Transplante renal; Lei de transplantes; Tuberculose geniturinária |
| 4 | Cirurgia geral, parte 1: avaliação e cuidados perioperatórios, abdome agudo, hérnias, pancreatite, vias biliares | `medicina.cirurgia-geral-abdome-perioperatorio` | 55 | ~650 | urgências e emergências | Avaliação pré-operatória; Cuidados perioperatórios; Pancreatite aguda; Abdome agudo; Hérnias |
| 5 | Cirurgia geral, parte 2: trauma (ATLS), politrauma e queimaduras | `medicina.cirurgia-trauma-queimaduras` | (junto com o 4) | ~640 | urgências e emergências | Tratamento do politraumatizado; Queimaduras; Trauma abdominal e torácico |
| 6 | Ginecologia: anatomia, endometriose, distopias, patologias da mama e do trato genital, climatério, contracepção | `medicina.ginecologia` | 52 | ~400 | (nada) | Endometriose; Distopias genitais; Patologias benignas e malignas da mama, vulva, vagina, útero e ovário; Anatomia clínica e cirúrgica do aparelho reprodutor feminino |
| 7 | Obstetrícia: gestação, pré-natal, parto e partograma, doenças da gestação, prematuridade | `medicina.obstetricia` | (junto com o 6) | ~420 | saúde nos ciclos de vida (enfermagem, só noções) | Anatomia e fisiologia da gestação; Doenças hipertensivas e diabetes gestacional; Mecanismo do trabalho de parto; Partograma; Prevenção da prematuridade; Sofrimento fetal; Hemorragias da gestação |
| 8 | Doenças infecciosas e parasitárias: IST/HIV-AIDS, tuberculose, hanseníase, arboviroses, parasitoses | `medicina.doencas-infecciosas-parasitarias` | 53 | 375 | controle de infecção; clínica infecto (noções) | AIDS; Infecções sexualmente transmissíveis; Parasitoses intestinais; Dengue e arboviroses; Doenças virais, bacterianas e parasitárias |
| 9 | Imunização: PNI, Calendário Nacional de Vacinação, rede de frio, imunização ocupacional | `saude-publica.imunizacao-pni` | 48 | 179 | políticas nacionais de saúde | Programa Nacional de Imunizações; Calendário Nacional de Vacinação; Rede de frio e conservação de vacinas; Esquema básico; Imunização ocupacional |
| 10 | Hematologia: anemias, coagulação, trombofilias, leucemias e linfomas, transfusão | `medicina.hematologia-hemoterapia` | 42 | 398 | patologia clínica e laboratório | Anemias; Fisiologia da coagulação; CIVD; Anticoagulação; Leucemias; Linfomas; Transfusões |
| 11 | Bioestatística aplicada à saúde | `saude-publica.bioestatistica` | 42 | 203 | epidemiologia e vigilância; epidemiologia clínica | Bioestatística: coleta, amostragem, análise, tabelas e gráficos; medidas de associação; testes |
| 12 | Terapia intensiva: ventilação mecânica, monitorização hemodinâmica, choque, sepse, falência de órgãos | `medicina.terapia-intensiva` | 40 | 230 | urgências e emergências | Ventilação mecânica; Monitorização hemodinâmica; Choque cardiogênico; Sepse; Falência de múltiplos órgãos |
| 13 | Anestesiologia: avaliação pré-anestésica, anestesia geral e regional, via aérea, anestesia em pediatria | `medicina.anestesiologia` | 38 | 309 | (nada) | Anestesia em pediatria; Anestesia local; Bloqueios; Monitorização |
| 14 | Pediatria e neonatologia: crescimento e desenvolvimento, aleitamento, recém-nascido, erros inatos | `medicina.pediatria-neonatologia` | 37 | 516 | saúde nos ciclos de vida (enfermagem) | Crescimento e desenvolvimento; Aleitamento materno; Cuidados com o recém-nascido; Erros inatos do metabolismo; Anemias na infância |
| 15 | Cuidados paliativos e bioética | `saude.cuidados-paliativos-bioetica` | 34 | 180 | oncologia e cuidado do paciente | Cuidados no fim da vida; Bioética; Cuidados paliativos em terapia intensiva |
| 16 | Segurança do paciente e qualidade em saúde (PNSP, metas, eventos adversos, humanização) | `saude.seguranca-paciente-qualidade` | 31 | 525 | controle de infecção | Cultura de segurança; Prevenção de eventos adversos e gerenciamento de riscos; Identificação do paciente; Comunicação segura; Humanização e cuidado centrado na pessoa; Atuação multiprofissional |
| 17 | Ortopedia e traumatologia | `medicina.ortopedia-traumatologia` | 30 | 229 | clínica reumatologia | Fraturas e luxações; Osteoartrite; Osteocondroses; Doenças osteometabólicas; Problemas ortopédicos na criança |
| 18 | Neurologia clínica | `medicina.neurologia` | 28 | 230 | urgências (AVC só na emergência) | Cefaleias; AVC; Epilepsia; Demências; Doenças neuromusculares e periféricas; Semiologia neurológica |
| 19 | Legislação da Ebserh: Lei 12.550/2011, Estatuto Social, Regimento Interno, Código de Ética e Conduta, Regulamento de Pessoal | `legislacao.ebserh` | 2 (com dezenas de cargos) | 163 | (nada) | Lei Federal 12.550/2011; Estatuto Social da Ebserh; Regimento Interno; Código de Ética e Conduta; Regulamento de Pessoal |

## Prioridade 2: outras áreas

| # | Matéria proposta | id sugerido | Concursos | Tópicos | Já existe (não repetir) | Tópicos de exemplo |
|---|---|---|---|---|---|---|
| 20 | Hidráulica, pneumática, instrumentação e automação industrial | `engenharia.instrumentacao-automacao-hidraulica-pneumatica` | 71 | 200 | manutenção industrial | Sistemas hidráulicos e pneumáticos; Instrumentação industrial; Transmissores; Válvulas de controle; CLP e automação |
| 21 | Educação e gestão ambiental (licenciamento, unidades de conservação, recursos naturais) | `meio-ambiente.gestao-licenciamento-unidades-conservacao` | 64 | 463 | política nacional de educação ambiental; direito ambiental | Educação ambiental; Unidades de Conservação; Licenciamento ambiental; Monitoramento dos recursos naturais |
| 22 | Medicina veterinária: clínica, zoonoses, produção animal, código de ética | `medicina-veterinaria.clinica-zoonoses-producao` | 55 | 170 | defesa sanitária, inspeção e ética | Zoonoses; Produção de ruminantes e suínos; Uso racional de medicamentos veterinários; Código de Ética do Médico-Veterinário |
| 23 | Ética profissional nos conselhos de classe (princípios comuns: sigilo, responsabilidade técnica, deveres, infrações) | `etica.etica-profissional-conselhos` | 48 | 149 | código de ética médica; ética no serviço público | Ética profissional; Responsabilidade técnica; Sigilo profissional; Códigos de ética das profissões |
| 24 | Estruturas e materiais de construção: concreto armado e protendido, pré-moldados, aço, esquadrias | `engenharia.estruturas-concreto-aco-materiais` | 48 | 90 | resistência dos materiais; edificações | Materiais de construção (aglomerantes, agregados, betuminosos); Concreto protendido; Pré-moldado; Estruturas de aço; Esquadrias |
| 25 | Alfabetização, letramento e educação infantil | `educacao.alfabetizacao-letramento-educacao-infantil` | 28 | 206 | LDB, BNCC e didática | Alfabetização e letramento; Educação infantil; Ludicidade |
| 26 | APIs e integração de sistemas: REST, JSON, OpenAPI/Swagger, webhooks | `informatica.apis-rest-integracao` | 18 | 56 | (ver as matérias de engenharia de software) | JSON; Swagger; Webhook; Evento negocial |
| 27 | Educação escolar indígena | `educacao.educacao-escolar-indigena` | 13 | 56 | diversidade e inclusão | Educação escolar indígena; Interculturalidade; Legislação da educação indígena |

## Prioridade 3: estados (poucos concursos, mas muitos cargos cada)

| # | Matéria proposta | id sugerido | Concursos | Tópicos | Tópicos de exemplo |
|---|---|---|---|---|---|
| 28 | Tocantins: história, geografia e economia | `tocantins.conhecimentos-estaduais` | 2 (SES-TO e outro, com dezenas de cargos) | 782 | Criação do Estado (movimentos emancipacionistas, 1988); Formação territorial; Povos indígenas e quilombolas; Unidades de Conservação; Clima, vegetação, hidrografia e relevo; Matriz produtiva e energética; Símbolos do Estado |
| 29 | Roraima: história, geografia e economia | `roraima.conhecimentos-estaduais` | 2 | 899 | Ocupação territorial; Presença portuguesa; Delimitação das fronteiras; Território Federal; Criação do Estado e dos municípios; Povos e terras indígenas; Governadores; Clima, solos, regime pluviométrico; Economia |
| 30 | Rio Grande do Norte: história e aspectos geoeconômicos | `rio-grande-do-norte.historia-aspectos-geoeconomicos` | 5 | 424 | Capitania do Rio Grande; Indígenas nos sertões; Motim das Mulheres; Mossoró e Lampião; Abolição em Mossoró (30 de setembro); Salineiros; RN na Segunda Guerra (Trampolim da Vitória, Barreira do Inferno); Seridó; Petróleo, fruticultura, carcinicultura e energias renováveis |

## Não precisa de matéria nova (é conserto de edital, eu cuido)

- **Pedaços sem contexto:** "Classificação.", "Características.", "Conceito.", "Competência.", "Princípios.", "Disposições gerais." etc. Cerca de 30 textos que se repetem em dezenas de editais ficaram soltos na divisão dos tópicos-pacote. Precisam voltar a ter o assunto no texto (ex.: "Tributos: classificação.") para serem ligados.
- **Itens isolados que já têm matéria parecida:** "Intertextualidade.", "Sistema monetário brasileiro.", "Falácias.", "Ciclo PDCA." etc. Ficaram de fora porque a API não tinha certeza. Vale uma ligação cuidadosa por disciplina.
- **Leis e normas municipais:** leis orgânicas, estatutos de servidores de cada cidade, notícias do site da prefeitura. Ficam sem matéria.
