package br.com.estudario.domain

import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

/** Um nível da amizade com o Folha: [days] dias de estudo juntos para chegar nele; [unlock] o que muda. */
data class FolhaBondLevel(val index: Int, val name: String, val days: Int, val unlock: String)

/**
 * O vínculo com o Folha: sobe com os dias em que a pessoa estudou (não precisam ser seguidos), então
 * ninguém perde amizade por faltar, só demora mais. Cada nível libera um jeito novo dele: gestos ao
 * toque, falas e, no fim, a aura de amizade.
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
 * A memória do Folha na tela inicial: o que ele fala quando tem algo pessoal para dizer (subiu a
 * amizade, você sumiu e voltou, a prova está chegando, o que você estudou ontem, seu nome). Quando não
 * tem nada especial, devolve nulo e a tela usa a frase de sempre.
 */
object FolhaMemory {
    data class Context(
        val name: String?,
        val bond: Int,
        /** O nível de amizade que acabou de subir (ainda não comemorado na tela), se houver. */
        val bondUp: FolhaBondLevel?,
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
        c.bondUp?.let { up ->
            return Line("Nossa amizade subiu: agora somos ${up.name}! ${up.unlock}", "ecstatic")
        }
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
        // de vez em quando, uma fala pessoal no lugar da de sempre
        if (c.seed % 2 != 0L) return null
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
    )
}
