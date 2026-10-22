# Cinco matérias — versões iniciais de 21/10/2026

Foram escritos cinco textos em Markdown, com exemplos práticos, explicação de siglas e cinco questões autorais comentadas em cada arquivo: **25 questões de exemplo no total**. As questões foram escritas individualmente, sem scripts de geração, montagem ou reordenação. Não são arquivos JSON prontos para o importador e não representam cinco matérias completas com vigência validada.

| Arquivo | Entrega efetiva | Pendência principal |
|---|---|---|
| `01-financiamento-saude.md` | Fundamentos de financiamento, cálculo, classificação de despesas e controle | Texto consolidado da LC 141, Constituição atual e requisitos detalhados |
| `02-campina-grande.md` | Introdução histórica com comércio, algodão, circulação e cultura | Cronologia, personagens, patrimônio e fontes locais |
| `03-ji-parana.md` | Introdução territorial e histórica com relações entre ambiente, ocupação e economia | Limites, distritos, mapas, séries climáticas, demografia e cronologia |
| `04-santa-luzia-processo-administrativo.md` | Fundamentos gerais de processo administrativo | Texto e identificação da norma municipal; não foi redigida uma exposição específica sem a lei |
| `05-seguranca-nuclear-radiologica.md` | Fundamentos de proteção, grandezas e segurança | Competências atuais, normas específicas, limites e requisitos de instalações |

## Fontes e limitações

Foram tentados Planalto (LC 141), IBGE Cidades (Campina Grande e Ji-Paraná), portal da Prefeitura de Santa Luzia/MG e portal da ANSN. Todos retornaram bloqueio de conexão HTTP 403. Os endereços estão nos textos. Não houve consulta bem-sucedida que permitisse declarar vigência ou validação documental.

A impossibilidade de conferir fontes não foi substituída por números de artigos, prazos locais ou estatísticas inventados. As matérias municipais permanecem introdutórias, pois explicações gerais não substituem fatos locais documentados. Santa Luzia permanece especialmente dependente da fonte: o material ensina conceitos, não afirma o conteúdo da norma citada no mapa.

## Contexto local conferido

Consultados os registros de editais de Campina Grande/PB, Ji-Paraná/RO e Santa Luzia/MG para identificar município, disciplina e escopo. O edital de Ji-Paraná contém 14 tópicos; os 12 mencionados no mapa eram apenas os tópicos sem vínculo. Os dois já vinculados não foram automaticamente considerados adequados.

Encontrados para revisão, sem alteração:

- “Regime pluviométrico”, em Ji-Paraná, associado a uma matéria de Roraima.
- “Ciclos econômicos”, em Ji-Paraná, associado a uma matéria geral de economia; verificar se cobre os ciclos históricos locais.
- A referência do mapa ao diploma de Santa Luzia como lei complementar precisa ser confirmada na publicação oficial.

## Revisão de conteúdo realizada

Conferidas a correspondência dos gabaritos com as alternativas escritas e as distinções conceituais centrais. Exemplos de cálculo têm suas premissas explícitas. Nenhum procedimento com fontes radioativas foi ensinado. Não se atribuiu a uma norma municipal não lida conteúdo de outra esfera.

## Publicação e consumo

Arquivos salvos apenas em `conteudo/entrada/`. Nenhum arquivo canônico, vínculo ou registro do banco foi alterado. Não houve commit, push, agente extra nem chamada a API de geração. O consumo exato do limite da conta não está disponível para medição nesta sessão.

Próxima etapa necessária: conferência documental para completar as lacunas e revisão editorial. Depois, adequação ao esquema e demais componentes exigidos pelo aplicativo. As pendências não foram tratadas como concluídas.

## Ampliação: 50 questões adicionais por matéria

Após confirmação do usuário de que deseja 50 por matéria, foram escritos cinco arquivos adicionais em formato certo/errado, com comentário individual: **250 novas questões**, além das 25 de exemplo incorporadas às teorias.

- [Financiamento da saúde — 50 questões](01-financiamento-saude-50-questoes.md)
- [Campina Grande — 50 questões](02-campina-grande-50-questoes.md)
- [Ji-Paraná — 50 questões](03-ji-parana-50-questoes.md)
- [Processo administrativo — 50 questões de fundamentos](04-santa-luzia-50-questoes.md)
- [Segurança nuclear e radiológica — 50 questões](05-seguranca-nuclear-50-questoes.md)

Cada arquivo contém itens Q01 a Q50 e respectivos gabaritos comentados, escritos individualmente. Não houve script de geração, embaralhamento ou equilíbrio de respostas. A conferência mecânica de contagem não constitui validação factual ou normativa.

Os limites documentais permanecem: Campina Grande e Ji-Paraná têm bancos introdutórios sobre os conceitos do texto inicial, sem cronologia ou estatísticas ainda não conferidas; Santa Luzia tem questões gerais, não específicas do diploma municipal; financiamento e segurança nuclear dependem de revisão normativa. Não se declara cobertura integral do edital. Esses limites estão também no cabeçalho de cada arquivo.

As questões estão em Markdown para revisão, não no esquema JSON de publicação. Nenhuma foi adicionada ao banco de produção.
