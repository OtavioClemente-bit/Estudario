package br.com.estudario.ui.setup

import br.com.estudario.time.monthValue
import kotlinx.datetime.toKotlinLocalDate
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.estudarioLayout
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.FeasibilityOption
import br.com.estudario.domain.planner.FeasibilityVerdict
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanningExplanationBuilder
import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.formatAvailabilityMinutes
import java.time.LocalDate

/**
 * Os passos do assistente que lidam com os três eixos e com o resumo.
 *
 * O princípio que organiza estas telas: **a pessoa confere, não preenche**. Toda matéria já chega
 * com um valor válido - a prioridade que o edital indicou, esforço normal e nenhum conhecimento
 * prévio declarado - e o assistente só pede que ela corrija o que discordar. Por isso nenhum destes
 * passos bloqueia o avanço, e nenhum deles abre uma página por matéria.
 */

/**
 * As matérias numa tela só: peso na prova, esforço e quanto a pessoa já sabe.
 *
 * Antes eram duas telas com uma fileira de cinco botões de texto que rolava para o lado - em
 * celular estreito "Muito alta" ficava fora da tela e muita gente nem via que existia. Agora cada eixo
 * é um medidor de cinco barrinhas que sempre cabe na largura, com o nível escrito em destaque ao lado.
 */
