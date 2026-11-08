# Caderno de conquistas — Estudário

Abra **galeria.html** no navegador para explorar os 50 emblemas. Funciona localmente, sem conta, instalação ou conexão à internet. A galeria permite buscar, filtrar as 11 categorias, alternar temas e versões bloqueadas, ampliar a arte e percorrer os níveis de uma família.

- `svg/`: 50 emblemas vetoriais com fundo transparente.
- `png/`: as mesmas 50 artes em PNG transparente de 512 × 512 px.
- `bloqueados/svg/` e `bloqueados/png/`: 50 versões de cada formato, com paleta neutra e cadeado. A silhueta completa é preservada para antecipar a recompensa.
- `apresentacao.png`: seleção de seis peças em fundo claro e escuro.
- `colecao-completa.png`: prancha com os 50 emblemas, nomes, IDs e faixas.
- `manifesto.json`: correspondência exata entre o catálogo e os arquivos, incluindo nomes de recursos Android.
- `colecao.mjs`: fonte das composições; importa os personagens oficiais de `design/turma/turma.js`.
- `galeria.template.html`: fonte da galeria, com as artes incorporadas durante a exportação.
- `DIRECAO.md`: direção visual e auditoria da implementação anterior.
- `verificacao.txt`: resultado das conferências de cobertura, dimensões e transparência.

## Como foram construídos

São desenhos vetoriais compostos com caminhos SVG: molduras próprias por categoria, fitas, folhas, asas, coroas, objetos e cenas por conquista. Os personagens centrais aproveitam a fonte oficial da Turma do Folha, preservando exatamente a linguagem da marca. Não houve geração de imagens por ImageGen nem uso de imagens de banco. “Artesanal” descreve a direção estética digital, e não uma alegação de desenho físico feito por uma pessoa.

Os 50 nomes, requisitos, IDs e níveis são extraídos do catálogo real, sem mudar as regras de conquista. O nível 7 de `seq-365` é preservado e recebe uma composição de encerramento exclusiva. Segue com a faixa Esmeralda, como o nome mostrado pela interface atual.

## Integração no site e no Android

Este pacote contém os ativos; o app e o site em produção ainda usam as artes anteriores. Nenhum arquivo fora de `conteudo/entrada/` foi alterado por este trabalho.

No site, usar o **ID completo** do emblema para selecionar `svg/<id>.svg`, ou a versão bloqueada. A função atual `folhaMedal` recebe apenas categoria e nível; a chamada deve passar a usar o ID de `Badge`. Conferir a grade de conquistas, detalhe ampliado, escada de níveis e comemoração. A animação da forja desenha a medalha antiga diretamente em canvas e também exige adaptação para mostrar esta arte ao revelar a recompensa.

No Android, os PNG podem ser copiados para `res/drawable-nodpi/`, usando os nomes `androidResource` do manifesto; por exemplo, `emblema_seq_365.png`. Renderizar pelo ID de `Badge`, preservando proporção e sem aplicar tint. A versão bloqueada é fornecida pronta. Conferir lista, detalhe ampliado, escada e comemorações. As artes têm área de respiro incorporada: não adicionar grandes margens internas ao componente.

Não derivar o arquivo apenas pela cor ou limitar o nível a seis: a conquista anual usa nível 7 e possui uma composição diferente de `seq-100`.

SVG é o formato recomendado para o navegador. PNG de 512 px atende à lista e ao detalhe atual do Android; manter o SVG como fonte para novos tamanhos.

## Evidências da revisão

Em 23/10/2026, os hashes Git dos arquivos locais coincidiram com os retornados pelo conector do GitHub em `main`:

| Arquivo | SHA do blob |
| --- | --- |
| BadgeCatalog.kt | `338dd768e65a263ae6be05881e183d7d803c42f6` |
| MedalArt.kt | `ee284d992827efe942603007c249276e61c2b856` |
| design/turma/turma.js | `84bc8831c18624666b446794c274e7639c443529` |

As pranchas foram renderizadas e inspecionadas. A galeria foi conferida no navegador em largura estreita, nos temas claro e escuro, com detalhe, bloqueados, busca e os 11 filtros. Os SVG de toda a coleção foram exportados em PNG e conferidos com canal alfa e transparência. A página privada de conquistas não foi acessada por login: sua implementação foi conferida pelo código do site e do Android.

Para regenerar neste checkout: executar `node conteudo/entrada/emblemas-estudario-2026-10-23/colecao.mjs` na raiz. O arquivo usa o Sharp já disponível no runtime local do Codex, sem instalar dependências no projeto. Para transferir a fonte a outro computador, ajustar a localização desse pacote na chamada `require`.
