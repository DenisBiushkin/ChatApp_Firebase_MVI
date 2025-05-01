package com.example.unmei.presentation.create_group_feature.viemodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.AttachmentDraft
import com.example.unmei.domain.model.UploadProgress
import com.example.unmei.domain.usecase.messages.CreatePublicChatUseCase
import com.example.unmei.domain.usecase.user.GetFriendsByUserIdUseCase
import com.example.unmei.presentation.create_group_feature.model.CreateGroupContentState
import com.example.unmei.presentation.create_group_feature.model.CreateGroupItemUi
import com.example.unmei.presentation.create_group_feature.model.CreateGroupVMState
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansApp.STORAGE_ROOM_PHOTO_FOLDER
import com.example.unmei.util.ConstansApp.STORAGE_ROOM_REFERENCE
import com.example.unmei.util.ConstansDev.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val getFriendsByUserIdUseCase: GetFriendsByUserIdUseCase,
    private val createPublicChatUseCase: CreatePublicChatUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val remoteDataSource: RemoteDataSource
):ViewModel() {

    private val _state:MutableStateFlow<CreateGroupVMState> = MutableStateFlow(CreateGroupVMState())
    val state = _state.asStateFlow()

    init{
        getNavAguments()
    }
    private fun getNavAguments(){
        val userId = savedStateHandle.get<String>(ConstansApp.CREATEGROUP_ARGUMENT_USERID)
        if (userId==null){
            return
        }
        _state.update { it.copy(currentUserId = userId) }
        Log.d(TAG,"Create Group, currentUserId:$userId")
        getUserContacts()
    }
    fun addUserInChat(user: CreateGroupItemUi){
        if (state.value.selectedContacts.keys.contains(user.id)){
            _state.update {
                it.copy(
                    selectedContacts = state.value.selectedContacts.minus(user.id)
                )
            }
            return
        }
        _state.update {

            it.copy(
                selectedContacts = state.value.selectedContacts.plus(user.id to user )
            )
        }
    }
    fun onSelectIconUri(uri: Uri){
        _state.update {
            it.copy(
                chatIconUri = uri
            )
        }
    }
    fun createNewChat(){
        if (state.value.selectedContacts.isEmpty()){
            return
        }
        if (state.value.chatName.isBlank()){
            return
        }
        if (state.value.chatIconUri==null){
            return
        }
        viewModelScope.launch {
            val pathString = STORAGE_ROOM_REFERENCE+"/-OP1eRdshClb_BOlU3rl/"+ STORAGE_ROOM_PHOTO_FOLDER
            remoteDataSource.uploadAttachmentWithProgressRemote(
                 pathString=pathString,
                draft = AttachmentDraft(
                    uri=state.value.chatIconUri!!,
                    mimeType = ""
                )
            ).collect{
                progress->
                when(progress){
                    is UploadProgress.Failed ->  Log.d(TAG,"Ошибка загрузки")
                    is UploadProgress.Success ->  Log.d(TAG,"Успешно загружено")
                    is UploadProgress.Uploading ->   Log.d(TAG,"процесс загрузкт ${progress.progress * 100}%")
                }


        }
//        viewModelScope.launch {
//            //на всякий случай
//            val membersIds =(state.value.selectedContacts.keys.toSet()+ setOf(state.value.currentUserId)).toList()
//            val resultCreateGroup=createPublicChatUseCase.execute(
//                chatName = state.value.chatName,
//                moderatorsIds = listOf(state.value.currentUserId),
//                membersIds = membersIds,
//                iconUri = state.value.chatIconUri!!
//            )
//            when(resultCreateGroup){
//                is Resource.Error -> Log.d(TAG,"Ошибка создания группы")
//                is Resource.Loading -> Log.d(TAG,"Загрузка ")
//                is Resource.Success -> Log.d(TAG,"Успешное создание группы ${resultCreateGroup.data}")
//            }
        }
    }
    fun textChatNameChanged(text:String){
        _state.update { it.copy(chatName =text ) }
    }

    private fun getUserContacts(){
        viewModelScope.launch {
            val contactList=getFriendsByUserIdUseCase.execute(state.value.currentUserId)
            if (contactList!=null){
                val listContacts=contactList.map {
                    it.toGroupContacts()
                }
                val groupedContacts=listContacts.groupBy { it.fullName.uppercase()[0].toString() }.toSortedMap()
                _state.update {
                   it.copy(
                       groupedContacts = groupedContacts,
                       contentState = CreateGroupContentState.CONTENT
                   )
                }
                return@launch
            }
            _state.update { it.copy(contentState = CreateGroupContentState.EMPTY) }
        }
    }

}
