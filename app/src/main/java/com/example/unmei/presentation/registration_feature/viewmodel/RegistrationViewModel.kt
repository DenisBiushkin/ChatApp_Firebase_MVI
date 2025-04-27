package com.example.unmei.presentation.registration_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.domain.repository.AuthRepository
import com.example.unmei.presentation.registration_feature.model.RegistrationVMEvent
import com.example.unmei.presentation.registration_feature.model.RegistrationVMState
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.example.unmei.util.ValidationSignInOrRegister
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
):ViewModel() {
    private  var auth: FirebaseAuth= FirebaseAuth.getInstance()


    private val _state:MutableStateFlow<RegistrationVMState> = MutableStateFlow<RegistrationVMState>(RegistrationVMState())

    val state: StateFlow<RegistrationVMState> = _state.asStateFlow()


    fun  onEvent(event: RegistrationVMEvent){
        when(event){
            is RegistrationVMEvent.FullNameChange -> {_state.value=state.value.copy(fullName = event.value)}
            is RegistrationVMEvent.EmailOrPhoneChange -> {_state.value=state.value.copy(emailOrPhone = event.value)}
            is RegistrationVMEvent.FirstPasswordChange -> {_state.value=state.value.copy(firstPassword = event.value)}
            is RegistrationVMEvent.SecondPasswordChange -> {_state.value=state.value.copy(secondPassword = event.value)}
            is RegistrationVMEvent.Registration -> {
                startRegistrationWithEmail()
            }
        }
    }
    fun startRegistrationWithEmail() {
        val res=ValidationSignInOrRegister.validateRegister(
            state.value.fullName,
            state.value.emailOrPhone,
            state.value.firstPassword,
            state.value.secondPassword
        )
        if(res is Resource.Error){
            _state.value=state.value.copy(
                textAlert =res.message ?: "Непредвиденная ошибка",
                showAlert = true
            )
            return
        }
       viewModelScope.launch {
           Log.d(TAG,"Выполнение регистрации")
           _state.value = state.value.copy(isLoading = true)

           val result=authRepository.registerWithEmail(
              email = state.value.emailOrPhone,
               password = state.value.firstPassword,
               fullName = state.value.fullName
           )
           when(result){
               is Resource.Error -> {
                   _state.value = state.value.copy(
                       isLoading = false,
                       showAlert = true,
                       textAlert = result.message ?: "Неизвестная ошибка регистрации"
                   )
               }
               is Resource.Loading -> {
                   //nothing
               }
               is Resource.Success -> {
                   _state.value = state.value.copy(
                       isLoading = false
                   )
               }
           }
       }
    }
    fun resetState(){
        _state.value=state.value.copy(
            showAlert = false,
            textAlert = "",
        )
    }





    private fun RegisterUser(){

    }
}