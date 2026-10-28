package br.com.estudario.ui.help

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.components.MarkColors
import br.com.estudario.ui.studio.Gx
import br.com.estudario.ui.studio.GxKicker
import br.com.estudario.ui.studio.GxLaunch
import br.com.estudario.ui.studio.GxPanel
import br.com.estudario.ui.studio.GxSection

/**
 * Os "?" das telas (iguais aos do site): uma janela do estúdio que explica tudo o que a tela faz,
 * com os limites de verdade, para ninguém deixar de usar uma função por não saber que ela existe.
 */

/** O botão redondo "?" que abre a ajuda da tela. */
@Composable
fun HelpButton(label: String, onClick: () -> Unit) {
    Box(
        Modifier.padding(4.dp).size(36.dp).shadow(6.dp, CircleShape, ambientColor = Color(0xFFA78BFF), spotColor = Color(0xFFA78BFF))
            .clip(CircleShape).background(Brush.verticalGradient(listOf(Color.White, Color(0xFFCFC4FF))))
            .clickable(onClick = onClick).semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) { Text("?", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A1440)) }
}

@Composable
private fun HelpDialog(kicker: String, title: String, subtitle: String, cta: String, onClose: () -> Unit, extra: (@Composable () -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    br.com.estudario.ui.studio.GxDialog(onClose) {
        run {
            Row(Modifier.fillMaxWidth().padding(start = 18.dp, end = 10.dp, top = 16.dp, bottom = 6.dp), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    GxKicker(kicker)
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Gx.strong)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Gx.mute)
                }
                Box(Modifier.size(40.dp).clip(RoundedCornerShape(13.dp)).background(Gx.tint(0.06f)).clickable(onClick = onClose), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Close, "Fechar", tint = Gx.strong)
                }
            }
            Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
            Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                GxLaunch(cta, glyph = Glyph.Check, onClick = onClose)
                extra?.invoke()
            }
        }
    }
}

/** Itens com bolinha na cor da parte; o texto antes de ": " fica em negrito. */
@Composable
private fun Points(color: Color, vararg items: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.Top) {
                Box(Modifier.padding(top = 7.dp, end = 10.dp).size(6.dp).clip(CircleShape).background(color))
                val cut = item.indexOf(": ").takeIf { it in 1..40 }
                Text(
                    buildAnnotatedString {
                        if (cut != null) {
                            withStyle(SpanStyle(fontWeight = FontWeight.Black, color = Gx.ink)) { append(item.substring(0, cut + 1)) }
                            append(item.substring(cut + 1))
                        } else append(item)
                    },
                    style = MaterialTheme.typography.bodyMedium, color = Gx.soft,
                )
            }
        }
    }
}

