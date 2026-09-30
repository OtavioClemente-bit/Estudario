package br.com.estudario.data

import br.com.estudario.BuildConfig
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.Difficulty
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionOptionEntity
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.TheoryMarkEntity
import br.com.estudario.data.local.TopicSnippetEntity
import br.com.estudario.data.local.UserNoteEntity

/**
 * Só nas versões de desenvolvimento (debug e "Estudário Teste"): carrega a demonstração e alguns
 * itens salvos, para testar Caderno, treino e simulado sem precisar gerar nada. Disparado por
 * `adb shell am start -n br.com.estudario.teste/br.com.estudario.MainActivity --ez seedDemo true`.
 */
object DebugSeed {
    suspend fun run(db: AppDatabase, repository: StudyRepository) {
        if (!BuildConfig.DEBUG) return
        repository.loadDemoData()
        val dao = db.dao()
        val theory = dao.theoriesOnce().firstOrNull { it.externalId == "demo-teoria-hash" } ?: return
        if (dao.theoryMarksOnce().any { it.theoryId == theory.id }) return
        val topicId = theory.topicId
        dao.insertTheoryMark(TheoryMarkEntity(theoryId = theory.id, blockIndex = 2, quote = theory.markdown.split("\n\n").getOrElse(2) { theory.title }, note = "Cai muito: efeito avalanche."))
        dao.insertTheoryMark(TheoryMarkEntity(theoryId = theory.id, blockIndex = 4, quote = theory.markdown.split("\n\n").getOrElse(4) { theory.title }, note = ""))
        listOf(
            "Em relação às funções hash criptográficas, a resistência à colisão garante que é computacionalmente inviável encontrar duas entradas distintas com o mesmo resumo." to "C",
            "Uma função hash pode ser revertida com a chave correta, recuperando a mensagem original." to "E",
        ).forEachIndexed { index, (statement, correct) ->
            val id = dao.insertQuestion(QuestionEntity(topicId = topicId, externalId = "debug-q-$index", board = "Cebraspe", year = 2024, difficulty = Difficulty.MEDIA, statement = statement, explanation = "Hash não cifra.", isFavorite = true))
            dao.insertOptions(listOf(QuestionOptionEntity(questionId = id, key = "C", text = "Certo", isCorrect = correct == "C", position = 0), QuestionOptionEntity(questionId = id, key = "E", text = "Errado", isCorrect = correct == "E", position = 1)))
        }
        dao.insertSnippet(TopicSnippetEntity(topicId = topicId, kind = SnippetKind.BIZU, text = "Hash garante integridade, não confidencialidade.", isFavorite = true))
        dao.insertSnippet(TopicSnippetEntity(topicId = topicId, kind = SnippetKind.PEGADINHA, text = "\"Descriptografar o hash\" não existe: a banca troca hash por cifra.", isFavorite = true))
        dao.insertSnippet(TopicSnippetEntity(topicId = topicId, kind = SnippetKind.RECUPERACAO, text = "O que é efeito avalanche?", answer = "Mudar um bit da entrada muda muito a saída.", isFavorite = true, externalId = "flashcard:debug:1"))
        dao.insertNote(UserNoteEntity(text = "Revisar SHA-256 x MD5 antes da prova.", topicId = topicId))
    }
}
