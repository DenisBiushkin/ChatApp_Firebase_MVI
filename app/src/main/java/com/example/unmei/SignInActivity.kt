package com.example.unmei

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.unmei.ui.theme.UnmeiTheme
import com.example.unmei.util.ConstansDev.TAG
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.exp

class SignInActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  launcher :ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инициализация Firebase Auth
        auth = Firebase.auth

        launcher = registerForActivityResult(
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

        setContent {
            UnmeiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                }
            }
        }
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
        launcher.launch(signInClient.signInIntent)
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
