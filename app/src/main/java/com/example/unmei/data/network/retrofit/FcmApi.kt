package com.example.unmei.data.network.retrofit

import com.example.unmei.data.model.FcmMessage
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApi {

    @POST("v1/projects/917566080714/messages:send")
    suspend fun sendMessage(
        @Body body: FcmMessage
    )


}