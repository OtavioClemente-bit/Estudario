package br.com.estudario.ui.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Scale
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
 * com um valor válido - a prioridade que o edital indicou, dificuldade normal e nenhum
 * conhecimento prévio declarado - e o assistente só pede que ela corrija o que discordar. Por isso
 * nenhum destes passos bloqueia o avanço, e nenhum deles abre uma página por matéria.
 */

/**
 * Eixo 1 - como a prova distribui a atenção.
 *
 * Vem logo depois do edital, porque é a primeira coisa que o Estudário precisa entender: o peso de
 * cada matéria na prova. O valor mostrado já é o do edital; o que a pessoa faz aqui é discordar.
 */
@Composable
internal fun SubjectPriorityStep(
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    snapshot: InitialSetupSnapshot,
    officialPriorities: Map<Long, PlanPriority>,
    onSelect: (String, ExamPriority) -> Unit,
    onReset: (String) -> Unit,
    onContinue: () -> Unit,
) {
    var lastTouched by remember { mutableStateOf<Long?>(null) }
    val topicsBySubject = remember(topics) { topics.groupBy { it.subjectId } }
    val adjusted = subjects.count { it.id.toString() in snapshot.subjectPriorities }

    WizardPage(
        eyebrow = "Peso na prova",
        question = "Como esta prova distribui a atenção?",
        aside = "Já defini uma prioridade inicial para cada matéria a partir do edital. Ajuste o que não bater com o que você viu.",
        icon = Icons.Outlined.Scale,
        showScrollIndicator = true,
        bottom = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    if (adjusted == 0) "Nada ajustado - seguimos com o que o edital indica." else "$adjusted matéria(s) ajustada(s) por você.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    SetupPrimaryButton("Continuar", onContinue)
                }
            }
        },
    ) {
        if (subjects.isEmpty()) {
            SetupCard { Text("Volte à revisão do edital para adicionar suas matérias.") }
            return@WizardPage
        }
        SetupCard {
            Text(
                "Encontrei ${subjects.size} matéria(s) e ${topics.size} tópico(s) no seu edital.",
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                "A prioridade decide quanto espaço cada matéria ocupa no plano. Ela não tem nada a ver com o quanto a matéria é difícil para você - isso vem na próxima tela.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        subjects.forEach { subject ->
            val key = subject.id.toString()
            val suggested = SetupPlannerPreviewFactory.suggestedPriority(officialPriorities, subject.id)
            val current = snapshot.subjectPriorities[key] ?: suggested
            val edited = key in snapshot.subjectPriorities
            SetupCard {
                WizardSubjectHeader(
                    name = subject.name,
                    detail = "${topicsBySubject[subject.id]?.size ?: 0} tópico(s)" +
                        if (edited) " • ajustado por você" else " • sugerido pelo edital",
                )
                WizardScale(
                    options = ExamPriority.entries,
                    selected = current,
                    label = { it.label },
                    testTagPrefix = "priority_${subject.id}",
                ) { chosen ->
                    lastTouched = subject.id
                    if (chosen == suggested) onReset(key) else onSelect(key, chosen)
                }
                if (edited) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { lastTouched = null; onReset(key) }) {
                            Text("Voltar ao sugerido (${suggested.label.lowercase()})")
                        }
                    }
                }
            }
        }
        WizardFeedback(
            lastTouched?.let { id ->
                subjects.firstOrNull { it.id == id }?.let { subject ->
                    val priority = snapshot.subjectPriorities[subject.id.toString()]
                        ?: SetupPlannerPreviewFactory.suggestedPriority(officialPriorities, subject.id)
                    priorityFeedback(subject.name, priority)
                }
            },
        )
    }
}

/**
 * Eixos 2 e 3 - dificuldade e conhecimento prévio, na mesma tela.
 *
 * São perguntas diferentes e o motor as usa de formas diferentes, mas para a pessoa elas são a
 * mesma conversa ("como você está em cada matéria"), então ficam juntas. Todas começam em Normal /
 * Nunca estudei: ninguém precisa tocar em nada para continuar.
 */
