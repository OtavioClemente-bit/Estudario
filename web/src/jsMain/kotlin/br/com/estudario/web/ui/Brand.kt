package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.PriorityResolver
import br.com.estudario.web.data.Snapshot
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date
import kotlin.math.exp

// ------------------------------------------------------------------ o Folha falando

/**
 * O Folha conversando (FolhaSays/FolhaTalking do app): o texto aparece no balão como se ele
 * estivesse falando, com pausas nas vírgulas e nos pontos, e a boca acompanha. Terminada a fala,
 * ele fica ouvindo. O texto inteiro, invisível, reserva a altura do balão para nada pular.
 */
@Composable
fun FolhaTalking(text: String, size: Int = 88, onFolhaClick: (() -> Unit)? = null, extra: String? = null) {
    var shown by remember(text) { mutableStateOf(0) }
    LaunchedEffect(text) {
        shown = 0
        while (shown < text.length) {
            val ch = text[shown]
            shown++
            delay(
                when (ch) {
                    '.', '!', '?', ':' -> 220L
                    ',' -> 110L
                    ' ' -> 18L
                    else -> 16L
                },
            )
        }
    }
    val talking = shown < text.length
    Div({ classes(*listOfNotNull("folha-talk", extra).toTypedArray()) }) {
        Div({
            classes("folha-talk-avatar")
            onFolhaClick?.let { click -> onClick { click() } }
        }) { key(size) { Folha(if (talking) "talking" else "idle", size) } }
        Div({ classes("talk-bubble"); attr("aria-live", "polite") }) {
            Span({ classes("ghost"); attr("aria-hidden", "true") }) { Text(text) }
            Span({ classes("live") }) {
                Text(text.take(shown))
                if (talking) Span({ classes("caret") })
            }
        }
    }
}

/** O que o Folha fala: FolhaLines.kt do app, com a mesma escolha por semente. */
object FolhaLines {
    private fun <T> List<T>.pick(seed: Long): T = this[((seed % size) + size).toInt() % size]

    private val homeStart = listOf(
        "Bora? Seu próximo estudo está aqui.",
        "Quem estuda hoje não corre atrás amanhã.",
        "Um tópico de cada vez. É assim que o edital acaba.",
        "A vaga não tem nome ainda. Vamos colocar o seu?",
        "Constância vence talento que não aparece.",
        "Hoje é um bom dia para ficar mais perto da posse.",
        "Pouco todo dia rende mais que muito de vez em quando.",
        "Abre o tópico comigo? Eu seguro a página.",
        "O concorrente também está estudando. Bora na frente?",
        "Cada tópico fechado é um ponto a mais na prova.",
        "Disciplina é lembrar do que você quer, mesmo cansado.",
        "Sua aprovação está sendo construída agora.",
        "Edital grande se vence assim: começando.",
        "Café, foco e eu. Partiu?",
        "Dez minutos já contam. Começa e vê o tempo passar.",
        "A prova não pergunta se você estava com vontade.",
        "Seu eu do futuro vai agradecer por esse estudo.",
        "Bora transformar esse tópico em acerto na prova?",
    )
    private val homeContinue = listOf(
        "Bora terminar o que começamos?",
        "Você parou no meio. Eu guardei a página.",
        "Falta pouco para fechar esse tópico.",
        "Começou, agora termina. É assim que se passa.",
        "Voltou! Vamos de onde você parou.",
        "Esse tópico está quase seu. Só mais um pouco.",
    )
    private val homeGenerate = listOf(
        "Vamos preparar o material deste tópico?",
        "Eu escrevo a teoria, você só estuda. Topa?",
        "Esse tópico ainda está em branco. Bora preencher?",
        "Um clique e eu monto teoria, flashcards e questões.",
        "Deixa comigo: preparo tudo do jeito da sua banca.",
    )
    private val morning = listOf("Bom dia! Cabeça descansada aprende mais rápido.", "Começar cedo é sair na frente. Bora?")
    private val night = listOf("Estudo da noite também conta. Bora fechar o dia bem?", "Um último tópico antes de dormir? A memória agradece.")

