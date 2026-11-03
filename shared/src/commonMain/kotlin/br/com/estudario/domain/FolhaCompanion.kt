package br.com.estudario.domain

import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

/** Um nível da amizade com o Folha: [days] dias de estudo juntos para chegar nele; [unlock] o que muda. */
data class FolhaBondLevel(val index: Int, val name: String, val days: Int, val unlock: String)

/**
 * O vínculo com o Folha: sobe com os dias em que a pessoa estudou (não precisam ser seguidos). Não
 * aparece em tela nenhuma; só faz o Folha ficar mais próximo com o tempo (gestos novos ao toque e
 * falas de amigo).
 */
object FolhaBond {
    val levels: List<FolhaBondLevel> = listOf(
        FolhaBondLevel(0, "Recém-chegados", 0, "Ele te cumprimenta e reage ao toque."),
        FolhaBondLevel(1, "Colegas de estudo", 3, "Ele passa a lembrar o que você estudou."),
        FolhaBondLevel(2, "Parceiros", 7, "Gesto novo: uma dancinha quando você toca nele."),
        FolhaBondLevel(3, "Amigos", 15, "Gesto novo: um abraço apertado."),
        FolhaBondLevel(4, "Melhores amigos", 30, "Ele te chama pelo nome e ganha falas de amigo."),
        FolhaBondLevel(5, "Inseparáveis", 60, "Gesto novo: olhos de estrela e chuva de brilhos."),
        FolhaBondLevel(6, "Lendários", 120, "Corações dourados em volta dele quando você chega."),
    )

    fun level(activeDays: Int): FolhaBondLevel = levels.last { activeDays >= it.days }
    fun next(activeDays: Int): FolhaBondLevel? = levels.firstOrNull { it.days > activeDays }

    /** 0..1 até o próximo nível (1 no último). */
    fun progress(activeDays: Int): Float {
        val cur = level(activeDays); val nxt = next(activeDays) ?: return 1f
        return ((activeDays - cur.days).toFloat() / (nxt.days - cur.days)).coerceIn(0f, 1f)
    }

    /** Dias desde o último dia com estudo de verdade (0 = hoje, 1 = ontem); nulo se nunca estudou. */
    fun daysAway(calendar: List<DailyActivity>, today: LocalDate): Int? {
        val last = calendar.filter { it.questions > 0 || it.planTasks > 0 || it.reviews > 0 }.maxOfOrNull { it.date.toEpochDay() } ?: return null
        return (today.toEpochDay() - last).toInt().coerceAtLeast(0)
    }
}

/**
 * A memória do Folha na tela inicial: o que ele fala quando tem algo pessoal para dizer (você sumiu e voltou, a prova está chegando, o que você estudou ontem, seu nome). Quando não
 * tem nada especial, devolve nulo e a tela usa a frase de sempre.
 */
object FolhaMemory {
    data class Context(
        val name: String?,
        val bond: Int,
        val daysAway: Int?,
        val todayDone: Boolean,
        val streak: Int,
        val daysToExam: Int?,
        /** A matéria estudada por último e há quantos dias. */
        val lastSubject: String?,
        val lastSubjectDaysAgo: Int?,
        val hour: Int,
        val seed: Long,
    )

    /** [mood]: o humor do Folha enquanto fala ("happy", "sad", "ecstatic", "proud", "wave", "talking"). */
    data class Line(val text: String, val mood: String)

    private fun <T> List<T>.pick(seed: Long): T = this[((seed % size) + size).toInt() % size]

