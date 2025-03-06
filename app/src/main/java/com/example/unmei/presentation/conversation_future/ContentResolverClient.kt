package com.example.unmei.presentation.conversation_future

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class ContentResolverClient(
    val context: Context
) {




    suspend fun getAllImagesUri():List<Uri>{

        val imageUris = mutableListOf<Uri>()
        val contentResolver = context.contentResolver
        // URI таблицы изображений
        val imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        // Какие колонки хотим получить
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )
        // Сортируем по дате, чтобы сначала шли самые свежие
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        contentResolver.query(
            imagesUri,
            projection,
            null,  // selection
            null,  // selectionArgs
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {

                val id = cursor.getLong(idColumn)

                val uri = ContentUris.withAppendedId(imagesUri, id)
                imageUris.add(uri)
            }
        }
        return  imageUris
    }
}