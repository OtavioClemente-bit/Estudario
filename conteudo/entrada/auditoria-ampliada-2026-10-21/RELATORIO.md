# Auditoria ampliada do Estudário — 21/10/2026

Base: `b13dc28b5`. Trabalho individual, sem subagentes e sem chamadas a modelos pagos externos. Scripts e resultados estão nesta pasta. Nenhum conteúdo canônico foi alterado ou publicado.

**Parecer consolidado mais recente:** consulte **`AUDITORIA-GLOBAL.md`**, com varredura das 255.872 questões nas três bases e 42 questões com achados confirmados em 11 matérias. `achados-consolidados.csv` e `achados-consolidados.json` contêm a lista única com evidências e localização. As seções deste relatório preservam os detalhes das etapas anteriores.

**Situação: auditoria integral ainda em andamento.** O censo estrutural e as verificações de integração descritas abaixo cobrem a base inteira. A leitura humana acumulada cobre **504 das 166.151 questões canônicas**, em 23 matérias. **Banco de Dados, Probabilidade e Porcentagem** tiveram leitura interna integral, cada uma com cinco capítulos e 145 questões, além de resumo, flashcards, dicas, pegadinhas e recordação ativa. Restam **165.647 questões canônicas sem leitura manual**. Não há certificação factual global nem inferência estatística de taxa de erro a partir dessa amostra dirigida.

**Continuação mais recente:** `LOTE-02-MATEMATICA.md` registra 277 questões novas lidas, 21 propostas de revisão e 21 verificações matemáticas exatas. Foi reproduzida no montador oficial a entrega de uma questão de Probabilidade sem os dados que ela referencia em outro item. As propostas estão em `erratas-probabilidade.json` e `erratas-porcentagem.json`, sem alterações nos arquivos canônicos.

## Retificação do relatório anterior

**As 674 explicações curtas não são 674 falhas de validação.** O validador oficial permite, em múltipla escolha, campo de explicação com pelo menos 40 caracteres quando todas as alternativas têm comentários e a soma alcança pelo menos 180 caracteres. Todas as 674 ocorrências atendem a essa exceção. `deliverQuestion` anexa os comentários à explicação entregue. O relatório anterior foi retificado; a contagem bruta permanece registrada, com a classificação corrigida.

**`errorConceptKey: null` é permitido pelo contrato.** As 113.599 ausências são informação sobre cobertura de metadados, não defeito de formato. O código Android mantém a ausência como nulo; as rotinas de pontos fracos usam conceitos cadastrados e contagens por conceito. Ampliar esses vínculos pode melhorar a cobertura diagnóstica, mas não se deve preenchê-los artificialmente.

## Cobertura técnica executada

| Universo | Quantidade | Verificação |
|---|---:|---|
| Matérias | 1.184 | Validador oficial, aliases, IDs e referências |
| Questões canônicas | 166.151 | Censo e conversor oficial de entrega |
| Recortes | 4.592 | Validador oficial e referência à matéria |
| Questões nos recortes | 79.815 | Incluídas na validação de seus recortes |
| Editais | 4.206 | Referências, tópicos, dados mínimos de catálogo |
| Tópicos de edital | 496.908 | Vínculo com matéria existente ou ausência registrada |
| Arquivos de foco | 838 | Índices de capítulos e identificadores posicionais de questões |
| Tópicos nos arquivos de foco | 39.431 | Verificação mecânica das referências |
| Simulados | 100 | JSON, edital, matéria, numeração e marcação da resposta |
| Questões nos simulados | 9.906 | Triagem mecânica adicional, sem revisão factual |
| URLs distintas de fontes das matérias | 6.840 | Inventário; não significa 6.840 fontes acessadas |

O importador oficial foi executado em modo de leitura, sem `--publicar`, por adaptador Node. Informou **“Tudo certo”**, com 1.184 matérias, 4.592 recortes, 283.845 apelidos, 4.048 editais no catálogo e 65.553 apelidos com foco. A validação aceita tópicos de edital ainda não vinculados e os reporta como pendência.

