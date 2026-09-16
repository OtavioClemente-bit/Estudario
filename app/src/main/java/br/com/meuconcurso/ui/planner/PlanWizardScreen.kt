package br.com.meuconcurso.ui.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.local.CompetitionEntity
import br.com.meuconcurso.data.local.SubjectEntity
import br.com.meuconcurso.data.planner.CreatePlanInput
import br.com.meuconcurso.data.local.planner.AvailabilityMode
import br.com.meuconcurso.data.local.planner.StudyAvailabilityEntity
import java.time.LocalDate

@Composable
fun PlanWizardScreen(competitions: List<CompetitionEntity>, subjects: List<SubjectEntity>, onCancel: () -> Unit, onCreate: (CreatePlanInput) -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    var competitionId by remember { mutableLongStateOf(competitions.firstOrNull { it.isPrimary }?.id ?: competitions.firstOrNull()?.id ?: 0) }
    var name by remember { mutableStateOf("Meu plano de estudos") }
    var objective by remember { mutableStateOf("Concluir o edital com revisões e questões") }
    var dailyMinutes by remember { mutableFloatStateOf(120f) }
    var advanced by remember { mutableStateOf(false) }
    val advancedMinutes = remember { mutableStateListOf(120, 120, 120, 120, 120, 120, 0) }
    var questions by remember { mutableStateOf("100") }
    var discursives by remember { mutableStateOf("2") }
    val selectedSubjects = subjects.filter { it.competitionId == competitionId }
    val capacity = if (advanced) advancedMinutes.sum() else dailyMinutes.toInt() * 6
    val labels = listOf("Objetivo", "Concurso", "Datas", "Disponibilidade", "Prioridades", "Metas", "Revisão")
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Column { Text("Criar plano — ${labels[step]}"); LinearProgressIndicator({ (step + 1) / 7f }, Modifier.fillMaxWidth()) } },
        text = { Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            when (step) {
                0 -> { OutlinedTextField(name, { name = it }, label = { Text("Nome") }); OutlinedTextField(objective, { objective = it }, label = { Text("Objetivo") }) }
                1 -> competitions.forEach { item -> FilterChip(competitionId == item.id, { competitionId = item.id }, { Text(item.name) }) }
                2 -> { Text("Início: ${LocalDate.now()}"); Text("A data da prova poderá ser adicionada depois.", style = MaterialTheme.typography.bodySmall) }
                3 -> { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { FilterChip(!advanced, { advanced = false }, { Text("Simples") }); FilterChip(advanced, { advanced = true }, { Text("Avançada") }) }; if (!advanced) { Text("${dailyMinutes.toInt()} minutos líquidos por dia, segunda a sábado", fontWeight = FontWeight.Bold); Slider(dailyMinutes, { dailyMinutes = it }, valueRange = 30f..360f, steps = 10) } else { listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom").forEachIndexed { index, label -> Row { Text("$label: ${advancedMinutes[index]} min", Modifier.width(96.dp)); Slider(advancedMinutes[index].toFloat(), { advancedMinutes[index] = it.toInt() }, Modifier.weight(1f), valueRange = 0f..360f, steps = 11) } } }; Text("Capacidade semanal: ${minutesLabel(capacity)}") }
                4 -> { Text("${selectedSubjects.size} matéria(s) do edital serão incluídas."); selectedSubjects.take(8).forEach { Text("• ${it.name}") }; Text("Prioridade inicial: média; ajuste fino disponível após criar.", style = MaterialTheme.typography.bodySmall) }
                5 -> { OutlinedTextField(questions, { questions = it }, label = { Text("Questões por semana") }); OutlinedTextField(discursives, { discursives = it }, label = { Text("Discursivas por mês") }) }
                else -> { Text(name, fontWeight = FontWeight.Bold); Text(objective); Text("${competitions.firstOrNull { it.id == competitionId }?.name.orEmpty()} • ${selectedSubjects.size} matérias"); Text("${minutesLabel(capacity)} líquidos/semana • $questions questões • $discursives discursivas"); if (selectedSubjects.isEmpty()) Text("Adicione matérias ao edital antes de gerar o plano.", color = MaterialTheme.colorScheme.error) }
            }
        } },
        confirmButton = { TextButton(enabled = name.isNotBlank() && objective.isNotBlank() && competitionId > 0 && dailyMinutes > 0 && selectedSubjects.isNotEmpty(), onClick = {
            if (step < 6) step++ else {
                val availability = if (advanced) advancedMinutes.mapIndexed { index, value -> StudyAvailabilityEntity("pending", index + 1, value, value == 0, AvailabilityMode.ADVANCED) } else StudyPlanViewModel.defaultAvailability(minutes = dailyMinutes.toInt()).map { it.copy(mode = AvailabilityMode.SIMPLE) }
                onCreate(CreatePlanInput(competitionId, name, objective, LocalDate.now(), null, availability, StudyPlanViewModel.subjectInputs(selectedSubjects), active = true))
                onCancel()
            }
        }) { Text(if (step == 6) "Criar e gerar" else "Continuar") } },
        dismissButton = { TextButton(onClick = { if (step > 0) step-- else onCancel() }) { Text(if (step > 0) "Voltar" else "Cancelar") } },
    )
}
