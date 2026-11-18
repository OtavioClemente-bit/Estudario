package br.com.estudario.web.data

import br.com.estudario.domain.Celebrated
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * A lista "celebrated" da conta: o que o app mostrou não aparece de novo no site, e o que o site grava
 * é refeito por cima da versão da conta (Store) somando, sem apagar o que o app viu.
 */
class CelebratedSyncTest {
    @Test
    fun vistoNoAppNaoRepeteNoSite() {
        val local = Snapshot.empty()
        assertFalse(Celebrated.level(14) in Prefs.celebratedIn(local))
        // O celular comemorou o nível 14 e o dia, e publicou.
        val account = Prefs.withSynced(local, Celebrated.SYNCED, setOf(Celebrated.level(14), Celebrated.day(20_400)))
        val seen = Prefs.celebratedIn(account)
        assertTrue(Celebrated.level(14) in seen)
        assertTrue(Celebrated.day(20_400) in seen)
    }

    @Test
    fun gravacaoDoSiteRefeitaPorCimaDaContaUne() {
        val siteEdit: (Snapshot) -> Snapshot = { Prefs.withSynced(it, Celebrated.SYNCED, setOf(Celebrated.badge("streak-7"))) }
        val account = Prefs.withSynced(Snapshot.empty(), Celebrated.SYNCED, setOf(Celebrated.tour("EDITAL")))
        val replayed = siteEdit(account)
        assertEquals(setOf("tour:EDITAL", "badge:streak-7"), Prefs.celebratedIn(replayed))
    }

    @Test
    fun listasAntigasContamComoVistas() {
        var data = Prefs.withSynced(Snapshot.empty(), Celebrated.LEGACY_BADGES, setOf("primeira"))
        data = Prefs.withSynced(data, Celebrated.LEGACY_PIECES, setOf("HAT:bone"))
        assertEquals(setOf("badge:primeira", "piece:HAT:bone"), Prefs.celebratedIn(data))
    }
}
