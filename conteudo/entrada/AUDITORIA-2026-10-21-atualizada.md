# Auditoria das matérias do Estudário — base atualizada

Data: 21/10/2026. Base: commit `b13dc28b5`, após o git pull.
Execução individual, sem agentes auxiliares ou chamadas a modelos externos. Nenhuma matéria foi alterada.

**Retificação após aprofundamento:** o validador oficial aceita explicação curta acompanhada de comentários suficientes nas alternativas. As 674 ocorrências anteriormente classificadas como abaixo do mínimo atendem a essa exceção. `errorConceptKey: null` também é permitido pelo contrato. O relatório ampliado em `auditoria-ampliada-2026-10-21/RELATORIO.md` traz a classificação revisada, integração e fontes conferidas.

Este relatório substitui o parecer anterior para a base atual. O relatório anterior permanece como registro histórico; seus achados de Crase não devem ser aplicados automaticamente à versão nova.

## Parecer executivo

A biblioteca tem **1.184 matérias e 166.151 questões**. A triagem estrutural apresenta boa consistência, mas não comprova correção factual. A revisão manual dirigida inicial de **73 questões em 14 matérias** encontrou um gabarito tecnicamente incorreto sobre normalização de bancos de dados, além de problemas pedagógicos em exemplos de distratores e dificuldade. O censo identificou **674 campos de explicação menores que 120 caracteres em 24 matérias**; a revisão posterior confirmou que todos atendem à exceção do validador por comentários nas alternativas.

Os cinco defeitos de lacunas e o erro de redação sobre “até” apontados na auditoria anterior foram corrigidos na matéria atual de Crase. A precisão factual global permanece não certificada: a amostra é dirigida, pequena e não probabilística.

## Escopo e limites

- Censo dos arquivos JSON de `conteudo/materias/`, incluindo questões antigas e acrescentadas na atualização.
- Leitura manual dos enunciados, de todas as opções e das explicações das 73 questões listadas no registro de cobertura ao final.
- Inspeção adicional dos trechos de teoria e das fontes da matéria de Banco de Dados relacionados ao achado técnico.
- Exclusões: `entrada/`, `recortes/`, `editais/`, `simulados/`, pacotes instalados, renderização no aplicativo e comportamento em produção.
- Não houve consulta externa às fontes. URLs e datas declaradas pelos arquivos não foram certificadas. A auditoria não atesta a vigência de leis, decretos, precedentes, protocolos clínicos ou versões de software de 2026.
- Não foi executado o script de auditoria pela API da Anthropic existente no repositório. A análise usou leitura local e contagens mecânicas.

## Achados e recomendações

### A01 — Alta — Gabarito incorreto por ignorar chaves candidatas alternativas

**Local:** `conteudo/materias/informatica/banco-dados-sql.json`, questão 78, enunciado na linha 2713 e explicação na linha 2728. Numeração corresponde à posição no array `questions`, começando em 1.

**Enunciado:** “Em uma relação com chave primária simples (um único atributo), é possível haver violação da 2FN por dependência parcial.”

**Gabarito registrado:** Errado. A explicação afirma: “Com chave simples, a relação em 1FN já está em 2FN.”

**Problema:** a 2ª Forma Normal considera dependências de atributos não primos em relação a **todas as chaves candidatas**, não apenas à candidata escolhida como primária. Uma primária simples pode coexistir com outra candidata composta. O enunciado pergunta se a violação é possível: é possível, portanto o gabarito geral deve ser **Certo**.

**Contraexemplo verificável:** considere uma relação em 1FN `R(A, B, C, D)` com dependências `A → BCD`, `BC → A` e `B → D`. Suas chaves candidatas mínimas são `{A}` e `{B,C}`; escolha `{A}` como primária. O atributo `D` é não primo e depende de `B`, parte própria da candidata `{B,C}`. Há violação de 2FN, apesar da primária simples. Os fechos calculados confirmam: `A+ = ABCD`, `(BC)+ = ABCD`, `B+ = BD`, `C+ = C`.

**Recomendação:** mudar o gabarito para Certo e explicar o papel de todas as candidatas; alternativamente, restringir expressamente a afirmação às relações cujas chaves candidatas sejam todas simples. Revisar também o aviso do capítulo “2. Modelagem ER, integridade, mapeamento e normalização” que diz que chave de uma coluna impede violação por dependência parcial: a formulação precisa da mesma ressalva.

**Evidência:** definição formal e contraexemplo por dependências funcionais, com fechos conferidos localmente. Não depende de atualização legislativa ou de recurso específico de um SGBD.

### A02 — Retificado — Campos curtos aceitos com comentários nas alternativas

