package br.com.estudario.data.sync

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import br.com.estudario.EstudarioApplication
import br.com.estudario.domain.ProfileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * A imagem do perfil igual no app e no site (ProfileImage, "avatar" na foto da conta). Antes o app
 * guardava a escolha só no celular (um arquivo ou "avatar:chave") e o site só no navegador, então
 * cada lado mostrava uma imagem diferente.
 *
 * A foto do Google fica em [GOOGLE_FILE] (separada da foto escolhida), para "voltar para a foto da
 * conta" funcionar também quando a escolha vem do site.
 */
object ProfileImageSync {
    const val GOOGLE_FILE = "profile_google.jpg"
    private const val SIDE = 256

    private fun app(context: Context) = context.applicationContext as EstudarioApplication

    /** Grava a escolha feita aqui (vale sobre as mais antigas de outros aparelhos). */
    suspend fun recordChoice(context: Context, value: String) {
        val clean = ProfileImage.normalize(value) ?: return
        app(context).preferences.setSyncedAvatar(clean, System.currentTimeMillis())
    }

    /** A foto escolhida aqui, já no formato da conta ("photo:data:…"), ou null se não couber. */
    suspend fun encodePhoto(path: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            val bitmap = BitmapFactory.decodeFile(path) ?: return@runCatching null
            val side = minOf(bitmap.width, bitmap.height)
            val square = Bitmap.createBitmap(bitmap, (bitmap.width - side) / 2, (bitmap.height - side) / 2, side, side)
            val scaled = Bitmap.createScaledBitmap(square, SIDE, SIDE, true)
            val out = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, 82, out)
            ProfileImage.normalize(ProfileImage.PHOTO_PREFIX + "data:image/jpeg;base64," + Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP))
        }.getOrNull()
    }

    /** O que vai para a conta: a escolha gravada ou, de quem escolheu antes desta versão, a imagem atual (com data 0). */
    suspend fun localValue(context: Context): Pair<String, Long>? {
        val (saved, at) = app(context).preferences.syncedAvatar()
        ProfileImage.normalize(saved)?.let { return it to at }
        val path = app(context).preferences.userPhotoPath.first()
        val derived = when {
            path == null || File(path).name == GOOGLE_FILE -> ProfileImage.GOOGLE
            path.startsWith(ProfileImage.PRESET_PREFIX) -> ProfileImage.normalize(path)
            else -> encodePhoto(path)
        } ?: return null
        app(context).preferences.setSyncedAvatar(derived, 0L)
        return derived to 0L
    }

    /** Adota a imagem de outro aparelho se ela foi escolhida depois da daqui. */
    suspend fun applyRemote(context: Context, value: String?, updatedAt: Long) {
        val remote = ProfileImage.normalize(value) ?: return
        val prefs = app(context).preferences
        val (_, localAt) = prefs.syncedAvatar()
        if (updatedAt <= localAt) return
        val dir = context.applicationContext.filesDir
        val path: String? = when {
            remote.startsWith(ProfileImage.PRESET_PREFIX) -> remote
            remote == ProfileImage.GOOGLE -> File(dir, GOOGLE_FILE).takeIf { it.exists() }?.absolutePath
            else -> withContext(Dispatchers.IO) {
                runCatching {
                    val base64 = ProfileImage.photoDataUrl(remote)!!.substringAfter("base64,", "")
                    val bytes = Base64.decode(base64, Base64.DEFAULT)
                    val target = File(dir, "profile_${System.currentTimeMillis()}.jpg")
                    target.writeBytes(bytes)
                    target.absolutePath
                }.getOrNull()
            } ?: return
        }
        withContext(Dispatchers.IO) { deleteChosenPhotos(context, keep = path?.let { File(it).name }) }
        prefs.setUserPhotoPath(path)
        prefs.setSyncedAvatar(remote, updatedAt)
    }

    /** Apaga as fotos escolhidas antigas (a do Google fica, para poder voltar a ela). */
    fun deleteChosenPhotos(context: Context, keep: String?) {
        context.applicationContext.filesDir.listFiles { f -> f.name.startsWith("profile_") && f.name != keep && f.name != GOOGLE_FILE }?.forEach { it.delete() }
    }
}
