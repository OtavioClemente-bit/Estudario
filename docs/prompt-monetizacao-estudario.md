# Prompt de produto — anúncios, planos e limites do Estudário

Quero que você atue como consultor de produto, monetização e custos para o aplicativo Estudário, um app Android de estudos para concursos que gera edital, plano de estudos, conteúdo por matéria/tópico e questões com IA.

## Contexto e objetivo

O Estudário está em teste fechado. Quero testar adesão antes de assumir custos fixos ou prometer uso ilimitado. Minha prioridade é manter o custo operacional próximo de zero enquanto descubro se as pessoas realmente usam o produto. Hoje existe geração por IA do Estudário e também uma opção de compartilhar o prompt com a IA preferida do usuário. Quero avaliar anúncios e, futuramente, planos Grátis, Essencial e Pro com cobrança mensal/anual.

Faça uma proposta prática, simples de explicar no app e sustentável. Não trate hipótese como dado confirmado. Separe fatos, estimativas e decisões que ainda precisam de validação.

## Custos e premissas já levantados

- O backend usa GPT-6 Luna. A tabela pública consultada informa US$ 0,10 por milhão de tokens de entrada e US$ 0,50 por milhão de tokens de saída no processamento padrão de contexto curto. Verifique os preços atuais antes de fechar qualquer cálculo e cite a fonte oficial.
- Uma amostra de geração de conteúdo observada tinha aproximadamente 32.073 tokens de entrada e 9.778 tokens de saída: estimativa de cerca de US$ 0,0081, aproximadamente R$ 0,04 pela cotação de referência usada na conversa. A amostra é pequena e não representa pior caso.
- Com essa média, 200 gerações de conteúdo em um mês dariam aproximadamente US$ 1,62 (cerca de R$ 8,45 pela cotação de referência). Apresente como cenário ilustrativo, não como garantia de custo.
- Uma amostra de criação de edital ficou perto de R$ 0,035 por execução; também há poucos dados.
- Geração de questões pode ter custo maior conforme a quantidade solicitada, contexto antirrepetição enviado e tamanho das respostas. O prompt atual de questões extras permite solicitar de 5 a 30 questões. Ainda não temos uma medição de custo confiável por lote de 10, 20 e 30.
- Não confundir quantidade de questões respondidas no banco local com quantidade de chamadas pagas à IA.
- Infraestrutura Supabase está no Free durante a validação. Considere risco de pausa/limites do plano gratuito e explique o gatilho objetivo que justificaria migrar, sem incluir custo fixo enquanto não for necessário.
- Confirme câmbio atual apenas se for necessário expressar estimativas em reais; informe a data e que a conversão varia.

## Direção desejada para os planos

Considere como hipótese inicial, sujeita a crítica:

- **Grátis:** 1 edital total e 1 plano total, sem renovação mensal; mais restrito em gerações mensais de conteúdo; anúncios opcionais para ganhar pequenas cotas adicionais.
- **Essencial:** mais editais e planos que o Grátis, renovados mensalmente; cota intermediária de conteúdo e de geração de questões extras.
- **Pro:** mais editais e planos que o Essencial, renovados mensalmente; maior cota de conteúdo e questões extras. Avaliar 200 gerações mensais de conteúdo como teto, lembrando a estimativa ilustrativa de aproximadamente R$ 8,45 de API por usuário que consumir as 200 na média observada.
- Edital e plano podem ter cotas mensais, ainda que normalmente não precisem ser recriados todo mês. Avalie se faz mais sentido cota acumulada, cota mensal ou saldo que não expira.
- Mostrar claramente o consumo e saldo restante, por exemplo: “Restam 3 de 10 gerações neste mês”, com data de renovação e explicação do que consome uma unidade.
- Geração que falhar antes de entregar resultado válido não deve consumir cota. Defina tratamento de cancelamento, repetição por erro técnico, idempotência e tentativa duplicada.
- Todo limite que protege gasto deve ser aplicado e validado no backend, nunca somente na interface Android. Não colocar chave de API ou segredo no app.

Proponha uma primeira tabela com limites numéricos para Grátis, Essencial e Pro, incluindo:
1. editais;
2. planos de estudo;
3. gerações de conteúdo por mês;
4. lotes de questões extras por mês;
5. quantidade máxima de questões por lote e por tópico;
6. eventual crédito bônus por anúncio.

