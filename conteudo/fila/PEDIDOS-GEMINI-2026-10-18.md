# Pedidos para o Gemini: 30 matérias novas (18/10/2026)

**Como usar:**
1. Cole a **Mensagem 0** uma vez, no começo da conversa.
2. Depois cole **um pedido por mensagem**, na ordem.
3. A cada 5 matérias, mande os arquivos JSON para o Claude conferir e publicar.
4. Se o Gemini começar a errar o formato, abra uma conversa nova e cole a Mensagem 0 de novo.

---

## Mensagem 0 (regras: cole uma vez no começo)

```
Você vai escrever matérias de estudo para concursos públicos brasileiros para o app Estudário. Vou pedir UMA matéria por mensagem. Cada resposta deve ser UM arquivo JSON completo e válido, sem nada antes ou depois, no formato abaixo. Escreva em português do Brasil, com nível de cursinho preparatório caro: teoria completa, exemplos, como a banca cobra e pegadinhas.

FORMATO (todos os campos são obrigatórios):
{
  "id": "<o id que eu passar>",
  "subject": "<área, ex.: Medicina>",
  "title": "<título curto da matéria>",
  "version": 1,
  "status": "PUBLISHED",
  "aliases": ["<o título>", "<cada tópico de exemplo que eu passar, copiado exatamente>", "..."],
  "scope": { "covers": "<o que a matéria cobre, 20 a 2000 caracteres>", "excludes": "<o que fica de fora (pode ser vazio)>" },
  "theoryTitle": "<título da teoria>",
  "chapters": [ { "title": "<título único>", "markdown": "<texto em Markdown>" } ],
  "summary": "<resumo em Markdown, mínimo 400 caracteres>",
  "flashcards": [ { "front": "...", "back": "..." } ],
  "tips": ["..."],
  "traps": ["..."],
  "activeRecall": [ { "question": "...", "answer": "..." } ],
  "errorConcepts": [ { "key": "e1", "title": "...", "summary": "..." } ],
  "questions": [ ... ],
  "sources": [ { "kind": "OFICIAL", "title": "...", "publisher": "...", "reference": "...", "url": "https://...", "accessedAt": "2026-10-18" } ]
}

QUANTIDADES MÍNIMAS:
- chapters: 3 a 6 capítulos, cada um com no mínimo 800 caracteres; a teoria somada deve passar de 6.000 caracteres (o ideal é 15.000 a 30.000).
- flashcards: 12 ou mais, sem frente repetida. tips: 3 ou mais. traps: 3 ou mais. activeRecall: 5 ou mais. errorConcepts: 2 ou mais.
- questions: 40 ou mais, sendo pelo menos 15 Certo/Errado e pelo menos 15 de múltipla escolha com 5 alternativas (A a E), e pelo menos 8 de cada dificuldade (FACIL, MEDIA, DIFICIL).

CADA QUESTÃO:
{
  "statement": "<enunciado, 20 a 4000 caracteres>",
  "format": "MULTIPLE_CHOICE" ou "TRUE_FALSE",
  "difficulty": "FACIL" | "MEDIA" | "DIFICIL",
  "options": [ {"key":"A","text":"...","correct":false}, ... ],
  "explanation": "<mínimo 150 caracteres: por que a certa está certa e onde está o erro das outras>",
  "section": "<título EXATO de um dos capítulos>",
  "errorConceptKey": "<uma chave de errorConcepts, ou null>",
  "sourceType": "AUTHORIAL", "board": null, "agency": null, "year": null, "sourceUrl": null
}
- TRUE_FALSE usa exatamente as opções {"key":"C","text":"Certo"} e {"key":"E","text":"Errado"}, com uma correta.
- Exatamente UMA opção correta. Sem alternativas repetidas. Sem enunciado repetido.
- A alternativa correta NÃO pode ser sempre a mais longa nem sempre a mais curta (no máximo 40% das vezes cada); distribua a letra certa entre A, B, C, D e E.
- Não comece várias alternativas ou explicações com a mesma frase de molde; varie o que se pergunta (caso clínico, conceito, conduta, exceção, "assinale a incorreta").
- Em Certo/Errado, a explicação fala do próprio item (não de "alternativas").

REGRAS DE CONTEÚDO:
- Siga diretrizes e protocolos oficiais vigentes (Ministério da Saúde, conselhos profissionais, leis federais). Se não tiver CERTEZA de um número de lei, portaria, resolução, dose ou valor, NÃO cite o número: explique o conteúdo. Nunca invente norma, julgado, estudo ou dado.
- Não cite julgados ou normas "recentes" que você não consiga confirmar.
- sources: só fontes oficiais reais (kind "OFICIAL", ou "COMPLEMENTAR" para manuais e livros conhecidos), com link real.
- Não escreva texto de rascunho ("lorem", "exemplo genérico", "a completar").

Responda "OK, pode mandar a primeira" e aguarde.
```

---

## Pedidos (um por mensagem)

