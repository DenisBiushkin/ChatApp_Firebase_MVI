package com.example.unmei.presentation.editProfile_feature.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.user.UpdateProfileFullNameById
import com.example.unmei.domain.usecase.user.UpdateProfilePhotoUseCase
import com.example.unmei.domain.usecase.user.UpdateProfileUserNameById
import com.example.unmei.presentation.editProfile_feature.model.AlertStatus
import com.example.unmei.presentation.editProfile_feature.model.EditProfileScreenContent
import com.example.unmei.presentation.editProfile_feature.model.EditProfileVMState
import com.example.unmei.presentation.editProfile_feature.model.Field
import com.example.unmei.presentation.editProfile_feature.model.SaveResult
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val updateProfileUserNameById: UpdateProfileUserNameById,
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase
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
            _state.update { it.copy(contentState = EditProfileScreenContent.ERROR) }
            return
        }
        _state.update {
            it.copy(
                userId = userId
            )
        }
        loadUser()
    }
    fun changeFields(
        firstName: String, lastName: String, username: String
    ){
       _state.update {
           it.copy(
               secondName = lastName,
               firstName = firstName,
               userName = username
           )
       }
    }

    fun onAcceptData(){
        val fullName = state.value.firstName+" "+state.value.secondName
        val userName=state.value.userName
        val iconUrl = state.value.iconUrl
        Log.d(TAG,"EDITProfile Сохранение новых данных")

        if (fullName!=oldDataUser.fullName){
            Log.d(TAG,"EDITProfile Изменение Fullname")
            updateFullName(fullName)
        }
        if (userName!=oldDataUser.userName){
            Log.d(TAG,"EDITProfile Изменение UserName")
            updateUserName(userName)
        }
        if (iconUrl!=oldDataUser.photoUrl){
            Log.d(TAG,"EDITProfile Изменение Photo")
            updatePhoto()
        }

    }
    fun onSelectIconUri(uri: Uri){
        _state.update {
            it.copy(
                selectedUri = uri,
                iconUrl = uri.toString()
            )
        }
    }
    private fun updatePhoto(){
        viewModelScope.launch {
            val field=Field.Photo
            addNewUpdatesInAlert(SaveResult.Loading( field))
            val updateResult=updateProfilePhotoUseCase.invoke(
                userId = state.value.userId,
                uri = state.value.selectedUri
            )
            when(updateResult){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    updateUpdatesResult(SaveResult.Error(field=field, message = updateResult.message?:"Ошибка сохранения"))
                    checkResults()
                }
                is Resource.Success -> {
                    updateUpdatesResult(SaveResult.Success(field))
                    checkResults()
                }
            }
        }
    }
    private fun checkResults(){
        //все поля не содержат поля в процессе выполнения
        val fieildsUpdated=state.value.alertUpdatesResult.none{
            it is SaveResult.Loading
        }
        _state.update {
            it.copy(
                showAlert = true
            )
        }
    }
    private fun addNewUpdatesInAlert(
        saveResult:SaveResult
    ){
        _state.update {
            it.copy(
                alertUpdatesResult = it.alertUpdatesResult.toMutableList().plus(saveResult),
                showAlert = true
            )
        }
    }
    private fun updateUpdatesResult(saveResult:SaveResult){
        val newListResults=_state.value.alertUpdatesResult.map {
            if (it.field ==saveResult.field){
                saveResult
            }else
                it
        }
        _state.update {
            it.copy(
                alertUpdatesResult = newListResults
            )
        }
    }
    private fun updateFullName(fullName:String){
        viewModelScope.launch {
            val field=Field.Fullname
            addNewUpdatesInAlert(SaveResult.Loading( field))
            val updateResult=updateProfileFullNameById.execute(
                userId = state.value.userId,
                newFullName =fullName
            )
            when(updateResult){
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    updateUpdatesResult(SaveResult.Error(field=field, message = updateResult.message?:"Ошибка сохранения"))
                    checkResults()
                }
                is Resource.Success -> {
                    updateUpdatesResult(SaveResult.Success(field))
                    checkResults()
                }
            }
        }
    }
    private fun updateUserName(userName:String){
        viewModelScope.launch {
            val field=Field.UserName
            addNewUpdatesInAlert(SaveResult.Loading(field))
            val updateResult=updateProfileUserNameById.execute(
                userId = state.value.userId,
                newUserName =userName
            )
            when(updateResult){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    updateUpdatesResult(SaveResult.Error(field=field, message = updateResult.message?:"Ошибка сохранения"))
                    checkResults()
                }
                is Resource.Success -> {
                    updateUpdatesResult(SaveResult.Success(field))
                    checkResults()
                }
            }
        }
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