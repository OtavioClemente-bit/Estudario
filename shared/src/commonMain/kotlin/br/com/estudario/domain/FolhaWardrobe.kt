package br.com.estudario.domain

/** Grupos do armário, na ordem em que aparecem. */
enum class FolhaGroup(val label: String) { SEU_FOLHA("Seu Folha"), ACESSORIOS("Acessórios"), EFEITOS("Efeitos") }

/**
 * Onde a peça vai no Folha. [key] é o nome que o desenho usa (folha-live.js e Folha.kt); [hint] explica
 * a categoria em uma linha; [group] organiza o armário.
 */
enum class FolhaSlot(val key: String, val label: String, val hint: String, val group: FolhaGroup) {
    CAPA("cover", "Capa", "A cor e o acabamento do livro. Os braços acompanham.", FolhaGroup.SEU_FOLHA),
    PAGINAS("pages", "Páginas", "O papel do Folha: pautado, quadriculado, pontilhado, de partitura ou liso.", FolhaGroup.SEU_FOLHA),
    TINTA("ink", "Tinta", "A cor dos olhos, das sobrancelhas e da boca.", FolhaGroup.SEU_FOLHA),
    BOCHECHAS("cheeks", "Bochechas", "O rosado do rosto.", FolhaGroup.SEU_FOLHA),
    FITA("ribbon", "Marcador", "A fita de marcar página.", FolhaGroup.SEU_FOLHA),
    LUVAS("gloves", "Luvas", "As mãos do Folha. Elas aparecem melhor na pose de mostrar.", FolhaGroup.SEU_FOLHA),
    CHAPEU("hat", "Chapéu", "No lugar do capelo de formatura.", FolhaGroup.ACESSORIOS),
    OCULOS("eyes", "Óculos", "Seguem o olhar do Folha.", FolhaGroup.ACESSORIOS),
    PESCOCO("neck", "Peito", "Gravatas, medalhas e enfeites no pé das páginas.", FolhaGroup.ACESSORIOS),
    MAO("hand", "Na mão", "Um item que ele segura na mão esquerda e leva junto em todos os gestos.", FolhaGroup.ACESSORIOS),
    AURA("aura", "Aura", "Um efeito em volta dele. Toque no Folha para ver o estouro.", FolhaGroup.EFEITOS),
    COMPANHEIRO("pet", "Companheiro", "Um amigo que fica ao lado dele, comemora junto e cochila junto.", FolhaGroup.EFEITOS),
}

/**
 * Coleção: um conjunto de peças com tema. Completar libera a [reward], uma peça exclusiva que só sai daqui.
 * [pieces] e [reward] são (lugar, id) de peças do armário.
 */
data class FolhaCollection(
    val id: String,
    val name: String,
    val description: String,
    val pieces: List<Pair<FolhaSlot, String>>,
    val reward: Pair<FolhaSlot, String>,
)

/** Raridade da peça: quanto mais difícil a conquista, mais rara. */
enum class FolhaRarity(val label: String) { COMUM("Comum"), RARA("Rara"), EPICA("Épica"), LENDARIA("Lendária") }

/**
 * Uma peça do armário. Quem libera é um emblema ([badgeId]) ou um nível ([level]); sem nenhum dos dois,
 * já vem liberada. O [id] é o mesmo que o desenho usa.
 */
data class FolhaItem(
    val id: String,
    val slot: FolhaSlot,
    val name: String,
    val rarity: FolhaRarity = FolhaRarity.COMUM,
    val badgeId: String? = null,
    val level: Int? = null,
    /** Recompensa exclusiva de uma coleção (sai ao completar as peças dela). */
    val collection: String? = null,
)

/**
 * O armário do Folha: doze categorias de peças que a pessoa libera estudando. Cada peça sai de uma
 * conquista de verdade (todos os emblemas liberam alguma coisa, e os níveis também), então nada é
 * comprado nem ganho por engano, e quem troca de aparelho recupera tudo junto com o histórico.
 */
object FolhaWardrobe {
    private fun item(id: String, slot: FolhaSlot, name: String, rarity: FolhaRarity = FolhaRarity.COMUM, badge: String? = null, level: Int? = null, collection: String? = null) =
        FolhaItem(id, slot, name, rarity, badge, level, collection)