### 1. Odontologia clínica
```
Matéria 1. id: "odontologia.clinica-cirurgia-materiais-radiologia". subject: "Odontologia". Título sugerido: "Odontologia clínica: dentística, cirurgia oral menor, materiais, radiologia e patologia bucal".
Cubra: dentística restauradora (isolamento, ionômero, compósitos, adesivos), cirurgia oral menor (exodontias, alvéolo seco, hemostasia, suturas), materiais dentários, radiologia odontológica (técnicas intrabucais, interpretação, proteção), patologia e diagnóstico bucal, terapêutica e farmacologia em odontologia, urgências e emergências no consultório (síncope, alergia, convulsão, hipoglicemia, crise hipertensiva), atendimento a pacientes com necessidades especiais, ética e legislação odontológica (Código de Ética Odontológica, responsabilidade técnica).
NÃO repita (já existem): endodontia, odontopediatria, periodontia, prótese/oclusão/DTM, saúde coletiva e biossegurança.
Tópicos de edital para aliases: "Materiais dentários." | "Terapêutica e farmacologia de interesse na clínica odontológica." | "Radiologia odontológica: indicações, técnicas intrabucais básicas, interpretação radiográfica inicial e critérios de proteção." | "Urgências e emergências em Odontologia." | "Diagnósticos odontológicos por imagem: técnica e interpretação de imagens." | "Patologia e diagnóstico bucal." | "Patologia bucal." | "Atendimento odontológico à pacientes com necessidades especiais." | "Código de ética odontológica." | "Dentística restauradora." | "Cirurgia oral menor."
```

### 2. Nutrição
```
Matéria 2. id: "nutricao.dietoterapia-avaliacao-politicas". subject: "Nutrição". Título sugerido: "Nutrição: alimentação normal, avaliação nutricional, dietoterapia e políticas de alimentação".
Cubra: nutrição normal (leis da alimentação, macro e micronutrientes, recomendações, planejamento dietético), nutrição nos ciclos de vida (gestante, lactente, criança, idoso), avaliação do estado nutricional (antropometria, indicadores, inquéritos), dietoterapia (cardiovascular, diabetes, obesidade, dislipidemias, renal, hepática, gastrointestinal, câncer), terapia nutricional enteral e parenteral (noções), Política Nacional de Alimentação e Nutrição, Programa Nacional de Alimentação Escolar, Guia Alimentar para a População Brasileira, segurança alimentar e nutricional.
Tópicos de edital para aliases: "Nutrição." | "Noções de nutrição e dietética." | "Alimentação da criança." | "Política Nacional de Alimentação e Nutrição." | "Avaliação do estado e situação nutricional da população." | "Alimentação escolar." | "Nutrição normal: definição, leis da alimentação; requerimentos, recomendações de nutrientes e planejamento dietético." | "Dietoterapia nas enfermidades do sistema cardiovascular." | "Dietoterapia nos distúrbios metabólicos: obesidade; diabete melito; dislipidemias." | "Dietoterapia das doenças renais."
```

### 3. Urologia e nefrologia
```
Matéria 3. id: "medicina.urologia-nefrologia-transplante". subject: "Medicina". Título sugerido: "Urologia e nefrologia: doenças do trato geniturinário, insuficiência renal e transplante".
Cubra: anatomia e semiologia geniturinária, infecção urinária, litíase, uropatia obstrutiva, hiperplasia e câncer de próstata, tumores de rim, bexiga e testículo, urgências urológicas (retenção, torção testicular, priapismo, trauma geniturinário), tuberculose geniturinária, insuficiência renal aguda e crônica, distúrbios hidroeletrolíticos e acidobásicos ligados ao rim, glomerulopatias, hipertensão renovascular, diálise (indicações), transplante renal e imunologia do transplante, Lei de Transplantes (doação, morte encefálica, crimes) — sem inventar artigos.
NÃO repita: clínica endócrino/renal/gastro/infecto (noções gerais já existem).
Tópicos de edital para aliases: "Transplante renal." | "Lei de transplantes (Lei nº 9.434/1997)." | "Tuberculose do aparelho geniturinário." | "Imunologia e transplantes." | "Doenças vasculares do aparelho geniturinário." | "Urgências do aparelho geniturinário." | "Litíase e infecções do trato geniturinário." | "Neoplasias benignas e malignas do sistema geniturinário." | "Traumatismo do sistema geniturinário." | "Hipertensão renovascular." | "Cirurgias do aparelho geniturinário."
```

### 4. Cirurgia geral: abdome e perioperatório
```
Matéria 4. id: "medicina.cirurgia-geral-abdome-perioperatorio". subject: "Medicina". Título sugerido: "Cirurgia geral: avaliação perioperatória, abdome agudo, hérnias e vias biliares".
Cubra: avaliação pré-operatória e risco cirúrgico, cuidados pré, intra e pós-operatórios, resposta metabólica ao trauma cirúrgico, hidratação e equilíbrio hidroeletrolítico no cirúrgico, infecção de sítio cirúrgico e antibioticoprofilaxia, complicações pós-operatórias, abdome agudo (inflamatório, obstrutivo, perfurativo, vascular, hemorrágico), apendicite, colecistite e coledocolitíase, pancreatite aguda, hérnias da parede abdominal, obstrução intestinal, hemorragia digestiva (abordagem cirúrgica), cirurgia pediátrica comum (hérnias, fimose, abdome agudo na criança).
NÃO repita: urgências e emergências clínicas (já existe); trauma fica na matéria 5.
Tópicos de edital para aliases: "Avaliação pré-operatória." | "Cuidados perioperatórios." | "Pancreatite aguda." | "Abdome agudo." | "Hérnias." | "Assistência de enfermagem perioperatória." | "Problemas cirúrgicos mais comuns na infância e adolescência: hérnias, fimose, parafimose, abdome agudo, escroto agudo."
```

