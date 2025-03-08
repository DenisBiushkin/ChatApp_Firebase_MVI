package com.example.unmei.domain.repository

import android.net.Uri
import androidx.core.location.LocationRequestCompat.Quality

interface MediaRepository {

    fun getByteArrayFromUriImage(quality: Int = 100, uri: Uri):ByteArray
}