    private val C = FolhaRarity.COMUM
    private val R = FolhaRarity.RARA
    private val E = FolhaRarity.EPICA
    private val L = FolhaRarity.LENDARIA

    val items: List<FolhaItem> = listOf(
        // o próprio Folha: escolhas livres, sem conquista
        item("lisa", FolhaSlot.PAGINAS, "Lisas"),
        item("pautada", FolhaSlot.PAGINAS, "Pautadas"),
        item("quadriculada", FolhaSlot.PAGINAS, "Quadriculadas"),
        item("pontilhada", FolhaSlot.PAGINAS, "Pontilhadas"),
        item("partitura", FolhaSlot.PAGINAS, "Partitura"),
        item("marinho", FolhaSlot.TINTA, "Azul-marinho"),
        item("grafite", FolhaSlot.TINTA, "Grafite"),
        item("ameixa", FolhaSlot.TINTA, "Ameixa"),
        item("cafe", FolhaSlot.TINTA, "Café"),
        item("floresta", FolhaSlot.TINTA, "Floresta"),
        item("rosa", FolhaSlot.BOCHECHAS, "Rosadas"),
        item("pessego", FolhaSlot.BOCHECHAS, "Pêssego"),
        item("lilas", FolhaSlot.BOCHECHAS, "Lilás"),
        item("coral", FolhaSlot.BOCHECHAS, "Coral"),
        item("nenhuma", FolhaSlot.BOCHECHAS, "Sem rubor"),

        // recompensas de coleção (exclusivas)
        item("tweed", FolhaSlot.CAPA, "Tweed de detetive", E, collection = "detetive"),
        item("runas", FolhaSlot.AURA, "Runas mágicas", L, collection = "mago"),
        item("pasta", FolhaSlot.MAO, "Pasta executiva", E, collection = "executivo"),
        item("fogos", FolhaSlot.AURA, "Fogos de artifício", L, collection = "campeao"),
        item("real", FolhaSlot.CAPA, "Veludo real", L, collection = "realeza"),
        item("coelho", FolhaSlot.COMPANHEIRO, "Coelhinho", E, collection = "fofura"),

        // capas
        item("indigo", FolhaSlot.CAPA, "Índigo Estudário"),
        item("esmeralda", FolhaSlot.CAPA, "Esmeralda", C, badge = "meta-1"),
        item("bolinhas", FolhaSlot.CAPA, "Bolinhas", C, level = 3),
        item("rubi", FolhaSlot.CAPA, "Rubi", R, badge = "seq-7"),
        item("ametista", FolhaSlot.CAPA, "Ametista", R, level = 5),
        item("xadrez", FolhaSlot.CAPA, "Xadrez escocês", R, badge = "plano-50"),
        item("jeans", FolhaSlot.CAPA, "Jeans", R, badge = "revisao-25"),
        item("couro", FolhaSlot.CAPA, "Couro clássico", R, badge = "topico-50"),
        item("grafite", FolhaSlot.CAPA, "Grafite", R, badge = "quest-500"),
        item("noite", FolhaSlot.CAPA, "Noite estrelada", E, badge = "seq-30"),
        item("holografica", FolhaSlot.CAPA, "Holográfica", E, badge = "seq-100"),
        item("ouro", FolhaSlot.CAPA, "Edição de ouro", E, badge = "edital-100"),
        item("obsidiana", FolhaSlot.CAPA, "Obsidiana e ouro", L, badge = "plano-1000"),
        item("diamante", FolhaSlot.CAPA, "Diamante", L, badge = "seq-365"),

        // chapéus
        item("capelo", FolhaSlot.CHAPEU, "Capelo de formatura"),
        item("bone", FolhaSlot.CHAPEU, "Boné do Estudário", C, badge = "seq-3"),
        item("boina", FolhaSlot.CHAPEU, "Boina de artista", C, badge = "discursiva-5"),
        item("explorador", FolhaSlot.CHAPEU, "Chapéu de explorador", R, badge = "topico-150"),
        item("fones", FolhaSlot.CHAPEU, "Fones de foco", R, badge = "maratona-240"),
        item("louros", FolhaSlot.CHAPEU, "Capelo com louros", R, badge = "simulado-1"),
        item("cartola", FolhaSlot.CHAPEU, "Cartola de gala", E, badge = "plano-200"),
        item("mago", FolhaSlot.CHAPEU, "Chapéu de mago", E, badge = "revisao-100"),
        item("capelo-ouro", FolhaSlot.CHAPEU, "Capelo de ouro", E, badge = "edital-75"),
        item("coroa", FolhaSlot.CHAPEU, "Coroa", L, badge = "acerto-90"),
        item("aureola", FolhaSlot.CHAPEU, "Auréola", L, badge = "revisao-1000"),

        // óculos
        item("nenhum", FolhaSlot.OCULOS, "Sem óculos"),
        item("escuros", FolhaSlot.OCULOS, "Óculos escuros", C, level = 2),
        item("leitura", FolhaSlot.OCULOS, "Óculos de leitura", C, badge = "topico-10"),
        item("coracao", FolhaSlot.OCULOS, "Coração", R, badge = "meta-10"),
        item("nerd", FolhaSlot.OCULOS, "Armação grossa", R, badge = "edital-25"),
        item("estrela", FolhaSlot.OCULOS, "Estrela de festa", R, badge = "maratona-120"),
        item("monoculo", FolhaSlot.OCULOS, "Monóculo", E, badge = "revisao-400"),

        // peito
        item("nenhum", FolhaSlot.PESCOCO, "Nada"),
        item("flor", FolhaSlot.PESCOCO, "Flor na lapela", C, badge = "revisao-1"),
        item("borboleta", FolhaSlot.PESCOCO, "Gravata-borboleta", C, badge = "plano-10"),
        item("cachecol", FolhaSlot.PESCOCO, "Cachecol listrado", R, badge = "seq-14"),
        item("broche", FolhaSlot.PESCOCO, "Broche de estrela", R, level = 10),
        item("gravata", FolhaSlot.PESCOCO, "Gravata de executivo", E, badge = "simulado-20"),
        item("medalha", FolhaSlot.PESCOCO, "Medalha de ouro", E, badge = "meta-100"),
        item("roseta", FolhaSlot.PESCOCO, "Roseta de campeão", L, badge = "meta-250"),

        // na mão
        item("nenhum", FolhaSlot.MAO, "Mão livre"),
        item("lapis", FolhaSlot.MAO, "Lápis", C, badge = "acerto-70"),
        item("balao", FolhaSlot.MAO, "Balão", C, level = 4),
        item("livro", FolhaSlot.MAO, "Livrinho de bolso", R, badge = "edital-50"),
        item("marcatexto", FolhaSlot.MAO, "Marca-texto", R, badge = "quest-2000"),
        item("lupa", FolhaSlot.MAO, "Lupa de detetive", E, badge = "topico-400"),
        item("varinha", FolhaSlot.MAO, "Varinha mágica", E, badge = "maratona-360"),
        item("trofeu", FolhaSlot.MAO, "Troféu", L, badge = "simulado-50"),

        // aura
        item("nenhum", FolhaSlot.AURA, "Sem aura"),
        item("brilhos", FolhaSlot.AURA, "Brilhos", C, level = 6),
        item("coracoes", FolhaSlot.AURA, "Corações", R, badge = "discursiva-20"),
        item("notas", FolhaSlot.AURA, "Notas musicais", R, badge = "plano-500"),
        item("confete", FolhaSlot.AURA, "Confete", E, badge = "quest-5000"),
        item("orbita", FolhaSlot.AURA, "Estrelas em órbita", E, badge = "seq-60"),
        item("chamas", FolhaSlot.AURA, "Chamas", L, badge = "maratona-480"),
        item("galaxia", FolhaSlot.AURA, "Galáxia", L, badge = "quest-10000"),

        // marcador
        item("menta", FolhaSlot.FITA, "Menta"),
        item("ceu", FolhaSlot.FITA, "Céu", C, badge = "quest-100"),
        item("coral", FolhaSlot.FITA, "Coral", C, badge = "revisao-10"),
        item("lilas", FolhaSlot.FITA, "Lilás", C, badge = "discursiva-1"),
        item("preta", FolhaSlot.FITA, "Preta", R, badge = "simulado-5"),
        item("dourada", FolhaSlot.FITA, "Dourada", R, badge = "acerto-80"),
        item("arcoiris", FolhaSlot.FITA, "Arco-íris", E, badge = "meta-50"),

        // luvas
        item("branca", FolhaSlot.LUVAS, "Brancas"),
        item("menta", FolhaSlot.LUVAS, "Menta", C, level = 8),
        item("coral", FolhaSlot.LUVAS, "Coral", C, level = 12),
        item("preta", FolhaSlot.LUVAS, "Pretas", R, level = 15),
        item("dourada", FolhaSlot.LUVAS, "Douradas", E, level = 20),
        item("arcoiris", FolhaSlot.LUVAS, "Arco-íris", E, level = 28),
        item("boxe", FolhaSlot.LUVAS, "Luvas de boxe", L, level = 40),

        // companheiro
        item("nenhum", FolhaSlot.COMPANHEIRO, "Sozinho"),
        item("planta", FolhaSlot.COMPANHEIRO, "Plantinha", C, level = 7),
        item("patinho", FolhaSlot.COMPANHEIRO, "Patinho de borracha", C, level = 9),
        item("gatinho", FolhaSlot.COMPANHEIRO, "Gatinho", R, level = 14),
        item("coruja", FolhaSlot.COMPANHEIRO, "Coruja de capelo", R, level = 18),
        item("robo", FolhaSlot.COMPANHEIRO, "Robozinho", E, level = 24),
        item("dragao", FolhaSlot.COMPANHEIRO, "Dragãozinho", L, level = 35),
    )

