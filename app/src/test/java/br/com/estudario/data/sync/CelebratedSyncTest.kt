package br.com.estudario.data.sync

import br.com.estudario.domain.Celebrated
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Animação vista num aparelho não aparece no outro depois de sincronizar (lista "celebrated" de userPrefs). */
class CelebratedSyncTest {
    private fun prefs(vararg keys: String) = JSONObject().put(Celebrated.SYNCED, JSONArray(keys.toList()))
    private fun JSONObject.celebrated(): Set<String> =
        getJSONArray(Celebrated.SYNCED).let { a -> (0 until a.length()).map { a.getString(it) }.toSet() }

    @Test
    fun mergeUneOQueCadaAparelhoViu() {
        val site = prefs(Celebrated.level(14), Celebrated.day(20_400))
        val phone = prefs(Celebrated.badge("streak-7"), Celebrated.tour("EDITAL"))
        for ((a, b) in listOf(site to phone, phone to site)) {
            val merged = SyncedPrefs.merge(a, b)!!.celebrated()
            assertEquals(setOf("level:14", "day:20400", "badge:streak-7", "tour:EDITAL"), merged)
        }
    }

    @Test
    fun foiVistoNoSiteNaoRepeteNoAppDepoisDaFoto() {
        val phoneBefore = prefs()
        assertFalse(Celebrated.level(14) in phoneBefore.celebrated())
        // O site comemorou o nível 14 e publicou; o celular junta a foto da conta com a sua.
        val account = JSONObject().put(SyncedPrefs.SNAPSHOT_KEY, prefs(Celebrated.level(14)))
        val local = JSONObject().put(SyncedPrefs.SNAPSHOT_KEY, phoneBefore)
        val merged = SnapshotMerge.merge(JSONObject(), local, account).getJSONObject(SyncedPrefs.SNAPSHOT_KEY)
        assertTrue(Celebrated.level(14) in merged.celebrated())
    }

    @Test
    fun nadaVistoSomeNaJuncao() {
        val a = prefs(Celebrated.level(3), Celebrated.streakSaved("2026-11-10"))
        val merged = SyncedPrefs.merge(a, prefs())!!.celebrated()
        assertTrue(merged.containsAll(a.celebrated()))
        assertEquals(a.celebrated(), SyncedPrefs.merge(prefs(), a)!!.celebrated())
    }

    @Test
    fun listasAntigasContamComoVistas() {
        val all = Celebrated.all(setOf("level:2"), legacyBadges = setOf("primeira"), legacyPieces = setOf("HAT:bone"))
        assertEquals(setOf("level:2", "badge:primeira", "piece:HAT:bone"), all)
    }
}
