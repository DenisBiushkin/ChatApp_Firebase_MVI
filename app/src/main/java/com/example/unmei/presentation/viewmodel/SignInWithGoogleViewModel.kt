package com.example.unmei.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.unmei.presentation.model.SignInState
import com.example.unmei.presentation.sign_in.GoogleAuthUiClient
import com.example.unmei.presentation.sign_in.SignInResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SignInWithGoogleViewModel (
    private val auth: FirebaseAuth,
    private val googleAuthUiClient: GoogleAuthUiClient
): ViewModel() {
    private val TAG="MyTag"


    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state

    fun SignInWithGoogle(){

    }

    fun inSignResult(result: SignInResult){
        _state.update {
            it.copy(
                isSignInSuccess = result.data!=null,
                signInErrorMessage = result.errorMessage
            )
        }
    }
    fun resetState(){
        _state.update {
            SignInState()
        }
    }









   fun SignUp(email:String, password:String){
       auth.createUserWithEmailAndPassword(email, password)
           .addOnCompleteListener() { task ->
               if (task.isSuccessful) {
                   // Sign in success, update UI with the signed-in user's information
                   Log.d(TAG, "createUserWithEmail:success")
                   val user = auth.currentUser
               } else {
                   // If sign in fails, display a message to the user.
                   Log.w(TAG, "createUserWithEmail:failure", task.exception)
               }
           }
   }
    fun SignIn(email:String, password:String){

//        _state.value=LoginScreenState(
//            message ="UserState: "+ auth?.currentUser?.toString()
//        )
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "Success SignIn")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "Failure SignIn", task.exception)
                }
        }
    }
    fun SignOut(){
        Log.d(TAG,"Вы вышли из аккаунта")
        auth.signOut()
    }

    fun DeleteAccount(email:String,password: String){
        val credential = EmailAuthProvider
            .getCredential(email,password)
        Log.d(TAG,"Аккаунт в процессе удаления")
        auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {
            if(it.isSuccessful){
                auth.currentUser?.delete()?.addOnCompleteListener {

                }
                Log.d(TAG,"Аккаунт удален")
            }else{
                Log.d(TAG,it.exception.toString())

            }
        }

    }
}