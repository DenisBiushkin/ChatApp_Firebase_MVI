package com.example.unmei

import android.content.Intent
import android.os.Bundle
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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

        val explicitIntent = Intent(this,MainActivity::class.java)
        explicitIntent.putExtra("key1","Destiny")

        launcher = registerForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ){
            activityResult ->

        }
        SignInWithGoogle()

        setContent {
            UnmeiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


    private fun getGoogleClient():GoogleSignInClient{
        val signInRequest = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
                .build()
        return GoogleSignIn.getClient(this,signInRequest)
    }
    private fun SignInWithGoogle(){
        val signInClient = getGoogleClient()
        launcher.launch(signInClient.signInIntent)
    }

}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    UnmeiTheme {
        Greeting2("Android")
    }
}