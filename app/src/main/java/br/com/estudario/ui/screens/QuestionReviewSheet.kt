package br.com.estudario.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.MarkdownText

/** Um pedaço do material do tópico, já pontuado pela proximidade com o enunciado da questão. */
private data class Trecho(
    val origem: String,
    val titulo: String,
    val corpo: String,
    val pontos: Int,
    /** A própria IA apontou esta seção como a que responde a questão. */
    val apontado: Boolean = false,
)

/** Compara títulos ignorando acento, caixa, numeração e pontuação. */
private fun normalizar(texto: String): String = java.text.Normalizer
    .normalize(texto.lowercase(), java.text.Normalizer.Form.NFD)
    .replace(Regex("\\p{Mn}+"), "")
    .replace(Regex("[^a-z0-9 ]"), " ")
    .replace(Regex("\\s+"), " ")
    .trim()

private val VAZIAS = setOf(
    "para", "como", "pelo", "pela", "pelos", "pelas", "essa", "esse", "isso", "aquele", "aquela",
    "sobre", "entre", "quando", "onde", "porque", "todos", "todas", "cada", "mais", "menos",
    "deve", "devem", "pode", "podem", "seja", "sejam", "está", "estão", "será", "serão", "sendo",
    "qual", "quais", "seguinte", "seguintes", "acordo", "respeito", "julgue", "item", "assinale",
    "alternativa", "correta", "incorreta", "opção", "questão", "considere", "analise",
)

/** Palavras que realmente identificam o assunto do enunciado. */
private fun palavrasChave(texto: String): Set<String> = texto
    .lowercase()
    .split(Regex("[^\\p{L}\\p{N}]+"))
    .filter { it.length > 4 && it !in VAZIAS }
    .toSet()

private fun pontuar(chaves: Set<String>, texto: String): Int {
    if (chaves.isEmpty()) return 0
    val corpo = texto.lowercase()
    return chaves.count { corpo.contains(it) }
}

/** Quebra o markdown em seções pelos títulos, para abrir direto na parte certa. */
private fun secoes(markdown: String, origem: String, tituloPadrao: String, chaves: Set<String>): List<Trecho> {
    val linhas = markdown.lines()
    val blocos = mutableListOf<Pair<String, StringBuilder>>()
    var atual = tituloPadrao to StringBuilder()
    linhas.forEach { linha ->
        val titulo = Regex("^#{1,4}\\s+(.*)").find(linha.trim())?.groupValues?.get(1)
        if (titulo != null) {
            if (atual.second.isNotBlank()) blocos += atual
            atual = titulo to StringBuilder()
        } else {
            atual.second.appendLine(linha)
        }
    }
    if (atual.second.isNotBlank()) blocos += atual
    return blocos.map { (titulo, corpo) ->
        val texto = corpo.toString().trim()
        Trecho(origem, titulo, texto, pontuar(chaves, "$titulo $texto"))
    }.filter { it.corpo.isNotBlank() }
}

/**
 * Resumo da questão errada, aberto por cima do quiz.
 *
 * Duas coisas importam aqui: a pessoa não perde a sessão de questões (a folha é uma camada, não
 * uma tela nova), e ela cai direto no trecho que fala do assunto — o app compara as palavras do
 * enunciado com os títulos e o corpo do material do tópico e abre o pedaço mais próximo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionReviewSheet(
    viewModel: AppViewModel,
    question: QuestionEntity,
    onOpenTopic: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val topics by viewModel.topics.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val topic = topics.firstOrNull { it.id == question.topicId }

    val trechos = remember(question.id, summaries, theories, snippets) {
        val chaves = palavrasChave(question.statement + " " + question.explanation)
        val alvo = question.reviewAnchor?.takeIf { it.isNotBlank() }?.let(::normalizar)
        val doTopico = buildList {
            summaries.filter { it.topicId == question.topicId }.forEach { resumo ->
                addAll(secoes(resumo.markdown, "Resumo", resumo.title, chaves))
            }
            theories.filter { it.topicId == question.topicId }.forEach { teoria ->
                addAll(secoes(teoria.markdown, "Teoria", teoria.title, chaves))
            }
            snippets.filter { it.topicId == question.topicId }.forEach { bizu ->
                val titulo = when (bizu.kind) {
                    SnippetKind.BIZU -> "Bizu"
                    SnippetKind.PEGADINHA -> "Pegadinha"
                    SnippetKind.RECUPERACAO -> "Recuperação"
                }
                add(Trecho("Memorização", titulo, bizu.text, pontuar(chaves, bizu.text)))
            }
        }
        // Quando a IA disse de qual seção a questão saiu, ela vem primeiro; a pontuação por
        // palavra-chave fica de reserva para o material antigo, que não tem esse vínculo.
        doTopico
            .map { trecho ->
                val bate = alvo != null && normalizar(trecho.titulo).let { it == alvo || it.contains(alvo) || alvo.contains(it) }
                if (bate) trecho.copy(apontado = true) else trecho
            }
            .sortedWith(compareByDescending<Trecho> { it.apontado }.thenByDescending { it.pontos }.thenBy { it.titulo })
    }

    var abertos by remember(question.id) { mutableStateOf(setOf(0)) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Revisão rápida", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
            Text(topic?.title ?: "Tópico da questão", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            if (trechos.isEmpty()) {
                Text(
                    "Este tópico ainda não tem resumo nem teoria importados. Gere o conteúdo pelo botão ✨ do tópico e ele aparece aqui na próxima vez que você errar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                val primeiro = trechos.first()
                if (primeiro.apontado || primeiro.pontos > 0) {
                    Text(
                        if (primeiro.apontado) "Esta questão foi escrita a partir do trecho aberto abaixo."
                        else "Abrimos no trecho que mais combina com o enunciado.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                trechos.take(8).forEachIndexed { index, trecho ->
                    val aberto = index in abertos
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                Modifier.fillMaxWidth().clickable {
                                    abertos = if (aberto) abertos - index else abertos + index
                                },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(trecho.origem.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                    Text(trecho.titulo, fontWeight = FontWeight.SemiBold)
                                }
                                if (index == 0 && (trecho.apontado || trecho.pontos > 0)) {
                                    AssistChip(onClick = {}, label = { Text(if (trecho.apontado) "é daqui" else "mais relevante") })
                                    Spacer(Modifier.width(4.dp))
                                }
                                Icon(if (aberto) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (aberto) "Recolher" else "Abrir")
                            }
                            if (aberto) MarkdownText(trecho.corpo)
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = { onDismiss(); onOpenTopic(question.topicId) },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Abrir o tópico inteiro") }
            Text(
                "Fechar esta folha devolve você exatamente na questão em que parou.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
