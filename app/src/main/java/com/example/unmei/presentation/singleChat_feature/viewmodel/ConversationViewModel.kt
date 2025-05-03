package com.example.unmei.presentation.singleChat_feature.viewmodel

import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.model.messages.Attachment
import com.example.unmei.domain.model.messages.RoomDetail
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.model.TypingStatus
import com.example.unmei.domain.model.UploadProgress
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.messages.CreatePrivateChatUseCase
import com.example.unmei.domain.usecase.messages.EnterChatUseCase
import com.example.unmei.domain.usecase.messages.LeftChatUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomAdvanceUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomSummariesUseCase
import com.example.unmei.domain.usecase.messages.SendMessageByChatIdWithLoadingFlow
import com.example.unmei.domain.usecase.messages.SendMessageUseCaseById
import com.example.unmei.domain.usecase.messages.SetTypingStatusUseCase
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.user.ObserveUserStatusByIdUseCase
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.singleChat_feature.model.AttachmentTypeUI
import com.example.unmei.presentation.singleChat_feature.model.ChatState
import com.example.unmei.presentation.singleChat_feature.model.ContentStateScreen
import com.example.unmei.presentation.singleChat_feature.model.ConversationEvent
import com.example.unmei.presentation.singleChat_feature.model.ConversationVMState
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.singleChat_feature.model.MessageType
import com.example.unmei.presentation.singleChat_feature.model.AttachmentUi
import com.example.unmei.util.ConstansApp.CHAT_ARGUMENT_JSON
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
import kotlin.time.measureTime


