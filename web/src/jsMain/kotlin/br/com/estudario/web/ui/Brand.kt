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
fun FolhaTalking(original: String, size: Int = 88, onFolhaClick: (() -> Unit)? = null, extra: String? = null, restMood: String = "idle") {
    // Tocando nele, a frase do toque entra neste balão por uns segundos (folha-live.js avisa pelo evento
    // "folha-quip"), em vez de abrir um segundo balão em cima; depois volta a frase principal.
    var quip by remember(original) { mutableStateOf<String?>(null) }
    LaunchedEffect(quip) { val q = quip ?: return@LaunchedEffect; delay(q.length * 45L + 2600L); quip = null }
    val text = quip ?: original
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
    Div({
        classes(*listOfNotNull("folha-talk", extra).toTypedArray())
        addEventListener("folha-quip") { e -> e.preventDefault(); quip = (e.nativeEvent.asDynamic().detail as? String) }
    }) {
        Div({
            classes("folha-talk-avatar")
            onFolhaClick?.let { click -> onClick { click() } }
        }) { key(size) { Folha(if (talking) "talking" else restMood, size, say = if (talking) text else null) } }
        Div({ classes("talk-bubble"); attr("aria-live", "polite") }) {
            Span({ classes("ghost"); attr("aria-hidden", "true") }) { Text(text) }
            Span({ classes("live") }) {
                Text(text.take(shown))
                if (talking) Span({ classes("caret") })
            }
        }
    }
}

/** O que o Folha fala: o mesmo banco do app (FolhaFalas, no módulo compartilhado), com a mesma escolha por semente. */
object FolhaLines {
    private val F = br.com.estudario.domain.FolhaFalas
    fun forHome(continuing: Boolean, needsMaterial: Boolean, hour: Int, seed: Long, daysToExam: Long? = null): String =
        F.home(continuing, needsMaterial, hour, seed, ((Date().getDay() + 6) % 7) + 1, daysToExam)
    /** Dia sem tarefa pendente: a missão acabou ou é folga. */
    fun forDayDone(seed: Long): String = F.dayDone(seed)
    /** O Folha no perfil: comenta o caminho da pessoa, nunca do mesmo jeito. */
    fun forProfile(level: Int, streak: Int, xpToday: Int, seed: Long): String = F.profile(level, streak, xpToday, seed)
    fun forResult(percent: Int, seed: Long): String = F.result(percent, seed)
    /** Desempenho: o Folha lê o acerto do período com a pessoa. */
    fun forStats(accuracy: Int?, seed: Long): String = F.stats(accuracy, seed)
    fun forAnswer(correct: Boolean, seed: Long): String = F.answer(correct, seed)
    fun forRelief(seed: Long): String = F.relief(seed)
    fun forFire(seed: Long): String = F.fire(seed)
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
    /** A cena da oficina: plan, material, questions ou edital. */
    scene: String = "material",
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
    Div({ classes("process-view", "ws-view") }) {
        Div({ classes("process-head") }) {
            if (eyebrow != null) Span({ classes("eyebrow") }) { Text(eyebrow.uppercase()) }
            // O marca-texto atrás do título anda com o progresso.
            H2 { Span({ classes("ws-title"); attr("style", "--p:${(progress * 100).toInt()}%") }) { Text(title) } }
        }
        Workshop(scene)
        Div({ classes("ws-bottom") }) {
            val stage = detail ?: stages.getOrNull(current.coerceAtMost(stages.lastIndex))
            Div({ classes("ws-say") }) {
                if (stage != null) key(stage) { Span({ classes("ws-say-text") }) { Text("$stage…") } }
                Span({ classes("ws-time") }) { Text("${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}") }
            }
            // A lista de tarefas do Folha, escrita à mão: o que já foi feito fica riscado.
            if (stages.size > 1) org.jetbrains.compose.web.dom.Ul({ classes("ws-list") }) {
                stages.forEachIndexed { i, s ->
                    org.jetbrains.compose.web.dom.Li({ classes(*listOfNotNull(when { i < current -> "done"; i == current -> "now"; else -> null }).toTypedArray()) }) {
                        Span({ classes("ws-box") }) { if (i < current) Icon("check", plain = true) else if (i == current) Icon("Pencil") }
                        Span({ classes("ws-txt") }) { Text(s) }
                    }
                }
            } else Div({ classes("process-bar") }) { Span({ attr("style", "width:${(progress * 100).toInt()}%") }) }
        }
        footer?.invoke()
    }
}

/** A oficina do Folha (workshop.js): a cena do trabalho de cada tipo de geração. */
@Composable
fun Workshop(kind: String) {
    key(kind) {
        TagElement<org.w3c.dom.HTMLElement>(
            elementBuilder = ElementBuilder.createBuilder("estudario-workshop"),
            applyAttrs = { attr("kind", kind) },
            content = null,
        )
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

/** Peso da matéria na prova (frase lida no quadro do edital), quando o edital informa. */
fun subjectExamWeight(data: Snapshot, subjectId: Long): String? =
    (data.array(br.com.estudario.web.data.Keys.SUBJECTS).firstOrNull { (it as? kotlinx.serialization.json.JsonObject)?.get("id")?.toString() == subjectId.toString() } as? kotlinx.serialization.json.JsonObject)
        ?.get("assessedPriorityEvidenceJson")?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content }
        .let { br.com.estudario.domain.ExamWeights.descriptionOfJson(it) }
