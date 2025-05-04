package com.example.unmei.presentation.chat_list_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.usecase.messages.ObserveChatsByUserIdUseCase
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.user.ObserveUserStatusByIdUseCase
import com.example.unmei.presentation.chat_list_feature.model.ChatVMState
import com.example.unmei.presentation.singleChat_feature.model.ContentStateScreen
import com.example.unmei.util.ChatSessionManager
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatListViewModel @Inject constructor(
    val observeRoomsUserUseCase: ObserveRoomsUserUseCase,
    val observeUserStatusByIdUseCase: ObserveUserStatusByIdUseCase,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val chatSessionManager: ChatSessionManager,
    val observeChatsByUserIdUseCase: ObserveChatsByUserIdUseCase,
    val repository: MainRepository,
    val remote:RemoteDataSource

):ViewModel() {
    private  val _state = MutableStateFlow<ChatVMState>(ChatVMState())
    val state: StateFlow<ChatVMState> = _state.asStateFlow()
    private lateinit var currentUser:User
    private val currentUsrUid = Firebase.auth.currentUser!!.uid

     init {
         viewModelScope.launch {

             currentUser = getUserByIdUseCase.execute(currentUsrUid)?:User(fullName = "unknown", userName = "unknown", photoUrl = "")
             _state.value = state.value.copy(
                 fullName = currentUser.fullName,
                 iconUrl = currentUser.photoUrl,
                 signInData = "SignIn With Google",
                 userId = currentUsrUid
             )
             chatSessionManager.setCurrentUser(currentUser)

             observeChatRoomsAdvanced()
         }
     }

    private suspend fun observeChatRoomsAdvanced() {
        observeChatsByUserIdUseCase(currentUsrUid).collect{
            chatItems ->
            if(chatItems.isEmpty()){
                _state.value = state.value.copy(
                    contentState = ContentStateScreen.EmptyType
                )
                Log.d(TAG,"Чатов нет Высветить пустой экран с текстом")
                return@collect
            }

            val chatItemAdvenced = chatItems.map {
                it.toUiChatList(currentUsrUid)
            }.sortedByDescending { it.timestamp }

            _state.value = state.value.copy(
                chatListAdv = chatItemAdvenced,
                contentState = ContentStateScreen.Content
            )
        }
    }

}