    fun forHome(continuing: Boolean, needsMaterial: Boolean, hour: Int, seed: Long): String = when {
        needsMaterial -> homeGenerate.pick(seed)
        continuing -> homeContinue.pick(seed)
        hour in 5..9 && seed % 4 == 0L -> morning.pick(seed / 4)
        (hour >= 21 || hour < 2) && seed % 4 == 0L -> night.pick(seed / 4)
        else -> homeStart.pick(seed)
    }

    /** Dia sem tarefa pendente: a missão acabou ou é folga. */
    fun forDayDone(seed: Long): String = listOf(
        "Missão cumprida! Descansa, que amanhã a gente continua.",
        "Dia fechado. É assim, um dia de cada vez, que se chega na posse.",
        "Tudo feito por hoje. Tô orgulhoso de você!",
        "Plano do dia concluído. A sequência está garantida.",
    ).pick(seed)

    /** O Folha no perfil: comenta o caminho da pessoa, nunca do mesmo jeito. */
    fun forProfile(level: Int, streak: Int, xpToday: Int, seed: Long): String {
        val options = buildList {
            if (streak >= 2) {
                add("$streak dias seguidos! Constância vence talento.")
                add("$streak dias sem falhar. Essa sequência é sua, não deixa ela cair!")
            } else add("Bora começar uma sequência hoje? O primeiro dia é o mais importante.")
            if (xpToday > 0) add("+$xpToday XP hoje. Cada ponto é um passo mais perto da posse.")
            else add("Ainda sem XP hoje. Um tópico já muda isso.")
            add("Nível $level! Quem chegou até aqui não para no meio do caminho.")
            add("Cada emblema aqui foi conquistado de verdade. Bora pelo próximo?")
            add("Seu histórico conta a história da sua aprovação. E ela está ficando boa.")
            add("Eu guardo tudo o que você estudou. Você só precisa continuar.")
        }
        return options.pick(seed)
    }

    fun forResult(percent: Int, seed: Long): String = when {
        percent >= 90 -> listOf("Que bateria! Com esse desempenho, a aprovação é questão de tempo.", "Impressionante. Você está voando nesse conteúdo!").pick(seed)
        percent >= 70 -> listOf("Muito bom! Acima de 70% é ritmo de aprovado.", "Mandou bem! Os erros já foram pro caderno pra gente revisar.").pick(seed)
        percent >= 50 -> listOf("Bom caminho! Revisando os erros, a próxima sai bem melhor.", "Metade já está dominada. Bora atacar o resto juntos?").pick(seed)
        else -> listOf("Dia difícil acontece. O importante é que você treinou, e cada erro virou aprendizado.", "Não desanima! Os erros estão guardados e vão voltar na hora certa pra você fixar.").pick(seed)
    }

    /** Desempenho: o Folha lê o acerto do período com a pessoa. */
    fun forStats(accuracy: Int?, seed: Long): String = when {
        accuracy == null -> "Resolve umas questões comigo? Aí eu te mostro onde você está forte e onde reforçar."
        accuracy >= 70 -> listOf("Acerto de aprovado! Agora é manter o ritmo.", "Acima de 70%. A banca que se prepare!").pick(seed)
        accuracy >= 50 -> listOf("No caminho certo. Revisando os pontos fracos, isso sobe rápido.", "Metade do caminho andado. Bora reforçar as matérias em vermelho?").pick(seed)
        else -> listOf("Base em construção. Cada erro agora é um acerto no dia da prova.", "Calma: errar treinando é o melhor jeito de não errar na prova.").pick(seed)
    }
}

/** Semente que muda a cada vez que a tela abre, como no app (segundos desde 1970). */
@Composable
fun rememberSeed(): Long = remember { (Date.now() / 1000).toLong() }