### 5. Cirurgia: trauma e queimaduras
```
Matéria 5. id: "medicina.cirurgia-trauma-queimaduras". subject: "Medicina". Título sugerido: "Trauma e queimaduras: atendimento inicial ao politraumatizado".
Cubra: atendimento inicial ao politraumatizado (sequência ABCDE, avaliação primária e secundária), via aérea no trauma, choque hemorrágico e reposição, trauma torácico, trauma abdominal e pélvico, trauma cranioencefálico e raquimedular, trauma de extremidades, trauma na criança, na gestante e no idoso, queimaduras (classificação, cálculo de superfície, reposição volêmica, critérios de transferência, lesão inalatória), lesões por eletricidade.
NÃO repita: a matéria 4 (abdome agudo não traumático); urgências clínicas.
Tópicos de edital para aliases: "Queimaduras." | "Tratamento do paciente politraumatizado." | "Trauma." | "Atendimento inicial ao politraumatizado." | "Trauma abdominal." | "Trauma torácico."
```

### 6. Ginecologia
```
Matéria 6. id: "medicina.ginecologia". subject: "Medicina". Título sugerido: "Ginecologia: anatomia, doenças benignas e malignas, climatério e contracepção".
Cubra: anatomia clínica e cirúrgica do aparelho reprodutor feminino, ciclo menstrual e sangramento uterino anormal, endometriose, miomas, distopias genitais e incontinência urinária, doença inflamatória pélvica e vulvovaginites, patologias benignas e malignas da mama (rastreamento), do colo do útero (rastreamento e lesões precursoras), do endométrio, da vulva, da vagina e do ovário, climatério e terapia hormonal, contracepção e planejamento familiar, infertilidade (noções), violência sexual (atendimento).
Tópicos de edital para aliases: "Endometriose." | "Distopias genitais." | "Distúrbios urogenitais." | "Patologias benignas e malignas da mama, da vulva, da vagina, do útero e do ovário." | "Anatomia clínica e cirúrgica do aparelho reprodutor feminino." | "Tuberculose do aparelho geniturinário."
```

### 7. Obstetrícia
```
Matéria 7. id: "medicina.obstetricia". subject: "Medicina". Título sugerido: "Obstetrícia: pré-natal, parto e intercorrências da gestação".
Cubra: anatomia e fisiologia da gestação, diagnóstico de gravidez, assistência pré-natal (consultas, exames, suplementação), doenças hipertensivas da gestação (pré-eclâmpsia, eclâmpsia, HELLP), diabetes gestacional, doenças renais e outras condições clínicas na gestação, trombofilias, hemorragias da primeira metade (aborto, gravidez ectópica, mola hidatiforme) e do terceiro trimestre (placenta prévia, descolamento), infecções de transmissão vertical, prevenção da prematuridade e trabalho de parto prematuro, mecanismo do trabalho de parto, assistência ao parto e partograma, analgesia e anestesia intraparto (indicações), sofrimento fetal agudo e crônico, puerpério e suas complicações, aleitamento.
NÃO repita: a matéria 6 (ginecologia).
Tópicos de edital para aliases: "Anatomia e fisiologia da gestação." | "Indicações de analgesia e anestesia intraparto." | "Prevenção da prematuridade." | "Doenças hipertensivas na gestação." | "Diabetes gestacional." | "Doenças renais e outras condições clínicas na gestação." | "Mecanismo do trabalho de parto." | "Assistência ao parto e uso do partograma." | "Sofrimento fetal crônico e agudo." | "Trombofilias." | "Aborto, gravidez ectópica, mola hidatiforme, coriocarcinoma e transmissões de infecções maternas fetais." | "Hemorragia de terceiro trimestre."
```

### 8. Doenças infecciosas e parasitárias
```
Matéria 8. id: "medicina.doencas-infecciosas-parasitarias". subject: "Medicina". Título sugerido: "Doenças infecciosas e parasitárias: IST/HIV, tuberculose, hanseníase, arboviroses e parasitoses".
Cubra: infecções sexualmente transmissíveis (sífilis, gonorreia, clamídia, HPV, herpes; abordagem sindrômica), HIV/aids (diagnóstico, profilaxias PrEP e PEP, infecções oportunistas, princípios do tratamento), hepatites virais, tuberculose (diagnóstico, tratamento, contatos), hanseníase, dengue, zika, chikungunya e febre amarela, malária, leishmanioses, doença de Chagas, parasitoses intestinais, meningites, doenças de veiculação hídrica e alimentar, noções de microbiologia e parasitologia.
NÃO repita: controle de infecção hospitalar (já existe).
Tópicos de edital para aliases: "AIDS." | "Doenças sexualmente transmissíveis." | "Infecções sexualmente transmissíveis." | "Parasitoses intestinais." | "Dengue." | "Doenças infecciosas e parasitárias do SNC." | "Noções de microbiologia e parasitologia." | "Doenças humanas virais, bacterianas e parasitárias."
```

