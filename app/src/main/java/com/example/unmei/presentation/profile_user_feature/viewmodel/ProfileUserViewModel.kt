package com.example.unmei.presentation.profile_user_feature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.presentation.profile_user_feature.model.ProfileVMState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileUserViewModel @Inject constructor(
    val getUserByIdUseCase: GetUserByIdUseCase,
):ViewModel() {

    private val currentUsrUid = Firebase.auth.currentUser!!.uid
    private val _state = MutableStateFlow<ProfileVMState>(ProfileVMState())
    val state: StateFlow<ProfileVMState> = _state.asStateFlow()


    private fun initData(){
        getUserDetail()
    }
    private fun getUserDetail(){
        viewModelScope.launch {
            if (state.value.userId.isEmpty()){
                //ошибка
                return@launch
            }
            //getSummeries
            getUserByIdUseCase.execute(state.value.userId)?.let {
                _state.value= state.value.copy(
                    fullName = it.fullName,
                    iconUrl = it.photoUrl,
                    isMine = state.value.userId==currentUsrUid
                )
                return@launch
            }

            //ошибка

        }
    }
    fun saveData(navData:String){
        _state.value=state.value.copy(
            userId = navData
        )
        initData()
    }
}