// ------------------------------------------------------------------ espera "trabalhando"

/** A cena do processamento (process.js): Folha lendo no meio, folhas orbitando e galáxia atrás. */
@Composable
fun ProcessScene(size: Int = 300) {
    TagElement<org.w3c.dom.HTMLElement>(
        elementBuilder = ElementBuilder.createBuilder("estudario-process"),
        applyAttrs = { classes("process-scene"); attr("size", size.toString()) },
        content = null,
    )
}

/**
 * Tela de processamento (EstudarioProcessView do app): título, a cena, o balão em que o Folha conta
 * o que está fazendo, a trilha das etapas e o tempo. As etapas andam pelo tempo ([stageMillis]) e a
 * última fica ativa até o trabalho terminar; a barra nunca chega a 100% sozinha.
 */
@Composable
fun ProcessView(
    title: String,
    stages: List<String>,
    eyebrow: String? = null,
    stageMillis: Long = 5_500L,
    stageIndex: Int? = null,
    /** Primeira etapa contada pelo tempo, quando as anteriores já passaram de verdade. */
    stageOffset: Int = 0,
    detail: String? = null,
    sceneSize: Int = 300,
    footer: (@Composable () -> Unit)? = null,
) {
    var elapsed by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        val start = Date.now()
        while (true) { delay(250); elapsed = (Date.now() - start).toLong() }
    }
    var offsetStart by remember(stageOffset, stageIndex == null) { mutableStateOf(-1L) }
    if (stageIndex == null && offsetStart < 0) offsetStart = elapsed
    val timed = ((elapsed - offsetStart.coerceAtLeast(0)) / stageMillis).toInt()
    val current = stageIndex?.coerceIn(0, stages.size) ?: if (stages.isEmpty()) 0 else minOf(stageOffset + timed, stages.lastIndex)
    val expected = (stageMillis * maxOf(stages.size, 1)).toDouble()
    val progress = (0.94 * (1 - exp(-elapsed / (expected * 0.55)))).coerceIn(0.03, 0.94)
    val seconds = elapsed / 1000
    Div({ classes("process-view") }) {
        Div({ classes("process-head") }) {
            if (eyebrow != null) Span({ classes("eyebrow") }) { Text(eyebrow.uppercase()) }
            H2 { Text(title) }
        }
        ProcessScene(sceneSize)
        Div({ classes("process-speech") }) {
            val stage = detail ?: stages.getOrNull(current.coerceAtMost(stages.lastIndex))
            if (stage != null) key(stage) { Div({ classes("stage") }) { Text("$stage…") } }
            Div({ classes("meta") }) {
                Text("${if (stages.size > 1) "Etapa ${minOf(current + 1, stages.size)} de ${stages.size} · " else ""}${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}")
            }
        }
        if (stages.size > 1) StageTrail(stages.size, current, progress)
        else Div({ classes("process-bar") }) { Span({ attr("style", "width:${(progress * 100).toInt()}%") }) }
        footer?.invoke()
    }
}

/** A trilha das etapas: feita tem o certo, a atual pulsa, as próximas esperam em cinza. */
@Composable
private fun StageTrail(count: Int, current: Int, progress: Double) {
    val within = ((progress * count) - current).coerceIn(0.0, 0.85)
    val reach = if (count <= 1) 100.0 else ((current + if (current >= count - 1) 0.0 else within) / (count - 1) * 100).coerceIn(0.0, 100.0)
    Div({ classes("stage-trail"); attr("role", "progressbar"); attr("aria-valuenow", "${(progress * 100).toInt()}") }) {
        Div({ classes("rail") }) { Span({ attr("style", "width:$reach%") }) }
        (0 until count).forEach { i ->
            Span({
                classes(*listOfNotNull("node", when { i < current -> "done"; i == current -> "now"; else -> null }).toTypedArray())
                attr("style", "left:${if (count <= 1) 50.0 else i * 100.0 / (count - 1)}%")
            }) { if (i < current) Icon("check", plain = true) }
        }
    }
}

