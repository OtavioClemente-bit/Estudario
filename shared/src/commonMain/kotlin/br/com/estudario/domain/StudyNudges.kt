package br.com.estudario.domain

/**
 * As falas do Folha nas notificações, como nos apps grandes de hábito: cada situação (revisão que
 * vence, sequência em risco, última chance, voltar depois de dias sumido, contagem da prova…) tem
 * várias frases, que giram dia a dia para não cansar, sempre com o dado concreto do dia (o tópico da
 * revisão, quantas questões faltam, quantos dias de sequência). Quem decide QUANDO avisar é o app;
 * aqui fica O QUE dizer.
 */
object StudyNudges {
    enum class Kind { DAILY, REVIEW_MORNING, REVIEWS_DUE, ALMOST_GOAL, PLAN_TASK, WEAK_SPOT, STREAK_RISK, LAST_CALL, COMEBACK, EXAM_COUNTDOWN }

    data class Facts(
        val name: String? = null,
        val streak: Int = 0,
        /** Dias sem estudar (0 = estudou ontem ou hoje). */
        val daysAway: Int = 0,
        val dueReviews: Int = 0,
        val firstReview: String? = null,
        val missing: Int = 0,
        val doneToday: Int = 0,
        val todayTask: String? = null,
        val taskMinutes: Int = 0,
        val weakSubject: String? = null,
        val weakAccuracy: Int = 0,
        val daysToExam: Int? = null,
        val competition: String? = null,
    )

    /** [mood]: o humor do Folha no ícone da notificação (nome do FolhaMood). */
    data class Nudge(val kind: Kind, val title: String, val body: String, val mood: String)

    /** Os dias de contagem da prova que merecem um aviso próprio. */
    val EXAM_MILESTONES = setOf(100, 60, 45, 30, 21, 15, 10, 7, 5, 3, 2, 1)

    /**
     * A situação do lembrete do dia, da mais importante para a menos: sumido há dias, marco da prova,
     * quase fechando o dia, revisão vencendo, tarefa do plano, ponto fraco, genérico.
     */
    fun dailyKind(f: Facts): Kind = when {
        f.daysAway >= 2 -> Kind.COMEBACK
        f.daysToExam != null && f.daysToExam in EXAM_MILESTONES -> Kind.EXAM_COUNTDOWN
        f.missing in 1..15 && f.doneToday > 0 -> Kind.ALMOST_GOAL
        f.dueReviews > 0 -> Kind.REVIEWS_DUE
        f.todayTask != null -> Kind.PLAN_TASK
        f.weakSubject != null -> Kind.WEAK_SPOT
        else -> Kind.DAILY
    }