@Composable
internal fun SubjectProfileStep(
    subjects: List<SubjectEntity>,
    snapshot: InitialSetupSnapshot,
    officialPriorities: Map<Long, PlanPriority>,
    onDifficulty: (String, PersonalDifficulty) -> Unit,
    onKnowledge: (String, InitialKnowledge) -> Unit,
    onContinue: () -> Unit,
) {
    var lastTouched by remember { mutableStateOf<Long?>(null) }
    val tuned = snapshot.tunedSubjectCount(subjects.mapTo(hashSetOf()) { it.id.toString() })

    WizardPage(
        eyebrow = "E para você",
        question = "Quais dessas costumam dar mais trabalho?",
        aside = "Não precisa pensar demais. Tudo começa em \"normal\" e pode ser ajustado depois - seus resultados nas questões vão corrigir isso sozinhos.",
        icon = Icons.Outlined.Person,
        showScrollIndicator = true,
        bottom = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    if (tuned == 0) "Você pode seguir sem mexer em nada." else "$tuned matéria(s) com resposta sua.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    SetupPrimaryButton("Continuar", onContinue)
                }
            }
        },
    ) {
        if (subjects.isEmpty()) {
            SetupCard { Text("Volte à revisão do edital para adicionar suas matérias.") }
            return@WizardPage
        }
        subjects.forEach { subject ->
            val key = subject.id.toString()
            val difficulty = snapshot.subjectDifficulties[key] ?: PersonalDifficulty.DEFAULT
            val knowledge = snapshot.subjectKnowledge[key] ?: InitialKnowledge.DEFAULT
            SetupCard {
                WizardSubjectHeader(subject.name, null)
                Text(
                    "Esforço que exige de você",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                WizardScale(
                    options = PersonalDifficulty.entries,
                    selected = difficulty,
                    label = { it.label },
                    testTagPrefix = "difficulty_${subject.id}",
                ) { lastTouched = subject.id; onDifficulty(key, it) }
                HorizontalDivider(Modifier.padding(vertical = 4.dp))
                Text(
                    "Quanto você já conhece",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                WizardScale(
                    options = InitialKnowledge.entries,
                    selected = knowledge,
                    label = { it.label },
                    testTagPrefix = "knowledge_${subject.id}",
                ) { lastTouched = subject.id; onKnowledge(key, it) }
            }
        }
        WizardFeedback(
            lastTouched?.let { id ->
                subjects.firstOrNull { it.id == id }?.let { subject ->
                    val official = SetupPlannerPreviewFactory.suggestedPriority(officialPriorities, subject.id)
                    PlanningExplanationBuilder.wizardFeedback(
                        subject.name,
                        snapshot.dimensionsFor(subject.id.toString(), official),
                    )
                }
            },
        )
    }
}

/**
 * O resumo antes de montar.
 *
 * Roda o cálculo de verdade - necessidade por matéria, carga restante e viabilidade - e mostra o
 * resultado. Quando o tempo não fecha, ele **diz** e oferece as opções que o motor calculou; ele
 * não altera a carga da pessoa por conta própria.
 */
@Composable
internal fun PlanSummaryStep(
    snapshot: InitialSetupSnapshot,
    subjects: List<SubjectEntity>,
    topics: List<TopicEntity>,
    officialPriorities: Map<Long, PlanPriority>,
    onContinue: () -> Unit,
    onReviewAvailability: () -> Unit,
) {
    var completedStages by remember(snapshot, subjects, topics) { mutableStateOf(0) }
    val today = remember { LocalDate.now() }
    val computation = produceWizardResult(snapshot, subjects, topics, officialPriorities) {
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
                aside = "Confira antes de montar. Tudo aqui pode mudar depois, sem refazer o plano do zero.",
                icon = Icons.Outlined.Insights,
                showScrollIndicator = true,
                bottom = { SetupPrimaryButton("Montar meu plano", onContinue) },
            ) {
                SetupCard {
                    PlanningExplanationBuilder.profileSummary(
                        weeklyMinutes = preview.weeklyMinutes,
                        sessionMinutes = preview.sessionMinutes,
                        examDate = preview.examDate,
                        today = today,
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
private fun SummaryRow(label: String, value: String) {
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

/**
 * Devolutiva do passo de prioridade.
 *
 * Diferente da devolutiva de dificuldade, esta fala do plano, não da pessoa - é sobre quanto espaço
 * a matéria vai ocupar.
 */
private fun priorityFeedback(name: String, priority: ExamPriority): String = when (priority) {
    ExamPriority.VERY_HIGH -> "$name passa a ocupar o maior espaço do seu plano."
    ExamPriority.HIGH -> "$name ganha presença alta no rodízio semanal."
    ExamPriority.MEDIUM -> "$name fica com presença equilibrada."
    ExamPriority.LOW -> "$name aparece com menos frequência, mas não sai do plano."
    ExamPriority.VERY_LOW -> "$name entra só para não ficar descoberta."
}

/** Linha de disponibilidade usada no resumo e na tela de rotina. */
internal fun availabilitySummary(snapshot: InitialSetupSnapshot): String =
    formatAvailabilityMinutes(snapshot.weeklyMinutes)
