package br.com.estudario.ui.tour

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Guias passo a passo, separados por contexto. Cada guia aparece sozinho na primeira vez em que
 * faz sentido (abrir o app, importar o edital, abrir Plano ou Treinar) e pode ser repetido pelo
 * seletor de ajuda. A pessoa avança pelas setas do cartão; não precisa tocar nos botões
 * reais. Quando o passo aponta para um elemento ([TourStep.key]), esse elemento reporta a própria
 * posição via [tourTarget] e o [TourOverlay] recorta um destaque em volta dele.
 */
enum class TourId(val title: String, val subtitle: String) {
    EDITAL("Primeiros passos", "Criar o concurso e montar o edital com IA"),
    CONTENT("Conteúdo das matérias", "Gerar teoria, resumos e questões; revisões e fila"),
    PLAN("Plano de estudos", "Gerar o plano com IA, importar e acompanhar"),
    TRAIN("Treinar", "Desafio do dia, modos de treino e caderno de erros"),
    MORE("Revisões, fila e desempenho", "Onde acompanhar revisões, fila, desempenho e erros"),
    PROFILE("Perfil, XP e emblemas", "Seu nível, sua sequência e as conquistas"),
}

enum class HelpGuide(val title: String, val subtitle: String, val tour: TourId) {
    EDITAL("Guia do edital", "Aprenda a criar o concurso e importar o edital", TourId.EDITAL),
    MATERIAL("Guia do material", "Aprenda a gerar conteúdo tópico por tópico", TourId.CONTENT),
}

fun helpGuideOptions(): List<HelpGuide> = listOf(HelpGuide.EDITAL, HelpGuide.MATERIAL)

enum class TourKey {
    HOME_PROFILE, HOME_MISSION,
    NAV_MENU, NAV_EDITAL, EDITAL_CREATE, EDITAL_AI, EDITAL_IMPORT, SUBJECT_AI,
    PLAN_AI, PLAN_IMPORT, PLAN_CREATE, PLAN_TABS, PLAN_MANAGE,
    TRAIN_DAILY, TRAIN_MODES, TRAIN_START,
    MORE_REVIEWS, MORE_QUEUE, MORE_STATS, MORE_ERRORS, MORE_GUIDE,
}

/** [key] nulo = cartão de explicação centralizado, sem destaque. [route] = aba onde o passo acontece. */
data class TourStep(val route: String, val key: TourKey?, val title: String, val description: String, val video: TutorialVideo? = null)

