# Auditoria global do conteúdo — Estudário

**Base examinada:** commit `b13dc28b5`, em 21/10/2026. Execução individual, sem subagentes, chamadas a modelos externos ou publicação. Este é o parecer consolidado mais recente; reúne os achados anteriores revalidados e os resultados da varredura global.

## Resultado

Foram consolidadas **42 questões com problemas confirmados, em 11 matérias**: **19 de gravidade alta e 23 de gravidade média**. São 26 questões já apontadas nos lotes anteriores, após retirar duas recomendações de precisão da contagem de erros, e **16 novos achados**. As duas recomendações retiradas referem-se à distinção padrão SQL/produto nas questões 123 e 129 de Banco de Dados; continuam registradas como sugestões, sem serem tratadas como gabaritos comprovadamente errados.

**Parecer:** a biblioteca passa no validador estrutural, mas contém falhas de conteúdo e de correção que prejudicam o estudo. Uma opção marcada como correta pode contrariar a própria explicação, mesmo quando o arquivo tem estrutura válida. A aprovação técnica atual não deve ser usada como selo de revisão factual.

| Problema confirmado | Questões | Gravidade |
|---|---:|---|
| Gabarito marcado incorretamente | 7 | Alta |
| Sem alternativa compatível/correta na formulação examinada | 2 | Alta |
| Mais de uma alternativa defensável | 1 | Alta |
| Dados insuficientes para a resposta única pretendida | 1 | Alta |
| Dependência de dados presentes apenas em outra questão | 8 | Alta |
| Explicação jurídica incompatível com as letras e opções atuais | 9 | Média |
| Outros erros de explicação/comentários e ambiguidades reproduzíveis | 14 | Média |
| **Total de questões distintas** | **42** | **19 altas / 23 médias** |

As categorias contam questões, não trechos: uma questão com dois comentários defeituosos continua sendo uma ocorrência. A lista não pretende estimar a taxa global de erro, nem provar que não há outros problemas.

## Cobertura da varredura

A leitura computacional percorreu os campos de todas as **255.872 questões** em matérias, recortes e simulados. Foi complementada pelo censo de editais e focos e pela confirmação humana dos casos selecionados. Os detectores comparam marcações, textos, referências internas e expressões numéricas restritas; não compreendem automaticamente todo o conteúdo de qualquer disciplina.

| Conjunto | Arquivos | Questões ou referências |
|---|---:|---:|
| Matérias | 1.184 | 166.151 questões |
| Recortes | 4.592 | 79.815 questões |
| Simulados | 100 | 9.906 questões |
| Editais | 4.206 | 496.908 tópicos |
| Focos | 838 | 39.431 tópicos de foco |
| **Total de arquivos desses conjuntos** | **10.920** | — |

**Limite fundamental:** a leitura humana acumulada é de **504 questões canônicas em 23 matérias**, com leitura interna integral de três matérias: Banco de Dados, Probabilidade e Porcentagem. Permanecem 165.647 questões canônicas sem leitura manual. Os recortes e simulados foram varridos integralmente pelas regras mecânicas, sem certificação semântica integral. Portanto, esta é uma auditoria de alcance global com confirmação de achados, não uma declaração de que todas as questões foram resolvidas individualmente por um revisor.

## Prioridade imediata: gabaritos e itens sem resposta válida

Os caminhos abaixo são relativos a `conteudo/materias/`. A numeração começa em 1 no array `questions`.

| Matéria / questão | Marcada | Resposta ou ação adequada | Evidência |
|---|---|---|---|
| `administracao-geral/projetos-qualidade.json`, 61 | D | **E** | Os grupos de processos do PMBOK 6 estão em E; D descreve outro conjunto e seu comentário diz “Errada” |
| `administracao-publica/modelos-governanca.json`, 59 | D | **E** | D é fidelidade pessoal ao soberano; a pergunta e a explicação tratam de autoridade legal-racional, descrita em E |
| `direito-constitucional/poder-legislativo.json`, 107 | D | **E** | A explicação atribui a declaração à Mesa da Casa, E; D diz decisão do Senado em qualquer caso |
| `portugues/figuras-de-linguagem.json`, 88 | D | **E** | D afirma que antítese exige “como”; a distinção explicada entre antítese e paradoxo está em E |
| `portugues/redacao-oficial.json`, 139 | C | **E** | C transforma frase simples em rebuscada; E faz a simplificação pedida e é a última alternativa mencionada na explicação |
| `engenharia/desenho-tecnico-cad.json`, 16 | B | **E** | O pedido é interoperabilidade semântica BIM: IFC, E; B é PDF vetorial, que a própria explicação rejeita |
| `informatica/banco-dados-sql.json`, 78 | E/Errado | **C/Certo** | Primária simples não exclui outra candidata composta com dependência parcial; há contraexemplo formal reproduzido |
| `direito-civil/obrigacoes.json`, 132 | D | **Reformular; nenhuma opção expressa a regra explicada** | Todas as opções têm comentário de erro; D nega o direito à pena, enquanto a explicação descreve escolha a benefício do credor |
| `informatica/banco-dados-sql.json`, 91 | D | **Reformular D** | FNBC exige superchave para determinante de dependência não trivial; a opção exige candidata mínima, condição diferente |