/** Etiquetas coloridas (cores do grifo, degraus da escada). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Chips(items: List<Pair<String, Color>>) {
    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { (label, color) ->
            Text(
                label,
                Modifier.clip(RoundedCornerShape(50)).background(color).border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color(0xFF1A1440), maxLines = 1,
            )
        }
    }
}

// ------------------------------------------------------------------ leitura do tópico

/** O "?" da leitura da teoria e da página do tópico: grifar, anotar, ajustar a letra e onde tudo fica. */
@Composable
fun ReaderHelp(onClose: () -> Unit) {
    HelpDialog("Como usar a leitura", "Tudo o que dá para fazer aqui", "Grifos e notas vão para o Caderno de estudo e aparecem no app e no site.", "Entendi, bora ler", onClose) {
        GxSection(Glyph.OpenBook, "Ler e voltar de onde parou", "Ao abrir de novo, a leitura volta onde você parou (o aviso tem \"Do início\" se quiser recomeçar).", Gx.violet) {
            Points(Gx.violet,
                "Índice: o botão ao lado do Aa mostra os capítulos (quando há mais de um) e marca os que têm grifo.",
                "A linha no topo mostra quanto você já leu. No fim do texto, a leitura conta como 100%.",
            )
        }
        GxSection(Glyph.Pencil, "Grifar", "Não tem limite de grifos. São 3 cores:", Gx.gold) {
            Chips(MarkColors.all.map { it.label to it.color })
            Points(Gx.gold,
                "Só um trecho: segure uma palavra, arraste até onde quiser e escolha a cor no menu que aparece (ou \"Grifar com nota\").",
                "O parágrafo inteiro: toque nele e use \"Grifar tudo\". Ali também tem Anotar, Copiar e Reportar.",
                "Já grifado: toque no grifo para trocar a cor, anotar ou tirar o grifo. Grifar por cima troca a cor e mantém a nota.",
            )
        }
        GxSection(Glyph.Notebook, "Anotar num trecho", "Abre uma folha de caderno para escrever com suas palavras.", Gx.cyan) {
            Points(Gx.cyan,
                "Etiquetas: Macete, Resumo, Dúvida, Pegadinha ou Letra da lei (uma por nota).",
                "Até 4000 caracteres por nota. A nota aparece num balão embaixo do parágrafo; toque para ler ou editar.",
                "Notas do tópico inteiro: na página do tópico, em \"Minhas anotações\".",
            )
        }
        GxSection(Glyph.Gear, "Aa: ajustar a leitura", "4 tamanhos de letra, fonte Livro ou Simples, página Normal, Sépia ou Noite e \"Só texto\" (sem o Folha nem nada se mexendo, para ler muito tempo).", Gx.mint) {}
        GxSection(Glyph.Bulb, "Caixas que importam", "\"Atenção\" marca pegadinha e erro comum; \"Dica\" marca bizu e macete. Vale ler com calma.", Gx.orange) {}
        GxSection(Glyph.Timer, "Foco e concluir o estudo", "\"Modo foco\" conta o seu tempo de estudo. \"Concluir estudo\" marca o tópico como estudado e agenda as revisões de 1, 7 e 30 dias (depois o intervalo cresce).", Gx.pink) {}
        GxSection(Glyph.Cards, "As outras abas do tópico", null, Gx.violet) {
            Points(Gx.violet,
                "Revisão: flashcards, resumos e memorização ativa.",
                "Dicas: dicas que decidem questão e pegadinhas da banca (★ guarda no Caderno).",
                "Questões, Erros e Histórico: treine o tópico, veja onde você erra e tudo o que já fez nele.",
            )
        }
        GxSection(Glyph.Books, "Onde tudo fica depois", "No Caderno de estudo, em Grifos e Anotações, com \"Abrir na teoria\" para voltar ao trecho. Os grifos também voltam em cartão (\"Grifos de hoje\") depois de 1, 3, 7, 15 e 30 dias; os rosa (Revisar) aparecem sempre.", Gx.mint) {}
    }
}

// ------------------------------------------------------------------ revisões

/** O "?" das revisões: o que são as etapas, de quanto em quanto tempo voltam e o que cada botão faz. */
@Composable
fun ReviewsHelp(onClose: () -> Unit) {
    HelpDialog("Como funciona", "Revisão espaçada, etapa por etapa", "A gente esquece rápido o que estuda. Revisar na hora certa, um pouco antes de esquecer, é o que faz o conteúdo ficar.", "Entendi", onClose) {
        GxSection(Glyph.OpenBook, "Estudou um tópico, nasceu uma revisão", "Cada tópico que você conclui entra aqui sozinho, com a data certa de voltar.", Gx.violet) {}
        GxSection(Glyph.Calendar, "O que é a etapa", "É a vez daquela revisão: 1ª, 2ª, 3ª... A cada etapa feita, o tópico demora mais para voltar.", Gx.cyan) {
            Chips(listOf("1ª · 1 dia" to Gx.violet, "2ª · 7 dias" to Gx.cyan, "3ª · 30 dias" to Gx.mint))
            Points(Gx.cyan, "Depois da 3ª, o intervalo continua crescendo.")
        }
        GxSection(Glyph.Star, "Como foi? Você decide o ritmo", "Difícil: a mesma etapa volta amanhã. Normal: avança no tempo de sempre. Fácil: avança e o próximo encontro fica mais longe.", Gx.gold) {}
        GxSection(Glyph.Gear, "A engrenagem", "Quantas questões (até 50) e flashcards (até 30) entram em cada revisão, a ordem das questões e se oferece o resumo e os seus grifos. Na sessão dá para escolher Rápida, Equilibrada ou Completa.", Gx.mint) {}
        GxSection(Glyph.Warning, "Atrasadas vêm primeiro", "Passou do dia? Ela aparece como atrasada no topo. \"Pular\" tira essa revisão da lista: use só quando o tópico não importar mais.", Gx.red) {}
    }
}

// ------------------------------------------------------------------ caderno de erros

