package com.example.unmei.presentation.profile_user_feature.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.user.GetUserWithStatusUseCase
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.profile_user_feature.model.BlockInfo
import com.example.unmei.presentation.profile_user_feature.model.ProfileDetailScreenContent
import com.example.unmei.presentation.profile_user_feature.model.ProfileVMState
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.getAdvancedStatusUser
import com.example.unmei.util.timestampToLocalDateTime
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileUserViewModel @Inject constructor(
    val getUserByIdUseCase: GetUserByIdUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val getUserWithStatusUseCase: GetUserWithStatusUseCase,
):ViewModel() {

    private val currentUsrUid = Firebase.auth.currentUser!!.uid
    private val _state = MutableStateFlow<ProfileVMState>(ProfileVMState())
    val state: StateFlow<ProfileVMState> = _state.asStateFlow()

    init {
        getNavData()
    }

    private fun getNavData(){
        val dataJson = savedStateHandle.get<String>(ConstansApp.PROFILE_ARGUMENT_JSON)
        if (dataJson==null){
            _state.update {
                it.copy(content = ProfileDetailScreenContent.ERROR)
            }
            return
        }
        _state.update { it.copy(userId = Screens.Profile.fromJsonData(dataJson))}
        getUserDetail()
    }
    private fun getUserDetail(){
        viewModelScope.launch {

            _state.update { it.copy(content = ProfileDetailScreenContent.LOADING) }

            val userExtend=getUserWithStatusUseCase.invoke(userId = state.value.userId)
            userExtend?.let {
                currenUserExt->
                val user =currenUserExt.user
                val status = currenUserExt.statusUser
                when(status.status){
                    Status.OFFLINE ->_state.update {
                        it.copy(statusUser = getAdvancedStatusUser(currenUserExt.statusUser.lastSeen))
                    }
                    Status.ONLINE -> _state.update { it.copy(statusUser = "Online") }
                    Status.RECENTLY -> _state.update { it.copy(statusUser = "был(а) недавно") }

                }
                _state.update {
                    it.copy(
                        fullName =user.fullName,
                        iconUrl = user.photoUrl,
                        userName = user.userName,
                        isMine = user.uid == currentUsrUid,
                        age = user.age,
                        timeStamp = user.timestamp,
                        content = ProfileDetailScreenContent.CONTENT
                    )
                }
                genListInfoUser()
                return@launch
            }

            _state.update {
                it.copy(content = ProfileDetailScreenContent.ERROR)
            }
        }
    }
    private  fun genListInfoUser(){
        val listInfoUser = mutableListOf<BlockInfo>()
        listInfoUser.add(BlockInfo("Полное имя",state.value.fullName))
        listInfoUser.add(BlockInfo("Имя пользователя (username)", state.value.userName))
         if(state.value.timeStamp!=0L){
             val localDateTimeRegister=timestampToLocalDateTime(state.value.timeStamp)
             listInfoUser.add(BlockInfo("Дата регистрации",formatDate(state.value.timeStamp)))
         }
        _state.update {
            it.copy(listInfo = listInfoUser)
        }
    }
    private fun formatDate(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
        val date = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        return date.format(formatter)
    }

}