Recomenda-se suspender esses nove itens do uso avaliativo até a integração e revisão das correções. No item de Obrigações, uma simples troca de letra não resolve. No item 91 de Banco de Dados, a conclusão decorre da definição formal explicitada na própria explicação; o ajuste é trocar a exigência de candidata pela de superchave e manter a ressalva de dependência não trivial.

Nos achados jurídicos, a contradição interna está comprovada pelos próprios campos do item. A certificação da vigência das normas e precedentes continua separada e pendente de fonte primária acessível.

## Outros problemas de gravidade alta

**Banco de Dados, questão 90:** a pergunta oferece 2FN e 3FN como respostas distintas, sem pedir a primeira forma normal violada. Uma violação de 2FN também impede 3FN. Há ainda omissão sobre o atributo ser não primo. O pacote de erratas propõe dados completos e comando inequívoco.

**Probabilidade, questões 76, 82, 83, 92, 133, 137, 141 e 143:** dependem de números de outro item. Foi reproduzida a entrega da questão 82 sem a 81 no montador real, usando cenário de questão anterior já vista. Não há transporte automático desses dados. Alguns itens permitem perceber um argumento falho sem todos os números, mas não permitem reconstruir a situação e o cálculo numérico apresentado na explicação. Cada item deve conter os dados necessários.

**Probabilidade, questão 139:** a soma `P(A₁)+P(A₂)=1` não demonstra que os eventos formam partição. Sem essa hipótese, os mesmos dados admitem modelos com respostas 0,12, 0,13 ou 0,15, valores presentes nas opções. A proposta acrescenta a hipótese de partição; os três modelos foram verificados com conjuntos finitos.

## Nove explicações jurídicas desalinhadas

Arquivo: `direito-penal/jurisprudencia-tribunais-superiores.json`.

| Questão | Letra marcada no arquivo | Letra anunciada na explicação |
|---|---|---|
| 3 | E | C |
| 6 | E | C |
| 22 | E | C |
| 23 | A | B |
| 32 | D | B |
| 33 | E | C |
| 35 | D | B |
| 37 | A | C |
| 38 | A | C |

Não é mera preferência de redação: as letras anunciadas apontam para afirmações incompatíveis com o desenvolvimento da própria justificativa. Em alguns itens, como 3, 6 e 37, também estão trocadas referências às alternativas no corpo do texto. A questão 37 chega a chamar A de falsa ao discutir um conteúdo que hoje está em C.

**Ação:** reconstruir a explicação com base no conteúdo de cada opção atual. Não alterar o gabarito para fazê-lo coincidir com uma letra antiga. Conferir separadamente os enunciados das súmulas, os precedentes e os marcos legais antes de atestar atualização jurídica.

## Distribuição de todos os 42 casos

| Arquivo relativo a `conteudo/materias/` | Questões com achado confirmado |
|---|---|
| `informatica/banco-dados-sql.json` | 78, 90, 91, 93, 104 |
| `matematica/probabilidade.json` | 76, 82, 83, 88, 92, 125, 133, 137, 139, 141, 143, 145 |
| `matematica/porcentagem.json` | 19, 44, 49, 79, 99, 104, 112, 124, 134 |
| `administracao-geral/projetos-qualidade.json` | 61 |
| `administracao-publica/modelos-governanca.json` | 59 |
| `direito-civil/obrigacoes.json` | 132 |
| `direito-constitucional/poder-legislativo.json` | 107 |
| `portugues/figuras-de-linguagem.json` | 88 |
| `portugues/redacao-oficial.json` | 139 |
| `engenharia/desenho-tecnico-cad.json` | 16 |
| `direito-penal/jurisprudencia-tribunais-superiores.json` | 3, 6, 22, 23, 32, 33, 35, 37, 38 |

Os 14 problemas médios restantes envolvem explicações ou comentários numéricos incorretos, generalização falsa nos limites percentuais, ambiguidade entre total e subgrupo e confusão entre peça recuperável e peça efetivamente recuperada. Os contraexemplos e as propostas estão nos relatórios e erratas anteriores, incorporados ao inventário consolidado.

