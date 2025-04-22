package com.example.unmei.presentation.registration_feature.viewmodel

import androidx.lifecycle.ViewModel
import com.example.unmei.presentation.registration_feature.model.RegistrationVMEvent
import com.example.unmei.presentation.registration_feature.model.RegistrationVMState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(

):ViewModel() {

    private val _state:MutableStateFlow<RegistrationVMState> = MutableStateFlow<RegistrationVMState>(RegistrationVMState())

    val state: StateFlow<RegistrationVMState> = _state.asStateFlow()


    fun  onEvent(event: RegistrationVMEvent){
        when(event){
            is RegistrationVMEvent.FullNameChange -> {_state.value=state.value.copy(fullName = event.value)}
            is RegistrationVMEvent.EmailOrPhoneChange -> {_state.value=state.value.copy(emailOrPhone = event.value)}
            is RegistrationVMEvent.FirstPasswordChange -> {_state.value=state.value.copy(firstPassword = event.value)}
            is RegistrationVMEvent.SecondPasswordChange -> {_state.value=state.value.copy(secondPassword = event.value)}
            is RegistrationVMEvent.Registration -> {

            }
        }
    }
    private fun RegisterUser(){

    }
}