fun tourSteps(id: TourId): List<TourStep> = when (id) {
    TourId.EDITAL -> listOf(
        TourStep("home", null, "Bem-vindo ao Estudário", "Este guia apresenta três etapas da sua preparação: organizar o edital, criar o plano e treinar. Use as setas para avançar sem interagir com a tela principal."),
        TourStep("home", TourKey.NAV_MENU, "Acesse todas as ferramentas", "Toque no nome do app para abrir o menu. Ali você encontra revisões, fila de estudos, desempenho, backup e os guias de orientação."),
        TourStep("home", TourKey.NAV_EDITAL, "1. Comece pelo edital", "Aqui ficam as matérias e os tópicos do seu concurso. O plano, as revisões e as questões são organizados a partir desse conteúdo."),
        TourStep("syllabus", TourKey.EDITAL_CREATE, "Crie seu edital manualmente", "Toque no botão + para criar o concurso e adicionar as matérias e os tópicos manualmente."),
        TourStep("syllabus", TourKey.EDITAL_AI, "Peça ajuda à IA", "No botão ✨, selecione as opções, como cargo, banca e conteúdo, anexe o PDF do edital e compartilhe a solicitação com o ChatGPT, Gemini ou outra ferramenta de IA. Não é necessário escrever ou editar o prompt.", TutorialVideo.EDITAL),
        TourStep("syllabus", TourKey.EDITAL_IMPORT, "Importe a resposta", "Quando a IA gerar o arquivo .estudo, abra-o com o Estudário, compartilhe a resposta com o app ou selecione o arquivo por aqui. Você também pode copiar o texto e usar a opção “Colar resposta da IA”."),
        TourStep("syllabus", null, "A IA cria o conteúdo. O app organiza o plano.", "Use a IA para produzir edital, teoria e questões. O Estudário monta o plano de estudos automaticamente e funciona offline. Assim que o edital estiver pronto, você poderá gerar o conteúdo do primeiro tópico."),
    )
    TourId.CONTENT -> listOf(
        TourStep("syllabus", null, "Edital importado", "Gere teoria, resumos e questões para um tópico por vez. Essa abordagem preserva o detalhamento e reduz o risco de informações imprecisas. Revise o material antes de estudar."),
        TourStep("syllabus", TourKey.SUBJECT_AI, "Gere o tópico atual", "Toque em ✨ na matéria, escolha o tópico e selecione o conteúdo desejado, como teoria, resumo ou questões. Depois, envie a solicitação à IA.", TutorialVideo.CONTENT),
        TourStep("syllabus", TourKey.EDITAL_IMPORT, "Importe o conteúdo", "A IA devolve um arquivo .estudo. Abra-o com o app, compartilhe o texto ou use este botão. Cada teoria e questão será associada ao tópico correto, sem duplicação."),
        TourStep("syllabus", null, "Acesso rápido no tópico", "Dentro do tópico, o botão ✨ abre o gerador com o assunto já selecionado. Repita o processo para cada novo tópico."),
        TourStep("syllabus", null, "Revisões espaçadas", "Ao marcar um tópico como estudado, o app agenda revisões em D+1, D+7 e D+30, ou segue o ciclo intensivo. Os intervalos aumentam com a consolidação e diminuem quando há dificuldade. As revisões aparecem no Início e em Mais, na seção Revisões espaçadas."),
        TourStep("syllabus", null, "Fila de estudos", "No menu ⋮ de cada tópico, selecione “Adicionar à fila”. O próximo item ficará em destaque no Início até a conclusão do bloco."),
    )
    TourId.PLAN -> listOf(
        TourStep("plan", null, "Duas formas de criar seu plano", "Você pode montar o plano no próprio app, com regras definidas para sua rotina, ou solicitar uma versão personalizada a uma ferramenta de IA. As duas opções funcionam em conjunto."),
        TourStep("plan", TourKey.PLAN_CREATE, "1. Monte o plano no app", "Informe sua etapa atual, a data da prova, o tempo disponível, a duração dos blocos, o peso de cada matéria e suas metas. Antes de criar, o app mostra uma prévia das horas por matéria, das fases e da previsão de conclusão do edital."),
        TourStep("plan", null, "Como o app organiza o estudo", "Revisões atrasadas vêm antes de conteúdo novo. As matérias se alternam conforme o peso definido, e os tópicos com mais erros retornam como reforço."),
        TourStep("plan", null, "Ajustes automáticos", "Quando uma atividade atrasa, ela entra no próximo replanejamento. Se você alterar o tempo disponível ou o peso de uma matéria, o cronograma é atualizado sem perder o histórico já realizado."),
        TourStep("plan", TourKey.PLAN_AI, "2. Gere um plano com IA", "Use esta opção para estratégias específicas, como uma banca ou um cronograma personalizado. Em ✨, o app prepara a solicitação com seu edital e seu tempo disponível para você enviar ao ChatGPT, Gemini ou outra ferramenta."),
        TourStep("plan", TourKey.PLAN_IMPORT, "Importe o arquivo .plano", "A IA devolve um arquivo .plano. Abra-o com o Estudário, compartilhe a resposta com o app ou selecione o arquivo por aqui."),
        TourStep("plan", TourKey.PLAN_TABS, "Acompanhe hoje, semana, mês e ano", "Acompanhe as tarefas do dia, registre o que realizou e consulte as metas da semana, do mês e das fases da preparação. Em Hoje, “Por que este plano” explica as regras usadas pelo cronograma."),
        TourStep("plan", TourKey.PLAN_MANAGE, "Gerencie seus planos", "Troque o plano ativo, ajuste sua disponibilidade e os pesos das matérias, ou exporte o contexto para uma IA revisar o que já foi realizado."),
    )
    TourId.TRAIN -> listOf(
        TourStep("train", null, "Hora de treinar", "As questões são baseadas nos conteúdos importados. Cada resposta atualiza o domínio do tópico e os indicadores de desempenho."),
        TourStep("train", TourKey.TRAIN_DAILY, "Desafio do dia", "São dez questões selecionadas entre erros recorrentes, revisões atrasadas e tópicos com menor domínio."),
        TourStep("train", TourKey.TRAIN_MODES, "Escolha o modo de treino", "O treino inteligente prioriza o que merece mais atenção. Você também pode revisar apenas as questões erradas, suas favoritas ou iniciar um simulado."),
        TourStep("train", TourKey.TRAIN_START, "Configure a sessão", "Filtre por matéria, tópico, banca e dificuldade, defina a quantidade de questões e comece."),
        TourStep("train", null, "Caderno de erros", "Toda questão errada vai para o Caderno de erros e retorna automaticamente. Ela volta três dias após o erro, dez dias após um acerto e trinta dias após o acerto seguinte. Um novo erro reinicia o ciclo."),
    )
    TourId.PROFILE -> listOf(
        TourStep("home", TourKey.HOME_PROFILE, "Seu espaço pessoal", "Veja sua foto, seu nome, a sequência de dias, o nível e o XP do dia. Toque nesses elementos para abrir o perfil."),
        TourStep("home", TourKey.HOME_MISSION, "A missão de hoje", "O anel mostra quanto falta para atingir a meta do dia. Abaixo, você acompanha o XP que ainda pode conquistar."),
        TourStep("home", null, "Sequência e XP", "Cada tarefa concluída gera XP. As atividades do plano têm um peso maior que as questões avulsas. O dia entra na sequência quando você atinge a meta definida no perfil."),
        TourStep("home", null, "Emblemas", "São 44 emblemas, do bronze ao esmeralda. No perfil, você acompanha as conquistas obtidas e o progresso necessário para desbloquear as próximas."),
        TourStep("home", TourKey.HOME_PROFILE, "Tudo no perfil", "Toque na sua foto para consultar nível, recompensas, frequência de estudos, emblemas, meta diária e backup."),
    )
    TourId.MORE -> listOf(
        TourStep("more", TourKey.MORE_REVIEWS, "Revisões espaçadas", "Consulte as revisões do dia e as que estão atrasadas. Informe se o conteúdo foi fácil ou difícil, e o app ajustará a próxima data."),
        TourStep("more", TourKey.MORE_QUEUE, "Fila de estudos", "Consulte os próximos tópicos e reorganize, pause, adie ou conclua os blocos conforme sua rotina."),
        TourStep("more", TourKey.MORE_STATS, "Desempenho", "Acompanhe acertos por matéria, evolução, pontos fortes e assuntos que precisam de revisão."),
        TourStep("more", TourKey.MORE_ERRORS, "Caderno de erros", "Revise as questões erradas, os conceitos relacionados e a data prevista para cada nova tentativa."),
        TourStep("more", null, "Modo foco", "Inicie uma sessão cronometrada e sem interrupções. O app ativa o Não Perturbe, mantém a tela acesa e registra o tempo real de estudo. Encerre pela tela ou pela notificação."),
        TourStep("more", TourKey.MORE_GUIDE, "Consulte os guias", "Quando precisar, repita qualquer guia nesta seção."),
    )
}

