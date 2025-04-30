package com.example.unmei.presentation.groupChat_feature.viemodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.domain.usecase.messages.EnterChatUseCase
import com.example.unmei.domain.usecase.messages.LeftChatUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomAdvanceUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomSummariesUseCase
import com.example.unmei.domain.usecase.messages.SetTypingStatusUseCase
import com.example.unmei.domain.usecase.user.ObserveUserStatusByIdUseCase
import com.example.unmei.presentation.groupChat_feature.model.GroupChatVMState
import com.example.unmei.presentation.singleChat_feature.model.ConversationEvent
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val observeUserStatusByIdUseCase: ObserveUserStatusByIdUseCase,
    private val observeRoomSummariesUseCase: ObserveRoomSummariesUseCase,
    private val observeChatRoomAdvanceUseCase: ObserveChatRoomAdvanceUseCase,
    private val setTypingStatusUseCase: SetTypingStatusUseCase,
    private val enterChatUseCase: EnterChatUseCase,
    private val leftChatUseCase: LeftChatUseCase,
    private val savedStateHandle: SavedStateHandle
):ViewModel() {

    private val _state = MutableStateFlow<GroupChatVMState>(GroupChatVMState())
    private val actualDataRoom = MutableStateFlow<ChatRoomAdvance?>(null)
    private val actualUnreadCount = MutableStateFlow<Map<String,Int>>(emptyMap())

    val state: StateFlow<GroupChatVMState> = _state.asStateFlow()

    init {
        getNavArguments()
    }

    private fun getNavArguments(){
        val navChatId = savedStateHandle.get<String>(ConstansApp.GROUPCHAT_ARGUMENT_CHATID)
        val currentUserId =  Firebase.auth.currentUser?.uid

        if((navChatId==null) && (currentUserId==null)){
            //ошибка загрузки чата
            return
        }
        _state.update {it.copy(
            chatId = navChatId!!,
            currentUsrUid = currentUserId!!
            )
        }
    }

    private fun initChatObserving(){


        enterChat(state.value.chatId,state.value.currentUsrUid)
        observeActualDataRoom(state.value.chatId)
    }
    private fun observeActualDataRoom(chatId: String){
        observeChatRoomAdvanceUseCase.execute(chatId).onEach {
                chatRoom -> actualDataRoom.value =chatRoom
        }.launchIn(viewModelScope)
    }
    private fun enterChat(chatId: String,currentUserId:String){
        viewModelScope.launch {
            Log.d(TAG,"Вход в чат")
            enterChatUseCase.execute(chatId = chatId,currentUserId)
        }
    }
    private fun leftChat(){
        viewModelScope.launch {
            leftChatUseCase.execute(state.value.chatId,state.value.currentUsrUid)
        }
    }
    fun onEvent(event: ConversationEvent){
        when(event){
            ConversationEvent.LoadingNewMessage -> {
             //   loadOldMessages()
            }
            is ConversationEvent.ChangeSelectedMessages -> {
              //  onChangeSelectedMessages(event.id)
            }
            is ConversationEvent.OpenCloseBottomSheet -> {
                _state.value= state.value.copy(
                    bottomSheetVisibility = !state.value.bottomSheetVisibility
                )
            }
            is ConversationEvent.SelectedMediaToSend -> {
                _state.value = state.value.copy(
                    selectedUrisForRequest = event.value,
                    bottomSheetVisibility = !state.value.bottomSheetVisibility
                )
            }

            is ConversationEvent.SendMessage -> {
              //  selectActionWithMessage()
            }
            is ConversationEvent.OnValueChangeTextMessage -> {_state.update { it.copy(textMessage = event.text) }}
            ConversationEvent.Offoptions -> {
                _state.value = state.value.copy(
                    optionsVisibility =false,
                    selectedMessagesIds = emptySet()
                )
            }
            ConversationEvent.DeleteSelectedMessages ->{
              //  deleteMessagesInChat()
            }
            ConversationEvent.LeftChat -> leftChat()
        }
    }

}