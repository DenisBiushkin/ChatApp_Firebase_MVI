package com.example.unmei.presentation.groupChat_feature.viemodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.model.TypingStatus
import com.example.unmei.domain.model.UploadProgress
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.RoomDetail
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.usecase.messages.EnterChatUseCase
import com.example.unmei.domain.usecase.messages.LeftChatUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomAdvanceUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomSummariesUseCase
import com.example.unmei.domain.usecase.messages.SendMessageByChatIdWithLoadingFlow
import com.example.unmei.domain.usecase.messages.SetTypingStatusUseCase
import com.example.unmei.domain.usecase.user.GetUsersWithStatusUseCase
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.groupChat_feature.model.GroupChatVMState
import com.example.unmei.presentation.groupChat_feature.model.InitChatResult
import com.example.unmei.presentation.singleChat_feature.model.AttachmentTypeUI
import com.example.unmei.presentation.singleChat_feature.model.AttachmentUi
import com.example.unmei.presentation.singleChat_feature.model.ContentStateScreen
import com.example.unmei.presentation.singleChat_feature.model.ConversationEvent
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.singleChat_feature.model.MessageType
import com.example.unmei.presentation.util.pagging.MessagePaginator
import com.example.unmei.util.ChatSessionManager
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.example.unmei.util.timestampToStringHourMinute
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val repository: MainRepository,
    private val observeRoomSummariesUseCase: ObserveRoomSummariesUseCase,
    private val observeChatRoomAdvanceUseCase: ObserveChatRoomAdvanceUseCase,
    private val getUsersWithStatusUseCase: GetUsersWithStatusUseCase,
    private val setTypingStatusUseCase: SetTypingStatusUseCase,
    private val sendMessageByChatIdWithLoadingFlow: SendMessageByChatIdWithLoadingFlow,
    private val enterChatUseCase: EnterChatUseCase,
    private val leftChatUseCase: LeftChatUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val chatSessionManager: ChatSessionManager,
):ViewModel() {

    private val _state = MutableStateFlow<GroupChatVMState>(GroupChatVMState())
    val state: StateFlow<GroupChatVMState> = _state.asStateFlow()

    private val actualDataRoom = MutableStateFlow<ChatRoomAdvance?>(null)
    private val actualUnreadCount = MutableStateFlow<Map<String,Int>>(emptyMap())

    private var updateActualUserJob : Job?= null
    private val countLoadOldMessages = 20

    private val sendingJobs = mutableMapOf<Job,String>()

    private val messagePaginator = MessagePaginator<String, Message>(
        initKey = state.value.lastMessageId,
        onLoadUpdated = {
            onLoad->
            _state.update {chatState->
            chatState.copy(
                loadingOldMessages = onLoad
            )
        }},
        onRequest = {
            nextLastMessageId->
            Log.d(TAG,"onRequest Id${state.value.chatId}")
            remoteDataSource.getPageMessagesByChatId(
                count = countLoadOldMessages,
                chatId = state.value.chatId,
                lastMessageKey = nextLastMessageId)
        },
        getNextKey = {
            newMessagesList->
            if (newMessagesList.isEmpty())
                return@MessagePaginator ""
            Log.d(TAG,"Last message"+newMessagesList.first())
           newMessagesList.first().id

        },
        onError = {
           Log.d(TAG,"Ошибка пагинации")
        },
        onSuccess = {
            items, newKey ->
            _state.update {
                val newMessagesUi=items.map { it.toMessageListItemUI(owUid = state.value.currentUsrUid, mapUsers = state.value.actualUserExtendedMap) }.toMutableList()
                newMessagesUi.reverse()
                addMessages(newMessagesUi)
                it.copy(
                  //  listMessage =newMessagesUi.plus(state.value.listMessage),
                    onReached = items.size < countLoadOldMessages,
                    lastMessageId = newKey,

                )

            }

        }
    )

    init {
        getNavArguments()
    }

    private fun observeStatusGroupChat(){
        viewModelScope.launch {
            observeRoomSummariesUseCase.execute(state.value.chatId).collect{
                actualUnreadCount.value = it.unreadedCount
                if (!it.typingUsersStatus.isEmpty()){
                    _state.value=state.value.copy(
                        isTyping = true,
                        chatStatus = "участник печататает"
                    )
                }else{
                    val countMembers = state.value.members.size
                    _state.value=state.value.copy(
                        isTyping = false,
                        chatStatus =pluralizeParticipants(countMembers)
                    )
                }
            }
        }
    }
    fun pluralizeParticipants(count: Int): String {
        val word = when {
            count % 10 == 1 && count % 100 != 11 -> "участник"
            count % 10 in 2..4 && (count % 100 !in 12..14) -> "участника"
            else -> "участников"
        }
        return "$count $word"
    }
    private fun getNavArguments(){
        val navChatId = savedStateHandle.get<String>(ConstansApp.GROUPCHAT_ARGUMENT_CHATID)
        val currentUserId =  Firebase.auth.currentUser?.uid

        if((navChatId==null) && (currentUserId==null)){
            Log.d(TAG,"GroupChatID: ERROR")
            //ошибка загрузки чата
            return
        }
        Log.d(TAG,"GroupChatID: $navChatId")
        _state.update {it.copy(
            chatId = navChatId!!,
            currentUsrUid = currentUserId!!
        )
        }
        initChatObserving()
    }

    private suspend fun primaryInitChat(): InitChatResult = coroutineScope {
        return@coroutineScope runCatching {
            val chatId = state.value.chatId
            val userId = state.value.currentUsrUid

            val groupDataDeferred = async { remoteDataSource.getChatRoomById(chatId) }
            val enterChatDeferred = async { enterChatUseCase.execute(chatId, userId) }

            val groupDataResult = groupDataDeferred.await()
            enterChatDeferred.await()

            if (groupDataResult == null) {
                return@runCatching InitChatResult.Failure("Данные чата не найдены")
            }

            val userDataDeferred=async { getUsersWithStatusUseCase(groupDataResult.members.toList()) }
            val userData = userDataDeferred.await()
            if (userData.isEmpty()){
                return@runCatching InitChatResult.Failure("Данные пользователей не найдены")
            }

            _state.update {
                it.copy(
                    chatName = groupDataResult.chatName,
                    chatIconUrl = groupDataResult.iconUrl,
                    members = groupDataResult.members,
                    moderators = groupDataResult.moderators,
                    timestamp = groupDataResult.timestamp,
                    actualUserExtendedMap = userData.associateBy { it.user.uid }
                )
            }

            InitChatResult.Success
        }.getOrElse { e ->
            e.printStackTrace()
            InitChatResult.Failure("Ошибка: ${e.message}")
        }
    }

    private fun removeMessagesByIds(idsToRemove: Set<String>) {
        _state.update { currentState ->
            val newGrouped = mutableMapOf<LocalDate, List<MessageListItemUI>>()
            val newIdIndex = currentState.idIndex.toMutableMap()

            for ((date, messages) in currentState.grouped) {

                val filtered = messages.filterNot { idsToRemove.contains(it.messageId) }
                if (filtered.isNotEmpty()) {
                    newGrouped[date] = filtered
                }
            }

            idsToRemove.forEach { newIdIndex.remove(it) }

            currentState.copy(
                grouped = newGrouped,
                idIndex = newIdIndex
            )
        }
    }

    fun loadNextMessages(){
        viewModelScope.launch {
            messagePaginator.loadNextItems()
        }
    }

    private fun initChatObserving() {
        viewModelScope.launch {
            _state.update { it.copy(contentState = ContentStateScreen.Loading) }
            when (val result = primaryInitChat()) {
                is InitChatResult.Success -> {
                    enterChat(state.value.chatId,state.value.currentUsrUid)
                    loadNextMessages()
                    observeActualDataRoom(state.value.chatId)
                    observeStatusGroupChat()
                    observeTypingFieldStatus()
                    observeMessages()
                    _state.update { it.copy(contentState = ContentStateScreen.Content) }
                }
                is InitChatResult.Failure -> {
                    _state.update {
                        Log.d(TAG,"${result.reason.toString()}")
                        it.copy(contentState = ContentStateScreen.Error(result.reason))
                    }
                }
            }
        }
    }



    private fun observeActualDataRoom(chatId: String){
        observeChatRoomAdvanceUseCase.execute(chatId).onEach {
            chatRoom -> actualDataRoom.value =chatRoom
            updateRoomDataInState(chatRoom)
            UpdateActualUserExtended()
        }.launchIn(viewModelScope)
    }
    private fun UpdateActualUserExtended(){
        updateActualUserJob?.cancel()
        updateActualUserJob=viewModelScope.launch {
            val result=getUsersWithStatusUseCase(state.value.members.toList()).map {
                it.user.uid to it
            }.toMap()
            _state.update {
                it.copy(
                    actualUserExtendedMap = result
                )
            }
        }

    }
    private fun updateRoomDataInState(chatRoom:ChatRoomAdvance){
        _state.update {
            it.copy(
                chatName = chatRoom.chatName,
                chatIconUrl = chatRoom.iconUrl,
                members = chatRoom.members,
                moderators = chatRoom.moderators,
                timestamp = chatRoom.timestamp
            )
        }
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
    @OptIn(FlowPreview::class)
    private fun observeTypingFieldStatus() {
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
                            groupId = state.value.chatId,
                            userId =state.value. currentUsrUid,
                            status = newStatus
                        )
                        lastStatus = newStatus
                    }
                }
        }
    }
    private fun genTemporaryKey():String{
        return "Sending_${System.currentTimeMillis()}"
    }
    private fun getRoomDetail(): RoomDetail {
        return  RoomDetail(
            roomId =state.value.chatId,
            roomIconUrl =state.value.chatIconUrl,
            roomName = state.value.chatName,
            typeRoom = TypeRoom.PUBLIC
        )
    }
    private fun sendMessageWithLoadingFlow(){
        val temporaryMsgId = genTemporaryKey()
        val genMessageUi=genLoadingMessage(temporaryId =temporaryMsgId )
        val actual = actualDataRoom.value ?: return
        val offlineUsersIds = (actual.members.toSet()-actual.activeUsers.toSet())- setOf(state.value.currentUsrUid)
        val roomDetail = getRoomDetail()

        val currentJob=viewModelScope.launch {
            sendMessageByChatIdWithLoadingFlow(
                chatId = state.value.chatId,
                senderId = state.value.currentUsrUid,
                offlineUsersIds = offlineUsersIds.toList(),
                roomDetail = roomDetail,
                textMessage = state.value.textMessage,
                attachmentDraft = state.value.selectedMediaForRequest,
                prevUnreadCount = actualUnreadCount.value
            ).collect{
                result->
                val job = coroutineContext[Job]
                val loadingMsgId = sendingJobs[job]?: ""
                when(result){
                    is Resource.Success -> {
                        messageLoaded(messageId= loadingMsgId)
                        sendingJobs.remove(job)
                    }
                    is Resource.Error -> {
                        //сделать отметку что удалить
                        messageLoaded(messageId= loadingMsgId)
                        sendingJobs.remove(job)
                        Log.d(TAG,"Сообщение--НЕ Отправлено ${result.message}")
                    }
                    is Resource.Loading -> {
                        val progress = result.data ?: return@collect
                        when(progress){
                            is UploadProgress.Failed ->{}
                            is UploadProgress.Success ->{ onAttachmentUploaded(progress= progress, messageId = loadingMsgId) }
                            is UploadProgress.Uploading ->{ updateProgressAttachmentMessage(progress= progress, messageId = loadingMsgId) }
                        }
                    }
                }
            }
        }
        sendingJobs.put(key=currentJob, value = temporaryMsgId)
        _state.update {
            addMessages(listOf(genMessageUi))
            it.copy(
                selectedMediaForRequest = emptyList(),
                textMessage = ""
            )
        }
    }
    private fun messageLoaded(messageId:String){
        if(messageId.isEmpty())
            return
        val allMessages = state.value.grouped.values.flatten()
            .filterNot{ message-> message.messageId == messageId }
        val newMap = allMessages
            .groupBy { it.timestamp.toLocalDate() }
            .mapValues { (_, list) -> list.sortedByDescending { it.timestamp } }
        _state.update {
            it.copy(grouped =  newMap )
        }
    }
    private fun updateProgressAttachmentMessage(progress: UploadProgress.Uploading, messageId:String){
        if (messageId.isBlank())
            return
        val allMessages = state.value.grouped.values.flatten()
            .map{
                message->
                if (message.messageId == messageId){
                    val key= progress.uri.toString()
                    val newUiMap = message.attachmentsUi.toMutableMap()
                    newUiMap[key]=newUiMap[key]!!.copy(
                        progressValue = progress.progress,
                        isLoading = progress.progress!= 1.0f
                    )
                    message.copy(
                        attachmentsUi = newUiMap,
                        timestamp = LocalDateTime.now()
                    )
                }
                else message
            }
        val newMap = allMessages
            .groupBy { it.timestamp.toLocalDate() }
            .mapValues { (_, list) -> list.sortedByDescending { it.timestamp } }
        _state.update {
            it.copy(grouped =  newMap )
        }
    }
    private fun  onAttachmentUploaded(progress: UploadProgress.Success, messageId:String){
        if (messageId.isBlank())
            return
        val allMessages = state.value.grouped.values.flatten()
            .map { message->
                if (message.messageId == messageId){
                    val key= progress.uri.toString()
                    val newUiMap = message.attachmentsUi.toMutableMap()
                    newUiMap[key]=newUiMap[key]!!.copy(
                        isLoading = false,
                        uploadedUrl = progress.attachment.attachUrl
                    )
                    message.copy(
                        attachmentsUi = newUiMap,
                        timestamp = LocalDateTime.now()
                    )
                }
                else message
            }
        val newMap = allMessages
            .groupBy { it.timestamp.toLocalDate() }
            .mapValues { (_, list) -> list.sortedByDescending { it.timestamp } }
        _state.update {
            it.copy(grouped =  newMap )
        }
    }
    private fun addMessages(newMessages: List<MessageListItemUI>) {
        _state.update { currentState ->
            val newGrouped = currentState.grouped.toMutableMap()
            var newIdIndex = currentState.idIndex.toMutableMap()

            for (msg in newMessages) {
                val date = msg.timestamp.toLocalDate()

                // Получаем существующий список и заменяем/добавляем сообщение
                val updatedList = (newGrouped[date].orEmpty()
                    .filterNot { it.messageId == msg.messageId } + msg
                        )
                    .sortedByDescending { it.timestamp } // ← Сортировка по времени

                newGrouped[date] = updatedList
                newIdIndex[msg.messageId] = msg
            }

            currentState.copy(
                grouped = newGrouped,
                idIndex = newIdIndex
            )
        }
    }

    private fun genLoadingMessage(temporaryId:String): MessageListItemUI {
        val attachmentsUi=state.value.selectedMediaForRequest.mapIndexed { index, it,->
            it.uri.toString() to AttachmentUi(
                uri = it.uri,
                type = AttachmentTypeUI.IMAGE,
                progressValue = 0.1f,
                isLoading = true
            )
        }.toMap()

        return MessageListItemUI(
            messageId =temporaryId,
            text = "",
            timestamp = LocalDateTime.now(),
            timeString = timestampToStringHourMinute(LocalDateTime.now().nano.toLong()),
            fullName = state.value.actualUserExtendedMap?.get(state.value.currentUsrUid)?.user?.fullName?:"Undefined",
            isOwn = true,
            isChanged = false,
            type = MessageType.OnlyImage,
            status = MessageStatus.Loading,
            attachmentsUi = attachmentsUi
        )
    }

    private fun observeMessages(){
        viewModelScope.launch {
            repository.observeMessagesInChat(
                chatId= state.value.chatId,

            ).collect{
                when(it){
                    is ExtendedResource.Added -> {
                        it.data?.let{
                            if (!state.value.idIndex.keys.contains(it.id)){
                                val item=it.toMessageListItemUI(
                                    owUid = state.value.currentUsrUid,
                                    mapUsers =state.value.actualUserExtendedMap
                                )
                                addMessages(listOf(item))
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
    fun onEvent(event: ConversationEvent){
        when(event){
            ConversationEvent.LoadingNewMessage -> {
             //   loadOldMessages()
            }
            is ConversationEvent.ChangeSelectedMessages -> {
              //onChangeSelectedMessages(event.id)
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
                sendMessageWithLoadingFlow()
            }
            is ConversationEvent.OnValueChangeTextMessage -> {_state.update { it.copy(textMessage = event.text) }}
            ConversationEvent.OffOptions -> {
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