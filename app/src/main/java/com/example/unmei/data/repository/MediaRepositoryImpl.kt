package com.example.unmei.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.unmei.domain.repository.MediaRepository
import java.io.ByteArrayOutputStream

class MediaRepositoryImpl(
    private val context: Context
): MediaRepository {

    override fun getByteArrayFromUriImage(quality: Int, uri: Uri): ByteArray {

        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory
            .decodeStream(inputStream)


        val byteOutpuStream = ByteArrayOutputStream()


        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,byteOutpuStream)


        return byteOutpuStream.toByteArray()
    }
}