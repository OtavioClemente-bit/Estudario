package br.com.estudario.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.ContentSourceEntity
import br.com.estudario.data.local.SourceKind
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import java.text.DateFormat
import java.util.Date

/**
 * De onde veio cada conteúdo.
 *
 * O app não tem como verificar sozinho se a IA leu mesmo o que diz ter lido — o que ele pode fazer
 * é guardar a declaração dela e deixar a pessoa conferir com um toque. Fonte oficial e
 * complementar aparecem separadas de propósito: tratar as duas como iguais é o erro que faz alguém
 * estudar por um blog achando que é a lei.
 *
 * A lista abre agrupada por tópico e fechada. Com um edital inteiro importado são centenas de
 * fontes: despejar tudo de uma vez transforma a tela em rolagem infinita e ninguém confere nada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesScreen(viewModel: AppViewModel, onBack: () -> Unit, onOpenTopic: (Long) -> Unit) {
    val sources by viewModel.sources.collectAsState()
    val packages by viewModel.importPackages.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val uriHandler = LocalUriHandler.current
    var somenteOficiais by remember { mutableStateOf(false) }
    var porImportacao by remember { mutableStateOf(false) }
    val abertos = remember { mutableStateMapOf<String, Boolean>() }

    val visiveis = sources.filter { !somenteOficiais || it.kind == SourceKind.OFICIAL }
    val abrirUrl: (String) -> Unit = { url -> runCatching { uriHandler.openUri(url) } }

    // Um grupo por tópico, na ordem das matérias do edital. O que veio sem tópico fica por último.
    val porTopico = remember(visiveis, topics, subjects) {
        val topicoPorId = topics.associateBy { it.id }
        val materiaPorId = subjects.associateBy { it.id }
        visiveis.groupBy { it.topicId }
            .map { (topicId, lista) ->
                val topico = topicId?.let { topicoPorId[it] }
                val materia = topico?.let { materiaPorId[it.subjectId] }
                GrupoDeFontes(
                    chave = "topico-${topicId ?: 0L}",
                    titulo = topico?.title ?: "Sem tópico ligado",
                    apoio = materia?.name ?: "Fontes gerais da importação",
                    topicId = topico?.id,
                    ordem = materia?.position ?: Int.MAX_VALUE,
                    ordemInterna = topico?.position ?: Int.MAX_VALUE,
                    fontes = lista,
                )
            }
            .sortedWith(compareBy({ it.topicId == null }, { it.ordem }, { it.ordemInterna }, { it.titulo }))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Column { Text("Histórico e fontes"); Text("De onde veio o seu material", style = MaterialTheme.typography.bodySmall) } },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Voltar") } },
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("${sources.size} fonte(s) em ${packages.size} importação(ões)", fontWeight = FontWeight.Bold)
                        Text(
                            "As fontes são declaradas por quem gerou o material. O app guarda e mostra — conferir continua sendo com você, e é por isso que o link está aqui.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(!porImportacao, { porImportacao = false }, { Text("Por tópico") })
                            FilterChip(porImportacao, { porImportacao = true }, { Text("Por importação") })
                            FilterChip(somenteOficiais, { somenteOficiais = !somenteOficiais }, { Text("Só fontes oficiais") })
                        }
                    }
                }
            }

            if (sources.isEmpty()) {
                item {
                    EmptyState(
                        "Nenhuma fonte registrada ainda",
                        "Conteúdo gerado a partir de agora traz as fontes junto. O material importado antes disso não tem esse registro — gere de novo pelo botão ✨ se quiser a rastreabilidade.",
                    )
                }
            } else if (visiveis.isEmpty()) {
                item { EmptyState("Nenhuma fonte oficial", "Este material foi gerado só com fontes complementares. Desligue o filtro para ver todas.") }
            }

            if (!porImportacao) {
                porTopico.forEach { grupo ->
                    item(key = grupo.chave) {
                        GrupoRecolhivel(
                            titulo = grupo.titulo,
                            apoio = grupo.apoio,
                            resumo = resumo(grupo.fontes),
                            aberto = abertos[grupo.chave] == true,
                            onToggle = { abertos[grupo.chave] = abertos[grupo.chave] != true },
                            acao = grupo.topicId?.let { id -> { onOpenTopic(id) } },
                        ) {
                            grupo.fontes.forEach { fonte ->
                                SourceCard(fonte, topicTitle = null, onOpenTopic = {}, onOpenUrl = abrirUrl, showTopic = false)
                            }
                        }
                    }
                }
            } else {
                packages.forEach { pacote ->
                    val doPacote = visiveis.filter { it.packageId == pacote.packageId }
                    val chave = "pacote-${pacote.packageId}"
                    item(key = chave) {
                        GrupoRecolhivel(
                            titulo = pacote.fileName.takeIf { it.isNotBlank() } ?: pacote.packageId,
                            apoio = "${DateFormat.getDateInstance().format(Date(pacote.importedAt))} • ${pacote.createdCount} item(ns) novo(s)",
                            resumo = if (doPacote.isEmpty()) "sem fontes declaradas" else resumo(doPacote),
                            aberto = abertos[chave] == true,
                            onToggle = { abertos[chave] = abertos[chave] != true },
                            acao = null,
                        ) {
                            if (doPacote.isEmpty()) {
                                Text(
                                    "Esta importação não declarou fontes. Gere o conteúdo de novo se quiser a rastreabilidade.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            } else {
                                doPacote.forEach { fonte ->
                                    SourceCard(
                                        fonte = fonte,
                                        topicTitle = fonte.topicId?.let { id -> topics.firstOrNull { it.id == id }?.title },
                                        onOpenTopic = { fonte.topicId?.let(onOpenTopic) },
                                        onOpenUrl = abrirUrl,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class GrupoDeFontes(
    val chave: String,
    val titulo: String,
    val apoio: String,
    val topicId: Long?,
    val ordem: Int,
    val ordemInterna: Int,
    val fontes: List<ContentSourceEntity>,
)

private fun resumo(fontes: List<ContentSourceEntity>): String {
    val oficiais = fontes.count { it.kind == SourceKind.OFICIAL }
    return "${fontes.size} fonte(s) • $oficiais oficial(is)"
}

/** Cabeçalho que abre e fecha. Fechado por padrão: a pessoa escolhe o que quer conferir. */
@Composable
private fun GrupoRecolhivel(
    titulo: String,
    apoio: String,
    resumo: String,
    aberto: Boolean,
    onToggle: () -> Unit,
    acao: (() -> Unit)?,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth().animateContentSize()) {
        Column(Modifier.padding(4.dp)) {
            Row(
                Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(titulo, fontWeight = FontWeight.Bold)
                    Text(apoio, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(resumo, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
                Icon(if (aberto) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (aberto) "Recolher" else "Expandir")
            }
            if (aberto) {
                Column(Modifier.padding(12.dp, 0.dp, 12.dp, 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    conteudo()
                    if (acao != null) TextButton(onClick = acao, contentPadding = PaddingValues(0.dp)) { Text("Abrir o tópico") }
                }
            }
        }
    }
}

@Composable
fun SourceCard(
    fonte: ContentSourceEntity,
    topicTitle: String?,
    onOpenTopic: () -> Unit,
    onOpenUrl: (String) -> Unit,
    showTopic: Boolean = true,
) {
    val oficial = fonte.kind == SourceKind.OFICIAL
    val cor = if (oficial) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (oficial) Icon(Icons.Outlined.VerifiedUser, null, Modifier.size(15.dp), tint = cor)
                Text(
                    if (oficial) "FONTE OFICIAL" else "FONTE COMPLEMENTAR",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = cor,
                )
            }
            Text(fonte.title, fontWeight = FontWeight.SemiBold)
            val detalhe = listOfNotNull(
                fonte.publisher.takeIf { it.isNotBlank() },
                fonte.reference.takeIf { it.isNotBlank() },
                fonte.accessedAt.takeIf { it.isNotBlank() }?.let { "acesso em $it" },
            ).joinToString(" • ")
            if (detalhe.isNotBlank()) Text(detalhe, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (showTopic && topicTitle != null) {
                Text(
                    topicTitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onOpenTopic),
                )
            }
            fonte.url?.takeIf { it.startsWith("https://") || it.startsWith("http://") }?.let { url ->
                TextButton(onClick = { onOpenUrl(url) }, contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Outlined.OpenInNew, null, Modifier.size(15.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Abrir a fonte", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
