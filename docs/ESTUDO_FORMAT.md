# Formato `.estudo` — versão 2

## Objetivo

O `.estudo` v2 é um JSON UTF-8 capaz de transportar, em um único arquivo:

- concurso;
- edital completo, com matérias ordenadas;
- tópicos e subtópicos em qualquer profundidade;
- descrição e prioridade de cada item;
- teoria completa em formato de livro, dividida em capítulos;
- um ou vários resumos Markdown por tópico;
- questões com alternativas variáveis, resposta e explicação.

A versão 1 continua aceita para pacotes antigos de um único tópico. Para novos arquivos, use sempre a versão 2.

## Estratégia recomendada

Para editais grandes, não peça tudo à IA em uma única resposta. O fluxo mais confiável é:

1. **Pacote de estrutura:** envie o conteúdo programático completo e gere matérias/tópicos/subtópicos, deixando `teorias`, `resumos` e `questoes` vazios. Ao importar, o edital inteiro aparece imediatamente.
2. **Pacotes de conteúdo:** gere um arquivo menor para cada tópico, repetindo exatamente a hierarquia e preenchendo livro, resumo e questões.
3. **Importação incremental:** IDs estáveis localizam concurso, matéria e o subtópico folha exato; nomes são apenas fallback para arquivos antigos. Conteúdo é deduplicado por ID, `sourceId` e hash normalizado do enunciado.

Isso evita respostas truncadas, melhora a qualidade das questões e permite corrigir uma matéria sem recriar o edital.

## Estrutura raiz

```json
{
  "version": 2,
  "packageId": "edital-trt-ti-2026-v1",
  "concurso": {
    "id": "concurso-trt-ti-2026",
    "nome": "TRT — Tecnologia da Informação",
    "principal": true
  },
  "padroesQuestao": {
    "banca": "CEBRASPE",
    "orgao": "TRT",
    "ano": 2026,
    "dificuldade": "MEDIA",
    "origem": "Material autoral para revisão"
  },
  "materias": []
}
```

| Campo | Tipo | Obrigatório | Observação |
|---|---|---:|---|
| `version` | inteiro | sim | Deve ser `2` |
| `packageId` | string | sim | Identidade estável deste pacote |
| `concurso` | objeto | sim | Contém `nome` e `principal` opcional |
| `padroesQuestao` | objeto | não | Valores herdados por todas as questões |
| `materias` | array | sim | Ao menos uma matéria |

Os valores aceitos para `dificuldade` são `FACIL`, `MEDIA` e `DIFICIL`.

## Matéria

```json
{
  "id": "seguranca-informacao",
  "nome": "Segurança da Informação",
  "ordem": 0,
  "topicos": []
}
```

`id`, `nome` e `topicos` são obrigatórios. Toda matéria precisa de pelo menos um tópico. `ordem` é opcional e começa em zero.

## Tópico e subtópico

Tópicos e subtópicos têm exatamente o mesmo formato. `subtopicos` pode conter novos objetos recursivamente.

```json
{
  "id": "seguranca-controle-acesso",
  "titulo": "Controle de acesso",
  "descricao": "Identificação, autenticação, autorização e modelos de controle.",
  "ordem": 0,
  "prioridade": "ALTA",
  "contentOriginType": "EDITAL",
  "observacoes": "Item expresso no edital.",
  "teorias": [],
  "resumos": [],
  "questoes": [],
  "subtopicos": [
    {
      "id": "seguranca-controle-acesso-rbac",
      "titulo": "RBAC",
      "descricao": "Controle baseado em papéis.",
      "ordem": 0,
      "prioridade": "NORMAL",
      "observacoes": "",
      "teorias": [],
      "resumos": [],
      "questoes": [],
      "subtopicos": []
    }
  ]
}
```

