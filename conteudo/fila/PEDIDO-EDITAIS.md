# Pedido: arquivos de EDITAL (catálogo de concursos)

Você vai gerar o **arquivo de edital** de cada concurso da lista: as disciplinas e os tópicos do
conteúdo programático, no formato do catálogo do Estudário. **Não é para gerar matéria** (teoria,
questões): só a estrutura do edital. O Claude confere, liga cada tópico à matéria certa e publica.

## Regras

1. **Fonte oficial.** Use o edital publicado (PDF do órgão ou da banca). Copie o conteúdo
   programático com fidelidade: não invente tópico, não resuma a ponto de perder assunto, não
   acrescente o que o edital não pede. Se não conseguir acessar o edital oficial daquele concurso,
   **não crie o arquivo**: anote no relatório final que ficou pendente e por quê.
2. **Um assunto por tópico.** O erro que mais aconteceu até agora: um item do edital junta vários
   assuntos ("Equações do 1º e 2º graus e sistemas lineares"; "Razão e proporção; regra de três")
   e vira um tópico só. **Separe**: cada assunto que seria uma matéria diferente vira um tópico
   próprio ("Equações do 1º e 2º graus." e "Sistemas lineares."). Assuntos que andam sempre juntos
   e são uma matéria só podem ficar no mesmo tópico ("Ato administrativo: conceito, requisitos,
   atributos, classificação e espécies.").
3. **Texto do tópico.** Use as palavras do edital, sem a numeração ("1.", "1.2", "a)"), começando
   com maiúscula e terminando com ponto. Entre 3 e 400 caracteres. Sem repetir o mesmo tópico na
   mesma disciplina.
4. **Não ligue a matérias.** Todo tópico vai com `"topico": null`. O Claude faz a ligação.
5. **Disciplinas** com o nome que está no edital ("Noções de Direito Administrativo",
   "Raciocínio Lógico-Matemático"). Não junte disciplinas diferentes.
6. **Um arquivo por cargo.** Cargos com conteúdo programático diferente viram arquivos separados.
   Cargos com conteúdo idêntico podem ficar num arquivo só (diga isso em `cargoExibicao`).
7. **Dados do concurso só com certeza.** Banca, ano e número do edital vêm do próprio edital. Se
   não tiver certeza de um dado, deixe de fora (ou explique em `observacao`). Nunca invente número
   de edital, data ou banca.
8. Grave em `conteudo/entrada/edital-<nome-do-arquivo>.json`, **UTF-8 sem BOM**, um arquivo por
   vez, inteiro, só quando estiver pronto. Não grave em `conteudo/editais/`.
9. Depois de gravar cada um, rode o conferidor e **corrija** o que ele apontar:

       deno run --allow-read conteudo/fila/conferir-edital.ts conteudo/entrada/edital-<nome>.json

## Nome do arquivo

`<orgao>-<cargo>-<ano>` em minúsculas, sem acento, palavras separadas por hífen. Exemplos:
`pf-agente-2025`, `pmmg-cfsd-qppm-2025`, `inss-tecnico-2022`. Quando o mesmo concurso tem vários
cargos, o cargo vem depois de dois hífens: `trt3-2022--analista-judiciaria`.

## Formato

```json
{
  "concurso": "Polícia Federal",
  "orgao": "Polícia Federal",
  "cargo": "Agente de Polícia Federal",
  "cargoExibicao": "Agente de Polícia Federal",
  "banca": "Cebraspe",
  "ano": 2025,
  "referencia": "Edital nº 1 · PF · Policial, de 20 de maio de 2025",
  "url": "https://link-oficial-do-edital.pdf",
  "observacao": "Opcional: aviso curto para quem vai estudar (ex.: a prova tem redação).",
  "sinonimos": ["pf", "policia federal", "agente pf", "agente federal"],
  "disciplinas": [
    {
      "nome": "Língua Portuguesa",
      "topicos": [
        { "texto": "Compreensão e interpretação de textos de gêneros variados.", "topico": null },
        { "texto": "Reconhecimento de tipos e gêneros textuais.", "topico": null }
      ]
    }
  ]
}
```

- `concurso`: nome curto pelo qual as pessoas procuram (sigla ou nome popular).
- `cargoExibicao`: o cargo como aparece para o aluno.
- `ano`: número, sem aspas.
- `url`: só o link oficial; se não tiver, apague a linha.
- `sinonimos`: de 4 a 15 termos de busca, em minúsculas e **sem acento** (siglas, apelidos, cargo).

## Relatório final

Ao terminar, liste: os arquivos gravados (com quantas disciplinas e tópicos cada um) e os concursos
da lista que ficaram pendentes, com o motivo (edital não encontrado, cargo ambíguo, etc.).
