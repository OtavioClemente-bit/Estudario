# Livro do nível: encadernação viva

Pedido: aprimorar fortemente o livro existente, preservar a identidade do Estudário e mostrar somente o número dentro do marcador. Aplicar no app e no site, como nas alterações anteriores do perfil.

Direção escolhida dentro da liberdade criativa já solicitada: livro aberto com páginas de papel quente, contorno violeta de caneta, profundidade em camadas e marcador de tecido com número protagonista. Sem texto NÍVEL no desenho. Preservar o mascote e as outras ilustrações.

Progressão: início neutro e páginas vazias; escrita, dobradura, douração, cantoneiras e pequenas gravuras surgem gradualmente. Ornamentos permanecem presos ao livro; nada de halo ou brasão ao redor. As faixas altas ganham detalhes finos adicionais. Movimento de repouso leve; ao toque, folhas atravessam a lombada com sombra e verso, enquanto o marcador acompanha o gesto. Respeitar movimento reduzido e legibilidade de 1 a 200.

1. Redesenhar as curvas em profile-art.js e conferir em prancha local, nos temas claro e escuro e em tamanhos reais.
2. Portar as mesmas curvas e gestos para ProfileIllustrations.kt, mantendo as demais ilustrações. Retirar NÍVEL e alinhar o número ao novo marcador.
3. Conferir clique/teclado, faixa inicial e avançada, níveis de três dígitos, fontes grandes e ausência de recorte. Compilar Preview e site; executar as verificações adequadas.
4. Publicar o site validado e disponibilizar o Preview Android para teste. Sem commit/push.
## Ajuste final solicitado durante a revisão

As linhas que simulavam escrita foram substituídas por gravuras fechadas de folhas, bússola, estrela e lua. O marcador ficou mais curto e estreito para mostrar as páginas. A encadernação ganhou curva central arredondada e costura visível. O cordão agora tem dupla linha de corda, nó e cinco fios separados, com movimento ancorado no canto do livro.

Verificado no Android: Preview compilado e instalado no celular KF85CQ4LT8FIOFCI; prancha final capturada; nível 200 com fonte 2x no emulador sem corte do número. A captura preta posterior ao toque no celular foi a tela apagada (mWakefulness=Dozing); processo do app permaneceu ativo e não havia erro AndroidRuntime. Não alterar bloqueio do usuário.

Verificado no site: renderer com 13 níveis e 4.000 quadros, 49 curvas estáticas sem crescimento de cache, somente números no desenho e modo de movimento reduzido. Revisão independente sem achados importantes. Publicação e3ccb60f.estudario-app.pages.dev, domínio app.estudario.com.br, versão 9fff2d7c-191921. Index e hash do asset conferidos em produção; perfil real carregado, toque acionado e console sem erros. Artefatos de QA excluídos da publicação.
