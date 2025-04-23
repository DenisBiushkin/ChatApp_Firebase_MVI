package com.example.unmei.presentation.sign_in_feature.components

import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.sign_in_feature.model.SignInVMEvent
import com.example.unmei.presentation.sign_in_feature.viewmodel.SignInWithGoogleViewModel
import com.example.unmei.util.ConstansDev.TAG
import kotlinx.coroutines.launch
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.presentation.util.LoadingScreen
import com.example.unmei.util.ConstansApp.MAIN_NAVIGATE_ROUTE

@Composable
fun LoginInScreenFull(
    navController: NavController,
    viewModel: SignInWithGoogleViewModel = hiltViewModel(),
    googleAuthUiClient: GoogleAuthUiClient
){

    val state=viewModel.state.collectAsState()
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
        navController.navigate(MAIN_NAVIGATE_ROUTE){
            popUpTo(MAIN_NAVIGATE_ROUTE) { inclusive = false }
        }
    }
    val loaclContext=LocalContext.current
    LaunchedEffect(state.value.showAlert) {
        if (state.value.showAlert) {
            Toast.makeText(loaclContext, state.value.textAlert, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }
//не робит
        if (state.value.isLoading) {
            LoadingScreen ()
        }



    SignInScreen(
        emailField = state.value.emailField,
        emailFieldOnChange = {
           viewModel.onEvent(SignInVMEvent.EmailValueChange(it))
        },
        passwordField = state.value.passwordField,
        passwordFieldOnChange = {
            viewModel.onEvent(SignInVMEvent.PasswordValueChange(it))
        },
        forgotOnClick = {

        },
        signInOnClick = {
            viewModel.onEvent(SignInVMEvent.SignInWithEmail)
        },
        signInWithGoogleOnClick = {
                //navController.navigate(MAIN_NAVIGATE_ROUTE)
            scopeGoogleOneTap.launch {
                val signInIntenSender: IntentSender? = googleAuthUiClient.signInGoogle()
                Log.d(TAG,"signInIntenSender "+signInIntenSender.toString())
                //отправляем  IntentSender, результат получим в launcher
                launcher.launch(
                    input= IntentSenderRequest.Builder(
                        //если акканут не был выбран Null выходим из курутины
                        signInIntenSender ?: return@launch
                    ).build()
                )
            }
        },
        createAccountOnClick = {
            navController.navigate(Screens.Registration.route)
        },
    )
}