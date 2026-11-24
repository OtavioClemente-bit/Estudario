package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Auth
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * O canto da mesa (telas largas): no lugar de rabiscos genéricos, um post-it do Folha colado no
 * caderno, com fita, a "dica do Folha" escrita à mão e a assinatura dele; ao lado, o lápis e o
 * marca-texto da turma. Muda de dica todo dia. Fica atrás do conteúdo e não pega clique.
 */
@Composable
fun DeskNote() {
    // Clicou no post-it: ele descola, cai e some de todas as telas até a página ser recarregada.
    if (DeskNoteState.gone) return
    var falling by remember { mutableStateOf(false) }
    LaunchedEffect(falling) { if (falling) { kotlinx.coroutines.delay(1100); DeskNoteState.gone = true } }
    // Todo dia um post-it diferente: a dica, a cor do papel, a fita, os adesivos da turma em volta e a
    // carinha do Folha mudam juntos (antes só a frase mudava e o adesivo era sempre o mesmo).
    val day = remember { (kotlin.js.Date.now() / 86_400_000).toLong() }
    val tip = remember { DeskTipPicker.tipFor(day) }
    val look = remember { DESK_LOOKS[((day * 7 + 3) % DESK_LOOKS.size).toInt()] }
    Div({ classes(*listOfNotNull("desk-note", "dl-${look.key}", if (falling) "falling" else null).toTypedArray()); attr("aria-hidden", "true") }) {
        Div({ classes("desk-postit"); attr("title", "Descolar"); onClick { falling = true } }) {
            Span({ classes("desk-tape") })
            B { Text(look.kicker) }
            Div({ classes("desk-tip") }) { Text(tip) }
            Div({ classes("desk-sign") }) { Folha(look.mood, 34, live = false); Span { Text("Folha") } }
        }
        Span({ classes("desk-pencil", "tu-hover") }) { Icon(look.left) }
        Span({ classes("desk-bulb", "tu-hover") }) { Icon(look.right) }
    }
}

private val DESK_TIPS = listOf(
    "Revisar no dia certo vale mais que reler tudo.",
    "Errou? Ótimo: erro anotado vira ponto na prova.",
    "Leia o enunciado até o fim. A banca esconde o \"exceto\".",
    "Grife pouco. Grifo demais é livro pintado.",
    "Questão comentada ensina mais que teoria repetida.",
    "Um tópico por vez. É assim que o edital acaba.",
    "Dormir bem também é estudar.",
    "Melhor 40 minutos todo dia que 6 horas no domingo.",
    "Explique em voz alta: se travou, é ali que falta.",
    "Simulado é treino de prova, não de ansiedade.",
    "Revisão curta hoje poupa uma releitura inteira daqui a um mês.",
    "Esqueceu? Normal. Lembrar de novo é o que fixa.",
    "Tente lembrar antes de abrir o resumo. O esforço é o treino.",
    "Errou duas vezes a mesma? Escreva por que, com suas palavras.",
    "Acertou no chute? Conta como erro na hora de revisar.",
    "Faça questões da sua banca: cada uma tem seu jeito de perguntar.",
    "Leia as alternativas todas antes de marcar a primeira que parece certa.",
    "\"Sempre\", \"nunca\" e \"somente\" pedem atenção redobrada.",
    "Questão difícil? Marque, pule e volte. O relógio é da prova toda.",
    "Abra o edital de vez em quando: ele é o mapa do que cai.",
    "Matéria de peso maior merece mais horas na semana.",
    "Tópico que você odeia costuma ser o que mais rende ponto.",
    "Comece pelo que é mais difícil enquanto a cabeça está fresca.",
    "Celular longe da mesa vale mais que força de vontade.",
    "Blocos de foco com pausa curta rendem mais que horas arrastadas.",
    "Na pausa, levante e beba água. Tela no intervalo não descansa.",
    "Estudar no mesmo horário vira hábito, e hábito não cansa.",
    "Dia ruim? Faça 10 questões. Pouco ainda mantém a sequência.",
    "Planeje a semana no domingo. Na segunda, só execute.",
    "Sono é quando a memória arruma o que você estudou.",
    "Virar a noite antes da prova costuma custar mais do que rende.",
    "Treine no horário da prova: o corpo também precisa se acostumar.",
    "Faça um simulado inteiro, sem pausa, pelo menos uma vez por mês.",
    "Depois do simulado, o mais valioso é revisar os erros.",
    "Lei seca se aprende lendo a lei, não só o resumo dela.",
    "Junte matérias parecidas: comparar ajuda a não confundir.",
    "Resumo bom é o que você mesmo escreveu, curto e do seu jeito.",
    "Cartão de memória: uma pergunta, uma resposta, nada de parágrafo.",
    "Misture matérias na revisão: a prova também mistura.",
    "Comparar com os outros cansa. Compare com você da semana passada.",
    "Progresso de concurso é lento e invisível, até de repente aparecer.",
    "Pausa também é parte do plano. Descansar não é desistir.",
    "Releia a pergunta do enunciado antes de marcar.",
    "Certo ou errado? Uma palavra trocada muda tudo. Leia devagar.",
    "Anote as pegadinhas que te pegaram. A banca repete.",
    "Estudar cansado rende pouco. Às vezes dormir é a melhor revisão.",
    "Caminhar um pouco antes de estudar ajuda a cabeça a acordar.",
    "Fim de sessão: escreva em uma linha o que aprendeu hoje.",
    "Meta pequena cumprida vale mais que meta enorme abandonada.",
    "Constância ganha de intensidade. Todo dia um pouco.",
)

