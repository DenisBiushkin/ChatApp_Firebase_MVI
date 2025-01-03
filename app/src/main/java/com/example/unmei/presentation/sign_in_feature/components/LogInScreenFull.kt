package com.example.unmei.presentation.sign_in_feature.components

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unmei.presentation.sign_in_feature.viewmodel.SignInWithGoogleViewModel
import com.example.unmei.util.ConstansDev.TAG
import kotlinx.coroutines.launch
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansApp.MAIN_NAVIGATE_ROUTE

@Composable
fun LoginInScreenFull(
    navController: NavController,
    viewModel: SignInWithGoogleViewModel = hiltViewModel(),
    googleAuthUiClient: GoogleAuthUiClient
){

    val state=viewModel.state.collectAsState()

    val passwordTextField = remember {
        mutableStateOf("")
    }
    val emailTextField = remember {
        mutableStateOf("")
    }
    val scopeSignInFirebase = rememberCoroutineScope()
    val scopeGoogleOneTap = rememberCoroutineScope()
    //тот же самый лаунчер только для Сompose,также указывается контракт
    //здесь непосрдественно вход, когда запущенный launcher придет
    val launcher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
                result->
            if(result.resultCode == RESULT_OK){
                scopeSignInFirebase.launch {
                    val signInResult =  googleAuthUiClient.signInFirebaseWithIntent(
                        intent=result.data ?: return@launch//выход из корутины если null
                    )
                    viewModel.inSignResult(signInResult)
                }
            }
        }
    )
    if (state.value.isSignInSuccess){
        viewModel.resetState()

//        navController.navigate(MAIN_NAVIGATE_ROUTE){
//            popUpTo(MAIN_NAVIGATE_ROUTE) { inclusive = false }
//        }


    }
    SignInScreen(
        emailField = emailTextField.value,
        emailFieldOnChange = {
            emailTextField.value=it
        },
        passwordField = passwordTextField.value,
        passwordFieldOnChange = {
            passwordTextField.value=it
        },
        forgotOnClick = {

        },
        signInOnClick = {

        },
        signInWithGoogleOnClick = {

            navController.backQueue.forEachIndexed { index, navBackStackEntry ->
                Log.d(TAG, "Route $index: ${navBackStackEntry.destination.route}")
            }

            navController.navigate(MAIN_NAVIGATE_ROUTE)

//            scopeGoogleOneTap.launch {
//                val signInIntenSender: IntentSender? = googleAuthUiClient.signInGoogle()
//                Log.d(TAG,"signInIntenSender "+signInIntenSender.toString())
//                //отправляем  IntentSender, результат получим в launcher
//                launcher.launch(
//                    input= IntentSenderRequest.Builder(
//                        //если акканут не был выбран Null выходим из курутины
//                        signInIntenSender ?: return@launch
//                    ).build()
//                )
//            }
        },
        createAccountOnClick = {

        },
    )
}