| Campo | Obrigatório | Regra |
|---|---:|---|
| `id` | sim | Único entre todos os tópicos/subtópicos do pacote |
| `titulo` | sim | Use o nome exato e consistente entre pacotes |
| `descricao` | não | Escopo ou trecho explicativo do edital |
| `ordem` | não | Inteiro, preferencialmente iniciando em zero |
| `prioridade` | não | `BAIXA`, `NORMAL` ou `ALTA`; padrão `NORMAL` |
| `contentOriginType` | não | `EDITAL`, `DIDACTIC_SUBDIVISION` ou `AUXILIARY_CONTENT`; padrão seguro `EDITAL` |
| `observacoes` | não | Nota importada sem alterar o progresso |
| `teorias` | não | Livros completos; padrão vazio |
| `resumos` | não | Lista; padrão vazio |
| `questoes` | não | Lista; padrão vazio |
| `subtopicos` | não | Lista recursiva; padrão vazio |

Importar o edital não marca itens como estudados. Se um tópico já existe, descrição, ordem e prioridade são atualizadas, mas status, datas e progresso do usuário são preservados.

## Teoria completa (livro)

```json
{
  "id": "teoria-seguranca-rbac-v1",
  "titulo": "Teoria completa — RBAC",
  "capitulos": [
    {
      "id": "cap-01",
      "titulo": "1. Fundamentos",
      "markdown": "Texto longo, didático e autossuficiente em Markdown..."
    },
    {
      "id": "cap-02",
      "titulo": "2. Aplicações e pegadinhas",
      "markdown": "Desenvolvimento, exemplos e revisão do capítulo..."
    }
  ]
}
```

- `id`, `titulo` e ao menos um capítulo são obrigatórios;
- cada capítulo exige `id`, `titulo` e `markdown` não vazio;
- o app une os capítulos num livro contínuo e salva a posição de leitura;
- cada bloco/parágrafo pode ser marcado e receber uma observação pessoal;
- alternativamente, uma teoria pode trazer `markdown` diretamente no lugar de `capitulos`.

## Resumo

```json
{
  "id": "resumo-seguranca-rbac-v1",
  "titulo": "RBAC — revisão",
  "markdown": "# RBAC\n\nPermissões são associadas a **papéis**..."
}
```

- `id` deve ser único entre todos os resumos do pacote.
- `markdown` não pode ser vazio.
- Use títulos, listas, negrito, citações, tabelas simples e código quando necessário.
- Um tópico pode conter vários resumos de revisão. A teoria longa deve ficar em `teorias`, não em `resumos`.

## Questão

```json
{
  "id": "q-seguranca-rbac-001",
  "questionSourceType": "AUTHORIAL",
  "sourceId": null,
  "sourceUrl": null,
  "banca": "CEBRASPE",
  "orgao": "TRT",
  "ano": 2026,
  "dificuldade": "MEDIA",
  "origem": "Material autoral",
  "enunciado": "No RBAC, as permissões são normalmente associadas a:",
  "alternativas": [
    { "chave": "A", "texto": "Papéis", "correta": true },
    { "chave": "B", "texto": "Endereços IP", "correta": false }
  ],
  "explicacao": "No RBAC, usuários recebem papéis e os papéis concentram permissões.",
  "observacao": "Diferenciar RBAC de ABAC.",
  "tags": ["RBAC", "controle de acesso"]
}
```

Campos `banca`, `orgao`, `ano`, `dificuldade` e `origem` podem ser omitidos quando definidos em `padroesQuestao`. Um valor informado na questão substitui o padrão.

Regras:

- `id`, `enunciado`, `alternativas` e `explicacao` são obrigatórios;
- mínimo de duas alternativas;
- chaves não podem se repetir na questão;
- exatamente uma alternativa deve ter `correta: true`;
- múltipla escolha A–E e Certo/Errado são aceitos;
- IDs de questões devem ser únicos no pacote.

## Mesclagem e duplicidade

- Concurso: localizado pelo `nome`.
- Matéria: localizada pelo `nome` dentro do concurso.
- Tópico: localizado pelo `titulo` e pelo mesmo tópico pai.
- Teoria, resumo, item de memorização, questão e conceito de erro: identidade externa `packageId:id`.
- Reimportar o mesmo arquivo é seguro: escolha **Atualizar** para substituir apenas o conteúdo mantendo progresso, favoritos e histórico; **Criar cópia** mantém as duas versões; **Cancelar** não altera nada.
- Para acrescentar conteúdo ao mesmo pacote, mantenha o `packageId`, acrescente IDs novos e escolha Atualizar.