fun tourKeyForRoute(route: String): TourKey? = when (route) {
    "syllabus" -> TourKey.NAV_EDITAL
    else -> null
}

/** Guia que abre sozinho na primeira visita a uma aba. */
fun tourForRoute(route: String?): TourId? = when (route) {
    "home" -> TourId.PROFILE
    "plan" -> TourId.PLAN
    "train" -> TourId.TRAIN
    else -> null
}

/** Reporta a posição real deste elemento na tela, só enquanto ele for o alvo ativo do tour. */
fun Modifier.tourTarget(key: TourKey, activeKey: TourKey?, onBounds: (Rect) -> Unit): Modifier =
    if (key != activeKey) this else this.onGloballyPositioned { coordinates -> onBounds(coordinates.boundsInWindow()) }

@Composable
fun TourOverlay(
    tour: TourId?,
    step: TourStep?,
    stepIndex: Int,
    stepCount: Int,
    bounds: Rect?,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onWatchVideo: (TutorialVideo) -> Unit,
) {
    if (tour == null || step == null) return
    val density = LocalDensity.current
    var overlayOrigin by remember { mutableStateOf(Offset.Zero) }
    // A rolagem até o alvo pode levar alguns frames. O cartão permanece aberto e centralizado nesse
    // intervalo; assim o passo não some e reaparece enquanto a lista de Treinar se movimenta.
    // Quando a posição chega, o mesmo cartão ganha o recorte no alvo sem interromper o guia.

    val paddingPx = with(density) { 8.dp.toPx() }
    val hole = if (step.key != null && bounds != null) Rect(
        left = bounds.left - overlayOrigin.x - paddingPx,
        top = bounds.top - overlayOrigin.y - paddingPx,
        right = bounds.right - overlayOrigin.x + paddingPx,
        bottom = bounds.bottom - overlayOrigin.y + paddingPx,
    ) else null
    val primary = MaterialTheme.colorScheme.primary

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned { overlayOrigin = it.positionInWindow() }
            // Durante o guia os toques ficam só no cartão, mas tocar na área escura fecha o guia.
            // Sem essa saída, quem não visse o botão "Pular" ficava preso achando que travou.
            .pointerInput(onSkip) { detectTapGestures { onSkip() } },
    ) {
        val screenHeightPx = with(density) { maxHeight.toPx() }

        Canvas(
            Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen },
        ) {
            drawRect(Color.Black.copy(alpha = 0.72f))
            if (hole != null) {
                val radius = CornerRadius(14.dp.toPx())
                drawRoundRect(color = Color.Transparent, topLeft = Offset(hole.left, hole.top), size = Size(hole.width, hole.height), cornerRadius = radius, blendMode = BlendMode.Clear)
                drawRoundRect(color = primary, topLeft = Offset(hole.left, hole.top), size = Size(hole.width, hole.height), cornerRadius = radius, style = Stroke(width = 3.dp.toPx()))
            }
        }

        val alignment = when {
            hole == null -> Alignment.Center
            hole.center.y < screenHeightPx / 2f -> Alignment.BottomCenter
            else -> Alignment.TopCenter
        }
        Card(
            modifier = Modifier
                .align(alignment)
                .systemBarsPadding()
                .padding(horizontal = 20.dp, vertical = 28.dp)
                .widthIn(max = 440.dp)
                // Com fonte grande o cartão crescia até cobrir o botão que ele explica: agora ocupa no
                // máximo ~58% da altura e o texto rola dentro dele.
                .heightIn(max = maxHeight * 0.58f)
                // O cartão não é área escura: tocar nele não fecha o guia sem querer.
                .pointerInput(Unit) { detectTapGestures { } },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        ) {
            Column(Modifier.verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(tour.title.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                    TextButton(onClick = onSkip) { Text("Pular guia") }
                }
                Text(step.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(step.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                step.video?.let { video ->
                    OutlinedButton(onClick = { onWatchVideo(video) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.PlayCircleOutline, null)
                        Text("Ver demonstração curta", Modifier.padding(start = 8.dp))
                    }
                }
                Spacer(Modifier.size(2.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    repeat(stepCount) { index ->
                        Box(
                            Modifier
                                .padding(horizontal = 3.dp)
                                .size(if (index == stepIndex) 10.dp else 7.dp)
                                .background(if (index <= stepIndex) primary else MaterialTheme.colorScheme.outlineVariant, CircleShape),
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = onPrevious, enabled = stepIndex > 0) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Passo anterior")
                    }
                    Text(
                        "${stepIndex + 1} de $stepCount",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                    Button(onClick = onNext) {
                        if (stepIndex + 1 >= stepCount) {
                            Text("Concluir"); Spacer(Modifier.width(6.dp)); Icon(Icons.Outlined.Check, null)
                        } else {
                            Text("Próximo"); Spacer(Modifier.width(6.dp)); Icon(Icons.AutoMirrored.Outlined.ArrowForward, null)
                        }
                    }
                }
            }
        }
    }
}
