package br.com.estudario.domain

/**
 * Escolhas da pessoa que valem no celular e no site (bloco "userPrefs" da foto da conta, campos
 * "settings" e "settingsAt"): tema, explicação logo após responder, placar das questões, revisão
 * padrão ou intensiva, opções da revisão, leitura da teoria...
 *
 * Cada ajuste leva o valor (sempre em texto, o mesmo nos dois lados) e quando foi mudado. Vale o
 * mudado por último, ajuste por ajuste: trocar o tema no site não desfaz a letra escolhida no app.
 * Ajuste que um lado não conhece passa adiante sem ser apagado. Sem data (versão antiga, nunca
 * mexido) conta como 0: qualquer escolha feita depois vence.
 */
object SyncedSettings {
    const val SETTINGS = "settings"
    const val SETTINGS_AT = "settingsAt"

    /** "SYSTEM", "LIGHT" ou "DARK". */
    const val THEME = "theme"
    /** "1" mostra a explicação logo após responder; "0" só no fim. */
    const val EXPLAIN_RIGHT_AWAY = "explainRightAway"
    const val HUD_TIMER = "hudTimer"
    const val HUD_SCORE = "hudScore"
    /** Dias das revisões separados por vírgula ("1,7,30" padrão, "1,3,7,14,30" intensivo). */
    const val REVIEW_INTERVALS = "reviewIntervals"
    /** [ReviewPracticeOptions.encode]. */
    const val REVIEW_PRACTICE = "reviewPractice"
    const val DEFAULT_QUESTION_COUNT = "defaultQuestionCount"
    /** Degrau da letra da teoria, de "0" a "3" (o app e o site têm quatro tamanhos). */
    const val READER_SIZE = "readerSize"
    /** "livro" (com serifa) ou "simples". */
    const val READER_FONT = "readerFont"
    /** "normal", "sepia" ou "noite". */
    const val READER_TONE = "readerTone"
    const val READER_QUIET = "readerQuiet"

    data class Setting(val value: String, val at: Long)

    fun flag(value: Boolean) = if (value) "1" else "0"
    fun flag(value: String?): Boolean? = when (value) { "1" -> true; "0" -> false; else -> null }

    val readerTones = listOf("normal", "sepia", "noite")

    fun intervals(value: String?): List<Long>? =
        value?.split(',')?.mapNotNull { it.trim().toLongOrNull()?.takeIf { d -> d in 1..ReviewIntervals.MAX_DAYS } }?.takeIf { it.isNotEmpty() }

    /**
     * Junta os ajustes daqui com os de lá: cada um com a data mais nova (no empate, o de [a]).
     * Os que só um lado tem ficam.
     */
    fun merge(a: Map<String, Setting>, b: Map<String, Setting>): Map<String, Setting> =
        (a.keys + b.keys).associateWith { key ->
            val x = a[key]
            val y = b[key]
            when {
                x == null -> y!!
                y == null -> x
                y.at > x.at -> y
                else -> x
            }
        }

    /**
     * Registro do que este aparelho tem agora: ajuste que mudou desde o último registro ganha a data
     * [now]; o que continua igual mantém a data antiga. Ajuste sem registro (primeira vez, escolha
     * feita antes desta versão) entra com data 1: perde para qualquer escolha datada do outro aparelho,
     * mas ainda chega ao aparelho que nunca escolheu nada.
     */
    fun stamp(previous: Map<String, Setting>, current: Map<String, String>, now: Long): Map<String, Setting> =
        previous + current.mapValues { (key, value) ->
            val old = previous[key]
            when {
                old == null -> Setting(value, 1L)
                old.value == value -> old
                else -> Setting(value, now)
            }
        }

    /** Os ajustes de [remote] mais novos que os daqui: são os que este aparelho deve adotar. */
    fun newer(local: Map<String, Setting>, remote: Map<String, Setting>): Map<String, Setting> =
        remote.filter { (key, setting) -> setting.at > (local[key]?.at ?: 0L) && setting.at > 0L }
}
