// Apenas curvas constantes entram no cache; folhas animadas continuam transitórias.
private val bookPaths = HashMap<String, Path>()
private fun bookPath(svg: String): Path = bookPaths.getOrPut(svg) { PathParser().parsePathString(svg).toPath() }
private fun DrawScope.bookShape(svg: String, color: Color, width: Float = 1.6f) = inkShape(bookPath(svg), color, width)
private fun DrawScope.bookPen(svg: String, color: Color = RankInk, width: Float = 0.85f, alpha: Float = 1f) =
    pen(bookPath(svg), color.copy(alpha = color.alpha * alpha), width)
private fun DrawScope.bookFill(svg: String, color: Color, alpha: Float = 1f) = drawPath(bookPath(svg), color, alpha = alpha)

private fun DrawScope.rankDrawing(level: Int, c: RankColors, phase: Float, tap: Float) {
    val detail = (level / 5).coerceIn(0, 10)
    val active = tap > 0f && tap < 1f
    val lift = sin(tap * PI).toFloat()
    val paper = Color(0xFFF4E6C8)
    val ink = Color(0xFFAC997D)
    bookFill("M10 88C27 82 73 82 91 87C95 92 71 96 50 95C28 96 6 92 10 88Z", RankInk, 0.12f)
    val growth = 0.92f + detail * 0.006f
    scale(growth, growth, Offset(50f, 54f)) {
        val cover = "M7 30Q27 22 49 35Q72 21 92 28L95 79Q73 77 54 86Q50 90 46 86Q28 79 5 83Z"
        bookPen(cover, RankPaper, 5.5f)
        bookShape(cover, c.shade, 1.65f)
        bookShape("M7 30Q28 22 49 35Q71 22 92 28L93 74Q72 72 54 81Q50 85 46 81Q28 75 6 78Z", c.metal, 1.05f)
        bookFill("M7 74Q28 71 48 81L50 86Q28 78 6 81Z", c.shade)
        bookPen("M11 79Q30 76 46 82M56 82Q74 76 90 77", c.light, 0.75f)
        bookShape("M10 29Q29 23 49 36Q70 22 90 27L91 74Q71 72 51 84Q29 74 9 78Z", paper, 1.05f)
        bookPen("M13 73Q29 70 46 79M13 76Q29 73 45 81M56 79Q74 71 88 72M57 82Q74 74 88 75", ink, 0.55f)
        bookShape("M11 26C24 20 37 24 49 33L50 77C36 68 23 67 10 72Z", Color(0xFFFFF4D9), 1.25f)
        bookShape("M49 33C62 22 77 19 90 24L91 70C77 65 63 69 50 77Z", RankPaper, 1.25f)
        bookFill("M43 29L49 33L50 77L44 73Q47 54 43 29Z", Color(0xFFE7D6AF), 0.65f)
        bookFill("M49 33L55 29Q53 53 55 74L50 77Z", Color(0xFFE7D6AF), 0.4f)
        bookPen("M14 29Q28 25 40 31M60 29Q76 23 86 26", Color.White, 0.8f, 0.8f)
        if (detail >= 1) {
            bookPen("M24 64C27 57 27 48 24 41", c.shade, 0.85f)
            bookShape("M25 50C18 49 16 44 18 40C24 39 28 44 25 50Z", c.light, 0.75f)
            bookPen("M19 42L25 49", c.shade, 0.55f)
            littleSpark(76f, 47f, 4.4f, c.light)
        }
        if (detail >= 2) {
            bookShape("M26 58C25 51 29 47 33 47C36 52 32 57 26 58Z", c.light, 0.75f)
            bookPen("M32 49L27 56", c.shade, 0.55f)
            drawCircle(c.shade, 8f, Offset(76f, 47f), style = Stroke(0.7f))
            drawCircle(c.enamel, 1.15f, Offset(76f, 47f))
            val curl = sin(phase * 2f) * (0.25f + detail * 0.04f) + lift * 2f
            val fold = Path().apply {
                moveTo(80f - curl, 23f); quadraticTo(85f, 22f, 90f, 24f)
                lineTo(90f, 34f + curl); quadraticTo(87f - curl, 27f, 80f - curl, 23f); close()
            }
            inkShape(fold, paper, 0.75f)
            bookPen("M86 26L88 29", Color.White, 0.65f)
        }
        if (detail >= 3) {
            bookPen("M16 34L16 31Q23 28 30 32M70 31Q79 26 85 29L85 32M16 65L16 69Q25 68 32 72M70 72Q79 67 85 68L85 64", c.shade, 0.65f)
            bookPen("M76 36L76 38M76 56L76 58M65 47L67 47M85 47L87 47", c.shade, 0.7f)
            bookPen("M15 76Q29 73 41 79M60 79Q76 72 88 73", c.light, 0.6f)
        }
        if (detail >= 4) {
            val corners = "M7 66L6 79Q13 77 21 79M92 62L94 76Q87 75 79 78"
            bookPen(corners, RankInk, 2.7f); bookPen(corners, c.light, 1.25f)
            bookShape("M91 43L96 42L97 50L91 52Z", Color(0xFFBCA4DF), 0.75f)
        }
        if (detail >= 5) {
            bookShape("M25 63C19 63 17 60 18 57C23 55 27 59 25 63Z", c.enamel, 0.65f)
            bookPen("M19 58L24 62", c.light, 0.55f)
            bookPen("M48 67L52 69M48 72L52 74M48 77L52 79", c.shade, 0.7f)
        }
        if (detail >= 6) {
            bookShape("M80 60C73 58 71 68 79 69C75 66 76 62 80 60Z", c.metal, 0.65f)
            drawCircle(c.enamel, 0.7f, Offset(70f, 61f))
            drawCircle(c.enamel, 0.7f, Offset(84f, 65f))
        }
        if (detail >= 7) {
            bookPen("M13 78Q29 76 42 82M59 82Q76 75 89 76", c.shade, 0.6f)
            bookPen("M14 80Q29 78 41 84M60 84Q76 77 89 78", c.light, 0.6f)
        }
        if (detail >= 8) {
            bookPen("M8 43L8 34Q15 31 22 33M91 40L91 32Q85 29 78 32", c.light, 0.9f)
            bookPen("M12 39Q14 34 18 35M89 37Q85 32 82 34", c.enamel, 0.6f)
            bookShape("M91 55L96 54L97 62L91 64Z", Color(0xFFD69778), 0.7f)
        }
        if (detail >= 9) rotate(sin(phase * 2f) * 2.5f + sin(tap * 5 * PI).toFloat() * (1f - tap) * 8f, Offset(83f, 75f)) {
            val cord = "M83 75C93 76 94 83 89 87Q87 89 88 91"
            bookPen(cord, RankInk, 1.9f); bookPen(cord, c.light, 0.95f)
            bookPen("M84 76C91 78 92 83 88 87", c.shade, 0.45f)
            drawCircle(c.metal, 1.65f, Offset(88f, 90f))
            drawCircle(RankInk, 1.65f, Offset(88f, 90f), style = Stroke(0.7f))
            val threads = "M86 92Q84 95 84 98M87 92Q86 95 86 98M88 92L88 99M89 92Q90 95 90 98M90 92Q92 95 92 97"
            bookPen(threads, RankInk, 1.35f); bookPen(threads, c.light, 0.65f)
            bookPen("M85.5 92L90.5 92", c.shade, 0.8f)
        }
        if (detail >= 10) {
            bookShape("M19 30L22 27L25 30L22 32Z", c.light, 0.5f)
            bookShape("M77 28L80 25L83 27L80 30Z", c.light, 0.5f)
        }
        if (level >= 100) bookPen("M15 81Q29 79 40 85M61 85Q76 78 89 79", c.light, 0.65f)
        if (level >= 150) {
            bookPen("M15 35L15 39M18 34L18 37M83 34L83 37M86 33L86 37", c.light, 0.65f)
            drawCircle(c.light, 10f, Offset(76f, 47f), style = Stroke(0.5f))
        }
        if (level >= 200) bookPen("M14 35L14 39M17 34L17 38M84 33L84 37M87 32L87 36", c.light, 0.7f)
        if (active) {
            val turn = (tap * (1 + detail / 4)) % 1f
            val span = 40f * kotlin.math.cos(turn * PI).toFloat()
            val up = sin(turn * PI).toFloat() * 11f
            val shadow = Path().apply {
                moveTo(50f, 34f); lineTo(50f + span * 0.9f, 27f)
                lineTo(50f + span * 0.9f, 72f); lineTo(50f, 78f); close()
            }
            drawPath(shadow, RankInk, alpha = sin(turn * PI).toFloat() * 0.13f)
            val sheet = Path().apply {
                moveTo(50f, 33f)
                cubicTo(50f + span * 0.38f, 26f - up, 50f + span, 20f - up, 50f + span, 25f)
                lineTo(50f + span, 70f)
                cubicTo(50f + span * 0.8f, 66f - up * 0.25f, 50f + span * 0.4f, 70f, 50f, 77f); close()
            }
            inkShape(sheet, if (turn < 0.5f) RankPaper else Color(0xFFF5E7CB), 0.85f)
            pen(Path().apply { moveTo(50f, 34f); quadraticTo(50f + span * 0.2f, 51f - up * 0.25f, 50f, 75f) }, Color(0xFFD6C19B), 0.55f)
        }
    }
    rotate(bookmarkSway(detail, phase, tap), Offset(50f, 20f)) {
        bookFill("M39 20Q51 17 65 20L65 66L52 61L39 68Z", RankInk, 0.15f)
        val ribbon = "M37 19Q49 15 63 18L63 64L50 58L37 66Z"
        bookPen(ribbon, RankPaper, 3.8f); bookShape(ribbon, c.enamel, 1.55f)
        bookFill("M37 19L40 18L40 61L37 66Z", c.shade, 0.35f)
        bookPen("M41 22Q50 19 59 21", if (level < 5) Color(0xFFC2B4A2) else c.light, 0.7f, 0.7f)
        if (detail >= 3) bookPen("M40 27L40 55M60 26L60 55", c.light, 0.55f, 0.45f)
        if (detail >= 4) {
            bookShape("M37 19Q49 15 63 18L63 23Q50 20 37 24Z", c.metal, 0.8f)
            bookPen("M42 20Q50 18 59 19", c.light, 0.65f)
        }
        if (detail >= 7) bookPen("M45 56L50 53L55 56", c.light, 0.6f)
        if (detail >= 10) littleSpark(50f, 20f, 1.8f, c.light)
        if (level >= 100) {
            drawCircle(c.enamel, 0.65f, Offset(42f, 21f)); drawCircle(c.enamel, 0.65f, Offset(59f, 20f))
        }
    }
}

private fun DrawScope.bookLights(detail: Int, c: RankColors, phase: Float, tap: Float) {
    if (detail >= 6) repeat((detail - 5).coerceAtMost(2)) { i ->
        val travel = (phase / (2 * PI).toFloat() + i * 0.46f) % 1f
        littleSpark(if (i == 0) 19f else 83f, 26f - travel * 9f, 1.1f + detail * 0.035f, c.light, sin(travel * PI).toFloat() * 0.55f)
    }
    if (tap > 0f && tap < 1f && detail >= 2) littleSpark(84f + tap * 3f, 27f - tap * 8f, 1.7f, c.light, sin(tap * PI).toFloat() * 0.8f)
}
