package com.example.unmei.presentation.conversation_future.viewmodel

import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.model.messages.Attachment
import com.example.unmei.domain.model.messages.RoomDetail
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.messages.CreatePrivateChatUseCase
import com.example.unmei.domain.usecase.messages.EnterChatUseCase
import com.example.unmei.domain.usecase.messages.LeftChatUseCase
import com.example.unmei.domain.usecase.messages.NotifySendMessageUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomAdvanceUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomSummariesUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.messages.SendMessageUseCaseById
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.user.ObserveUserStatusByIdUseCase
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.conversation_future.model.ContentStateScreen
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.model.MessageListItemUI
import com.example.unmei.presentation.conversation_future.model.MessageType
import com.example.unmei.presentation.util.model.NavigateConversationData
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@RequiresApi(35)
@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val remote:RemoteDataSource,
    private val repository: MainRepository,
    private val createPrivateChatUseCase: CreatePrivateChatUseCase,
    private val sendMessageUseCaseById:  SendMessageUseCaseById,
    private val notifySendMessageUseCase: NotifySendMessageUseCase,
    private val observeUserStatusByIdUseCase: ObserveUserStatusByIdUseCase,
    private val observeRoomSummariesUseCase: ObserveRoomSummariesUseCase,

    private val observeChatRoomAdvanceUseCase: ObserveChatRoomAdvanceUseCase,
    private val enterChatUseCase: EnterChatUseCase,
    private val leftChatUseCase: LeftChatUseCase,
    //из room должно быть
    private val getUserByIdUseCase: GetUserByIdUseCase
):ViewModel() {

    val _state = MutableStateFlow<ConversationVMState>(ConversationVMState())
    val state:StateFlow<ConversationVMState> = _state.asStateFlow()

    private var lastLoadedIdMessage: String = ""
    private val currentUsrUid = Firebase.auth.currentUser!!.uid
    private val chatId="-OLURjaOA0bbkiWLD3jz"

    private var conversationInitJob: Job? = null

    //Свежее(Почти сырое)
     private val actualDataRoom = MutableStateFlow<ChatRoomAdvance?>(null)
     private val actualUnreadCount = MutableStateFlow<Map<String,Int>>(emptyMap())


    init {


    }
    private fun observeActualDataRoom(chatId: String){
        observeChatRoomAdvanceUseCase.execute(chatId).onEach {
                chatRoom ->
              actualDataRoom.value =chatRoom
        }.launchIn(
            viewModelScope
        )
    }

    private fun observeStatusChat(){
        viewModelScope.launch {
            val statusFlow =observeUserStatusByIdUseCase.execute(state.value.companionId)
            val summariesFlow = observeRoomSummariesUseCase.execute(state.value.groupId)
            combine(summariesFlow,statusFlow){ summary, presence ->
                Pair( presence,summary)
            }.collect{
                val statusUser = it.first
                val summeriesChat= it.second
                actualUnreadCount.value = it.second.unreadedCount


                when(statusUser.status){
                    Status.OFFLINE ->_state.value=state.value.copy(
                    statusChat = getAdvancedStatusUser(statusUser.lastSeen)
                    )
                    Status.ONLINE -> _state.value=state.value.copy(statusChat ="Online")
                    Status.RECENTLY -> _state.value=state.value.copy(statusChat ="был(а) недавно")
                }

                _state.value=state.value.copy(
                    isTyping = summeriesChat.typingUsersStatus.contains(state.value.companionId)
                )

            }
        }
    }
    private fun getAdvancedStatusUser(timeStamp:Long):String{
        val now = LocalDateTime.now().toLocalDate()
        val date = Instant.ofEpochMilli(timeStamp)
            .atZone(ZoneOffset.UTC) // Устанавливаем временную зону
            .toLocalDate() // Преобразуем в локальную дату
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        if (now==date){
           return "был(а) в "+sdf.format(Date(timeStamp))
        }
        if(
            (now.year==date.year)&&(now.dayOfMonth==date.dayOfMonth+1)
            ){
            return "был(а) вчера в "+sdf.format(Date(timeStamp))
        }
        val russianDayOfWeek = date.month.getDisplayName(TextStyle.SHORT, Locale("ru"))
        return "был(а) "+date.dayOfMonth.toString()+" "+russianDayOfWeek+" в "+sdf.format(Date(timeStamp))

    }

    @RequiresApi(35)
    private suspend fun initFirstMassages(chatId:String){
        repository.getBlockMessagesByChatId(chatId).collect {
            when (it) {
                is Resource.Error -> {
                    Log.d(TAG,"ошибка получения BlockMessage ${it.message}")
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingScreen = true
                    )
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        val responseMessages = it.data
                        val Messages = responseMessages.map {
                            it.toMessageListItemUI(currentUsrUid)
                        }

                        val uiMessages = Messages.asReversed()
                        val grouped = getGroupedMessages(uiMessages)
                        //   val uiMessageReversed = uiMessages.reversed()

                        Log.d(TAG,"first: ${uiMessages.first().timestamp} last: ${uiMessages.last().timestamp}")

//                        lastLoadedTimestamp = uiMessages.first().timestamp
//                        lastLoadedIdMessage = uiMessages.first().messageId

                        _state.value = state.value.copy(
                            listMessage = uiMessages,
                            loadingScreen = false,
                            groupedMapMessage = grouped
                        )

                    }
                }
            }
        }
    }
    fun saveNecessaryInfo(conversationNavData: NavigateConversationData){
       // Log.d(TAG,"ChatICon "+conversationNavData.chatUrl)
        _state.value = state.value.copy(
            chatFullName = conversationNavData.chatName,
            chatIconUrl = conversationNavData.chatUrl,
            companionId = conversationNavData.companionUid,
            contentState = ContentStateScreen.Loading,
            chatExistence = conversationNavData.chatExist,
            groupId = conversationNavData.chatUid ?: ""
        )
        selectContentState()
    }


    private suspend fun initConversation(){
        _state.value = state.value.copy(
            contentState = ContentStateScreen.Content,
            chatExistence = true
        )

        //getGroupById
        //
        observeStatusChat()
        observeActualDataRoom(state.value.groupId)
        enterChatUseCase.execute(chatId = state.value.groupId,currentUsrUid )
        observeMessages(state.value.groupId)




    }

    private fun selectContentState(){
        viewModelScope.launch {
            if (state.value.chatExistence){
                conversationInitJob?.cancel()
                conversationInitJob= launch { initConversation() }
                return@launch
            }
            val existence = repository
                .getExistencePrivateGroupByUids(
                currentUsrUid,
                state.value.companionId
                )
            if (existence!=null){//узнать если чат chats_by_Users
                _state.value = state.value.copy(
                    groupId = existence
                )
                conversationInitJob?.cancel()
                conversationInitJob= launch { initConversation() }
                return@launch
            }
            //ГГ чата нет, первое отправленое сообщение должно
            // создать приватную группу и перевести chatExistenc=true
            _state.value = state.value.copy(
                contentState = ContentStateScreen.EmptyType
            )
        }
    }

    private fun createNewChat(message: Message){
        viewModelScope.launch {
            val result =createPrivateChatUseCase.execute(
                chatName = state.value.chatFullName,
                iconUrl =state.value.chatIconUrl,
                membersIds = listOf(currentUsrUid,state.value.companionId),
                message = message
            )
            when(result){
                is Resource.Error ->{
                    Log.d(TAG,"Ошибка: "+result.message.toString())
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    _state.value = state.value.copy(
                        groupId = result.data.toString()
                    )
                    Log.d(TAG,"Успех: "+result.data.toString())
                    conversationInitJob?.cancel()
                    conversationInitJob= launch { initConversation() }
                    return@launch
                }
            }
        }
    }

    private fun uploadFile(fileUri: Uri) {
        val storageRef = FirebaseStorage.getInstance(ConstansDev.YOUR_PATHFOLDER_STORAGE).getReference("TestImages");
        val fileRef = storageRef.child("${System.currentTimeMillis()}.jpg")
        val ref=fileRef.putFile(fileUri)
    }

    private suspend fun observeMessages(chatId: String){
         repository.observeMessagesInChat(chatId).collect{
             when(it){
                 is ExtendedResource.Added -> {
                     it.data?.let{
                         val newMessageUi= it.toMessageListItemUI(currentUsrUid)
                         val key = newMessageUi.timestamp.toLocalDate()

                         state.value.listMessage.any { it==newMessageUi }.let {
                             found->
                             if (!found){
                                 var newListred = mutableListOf(newMessageUi)
                                 newListred.addAll(state.value.listMessage)
                                 _state.value =state.value.copy(
                                     listMessage = newListred
                                 )
                             }

                         }

//                       //  Log.d(TAG,"Данные получены")
//
//                         val timeWork=measureTimeMillis {
//                         //есть ли сообщеения в этом дне
//                         if(state.value.groupedMapMessage.containsKey(key)){//O(1)
//                           //  Log.d(TAG,"Такая группа есть")
//
//                             state.value.groupedMapMessage[key]?.let {listUi->
//                                 listUi.any { it==newMessageUi}.let { found->
//                                     if (!found){//нету такого сообщения
//                                         val newList = mutableListOf(newMessageUi)
//                                         newList.addAll(listUi)
//                                         val originalMap =state.value.groupedMapMessage
//                                         originalMap[key] = newList
//                                         Log.d(TAG,originalMap[key].toString())
//                                        _state.value= state.value.copy(
//                                            groupedMapMessage = originalMap
//                                        )
//                                         Log.d(TAG,state.value.groupedMapMessage[key].toString())
//                                     }
//                                 }
//                             }
//                         }else{
//                           //  Log.d(TAG,"Такой группа нет")
//                             val newLinkedHashMap = linkedMapOf(key to listOf(newMessageUi))
//                             newLinkedHashMap.putAll(state.value.groupedMapMessage)
//                             _state.value =state.value.copy(
//                                 groupedMapMessage = newLinkedHashMap
//                             )
//                         }
                   //      }
                     }

                 }
                 is ExtendedResource.Edited -> {
                     Log.d(TAG,"Сообщение редактировано")

                 }
                 is ExtendedResource.Error -> {

                 }
                 is ExtendedResource.Removed -> {
                     Log.d(TAG,"Сообщение удалено")
                     it.data?.let {
                     }
                 }
             }
         }
     }



    fun getGroupedMessages(listItemUI: List<MessageListItemUI>): LinkedHashMap<LocalDate, List<MessageListItemUI>> {
        val grouped = listItemUI.groupBy {
            it.timestamp.toLocalDate()
        }
        grouped.forEach {
            it.value.forEach {
            }
            Log.d(TAG, "------")
        }
        return LinkedHashMap(grouped)
    }


    private fun onChangeSelectedMessages(
        messageId:String
    ){
        if(messageId.isEmpty())
            return
        //пришел id  который уже есть и он 1, значит отменяем выбор сообщений
        if ((state.value.selectedMessages.size ==1) && (state.value.selectedMessages.containsKey(messageId))){
            _state.value = state.value.copy(
                selectedMessages = emptyMap(),
                optionsVisibility = false
            )
            return
        }
        //пришло id а список пуст значит пользователь хочет что сделать с сообщениями
        if(state.value.selectedMessages.isEmpty()){
            _state.value = state.value.copy(
                selectedMessages =  state.value.selectedMessages.plus(
                    messageId to true
                ),
                optionsVisibility = true
            )
            return
        }
        val isSelected = state.value.selectedMessages[messageId] == true
        //тот же пришедшйи id удаляем, новый прибавлем
        if (isSelected) {
            _state.value = state.value.copy(
                selectedMessages =  state.value.selectedMessages.minus(messageId)
            )
        } else {
            _state.value = state.value.copy(
                selectedMessages =  state.value.selectedMessages.plus(messageId to true)
            )
        }

    }

    //добавление сообщения типа ONLYIMAGES в список без отправки
    fun testFun(){
        val item = MessageListItemUI(
            text = "",
            timestamp = LocalDateTime.now(),
            timeString = "3:07",
            fullName = "",
            isOwn = true,
            isChanged = true,
            type = MessageType.Image(""),
            status = MessageStatus.Readed,
            attachments = listOf(Attachment.Image(state.value.selectedUrisForRequest.first().toString()))
        )
        viewModelScope.launch {
            uploadFile(state.value.selectedUrisForRequest.first())
        }
        Log.d(TAG,state.value.selectedUrisForRequest.toString())
        val currentList = state.value.listMessage.toMutableList()
        currentList.add(0, item)
        _state.value= state.value.copy(
            listMessage = currentList,
            selectedUrisForRequest = emptyList()
        )

    }

   // @Deprecated("Не подходит под новый ТИП сообщения")
    private fun loadOldMessages(){
        viewModelScope.launch {
            if(lastLoadedIdMessage.isEmpty()){
                return@launch
            }
            repository.getBlockMessagesByChatId(
                chatId, lastMessageKey = lastLoadedIdMessage
            ).collect{
                when (it) {
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            loadingOldMessages = true
                        )
                    }
                    is Resource.Success -> {
                        if (it.data != null) {
                            val responseMessages = it.data
                            val uiMessages = responseMessages.map {
                                it.toMessageListItemUI(currentUsrUid)
                            }
                            Log.d(TAG,"OLDMESSAGE first: ${uiMessages.first().timestamp} last: ${uiMessages.last().timestamp}")
//                            lastLoadedTimestamp = uiMessages.first().timestamp
//                            lastLoadedIdMessage = uiMessages.first().messageId
                            val newlist =uiMessages.plus(state.value.listMessage.toMutableList())
                            _state.value = state.value.copy(
                                listMessage = newlist,
                                loadingOldMessages = false
                            )
                        }
                    }
                }
            }
        }
    }

    private  fun sendMessageById(
        message: Message,
        chatId: String
    ){
        Log.d(TAG,"Sending Message id: $chatId")
        viewModelScope.launch {
            val actual = actualDataRoom.value ?: return@launch
            var offlineUsersIds = (actual.members.toSet()-actual.activeUsers.toSet())- setOf(currentUsrUid)
            Log.d(TAG,"Кому отправить уведомление ${offlineUsersIds}")
            val currentUser=getUserByIdUseCase.execute(currentUsrUid)?:User(
                fullName = currentUsrUid,
                photoUrl = "",
                userName = ""
            )
            val roomDetail = RoomDetail(roomId =chatId, roomIconUrl =currentUser.photoUrl , roomName = currentUser.fullName, typeRoom = TypeRoom.PRIVATE,)
            val result=sendMessageUseCaseById.execute(
                message = message,
                chatId = chatId,
                offlineUsersIds=offlineUsersIds.toList(),
                prevUnreadCount =actualUnreadCount.value,
                roomDetail=roomDetail
            )
            when (result){
                is Resource.Error -> {}
                is Resource.Loading ->{}
                is Resource.Success -> {}
            }
        }
    }

    fun selectActionWithMessage(){
        if (!state.value.textMessage.isEmpty() || !state.value.selectedUrisForRequest.isEmpty()){
            //Log.d(TAG,"Попали в условие  ${state.value.chatExistence}")
            val newMessage = Message(
                senderId = currentUsrUid,
                text = state.value.textMessage,
              //  attachment = state.value.selectedUrisForRequest
            )
            if (!state.value.chatExistence ){
                //create Chat
                createNewChat(newMessage)
                return
            }
            //send Message
            sendMessageById(message = newMessage, chatId = state.value.groupId)
        }
    }

    fun onEvent(event: ConversationEvent){
       when(event){
           ConversationEvent.LoadingNewMessage -> {
               loadOldMessages()
           }
           is ConversationEvent.ChangeSelectedMessages -> {
              onChangeSelectedMessages(event.id)
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
               selectActionWithMessage()
           }
           is ConversationEvent.OnValueChangeTextMessage -> {_state.update { it.copy(textMessage = event.text) }}

           ConversationEvent.Offoptions -> {
               _state.value = state.value.copy(
                   optionsVisibility =false,
                   selectedMessages = emptyMap()
               )
           }

           ConversationEvent.DeleteSelectedMessages ->{
               deleteMessagesInChat()
           }

           ConversationEvent.LeftChat -> leftChat()
       }
    }
    private fun leftChat(){
        viewModelScope.launch {
            leftChatUseCase.execute(state.value.groupId,currentUsrUid)
        }
    }

    @Deprecated("Удалят и наши и ваши сообщения")
    fun deleteMessagesInChat(){
        viewModelScope.launch {
            if(state.value.selectedMessages.isEmpty())
                return@launch
            val messagesId = state.value.selectedMessages.keys.toList()
            repository.deleteMessagesInChat(messagesId, chatId = chatId).collect{
                when(it){
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val origanlist =state.value.listMessage.toMutableList()

                        origanlist.removeAll { it.messageId in messagesId }

                        _state.value= state.value.copy(
                            listMessage =origanlist
                        )
                    }
                }
            }
        }
    }
}