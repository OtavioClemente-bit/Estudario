package br.com.estudario.domain

/**
 * A imagem do perfil como fica na foto da conta ("userPrefs": "avatar" + "avatarUpdatedAt"), a mesma
 * no app e no site. Vale a escolhida por último, em qualquer aparelho (como o nome).
 *
 * - "avatar:<chave>": um dos personagens (as chaves de PresetAvatar no app e no site);
 * - "photo:data:image/jpeg;base64,…": uma foto da pessoa, quadrada e pequena (256 px);
 * - "google": a foto da conta Google (ou as iniciais, sem ela).
 */
object ProfileImage {
    const val KEY = "avatar"
    const val UPDATED_AT = "avatarUpdatedAt"
    const val GOOGLE = "google"
    const val PRESET_PREFIX = "avatar:"
    const val PHOTO_PREFIX = "photo:"

    /** Teto da foto na conta (base64): acima disso não vai, para não pesar a sincronização. */
    const val MAX_PHOTO_CHARS = 120_000

    val presetKeys = listOf(
        "folha", "capelo", "lampada", "alvo", "trofeu", "chama", "cerebro", "livros",
        "estrela", "diamante", "medalha", "escudo", "cafe", "lua", "coracao", "dado",
    )

    /** O valor aceito (personagem conhecido, foto dentro do teto ou "google"); outro vira null. */
    fun normalize(value: String?): String? {
        val v = value?.trim().orEmpty()
        return when {
            v == GOOGLE -> GOOGLE
            v.startsWith(PRESET_PREFIX) -> v.takeIf { it.removePrefix(PRESET_PREFIX) in presetKeys }
            v.startsWith(PHOTO_PREFIX + "data:image/") -> v.takeIf { it.length <= MAX_PHOTO_CHARS }
            else -> null
        }
    }

    fun presetKey(value: String?): String? = normalize(value)?.takeIf { it.startsWith(PRESET_PREFIX) }?.removePrefix(PRESET_PREFIX)
    fun photoDataUrl(value: String?): String? = normalize(value)?.takeIf { it.startsWith(PHOTO_PREFIX) }?.removePrefix(PHOTO_PREFIX)

    /**
     * Entre duas escolhas (valor, quando), a que vale: a mais nova; no empate, a [a].
     * Sem valor válido de um lado, vale o outro.
     */
    fun pick(a: Pair<String?, Long>, b: Pair<String?, Long>): Pair<String, Long>? {
        val va = normalize(a.first)
        val vb = normalize(b.first)
        return when {
            va == null && vb == null -> null
            va == null -> vb!! to b.second
            vb == null -> va to a.second
            b.second > a.second -> vb to b.second
            else -> va to a.second
        }
    }
}