    /** Coleções, da mais fácil de completar para a mais difícil. */
    val collections: List<FolhaCollection> = listOf(
        FolhaCollection("fofura", "Fofura", "Coração nos olhos, flor no peito e balão na mão.",
            listOf(FolhaSlot.CAPA to "bolinhas", FolhaSlot.OCULOS to "coracao", FolhaSlot.PESCOCO to "flor", FolhaSlot.MAO to "balao"), FolhaSlot.COMPANHEIRO to "coelho"),
        FolhaCollection("detetive", "Detetive", "Para investigar cada tópico do edital.",
            listOf(FolhaSlot.CHAPEU to "explorador", FolhaSlot.OCULOS to "leitura", FolhaSlot.MAO to "lupa", FolhaSlot.COMPANHEIRO to "coruja"), FolhaSlot.CAPA to "tweed"),
        FolhaCollection("campeao", "Campeão", "Boné, óculos escuros, medalha e troféu.",
            listOf(FolhaSlot.CHAPEU to "bone", FolhaSlot.OCULOS to "escuros", FolhaSlot.PESCOCO to "medalha", FolhaSlot.MAO to "trofeu"), FolhaSlot.AURA to "fogos"),
        FolhaCollection("executivo", "Executivo", "Cartola, monóculo, gravata e capa grafite.",
            listOf(FolhaSlot.CAPA to "grafite", FolhaSlot.CHAPEU to "cartola", FolhaSlot.OCULOS to "monoculo", FolhaSlot.PESCOCO to "gravata"), FolhaSlot.MAO to "pasta"),
        FolhaCollection("mago", "Mago", "Chapéu, varinha, óculos de estrela e o dragãozinho.",
            listOf(FolhaSlot.CHAPEU to "mago", FolhaSlot.OCULOS to "estrela", FolhaSlot.MAO to "varinha", FolhaSlot.COMPANHEIRO to "dragao"), FolhaSlot.AURA to "runas"),
        FolhaCollection("realeza", "Realeza", "Coroa, roseta, luvas e marcador de ouro.",
            listOf(FolhaSlot.CHAPEU to "coroa", FolhaSlot.PESCOCO to "roseta", FolhaSlot.LUVAS to "dourada", FolhaSlot.FITA to "dourada"), FolhaSlot.CAPA to "real"),
    )

