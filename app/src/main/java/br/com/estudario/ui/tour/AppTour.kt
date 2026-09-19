package br.com.estudario.ui.tour

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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

/**
 * Guias passo a passo, separados por contexto. Cada guia aparece sozinho na primeira vez em que
 * faz sentido (abrir o app, importar o edital, abrir Plano/Treinar/Mais) e pode ser repetido em
 * Mais › Como usar o app. A pessoa avança pelas setas do cartão; não precisa tocar nos botões
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

enum class TourKey {
    HOME_PROFILE, HOME_MISSION,
    NAV_EDITAL, EDITAL_CREATE, EDITAL_AI, EDITAL_IMPORT, SUBJECT_AI,
    PLAN_AI, PLAN_IMPORT, PLAN_CREATE, PLAN_TABS, PLAN_MANAGE,
    TRAIN_DAILY, TRAIN_MODES, TRAIN_START,
    MORE_REVIEWS, MORE_QUEUE, MORE_STATS, MORE_ERRORS, MORE_GUIDE,
}

/** [key] nulo = cartão de explicação centralizado, sem destaque. [route] = aba onde o passo acontece. */
data class TourStep(val route: String, val key: TourKey?, val title: String, val description: String, val video: TutorialVideo? = null)

fun tourSteps(id: TourId): List<TourStep> = when (id) {
    TourId.EDITAL -> listOf(
        TourStep("home", null, "Bem-vindo ao Estudário", "Vou te mostrar o caminho em três etapas: montar o edital, criar o plano e treinar. Use as setas para avançar — não precisa tocar em nada na tela."),
        TourStep("home", TourKey.NAV_EDITAL, "1. Tudo começa pelo Edital", "Aqui ficam as matérias e os tópicos do seu concurso. Plano, revisões e questões partem dele."),
        TourStep("syllabus", TourKey.EDITAL_CREATE, "Criar manualmente", "No + você cria o concurso e depois adiciona as matérias e os tópicos, se preferir digitar."),
        TourStep("syllabus", TourKey.EDITAL_AI, "Ou peça para a IA montar", "No botão ✨ você escolhe as opções (cargo, banca, o que incluir), anexa o PDF do edital e compartilha direto com o ChatGPT, Gemini ou outro app de IA. Você não precisa escrever nem editar prompt.", TutorialVideo.EDITAL),
        TourStep("syllabus", TourKey.EDITAL_IMPORT, "Traga a resposta de volta", "Quando a IA gerar o arquivo .estudo: abra o arquivo com o Estudário, compartilhe a resposta com o app ou toque aqui para escolher o arquivo. Também dá para copiar o texto e usar “Colar resposta da IA”."),
        TourStep("syllabus", null, "A IA é para o conteúdo, não para o plano", "Edital, teoria e questões valem a pena pedir para a IA — é texto que alguém precisa escrever. O plano de estudos o app monta sozinho, offline, com regras que você confere na tela. Tudo aqui também pode ser digitado à mão pelo +."),
        TourStep("syllabus", null, "Depois do edital", "Assim que o edital for importado, eu mostro como gerar o conteúdo do tópico que você vai estudar. Plano e Treinar têm seus próprios guias na primeira vez que você abrir essas abas."),
    )
    TourId.CONTENT -> listOf(
        TourStep("syllabus", null, "Edital importado!", "Recomendamos gerar teoria, resumos e questões de um tópico por vez. Pedidos com vários tópicos podem reduzir o detalhamento e trazer informações imprecisas ou sem fonte verificável. Confira o material antes de estudar."),
        TourStep("syllabus", TourKey.SUBJECT_AI, "Gere o tópico de hoje", "Toque em ✨ na matéria e selecione o tópico que vai estudar. Escolha o que quer receber (teoria, resumo, questões…) e envie para a IA. Gerar um tópico por vez facilita a conferência das fontes e a revisão do resultado.", TutorialVideo.CONTENT),
        TourStep("syllabus", TourKey.EDITAL_IMPORT, "Importe o conteúdo", "A IA devolve um .estudo: abra com o app, compartilhe o texto ou use este botão. Cada teoria e questão cai no tópico certo, sem duplicar."),
        TourStep("syllabus", null, "Atalho no próprio tópico", "Dentro do tópico, o botão ✨ já abre o gerador com só aquele assunto selecionado. Use quando chegar a hora de estudá-lo; depois repita no próximo tópico."),
        TourStep("syllabus", null, "Revisões espaçadas", "Ao marcar um tópico como estudado, o app agenda revisões (D+1, D+7 e D+30, ou o ciclo intensivo). Depois do D+30 a agenda não acaba: o tópico volta com intervalo maior a cada rodada, e mais cedo quando você sente dificuldade. Revisar no dia certo é o que fixa o conteúdo — elas aparecem no Início e em Mais › Revisões espaçadas."),
        TourStep("syllabus", null, "Fila de estudos", "No menu ⋮ de cada tópico use “Adicionar à fila”. O próximo item da fila fica em destaque no Início até você concluir o bloco."),
    )
    TourId.PLAN -> listOf(
        TourStep("plan", null, "Dois caminhos, e os dois funcionam", "Só o edital precisa mesmo de IA. O plano tem duas portas: o app monta sozinho, com regras fixas, ou você pede para uma IA montar. Vou mostrar as duas — comece pela primeira."),
        TourStep("plan", TourKey.PLAN_CREATE, "1. Montar aqui, sem IA", "Sete perguntas: em que ponto você está, data da prova, quanto tempo tem de verdade, tamanho do bloco, peso de cada matéria e suas metas. Antes de criar, o app mostra a prévia — horas por matéria, fases e a previsão de quando o edital acaba."),
        TourStep("plan", null, "O que o app decide sozinho", "Revisão atrasada vem antes de conteúdo novo. Cada tópico entra como teoria e, na sequência, questões daquele tópico. As matérias entram em rodízio proporcional ao peso, nunca quatro horas seguidas da mesma. Tópico com acerto baixo volta como reforço. Simulado e discursiva caem no seu dia mais livre."),
        TourStep("plan", null, "Ele se conserta sozinho", "Atrasou um dia? O que ficou para trás volta na frente no próximo replanejamento. Mudou suas horas ou o peso de uma matéria? O cronograma inteiro é refeito na hora, sem perder o histórico do que você já fez."),
        TourStep("plan", TourKey.PLAN_AI, "2. Gerar com IA", "Use quando quiser um plano fora do padrão — uma banca específica, uma estratégia que você leu, um cronograma que alguém te passou. Em ✨ o app monta o pedido com o seu edital e as suas horas para você colar no ChatGPT, Gemini ou outro app."),
        TourStep("plan", TourKey.PLAN_IMPORT, "Trazer o .plano de volta", "A IA devolve um arquivo .plano: abra com o Estudário, compartilhe a resposta com o app ou toque aqui para escolher o arquivo."),
        TourStep("plan", TourKey.PLAN_TABS, "Hoje, semana, mês e ano", "Acompanhe as tarefas do dia, registre o que fez e veja as metas da semana, do mês e as fases da preparação. Em Hoje, “Por que este plano” mostra as regras que geraram as tarefas."),
        TourStep("plan", TourKey.PLAN_MANAGE, "Gerenciar planos", "Troque o plano ativo, ajuste disponibilidade e pesos ou exporte o contexto para uma IA reavaliar o que você já fez."),
    )
    TourId.TRAIN -> listOf(
        TourStep("train", null, "Hora de treinar", "As questões vêm dos conteúdos que você importa. Cada resposta alimenta o domínio de cada tópico e o seu desempenho."),
        TourStep("train", TourKey.TRAIN_DAILY, "Desafio do dia", "10 questões escolhidas entre erros recorrentes, revisões atrasadas e tópicos com menor domínio."),
        TourStep("train", TourKey.TRAIN_MODES, "Escolha o modo", "O treino inteligente prioriza o que você mais precisa. Também dá para treinar só as erradas, as favoritas ou fazer um simulado."),
        TourStep("train", TourKey.TRAIN_START, "Monte a sessão", "Filtre por matéria, tópico, banca e dificuldade, defina a quantidade e comece."),
        TourStep("train", null, "Caderno de erros", "Toda questão errada vai para o Caderno de erros (em Mais) e volta sozinha: 3 dias depois do erro, 10 dias se você acertar, 30 no acerto seguinte. Errou de novo, ela recomeça em 3 dias."),
    )
    TourId.PROFILE -> listOf(
        TourStep("home", TourKey.HOME_PROFILE, "Este canto é seu", "Sua foto, seu nome e, ao lado, a sequência de dias, o nível e o XP de hoje. Toque em qualquer um deles para abrir o perfil."),
        TourStep("home", TourKey.HOME_MISSION, "A missão de hoje", "O anel mostra o quanto falta para fechar a meta do dia. Embaixo aparece quanto XP as atividades de hoje ainda valem — o que está na mesa esperando você."),
        TourStep("home", null, "Como o XP funciona", "Cada coisa que você conclui rende XP, e o app avisa quanto vale antes de você fazer. Tarefa do plano rende mais que questão avulsa, de propósito: seguir o cronograma é o que leva à aprovação. Simulado e discursiva são as que mais pagam."),
        TourStep("home", null, "O XP não se perde", "Ele é recalculado do seu histórico, não fica guardado num contador. Trocou de aparelho e restaurou o backup? O nível volta igualzinho. E questão avulsa tem teto diário, então não adianta moer o banco de questões para subir de nível."),
        TourStep("home", null, "Sequência e meta do dia", "O dia entra na sequência quando você bate a meta: X questões, OU uma tarefa do plano, OU uma revisão. Você escolhe o X no perfil. Ao fechar a meta pela primeira vez no dia, aparece a tela de comemoração."),
        TourStep("home", null, "Emblemas", "São 44, em onze categorias: sequência, metas, questões, pontaria, tópicos, edital, plano, revisões, simulados, discursivas e maratona. Cada um tem faixas de bronze a esmeralda. No perfil você vê os conquistados e, nos que faltam, exatamente quanto falta."),
        TourStep("home", TourKey.HOME_PROFILE, "Tudo fica no perfil", "Toque na sua foto para ver nível, quadro de recompensas, mapa de frequência estilo GitHub, emblemas, meta do dia e backup."),
    )
    TourId.MORE -> listOf(
        TourStep("more", TourKey.MORE_REVIEWS, "Revisões espaçadas", "Revisões do dia e atrasadas de cada tópico estudado. Faça a revisão, diga se foi fácil ou difícil e o app ajusta a próxima data."),
        TourStep("more", TourKey.MORE_QUEUE, "Fila de estudos", "A ordem dos próximos tópicos: reorganize, pause, adie ou conclua blocos."),
        TourStep("more", TourKey.MORE_STATS, "Desempenho", "Acertos por matéria, evolução, pontos fortes e assuntos que pedem revisão."),
        TourStep("more", TourKey.MORE_ERRORS, "Caderno de erros", "Questões que você errou, com status (novo, revisando, corrigido, recorrente), os conceitos que causam o erro e o dia em que cada questão volta para você refazer."),
        TourStep("more", null, "Modo foco", "Uma sessão de estudo cronometrada, sem ciclo forçado e sem alarme: o app liga o Não Perturbe do Android, segura a tela acesa e mede o tempo real. Você encerra quando quiser, pela tela ou pela notificação, e o telefone volta ao normal na hora. Comece pela tarefa do plano, por um tópico, ou faça uma sessão livre para estudar no livro."),
        TourStep("more", TourKey.MORE_GUIDE, "Rever os guias", "Quando quiser, repita qualquer guia por aqui."),
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
    "more" -> TourId.MORE
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
    // Se o alvo não aparecer (lista vazia, item fora da tela), o cartão aparece centralizado mesmo assim.
    var waitedForTarget by remember(tour, stepIndex) { mutableStateOf(step.key == null) }
    LaunchedEffect(tour, stepIndex) { if (step.key != null) { delay(650); waitedForTarget = true } }
    // Enquanto o cartão ainda não tem o que mostrar, o guia não desenha nem bloqueia a tela: antes
    // dava para ficar alguns instantes com tudo travado sem nenhum cartão visível.
    if (step.key != null && bounds == null && !waitedForTarget) return

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
            // Durante o guia os toques ficam só no cartão — mas tocar na área escura fecha o guia.
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
                // O cartão não é área escura: tocar nele não fecha o guia sem querer.
                .pointerInput(Unit) { detectTapGestures { } },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