@RequiresApi(35)
@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: MainRepository,
    private val createPrivateChatUseCase: CreatePrivateChatUseCase,
    private val sendMessageUseCaseById:  SendMessageUseCaseById,
    private val observeUserStatusByIdUseCase: ObserveUserStatusByIdUseCase,
    private val observeRoomSummariesUseCase: ObserveRoomSummariesUseCase,

    private val observeChatRoomAdvanceUseCase: ObserveChatRoomAdvanceUseCase,
    private val setTypingStatusUseCase: SetTypingStatusUseCase,
    private val enterChatUseCase: EnterChatUseCase,
    private val leftChatUseCase: LeftChatUseCase,
    //из room должно быть
    private val getUserByIdUseCase: GetUserByIdUseCase,

    private val savedStateHandle: SavedStateHandle,
    private val sendMessageByChatIdWithLoadingFlow: SendMessageByChatIdWithLoadingFlow
):ViewModel() {

    val _state = MutableStateFlow<ConversationVMState>(ConversationVMState())
    val state:StateFlow<ConversationVMState> = _state.asStateFlow()

    private var lastLoadedIdMessage: String = ""
    private val currentUsrUid = Firebase.auth.currentUser!!.uid
    //Свежее(Почти сырое)
     private val actualDataRoom = MutableStateFlow<ChatRoomAdvance?>(null)
     private val actualUnreadCount = MutableStateFlow<Map<String,Int>>(emptyMap())

    init {
//        _state.update {
//            it.copy(
//                contentState = ContentStateScreen.Content
//            )
//        }
       initVM()
    }

    private fun initVM(){
        val navDataJson=savedStateHandle.get<String>(CHAT_ARGUMENT_JSON)
        if(navDataJson==null){
            _state.update {
                it.copy(
                    //тут скорее вобще должен быть error
                    contentState = ContentStateScreen.EmptyType
                )
            }
            return
        }
        val chatNavData= Screens.Chat.fromJsonToExistenceData(navDataJson)
        _state.value = state.value.copy(chatFullName = chatNavData.chatName, chatIconUrl = chatNavData.chatUrl,
            contentState = ContentStateScreen.Loading, chatExistence = chatNavData.chatExist,
            companionId = chatNavData.companionUid, groupId = chatNavData.chatUid ?: "")
        selectChatState()
    }
    private fun selectChatState(){
        viewModelScope.launch {
            if (state.value.groupId.isNotBlank()) {
                initConversation()
                return@launch
            }
            val groupId = repository.getExistencePrivateGroupByUids(currentUsrUid, state.value.companionId)

            if (groupId!=null){//узнать если чат chats_by_Users
                _state.update { it.copy(groupId = groupId) }
                initConversation()
                return@launch
            }
            //ГГ чата нет, первое отправленое сообщение должно
            _state.update { it.copy(
                    contentState = ContentStateScreen.EmptyType,
                    chatState = ChatState.Create
                )
            }
        }
    }
    private fun observeActualDataRoom(chatId: String){
        observeChatRoomAdvanceUseCase.execute(chatId).onEach {
                chatRoom -> actualDataRoom.value =chatRoom
        }.launchIn(viewModelScope)
    }
    private fun initConversation(){
        _state.value = state.value.copy(
            contentState = ContentStateScreen.Content,
            chatExistence = true,
            chatState = ChatState.Chatting
        )
        //getGroupById
        observeStatusChat()
        observeTypingStatus()
        observeActualDataRoom(state.value.groupId)
        enterChat(state.value.groupId,currentUsrUid)
        observeMessages(state.value.groupId)
    }
    private fun enterChat(chatId: String,currentUserId:String){
        viewModelScope.launch {
            Log.d(TAG,"Вход в чат")
            enterChatUseCase.execute(chatId = chatId,currentUserId)
        }
    }
    private fun leftChat(){
        viewModelScope.launch {
            leftChatUseCase.execute(state.value.groupId,currentUsrUid)
        }
    }
    private fun createNewChat(message: Message){
        viewModelScope.launch {
            val result =createPrivateChatUseCase.execute(chatName = state.value.chatFullName, iconUrl =state.value.chatIconUrl,
                membersIds = listOf(currentUsrUid,state.value.companionId),
                message = message)
            when(result){
                is Resource.Error ->{ Log.d(TAG,"Ошибка Создания чата: "+result.message.toString()) }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            groupId = result.data.toString(),
                            chatState = ChatState.Chatting
                        )
                    }
                    initConversation()
                }
            }
        }
    }
    @OptIn(FlowPreview::class)
    private fun observeTypingStatus() {
        viewModelScope.launch {
            var lastStatus: TypingStatus = TypingStatus.NONE

            state.map { it.textMessage }
                .debounce(1500) // 1.5 секунды, чтобы быстрее реагировать
                .map { text ->
                    if (text.isNotBlank()) TypingStatus.TYPING else TypingStatus.NONE
                }
                .distinctUntilChanged() // отсекаем одинаковые статусы подряд
                .collectLatest { newStatus ->

                    if (newStatus != lastStatus) {
                        setTypingStatusUseCase(
                            groupId = state.value.groupId,
                            userId = currentUsrUid,
                            status = newStatus
                        )
                        lastStatus = newStatus
                    }
                }
        }
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


                Log.d(TAG,"Companion id ${state.value.companionId}")
                _state.value=state.value.copy(
                    isTyping = summeriesChat.typingUsersStatus.contains(state.value.companionId)
                )

            }
        }
    }


    @RequiresApi(35)
    private suspend fun initFirstMassages(chatId:String){
        repository.getBlockMessagesByChatId(chatId).collect {
            when (it) {
                is Resource.Error -> { Log.d(TAG,"ошибка получения BlockMessage ${it.message}") }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingScreen = true
                    ) }
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


    private fun uploadFile(fileUri: Uri) {
        val storageRef = FirebaseStorage.getInstance(ConstansDev.YOUR_PATHFOLDER_STORAGE).getReference("TestImages");
        val fileRef = storageRef.child("${System.currentTimeMillis()}.jpg")
        val ref=fileRef.putFile(fileUri)
    }

    private fun observeMessages(chatId: String){
        viewModelScope.launch {
            repository.observeMessagesInChat(chatId).collect{
                when(it){
                    is ExtendedResource.Added -> {
                        it.data?.let{
                            val newMessageUi= mutableListOf( it.toMessageListItemUI(
                                owUid = currentUsrUid
                            ))

                            _state.update {
                                val newMessageList=newMessageUi.toMutableList().plus(state.value.listMessage)
                                it.copy(listMessage = newMessageList)
                            }
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
    private fun getRoomDetail():RoomDetail{
       return  RoomDetail(
            roomId =state.value.groupId,
            roomIconUrl =state.value.chatIconUrl,
            roomName = state.value.chatFullName,
            typeRoom = TypeRoom.PRIVATE,)
    }

    //добавление сообщения типа ONLYIMAGES в список без отправки
    fun testFun(){
        val genMessageUi=genLoadingMessage()
        viewModelScope.launch {
            val actual = actualDataRoom.value ?: return@launch
            var offlineUsersIds = (actual.members.toSet()-actual.activeUsers.toSet())- setOf(currentUsrUid)
            val roomDetail = getRoomDetail()
            Log.d(TAG,"Uris "+state.value.selectedMediaForRequest.toString())
            sendMessageByChatIdWithLoadingFlow(
                chatId = state.value.groupId,
                senderId = currentUsrUid,
                offlineUsersIds = offlineUsersIds.toList(),
                roomDetail = roomDetail,
                textMessage = state.value.textMessage,
                attachmentDraft = state.value.selectedMediaForRequest,
                prevUnreadCount = actualUnreadCount.value
            ).collect{

                result->

                when(result){
                    is Resource.Success -> {
                        Log.d(TAG,"Сообщение--Отправлено")
                        messageLoaded(messageId= "Sending")
                    }
                    is Resource.Error -> {
                       Log.d(TAG,"Сообщение--НЕ Отправлено ${result.message}")

                    }
                    is Resource.Loading -> {
                        val progress = result.data ?: return@collect
                        when(progress){
                            is UploadProgress.Failed ->{}
                            is UploadProgress.Success ->{
                                onAttachmentUploaded(progress= progress, messageId = "Sending")
                            }
                            is UploadProgress.Uploading ->{
                                updateProgressAttachment(progress= progress, messageId = "Sending")
                            }
                        }
                    }

                }
            }

        }
        _state.update {
            val oldList=it.listMessage.toMutableList()
            oldList.add(0,genMessageUi)
            it.copy(
                listMessage = oldList,
                selectedMediaForRequest = emptyList(),
                textMessage = ""
            )
        }
    }
    private fun genLoadingMessage():MessageListItemUI{
        val attachmentsUi=state.value.selectedMediaForRequest.mapIndexed { index, it,->
            it.uri.toString() to AttachmentUi(
                uri = it.uri,
                type = AttachmentTypeUI.IMAGE,
                progressValue = 0.1f,
                isLoading = true
            )
        }.toMap()
        return MessageListItemUI(
            messageId = "Sending",
            text = "",
            timestamp = LocalDateTime.now(),
            timeString = "3:07",
            fullName = "",
            isOwn = true,
            isChanged = false,
            type = MessageType.OnlyImage,
            status = MessageStatus.Loading,
            attachmentsUi = attachmentsUi
        )
    }
    private fun messageLoaded(messageId:String){
        _state.update {
            val newList = state.value.listMessage.map { message->
                if (message.messageId == messageId) {
                    val newUiMap = message.attachmentsUi!!.map {it.key to it.value.copy(isLoading = false) }.toMap()
                    message.copy(
                        attachmentsUi = newUiMap,
                        status = MessageStatus.Readed
                    )
                } else { message }
            }
            it.copy(listMessage = newList)
        }
    }
    private fun updateProgressAttachment(progress: UploadProgress.Uploading, messageId:String){
        _state.update {
            val newList = state.value.listMessage.map { message->
                val key= progress.uri.toString()
                if (message.messageId == messageId) {
                    val newUiMap = message.attachmentsUi.toMutableMap()
                    newUiMap[key]=newUiMap[key]!!.copy(
                        progressValue = progress.progress,
                        isLoading = progress.progress!= 1.0f
                    )
                    message.copy(attachmentsUi = newUiMap)
                } else { message }
            }
            it.copy(listMessage = newList)
        }
    }
    private fun  onAttachmentUploaded(progress: UploadProgress.Success, messageId:String){
        _state.update {
            val newList = state.value.listMessage.map { message->
                val key= progress.uri.toString()
                if (message.messageId == messageId) {
                    val newUiMap = message.attachmentsUi.orEmpty().toMutableMap()
                    newUiMap[key]=newUiMap[key]!!.copy(
                        isLoading = false,
                        uploadedUrl = progress.attachment.attachUrl
                    )
                    message.copy(attachmentsUi = newUiMap)
                } else { message }
            }
            it.copy(listMessage = newList)
        }
    }

   // @Deprecated("Не подходит под новый ТИП сообщения")
    private fun loadOldMessages(){
        viewModelScope.launch {
            if(lastLoadedIdMessage.isEmpty()){
                return@launch
            }
            repository.getBlockMessagesByChatId(
                state.value.groupId, lastMessageKey = lastLoadedIdMessage
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
           _state.update { it.copy(textMessage = "") }
                val time= measureTime {  val result=sendMessageUseCaseById.execute(
                    message = message,
                    chatId = chatId,
                    offlineUsersIds=offlineUsersIds.toList(),
                    prevUnreadCount =actualUnreadCount.value,
                    roomDetail=roomDetail
                )
                when (result){
                    //отобразить ошибку
                    is Resource.Error -> {}
                    is Resource.Loading ->{}
                    is Resource.Success -> {}
                }
            }
            Log.d(TAG,"Время отправки сообщения $time")

        }
    }

    fun selectActionWithMessage(){
        if (!state.value.textMessage.isEmpty() || !state.value.selectedMediaForRequest.isEmpty()){
            val newMessage = Message(senderId = currentUsrUid, text = state.value.textMessage,
              //  attachment = state.value.selectedUrisForRequest
            )
            testFun()
            return
            if (state.value.chatState is ChatState.Create){
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
                   selectedMediaForRequest = event.value,
                   bottomSheetVisibility = !state.value.bottomSheetVisibility
               )
           }

           is ConversationEvent.SendMessage -> {
               selectActionWithMessage()
           }
           is ConversationEvent.OnValueChangeTextMessage -> {_state.update { it.copy(textMessage = event.text) }}
           ConversationEvent.OffOptions -> {
               _state.value = state.value.copy(
                   optionsVisibility =false,
                   selectedMessages = emptyMap())
           }
           ConversationEvent.DeleteSelectedMessages ->{ deleteMessagesInChat() }
           ConversationEvent.LeftChat -> leftChat()
       }
    }


    @Deprecated("Удалят и наши и ваши сообщения")
    fun deleteMessagesInChat(){
        viewModelScope.launch {
            if(state.value.selectedMessages.isEmpty())
                return@launch
            val messagesId = state.value.selectedMessages.keys.toList()
            repository.deleteMessagesInChat(messagesId, chatId =state.value.groupId).collect{
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
}