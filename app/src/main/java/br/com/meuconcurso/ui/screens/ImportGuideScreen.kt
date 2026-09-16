package br.com.meuconcurso.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.meuconcurso.data.preferences.PromptTemplates
import br.com.meuconcurso.ui.AppViewModel

@Composable
fun ImportGuideScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val savedEdital by viewModel.editalPrompt.collectAsState()
    val savedContent by viewModel.contentPrompt.collectAsState()
    var edital by remember(savedEdital) { mutableStateOf(savedEdital) }
    var content by remember(savedContent) { mutableStateOf(savedContent) }
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    fun copy(value: String) { clipboard.setText(AnnotatedString(value)); Toast.makeText(context, "Prompt copiado", Toast.LENGTH_SHORT).show() }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row {
                IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") }
                Column { Text("Modelos para gerar estudo com IA", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("Use no GPT, Claude, Gemini ou outra IA") }
            }
        }
        item {
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Fluxo recomendado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("1. Copie o prompt e envie para a IA que preferir, junto com o edital ou texto-base.")
                    Text("2. Gere um pacote por tópico com livro, resumo completo, revisão rápida, memorização e questões.")
                    Text("3. Peça somente o JSON no formato indicado, salve como .estudo e importe no app.")
                }
            }
        }
        item {
            PromptEditor("1. Prompt da estrutura do edital", "Cria matérias, tópicos e subtópicos, ainda sem conteúdo.", edital, { edital = it }, { copy(edital) }, { viewModel.saveEditalPrompt(edital); Toast.makeText(context, "Modelo salvo", Toast.LENGTH_SHORT).show() }, { edital = PromptTemplates.EDITAL; viewModel.saveEditalPrompt(edital) })
        }
        item {
            PromptEditor("2. Prompt do pacote completo", "Gera livro, resumo completo, revisão rápida, bizus, pegadinhas, recuperação ativa, questões e conceitos de erro.", content, { content = it }, { copy(content) }, { viewModel.saveContentPrompt(content); Toast.makeText(context, "Modelo salvo", Toast.LENGTH_SHORT).show() }, { content = PromptTemplates.CONTEUDO; viewModel.saveContentPrompt(content) })
        }
        item {
            Text("Depois de gerar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Depois que a IA responder, não copie explicações nem blocos de código: salve apenas o JSON puro em UTF-8 com extensão .estudo. Use Mais › Importar pacote .estudo. Se reutilizar o packageId, escolha Atualizar para preservar seu histórico ou Criar cópia para manter as duas versões.")
        }
    }
}

@Composable
private fun PromptEditor(title: String, explanation: String, value: String, onValueChange: (String) -> Unit, onCopy: () -> Unit, onSave: () -> Unit, onRestore: () -> Unit) {
    ElevatedCard {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(explanation, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(value, onValueChange, Modifier.fillMaxWidth(), label = { Text("Modelo editável") }, minLines = 10, maxLines = 18)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onRestore) { Icon(Icons.Outlined.Restore, "Restaurar original") }
                OutlinedButton(onClick = onCopy, modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.ContentCopy, null); Spacer(Modifier.width(4.dp)); Text("Copiar") }
                Button(onClick = onSave, enabled = value.isNotBlank(), modifier = Modifier.weight(1f)) { Icon(Icons.Outlined.Save, null); Spacer(Modifier.width(4.dp)); Text("Salvar") }
            }
        }
    }
}