    fun collection(id: String?): FolhaCollection? = collections.firstOrNull { it.id == id }
    fun piecesOf(c: FolhaCollection): List<FolhaItem> = c.pieces.mapNotNull { (slot, id) -> find(slot, id) }
    fun rewardOf(c: FolhaCollection): FolhaItem? = find(c.reward.first, c.reward.second)

    fun of(slot: FolhaSlot): List<FolhaItem> = items.filter { it.slot == slot }
    /** A peça padrão de cada lugar (a primeira não exclusiva). */
    fun default(slot: FolhaSlot): FolhaItem = of(slot).first { it.collection == null && it.badgeId == null && it.level == null }
    fun find(slot: FolhaSlot, id: String?): FolhaItem? = items.firstOrNull { it.slot == slot && it.id == id }

    fun unlocked(item: FolhaItem, summary: ProgressEngine.ProgressSummary): Boolean = when {
        item.collection != null -> collection(item.collection)?.let { c -> piecesOf(c).all { unlocked(it, summary) } } ?: false
        item.badgeId != null -> summary.badges.any { it.badge.id == item.badgeId && it.earned }
        item.level != null -> summary.level >= item.level
        else -> true
    }

    /** Como liberar, em uma linha ("Emblema Semana cheia: 7 dias seguidos batendo a meta", "Chegar ao nível 5"). */
    fun requirement(item: FolhaItem): String = when {
        item.collection != null -> "Complete a coleção ${collection(item.collection)?.name ?: ""}"
        item.badgeId != null -> BadgeCatalog.all.firstOrNull { it.id == item.badgeId }?.let { "Emblema ${it.name}: ${it.requirement.replaceFirstChar { c -> c.lowercaseChar() }}" } ?: "Emblema"
        item.level != null -> "Chegar ao nível ${item.level}"
        item.slot.group == FolhaGroup.SEU_FOLHA && item.slot != FolhaSlot.CAPA && item.slot != FolhaSlot.FITA && item.slot != FolhaSlot.LUVAS -> "Livre para escolher"
        else -> "Já vem com o Folha"
    }