Use como ponto de partida para debate: Grátis com 1 edital e 1 plano; Essencial e Pro com cotas maiores; conteúdo em faixas crescentes; Essencial com até 20 questões por lote e Pro com até 30 por lote. Você pode recomendar outros números se justificar usando custo, utilidade e experiência.

## Questões extras

Analise o fluxo atual do prompt: geração somente de questões, por tópico escolhido, com questões existentes da matéria incluídas para evitar repetição, quantidade explícita e dificuldade/formato configuráveis. Explique:

- se a cobrança/limite deve ser por lote, por questão, por tópico ou por chamadas mensais;
- por que 30 questões por tópico no Pro pode ou não ser bom;
- como limitar contexto repetido e saída para impedir que o custo cresça sem controle;
- uma política recomendada para Grátis, Essencial e Pro;
- como medir custo real por lote de 10, 20 e 30 antes de prometer limites.

Não invente custo unitário para questões sem dados. Se possível, dê a fórmula e os dados de telemetria que precisam ser coletados: modelo, tokens de entrada, tokens de saída, tipo da operação, quantidade solicitada, quantidade entregue, status, retries e custo estimado.

## Anúncios

Avalie anúncio recompensado opcional (rewarded ad), e não anúncios obrigatórios ou que interrompam estudo. Considere:

- oferecer pequena quantidade extra de geração após conclusão do anúncio;
- não recompensar antes de confirmação de conclusão;
- verificação segura no servidor por callback/Server-Side Verification (SSV), identificador de transação único e proteção contra replay/fraude;
- limite diário e mensal de recompensas;
- não conceder prêmio em caso de falha ou cancelamento da geração;
- separar “assistir anúncio” da garantia de que o modelo produzirá conteúdo válido;
- estimar ponto de equilíbrio apenas com eCPM real observado, país, preenchimento e taxa de conclusão; nunca presumir que um anúncio paga uma geração.

Diga se vale implementar anúncios já no teste fechado ou somente instrumentar interesse e medir uso primeiro. O objetivo do começo continua sendo não pagar do próprio bolso para usuários consumirem sem limite.

## Preços e cobrança

Proponha faixas iniciais de preço mensal e anual em reais para Essencial e Pro, mas deixe explícito que são hipóteses de pesquisa, não preços validados. Leve em conta:

- custo variável de IA, margem para usuários intensivos, taxa da loja, impostos e eventuais custos de infraestrutura;
- desconto anual sem tornar o plano inviável;
- evitar plano ilimitado;
- opção de manter apenas teste gratuito no fechado e ativar cobrança depois de medir retenção, conversão e custo;
- Google Play Billing para assinaturas Android, se aplicável, e implicações da taxa atual da loja devem ser verificadas em fonte oficial vigente.

Se os dados disponíveis forem insuficientes para definir preço, apresente uma faixa e o experimento que permitiria decidir, em vez de declarar um preço como definitivo.

## IA do Estudário versus IA preferida

Recomende manter ou remover “Compartilhar com minha IA preferida”. Compare:

- experiência guiada e previsível dentro do Estudário, cujo custo de API é do app;
- geração externa, com custo normalmente pago pelo usuário ao serviço escolhido, qualidade e importação menos previsíveis;
- clareza de interface, suporte e taxa de importação bem-sucedida.

Minha inclinação atual é manter as duas durante o teste: “Gerar no Estudário” como caminho principal e “Usar minha IA” como alternativa claramente identificada, sujeita aos limites e à medição de uso. Diga quais eventos medir para decidir depois.

## Entrega esperada

Responda em português brasileiro, linguagem direta e sem jargão desnecessário, nesta estrutura:

1. **Recomendação executiva** em poucas linhas.
2. **Tabela dos planos** com limites completos e observações de custo.
3. **Análise das questões extras** com política proposta e plano de medição.
4. **Anúncios**: quando implementar, quanto creditar e como proteger.
5. **Preços sugeridos** mensal/anual como faixas de teste, com premissas.
6. **IA do Estudário e IA preferida**: recomendação e métricas.
7. **Plano de validação no teste fechado** por etapas, incluindo eventos/indicadores e limites de gasto.
8. **Riscos e decisões que ainda dependem de dados**, sem esconder incerteza.

Inclua fontes atuais e confiáveis para preços de API, Google Play Billing/taxas e anúncios recompensados/SSV. Não afirme que a receita publicitária vai cobrir o custo sem dados. Não implemente nada no código neste momento; entregue uma proposta para discussão e aprovação.
