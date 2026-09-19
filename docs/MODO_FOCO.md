# Modo foco

Uma sessão de estudo cronometrada com o Não Perturbe do Android ligado. **Não é pomodoro**: não há
ciclo de 25/5, alarme no meio do estudo nem pausa obrigatória. O relógio só mede; quem decide quando
parar é a pessoa.

## Por que existe

Duas coisas que o app não tinha:

- o tempo de estudo era estimativa do plano, nunca tempo medido;
- não havia nada que segurasse a interrupção do celular durante o estudo.

## Como funciona

1. A pessoa começa pela tarefa do dia (botão **Modo foco** no cartão da tarefa), por um tópico
   (**Estudar com modo foco**) ou por uma sessão livre (**Mais › Modo foco**), útil para estudar no
   livro ou em videoaula.
2. O app liga o Não Perturbe, mantém a tela acesa enquanto a tela do foco estiver aberta, cala os
   próprios lembretes e mostra uma notificação fixa com o cronômetro e o botão **Encerrar**.
3. Ao encerrar, o Não Perturbe volta exatamente ao estado anterior e o tempo medido é registrado.

Sessão presa a uma tarefa do plano não vira sessão de estudo avulsa: quem registra o tempo dela é a
conclusão da tarefa, que abre sozinha, já preenchida com os minutos medidos. Contar os dois seria
inflar o histórico.

## Decisões de segurança

- **Filtro PRIORITY, não silêncio total.** As exceções que a pessoa já configurou no Android
  (favoritos, quem liga duas vezes) continuam passando. Quem tem filho pequeno não usa silêncio total.
- **O filtro anterior é guardado antes de mexer** e devolvido ao encerrar. Se não sabemos qual era
  (permissão negada), o app não mexe em nada — nunca "chuta" o normal.
- **Notificação fixa com o botão de encerrar**, para a pessoa nunca ficar muda sem saber por quê. O
  canal pede para passar pelo Não Perturbe, porque é justamente o controle que o desliga.
- **Rede de segurança de 4 horas.** Um trabalho do WorkManager encerra a sessão esquecida e devolve o
  Não Perturbe mesmo com o app fechado, avisando o que aconteceu. Abrir o app também reconcilia:
  sessão vencida é encerrada, sessão viva tem a notificação recolocada.
- **Permissão.** Ligar o Não Perturbe exige o acesso à política de notificações, concedido uma vez
  nas configurações do Android. O pedido aparece quando a pessoa ativa a opção, nunca no onboarding.
  Sem a permissão, a sessão funciona como cronômetro e o app diz o que está faltando.
- **Nada de bloquear outros apps.** Isso exigiria serviço de acessibilidade, é invasivo e
  problemático na loja. O escopo é o Não Perturbe do sistema e as notificações do próprio Estudário.

## XP

O modo foco não dá XP por tempo. O relógio correndo não é esforço, e pagar por ele abriria a porta
para deixar a sessão aberta de propósito. O XP continua vindo das atividades concluídas.
