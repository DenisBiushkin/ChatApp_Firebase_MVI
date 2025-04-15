package com.example.unmei.data.network.retrofit

import com.example.unmei.data.repository.token.FcmTokenManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class NotificationTokenHeaderAuthenticator(
    private val tokenManager: FcmTokenManager
):Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        TODO("Not yet implemented")
    }
}