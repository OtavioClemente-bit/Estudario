package br.com.estudario.domain

/**
 * Lembrete das animações que aparecem uma vez só (subir de nível, emblema, peça nova, dia fechado,
 * sequência salva, guias). Mora na foto da conta, em "userPrefs" -> "celebrated", e as chaves são as
 * mesmas no app e no site: o que foi visto num aparelho não aparece de novo no outro depois de
 * sincronizar. A lista só cresce: entre dois aparelhos vale a união.
 */
object Celebrated {
    /** Nome da lista dentro de "userPrefs". */
    const val SYNCED = "celebrated"
    /** Listas antigas, que continuam valendo como visto (e continuam sendo gravadas para apps antigos). */
    const val LEGACY_BADGES = "celebratedBadges"
    const val LEGACY_PIECES = "celebratedPieces"

    fun level(level: Int) = "level:$level"
    fun badge(id: String) = "badge:$id"
    /** [key] no formato das listas antigas: "SLOT:id". */
    fun piece(key: String) = "piece:$key"
    fun day(epochDay: Long) = "day:$epochDay"
    /** [isoDate] no formato AAAA-MM-DD. */
    fun streakSaved(isoDate: String) = "streak-saved:$isoDate"
    fun tour(name: String) = "tour:$name"

    /** Tudo o que conta como visto: a lista nova mais as antigas convertidas para as chaves novas. */
    fun all(celebrated: Set<String>, legacyBadges: Set<String> = emptySet(), legacyPieces: Set<String> = emptySet()): Set<String> =
        celebrated + legacyBadges.map(::badge) + legacyPieces.map(::piece)

    /** Junção entre dois aparelhos: nada visto se perde. */
    fun merge(a: Set<String>, b: Set<String>): Set<String> = (a + b).filter { it.isNotBlank() }.sorted().toSet()
}
