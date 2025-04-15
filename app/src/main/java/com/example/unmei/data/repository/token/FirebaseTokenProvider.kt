package com.example.unmei.data.repository.token

import android.content.Context
import com.example.unmei.util.ConstansApp.FCM_SERVICE_ACCOUNT_FILENAME
import com.example.unmei.util.ConstansApp.FCM_TOKEN_GET_URL
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseTokenProvider(
   private val  context: Context
) {
    private var credentials: GoogleCredentials? = null

    init  {
        if (credentials == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val stream = context.assets.open(FCM_SERVICE_ACCOUNT_FILENAME)
                credentials = GoogleCredentials
                    .fromStream(stream)
                    .createScoped(listOf(FCM_TOKEN_GET_URL))
            }
        }
    }

    suspend fun getAccessToken(): String? {
        //переключаем на поток ввода ввывода
       return withContext(Dispatchers.IO) {
            try {
                // Обновление токена, если он истек
                credentials?.refreshIfExpired()
                credentials?.accessToken?.tokenValue
            } catch (e: Exception) {
                null
            }
       }
    }
}