    /** A fala para [kind]; [seed] (o dia, por exemplo) escolhe a variação, para não repetir sempre a mesma. */
    fun pick(kind: Kind, f: Facts, seed: Int): Nudge {
        val n = f.name?.trim()?.split(' ')?.firstOrNull()?.takeIf { it.isNotBlank() }
        val oi = n?.let { "$it, " } ?: ""
        val oiCap = n?.let { "$it, " } ?: ""
        val dias = if (f.streak == 1) "1 dia" else "${f.streak} dias"
        val rev = if (f.dueReviews == 1) "1 revisão" else "${f.dueReviews} revisões"
        val min = (f.dueReviews * 3).coerceAtLeast(3)
        val topic = f.firstReview
        val faltam = if (f.missing == 1) "1 questão" else "${f.missing} questões"
        val prova = f.competition?.let { " do $it" } ?: ""
        val options: List<Pair<String, String>> = when (kind) {
            Kind.REVIEW_MORNING -> if (topic != null) listOf(
                "Bom dia! Hoje é dia de revisar $topic" to "Uns $min minutos agora e o conteúdo fica para a prova. Depois, o dia é seu.",
                "${oiCap}$topic quer te ver de novo 👀" to "Revisar no dia certo é o que impede o esquecimento. ${rev.replaceFirstChar { it.uppercase() }}, uns $min min.",
                "A revisão de $topic vence hoje" to "Se passar, você esquece metade. Se revisar, ela volta só daqui a vários dias.",
                "☕ Café + revisão de $topic?" to "Combinação perfeita para começar o dia: $min minutinhos e pronto.",
                "Seu cérebro pediu: $topic" to "Hoje é o melhor dia para fixar. Amanhã já seria mais difícil lembrar.",
            ) else listOf("Bom dia! Tem revisão te esperando" to "$rev para hoje, uns $min min. Começar cedo deixa o resto do dia leve.")
            Kind.REVIEWS_DUE -> listOf(
                (if (topic != null) "Sua revisão de $topic vence hoje" else "Você tem $rev para hoje") to "Uns $min minutos. Revisar no dia certo é o que fixa o conteúdo.",
                "${oiCap}tem $rev esperando por você" to "Rapidinho: $min minutos e o conteúdo não escapa.",
                (if (topic != null) "$topic está quase saindo da sua memória" to "Uma revisão agora e ele fica de vez. Leva uns $min minutos."
                else "Bora revisar? É rápido" to "$rev, uns $min minutos. Melhor que reaprender depois."),
            )
            Kind.ALMOST_GOAL -> listOf(
                "Faltam só $faltam para fechar o dia" to "Você já fez ${f.doneToday} hoje. Termina agora e o dia fica garantido" + (if (f.streak >= 2) " (e os seus $dias seguidos também) 🔥" else "."),
                "Quase lá! Mais $faltam" to "Você parou pertinho da meta. Uns minutinhos e pronto.",
                "${oiCap}não deixa a meta pela metade" to "Faltam $faltam. Eu sei que você consegue agora mesmo.",
            )
            Kind.PLAN_TASK -> listOf(
                "Hoje no plano: ${f.todayTask}" to "${f.taskMinutes} min e você segue no ritmo da aprovação." + if (f.streak >= 2) " Sequência de $dias 🔥" else "",
                "${oiCap}seu bloco de hoje está pronto" to "${f.todayTask}, ${f.taskMinutes} min. Eu já separei tudo.",
                "Bora de ${f.todayTask}?" to "É o próximo passo do seu plano. ${f.taskMinutes} minutos e um tópico a mais na bagagem.",
            )
            Kind.WEAK_SPOT -> listOf(
                "Que tal 10 questões de ${f.weakSubject}?" to "Foi a matéria que mais te derrubou essa semana (${f.weakAccuracy}% de acerto). Treinar o ponto fraco é o que mais sobe a nota.",
                "${f.weakSubject} está te devendo uma revanche" to "${f.weakAccuracy}% de acerto essa semana. Dez questões hoje já mudam isso.",
            )
            Kind.STREAK_RISK -> listOf(
                "Seus $dias seguidos acabam à meia-noite 🔥" to "Ainda dá tempo! Algumas questões ou uma revisão já salvam o dia.",
                "${oiCap}a sua sequência está em perigo!" to "$dias de esforço não podem parar hoje. Cinco minutinhos resolvem.",
                "O fogo da sequência está apagando… 🔥" to "Ainda não teve estudo hoje. Bora salvar os seus $dias antes de dormir?",
                "Ei! Hoje ainda não conta" to "Faça qualquer coisa: umas questões, uma revisão ou um tópico. A sequência de $dias agradece.",
            )
            Kind.LAST_CALL -> listOf(
                "Última chance de salvar os $dias 😱" to "Falta pouco para a meia-noite. Uma revisão rapidinha e a sequência continua.",
                "${oiCap}ainda dá tempo, eu juro!" to "Só alguns minutos separam você de manter os $dias seguidos.",
                "Antes de dormir: 5 minutos?" to "A sequência de $dias acaba à meia-noite. Eu espero você aqui.",
            )
            Kind.COMEBACK -> when {
                f.daysAway >= 7 -> listOf(
                    "${oiCap}estou com saudade 🥺" to "Faz ${f.daysAway} dias. Sem culpa: um tópico só hoje já recomeça tudo.",
                    "O seu caderno está te esperando" to "Faz ${f.daysAway} dias que a gente não estuda junto. Bora recomeçar devagar?",
                    "A prova$prova não esqueceu de você" to "E eu também não. Volta com 10 minutos hoje, sem pressão.",
                )
                else -> listOf(
                    "${oiCap}sumiu? 👀" to "Faz ${f.daysAway} dias sem estudo. Uma revisão rápida hoje e o ritmo volta.",
                    "Senti sua falta ontem" to "Hoje é um ótimo dia para voltar. Eu já separei por onde começar.",
                    "Bora voltar pro jogo?" to "${f.daysAway} dias parado esfriam o conteúdo. Dez minutos hoje esquentam de novo.",
                )
            }
            Kind.EXAM_COUNTDOWN -> {
                val d = f.daysToExam ?: 0
                val when_ = if (d == 1) "amanhã" else "em $d dias"
                listOf(
                    "Faltam $d ${if (d == 1) "dia" else "dias"} para a prova$prova" to (if (d <= 7) "Reta final: revise os erros e os tópicos que mais caem. Você chegou até aqui." else "Cada dia de estudo agora vale ouro. Bora fazer o de hoje?"),
                    "A prova$prova é $when_ 📅" to "Quem estuda um pouco todo dia chega mais tranquilo. O de hoje já está separado.",
                )
            }
            Kind.DAILY -> listOf(
                "${oi}bora estudar?".replaceFirstChar { it.uppercase() } to "Um bloquinho hoje e a posse fica mais perto.",
                "Seu estudo de hoje está esperando" to "Eu já separei o próximo passo. Abre aí que eu te mostro.",
                "Tô aqui com o seu plano pronto! 📚" to "Constância vale mais que uma sessão perfeita. Vamos de 15 minutos?",
                (if (f.streak >= 2) "Bora para o dia ${f.streak + 1} seguido? 🔥" else "Hoje pode ser o dia 1 de uma sequência") to "Abrir o app e fazer um pouquinho já conta.",
                "Uma questão por vez, uma vaga por vez" to "Seu concurso não vai se estudar sozinho. Bora?",
                "Lembra por que você começou? 💪" to "A aprovação é feita de dias comuns como hoje. Vamos fazer o de hoje valer.",
            )
        }
        val (title, body) = options[((seed % options.size) + options.size) % options.size]
        val mood = when (kind) {
            Kind.STREAK_RISK, Kind.LAST_CALL -> "SURPRISED"
            Kind.COMEBACK -> if (f.daysAway >= 7) "SAD" else "WAVE"
            Kind.ALMOST_GOAL, Kind.EXAM_COUNTDOWN -> "PROUD"
            Kind.REVIEW_MORNING, Kind.REVIEWS_DUE -> "THINKING"
            Kind.WEAK_SPOT -> "FOCUS"
            else -> "HAPPY"
        }
        return Nudge(kind, title, body, mood)
    }
}