    fun home(c: Context): Line? {
        val nome = c.name?.takeIf { it.isNotBlank() && it != "estudante" }
        val voce = nome ?: "você"
        val away = c.daysAway
        if (away != null && away >= 2) {
            if (c.todayDone) return Line(listOf(
                "Voltou com tudo! Tava com saudade, $voce.",
                "Ahh, que bom te ver estudando de novo! Senti falta.",
            ).pick(c.seed), "ecstatic")
            return Line(listOf(
                "Você sumiu $away dias… senti sua falta! Bora voltar devagarinho?",
                "Ei${nome?.let { ", $it" } ?: ""}! Fiquei te esperando. Um tópico hoje já me deixa feliz.",
                "Sem você aqui as páginas ficaram em branco. Volta pra mim?",
                "Faz $away dias que a gente não estuda junto. Um pouquinho hoje?",
            ).pick(c.seed), "sad")
        }
        c.daysToExam?.let { d ->
            if (d == 0) return Line("É hoje! Respira, confia no que você estudou. Tô torcendo daqui!", "proud")
            if (d in 1..7) return Line(listOf(
                "Faltam $d ${if (d == 1) "dia" else "dias"} pra prova. Agora é revisão e calma: a gente chega pronto.",
                "Reta final! $d ${if (d == 1) "dia" else "dias"}. Dorme bem, revisa os erros e confia.",
            ).pick(c.seed), "proud")
        }
        if (c.todayDone) return null
        // Quase sempre uma fala pessoal (soa como conversa); de vez em quando, a de sempre.
        if (c.seed % 4 == 0L) return null
        val options = buildList {
            if (c.lastSubject != null && c.lastSubjectDaysAgo == 1) add(Line("Ontem foi ${c.lastSubject}. Hoje a gente segue firme?", "talking"))
            if (c.lastSubject != null && c.lastSubjectDaysAgo == 0) add(Line("Já mandou ${c.lastSubject} hoje. Mais um pouquinho?", "happy"))
            if (nome != null && c.bond >= 1) add(Line("Bora, $nome? Eu seguro a página.", "wave"))
            if (nome != null && c.bond >= 4) add(Line("$nome, você é meu humano favorito. Bora estudar?", "happy"))
            if (c.streak >= 3) add(Line("${c.streak} dias seguidos juntos! Não deixa a gente cair hoje.", "proud"))
            c.daysToExam?.takeIf { it in 8..60 }?.let { add(Line("Faltam $it dias. Cada tópico agora vale ouro.", "talking")) }
            if (c.bond >= 3) add(Line("Amigo que estuda junto passa junto. Partiu?", "wave"))
            if (c.hour in 5..9) add(Line("Bom dia${nome?.let { ", $it" } ?: ""}! Cabeça fresquinha é a melhor hora.", "wave"))
            if (c.hour >= 22 || c.hour < 2) add(Line("Tá tarde${nome?.let { ", $it" } ?: ""}. Um tópico curtinho e cama, combinado?", "talking"))
            c.lastSubject?.let { s ->
                when (c.lastSubjectDaysAgo) {
                    0 -> {
                        add(Line("Vi que você já passou por $s hoje. Tá rendendo, hein?", "proud"))
                        add(Line("$s hoje já foi. Quer variar um pouco ou seguir nela?", "talking"))
                    }
                    1 -> {
                        add(Line("Fiquei pensando em $s, que você estudou ontem. Hoje uma revisãozinha cai bem.", "talking"))
                        add(Line("Ontem foi $s e você mandou bem. Bora manter o embalo?", "happy"))
                    }
                    in 2..6 -> add(Line("Faz uns dias que a gente não vê $s. Não deixa esfriar, tá?", "talking"))
                    else -> {}
                }
            }
            if (nome != null) {
                add(Line("Oi, $nome! Como você tá hoje? Eu tô animado pra gente estudar.", "wave"))
                add(Line("$nome, separei o próximo passo pra você. Bem tranquilo, prometo.", "talking"))
                add(Line("E aí, $nome? Pronto pra deixar o edital um pouco menor?", "happy"))
                if (c.hour in 12..17) add(Line("Boa tarde, $nome! Bora encaixar um estudo nesse pedaço do dia?", "wave"))
                if (c.hour in 18..21) add(Line("Boa noite, $nome! Cansou do dia? Começa leve que eu acompanho.", "talking"))
            }
            if (c.streak in 1..2) add(Line("Começamos uma sequência! Hoje é o dia de deixar ela maior.", "happy"))
            if (c.streak >= 7) add(Line("${c.streak} dias juntos sem falhar. Eu fico até emocionado.", "proud"))
            if (c.streak >= 14) add(Line("Mais de duas semanas seguidas, $voce. Isso já é quem você é.", "proud"))
            c.daysToExam?.takeIf { it in 61..400 }?.let { add(Line("Ainda faltam $it dias. Com calma e constância, dá pra chegar muito bem.", "talking")) }
            if (c.bond >= 2) add(Line("A gente já virou parceiro de estudo, né? Bora mais um dia.", "happy"))
            if (c.bond >= 5) add(Line("Depois de tanto tempo junto, eu já sei: hoje você vai mandar bem.", "proud"))
            if (away == 1) add(Line("Ontem você estudou, hoje a gente continua. Simples assim.", "happy"))
        }
        return options.takeIf { it.isNotEmpty() }?.pick(c.seed / 2)
    }

    /** Frases do toque que dependem da amizade (o site e o app sorteiam entre elas e as de sempre). */
    fun bondTapLines(bond: Int, name: String?): List<String> = buildList {
        val nome = name?.takeIf { it.isNotBlank() && it != "estudante" }
        if (bond >= 1) add("Lembra quando a gente começou? Olha você agora!")
        if (bond >= 2) add("Parceiro de estudo é pra isso!")
        if (bond >= 3) add("Amigo que estuda junto passa junto!")
        if (bond >= 4) { add(if (nome != null) "$nome, você é meu humano favorito!" else "Você é meu humano favorito!"); add("Melhores amigos não deixam a sequência cair!") }
        if (bond >= 5) add("Inseparáveis até a posse!")
        if (bond >= 6) add("Lendários! Já viu dupla melhor?")
    }

    /** Na bateria de questões: errou várias seguidas. */
    val toughLines = listOf(
        "Tá pesado, né? Respira fundo, lê a explicação com calma. A próxima vem.",
        "Bateria difícil acontece. Cada erro aqui é um que não acontece na prova.",
        "Ei, não desanima! Eu tô aqui. Vamos uma de cada vez.",
        "Errar três seguidas dói, mas é assim que o conteúdo gruda. Bora!",
        "Ei, eu sei que frustra. Que tal ler a teoria desse ponto e voltar depois?",
        "Respira comigo. Ninguém acerta tudo, e você tá aprendendo agora.",
        "Essas estão difíceis mesmo. Não é você, é o assunto pedindo mais uma volta.",
        "Tô do seu lado. Uma de cada vez, sem pressa.",
        "Quando todo mundo erra, quem revisa passa na frente. Vamos revisar?",
        "Não desiste agora. A próxima pode ser justamente a que vira o jogo.",
    )
}
