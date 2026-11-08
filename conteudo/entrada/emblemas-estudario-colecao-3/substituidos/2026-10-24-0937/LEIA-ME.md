# Estudário — Coleção III

**65 emblemas concluídos e conferidos visualmente, com zero pendências sinalizadas.** São 50 conquistas redesenhadas da Coleção II e 15 novas. As 14 correções da última rodada foram concluídas com acabamento em camadas.

## Pintura e acabamento

Cada cena foi gerada individualmente com ImageGen, com direção de pose, expressão e composição, usando png/plano-200.png da Coleção II como referência. A aparência combina nanquim, guache e lápis de cor. São ilustrações digitais geradas e editadas, sem atribuição de desenho físico a um ilustrador humano.

O dono autorizou acabamento por recortes, máscaras e sobreposições. Os reparos preservam pixels pintados: as curvas dos braços reutilizam a própria textura da cena, as capas escondem as raízes, e cartas pintadas criam sobreposições naturais quando necessário. Braços gráficos de teste e estudos frontais rejeitados não integram os PNGs finais.

## Auréolas

Cada faixa usa literalmente um único PNG de moldura, de molduras/tier-1.png a tier-7.png. A cena ocupa a janela interna e a moldura fixa é aplicada por cima. O relatório de montagem compara todos os pixels opacos da moldura.

| Tier | Faixa | Evolução |
|---|---|---|
| 1 | Bronze | Cobre com pátina, aro simples |
| 2 | Prata | Gravuras, reflexo frio e ramos inferiores |
| 3 | Ouro | Duplo aro âmbar e louros completos |
| 4 | Safira | Prata, camada azul e pedras lapidadas |
| 5 | Rubi | Carmim intenso, platina e joias vermelhas |
| 6 | Esmeralda | Ouro, verde e filigrana mais rica |
| 7 | Esmeralda anual | Halo radiante e quatro estações |

Fitas vazias, sem textos ou números nas artes. Cruz e check são os pictogramas pedidos para as cenas de erros. Legendas das pranchas ficam fora dos emblemas.

## Folha e anatomia

Livro de páginas creme, capa índigo, capelo e marcador menta. Dois braços índigo e mãos creme. As raízes ficam escondidas atrás dos contornos externos da capa. Sem pernas, pés, meias, sapatos ou torso humano.

Nas vistas frontais, o rosto ocupa as páginas internas e há um sulco central. Nas vistas traseiras, o rosto fica oculto. Em maratona-480, Folha descansa de costas, fechando as capas e guardando o marcador; a face indevida na espessura de papel foi removida. Essa solução preserva a pose traseira e evita repetir um retrato frontal.

A revisão visual verificou encaixes dos braços, mãos, membros extras, geometria do livro e coerência da cena em resolução original. A conferência é do assistente; a avaliação artística final continua sendo do dono.

## Entrega

- png/: 65 PNGs de 1254 × 1254 com transparência externa.
- app-512/: 65 PNGs de 512 × 512, reduzidos proporcionalmente sem cortes.
- manifesto.json: os campos solicitados, metadados Android e conferência por ID; complete: true.
- prompts.json: os 65 pedidos individuais e o registro do acabamento da pose traseira.
- molduras/: os sete arquivos fixos de faixa.
- galeria.html e dados.js: galeria local com busca, filtros, detalhe e download dos dois tamanhos.
- prancha-completa.png: os 65 emblemas e sete referências preservadas da Coleção II.
- evolucao-aureolas.png: comparação das sete faixas.
- revisao-1.png a revisao-7.png: pranchas menores para inspecionar a coleção.
- PENDENCIAS.md e pendencias.json: revisão encerrada, zero itens pendentes.
- galeria-final.png: captura da galeria revisada; galeria-verificacao.json registra os filtros e o detalhe conferidos no navegador.
- verificacao.json, verificacao-final.json e producao-final/camadas-verificacao.json: conferência de dimensões, transparência, metadados, hashes e reutilização literal das molduras.

## Registro dos reparos

producao-final/acabamento-pendente/camadas.json contém as 14 receitas finais de recorte, máscara e transformação. O nome dessa pasta é histórico; todos esses reparos foram aplicados e conferidos. Os originais de trabalho ficam em originais/ e os candidatos aceitos em png/. tentativas-nao-aprovadas.json documenta estudos descartados.

producao-final/retoques-camadas.json registra os retoques anteriores. As pastas correcoes-2 a correcoes-5 guardam pedidos e resultados históricos, incluindo tentativas rejeitadas e a limitação do gerador naquele momento. Não são uma fila atual. producao-final/status.json é o estado vigente por ID.

## Reprodução

montar-camadas.mjs monta as cenas de producao-final/png/ com as sete molduras. montar-pacote.mjs exporta miniaturas, metadados e pranchas; apresentar-faixas.mjs monta a comparação dos ranks. Não geram cenas. producao-final/acabamento-pendente/processar.mjs reaplica os 14 acabamentos a partir dos backups imutáveis, sem acúmulo de alterações; a instalação dos candidatos na produção é explícita após inspeção.

As 50 conquistas anteriores preservam IDs e requisitos. fenix-1 usa target 1 e unidade resgates; Madrugador e Coruja usam target 10 e unidade dias. androidResource usa emblema_ e troca os hífens por sublinhados. O tier 7 anual permanece distinto do tier 6.

Os 125 arquivos da Coleção II são conferidos por SHA-256 na exportação. Toda alteração deste trabalho está em conteudo/entrada/emblemas-estudario-colecao-3/. A aplicação e o conteúdo publicado não foram alterados. Não houve commit nem push.
