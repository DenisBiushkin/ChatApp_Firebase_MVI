package com.example.unmei.data.network.retrofit

import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("/send")
    suspend fun sendMessage(
        @Body body: String
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: String
    )
}