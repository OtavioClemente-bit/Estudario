package br.com.estudario.ui.assistant

/**
 * O que o Folha fala. A escolha usa uma semente estável (o id da questão, o dia, o número da
 * sequência), então a fala não troca a cada recomposição e também não é sempre a mesma.
 */
object FolhaLines {
    private fun <T> List<T>.pick(seed: Long): T = this[Math.floorMod(seed, size.toLong()).toInt()]

    private val correct = listOf(
        "Isso! Mandou muito bem.",
        "Acertou! Esse conteúdo já é seu.",
        "Na mosca! Banca nenhuma te pega nessa.",
        "Boa! É assim que a vaga vai chegando.",
        "Perfeito. Pode marcar esse ponto no placar.",
        "Acertou e com estilo. Próxima!",
    )

    private val wrong = listOf(
        "Errar aqui é melhor que errar na prova. Lê a explicação comigo?",
        "Tudo bem! Essa volta pro seu caderno de erros e na próxima é sua.",
        "Quase! Esse é exatamente o tipo de pegadinha que a banca adora.",
        "Respira. Cada erro agora é um ponto a mais no dia da prova.",
        "Faz parte. Entendendo o porquê, você não erra mais essa.",
        "Opa, essa enganou. Bora entender e virar o jogo?",
    )

    fun forAnswer(correct: Boolean, seed: Long): String = (if (correct) this.correct else wrong).pick(seed)

    /** Fim da bateria: a fala depende de como foi. */
    fun forResult(percent: Int, seed: Long): String = when {
        percent >= 90 -> listOf(
            "Que bateria! Com esse desempenho, a aprovação é questão de tempo.",
            "Impressionante. Você está voando nesse conteúdo!",
        ).pick(seed)
        percent >= 70 -> listOf(
            "Muito bom! Acima de 70% é ritmo de aprovado.",
            "Mandou bem! Os erros já foram pro caderno pra gente revisar.",
        ).pick(seed)
        percent >= 50 -> listOf(
            "Bom caminho! Revisando os erros, a próxima sai bem melhor.",
            "Metade já está dominada. Bora atacar o resto juntos?",
        ).pick(seed)
        else -> listOf(
            "Dia difícil acontece. O importante é que você treinou, e cada erro virou aprendizado.",
            "Não desanima! Os erros estão guardados e vão voltar na hora certa pra você fixar.",
        ).pick(seed)
    }

    /** Meta do dia batida: a sequência continua. */
    fun forStreak(days: Int, seed: Long): String = when {
        days <= 1 -> "Primeiro dia da sequência! Todo aprovado começou exatamente assim."
        days < 7 -> listOf(
            "$days dias seguidos! Constância vence talento.",
            "$days dias sem falhar. Tô orgulhoso de você!",
        ).pick(seed)
        days < 30 -> listOf(
            "$days dias de sequência! Isso já é hábito de aprovado.",
            "$days dias! A banca que se prepare.",
        ).pick(seed)
        else -> "$days dias seguidos. Você é a definição de disciplina!"
    }

    fun forBadge(seed: Long): String = listOf(
        "Olha o que você conquistou!",
        "Emblema novo na coleção! Merecido demais.",
        "Mais uma conquista. Bora pela próxima?",
    ).pick(seed)

    // ------------------------------------------------------------ Início, cartão "Agora"

    private val homeStart = listOf(
        "Bora? Seu próximo estudo está aqui.",
        "Quem estuda hoje não corre atrás amanhã.",
        "Um tópico de cada vez. É assim que o edital acaba.",
        "A vaga não tem nome ainda. Vamos colocar o seu?",
        "Constância vence talento que não aparece.",
        "Hoje é um bom dia para ficar mais perto da posse.",
        "Pouco todo dia rende mais que muito de vez em quando.",
        "Abre o tópico comigo? Eu seguro a página.",
        "O concorrente também está estudando. Bora na frente?",
        "Cada tópico fechado é um ponto a mais na prova.",
        "Disciplina é lembrar do que você quer, mesmo cansado.",
        "Sua aprovação está sendo construída agora.",
        "Edital grande se vence assim: começando.",
        "Café, foco e eu. Partiu?",
        "Dez minutos já contam. Começa e vê o tempo passar.",
        "A prova não pergunta se você estava com vontade.",
        "Seu eu do futuro vai agradecer por esse estudo.",
        "Bora transformar esse tópico em acerto na prova?",
    )

    private val homeContinue = listOf(
        "Bora terminar o que começamos?",
        "Você parou no meio. Eu guardei a página.",
        "Falta pouco para fechar esse tópico.",
        "Começou, agora termina. É assim que se passa.",
        "Voltou! Vamos de onde você parou.",
        "Esse tópico está quase seu. Só mais um pouco.",
    )

    private val homeGenerate = listOf(
        "Vamos preparar o material deste tópico?",
        "Eu escrevo a teoria, você só estuda. Topa?",
        "Esse tópico ainda está em branco. Bora preencher?",
        "Um toque e eu monto teoria, flashcards e questões.",
        "Deixa comigo: preparo tudo do jeito da sua banca.",
    )

    private val morning = listOf("Bom dia! Cabeça descansada aprende mais rápido.", "Começar cedo é sair na frente. Bora?")
    private val night = listOf("Estudo da noite também conta. Bora fechar o dia bem?", "Um último tópico antes de dormir? A memória agradece.")

    /**
     * Frase do Folha no cartão "Agora". [seed] muda a cada vez que o Início abre, então ele fala
     * coisas diferentes ao longo do dia. De manhã e à noite às vezes ele comenta o horário.
     */
    fun forHome(continuing: Boolean, needsMaterial: Boolean, hour: Int, seed: Long): String = when {
        needsMaterial -> homeGenerate.pick(seed)
        continuing -> homeContinue.pick(seed)
        hour in 5..9 && seed % 4 == 0L -> morning.pick(seed / 4)
        (hour >= 21 || hour < 2) && seed % 4 == 0L -> night.pick(seed / 4)
        else -> homeStart.pick(seed)
    }
}
