# Correção das pausas na navegação web

Causa comprovada: Forecast.of executava PlanProjection por até três anos na thread do DOM. Um caso de 1.000 tópicos bloqueou 4.695 ms; o atraso de 60 ms na Home apenas adiava o bloqueio.

1. Regressão: executar o motor em worker e verificar que o heartbeat da interface continua durante a projeção. Baseline falhou porque a entrada só suportava DOM.
2. Mover a previsão para worker reutilizando o motor Kotlin; transmitir apenas as tabelas necessárias e um resumo serializável da resposta. Preservar regras do Grátis.
3. Compartilhar a previsão por plano, tabelas relevantes, data/fuso e regras; cancelar projeção obsoleta. Home e ambas as visões do plano exibem cálculo pendente sem executar o motor na composição.
4. Verificar payload/cache com jsNodeTest, worker com carga grande, navegação real e falha do worker. Publicar somente após validação. Não fazer commit/push.
