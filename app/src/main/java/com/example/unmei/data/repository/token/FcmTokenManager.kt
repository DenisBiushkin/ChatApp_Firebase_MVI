package com.example.unmei.data.repository.token

interface FcmTokenManager {

    suspend fun getToken():String?

    suspend fun refreshToken(refreshToken:String)

    fun deleteToken()
}