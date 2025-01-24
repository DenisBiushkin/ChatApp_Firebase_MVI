package com.example.unmei.presentation.sign_in_feature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unmei.data.repository.MainRepositoryImpl
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.SaveUserOnceUseCase
import com.example.unmei.presentation.sign_in_feature.model.SignInState
import com.example.unmei.presentation.sign_in_feature.model.SignInResult
import com.example.unmei.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInWithGoogleViewModel @Inject constructor(
   private val saveUserOnceUseCase: SaveUserOnceUseCase
): ViewModel() {
    private val TAG="MyTag"


    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state

    fun SignInWithGoogle(){

    }

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
                saveUserOnceUseCase.execute(currentUser).collect{
                    it->
                    when(it){
                        is Resource.Error ->{

                        }
                        is Resource.Loading -> {

                        }
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

    }


    fun resetState(){
        _state.update {
            SignInState()
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