/**
 * Escolhe a dica do dia sem repetir: cada pessoa tem a sua ordem embaralhada do pool (semente pelo id
 * da conta) e as dicas vistas nos últimos dias ficam guardadas no navegador, por conta. Assim o
 * post-it percorre o pool inteiro antes de voltar a uma frase, e a mesma dica vale o dia todo.
 */
internal object DeskTipPicker {
    private const val PREFIX = "estudario.desknote."
    /** Quantos dias uma dica fica "descansando" antes de poder voltar. */
    private val REST_DAYS get() = (DESK_TIPS.size * 3 / 4).coerceAtLeast(1)

    fun tipFor(day: Long): String {
        val user = Auth.session?.userId ?: "anon"
        val key = PREFIX + user
        // Formato salvo: "dia:indice" do dia atual seguido do histórico "indice,indice,..." (mais recente primeiro).
        val saved = runCatching { kotlinx.browser.localStorage.getItem(key) }.getOrNull().orEmpty()
        val head = saved.substringBefore("|")
        val history = saved.substringAfter("|", "").split(",").mapNotNull { it.toIntOrNull() }.filter { it in DESK_TIPS.indices }
        if (head.substringBefore(":").toLongOrNull() == day) {
            head.substringAfter(":").toIntOrNull()?.takeIf { it in DESK_TIPS.indices }?.let { return DESK_TIPS[it] }
        }
        val recent = history.take(REST_DAYS).toSet()
        val order = DESK_TIPS.indices.shuffled(kotlin.random.Random(user.hashCode()))
        // Anda na ordem da pessoa a partir do dia e pega a primeira que não foi vista há pouco.
        val start = (day % order.size).toInt()
        val pick = (0 until order.size).map { order[(start + it) % order.size] }.firstOrNull { it !in recent } ?: order[start]
        val newHistory = (listOf(pick) + history.filter { it != pick }).take(DESK_TIPS.size)
        runCatching { kotlinx.browser.localStorage.setItem(key, "$day:$pick|" + newHistory.joinToString(",")) }
        return DESK_TIPS[pick]
    }
}

/** O visual do post-it do dia: cor do papel (classe dl-*), título, humor do Folha e os dois adesivos. */
private class DeskLook(val key: String, val kicker: String, val mood: String, val left: String, val right: String)

private val DESK_LOOKS = listOf(
    DeskLook("sun", "dica do Folha", "wink", "edit_note", "lightbulb"),
    DeskLook("mint", "lembrete do dia", "happy", "check_circle", "local_fire_department"),
    DeskLook("pink", "recado do Folha", "proud", "format_ink_highlighter", "favorite"),
    DeskLook("sky", "papo de prova", "thinking", "push_pin", "gps_fixed"),
    DeskLook("lilac", "bizu do dia", "wink", "style", "star"),
    DeskLook("peach", "anota aí", "happy", "bookmark", "coffee"),
    DeskLook("lime", "dica do Folha", "proud", "emoji_events", "bolt"),
)

/** Só na memória: um F5 traz o post-it de volta. */
private object DeskNoteState { var gone by mutableStateOf(false) }