### 9. Imunização
```
Matéria 9. id: "saude-publica.imunizacao-pni". subject: "Saúde Pública". Título sugerido: "Imunização: Programa Nacional de Imunizações, calendário vacinal e rede de frio".
Cubra: imunologia básica das vacinas (ativa, passiva, tipos de vacina), Programa Nacional de Imunizações (história e organização), Calendário Nacional de Vacinação por ciclo de vida (criança, adolescente, adulto, idoso, gestante) — descreva as vacinas e o esquema geral sem inventar idades ou doses de que não tenha certeza, rede de frio (conservação, temperatura, cadeia, perdas), vias e técnicas de administração, contraindicações e adiamentos, eventos supostamente atribuíveis à vacinação, imunização de grupos especiais (CRIE), imunização ocupacional dos profissionais de saúde.
Tópicos de edital para aliases: "Imunização: Programa Nacional de Imunizações – PNI e princípios gerais da vacinação." | "Calendário Nacional de Vacinação." | "Imunizações." | "Programa de imunização." | "Programa Nacional de Imunização." | "Esquema básico de imunização." | "Imunização ocupacional e rede de frio." | "Programa Nacional de Imunização e Imunização Ocupacional."
```

### 10. Hematologia
```
Matéria 10. id: "medicina.hematologia-hemoterapia". subject: "Medicina". Título sugerido: "Hematologia e hemoterapia: anemias, coagulação, neoplasias hematológicas e transfusão".
Cubra: hematopoese e hemograma, anemias (ferropriva, megaloblástica, de doença crônica, hemolíticas, falciforme, talassemias, aplásica), anemias na infância, distúrbios plaquetários, fisiologia da coagulação e fibrinólise, hemofilias e doença de von Willebrand, CIVD, trombofilias, anticoagulação (heparinas, antagonistas da vitamina K, anticoagulantes diretos — princípios), leucemias agudas e crônicas, linfomas, mieloma múltiplo, neutropenia febril, hemoterapia (hemocomponentes, indicações, reações transfusionais).
NÃO repita: patologia clínica e laboratório (já existe).
Tópicos de edital para aliases: "Anemias." | "Doenças hematológicas: anemias, pancitopenia, distúrbios plaquetários e da coagulação, neutropenia febril." | "Trombofilias." | "Leucemias." | "Fisiologia da coagulação e da fibrinólise." | "Linfomas." | "Coagulação intravascular disseminada." | "Anemias na infância." | "Anticoagulação." | "Transfusões sanguíneas." | "Transfusão." | "Anemias hemolíticas autoimunes."
```

### 11. Bioestatística
```
Matéria 11. id: "saude-publica.bioestatistica". subject: "Saúde Pública". Título sugerido: "Bioestatística aplicada à saúde".
Cubra: tipos de variáveis, coleta de dados e amostragem, apresentação tabular e gráfica, medidas de tendência central e dispersão, distribuição normal, probabilidade básica, intervalo de confiança, testes de hipóteses (erros tipo I e II, valor de p), principais testes (qui-quadrado, t, correlação e regressão — interpretação), medidas de associação (risco relativo, odds ratio, risco atribuível), testes diagnósticos (sensibilidade, especificidade, valores preditivos, curva ROC), tamanho de amostra (noções).
NÃO repita: epidemiologia e vigilância; epidemiologia clínica e ATS (já existem; aqui é a estatística).
Tópicos de edital para aliases: "Bioestatística." | "Noções de bioestatística." | "Bioestatística: coleta de dados, amostragem, análise dos dados, apresentação tabular e representação gráfica."
```

### 12. Terapia intensiva
```
Matéria 12. id: "medicina.terapia-intensiva". subject: "Medicina". Título sugerido: "Terapia intensiva: ventilação mecânica, choque, sepse e monitorização".
Cubra: admissão e critérios de UTI, monitorização hemodinâmica invasiva e não invasiva, choque (hipovolêmico, cardiogênico, distributivo, obstrutivo) e drogas vasoativas, sepse e choque séptico, insuficiência respiratória aguda e SDRA, ventilação mecânica invasiva e não invasiva (modos, parâmetros, desmame), sedação e analgesia na UTI, distúrbios hidroeletrolíticos e acidobásicos no paciente grave, falência de múltiplos órgãos, nutrição do paciente crítico (noções), prevenção de infecções relacionadas à assistência na UTI, cuidados paliativos em UTI (noções), morte encefálica (diagnóstico e protocolo, sem inventar números).
NÃO repita: urgências e emergências (PCR e atendimento inicial já existem).
Tópicos de edital para aliases: "Ventilação artificial." | "Ventilação mecânica." | "Avaliação hemodinâmica invasiva e não invasiva: métodos e interpretação." | "Monitorização hemodinâmica." | "Choque cardiogênico." | "Sepse." | "Choque - Falência de Múltiplos Órgãos." | "Terapia Intensiva em Cirurgia." | "Riscos inerentes aos procedimentos terapêuticos na UTI."
```

### 13. Anestesiologia
```
Matéria 13. id: "medicina.anestesiologia". subject: "Medicina". Título sugerido: "Anestesiologia: avaliação pré-anestésica, anestesia geral e regional e via aérea".
Cubra: avaliação pré-anestésica e classificação de estado físico, jejum, farmacologia dos anestésicos (inalatórios, venosos, opioides, bloqueadores neuromusculares, anestésicos locais e toxicidade), anestesia geral (indução, manutenção, despertar), anestesia regional (raquianestesia, peridural, bloqueios periféricos), via aérea (avaliação, via aérea difícil, intubação), monitorização intraoperatória, anestesia em pediatria, em obstetrícia e no idoso, complicações (hipertermia maligna, broncoaspiração, náusea e vômito), recuperação pós-anestésica, dor aguda pós-operatória.
Tópicos de edital para aliases: "Anestesia em pediatria." | "Anestesia local: técnicas infiltrativas e tronculares mais comuns, complicações e manejo." | "Monitorização neurofisiológica." | "Indicações de analgesia e anestesia intraparto."
```

