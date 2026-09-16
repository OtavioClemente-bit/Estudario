# Algoritmo de estudo V2

O aplicativo permanece local-first. As recomendações são calculadas no aparelho com os dados de teoria, questões, erros, revisões e sessões.

## Revisão espaçada

Ao marcar um tópico como estudado, o app cria revisões usando o ciclo configurado. O padrão é D+1, D+7 e D+30; o modo intensivo usa D+1, D+3, D+7, D+14 e D+30.

O estado não é salvo como um rótulo redundante. Ele é derivado da data: futura, disponível na janela de 24 horas, atrasada depois dessa janela, concluída ou ignorada. A revisão guiada passa por recuperação ativa, revisão rápida, até três questões e dificuldade percebida.

Depois da avaliação, a próxima revisão é adaptada: **Difícil** retorna no dia seguinte; **Normal** mantém a agenda; **Fácil** amplia em 35% o intervalo restante. O evento e os resultados continuam registrados para estatísticas.

## Rotação diária e fila

A fila só avança quando um bloco é concluído. “Não consegui estudar” registra um adiamento e o motivo, mas preserva a posição. Pausas, retomadas, conclusões e adiamentos formam um histórico local.

## Sequência

Qualquer tentativa de questão, revisão concluída ou bloco concluído conta como atividade. A sequência atual aceita atividade hoje ou ontem, evitando zerar visualmente o progresso antes de o dia terminar.
