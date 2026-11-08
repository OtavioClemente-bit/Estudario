# Regressão: repetir treino depois da revisão

Antes da correção, no site publicado em modo de demonstração:
1. Revisões espaçadas > Hash > Ir para as questões > responder > Avaliar > Concluir revisão.
2. Fazer questões > responder > Ver resultado.
3. Treinar de novo: a URL continua /treinar/questoes/topico-4-5 e o resultado continua visível. O endereço já era o destino do botão, portanto não há hashchange nem nova sessão.

Correção: cada tentativa tem uma chave de composição nova. O botão reinicia o mesmo recorte, com novo sorteio, respostas, tempo e id de sessão. A sessão concluída permanece no histórico; o vínculo com a tarefa do plano já foi consumido e não é repetido.

Verificação planejada: repetir o caminho da revisão no navegador; iniciar e concluir uma segunda bateria; repetir outra vez; confirmar questão habilitada, contadores zerados, cronômetro reiniciado e funcionamento do retorno ao conteúdo. Rodar jsNodeTest e build de produção; publicar e repetir em produção. Usar somente demonstração para não gravar respostas fictícias na conta.

App Android: QuizResult usa botão Voltar ao treino/onBack; EstudarioApp liga onBack a navController.popBackStack. Não há o botão defeituoso que abre a mesma URL.