### 14. Pediatria e neonatologia
```
Matéria 14. id: "medicina.pediatria-neonatologia". subject: "Medicina". Título sugerido: "Pediatria e neonatologia: crescimento, desenvolvimento, recém-nascido e doenças comuns da infância".
Cubra: crescimento e desenvolvimento (curvas, marcos), puericultura, aleitamento materno e alimentação complementar, recém-nascido (adaptação, reanimação neonatal — princípios, icterícia, triagens neonatais, prematuridade), erros inatos do metabolismo (noções e triagem), doenças respiratórias da infância (bronquiolite, asma, pneumonia), diarreia e desidratação, doenças exantemáticas, infecção urinária na criança, anemias na infância, desnutrição e obesidade infantil, maus-tratos, Estatuto da Criança e do Adolescente aplicado à saúde (noções).
NÃO repita: saúde nos ciclos de vida da enfermagem (já existe, mais básica).
Tópicos de edital para aliases: "Crescimento e desenvolvimento." | "Erros inatos do metabolismo." | "Anemias na infância." | "Aleitamento materno." | "Cuidados com o recém-nascido." | "Principais problemas ortopédicos na criança."
```

### 15. Cuidados paliativos e bioética
```
Matéria 15. id: "saude.cuidados-paliativos-bioetica". subject: "Saúde". Título sugerido: "Cuidados paliativos e bioética".
Cubra: princípios da bioética (autonomia, beneficência, não maleficência, justiça), consentimento livre e esclarecido, sigilo, terminalidade e decisões de fim de vida (ortotanásia, distanásia, eutanásia — conceitos e posição ética e legal no Brasil, sem inventar números de resolução), diretivas antecipadas de vontade, cuidados paliativos (conceito, indicações, equipe, controle de sintomas: dor, dispneia, náusea, delirium), comunicação de más notícias, luto, cuidados paliativos em UTI e na atenção primária, ética em pesquisa com seres humanos (noções).
NÃO repita: oncologia e cuidado do paciente (já existe).
Tópicos de edital para aliases: "Cuidados no fim da vida e cuidados paliativos." | "Bioética." | "Cuidados paliativos em terapia intensiva." | "Cuidados paliativos."
```

### 16. Segurança do paciente
```
Matéria 16. id: "saude.seguranca-paciente-qualidade". subject: "Saúde". Título sugerido: "Segurança do paciente e qualidade em saúde".
Cubra: conceitos (incidente, evento adverso, near miss, dano), Programa Nacional de Segurança do Paciente e núcleos de segurança, as metas internacionais de segurança (identificação, comunicação efetiva, medicamentos de alta vigilância, cirurgia segura, higiene das mãos, quedas e lesão por pressão), cultura de segurança, notificação e análise de eventos (causa raiz), gerenciamento de riscos, qualidade em saúde (dimensões, indicadores, acreditação, ciclo PDCA), humanização (Política Nacional de Humanização) e cuidado centrado na pessoa, trabalho em equipe multiprofissional e interdisciplinar, sigilo e confidencialidade.
NÃO repita: controle de infecção e esterilização (já existe).
Tópicos de edital para aliases: "Sigilo e confidencialidade." | "Comunicação segura e efetiva." | "Prevenção de eventos adversos e gerenciamento de riscos." | "Cultura de segurança." | "Humanização e cuidado centrado na pessoa." | "Atuação multiprofissional e interdisciplinar." | "Identificação do paciente e segurança na assistência." | "Qualidade e segurança no cuidado do paciente." | "Trabalho em equipe multiprofissional."
```

### 17. Ortopedia e traumatologia
```
Matéria 17. id: "medicina.ortopedia-traumatologia". subject: "Medicina". Título sugerido: "Ortopedia e traumatologia".
Cubra: princípios de fraturas (classificação, consolidação, imobilização, complicações como síndrome compartimental e embolia gordurosa), fraturas e luxações dos membros superiores e inferiores no adulto e na criança (lesões epifisárias), fraturas da coluna e da pelve, lesões ligamentares e meniscais, osteoartrite e alterações degenerativas, osteoporose e doenças osteometabólicas, osteomielite, artrite séptica e tuberculose osteoarticular, tumores ósseos (noções), lombalgia e cervicalgia, problemas ortopédicos da infância (displasia do quadril, pé torto, osteocondroses).
NÃO repita: clínica reumatológica (já existe).
Tópicos de edital para aliases: "Ortopedia." | "Principais problemas ortopédicos na criança." | "Osteoartrite." | "Osteocondroses." | "Alterações degenerativas osteoarticulares." | "Doenças osteometabólicas na prática ortopédica." | "Tuberculose osteoarticular." | "Problemas ortopédicos mais comuns na infância." | "Fraturas, luxações, lesões capsuloligamentares e epifisárias do membro superior e inferior em adultos e crianças."
```

### 18. Neurologia
```
Matéria 18. id: "medicina.neurologia". subject: "Medicina". Título sugerido: "Neurologia clínica".
Cubra: semiologia e exame neurológico, cefaleias primárias e secundárias, acidente vascular cerebral isquêmico e hemorrágico (diagnóstico, janela de tratamento — princípios, prevenção), epilepsias e estado de mal epiléptico, rebaixamento do nível de consciência e coma, demências, doença de Parkinson e distúrbios do movimento, esclerose múltipla, doenças neuromusculares e polineuropatias (Guillain-Barré), infecções do sistema nervoso central, tumores do sistema nervoso (noções), anatomia e fisiologia do sistema nervoso central e periférico.
NÃO repita: urgências e emergências (o atendimento inicial do AVC já existe).
Tópicos de edital para aliases: "Neurologia." | "Semiologia e exame físico neurológico." | "Doenças neurológicas, AVC, polirradiculoneurites, polineurites e doenças periféricas." | "Acidente Vascular Cerebral." | "Anatomia e fisiologia do sistema nervoso central e periférico." | "Doenças degenerativas e infecciosas do SNC."
```

