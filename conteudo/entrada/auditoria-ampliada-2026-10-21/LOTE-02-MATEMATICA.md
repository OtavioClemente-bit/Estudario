# Continuação da auditoria — Probabilidade e Porcentagem

Base: `b13dc28b5`. Data: 21/10/2026. Execução individual, sem subagentes ou modelos externos. Nenhuma matéria canônica foi alterada.

## Cobertura efetivamente realizada

Leitura de todas as 145 questões de **Probabilidade** e de todas as 145 de **Porcentagem**, incluindo enunciado, opções, explicação e comentários das alternativas. Foram lidos integralmente os cinco capítulos de cada matéria, escopo, resumo, flashcards, dicas, pegadinhas e recordação ativa. As fontes bibliográficas declaradas foram identificadas; suas URLs não foram consultadas neste lote.

Treze dessas questões já constavam da amostra anterior: cinco de Probabilidade e oito de Porcentagem. Portanto, este lote acrescenta **277 questões novas** à cobertura manual: o acumulado passa de 210 para **487 questões distintas**, em 14 matérias. Há agora **três matérias com leitura interna integral**, contando Banco de Dados. Restam **165.664 questões canônicas sem leitura manual**. Leitura interna integral não significa publicação aprovada ou conferência externa integral.

## Probabilidade — oito itens dependem de contexto ausente

Os itens abaixo remetem a dados de outras questões, mas o montador trata os itens individualmente. Não existe obrigação de entregar o item anterior junto com o posterior.

| Questão | Dados que faltam no próprio enunciado | Item que fornece os dados |
|---|---|---:|
| 76 | Quantidades que praticam futebol, vôlei e ambos | 75 |
| 82 | Quantidades que gostam de A, B e ambos | 81 |
| 83 | Quantidades que gostam de A, B e ambos | 81 |
| 92 | Quantidade de mulheres fumantes | 91 |
| 133 | Participações das máquinas na produção e taxas de defeito | 132 |
| 137 | Prevalência de 10% usada para chegar aos 50% na explicação | 136 |
| 141 | Composição das urnas e distribuição da escolha de urna | 140 |
| 143 | Participações das linhas na produção e taxas de defeito | 142 |

**Gravidade alta para uso isolado.** Alguns desses itens permitem identificar um argumento falho mesmo sem todos os números, como a inversão indevida da sensibilidade na questão 137. Ainda assim, a situação referenciada e o resultado numérico apresentado na explicação não estão definidos no próprio item.

**Reprodução do problema na integração:** com `assembleFromLibrary`, pedido de dez questões, estilo FIVE_OPTIONS, dificuldade MIXED e semente `auditoria-contexto-2`, a questão 82 foi entregue sem a 81. O cenário usa `avoidStatements` para excluir o item 81 já visto. A saída inclui “No mesmo grupo de 80 pessoas...”, mas não transporta os quantitativos de A, B e interseção. Evidência: `contexto-isolado-reproducao.json`.

**Correção proposta:** tornar cada enunciado autossuficiente, repetindo os dados necessários no próprio item. Isso preserva o fluxo de seleção atual e os gabaritos intencionados. Não é necessário reconstruir relações entre questões nem obrigar o aluno a rever um item anterior.

## Probabilidade — outras falhas confirmadas

### Questão 139 — Alta: aplicação da probabilidade total sem a hipótese de partição

O enunciado informa `P(A₁)=0,3`, `P(A₂)=0,7`, `P(B|A₁)=0,2` e `P(B|A₂)=0,1`, mas não declara que A₁ e A₂ são disjuntos e cobrem o espaço. Somar as duas probabilidades e obter 1 não prova que os eventos formam partição.

**Contraexemplo finito:** considere 100 resultados equiprováveis, com 20 em A₁ apenas, 60 em A₂ apenas, 10 na interseção e 10 fora da união. Coloque B em três resultados de A₁ apenas, quatro de A₂ apenas e três da interseção. Assim, B tem seis resultados entre os 30 de A₁ e sete entre os 70 de A₂, atendendo às condicionais fornecidas. Se B tiver mais dois, três ou cinco resultados fora da união, sua probabilidade total será, respectivamente, **0,12, 0,13 ou 0,15**. São três respostas diferentes compatíveis com os dados e presentes nas alternativas B, C e D.

Não são três gabaritos simultâneos sob um mesmo modelo: são três modelos admissíveis que demonstram que os dados não determinam uma única resposta. Acrescentar a hipótese de partição torna correto o valor intencionado de 0,13.

### Questão 88 — Média: exceção omitida na explicação

O gabarito Errado é coerente para a afirmação de que `P(A|B)` e `P(B|A)` são sempre iguais. A explicação, porém, afirma que só podem ser iguais com marginais iguais. Isso é falso quando a interseção tem probabilidade zero.

Em quatro resultados equiprováveis, tome A com um resultado e B com dois outros resultados. As marginais são 1/4 e 1/2, mas as duas condicionais são zero. A restrição de marginais iguais é necessária para igualdade das condicionais quando a interseção tem probabilidade **positiva**.

### Questão 125 — Média: justificativas numéricas de dois distratores

O gabarito de 15/28 está correto para uma bola de cada cor sem reposição, com três vermelhas e cinco azuis. Contudo:

- O comentário A atribui 5/14 a considerar só uma ordem. Uma ordem tem probabilidade **15/56**, não 5/14.
- O comentário D atribui 9/16 ao cálculo com reposição. Com reposição, a chance seria **15/32**, não 9/16.

### Questão 145 — Média: comentário incorreto

O gabarito 12/19 está correto. O comentário A diz que 6/19 resulta de usar apenas o numerador de Bayes, mas esse numerador é 0,06. A fração 6/19 é metade da resposta correta e não o numerador isolado.

### Avaliação da teoria

A teoria explicita adequadamente as principais hipóteses: finitude/equiprobabilidade da definição clássica, denominador positivo na condicional, distinção entre independência e exclusão mútua, amostragem com e sem reposição e partição na probabilidade total. O exemplo de independência par a par sem independência mútua é coerente. A principal inconsistência encontrada está entre essas ressalvas teóricas e a autonomia dos itens do banco.

Itens de dificuldade alta como 54, 56, 66–68, 75, 84 e 86 são predominantemente aplicações diretas; merecem calibração. Não foi atribuída nota global nem medida dificuldade empiricamente.

## Porcentagem — problemas de justificativa e de escopo

Não foi identificado, nesta leitura, resultado numérico central que exija simplesmente trocar a letra marcada. Foram identificadas as seguintes falhas específicas, com propostas individuais:

| Questão | Gravidade | Achado |
|---|---|---|
| 19 | Média | Os dados determinam 60 pessoas com a característica **dentro do grupo**, mas a pergunta não delimita isso claramente |
| 44 | Média | O resultado 60 é da interseção entre categoria e propriedade; não determina o total da propriedade na população inteira |
| 49 | Média | A explicação diz que se encontra a parte dividindo pela taxa; a operação apresentada encontra o **total** |
| 79 | Média | Comentário atribui R$ 550 a aplicar as duas taxas sobre a mesma base; esse erro daria R$ 510 |
| 99 | Média | Comentário atribui 31% a apenas dois aumentos de 10%; dois aumentos dão 21% |
| 104 | Média | Comentário atribui 24% a ignorar o desconto final; dois aumentos de 20% dão 44% |
| 112 | Média | Explicação diz que o produto de taxas de subconjuntos é sempre estritamente menor que cada taxa; há igualdade nos limites |
| 124 | Média | Repete a ambiguidade de contar a propriedade no subgrupo ou na população inteira |
| 134 | Média | O dado é de peças **recuperáveis**, mas o item conclui que foram **recuperadas** |

Nos itens 19, 44 e 124, a leitura intencionada de filtrar o subgrupo é reconhecível. A recomendação é explicitar a interseção e eliminar a leitura concorrente, sem inverter automaticamente o gabarito. Por exemplo, na população de 400 pessoas, os dados fixam 60 pessoas com a característica entre as 200 do grupo. Se outras 20 fora dele tiverem a característica, o total será 80, sem violar os dados fornecidos.

Na questão 134, ser recuperável é condição de possibilidade; não prova que alguma operação de recuperação foi realizada. Substituir a conclusão por “as peças defeituosas recuperáveis representam 10% do lote” resolve a diferença mantendo o cálculo.

### Avaliação da teoria

A teoria diferencia parte e base, variação relativa e pontos percentuais, fatores sucessivos, recuperação de reajustes, ponderação de taxas e margem sobre venda versus lucro sobre custo. Os exemplos e tabelas lidos são coerentes. Há ressalva explícita sobre arredondamento e condições dependentes de ordem nos fatores sucessivos. A qualidade do texto teórico é superior à de alguns comentários curtos do banco; passar no mínimo de caracteres não garante precisão da justificativa.

A matéria repete situações e operações com enunciados diferentes: por exemplo, os itens 1/46/56 repetem 15% de 320; 27/108/117 trabalham 25% de 80. Repetição pode servir à fixação, mas o montador deveria considerar a diversidade do raciocínio quando se pretende treino variado. Isso não é acusação de plágio nem prova de geração por molde.

## Verificação e arquivos

- `erratas-probabilidade.json`: 12 propostas relativas a questões, sendo oito de contexto, uma de hipótese de partição e três de explicação/comentários.
- `erratas-porcentagem.json`: nove propostas individuais de precisão de enunciado ou justificativa.
- `verificar-matematica.py` e `matematica-contraexemplos.json`: **21 verificações exatas aprovadas**, com frações e enumeração de conjuntos finitos. Esses testes conferem os casos registrados; não são um resolvedor geral do banco.
- `contexto-isolado-reproducao.json`: reprodução da perda de contexto no montador oficial.
- `contexto-dependente-candidatos.json`: 13 candidatos em seis matérias, encontrados por uma busca limitada a frases iniciais. Oito foram confirmados em Probabilidade; os demais são expressões autossuficientes ou exigem outro tipo de revisão. O arquivo não deve ser tratado como lista automática de defeitos nem como detecção exaustiva de contexto ausente.
- `cobertura-revisao.json`: registro acumulado, com hashes dos materiais e posições lidas.

As propostas foram redigidas individualmente e ficaram na pasta de entrada, respeitando as regras locais. Não foi feita publicação, alteração do banco canônico, chamada a modelo externo ou nova tentativa de rede neste lote. A conferência externa das URLs e a revisão dos conteúdos restantes continuam pendentes.