// ------------------------------------------------------------------ atenção (prova + dificuldade)

/** Attention.kt do app: quanto um assunto pede de atenção, em cor fixa nos dois temas. */
enum class Attention(val label: String, val short: String, val color: String) {
    HIGH("Atenção alta", "alta", "#E5484D"),
    MEDIUM("Atenção média", "média", "#F2A900"),
    LOW("Atenção leve", "leve", "#2FA36B"),
    ;

    companion object {
        fun of(priority: PriorityLevel, difficulty: String? = null): Attention {
            val weight = when (priority) {
                PriorityLevel.VERY_HIGH -> 2.0
                PriorityLevel.HIGH -> 1.5
                PriorityLevel.MEDIUM -> 1.0
                PriorityLevel.LOW -> 0.5
                PriorityLevel.VERY_LOW -> 0.0
            }
            val normalized = when (difficulty) {
                "VERY_EASY" -> 0.0; "EASY" -> 0.25; "HARD" -> 0.75; "VERY_HARD" -> 1.0; else -> 0.5
            }
            val score = weight + (normalized - 0.5) * 2
            return when {
                score >= 1.5 -> HIGH
                score >= 0.75 -> MEDIUM
                else -> LOW
            }
        }
    }
}

private fun level(override: String?, assessed: Boolean, score: Int, parent: PriorityLevel?): PriorityLevel =
    override?.let { o -> PriorityLevel.entries.firstOrNull { it.name == o } }
        ?: PriorityResolver.scoreToLevel(score).takeIf { assessed }
        ?: parent
        ?: PriorityLevel.MEDIUM

fun competitionPriority(data: Snapshot, competitionId: Long?): PriorityLevel? =
    data.competitions.firstOrNull { it.id == competitionId }?.let { level(it.userPriorityOverride, it.hasAssessedPriority, it.assessedPriorityScore, null) }

fun subjectPriority(subject: br.com.estudario.web.data.Subject, parent: PriorityLevel?): PriorityLevel =
    level(subject.userPriorityOverride, subject.hasAssessedPriority, subject.assessedPriorityScore, parent)

fun topicPriority(topic: br.com.estudario.web.data.Topic, parent: PriorityLevel): PriorityLevel =
    level(topic.userPriorityOverride, topic.hasAssessedPriority, topic.assessedPriorityScore, parent)

/** A dificuldade que a pessoa disse ter na matéria (assistente do plano). */
fun subjectDifficulty(data: Snapshot, subjectId: Long): String? {
    val plan = br.com.estudario.web.data.Queries.activePlan(data)
    return data.planSubjects.firstOrNull { it.subjectId == subjectId && (plan == null || it.planId == plan.id) }?.personalDifficulty
}

fun priorityLabel(level: PriorityLevel): String = when (level) {
    PriorityLevel.VERY_HIGH -> "Muito alta"
    PriorityLevel.HIGH -> "Alta"
    PriorityLevel.MEDIUM -> "Média"
    PriorityLevel.LOW -> "Baixa"
    PriorityLevel.VERY_LOW -> "Muito baixa"
}

// ------------------------------------------------------------------ cores de matéria e de acerto

/** subjectAccentColor do app: cada matéria sempre na mesma cor da paleta, pelo hash do nome. */
fun subjectColor(name: String): String {
    val key = name.trim().lowercase()
    var hash = 7
    key.forEach { hash = hash * 31 + it.code }
    val size = 8
    val index = ((hash % size) + size) % size
    return "var(--subject-$index)"
}

/** Escala de acerto da tela Desempenho: vermelho abaixo de 50%, âmbar até 70%, verde a partir daí. */
fun accuracyColor(percent: Int?): String = when {
    percent == null -> "#9AA0A6"
    percent < 50 -> "#E5484D"
    percent < 70 -> "#F2A900"
    else -> "#2FA36B"
}
