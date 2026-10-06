package br.com.estudario.data.sync

import br.com.estudario.data.sync.SyncPlanner.Action
import br.com.estudario.data.sync.SyncPlanner.Input
import org.junit.Assert.assertEquals
import org.junit.Test

class SyncPlannerTest {
    private fun input(
        baseRevision: Long? = 3,
        baseSha: String? = "base",
        localSha: String = "base",
        localEmpty: Boolean = false,
        remoteRevision: Long = 3,
        remoteSha: String? = "base",
    ) = Input(baseRevision, baseSha, localSha, localEmpty, remoteRevision, remoteSha)

    @Test fun nothingChanged() = assertEquals(Action.Nothing, SyncPlanner.decide(input()))

    @Test fun onlyLocalChangedUploads() = assertEquals(Action.Upload, SyncPlanner.decide(input(localSha = "new")))

    @Test fun onlyRemoteChangedDownloads() =
        assertEquals(Action.Download, SyncPlanner.decide(input(remoteRevision = 4, remoteSha = "other")))

    @Test fun bothChangedIsConflict() =
        assertEquals(Action.Conflict, SyncPlanner.decide(input(localSha = "mine", remoteRevision = 4, remoteSha = "theirs")))

    @Test fun bothChangedToSameContentAdoptsRemote() =
        assertEquals(Action.AdoptRemote, SyncPlanner.decide(input(localSha = "same", remoteRevision = 4, remoteSha = "same")))

    @Test fun emptyAccountReceivesFirstUpload() =
        assertEquals(Action.Upload, SyncPlanner.decide(input(baseRevision = null, baseSha = null, remoteRevision = 0, remoteSha = null)))

    @Test fun emptyAccountAndEmptyDeviceDoNothing() =
        assertEquals(Action.Nothing, SyncPlanner.decide(input(baseRevision = null, baseSha = null, localEmpty = true, remoteRevision = 0, remoteSha = null)))

    @Test fun newDeviceWithoutDataDownloads() =
        assertEquals(Action.Download, SyncPlanner.decide(input(baseRevision = null, baseSha = null, localSha = "empty", localEmpty = true, remoteRevision = 7, remoteSha = "cloud")))

    @Test fun newDeviceWithItsOwnDataAsksFirst() =
        assertEquals(Action.Conflict, SyncPlanner.decide(input(baseRevision = null, baseSha = null, localSha = "phone", remoteRevision = 7, remoteSha = "cloud")))

    @Test fun newDeviceWithIdenticalDataAdoptsRemote() =
        assertEquals(Action.AdoptRemote, SyncPlanner.decide(input(baseRevision = null, baseSha = null, localSha = "x", remoteRevision = 7, remoteSha = "x")))
}
