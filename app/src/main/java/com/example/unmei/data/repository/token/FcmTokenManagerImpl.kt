package com.example.unmei.data.repository.token

class FcmTokenManagerImpl (
    private val firebaseTokenProvider: FirebaseTokenProvider
):FcmTokenManager {
    override suspend fun getToken(): String? {
        //добавить datastore или sharedPref
        return firebaseTokenProvider.getAccessToken()
    }

    override suspend fun refreshToken(refreshToken:String) {
        //добавить datastore или sharedPref

    }

    override fun deleteToken() {
        //добавить datastore или sharedPref
        TODO("Not yet implemented")
    }
}