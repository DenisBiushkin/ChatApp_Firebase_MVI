package com.example.unmei.presentation.create_group_feature.viemodel

import androidx.lifecycle.ViewModel
import com.example.unmei.presentation.create_group_feature.model.CreateGroupVMState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CreateGroupViewModel @Inject constructor(

):ViewModel() {

    private val _state:MutableStateFlow<CreateGroupVMState> = MutableStateFlow(CreateGroupVMState())
    val state = _state.asStateFlow()

    fun addUserInChat(userId:String){
        if (state.value.selectedUsersIds.contains(userId)){
            _state.update {
                it.copy(
                    selectedUsersIds = state.value.selectedUsersIds.minus(userId)
                )
            }
            return
        }
        _state.update {
            it.copy(
                selectedUsersIds = state.value.selectedUsersIds.plus(userId)
            )
        }
    }

    fun textChatNameChanged(text:String){
        _state.update { it.copy(chatName =text ) }
    }

}
