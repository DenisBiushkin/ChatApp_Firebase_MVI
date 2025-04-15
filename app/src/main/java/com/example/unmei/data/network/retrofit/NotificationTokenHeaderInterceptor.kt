package com.example.unmei.data.network.retrofit

import com.example.unmei.data.repository.token.FcmTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class NotificationTokenHeaderInterceptor(
    private val tokenManager: FcmTokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalReq = chain.request()//оригинальный запрос

        val token= runBlocking {
            tokenManager.getToken() ?: ""
        }
        val request = originalReq.newBuilder()
            .addHeader("Authorization","Bearer "+token)
            .build()
        val finalyResponse = chain.proceed(request)//выполняет исходный запрос к серверу.
        return  finalyResponse
    }
}