**Extensão:** 674 questões, distribuídas em 24 matérias; aproximadamente 0,41% das questões da biblioteca. Embora o guia mencione 120 caracteres, `supabase/functions/_shared/library.ts` aceita explicação de pelo menos 40 caracteres com comentários em todas as alternativas e pelo menos 180 caracteres somados. Todas as 674 ocorrências atendem a essa exceção. A classificação anterior como descumprimento automático estava incorreta.

Matérias com mais ocorrências:

| Matéria | Explicações abaixo de 120 caracteres |
|---|---:|
| Administração geral — Funções administrativas | 49 |
| Administração geral — Materiais e logística | 48 |
| Informática — Internet, busca e redes sociais | 47 |
| Matemática — Probabilidade | 43 |
| Matemática — Equivalências e negações lógicas | 41 |
| Raciocínio lógico — Problemas aritméticos | 41 |

**Exemplo:** `informatica/internet-busca-redes-sociais.json`, questão 139, linha 5266: “Na comunicação síncrona, a interação é simultânea.” Além desse campo, há comentários nas alternativas, anexados pelo conversor oficial à explicação entregue. A qualidade desses comentários pode merecer revisão, mas sua existência não podia ser ignorada na contagem mínima.

**Recomendação:** revisar as ocorrências individualmente, acrescentando justificativa substantiva quando necessária. Não alongar texto apenas para atingir contagem. Extensão pequena não é prova de gabarito errado; extensão grande tampouco garante qualidade.

O arquivo de dados associado contém as 674 posições para localização precisa.

### A03 — Média — Distratores pouco plausíveis e dificuldade inflada na amostra

**Exemplo 1:** `administracao-geral/administracao-materiais-logistica.json`, questão 56, linha 2004. Pergunta pelo critério da curva ABC e oferece “sua cor”, “sua origem geográfica” e “o nome do fornecedor”. A eliminação dessas respostas exige pouco domínio do assunto. Preferir confusões reais entre valor de consumo, criticidade, giro, quantidade e prazo de reposição, mantendo uma resposta inequívoca.

**Exemplo 2:** `informatica/banco-dados-sql.json`, questão 133, linha 4748. Está classificada como DIFÍCIL, mas pergunta uma finalidade básica de `AFTER UPDATE` e inclui “remover o SGBD” e “criar a tabela” como opções. O gabarito de registrar alterações em auditoria é coerente; os distratores e a dificuldade merecem revisão.

**Recomendação:** estabelecer critérios de dificuldade baseados no raciocínio exigido, e distratores derivados de erros plausíveis. Estes exemplos sustentam revisão dos itens citados; não permitem estimar a incidência do problema em toda a biblioteca.

### A04 — Informativo — Ausência de vínculo a conceitos de erro

**Extensão:** 113.599 questões sem `errorConceptKey` preenchido, em 1.134 matérias — aproximadamente 68,37% das questões.

O guia define conceitos de erro, mas o validador oficial permite `errorConceptKey: null`. Portanto, ausência de vínculo não é falha de contrato. Nenhuma referência preenchida apontou para uma chave inexistente no conjunto `errorConcepts` da própria matéria.

**Recomendação:** revisar o contrato editorial e mapear cada questão ao conceito adequado quando esse vínculo for requerido. A ausência é uma lacuna de metadados; seu impacto em diagnóstico e revisão personalizada depende do fluxo que importa e consome os dados, que não foi auditado aqui. Não atribuir conceitos em massa por posição ou por distribuição artificial.

### A05 — Baixa — Dois enunciados repetidos entre matérias

Foram encontradas duas ocorrências adicionais com texto de enunciado idêntico após normalização de espaços e caixa:

- `direito-ambiental/fundamentos-snuc.json`, questão 92, e `direito-ambiental/saneamento-residuos-poluicao.json`, questão 107: responsabilidade civil ambiental e culpa. Ambos marcam Errado, sem conflito de gabarito identificado na leitura.
- `educacao/diversidade-inclusao-temas-transversais.json`, questão 139, e `informatica/internet-busca-redes-sociais.json`, questão 158: identificação do Marco Civil da Internet. Ambos apontam a Lei nº 12.965/2014, com conjuntos de opções diferentes.

**Recomendação:** decidir se a reutilização é intencional e se a seleção de simulados deve evitar repetições. Isso não prova plágio nem erro factual. Similaridade por paráfrase não foi medida; duas ocorrências literais não representam o total de questões semanticamente parecidas.

## Revalidação da auditoria anterior

Na versão 9 de Crase, com 168 questões:

| Achado anterior | Situação atual |
|---|---|
| Questão 36: “Antes de até” | Corrigido para “Após a preposição até” |
| Questão 48: fragmentos `__quela` e `__quilo` | Corrigido: lacunas compatíveis com expressões completas |
| Questão 50: quatro lacunas e três componentes | Corrigido: intervalo passou a ocupar uma lacuna |
| Questão 51: repetição das locuções | Corrigido |
| Questão 52: repetição dos destinos | Corrigido |
| Questão 55: repetição de `prazo` e `revisar` | Corrigido |

