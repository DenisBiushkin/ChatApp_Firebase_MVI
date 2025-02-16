package com.example.unmei.presentation.chat_list_feature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.usecase.GetUserByIdUseCase
import com.example.unmei.domain.usecase.ObserveChatRoomUseCase
import com.example.unmei.domain.usecase.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.ObserveUserStatusByIdUseCase
import com.example.unmei.domain.usecase.ObserveUserUseCase
import com.example.unmei.domain.usecase.SetStatusUserUseCase
import com.example.unmei.presentation.chat_list_feature.model.ChatItemUI
import com.example.unmei.presentation.chat_list_feature.model.ChatVMState
import com.example.unmei.util.ConstansDev
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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




    val db= FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
    val groupsRef = ""
    val refGroups= db.getReference("groups")
    val refMessages=db.getReference("messages")

    private val currentUserId ="u1DDSWtIHOSpcHIkLZl0SZGEsmB3";
    private lateinit var userChats:StateFlow<List<ChatItemUI>>



     init {
         viewModelScope.launch {
             val uidMessage= refMessages.push().key
            val mes= Message(
                 id = uidMessage!!,
                 senderId = "auegBKgwyvbwge7PQs9nfFRRFfj1",
                 text="Первое сообщение",
                 timestamp = ServerValue.TIMESTAMP,
                 type = "text",
                 readed = true,
                 mediaUrl = "url",
                 edited = true
             )
             val users= listOf("auegBKgwyvbwge7PQs9nfFRRFfj1","u1DDSWtIHOSpcHIkLZl0SZGEsmB3")

//             val res=remote.createPrivateRoom(users, message = mes)
//             res.data?.let {
//                 remote.saveIdRoomInUsers(users,it)
//             }

         }
         observeChatRooms()
         val currentUserUid = "u1DDSWtIHOSpcHIkLZl0SZGEsmB3"
         viewModelScope.launch {
             val userChatsWithStatus = userChats.flatMapLatest { listChat ->
                 val chatFlows = listChat.map { chatItem ->
                     if (chatItem.type == "private") {
                         val idCompanion = chatItem.members.first { it != currentUserUid }
                         // Одиночный запрос: загружаем данные собеседника
                         val companionInfo =
                             getUserByIdUseCase.execute(idCompanion) // suspend-функция
                         // Наблюдение за онлайном
                         val statusFlow = observeUserStatusByIdUseCase.execute(idCompanion)
                         // Объединяем статические данные с реактивными
                         combine(
                             flowOf(companionInfo), // Одиночный запрос
                             statusFlow // Реактивный статус
                         ) { info, status ->
                             chatItem.copy(
                                 isOnline = if (status.status == Status.ONLINE) true else false,
                                 nameChat = companionInfo?.fullName ?: "Unknown"
                             )
                         }
                     } else {
                         flowOf(chatItem)
                         // Наблюдение за печатью (если нужно)
//                     val typingFlow = observeTypingStatus(chatItem.id)
//
//                     typingFlow.map { isTyping ->
//                         chatItem.copy(isTyping = isTyping)
//                     }

                     }
                 }
                 // Объединяем все чаты в один поток
                 combine(chatFlows) { it.toList() }
             }.collect{
                 _state.value = state.value.copy(
                     chatList = it
                 )
             }

         }


     }
     private fun observeChatRooms(){
         userChats=observeRoomsUserUseCase
             .execute(currentUserId).flatMapLatest {
                     listRooms->
                 combine(
                     listRooms.map {
                             roomsId->
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
    fun sendCommand(){
        //при отпрвьке сообщения генерируется ключ для сообщения
        //зашивается гурппа в groups в realtime db
        //зашивается в User->groups обеим участникам или же всем кто в группе
        //
        val uidGroup = refGroups.push().key
        val uidMessage= refMessages.push().key
        //у каждой группы своя пачкас сообщений
        refMessages.child("-OGfKvopKTMOCp_bxJqe")
            .child(uidMessage!!).setValue(
            Message(
                id = uidMessage!!,
                senderId = "userId1",
                text="Первое сообщение",
                timestamp = ServerValue.TIMESTAMP,
                type = "text",
                readed = true,
                mediaUrl = "url",
                edited = true
            )
        )
    }

    private fun sendGroup(uid:String){
//       // val uid = refGroups.push().key
//        //у каждой группы свой uid дублируется дублируется в id
        //refGroups.child(uid!!).setValue(
//            ChatRoom(
//                id= uid,
//                type= GroupType.PRIVATE.type,
//                timestamp =ServerValue.TIMESTAMP,
//                members = mapOf(
//                    "userId1" to true,
//                    "userId1" to true,
//                )
//            )
//        )
    }
//        val ref = db.getReference("Main_DB")
//        ref.child("Message").setValue("Same Text")
//        fs.collection("TestUser")
//            .document().set(
//                TestUser(
//                    "Anna",
//                    "Schneider",
//                    20
//                )
//            )

}


