package com.example.unmei.presentation.chat_list_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.usecase.GetUserByIdUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.ObserveUserStatusByIdUseCase
import com.example.unmei.domain.usecase.ObserveUserUseCase
import com.example.unmei.domain.usecase.SetStatusUserUseCase
import com.example.unmei.presentation.chat_list_feature.model.ChatItemAdvenced
import com.example.unmei.presentation.chat_list_feature.model.ChatItemUI
import com.example.unmei.presentation.chat_list_feature.model.ChatListItemUiAdv
import com.example.unmei.presentation.chat_list_feature.model.ChatVMState
import com.example.unmei.presentation.chat_list_feature.model.TypingStatus
import com.example.unmei.presentation.chat_list_feature.model.contentMessage
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatListViewModel @Inject constructor(
    val  observeUserUseCase: ObserveUserUseCase,
    val observeRoomsUserUseCase: ObserveRoomsUserUseCase,
    val observeChatRoomUseCase: ObserveChatRoomUseCase,
    val observeUserStatusByIdUseCase: ObserveUserStatusByIdUseCase,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val repository: MainRepository,
    val setSetStatusUserUseCase: SetStatusUserUseCase,
    val remote:RemoteDataSource

):ViewModel() {
    private  val _state = MutableStateFlow<ChatVMState>(ChatVMState())
    val state: StateFlow<ChatVMState> = _state.asStateFlow()


    private val currentUsrUid = Firebase.auth.currentUser!!.uid

    val db= FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
    val groupsRef = ""
    val refGroups= db.getReference("groups")
    val refMessages=db.getReference("messages")

    private val currentUserId ="u1DDSWtIHOSpcHIkLZl0SZGEsmB3";
    private lateinit var userChats:StateFlow<List<ChatItemUI>>



     init {

        // observeChatRooms()
         val currentUserUid = "u1DDSWtIHOSpcHIkLZl0SZGEsmB3"
         viewModelScope.launch {

             observeChatRoomsAdvanced()
//             val userChatsWithStatus = userChats.flatMapLatest { listChat ->
//                 val chatFlows = listChat.map { chatItem ->
//                     if (chatItem.type == "private") {
//                         val idCompanion = chatItem.members.first { it != currentUserUid }
//                         // Одиночный запрос: загружаем данные собеседника
//                         val companionInfo =
//                             getUserByIdUseCase.execute(idCompanion) // suspend-функция
//
//
//                         // Наблюдение за онлайном
//                         val statusFlow = observeUserStatusByIdUseCase.execute(idCompanion)
//                         // Объединяем статические данные с реактивными
//                         combine(
//                             flowOf(companionInfo), // Одиночный запрос
//                             statusFlow // Реактивный статус
//                         ) { info, status ->
//                             chatItem.copy(
//                                 isOnline = if (status.status == Status.ONLINE) true else false,
//                                 nameChat = companionInfo?.fullName ?: "Unknown"
//                             )
//                         }
//                     } else {
//                         flowOf(chatItem)
//                         // Наблюдение за печатью (если нужно)
////                     val typingFlow = observeTypingStatus(chatItem.id)
////
////                     typingFlow.map { isTyping ->
////                         chatItem.copy(isTyping = isTyping)
////                     }
//
//                     }
//                 }
//                 // Объединяем все чаты в один поток
//                 combine(chatFlows) { it.toList() }
//             }.collect{
//                 _state.value = state.value.copy(
//                     chatList = it,
//                     isLoading = false
//                 )
//
//             }

         }


     }

    private suspend fun observeChatRoomsAdvanced() {
        observeRoomsUserUseCase.execute(currentUserId)
            .collectLatest { listRooms ->
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
                                    iconUrl = it.photo
                                )
                            }
                            observeUserStatusByIdUseCase.execute(idCompanion)
                        } else {
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
                            it.run {
                                ChatListItemUiAdv(
                                    chatName = it.chatRoom.chatName,
                                    chatId = it.chatRoom.id,
                                    typeChat = it.chatRoom.type,
                                    iconUrl = it.chatRoom.iconUrl,
                                    isOnline = it.status.status==Status.ONLINE,
                                    members = it.chatRoom.members.toSet(),
                                    moderators = it.chatRoom.moderators?.toSet(),
                                    timestamp = it.summaries.lastMessage?.timestamp ?: 0L,
                                    typingStatus = if (!it.summaries.typingUsersStatus.isEmpty()){
                                        val typingUsersSet = it.summaries.typingUsersStatus.toMutableSet()
                                            typingUsersSet.remove(currentUserId)
                                        var whoTyping:String ="Участник"
                                        if (it.chatRoom.type == TypeRoom.PRIVATE){
                                            whoTyping = it.chatRoom.chatName
                                        }
                                        TypingStatus.Typing(whoTyping.plus(":"))
                                    }else TypingStatus.None,
                                    unreadedCountMessage = it.summaries.unreadedCount[currentUsrUid]?: 0,
                                    lastMessageTimeString = if (it.summaries.lastMessage!=null) toStringTime(it.summaries.lastMessage.timestamp)else "",
                                    contentMessage=if (it.summaries.lastMessage!=null){
                                        val lastMessage = it.summaries.lastMessage
                                        var sender ="Вы"
                                        if (it.chatRoom.type== TypeRoom.PUBLIC){
                                            sender="Участник"
                                        }else{
                                            if (it.summaries.lastMessage.senderId!=currentUserId)
                                                sender = it.chatRoom.chatName
                                        }
                                        contentMessage(
                                            contentSender =sender.plus(":"),
                                            message = lastMessage
                                        )
                                    }else null
                                )
                            }
                        }
                        _state.value = state.value.copy(
                             chatListAdv = chatItemAdvenced,
                             isLoading = false
                        )
                    }
            }
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


    private fun observeChatRooms(){
         userChats=observeRoomsUserUseCase
             .execute(currentUserId).flatMapLatest {
                     listRooms->
                     combine(
                     listRooms.map {
                             roomsId->

                         //getChatRoomById()
                            observeChatRoomUseCase.execute(roomsId)
                     }
                 ){
                     it.toList().map { it.toChatItemUi() }
                 }
             }.stateIn(
                 viewModelScope,
                 SharingStarted.Lazily,//после появления первого подписчика
                 emptyList()
             )
     }




    fun observeUser(userid:String = "auegBKgwyvbwge7PQs9nfFRRFfj1"){
        viewModelScope.launch {
            observeUserStatusByIdUseCase.execute(userid).collect{
                data ->
                _state.value = state.value.copy(
                    isOnline = when(data.status){
                        Status.ONLINE -> true
                        else-> false
                    }
                )
            }

//            observeUserUseCase.execute(userid).collect{
//                    user->
//                _state.value = state.value.copy(
//                    isOnline = user.online
//                )
//                    //Log.d(TAG,user.online.toString())
//              //  Log.d(TAG,user.rooms.toString())
//
//            }
        }
    }


}