Não houve rejeição de matéria ou recorte pelo validador oficial. Não houve referência preenchida a matéria inexistente nos recortes ou editais, nem índice inválido nos arquivos de foco. Essas verificações certificam existência e formato; não certificam que um vínculo seleciona o conteúdo semanticamente correto.

## Achados de conteúdo — Banco de Dados

Arquivo: `conteudo/materias/informatica/banco-dados-sql.json`. Posições no array `questions`, começando em 1.

### Alta — Questão 78: gabarito incorreto

Afirma que é possível violar 2FN com chave primária simples e marca Errado. A 2FN considera todas as chaves candidatas. Em `R(A,B,C,D)`, com `A → BCD`, `BC → A` e `B → D`, as candidatas são A e BC. Mesmo escolhendo A como primária simples, D depende de parte de BC. O gabarito deve ser Certo. O aviso correspondente na teoria também precisa de ressalva.

### Alta — Questão 90: mais de uma alternativa defensável

Pergunta qual forma normal é violada por dependência parcial, oferecendo 2FN e 3FN separadamente, sem pedir a primeira violada. Uma relação que não atende 2FN também não atende 3FN. A própria justificativa do distrator reconhece a violação “mais evidente” da 2FN, o que não torna 3FN falsa. Além disso, a condição de atributo não primo não é explicitada. A proposta fixa as dependências e a única candidata, declara 1FN e pergunta a primeira forma normal não satisfeita.

### Alta — Questão 91: alternativa indicada exige condição errada

A opção D define FNBC exigindo que todo determinante seja **chave candidata**, enquanto a explicação corretamente fala em **superchave** para dependências não triviais. Candidata exige minimalidade; superchave pode conter atributos excedentes. Em relação com `A → BC`, a dependência derivada `AB → C` tem determinante superchave não mínimo e não impede FNBC. Corrigir a alternativa D. Revisar também o comentário D da questão 84 e a tabela teórica, que omite a ressalva de dependência não trivial.

### Média — Questão 93: DEFAULT não valida domínio

O gabarito B é coerente. A explicação lista DEFAULT entre os mecanismos que garantem validade dos valores. Um valor padrão não rejeita valor explícito inválido. A proposta distingue fornecimento de padrão e validação por tipos/restrições.

### Média — Questão 104: explicação generaliza UNKNOWN

O gabarito Certo pode ser mantido. A explicação diz que `NOT IN` com NULL resulta em desconhecido para todas as linhas. Mas `4 NOT IN (4,NULL)` é FALSE; `9 NOT IN (4,NULL)` é UNKNOWN. Nenhuma dessas duas linhas passa por WHERE, por motivos lógicos diferentes. A documentação oficial do PostgreSQL e a reprodução SQL dão suporte à correção.

### Média — Questões 123 e 129: falta fixar padrão ou produto

O capítulo distingue padrão SQL e implementação PostgreSQL, mas os itens usam formulações genéricas sobre READ UNCOMMITTED. No PostgreSQL 18 ele se comporta como READ COMMITTED, o que altera a interpretação de alternativas sobre leituras sujas. As propostas explicitam as garantias mínimas do padrão, preservando a distinção explicada na teoria. Não classifico esses dois itens como gabaritos definitivamente errados sem essa interpretação de contexto; são problemas de precisão e ambiguidade evitável.

### Outras ressalvas editoriais

- Questão 69: a definição de FK deve admitir chave elegível e autorreferência; limitar a explicação a primária de outra relação é incompleto.
- Questão 72: substituir a referência exclusiva à chave primária por todas as candidatas na explicação da 2FN.
- Questões difíceis como 54, 66, 103, 105, 118, 124, 132 e 133 exigem principalmente reconhecimento direto. O nível merece calibração; não foi medido empiricamente.
- Distratores como “remover o SGBD”, na questão 133, reduzem a qualidade do treino.
- A teoria é acessível, usa exemplos, tabelas e casos relevantes como CHECK com NULL e LEFT JOIN filtrado por WHERE. Os problemas de normalização precisam ser corrigidos antes de considerá-la revisada para publicação.

