package com.example.unmei.presentation.conversation_future.viewmodel

import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.model.Attachment
import com.example.unmei.domain.usecase.messages.CreatePrivateChatUseCase
import com.example.unmei.domain.usecase.messages.SendMessageUseCaseById
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.conversation_future.model.ConversationContentState
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
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


@RequiresApi(35)
@HiltViewModel
class ConversationViewModel @Inject constructor(
  val remote:RemoteDataSource,
    val repository: MainRepository,
    val createPrivateChatUseCase: CreatePrivateChatUseCase,
    val sendMessageUseCaseById:  SendMessageUseCaseById
):ViewModel() {

    val _state = MutableStateFlow<ConversationVMState>(ConversationVMState())
    val state:StateFlow<ConversationVMState> = _state.asStateFlow()

    private var lastLoadedIdMessage: String = ""
    private val currentUsrUid = Firebase.auth.currentUser!!.uid
    private val chatId="-OLURjaOA0bbkiWLD3jz"

    private var conversationInitJob: Job? = null


    init {
        val ids= listOf(
            "-OLz7BinABjm_psVir_I",
            "-OLz8kC0LlCf6J0RAK86",
            "-OLzA_yLjxKTCTu_Tr0y",
            "-OLzDKz-R0BvRyLr_b5X",
            "-OLz1bAdRr97nMTu_cCz",
            "-OLz2_jrc8-8fp4LZUB1",

        )
        viewModelScope.launch {
          ids.forEach {
              remote.cascadeDeleteRoomsAdvence(it)
          }
        }

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
        _state.value = state.value.copy(
            chatFullName = conversationNavData.chatName,
            chatIconUrl = conversationNavData.chatUrl,
            companionId = conversationNavData.companionUid,
            contentState = ConversationContentState.Loading,
            chatExistence = conversationNavData.chatExist,
            groupId = conversationNavData.chatUid ?: ""
        )
        selectContentState()
    }


    private suspend fun initConversation(){
        _state.value = state.value.copy(
            contentState = ConversationContentState.Messaging,
            chatExistence = true
        )

        //getGroupById
        //
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
                contentState = ConversationContentState.EmptyType
            )
        }
    }

    private fun createNewChat(message:Message){
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
        viewModelScope.launch {
            val result=sendMessageUseCaseById.execute(message,chatId)
            when (result){
                is Resource.Error -> {

                }
                is Resource.Loading ->{

                }
                is Resource.Success -> {

                }
            }
        }
    }
    fun selectActionWithMessage(text:String){
        if (!text.isEmpty() || !state.value.selectedUrisForRequest.isEmpty()){
            //Log.d(TAG,"Попали в условие  ${state.value.chatExistence}")
            val newMessage = Message(
                senderId = currentUsrUid,
                text = text,
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
               selectActionWithMessage(event.text)
           }
           ConversationEvent.Offoptions -> {
               _state.value = state.value.copy(
                   optionsVisibility =false,
                   selectedMessages = emptyMap()
               )
           }

           ConversationEvent.DeleteSelectedMessages ->{
               deleteMessagesInChat()
           }
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