### 19. Legislação da Ebserh
```
Matéria 19. id: "legislacao.ebserh". subject: "Legislação". Título sugerido: "Legislação da Ebserh: lei de criação, estatuto, regimento, código de ética e regulamento de pessoal".
Cubra: a lei que autorizou a criação da Empresa Brasileira de Serviços Hospitalares (natureza de empresa pública, finalidade, contrato com as universidades, regime de pessoal CLT, contratação temporária), o Estatuto Social (órgãos de administração e fiscalização, competências), o Regimento Interno (estrutura da sede e das filiais), o Código de Ética e Conduta (princípios, compromissos, conflito de interesses, canais de denúncia) e o Regulamento de Pessoal (admissão, deveres, regime disciplinar). Use apenas o que tiver certeza; os documentos internos mudam com frequência, então explique a lógica e a estrutura sem inventar números de artigos ou datas de aprovação.
Tópicos de edital para aliases: "Lei Federal nº 12.550, de 15 de dezembro de 2011." | "Estatuto Social da Ebserh." | "Regimento Interno da Ebserh." | "Código de Ética e Conduta da Ebserh." | "Regulamento de Pessoal da Ebserh."
```

### 20. Instrumentação e automação industrial
```
Matéria 20. id: "engenharia.instrumentacao-automacao-hidraulica-pneumatica". subject: "Engenharia". Título sugerido: "Instrumentação, automação industrial, hidráulica e pneumática".
Cubra: grandezas e medição (pressão, vazão, nível, temperatura), sensores e transdutores, transmissores (pneumáticos, eletrônicos analógicos, digitais, inteligentes, protocolos de campo — noções), elementos finais de controle e válvulas de controle, malhas de controle (on-off, PID), CLP (estrutura, linguagens, ladder), sistemas supervisórios (SCADA), sistemas hidráulicos (bombas, atuadores, válvulas, simbologia) e pneumáticos (compressores, tratamento do ar, cilindros, circuitos), automação predial (noções).
NÃO repita: manutenção industrial (já existe).
Tópicos de edital para aliases: "Sistemas hidráulicos e pneumáticos." | "Automação." | "Instrumentação." | "Instrumentação Industrial." | "Transmissão e transmissores pneumáticos, eletrônicos analógicos, digitais e inteligentes." | "Elementos finais de controle: válvulas de controle pneumáticas, hidráulicas e motorizadas." | "Aplicações em Automação Industrial."
```

### 21. Gestão ambiental
```
Matéria 21. id: "meio-ambiente.gestao-licenciamento-unidades-conservacao". subject: "Meio Ambiente". Título sugerido: "Gestão ambiental: licenciamento, unidades de conservação, recursos naturais e educação ambiental".
Cubra: conceitos de meio ambiente, recursos naturais renováveis e não renováveis, impactos ambientais e avaliação de impacto (EIA/RIMA), licenciamento ambiental (licenças prévia, de instalação e de operação; competências), Sistema Nacional de Unidades de Conservação (grupos de proteção integral e uso sustentável, categorias, criação e gestão), monitoramento da qualidade ambiental (água, ar, solo), áreas contaminadas e remediação (noções), sistemas de gestão ambiental (ISO 14001, noções), educação ambiental na prática (programas, projetos).
NÃO repita: Política Nacional de Educação Ambiental e direito ambiental (já existem; aqui é gestão técnica).
Tópicos de edital para aliases: "Unidades de Conservação." | "Recursos naturais." | "Educação ambiental." | "Educação Ambiental." | "Gestão ambiental." | "Monitoramento e avaliação da qualidade dos recursos naturais." | "Licenciamento ambiental."
```

### 22. Medicina veterinária
```
Matéria 22. id: "medicina-veterinaria.clinica-zoonoses-producao". subject: "Medicina Veterinária". Título sugerido: "Medicina veterinária: zoonoses, clínica, produção animal e ética profissional".
Cubra: zoonoses de importância em saúde pública (raiva, leptospirose, brucelose, tuberculose, leishmaniose, toxoplasmose, esporotricose), controle de populações animais e vigilância de zoonoses, clínica de pequenos e grandes animais (principais doenças), microbiologia e parasitologia veterinária, produção de ruminantes (bovinocultura de leite e de corte) e de não ruminantes (suinocultura, avicultura), uso racional e fiscalização de medicamentos veterinários, bem-estar animal, Código de Ética do Médico-Veterinário.
NÃO repita: defesa sanitária, inspeção de produtos de origem animal e ética (já existe; aqui é a parte clínica e de zoonoses).
Tópicos de edital para aliases: "Zoonoses." | "Produção de Ruminantes: Bovinocultura de leite e de corte." | "Produção de Não Ruminantes: Suinocultura." | "Uso racional de medicamentos veterinários." | "Fiscalização de produtos de uso veterinário." | "Código de Ética do Médico-Veterinário." | "Microbiologia veterinária."
```