@Composable
internal fun SubjectsStep(
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    snapshot: InitialSetupSnapshot,
    officialPriorities: Map<Long, PlanPriority>,
    onPriority: (String, ExamPriority) -> Unit,
    onResetPriority: (String) -> Unit,
    onDifficulty: (String, PersonalDifficulty) -> Unit,
    onKnowledge: (String, InitialKnowledge) -> Unit,
    onContinue: () -> Unit,
) {
    val topicsBySubject = remember(topics) { topics.groupBy { it.subjectId } }
    val ids = remember(subjects) { subjects.mapTo(hashSetOf()) { it.id.toString() } }
    val changed = subjects.count { it.id.toString() in snapshot.subjectPriorities } + snapshot.tunedSubjectCount(ids)

    WizardPage(
        eyebrow = "Suas matérias",
        question = "Ajuste só o que for diferente para você",
        aside = "Já deixei tudo preenchido com o que o edital indica. Toque nas barrinhas para mudar.",
        icon = Icons.Outlined.Tune,
        showScrollIndicator = true,
        bottom = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    if (changed == 0) "Pode seguir sem mexer em nada." else "Ajustes salvos. Dá para mudar depois na aba Plano.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                SetupPrimaryButton("Continuar", onContinue)
            }
        },
    ) {
        if (subjects.isEmpty()) {
            SetupCard { Text("Volte à revisão do edital para adicionar suas matérias.") }
            return@WizardPage
        }
        LevelLegend()
        subjects.forEach { subject ->
            val key = subject.id.toString()
            val suggested = SetupPlannerPreviewFactory.suggestedPriority(officialPriorities, subject.id)
            val priority = snapshot.subjectPriorities[key] ?: suggested
            val edited = key in snapshot.subjectPriorities
            val difficulty = snapshot.subjectDifficulties[key] ?: PersonalDifficulty.DEFAULT
            val knowledge = snapshot.subjectKnowledge[key] ?: InitialKnowledge.DEFAULT
            SetupCard {
                Text(subject.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "${topicsBySubject[subject.id]?.size ?: 0} tópico(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LevelMeter(
                    title = "Peso na prova",
                    options = ExamPriority.entries,
                    selected = priority,
                    label = { it.label },
                    color = Color(0xFF5B4BF5),
                    testTagPrefix = "priority_${subject.id}",
                ) { chosen -> if (chosen == suggested) onResetPriority(key) else onPriority(key, chosen) }
                if (edited) {
                    TextButton(onClick = { onResetPriority(key) }, modifier = Modifier.align(Alignment.End)) {
                        Text("Voltar ao do edital (${suggested.label.lowercase()})", style = MaterialTheme.typography.labelMedium)
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 2.dp))
                LevelMeter(
                    title = "Esforço para você",
                    options = PersonalDifficulty.entries,
                    selected = difficulty,
                    label = { it.label },
                    color = Color(0xFFFF7A45),
                    testTagPrefix = "difficulty_${subject.id}",
                ) { onDifficulty(key, it) }
                HorizontalDivider(Modifier.padding(vertical = 2.dp))
                LevelMeter(
                    title = "O quanto você já sabe",
                    options = InitialKnowledge.entries,
                    selected = knowledge,
                    label = { it.label },
                    color = Color(0xFF1FB574),
                    testTagPrefix = "knowledge_${subject.id}",
                ) { onKnowledge(key, it) }
                // O que essa resposta faz no plano, para a escolha ter peso de verdade.
                Text(
                    br.com.estudario.domain.planner.StudyPlanBlueprint.knowledgeEffect(knowledge, difficulty),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** O que cada medidor quer dizer, uma vez só no topo (em vez de repetir em cada matéria). */
@Composable
private fun LevelLegend() {
    SetupCard {
        Text("Como ler", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        listOf(
            Color(0xFF5B4BF5) to "Peso na prova: quanto espaço a matéria ocupa no plano.",
            Color(0xFFFF7A45) to "Esforço: o quanto ela costuma dar trabalho para você.",
            Color(0xFF1FB574) to "O quanto já sabe: matérias que você domina vão mais para revisão e questões.",
        ).forEach { (color, text) ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.size(width = 14.dp, height = 10.dp).background(color, RoundedCornerShape(3.dp)))
                Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * Medidor de cinco níveis: barrinhas crescentes que ocupam a largura toda (nunca cortam) e o nível
 * escrito ao lado do título. Tocar numa barrinha escolhe aquele nível.
 */
@Composable
internal fun <T> LevelMeter(
    title: String,
    options: List<T>,
    selected: T,
    label: (T) -> String,
    color: Color,
    testTagPrefix: String,
    onSelect: (T) -> Unit,
) {
    val index = options.indexOf(selected).coerceAtLeast(0)
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                label(selected),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .background(color.copy(alpha = 0.14f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            )
        }
        Row(
            Modifier.fillMaxWidth().height(34.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            options.forEachIndexed { i, option ->
                val on = i <= index
                val fill by animateColorAsState(if (on) color else MaterialTheme.colorScheme.surfaceVariant, label = "nivel")
                val shape = RoundedCornerShape(7.dp)
                Box(
                    Modifier
                        .weight(1f)
                        .height((14 + i * 5).dp)
                        .background(fill, shape)
                        .then(if (i == index) Modifier.border(2.dp, color.copy(alpha = 0.55f), shape) else Modifier)
                        .clickable(role = Role.RadioButton) { onSelect(option) }
                        .semantics { contentDescription = "$title: ${label(option)}"; this.selected = i == index }
                        .testTag("${testTagPrefix}_${(option as? Enum<*>)?.name ?: option.toString()}"),
                )
            }
        }
        Row(Modifier.fillMaxWidth()) {
            Text(label(options.first()), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
            Text(label(options.last()), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
        }
    }
}

/**
 * O resumo antes de montar.
 *
 * Roda o cálculo de verdade - necessidade por matéria, carga restante e viabilidade - e mostra o
 * resultado. Quando o tempo não fecha, ele **diz** e oferece as opções que o motor calculou; ele
 * não altera a carga da pessoa por conta própria. "Montar meu plano" monta direto daqui.
 */
@Composable
internal fun PlanSummaryStep(
    snapshot: InitialSetupSnapshot,
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    officialPriorities: Map<Long, PlanPriority>,
    building: Boolean,
    error: String?,
    onBuild: () -> Unit,
    onReviewAvailability: () -> Unit,
) {
    var completedStages by remember(snapshot, subjects, topics) { mutableStateOf(0) }
    val today = remember { LocalDate.now() }
    val computation = produceWizardResult(snapshot, subjects, topics, officialPriorities, minMillis = WIZARD_THINKING_MS + 500L) {
        SetupPlannerPreviewFactory.build(
            snapshot = snapshot,
            subjects = subjects,
            topics = topics,
            officialPriorities = officialPriorities,
            today = today,
            onStage = { completedStages = it },
        )
    }

    when (computation) {
        is WizardComputation.Running -> WizardProcessing(
            title = "Entendendo suas prioridades",
            stages = SetupPlannerPreviewFactory.PREVIEW_STAGES,
            completed = completedStages,
        )
        is WizardComputation.Ready -> {
            val preview = computation.value
            WizardPage(
                eyebrow = "Seu perfil de estudo",
                question = "É assim que eu entendi sua preparação.",
                aside = "Confira e monte. Tudo aqui pode mudar depois, sem refazer o plano do zero.",
                icon = Icons.Outlined.Insights,
                showScrollIndicator = true,
                bottom = {
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (error != null) Text(error, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                        SetupPrimaryButton(if (building) "Montando seu plano…" else "Montar meu plano", onBuild, enabled = !building)
                    }
                },
            ) {
                SetupCard {
                    PlanningExplanationBuilder.profileSummary(
                        weeklyMinutes = preview.weeklyMinutes,
                        sessionMinutes = preview.sessionMinutes,
                        examDate = preview.examDate?.toKotlinLocalDate(),
                        today = today.toKotlinLocalDate(),
                        topPrioritySubject = preview.topPriority?.name,
                        hardestSubject = preview.hardest?.name,
                    ).forEach { (label, value) -> SummaryRow(label, value) }
                    SummaryRow("Estratégia", snapshot.studyProfile.label)
                    SummaryRow("Alternância", snapshot.variety.label)
                }

                FeasibilityCard(preview, onReviewAvailability)

                if (preview.highlights().isNotEmpty()) {
                    SetupCard {
                        Text("Como vou montar isso", fontWeight = FontWeight.SemiBold)
                        preview.highlights().forEach { line ->
                            Text(
                                line,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

/** O aviso de tempo, com a conta aberta e as escolhas nas mãos da pessoa. */
@Composable
private fun FeasibilityCard(preview: SetupPlannerPreview, onReviewAvailability: () -> Unit) {
    SetupCard {
        Text(
            when (preview.feasibility.verdict) {
                FeasibilityVerdict.COMFORTABLE -> "Seu tempo está confortável"
                FeasibilityVerdict.TIGHT -> "Sua agenda está apertada para este edital"
                FeasibilityVerdict.INFEASIBLE -> "O edital inteiro não cabe antes da prova"
                FeasibilityVerdict.NO_EXAM_DATE -> "Sem data de prova definida"
            },
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            preview.feasibilityMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (preview.feasibility.options.isNotEmpty()) {
            Text(
                "Você escolhe:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            preview.feasibility.options.forEach { option ->
                Text(
                    "• " + PlanningExplanationBuilder.describeOption(option),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (preview.feasibility.options.any { it is FeasibilityOption.IncreaseWeeklyLoad }) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onReviewAvailability) { Text("Rever minha disponibilidade") }
                }
            }
        }
        preview.feasibility.targetCoverageDate?.let { target ->
            Text(
                "Meta de terminar o conteúdo: ${target.dayOfMonth}/${target.monthValue} - os " +
                    "${preview.feasibility.consolidationDays} dias finais ficam reservados para " +
                    "revisão, questões e simulados.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
internal fun SummaryRow(label: String, value: String) {
    // O rótulo é curto ("Estratégia", "Maior prioridade"), mas o valor pode ser um nome de cargo
    // gigante ("Analista Judiciário - Área Apoio Especializado - ..."). Sem um limite de largura no
    // valor, ele reivindica sua largura "natural" inteira e espreme o rótulo (que só tem weight)
    // até sobrar quase nada - daí o texto quebrando letra por letra. Os dois precisam de weight: o
    // valor então quebra em várias linhas em vez de roubar o espaço do rótulo.
    if (estudarioLayout().prefersStacking) {
        Column(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    } else {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** Linha de disponibilidade usada no resumo e na tela de rotina. */
internal fun availabilitySummary(snapshot: InitialSetupSnapshot): String =
    formatAvailabilityMinutes(snapshot.weeklyMinutes)
