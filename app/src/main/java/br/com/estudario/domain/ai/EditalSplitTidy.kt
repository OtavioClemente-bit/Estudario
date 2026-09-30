package br.com.estudario.domain.ai

import java.text.Normalizer

/**
 * Arruma a divisão de itens do edital feita pela IA. O tópico-pai só agrupa; quem gera material
 * são as folhas. Por isso não pode existir:
 * - subtópico com o mesmo nome do pai (o filho some e os netos sobem para o lugar dele);
 * - pai com um único subtópico que só repete o nome dele (vira um tópico só, que gera material direto).
 *   Filho único com nome próprio fica: ele gera o material e o pai guarda o texto do edital.
 *
 * Genérico para servir tanto à proposta da IA quanto ao arquivo .estudo do prompt manual.
 */
object EditalSplitTidy {
    fun <T> tidy(
        node: T,
        name: (T) -> String,
        children: (T) -> List<T>,
        rebuild: (node: T, name: String, children: List<T>, absorbed: List<T>) -> T,
        canAbsorb: (T) -> Boolean = { true },
    ): T {
        var current = rebuild(node, name(node), children(node).map { tidy(it, name, children, rebuild, canAbsorb) }, emptyList())
        while (true) {
            val kids = children(current)
            val parentKey = canonical(name(current))
            val echo = kids.firstOrNull { canAbsorb(it) && isEcho(parentKey, canonical(name(it))) }
            if (echo != null) {
                val lifted = kids.flatMap { if (it === echo) children(echo) else listOf(it) }
                current = rebuild(current, name(current), lifted, listOf(echo))
                continue
            }
            return current
        }
    }

    // "Articulação textual" x "Articulação textual." ou "Noções de articulação textual": o filho não acrescenta nada.
    // Filho contido no pai não conta: "JSON Web Tokens (JWT)" sai de dentro do item e é divisão legítima.
    private fun isEcho(parentKey: String, childKey: String) = childKey.isEmpty() || childKey == parentKey || (parentKey.length >= 12 && childKey.contains(parentKey) && childKey.length - parentKey.length <= 12)

    internal fun canonical(value: String): String = Normalizer.normalize(value.trim().lowercase(), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("[^a-z0-9]+"), " ")
        .trim()
}