### 23. Ética profissional nos conselhos de classe
```
Matéria 23. id: "etica.etica-profissional-conselhos". subject: "Ética". Título sugerido: "Ética profissional: conselhos de classe, responsabilidade técnica e sigilo".
Cubra: ética, moral e deontologia, o sistema de conselhos profissionais (natureza de autarquia, fiscalização do exercício, registro, anuidade), princípios comuns aos códigos de ética das profissões (dignidade, competência, sigilo profissional e suas exceções, responsabilidade técnica, publicidade, relação com colegas e clientes, conflito de interesses), infrações e penalidades éticas e processo ético-disciplinar (noções), responsabilidade civil, penal e ética do profissional, ética aplicada a áreas técnicas (TI, engenharia, comunicação, contabilidade) com exemplos.
NÃO repita: código de ética médica e ética no serviço público (já existem).
Tópicos de edital para aliases: "Ética profissional." | "Ética Profissional." | "Ética profissional (código de ética)." | "Ética profissional e responsabilidade técnica." | "Ética Profissional: responsabilidade técnica; sigilo profissional." | "Ética profissional aplicada à área de tecnologia da informação." | "Ética profissional: Princípios, deveres e responsabilidades profissionais. Sigilo e responsabilidade técnica."
```

### 24. Estruturas e materiais de construção
```
Matéria 24. id: "engenharia.estruturas-concreto-aco-materiais". subject: "Engenharia". Título sugerido: "Materiais de construção e estruturas de concreto armado, protendido, pré-moldado e aço".
Cubra: materiais de construção (aglomerantes, agregados, argamassas, concreto: dosagem, propriedades e ensaios; aço; madeira; materiais betuminosos; cerâmicos), concreto armado (princípios de dimensionamento de vigas, lajes e pilares, durabilidade e cobrimento), concreto protendido (noções e tipos de protensão), concreto pré-moldado (dimensionamento, detalhamento, montagem), estruturas de aço (ligações, perfis), estruturas de madeira (noções), fôrmas e escoramentos, esquadrias (tipos e materiais), pontes (noções).
NÃO repita: resistência dos materiais e análise estrutural; edificações (já existem).
Tópicos de edital para aliases: "Esquadrias." | "Materiais de construção civil. Aglomerantes e agregados. Materiais betuminosos. Propriedades físicas e mecânicas." | "Concreto protendido: noções gerais e tipos de protensão." | "Concreto pré-moldado: dimensionamento, detalhamento e montagem." | "Estruturas de Aço." | "Dimensionamento de estruturas de concreto armado e protendido."
```

### 25. Alfabetização e educação infantil
```
Matéria 25. id: "educacao.alfabetizacao-letramento-educacao-infantil". subject: "Educação". Título sugerido: "Alfabetização, letramento e educação infantil".
Cubra: alfabetização e letramento (conceitos e diferença), psicogênese da língua escrita (níveis de escrita), consciência fonológica, métodos de alfabetização (sintéticos, analíticos, a discussão atual), avaliação da alfabetização, Educação Infantil (concepção de criança, cuidar e educar, rotina, interações e brincadeira como eixos, campos de experiência da BNCC, avaliação sem caráter de promoção), ludicidade e jogos, transição para o ensino fundamental, políticas de alfabetização na idade certa (noções, sem inventar normas).
NÃO repita: LDB, BNCC e didática geral (já existem).
Tópicos de edital para aliases: "Alfabetização e letramento." | "Educação infantil." | "Educação Infantil." | "O lúdico na educação infantil." | "Psicogênese da língua escrita."
```

### 26. APIs e integração de sistemas
```
Matéria 26. id: "informatica.apis-rest-integracao". subject: "Informática". Título sugerido: "APIs e integração de sistemas: REST, JSON, OpenAPI e webhooks".
Cubra: conceito de API e de integração entre sistemas, HTTP (métodos, códigos de status, cabeçalhos), arquitetura REST (recursos, idempotência, versionamento, paginação), JSON e XML (sintaxe, validação com JSON Schema), documentação com OpenAPI/Swagger, autenticação e autorização em APIs (API key, OAuth 2.0, tokens JWT — noções), webhooks e eventos (eventos de negócio, filas e mensageria — noções), SOAP x REST, GraphQL (noções), microsserviços e API gateway (noções), segurança de APIs (principais riscos).
NÃO repita: engenharia de software e arquitetura de software (se já existirem; aqui é o foco em APIs).
Tópicos de edital para aliases: "JSON." | "Swagger." | "Webhook." | "Evento negocial." | "APIs REST." | "Integração de sistemas."
```

### 27. Educação escolar indígena
```
Matéria 27. id: "educacao.educacao-escolar-indigena". subject: "Educação". Título sugerido: "Educação escolar indígena".
Cubra: direitos dos povos indígenas à educação na Constituição e na LDB (ensino bilíngue, processos próprios de aprendizagem), princípios da educação escolar indígena (específica, diferenciada, intercultural, bilíngue ou multilíngue, comunitária), escola indígena e territórios etnoeducacionais (noções), formação de professores indígenas, currículo, calendário e material didático próprios, interculturalidade e práticas pedagógicas, avaliação, relação escola e comunidade. Não invente números de resoluções: explique o conteúdo.
NÃO repita: diversidade, inclusão e temas transversais (já existe).
Tópicos de edital para aliases: "Educação escolar indígena." | "Interculturalidade." | "Legislação da educação escolar indígena."
```

