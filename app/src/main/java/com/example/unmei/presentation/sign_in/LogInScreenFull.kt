package com.example.unmei.presentation.sign_in

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unmei.presentation.viewmodel.SignInWithGoogleViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginInScreenFull(
    navController: NavController,
   // viewModel: SignInWithGoogleViewModel = hiltViewModel(),
    googleAuthUiClient: GoogleAuthUiClient
){

  //  val state=viewModel.state.collectAsState()

    val passwordTextField = remember {
        mutableStateOf("")
    }
    val emailTextField = remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    //тот же самый лаунчер только для Сompose,также указывается контракт
    val launcher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
                result->
            if(result.resultCode == RESULT_OK){
                scope.launch {
                    val signInResult =  googleAuthUiClient.signWithIntent(
                        intent=result.data ?: return@launch
                    )
                }
            }
        }
    )
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

        },
        createAccountOnClick = {

        },
    )
}