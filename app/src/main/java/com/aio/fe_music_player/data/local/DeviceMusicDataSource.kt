package com.aio.fe_music_player.data.local

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.aio.fe_music_player.data.model.MusicData

class DeviceMusicDataSource(private val context: Context) {

    fun loadMusicData(): List<MusicData> {

        val musicDataList = mutableListOf<MusicData>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Audio.Media.RELATIVE_PATH, // API 29+
            MediaStore.Audio.Media.DATA // deprecated in API 29+
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        context.contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val bucketIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_ID)
            val bucketDisplayNameCol =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val artist = cursor.getString(artistCol)
                val duration = formattingDuration(cursor.getString(durationCol))
                val buckId = cursor.getString(bucketIdCol)
                val bucketDisplay = cursor.getString(bucketDisplayNameCol)
                val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                musicDataList.add(
                    MusicData(
                        contentUri.toString(),
                        name,
                        duration,
                        buckId.toLong(),
                        bucketDisplay
                    )
                )
            }
        }
        return musicDataList
    }

    private fun formattingDuration(duration: String?): String {

        duration?.let {
            val durationMillis = it.toLongOrNull() ?: 0L
            val minutes = durationMillis / 1000 / 60
            val seconds = (durationMillis / 1000) % 60
            val durationFormatted = String.format("%d:%02d", minutes, seconds)
            return durationFormatted
        }
        return ""
    }
}
