# Auditoria editorial e técnica — Estudário

Data: 21/10/2026. Execução individual, sem agentes auxiliares. Nenhuma matéria foi modificada.

## Parecer

A biblioteca apresenta boa consistência estrutural, mas a amostra contém falhas editoriais que prejudicam exercícios e uma explicação conceitualmente mal redigida. Não há evidência suficiente para certificar a precisão factual de toda a biblioteca. Recomenda-se corrigir os achados abaixo antes de considerar a matéria de Crase revisada.

## Escopo e método

- Censo mecânico dos 520 JSONs em `conteudo/materias/`: 23.807 questões, sendo 13.476 de múltipla escolha e 10.331 de Certo/Errado.
- Inspeção editorial direcionada de questões de Crase, Lei Anticorrupção e contratação direta; leitura adicional de seis questões iniciais de Gases e dos enunciados de seis questões difíceis dessa matéria. Isso não constitui revisão integral dessas quatro matérias.
- Verificações mecânicas de JSON, volume, alternativas marcadas, associação a capítulos, extensão de explicações, repetições literais e distribuição de gabaritos. Contagens não atestam verdade, originalidade, adequação pedagógica nem validade jurídica.
- Não houve consulta externa aos textos normativos ou bibliográficos. Links e datas de acesso presentes nos arquivos são declarações do material, não acessos comprovados por esta auditoria. Atualização legislativa em 2026 permanece pendente de conferência primária.
- Entrada, recortes, editais e conteúdo efetivamente instalado no app não foram auditados. O universo examinado é a biblioteca canônica do repositório.

## Achados confirmados

### A01 — Alta: alternativas não correspondem às lacunas

Arquivo: `conteudo/materias/portugues/crase.json`.

- Questão 48, linha 1848: o texto traz `__quela`, `__quilo` e `__ esta`; a resposta contém `àquela — àquilo — a esta`. Inserção literal produz `àquelaquela`, `àquiloquilo` e `a esta esta`. Uniformizar lacunas e alternativas: ou retirar os fragmentos e fornecer expressões completas, ou manter os fragmentos e oferecer somente os elementos faltantes.
- Questão 50, linha 1928: há quatro lacunas em `__ 14 horas`, `__ 9 __ 12 horas`, `__ tarde inteira`, mas as opções agrupam três componentes, incluindo `das 9 às 12`. Ajustar para quatro componentes (`às — das — às — a`) ou transformar o intervalo inteiro em uma única lacuna, sem repetir os numerais.
- Questão 51, linha 1968: as palavras `noite`, `medida que` e `pressas` já estão no enunciado, mas aparecem novamente nas opções. A resposta deve fornecer apenas `à — à — às`, ou as lacunas devem substituir as expressões inteiras.
- Questão 52, linha 2008: os destinos já estão escritos e são repetidos nas opções. Fornecer somente `à — a — a` ou retirar os topônimos do texto a preencher.
- Questão 55, linha 2128: a opção `à maneira de — a prazo — a revisar` repete `prazo` e `revisar` já presentes no enunciado. Usar `à maneira de — a — a`, ou reformular todas as lacunas para expressões completas.

Impacto: o estudante precisa adivinhar se deve inserir ou substituir texto, apesar de a intenção gramatical das respostas ser reconhecível. São cinco exercícios com defeito de montagem; não são cinco gabaritos conceitualmente refutados.

### A02 — Média: explicação troca a posição da preposição

Arquivo: `conteudo/materias/portugues/crase.json`, questão 36, linha 1533.

Trecho: “Antes de até, pode-se empregar ou omitir a preposição a”. Os exemplos do próprio item são `até a entrada` e `até à entrada`. A preposição facultativa discutida ocorre **depois de até**. Corrigir a explicação para: “Depois de até, admite-se a presença ou ausência da preposição a; diante do artigo feminino, essa variação permite até a entrada e até à entrada”. O gabarito Certo pode ser mantido.

## Pontos para revisão especializada — sem erro factual declarado

1. **Crase, questão 54, linha 2088:** apresenta a ausência de determinação de distância como critério para uma resposta única. O uso de `a distância` e `à distância` merece fundamentação em referência normativa explicitamente adotada, pois há variação de tratamento em fontes e contextos. Não alterar gabarito sem essa conferência.
2. **Contratação direta, questão 21, linha 1022:** afirma teto de R$ 130.984,20 para 2026 e cita decreto de 2025 nas fontes. A comparação aritmética com R$ 131.000,00 está coerente internamente, mas o valor e a vigência do decreto precisam ser conferidos em fonte oficial. Não há fundamento nesta auditoria para chamar esse valor de falso ou atualizado.
3. **Fontes da Lei Anticorrupção:** textos legais do Planalto estão classificados como `COMPLEMENTAR`, enquanto outra matéria usa `OFICIAL` para leis. É uma inconsistência de metadados; sua consequência depende de como o aplicativo usa esse campo. Padronizar após verificar o contrato de dados.
4. **Dificuldade e distratores:** em Gases, a questão sobre CO inclui alternativas como “só existe em temperaturas abaixo de zero” e “não oferece risco quando misturado ao ar”. Elas são facilmente elimináveis e enfraquecem o treino em nível de prova. Em Crase, itens difíceis como a questão 25 exigem sobretudo reconhecimento direto de regra. Revisar a dificuldade com critérios pedagógicos e exemplos de provas; os rótulos atuais não provam dificuldade real.

## Resultados mecânicos

Nos critérios efetivamente verificados:

- 520/520 arquivos abrem como JSON.
- Todas as matérias têm pelo menos 40 questões, 12 flashcards, uma fonte e 6.000 caracteres somados de teoria.
- Todas as 23.807 questões têm exatamente uma opção com `correct` verdadeiro e capítulo correspondente a `section`.
- Todas as explicações têm pelo menos 120 caracteres.
- Nenhuma matéria apresenta repetição literal de enunciado dentro dela; nenhuma supera 15% de explicações integralmente idênticas dentro dela. Paráfrases e repetições entre matérias não foram medidas.
- Cada matéria tem pelo menos oito questões de cada dificuldade FACIL, MEDIA e DIFICIL.
- Nas questões com cinco alternativas, nenhuma matéria concentra mais de 40% dos gabaritos em uma letra.
- Nenhuma matéria supera 40% de respostas estritamente mais longas ou estritamente mais curtas nas questões com cinco alternativas. Se empates forem contados como extremos, aparecem 76 matérias acima do limite de maior tamanho e 81 no de menor tamanho; isso é sensível à definição e não constitui, sozinho, evidência de pista indevida.

Não foram verificados todos os requisitos do guia de geração. Em particular, não foram certificados: quantidade mínima por formato em cada matéria, qualidade semântica das explicações, validade de URLs, normas vigentes, correspondência a editais e originalidade.

## Encaminhamento

Prioridade imediata: corrigir A01 e A02 em proposta separada para revisão, respeitando o fluxo de `conteudo/entrada/`. Depois, conferir referências primárias dos temas jurídicos e revisar gabaritos lendo todas as opções. Para controlar consumo, continuar por lotes pequenos, com registro de cobertura e pendências, sem agentes paralelos.

Conclusão limitada: estrutura consistente nos testes realizados; qualidade editorial com problemas confirmados na amostra; precisão factual global ainda não certificada. Não é defensável atribuir uma nota numérica à biblioteca inteira com esta cobertura.
