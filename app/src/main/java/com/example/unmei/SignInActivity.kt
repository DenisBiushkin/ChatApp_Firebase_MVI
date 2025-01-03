package com.example.unmei

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult.Companion.resultCodeToString
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.presentation.util.ui.theme.UnmeiTheme


import com.example.unmei.util.ConstansDev.TAG
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignInActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  launcher1 :ActivityResultLauncher<Intent>

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инициализация Firebase Auth
        auth = Firebase.auth




        val launcher= registerForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),//какой контракт, с какими активити
        ){
                result ->//тот же ActivityResult
            Log.d(TAG, resultCodeToString(result.resultCode))
            if(result.resultCode== RESULT_OK){
                lifecycleScope.launch {
                    googleAuthUiClient.signInFirebaseWithIntent(
                        intent=result.data ?: return@launch
                    )
                }
            }


        }

        lifecycleScope.launch {
            val signInIntenSender: IntentSender? = googleAuthUiClient.signInGoogle()
            Log.d(TAG,"signInIntenSender "+signInIntenSender.toString())
            launcher.launch(
                input= IntentSenderRequest.Builder(
                    signInIntenSender ?: return@launch
                ).build()
            )
        }

        setContent {
            UnmeiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                }
            }
        }
    }
   private fun oldGoogleSignIn(){
       launcher1 = registerForActivityResult(
           contract = ActivityResultContracts.StartActivityForResult()
       ){
               activityResult ->
           Log.d(TAG,activityResult.resultCode.toString())
           //достаем аккаунт
           val task=GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

           try {
               //выбранный аккаунт
               val account= task.getResult(ApiException::class.java)
               if(account!=null){
                   account.idToken?.let {
                       firebaseAuthWithGoogle(it)
                   }
               }

           }catch (e:ApiException){

           }
       }

       signInWithGoogle()

   }
    //Google ID Token — это токен, который подтверждает аутентификацию пользователя
    // и может использоваться сервером для подтверждения личности пользователя.
    private fun getGoogleClient():GoogleSignInClient{
        //открывает google окно для выбора аккаунта
        val signInRequest = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))

            .requestEmail()
                .build()
        //готовый клиент для отправки
        return GoogleSignIn.getClient(this,signInRequest)
    }


    private fun signInWithGoogle(){
        //запускаем с помощью intent()
        val signInClient = getGoogleClient()
        launcher1.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(googleIdToken:String){
        val googleCredentials = GoogleAuthProvider
            .getCredential(googleIdToken,null)
        auth.signInWithCredential(googleCredentials).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d(TAG,"Success SignIN")
            }else{
                Log.d(TAG,"Faild SignIN")
            }
        }
    }



}
