package com.example.unmei

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unmei.domain.model.TestUser
import com.example.unmei.ui.theme.UnmeiTheme
import com.example.unmei.util.ConstansDev
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val fs=Firebase.firestore

        val db=FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
        val ref = db.getReference("Main_DB")
        ref.child("Message").setValue("Same Text")


//        fs.collection("TestUser")
//            .document().set(
//                TestUser(
//                    "Denis",
//                    "Biushkin",
//                    20
//                )
//            )

        setContent {
            UnmeiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnmeiTheme {
        Greeting("Android")
    }
}