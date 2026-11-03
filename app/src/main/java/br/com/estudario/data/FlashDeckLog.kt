package br.com.estudario.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Os baralhos de flashcards terminados, em qualquer lugar (tópico, Caderno, revisão), para o emblema
 * de flashcards. Antes ele só contava a tarefa FLASHCARDS do plano, que quase nunca aparece, e
 * nada do que a pessoa praticava contava. Cada baralho conta uma vez por dia ("deck-<id>-<dia>").
 *
 * Fica em folha_look ("baralhos") e vai na foto sincronizada como "flashDecks" (SyncedPrefs), a
 * mesma lista que o site soma.
 */
object FlashDeckLog {
    private const val PREFS = "folha_look"
    const val KEY = "baralhos"
    private val count = MutableStateFlow(-1)

    private fun ids(context: Context): Set<String> =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, null)
            ?.split(",")?.filter { it.isNotBlank() }?.toSet().orEmpty()

    fun count(context: Context): StateFlow<Int> {
        if (count.value < 0) refresh(context)
        return count
    }

    fun refresh(context: Context) { count.value = ids(context).size }

    fun record(context: Context, id: String) {
        val all = ids(context) + id
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, all.sorted().joinToString(",")).apply()
        count.value = all.size
    }

    fun today(): Long = java.time.LocalDate.now().toEpochDay()
}
