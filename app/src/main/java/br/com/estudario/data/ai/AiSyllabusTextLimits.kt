package br.com.estudario.data.ai

/** UTF-16 limits shared with syllabus-text-limits.ts. Never shorten source items.
 * 4,000 accommodates literal compound paragraphs while bounding each of the
 * six ancestor/leaf titles sent to subsequent content generation.
 */
object AiSyllabusTextLimits {
    const val SHORT_NAME = 200
    const val SOURCE_TITLE = 4_000
    const val DESCRIPTION = 8_000

    fun validShortName(value: String): Boolean = valid(value, SHORT_NAME, multiline = false)
    fun validSourceTitle(value: String): Boolean = valid(value, SOURCE_TITLE, multiline = true)
    fun validDescription(value: String): Boolean = valid(value, DESCRIPTION, multiline = true)

    private fun valid(value: String, limit: Int, multiline: Boolean): Boolean =
        value.isNotBlank() && value.length <= limit && value.none {
            it.isISOControl() && !(multiline && it in "\n\r\t")
        }

    internal fun contentTitle(value: String): String {
        require(validSourceTitle(value)) { "topicPath: invalid source title" }
        return value
    }
}
