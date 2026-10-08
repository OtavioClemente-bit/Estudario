# Ligações tópico → matéria (08/10/2026)

São os 66.435 tópicos de edital que estavam com `"topico": null`. Agentes leram cada um, junto com a disciplina e o cargo, e decidiram qual matéria realmente ensina aquele conteúdo, ou que nenhuma ensina.

**Método**
- **Piloto:** 20 editais, 2.613 tópicos, lidos pelo modelo maior com o catálogo inteiro.
- **Restante:** o Sonnet, com 5 a 30 matérias candidatas por tópico, escolhidas pela área da disciplina mais as parecidas. Ele podia consultar o catálogo completo quando nenhuma candidata servia.
- **Teste:** comparado ao piloto, o Sonnet concordou em cerca de 95% das ligações de confiança alta.
- **Amostra final:** conferi 40 ligações de confiança alta sorteadas, e todas fazem sentido.

**Arquivos**
| Arquivo | Tópicos | Uso |
|---|---|---|
| `ligacoes-alta.json` | 23.279 | Aplicar direto |
| `ligacoes-media.json` | 19.690 | A matéria cobre, mas não é o foco. Aplicar depois de revisar por amostra |
| `topicos-sem-materia.json` | 23.466 | Nenhuma matéria ensina. Serve para planejar matérias novas |
| `ligacoes-piloto-2026-10-08.json` | 2.613 | Piloto com motivo de cada decisão; já está somado aos três arquivos acima |

**Como aplicar** (Claude local, depois de publicar as ondas 6 a 8)
```
node conteudo/pedidos/aplicar-ligacoes.mjs            # só mostra quantos mudariam
node conteudo/pedidos/aplicar-ligacoes.mjs --gravar   # aplica as de confiança alta
node conteudo/pedidos/aplicar-ligacoes.mjs --com-media --gravar   # inclui as de confiança média (depois de revisar)
```

**O que o script faz**
- Só preenche tópicos que ainda estão `null` e cujo texto continua igual ao da proposta.
- Pula as ligações que apontam para matérias ainda não publicadas, como as de `conteudo/entrada/` (1.781 nas de confiança alta). Rode de novo depois de publicar para pegar essas.
- Depois de aplicar, rodar `importar.ts` / `publicar.sh` como de costume.

**Por que alguns tópicos ficaram sem matéria**
- **Tópicos-pacote**, como "crase, regência, concordância, pontuação". O edital aceita uma só matéria por tópico, e nenhuma cobre o tópico inteiro. A solução é permitir uma lista de matérias por tópico, o que exige mudar o formato do edital e o app.
- **Pedaços soltos** ("Seção II.", "Conceito.") sem assunto claro.
- **Normas próprias de órgão** (regimento interno, leis estaduais, RICMS) e atualidades.
- **Cargos de especialista** (engenharia, saúde, Anvisa, professor), além de lacunas reais da biblioteca.
