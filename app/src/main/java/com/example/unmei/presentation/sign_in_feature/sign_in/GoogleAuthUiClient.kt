package com.example.unmei.presentation.sign_in_feature.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.unmei.R
import com.example.unmei.presentation.sign_in_feature.model.SignInResult
import com.example.unmei.presentation.sign_in_feature.model.UserData
import com.example.unmei.util.ConstansDev.TAG
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient (
    val context: Context,
    val oneTapClient: SignInClient
){
    private val auth= Firebase.auth


    //начинаем вход через Intent
    //отправив IntentSender откроется окно выбора аккаунтов Google
    //Результат возврата будет Intent
    suspend fun signInGoogle(): IntentSender?{//

        val result= try {
            //процесс входа в систему через Google Identity Services
            oneTapClient.beginSignIn(
                buildSignInRequest()//запрос
            ).await()
            //синхронно ждем ответ
            //т.е пока не получим ответ не продолжим
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            Log.d(TAG,"SignInGoogle"+e.message.toString())
            null
        }
        return result?.pendingIntent?.intentSender
    }
    //Google ID Token — это токен, который подтверждает аутентификацию пользователя
    // и может использоваться сервером для подтверждения личности пользователя.
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()//настройка параметров запроса
            .setGoogleIdTokenRequestOptions(//параметры для получения Google ID Token
                GoogleIdTokenRequestOptions.builder()//добавление опций запроса
                    .setSupported(true)//включена поддержка получения Google ID Token
                    .setFilterByAuthorizedAccounts(false)//выпадут все аккаунты гугл на устройстве
                    .setServerClientId(context.getString(R.string.default_web_client_id))// идентификатор клиента сервера (Server Client ID)
                    .build()
            ).setAutoSelectEnabled(true)
            //если есть только один аккаунт гугл то он будет использоваться автоматически
            .build()
    }


    suspend fun signInFirebaseWithIntent(intent: Intent): SignInResult {//ели нашел Intent....
        //учетные данные из Intent когда выбрали аккаунт
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
//        Log.d(TAG,"signWithIntent ______")
        val googleIdToken=credential.googleIdToken//токен доступа по выбранному аккаунту
//        Log.d(TAG,googleIdToken.toString())
//        Log.d(TAG,credential.displayName.toString())
//        Log.d(TAG,"signWithIntent ______")
        //учетные данные по токену
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try{
            //регистрируемся на Firebase с помощью этих данных
            //и сразу получаем информацию о пользователе
            //ждем ответа синхронно т.к вызываем в сопрограмме
            val user=auth.signInWithCredential(googleCredentials).await().user

            SignInResult(
                data =user?.run{
                    UserData(
                        userId = uid,
                        userName = displayName,
                        ProfilePictureUrl=photoUrl?.toString()
                    )
                },
                errorMessage = null
            )

        }catch (e:Exception){
//            e.printStackTrace()
//            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }
    suspend fun signOut(){
        try{
            //отключу пока выход из акканута гугл
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e

        }
    }
    fun getSignedUser(): UserData?{
        return auth.currentUser?.run {
            UserData(
                userId = uid,
                userName = displayName,
                ProfilePictureUrl=photoUrl?.toString()
            )
        }
    }


}