Cada ocorrência possui identificador `AG-001` a `AG-042`, caminho, posição, linha do enunciado, hash do arquivo, texto marcado, explicação atual e descrição do problema em **`achados-consolidados.json`**. A versão **`achados-consolidados.csv`** permite filtrar por matéria, gravidade e tipo. Não se trata de 42 questões novas: esse é o total único confirmado até esta etapa.

## Cobertura de editais e entrega

- **49.952 tópicos sem matéria vinculada**, entre 496.908 tópicos: aproximadamente 10,05%. Isso não equivale a 49.952 matérias ausentes; pode haver repetição ou conteúdo existente ainda sem ligação.
- **54 matérias sem entrega de dez questões de quatro alternativas** no cenário exercitado. Os demais estilos testados montaram todas as matérias. A ausência de cobertura não é erro factual nem falha de execução.
- Existem conflitos de destino em 3.462 textos normalizados de tópico, ou 1.574 quando qualificados por disciplina. O importador resolve por maioria. Os números são riscos de associação a revisar, não contagem de vínculos comprovadamente errados.

Esses itens são pendências operacionais, separadas dos 42 defeitos de conteúdo confirmados.

## Verificações técnicas e o que elas comprovam

O censo oficial foi reexecutado nesta etapa sobre todas as matérias e recortes: **zero rejeições pelo validador**, apesar dos erros de conteúdo acima. A execução percorreu as referências de editais e focos e exercitou 222.701 conversões de questão. Não houve falha de preservação da resposta marcada nas condições testadas; preservar uma marcação originalmente errada não corrige o conteúdo.

Os resultados de montagem da mesma base registram 9.328 pedidos, 9.274 montados e 54 retornos sem cobertura para quatro alternativas, sem erros de execução. Há registro de 21 testes existentes aprovados sob adaptador Node e de verificações matemáticas exatas. Nenhum desses resultados equivale a teste visual do app, teste autenticado de produção ou execução nativa Android/Deno.

## Alertas descartados e limites dos detectores

- Os **674 campos de explicação menores que 120 caracteres** atendem à exceção do validador por comentários nas opções. Não são 674 defeitos estruturais.
- `errorConceptKey: null` é permitido; as 113.599 ausências não são tratadas como erro.
- Dos 424 itens que acionaram o detector de comentário contrário à marcação, **seis casos positivos foram confirmados como defeito**. Os outros 418 possuem comandos negativos no enunciado; não foram contabilizados automaticamente como erro. Foram inspecionados exemplos que corretamente pedem a alternativa incorreta.
- Os sete alertas de alternativas numericamente equivalentes encontrados pela regra restrita foram descartados como prova de dupla resposta: os comandos exigem forma **irredutível**, que distingue as representações.
- Alertas de igualdades numéricas não foram promovidos automaticamente a erro. Extração parcial, sinal, unidade, LaTeX, aproximação e cálculo errado citado como distrator produzem falsos positivos. A categoria bruta está explicitamente marcada como candidata.
- Não foram encontrados conflitos de resposta para itens com **mesmo enunciado e mesmo conjunto textual de opções**, cruzando as três bases. O resultado se limita à identidade textual normalizada, não certifica equivalência semântica nem ausência de erros compartilhados.

## Limites que permanecem

A rede bloqueou os acessos diretos finais a Planalto, STF, COFEN e documentação PostgreSQL com HTTP 403; o registro está em `fontes/ultima-tentativa.json`. Foi possível conferir trechos de documentação primária do PostgreSQL 18 no repositório oficial do projeto pelo GitHub. Não se declara revisão atualizada de todas as leis, jurisprudência, protocolos e bibliografia a partir de 6.840 URLs apenas inventariadas.

Também permanecem pendentes a revisão semântica de todas as questões não lidas, a correspondência integral dos editais a seus documentos oficiais e a inspeção do app em dispositivo/produção. Os arquivos sem achado pelas regras aplicadas não receberam selo de “conteúdo correto”.

## Encaminhamento concreto

1. Corrigir ou retirar do uso avaliativo os nove itens da primeira tabela; revisar em seguida os dez itens de resposta múltipla, hipótese insuficiente e contexto perdido.
2. Reescrever as nove explicações jurídicas para corresponderem às opções atuais, com conferência das referências.
3. Integrar as demais erratas confirmadas e revalidar cada material final.
4. Acrescentar ao processo editorial uma checagem de consistência entre marcação e justificativa, levando em conta comandos que pedem a alternativa incorreta. A checagem deve rejeitar contradições evidentes e encaminhar casos semânticos para leitura, sem “corrigir” gabaritos por programa.

As propostas e evidências estão em `conteudo/entrada/`, conforme `AGENTS.md` e `conteudo/AGENTS.md`. Não houve alterações em `materias/`, `recortes/`, `editais/`, no código do app ou em produção.
