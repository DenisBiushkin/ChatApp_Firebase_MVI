package com.example.unmei.presentation.editProfile_feature.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.user.UpdateProfileFullNameById
import com.example.unmei.domain.usecase.user.UpdateProfileUserNameById
import com.example.unmei.presentation.editProfile_feature.model.EditProfileScreenContent
import com.example.unmei.presentation.editProfile_feature.model.EditProfileVMState
import com.example.unmei.util.ConstansApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateProfileFullNameById: UpdateProfileFullNameById,
    private val updateProfileUserNameById: UpdateProfileUserNameById
) :ViewModel(){

    private lateinit var oldDataUser: User
    private val _state = MutableStateFlow<EditProfileVMState>(EditProfileVMState())
    val state = _state.asStateFlow()
    init{
        getArguments()
    }
    private fun getArguments(){
        val userId = savedStateHandle.get<String>(ConstansApp.EDITPROFILE_ARGUMENT_USERID)
        if (userId==null){
            _state.update {
                it.copy(contentState = EditProfileScreenContent.ERROR)
            }
            return
        }
        _state.update {
            it.copy(
                userId = userId
            )
        }
        loadUser()
    }
    private fun loadUser(){
        viewModelScope.launch {
            val user=getUserByIdUseCase.execute(state.value.userId)
            if (user!=null){
                oldDataUser = user
                val splitedFullName = user.fullName.split(" ")
                _state.update {
                    it.copy(
                        firstName = splitedFullName.first(),
                        secondName = splitedFullName.filterIndexed { index, s -> index >0   }.joinToString(" "),
                        userName = user.userName,
                        iconUrl = user.photoUrl,
                        contentState = EditProfileScreenContent.CONTENT
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    contentState = EditProfileScreenContent.ERROR
                )
            }
        }
    }
}