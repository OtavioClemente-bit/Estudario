package br.com.estudario.domain

/** Onde o item vai no Folha. */
enum class FolhaSlot(val label: String) {
    CAPA("Capa"),
    FITA("Fita"),
    ACESSORIO("Acessório"),
}

/**
 * Um item do armário do Folha. Quem desbloqueia é um emblema ([badgeId]) ou um nível ([level]); sem
 * nenhum dos dois, vem liberado. O [id] é o mesmo que o desenho usa (folha-live.js e Folha.kt).
 */
data class FolhaItem(
    val id: String,
    val slot: FolhaSlot,
    val name: String,
    val badgeId: String? = null,
    val level: Int? = null,
)

/**
 * O armário do Folha: capas, fitas e acessórios que a pessoa ganha estudando. Cada item sai de uma
 * conquista de verdade (emblema ou nível), então nada é comprado nem ganho por engano, e quem troca
 * de aparelho recupera tudo junto com o histórico.
 */
object FolhaWardrobe {
    val items: List<FolhaItem> = listOf(
        FolhaItem("indigo", FolhaSlot.CAPA, "Índigo Estudário"),
        FolhaItem("esmeralda", FolhaSlot.CAPA, "Esmeralda", badgeId = "meta-1"),
        FolhaItem("rubi", FolhaSlot.CAPA, "Rubi", badgeId = "seq-7"),
        FolhaItem("ametista", FolhaSlot.CAPA, "Ametista", level = 5),
        FolhaItem("couro", FolhaSlot.CAPA, "Couro clássico", badgeId = "topico-50"),
        FolhaItem("grafite", FolhaSlot.CAPA, "Grafite", badgeId = "quest-500"),
        FolhaItem("noite", FolhaSlot.CAPA, "Noite estrelada", badgeId = "seq-30"),
        FolhaItem("ouro", FolhaSlot.CAPA, "Edição de ouro", badgeId = "edital-100"),

        FolhaItem("menta", FolhaSlot.FITA, "Menta"),
        FolhaItem("ceu", FolhaSlot.FITA, "Céu", badgeId = "quest-100"),
        FolhaItem("coral", FolhaSlot.FITA, "Coral", badgeId = "revisao-10"),
        FolhaItem("lilas", FolhaSlot.FITA, "Lilás", badgeId = "discursiva-1"),
        FolhaItem("dourada", FolhaSlot.FITA, "Dourada", badgeId = "acerto-80"),

        FolhaItem("nenhum", FolhaSlot.ACESSORIO, "Nenhum"),
        FolhaItem("oculos", FolhaSlot.ACESSORIO, "Óculos de leitura", badgeId = "topico-10"),
        FolhaItem("lapis", FolhaSlot.ACESSORIO, "Lápis no capelo", badgeId = "plano-10"),
        FolhaItem("gravata", FolhaSlot.ACESSORIO, "Gravata-borboleta", badgeId = "meta-10"),
        FolhaItem("estrela", FolhaSlot.ACESSORIO, "Broche de estrela", badgeId = "maratona-120"),
        FolhaItem("louros", FolhaSlot.ACESSORIO, "Coroa de louros", badgeId = "simulado-1"),
    )

    fun of(slot: FolhaSlot): List<FolhaItem> = items.filter { it.slot == slot }
    fun default(slot: FolhaSlot): FolhaItem = of(slot).first()
    fun byId(id: String?): FolhaItem? = items.firstOrNull { it.id == id }

    fun unlocked(item: FolhaItem, summary: ProgressEngine.ProgressSummary): Boolean = when {
        item.badgeId != null -> summary.badges.any { it.badge.id == item.badgeId && it.earned }
        item.level != null -> summary.level >= item.level
        else -> true
    }

    /** Como desbloquear, em uma linha ("Emblema Semana cheia", "Nível 5"). */
    fun requirement(item: FolhaItem): String = when {
        item.badgeId != null -> BadgeCatalog.all.firstOrNull { it.id == item.badgeId }?.let { "Emblema ${it.name}: ${it.requirement.replaceFirstChar { c -> c.lowercaseChar() }}" } ?: "Emblema"
        item.level != null -> "Chegar ao nível ${item.level}"
        else -> "Liberado"
    }

    /** A escolha salva, mas só se ainda vale (item existe, é do lugar certo e está desbloqueado). */
    fun resolve(slot: FolhaSlot, chosen: String?, summary: ProgressEngine.ProgressSummary): FolhaItem =
        byId(chosen)?.takeIf { it.slot == slot && unlocked(it, summary) } ?: default(slot)

    /** Itens que este resumo desbloqueia e o [before] ainda não (para avisar "novo no armário"). */
    fun newlyUnlocked(before: ProgressEngine.ProgressSummary, after: ProgressEngine.ProgressSummary): List<FolhaItem> =
        items.filter { unlocked(it, after) && !unlocked(it, before) }
}