### 28. Tocantins
```
Matéria 28. id: "tocantins.conhecimentos-estaduais". subject: "Tocantins". Título sugerido: "Tocantins: história, geografia e economia".
Cubra: formação histórica (povos indígenas, colonização, bandeiras, mineração, norte de Goiás), movimentos emancipacionistas e criação do Estado pela Constituição de 1988, instalação e capital (Palmas), organização política e administrativa, divisão política e regiões, símbolos do Estado, povos indígenas e comunidades quilombolas, aspectos demográficos e migração, relevo, clima, vegetação (Cerrado e transição amazônica) e hidrografia (Araguaia, Tocantins), unidades de conservação (Jalapão, Ilha do Bananal), economia (agropecuária, matriz produtiva e energética, hidrelétricas), patrimônio histórico, cultural e ambiental, desenvolvimento regional.
Tópicos de edital para aliases: "Processo histórico de criação do Estado do Tocantins." | "Processo de criação do Estado do Tocantins." | "Organização política e administrativa." | "Formação territorial." | "Aspectos demográficos." | "Povos indígenas e comunidades quilombolas." | "Patrimônio histórico, cultural e ambiental." | "Recursos naturais." | "Unidades de Conservação." | "Economia do Estado." | "Desenvolvimento regional." | "Matriz produtiva." | "Matriz energética." | "Símbolos do Estado do Tocantins." | "Movimentos emancipacionistas e criação do estado em 1988." | "Aspectos econômicos e físicos do Tocantins."
OBS: "Clima.", "Recursos naturais." etc. aparecem soltos no edital de Tocantins; o Claude liga pelo contexto, não precisa pôr esses curtos nos aliases.
```

### 29. Roraima
```
Matéria 29. id: "roraima.conhecimentos-estaduais". subject: "Roraima". Título sugerido: "Roraima: história, geografia e economia".
Cubra: ocupação territorial e presença portuguesa (fortes, aldeamentos), interesses estrangeiros e a questão de fronteiras (disputa com a Inglaterra pela região do Pirara e arbitragem), a vida na região no século XIX, Roraima no século XX, criação do Território Federal (do Rio Branco a Roraima), fluxos migratórios, criação do Estado em 1988 e dos municípios, governadores do Território e do Estado (sem inventar nomes ou datas de que não tenha certeza), patrimônios históricos e pontos turísticos, terras e povos indígenas (Yanomami, Macuxi, Raposa Serra do Sol), geografia (clima, solos, regime de chuvas, relevo, hidrografia, lavrado), economia (extrativismo, agropecuária, indústria e comércio).
Tópicos de edital para aliases: "A ocupação territorial de Roraima." | "Interesses estrangeiros na região." | "A presença portuguesa." | "A vida na região no século XIX." | "Roraima no século XX." | "A delimitação das fronteiras." | "A criação do Território Federal." | "Os fluxos migratórios." | "A criação do Estado e dos seus municípios." | "Patrimônios históricos de Roraima." | "Terras indígenas." | "Povos indígenas de Roraima." | "Governadores do Território Federal de Roraima." | "Governadores do Estado de Roraima." | "Geografia de Roraima." | "Economia do Estado de Roraima."
```

### 30. Rio Grande do Norte
```
Matéria 30. id: "rio-grande-do-norte.historia-aspectos-geoeconomicos". subject: "Rio Grande do Norte". Título sugerido: "Rio Grande do Norte: história e aspectos geoeconômicos".
Cubra: a Capitania do Rio Grande no sistema de capitanias (século XVI), conquista e Forte dos Reis Magos, presença holandesa, populações indígenas nos sertões (Guerra dos Bárbaros) e comunidades quilombolas, economia e tributação no período colonial, a seca e a questão sanitária no século XIX, o Motim das Mulheres, a abolição pioneira em Mossoró (30 de setembro), Mossoró e a resistência ao bando de Lampião, o movimento de 1930 no estado, o RN na Segunda Guerra Mundial (Trampolim da Vitória, base em Parnamirim; Barreira do Inferno depois, já no programa espacial), a imprensa potiguar ("O Mossoroense"), salineiros e a extração de sal, mineração no Seridó, atividades econômicas (agropecuária, pesca, fruticultura irrigada, carcinicultura, petróleo e gás, turismo, energias renováveis eólica e solar).
NÃO repita: as matérias de legislação do RN (constituição estadual, estatutos e regimentos já existem).
Tópicos de edital para aliases: "A Capitania do Rio Grande na história das capitanias donatárias (Século XVI)." | "Indígenas nos sertões do Rio Grande colonial." | "Motim das Mulheres." | "O movimento de 1930 no Rio Grande do Norte." | "Economia e fiscalidade no período colonial: Arrecadação de tributos na capitania do Rio Grande." | "A seca e a questão sanitária no Século XIX." | "Populações Indígenas no Rio Grande do Norte." | "Comunidades Quilombolas no estado do Rio Grande do Norte." | "A Barreira do Inferno, Trampolim da Vitória e o Rio Grande do Norte na Segunda Guerra Mundial." | "Terra dos salineiros: Trabalhadores da extração de sal no Rio Grande do Norte." | "O 30 de setembro e a política Abolicionista de Vanguarda Mossoroense." | "Política e sociedade: Mossoró e a resistência ao bando de Lampião." | "A Indústria Mineradora do Seridó." | "Transformação da indústria das energias renováveis no panorama econômico e geográfico do Rio Grande do Norte."
```