**Pendência mantida:** a questão 54 adota como resposta única `a — à — a` para usos de distância. A convenção e as variantes aceitas precisam ser sustentadas por referência normativa explícita. Sem consulta a essa referência, não classifico o gabarito como errado.

## Censo estrutural

| Verificação | Resultado |
|---|---:|
| Matérias JSON | 1.184 |
| Questões | 166.151 |
| Múltipla escolha, cinco alternativas | 85.834 |
| Certo/Errado, duas alternativas | 80.317 |
| Classificadas como fáceis | 53.922 |
| Classificadas como médias | 57.254 |
| Classificadas como difíceis | 54.975 |
| JSON inválido | 0 |
| Questão sem exatamente uma opção marcada correta | 0 |
| Referência a capítulo inexistente em `section` | 0 |
| Questão com opções de texto idêntico dentro do item | 0 |
| Matéria com enunciado repetido internamente | 0 |
| Questão com explicação menor que 120 caracteres | 674 |

Também não houve ocorrência nos seguintes critérios mecânicos: menos de 40 questões; menos de 15 Certo/Errado ou de 15 questões com cinco alternativas; menos de oito questões de cada dificuldade; quantidade de capítulos fora de 2–6; capítulo com menos de 800 caracteres; teoria somada com menos de 6.000 caracteres; resumo com menos de 400 caracteres; flashcards fora de 12–30; matéria sem fontes; fonte sem URL; data de acesso posterior à data desta auditoria; questão REAL sem URL, banca ou ano.

Nas questões com cinco alternativas, nenhuma matéria teve mais de 40% dos gabaritos em uma letra, nem mais de 40% das respostas **estritamente** mais longas ou mais curtas. Empates de comprimento não foram tratados como pistas. Nenhuma matéria ultrapassou 15% de explicações integralmente idênticas. Frases recorrentes dentro de explicações diferentes não foram medidas.

Esses resultados medem preenchimento e forma. Uma única alternativa marcada não significa uma única alternativa semanticamente correta. Os rótulos de dificuldade não foram validados em larga escala.

## Cobertura da revisão manual

Posições no array `questions`, começando em 1. Total: 73 questões distintas em 14 matérias.

| Arquivo relativo a `conteudo/materias/` | Questões lidas integralmente |
|---|---|
| `portugues/crase.json` | 36, 48, 50, 51, 52, 54, 55 |
| `matematica/probabilidade.json` | 61, 62, 63, 100, 101 |
| `informatica/autenticacao-controle-acesso-sso.json` | 61, 62, 63, 100, 101 |
| `matematica/porcentagem.json` | 56, 67, 78, 89, 100, 111, 122, 133 |
| `matematica/equivalencias-negacoes-logicas.json` | 56, 67, 78, 89, 100, 111, 122, 133 |
| `direito-administrativo/contratacao-direta.json` | 56, 67, 78, 89, 100, 111, 122, 133 |
| `enfermagem/urgencia-emergencia-trauma.json` | 56, 67, 78, 89, 100, 111, 122, 133 |
| `informatica/banco-dados-sql.json` | 56, 67, 78, 89, 100, 111, 122, 133 |
| `direito-constitucional/direitos-individuais-coletivos.json` | 56, 67, 78, 89, 100, 111, 122, 133 |
| `direito-ambiental/fundamentos-snuc.json` | 92 |
| `direito-ambiental/saneamento-residuos-poluicao.json` | 107 |
| `educacao/diversidade-inclusao-temas-transversais.json` | 139 |
| `informatica/internet-busca-redes-sociais.json` | 139, 158 |
| `administracao-geral/administracao-materiais-logistica.json` | 55, 56, 57 |

A amostra foi escolhida para revalidar achados e inspecionar conteúdo acrescentado em diferentes áreas. Não é representativa estatisticamente e não permite calcular taxa global de erro. Leitura de questões jurídicas e clínicas identifica incoerências aparentes, mas não substitui conferência de vigência em fontes primárias.

## Prioridades para correção e continuação

1. Corrigir o gabarito e a ressalva teórica de 2FN em proposta separada, para revisão antes da publicação.
2. Revisar a qualidade das justificativas e dos distratores concretamente citados, considerando também os comentários entregues; não alongar automaticamente os 674 campos curtos aceitos.
3. Confirmar o uso de `errorConceptKey` no fluxo do app e completar os vínculos requeridos.
4. Conferir os temas jurídicos, clínicos e tecnológicos em fontes primárias, por lotes pequenos e com registro da cobertura. Valores e normas de 2026 permanecem pendentes.

**Arquivos entregues:** este parecer e `AUDITORIA-2026-10-21-atualizada-dados.json`, com inventário de matérias, totais e todas as ocorrências do censo. As matérias continuam intactas. Não se atribui nota global nem selo de revisão factual integral com esta cobertura.
