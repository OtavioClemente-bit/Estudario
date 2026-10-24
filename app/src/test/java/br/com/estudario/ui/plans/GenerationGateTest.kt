package br.com.estudario.ui.plans

import br.com.estudario.data.ai.AiFreeAdAdvances
import br.com.estudario.data.ai.AiFreeAllowance
import br.com.estudario.data.ai.AiFreeCount
import br.com.estudario.data.ai.AiPlanSummary
import br.com.estudario.data.ai.AiPlanUsage
import org.junit.Assert.assertEquals
import org.junit.Test

class GenerationGateTest {
    private fun usage(feature: String, remaining: Int) = AiPlanUsage(feature, "MONTHLY", 10, 10 - remaining, remaining, "2026-10-01")

    private fun free(planDayUsed: Int = 0, weeklyUsed: Int = 1, credits: Int = 0, adsToday: Int = 0, batches: Int = 3) = AiPlanSummary(
        planTier = "FREE",
        usage = listOf(usage("QUESTION_BATCH", batches)),
        freeAllowance = AiFreeAllowance(
            planDay = AiFreeCount(planDayUsed, 2),
            weeklyAdvance = AiFreeCount(weeklyUsed, 1),
            adAdvances = AiFreeAdAdvances(enabled = true, today = adsToday, dailyLimit = 2, month = adsToday, monthlyLimit = 5, available = credits),
        ),
    )

    @Test fun `free advancing without credit or weekly shows the ad before the wizard`() {
        assertEquals(GenerationGate.NeedsAd::class, generationGate(free(), "ADVANCE", false)::class)
    }

    @Test fun `free advancing with a confirmed ad credit or the weekly advance goes straight`() {
        assertEquals(GenerationGate.Go, generationGate(free(credits = 1), "ADVANCE", false))
        assertEquals(GenerationGate.Go, generationGate(free(weeklyUsed = 0), "ADVANCE", false))
    }

    @Test fun `free with no ads left today gets the offer, not a dead end`() {
        assertEquals(GenerationGate.Blocked(UpgradeReason.ADVANCE), generationGate(free(adsToday = 2), "ADVANCE", false))
    }

    @Test fun `free plan day and outside the plan`() {
        assertEquals(GenerationGate.Go, generationGate(free(planDayUsed = 1), "PLAN_DAY", false))
        assertEquals(GenerationGate.Blocked(UpgradeReason.PLAN_DAY), generationGate(free(planDayUsed = 2), "PLAN_DAY", false))
        assertEquals(GenerationGate.Blocked(UpgradeReason.EXTRAS), generationGate(free(), "EXTRA", false))
    }

    @Test fun `question batches and paid content stop when the month is over`() {
        assertEquals(GenerationGate.Blocked(UpgradeReason.QUESTIONS), generationGate(free(batches = 0), "EXTRA", true))
        val paid = AiPlanSummary(planTier = "ESSENCIAL", usage = listOf(usage("CONTENT_GENERATION", 0), usage("QUESTION_BATCH", 4)))
        assertEquals(GenerationGate.Blocked(UpgradeReason.CONTENT), generationGate(paid, "EXTRA", false))
        assertEquals(GenerationGate.Go, generationGate(paid, "EXTRA", true))
    }

    private fun paid(contentLeft: Int, planToday: Int) = AiPlanSummary(
        planTier = "ESSENCIAL",
        usage = listOf(usage("CONTENT_GENERATION", contentLeft)),
        planToday = AiFreeCount(planToday, 4),
    )

    @Test fun `paid plan materials are free up to four a day, even with the month used up`() {
        assertEquals(GenerationGate.Go, generationGate(paid(contentLeft = 5, planToday = 3), "PLAN_DAY", false))
        assertEquals(GenerationGate.Go, generationGate(paid(contentLeft = 0, planToday = 3), "ADVANCE", false))
    }

    @Test fun `paid fifth plan material asks first and spends the month, none left means the offer`() {
        assertEquals(GenerationGate.OverDailyPlan(4, 5), generationGate(paid(contentLeft = 5, planToday = 4), "PLAN_DAY", false))
        assertEquals(GenerationGate.Blocked(UpgradeReason.CONTENT), generationGate(paid(contentLeft = 0, planToday = 4), "ADVANCE", false))
        assertEquals(GenerationGate.Blocked(UpgradeReason.CONTENT), generationGate(paid(contentLeft = 0, planToday = 0), "EXTRA", false))
    }
}