/** O "?" do caderno de erros: a Virada, a escada de 3, 10 e 30 dias e como usar a tela. */
@Composable
fun ViradaHelp(onClose: () -> Unit) {
    HelpDialog("Como funciona", "A Virada: erro que vira acerto", "O caderno de erros não é um depósito. Cada erro é uma questão que você ainda vai acertar na prova.", "Entendi, bora virar", onClose) {
        GxSection(Glyph.Oops, "1. Errou, foi para a mesa", "Toda questão que você erra vem para cá sozinha, com o tópico e o motivo do erro.", Gx.red) {}
        GxSection(Glyph.Cycle, "2. Sobe a escada de reencontro", "Acertou de novo? Sobe um degrau e volta daqui a 3 dias, depois 10, depois 30. Errou no caminho, volta para a mesa.", Gx.gold) {
            Chips(listOf("Mesa" to Gx.red, "3 d" to Gx.gold, "10 d" to Gx.cyan, "30 d" to Gx.violet, "Virou!" to Gx.mint))
        }
        GxSection(Glyph.Check, "3. Acertou os três degraus, virou", "Só vira quem acertou nos três reencontros: aí o conteúdo ficou de verdade. O placar mostra quantos já viraram.", Gx.mint) {}
        GxSection(Glyph.Target, "Missão de hoje", "Todo dia eu separo até 5 erros: os da mesa e os que vencem hoje. Uns 6 minutos.", Gx.violet) {}
        GxSection(Glyph.Bulb, "Por que você erra", "Ao errar de novo, marque o motivo (não sabia, confundi conceitos, li rápido, caí na pegadinha). Eu junto o padrão e dou a dica certa.", Gx.cyan) {}
        GxSection(Glyph.Books, "Por tópico", "Em \"Seus erros por tópico\" você vira só os erros de um tópico ou abre a teoria dele.", Gx.orange) {}
    }
}

// ------------------------------------------------------------------ treinar

/** O "?" do Treinar: os modos, os filtros e o que dá para fazer durante a bateria. [onTour] mostra o passo a passo na tela. */
@Composable
fun TrainHelp(onClose: () -> Unit, onTour: (() -> Unit)? = null) {
    HelpDialog(
        "Como funciona", "Treinar do seu jeito", "Escolha o modo, filtre se quiser e comece. O que você erra vai para o caderno de erros sozinho.", "Entendi, bora treinar", onClose,
        extra = onTour?.let { tour -> { TextButton(onClick = { onClose(); tour() }, modifier = Modifier.fillMaxWidth()) { Text("Ver o passo a passo na tela", color = Gx.mute) } } },
    ) {
        GxSection(Glyph.Chart, "Seu diagnóstico", "O medidor mostra seu acerto geral. Embaixo, até 3 treinos prontos (o primeiro é o recomendado agora) e as matérias da mais fraca para a mais forte: toque numa para treinar só ela.", Gx.cyan) {}
        GxSection(Glyph.Target, "Os modos", "Como as questões são escolhidas:", Gx.violet) {
            Points(Gx.violet,
                "Treino inteligente: mistura seus erros, os tópicos mais fracos e questões novas.",
                "Nunca acertadas: as que você ainda não acertou nenhuma vez.",
                "Mais erradas: onde está o ponto fraco de verdade.",
                "Questões novas: só as que você nunca respondeu.",
                "Favoritas: as que você marcou com o coração.",
                "Aleatórias: sorteio livre.",
            )
        }
        GxSection(Glyph.Gear, "Filtros", "Matéria e tópico, dificuldade (mista, fácil, média ou difícil) e banca. A quantidade vai de 5 a 50 questões.", Gx.gold) {}
        GxSection(Glyph.Clipboard, "Respondendo", null, Gx.mint) {
            Points(Gx.mint,
                "Riscar alternativa: arraste a alternativa para o lado. Riscada, tocar desfaz o risco.",
                "Coração: guarda a questão para o modo Favoritas.",
                "Reportar: achou erro na questão? O botão fica no topo dela.",
                "Explicação na hora ou no fim: escolha em Ajustes, \"Explicação logo após responder\".",
            )
        }
        GxSection(Glyph.Cycle, "Errou? Não se perde", "As erradas vão para o caderno de erros e sobem a escada da Virada (3, 10 e 30 dias) quando você acerta de novo.", Gx.red) {}
    }
}
