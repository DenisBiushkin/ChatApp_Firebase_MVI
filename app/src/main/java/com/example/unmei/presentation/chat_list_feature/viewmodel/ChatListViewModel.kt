package com.example.unmei.presentation.chat_list_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.user.ObserveUserStatusByIdUseCase
import com.example.unmei.presentation.chat_list_feature.model.ChatItemAdvenced
import com.example.unmei.presentation.chat_list_feature.model.ChatListItemUiAdv
import com.example.unmei.presentation.chat_list_feature.model.ChatVMState
import com.example.unmei.presentation.chat_list_feature.model.TypingStatus
import com.example.unmei.presentation.chat_list_feature.model.contentMessage
import com.example.unmei.presentation.conversation_future.model.ContentStateScreen
import com.example.unmei.util.ChatSessionManager
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.system.measureNanoTime
import kotlin.time.measureTime


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatListViewModel @Inject constructor(
    val observeRoomsUserUseCase: ObserveRoomsUserUseCase,
    val observeUserStatusByIdUseCase: ObserveUserStatusByIdUseCase,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val chatSessionManager: ChatSessionManager,
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
        observeRoomsUserUseCase.execute(currentUsrUid)
            .collectLatest { listRooms ->
                //чекнуть на Null->EmptyScreen()
                if (listRooms==null){
                    _state.value=state.value.copy(contentState = ContentStateScreen.EmptyType)
                    return@collectLatest
                }
                val chatFlows = listRooms.mapNotNull { roomId ->
                    remote.getChatRoomById(roomId)?.let { chatRoom ->
                        val summariesFlow = remote.observeRoomSammaries(chatRoom.id)
                        var newChatRoom = chatRoom
                        val statusFlow = if (chatRoom.type == TypeRoom.PRIVATE) {
                            val idCompanion = chatRoom.members.first { it != currentUsrUid }
                            val userData = getUserByIdUseCase.execute(idCompanion)
                            Log.d(TAG,"USerDATA $userData")
                            userData?.let {
                                newChatRoom = chatRoom.copy(
                                    chatName = it.fullName,
                                    iconUrl = it.photoUrl
                                )
                            }
                            observeUserStatusByIdUseCase.execute(idCompanion)
                        } else {
                            //получить имена всех абобусов
                            flowOf(StatusUser(status = Status.OFFLINE, lastSeen = 0L))
                        }
                        combine(statusFlow, summariesFlow) { status, summaries ->
                            ChatItemAdvenced(
                                chatRoom = newChatRoom,
                                status = status,
                                summaries = summaries
                            )
                        }
                    }
                }
                    combine(chatFlows){
                        it.toList()
                    }
                    .collect { chatItems ->
                        val chatItemAdvenced = chatItems.map {
                            toUiChatList(it)
                        }.sortedByDescending { it.timestamp }
                        _state.value = state.value.copy(
                             chatListAdv = chatItemAdvenced,
                             contentState = ContentStateScreen.Content
                        )
                    }
            }
    }
    private fun toUiChatList(chatItemAdvenced: ChatItemAdvenced):ChatListItemUiAdv {
         val it= chatItemAdvenced

        return ChatListItemUiAdv(
                chatName = it.chatRoom.chatName,
                chatId = it.chatRoom.id,
                typeChat = it.chatRoom.type,
                iconUrl = it.chatRoom.iconUrl,
                isOnline = it.status.status == Status.ONLINE,
                members = it.chatRoom.members.toSet(),
                moderators = it.chatRoom.moderators?.toSet(),
                timestamp = it.summaries.lastMessage?.timestamp ?: 0L,
                typingStatus =
                if (!it.summaries.typingUsersStatus.isEmpty()) {

                    val typingUsersSet = it.summaries.typingUsersStatus.toMutableSet()
                    typingUsersSet.remove(currentUsrUid)
                    var whoTyping: String = "Участник"
                    if (it.chatRoom.type == TypeRoom.PRIVATE) {
                        whoTyping = it.chatRoom.chatName
                    }
                    TypingStatus.Typing(whoTyping.plus(":"))
                } else
                    TypingStatus.None,
                unreadedCountMessage = it.summaries.unreadedCount[currentUsrUid] ?: 0,
                lastMessageTimeString = if (it.summaries.lastMessage != null) toStringTime(it.summaries.lastMessage.timestamp) else "",
                contentMessage = if (it.summaries.lastMessage != null) {
                    val lastMessage = it.summaries.lastMessage
                    var sender = "Вы"
                    if (it.chatRoom.type == TypeRoom.PUBLIC) {
                        sender = "Участник"
                    } else {
                        if (it.summaries.lastMessage.senderId != currentUsrUid)
                            sender = it.chatRoom.chatName
                    }
                    contentMessage(
                        contentSender = sender.plus(":"),
                        message = lastMessage
                    )
                } else null
            )

    }
    private fun toStringTime(timeStamp:Long):String{
        //val timeStamp: Long =1737392296
        val formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")
        val date = Instant.ofEpochMilli(timeStamp)
            .atZone(ZoneOffset.UTC) // Устанавливаем временную зону
            .toLocalDate() // Преобразуем в локальную дату
        val text = date.format(formatter)
        val russianDayOfWeek = date.month.getDisplayName(TextStyle.SHORT, Locale("ru"))
        return date.dayOfMonth.toString()+" "+russianDayOfWeek
    }

}


