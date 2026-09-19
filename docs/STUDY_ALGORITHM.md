# Algoritmo de estudo V2

O aplicativo permanece local-first. As recomendações são calculadas no aparelho com os dados de teoria, questões, erros, revisões e sessões.

## Revisão espaçada

Ao marcar um tópico como estudado, o app cria revisões usando o ciclo configurado. O padrão é D+1, D+7 e D+30; o modo intensivo usa D+1, D+3, D+7, D+14 e D+30.

O estado não é salvo como um rótulo redundante. Ele é derivado da data: futura, disponível na janela de 24 horas, atrasada depois dessa janela, concluída ou ignorada. A revisão guiada passa por recuperação ativa, revisão rápida, até três questões e dificuldade percebida.

Depois da avaliação, a próxima revisão é adaptada: **Difícil** retorna no dia seguinte; **Normal** mantém a agenda; **Fácil** amplia em 35% o intervalo restante. O evento e os resultados continuam registrados para estatísticas.

### Revisão perpétua

O ciclo não termina no último estágio. Ao concluir a última revisão agendada, o app cria a próxima com intervalo dobrado — 60, 120 e 240 dias, que é o teto. Esse intervalo também responde à dificuldade percebida (difícil pela metade, fácil 35% maior) e ao aproveitamento das questões da própria revisão: errar mais da metade vale como difícil mesmo que a pessoa marque normal. Sem isso, um tópico estudado saía do ciclo para sempre depois do D+30.

As três questões da revisão guiada não são sorteadas às cegas: vêm primeiro as que a pessoa já errou naquele tópico (as com mais erros na frente), depois as que ela nunca respondeu e só então o resto.

## Escada de reencontro com o erro

Toda questão errada é agendada para voltar sozinha: 3 dias depois do erro, 10 dias no primeiro acerto, 30 no acerto seguinte. No terceiro acerto consecutivo a questão sai da escada; errar em qualquer ponto zera e volta para 3 dias. O caderno de erros mostra a data de retorno, o modo "Treinar meus erros" ordena pelas que já venceram e o Início troca o contador de erros pendentes pelo número de erros que voltam hoje.

## Rotação diária e fila

A fila só avança quando um bloco é concluído. “Não consegui estudar” registra um adiamento e o motivo, mas preserva a posição. Pausas, retomadas, conclusões e adiamentos formam um histórico local.

## Sequência

Qualquer tentativa de questão, revisão concluída ou bloco concluído conta como atividade. A sequência atual aceita atividade hoje ou ontem, evitando zerar visualmente o progresso antes de o dia terminar.

## Modo foco

Sessão de estudo cronometrada com o Não Perturbe do sistema ligado, sem ciclo forçado e sem alarme.
O tempo medido entra no histórico e na sequência; o XP continua vindo das atividades concluídas, não
do relógio correndo. Detalhes e decisões de segurança em [MODO_FOCO.md](MODO_FOCO.md).
