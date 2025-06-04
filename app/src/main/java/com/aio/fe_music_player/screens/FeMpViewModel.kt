package com.aio.fe_music_player.screens

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel

class FeMpViewModel : ViewModel() {


    fun loadMusicData(mContext: Context) {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        if (uri != null) {
            Log.d("TestTestTest", "uri : $uri")
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA // deprecated in API 29+
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        mContext.contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val artist = cursor.getString(artistCol)
                val data = cursor.getString(dataCol)

                Log.d("TestTestTest", "MusicData : $data")
            }
        }
    }


}