## Validações

Antes de mostrar a prévia, o app verifica:

- JSON e versão;
- campos obrigatórios;
- ao menos uma matéria e um tópico por matéria;
- IDs únicos de matérias, tópicos, teorias, resumos, itens de memorização, conceitos e questões;
- prioridade/dificuldade válidas;
- Markdown não vazio;
- alternativas completas, com chaves únicas e exatamente uma correta.

A importação ocorre em uma transação: um erro impede salvamento parcial.

## Arquivos de exemplo

- `examples/edital-completo-v2.estudo`: estrutura de um edital com subtópicos;
- `examples/conteudo-controle-acesso-v2.estudo`: livro, resumo e exercícios que se encaixam nessa estrutura;
- `examples/seguranca-criptografia.estudo`: exemplo legado v1, ainda compatível.
- `examples/conteudo-hash-v2-completo.estudo`: modelo simples V2 com todas as camadas de aprendizagem.

## Prompts prontos

Consulte `docs/PROMPT_GERAR_ESTUDO.md` para os prompts de edital e conteúdo. O aplicativo também oferece em **Mais › Gerar estudo com IA** um terceiro modelo editável para montar arquivos `.plano`, com fases, metas e tarefas no formato aceito pelo planejador.
# Formato simples V2

Para gerar um único tópico, use a raiz simples com `version`, `packageId`, `competition`, `subject`, `topic`, `summary`, `quickReview`, `tips`, `traps`, `activeRecall`, `questions`, `errorConcepts` e `tags`. Teorias podem ser enviadas em `teorias` ou `theories`.

O formato hierárquico V2 existente (`concurso`, `materias`, `topicos`, `subtopicos`) e o V1 continuam aceitos. Em uma reimportação com o mesmo `packageId`, o app oferece atualizar o conteúdo preservando progresso/favoritos/histórico ou criar uma cópia com IDs independentes.

## Nome do formato

O campo `format` aceita `estudario-estudo` (atual), `meu-concurso-estudo` (nome antigo do app) e `estudo`. Arquivos sem o campo `format` continuam sendo reconhecidos pela presença de `materias` ou `packageId`.

## Vínculo da questão com o material (`secao`)

Cada questão aceita um campo opcional `secao` (também aceito como `reviewAnchor` ou `ancora`) com o
título exato de um capítulo da teoria ou de uma seção do resumo do mesmo pacote.

Quando a pessoa erra a questão no app, a revisão abre direto nesse trecho em vez de mostrar o
material inteiro. Sem o campo, o app cai num plano B: compara as palavras do enunciado com os
títulos e o corpo das seções e abre a mais próxima. O vínculo explícito é sempre melhor — o plano B
existe para o material gerado antes deste campo.

## Campos de rastreio da questão

| Campo | O que é |
|---|---|
| `secao` | Título exato do capítulo/seção do mesmo pacote que responde a questão. Ao errar, a revisão abre nesse trecho. |
| `conceitoErro` | Id de um item de `errorConcepts` do mesmo pacote. Ao errar, o caderno de erros liga o erro a esse conceito em vez de adivinhar pelo título do tópico. |
| `questionSourceType` | `REAL`, `REAL_ADAPTED` ou `AUTHORIAL`. Questão marcada como real **sem `sourceId` nem `sourceUrl` é rebaixada para autoral na importação**, perdendo banca/órgão/ano — procedência que não dá para conferir não é exibida. |

Questão `AUTHORIAL` **não herda** banca, órgão nem ano do bloco `padroesQuestao`: herdar faria uma
questão inventada aparecer com a cara de uma prova que existiu.

## Recorte do tópico (`escopo`)

Cada tópico aceita um objeto opcional `escopo` com `cobre` e `naoCobre`, derivados do **texto do
item do edital** — não do que é interessante sobre o assunto.

```json
"escopo": { "cobre": "conceitos, tipos e finalidade", "naoCobre": "implementação e algoritmos" }
```

O app mostra esse recorte dentro do tópico para a pessoa conferir contra o edital dela. É a defesa
contra o caso mais caro: estudar quarenta páginas de um assunto que o edital pediu em uma linha.
