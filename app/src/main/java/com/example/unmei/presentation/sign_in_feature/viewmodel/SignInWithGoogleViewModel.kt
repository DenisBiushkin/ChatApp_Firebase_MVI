package com.example.unmei.presentation.sign_in_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.repository.AuthRepositoryImpl
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.AuthRepository
import com.example.unmei.domain.usecase.user.SaveUserOnceUseCase
import com.example.unmei.presentation.sign_in_feature.model.SignInVMState
import com.example.unmei.presentation.sign_in_feature.model.SignInResult
import com.example.unmei.presentation.sign_in_feature.model.SignInVMEvent
import com.example.unmei.util.Resource
import com.example.unmei.util.ValidationSignInOrRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInWithGoogleViewModel @Inject constructor(
   private val saveUserOnceUseCase: SaveUserOnceUseCase,
    private val authRepository: AuthRepository
): ViewModel() {
    private val TAG="MyTag"


    private val _state = MutableStateFlow(SignInVMState())
    val state: StateFlow<SignInVMState> = _state

    fun inSignResult(result: SignInResult){
        if (result.data!=null){
            val data= result.data
            val currentUser = User(
                uid = data.userId,
                fullName = data.userName ?: "Unknown",
                photo = data.ProfilePictureUrl ?: "Unknown",
                age = "20"
            )
            viewModelScope.launch {
               val res= saveUserOnceUseCase.execute(currentUser)
                    when(res){
                        is Resource.Error ->{}
                        is Resource.Loading -> {}
                        is Resource.Success ->{
                            _state.update {
                                it.copy(
                                    isSignInSuccess = true,
                                    signInErrorMessage = result.errorMessage
                                )
                            }
                        }
                    }

            }

        }

    }
    fun onEvent(event:SignInVMEvent){
        when(event){
            is SignInVMEvent.EmailValueChange -> _state.value=state.value.copy(emailField = event.value)
            is SignInVMEvent.PasswordValueChange -> _state.value=state.value.copy(passwordField= event.value)
            is SignInVMEvent.SignInWithEmail -> signInWithEmail()
        }
    }
   private fun signInWithEmail(){
       Log.d(TAG,"Осуществляется вход с помощью Email")
       val res=ValidationSignInOrRegister.validateSignIn(
           state.value.emailField,
           state.value.passwordField,
       )
       if(res is Resource.Error){
           _state.value=state.value.copy(
               textAlert =res.message ?: "Непредвиденная ошибка",
               showAlert = true
           )
           return
       }
        viewModelScope.launch {
            _state.value=state.value.copy(isLoading = true)
            val result=authRepository.signInWithEmailAndPassword(state.value.emailField, state.value.passwordField,)
            when(result){
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        showAlert = true,
                        textAlert = result.message ?: "Неизвестная ошибка регистрации"
                    )
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        isSignInSuccess = true
                    )
                }
            }
        }
    }
    fun resetState(){
        _state.update {
            SignInVMState()
        }
    }









//   fun SignUp(email:String, password:String){
//       auth.createUserWithEmailAndPassword(email, password)
//           .addOnCompleteListener() { task ->
//               if (task.isSuccessful) {
//                   // Sign in success, update UI with the signed-in user's information
//                   Log.d(TAG, "createUserWithEmail:success")
//                   val user = auth.currentUser
//               } else {
//                   // If sign in fails, display a message to the user.
//                   Log.w(TAG, "createUserWithEmail:failure", task.exception)
//               }
//           }
//   }
//    fun SignIn(email:String, password:String){
//
////        _state.value=LoginScreenState(
////            message ="UserState: "+ auth?.currentUser?.toString()
////        )
//        auth.signInWithEmailAndPassword(email,password)
//            .addOnCompleteListener {
//                task->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "Success SignIn")
//                    val user = auth.currentUser
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.d(TAG, "Failure SignIn", task.exception)
//                }
//        }
//    }
//    fun SignOut(){
//        Log.d(TAG,"Вы вышли из аккаунта")
//        auth.signOut()
//    }
//
//    fun DeleteAccount(email:String,password: String){
//        val credential = EmailAuthProvider
//            .getCredential(email,password)
//        Log.d(TAG,"Аккаунт в процессе удаления")
//        auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {
//            if(it.isSuccessful){
//                auth.currentUser?.delete()?.addOnCompleteListener {
//
//                }
//                Log.d(TAG,"Аккаунт удален")
//            }else{
//                Log.d(TAG,it.exception.toString())
//
//            }
//        }
//
//    }
}