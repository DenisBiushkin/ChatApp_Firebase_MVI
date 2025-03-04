package com.example.unmei

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.unmei.data.repository.MainRepositoryImpl
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.SetStatusUserUseCase
import com.example.unmei.presentation.Navigation.HostNavGraph
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.presentation.util.ui.theme.UnmeiTheme
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.Resource
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth


    @Inject
    lateinit var setStatusUserUseCase: SetStatusUserUseCase

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val status = StatusUser(
                Status.ONLINE,
                System.currentTimeMillis()
            )
            if(auth.currentUser !=null){
                setStatusUserUseCase.execute(auth.currentUser!!.uid,status)
            }

        }
    }
    override fun onStop() {
        super.onStop()
        CoroutineScope(Dispatchers.IO).launch {
            val status = StatusUser(
                Status.OFFLINE,
                System.currentTimeMillis()
            )
            if(auth.currentUser !=null) {
                setStatusUserUseCase.execute(auth.currentUser!!.uid, status)
            }
        }
    }
    //private val repositorty: MainRepositoryImpl = MainRepositoryImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        var startDestinationRoute = ConstansApp.AUTH_NAVIGATE_ROUTE
        val currentUser = auth.currentUser

        if(currentUser!=null){
            startDestinationRoute=ConstansApp.MAIN_NAVIGATE_ROUTE
        }

        //на время разработок внутрянки
       // startDestinationRoute=ConstansApp.MAIN_NAVIGATE_ROUTE

//        lifecycleScope.launch {
//            repositorty.saveUser(
//                User(
//                    uid=googleAuthUiClient.getSignedUser()!!.userId,
//                    firstName = "Marcile",
//                    secondName = "Donato",
//                    sex = "Female",
//                    age="20"
//            )
//            ).collect{
//                data->
//                when(data){
//                    is Resource.Error -> {
//
//                    }
//                    is Resource.Loading -> {
//
//                    }
//                    is Resource.Success -> {
//
//                    }
//                }
//            }
//        }


//        val fs=Firebase.firestore
//
//        val db=FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
//        val ref = db.getReference("Main_DB")
//        ref.child("Message").setValue("Same Text")
//        fs.collection("TestUser")
//            .document().set(
//                TestUser(
//                    "Anna",
//                    "Schneider",
//                    20
//                )
//            )

        setContent {
            UnmeiTheme {
                HostNavGraph(
                    navHostController = rememberNavController(),
                    startDestinationRoute=startDestinationRoute,
                    googleAuthUiClient=googleAuthUiClient
                )
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