As sete propostas principais e as ressalvas teóricas estão em **`erratas-banco-dados.json`**. São propostas de revisão, não um pacote canônico pronto para importar. Foram escritas individualmente, sem gerar ou alterar questões por programa. Os contraexemplos estão em `contraexemplos.json` e podem ser reproduzidos por `verificar-contraexemplos.py`.

## Achados de cobertura e qualidade na base inteira

### Tópicos de edital sem vínculo

**49.952 tópicos de 496.908 estão sem matéria vinculada: aproximadamente 10,05%.** Há pelo menos uma ausência em 4.094 editais. Isso não significa que faltam 49.952 matérias: textos repetem-se entre concursos, um assunto pode estar coberto em matéria existente e ainda não ligado, ou exigir recorte específico.

Há 3.462 textos normalizados de tópico ligados a mais de uma matéria em editais distintos; mesmo acrescentando a disciplina, há 1.574 chaves com múltiplos destinos. O importador resolve essas disputas por maioria de votos e desempate pelo identificador. Isso é comportamento observado no código, não prova de ligação errada em cada caso. A cobertura semântica e as escolhas por maioria ainda precisam de revisão especializada.

Três editais de catálogo não apresentam URL HTTP no campo esperado. As ocorrências de ausência de vínculo e os totais estão em `edital-cobertura.json` e `censo-integracao.json`. Editais mais extensos com muitas ausências incluem os de promotor do MPRJ, cartórios do TJMS e defensor da DPE-BA.

### Comentários repetidos

**444 questões em 52 matérias** repetem o mesmo comentário em todas as quatro alternativas incorretas. Exemplo: “Errada: não é critério.” nas opções da questão 56 de Materiais e Logística. É sinal para revisão pedagógica, não prova automática de erro: a explicação principal pode conter justificativa suficiente. As posições estão em `comentarios-repetidos.json`.

### Similaridade textual

Além dos dois enunciados literais repetidos entre matérias já registrados no relatório anterior, a normalização de números encontrou dois grupos de três ou mais enunciados com a mesma redação: quatro perguntas de MDC e três de MMC. Variar os números pode ser exercício legítimo. Não foram encontrados parágrafos teóricos idênticos de pelo menos 300 caracteres em matérias distintas, após normalizar espaços e caixa.

O método não detecta todas as paráfrases, não é detector de plágio e não prova diversidade semântica. Não foram emitidas acusações de geração por molde com base nesses resultados.

## Integração e aplicativo

Foram exercitadas **222.701 conversões de questão**, incluindo entrega de cinco alternativas e conversão elegível para quatro. Nessa execução, com uma semente fixa, o conversor preservou o texto da opção correta, sua marcação única e o campo de explicação, e retirou os comentários das opções para a entrega prevista. Não foram persistidas questões convertidas.

As montagens em lote estão em `montagem-materias.json`: cada matéria foi solicitada com dez questões, nível misto e todos os blocos, nos quatro estilos; cada recorte também foi exercitado em MIXED. A abrangência dessas combinações não equivale a testar todos os pedidos, sementes, focos ou estados de conta.

Resultado: 9.328 pedidos exercitados, 9.274 montados e zero erros de execução. Os 54 retornos nulos ocorreram no estilo de quatro alternativas; os demais estilos montaram todas as matérias e os 4.592 recortes montaram no cenário MIXED. Os nulos indicam falta de cobertura para esse pedido específico, não rejeição do arquivo pelo validador.