    /** O andamento até liberar: atual, alvo e a unidade. Nulo quando já vem liberada. */
    fun progress(item: FolhaItem, summary: ProgressEngine.ProgressSummary): Triple<Int, Int, String>? {
        item.collection?.let { id -> val c = collection(id) ?: return null; val ps = piecesOf(c); return Triple(ps.count { unlocked(it, summary) }, ps.size, "peças") }
        item.level?.let { return Triple(summary.level.coerceAtMost(it), it, "nível") }
        val p = summary.badges.firstOrNull { it.badge.id == item.badgeId } ?: return null
        return Triple(p.current.coerceAtMost(p.target), p.target, p.badge.unit)
    }

    /** A escolha salva, mas só se ainda vale (peça existe nesse lugar e está liberada). */
    fun resolve(slot: FolhaSlot, chosen: String?, summary: ProgressEngine.ProgressSummary): FolhaItem =
        find(slot, chosen)?.takeIf { unlocked(it, summary) } ?: default(slot)

    /** A peça trancada mais perto de sair (para mostrar "próxima peça"). */
    fun next(summary: ProgressEngine.ProgressSummary): Pair<FolhaItem, Float>? = items.filterNot { unlocked(it, summary) }
        .mapNotNull { item -> progress(item, summary)?.let { (cur, target, _) -> item to (cur.toFloat() / target.coerceAtLeast(1)) } }
        .maxByOrNull { it.second }

    /** Peças que o emblema libera (para a comemoração e a tela de conquistas). */
    fun unlockedBy(badgeId: String): List<FolhaItem> = items.filter { it.badgeId == badgeId }

    /** Peças que o nível libera. */
    fun unlockedAtLevel(level: Int): List<FolhaItem> = items.filter { it.level == level }

    /** Um visual sorteado entre as peças liberadas (o botão "Surpreenda-me"). */
    fun random(summary: ProgressEngine.ProgressSummary, random: kotlin.random.Random = kotlin.random.Random.Default): Map<FolhaSlot, FolhaItem> =
        FolhaSlot.entries.associateWith { slot -> of(slot).filter { unlocked(it, summary) }.random(random) }

    /** Escolhas antigas, de quando o armário tinha um "acessório" só. */
    fun legacyAccessory(id: String?): FolhaItem? = when (id) {
        "oculos" -> find(FolhaSlot.OCULOS, "leitura")
        "gravata" -> find(FolhaSlot.PESCOCO, "borboleta")
        "estrela" -> find(FolhaSlot.PESCOCO, "broche")
        "louros" -> find(FolhaSlot.CHAPEU, "louros")
        "lapis" -> find(FolhaSlot.MAO, "lapis")
        else -> null
    }
}