Os **21 testes existentes** de biblioteca e simulados passaram sob Node 24, com transformação TypeScript e adaptador de `Deno.test`/assertions. A execução não foi nativa Deno. Relatório: `testes-existentes.json`.

Na leitura estática do Android, o mapeamento preserva `errorConceptKey: null`, converte flashcards em seções e recebe a explicação montada no servidor. O leitor usa Markwon com plugins de tabelas e LaTeX. O importador de simulados concatena `textoBase` e enunciado. Isso foi conferido no código; não há prova visual de funcionamento em dispositivo.

Não havia Android SDK configurado e existente nas variáveis usuais do ambiente, nem execução autenticada do app disponível. Não foram realizados testes instrumentados, inspeção visual de telas, validação de acessibilidade em dispositivo ou conferência da versão publicada em produção.

## Fontes externas e bloqueios

Os acessos diretos ao Planalto e à documentação PostgreSQL retornaram HTTP 403 no túnel da rede. A política observada estava restrita e aplicada. A documentação primária do PostgreSQL 18 foi obtida com sucesso no repositório oficial `postgres/postgres`, tag `REL_18_0`, pelos arquivos `ddl.sgml`, `func.sgml` e `mvcc.sgml`. As cópias e os registros de acesso estão em `fontes/`.

Foram conferidos os trechos sobre DEFAULT, chave estrangeira autorreferente e chave referenciada elegível, semântica de NOT IN com NULL e equivalência de READ UNCOMMITTED a READ COMMITTED. Referência estável à versão 18 não certifica a versão corrente de toda URL `/current/` declarada pelas matérias.

O usuário informou que não consegue liberar domínios e pediu uma última tentativa. O resultado final dessa tentativa está em **`fontes/ultima-tentativa.json`**. Não foram contornadas restrições de rede nem usadas cópias não oficiais para declarar vigência jurídica.

## O que ainda impede o encerramento integral

1. Leitura e resolução das 165.647 questões canônicas restantes, além da revisão factual das 79.815 questões de recortes e 9.906 de simulados.
2. Revisão integral das teorias e dos materiais auxiliares das demais 1.181 matérias.
3. Conferência externa de legislação, jurisprudência, protocolos e bibliografia, com registro da fonte e da versão efetivamente acessada.
4. Revisão semântica das ligações de editais e da seleção por foco, incluindo os tópicos sem vínculo e conflitos de destino.
5. Integração das erratas no fluxo de revisão/publicação e revalidação do material resultante.
6. Testes do app em dispositivo e confronto com os dados publicados em produção.

`cobertura-revisao.json` registra arquivo, hash, posições efetivamente lidas e estado de revisão de todas as 1.184 matérias. Não se deve marcar como revisada uma matéria apenas porque passou no validador. A fila preserva o trabalho para continuação sem reler o que já foi conferido, desde que o hash permaneça igual.

As regras de `AGENTS.md` e `conteudo/AGENTS.md` reservam a escrita à pasta de entrada e proíbem alterações diretas nas matérias publicadas. Por isso o trabalho entrega erratas e evidências neste local, sem alterar a base canônica.

## Reprodução das verificações

Na raiz do repositório, executar sequencialmente:

```sh
node --experimental-transform-types conteudo/entrada/auditoria-ampliada-2026-10-21/auditar.mjs
node --experimental-transform-types conteudo/entrada/auditoria-ampliada-2026-10-21/montagem-materias.mjs
node --experimental-transform-types conteudo/entrada/auditoria-ampliada-2026-10-21/testes-existentes.mjs
python conteudo/entrada/auditoria-ampliada-2026-10-21/verificar-contraexemplos.py
python conteudo/entrada/auditoria-ampliada-2026-10-21/verificar-matematica.py
```

O importador oficial também pode ser reproduzido por `importador-leitura.mjs`, que fixa argumentos vazios e termina antes do trecho de publicação. Seu log foi preservado. Os scripts de auditoria escrevem apenas seus relatórios na pasta de entrada.
