package com.example.unmei.presentation.conversation_future.viewmodel

import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.R
import com.example.unmei.data.model.MessageResponse
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.model.Attachment
import com.example.unmei.domain.usecase.CreatePrivateChatUseCase
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.model.MessageListItemUI
import com.example.unmei.presentation.conversation_future.model.MessageType
import com.example.unmei.util.ConstansApp.MESAGES_SUMMERIES_DB
import com.example.unmei.util.ConstansApp.MESSAGES_REFERENCE_DB
import com.example.unmei.util.ConstansApp.ROOMS_REFERENCE_DB
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.whileSelect
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import javax.annotation.meta.When
import javax.inject.Inject

import kotlin.system.measureTimeMillis


@RequiresApi(35)
@HiltViewModel
class ConversationViewModel @Inject constructor(
  val remote:RemoteDataSource,
    val repository: MainRepository,
    val createPrivateChatUseCase: CreatePrivateChatUseCase
):ViewModel() {

    val _state = MutableStateFlow<ConversationVMState>(ConversationVMState())
    val state:StateFlow<ConversationVMState> = _state.asStateFlow()
    val refMessages = FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB).getReference(
        MESSAGES_REFERENCE_DB)
    private var lastLoadedTimestamp: Long = Long.MAX_VALUE
    private var lastLoadedIdMessage: String = ""
    private val currentUsrUid = Firebase.auth.currentUser!!.uid
    private val chatId="-OLURjaOA0bbkiWLD3jz"





    init {

        viewModelScope.launch {

          //  initFirstMassages(chatId)

           // Log.d(TAG,.toString())

//           val result =createPrivateChatUseCase.execute(
//                chatName = "РАзработка",
//                iconUrl = "https://firebasestorage.googleapis.com/v0/b/repository-d6c1a.appspot.com/o/TestImages%2FTohsaka.jpg?alt=media&token=b9c481c6-eb64-4525-a898-76586632761e",
//                membersIds = listOf("auegBKgwyvbwge7PQs9nfFRRFfj1","u1DDSWtIHOSpcHIkLZl0SZGEsmB3")
//            )
//            when(result){
//                is Resource.Error ->{Log.d(TAG,"Ошибка: "+result.message.toString())}
//                is Resource.Loading -> {}
//                is Resource.Success -> {Log.d(TAG,"Успех: "+result.data.toString())}
//            }
            observeMessages(chatId)



        }
    }



    private fun uploadFile(fileUri: Uri) {
        val storageRef = FirebaseStorage.getInstance(ConstansDev.YOUR_PATHFOLDER_STORAGE).getReference("TestImages");
        val fileRef = storageRef.child("${System.currentTimeMillis()}.jpg")
        val ref=fileRef.putFile(fileUri)
//            .addOnSuccessListener {
//                fileRef.downloadUrl.addOnSuccessListener { uri ->
//                    val downloadUrl = uri.toString()
//                    //Toast.makeText(this, "Файл загружен! URL: $downloadUrl", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .addOnFailureListener { e ->
//                // Toast.makeText(this, "Ошибка загрузки: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
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
   // @Deprecated("Не подходит под новый ТИП сообщения")
    suspend fun sendMessage(message: Message){
        val uidMessage= refMessages.push().key!!
        val msg= MessageResponse().fromMessageToResp(message).copy(
            id= uidMessage
        )
       val reference =FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB).reference
       val updates = mapOf(
           "${MESSAGES_REFERENCE_DB}/$chatId/$uidMessage" to msg,
           "$MESAGES_SUMMERIES_DB/$chatId/lastMessage" to msg
           //Удалить UID чата у каждого участника
       )
       reference.updateChildren(updates).await()
    }


    fun testGroped(){
        val groupedMessages =state.value.listMessage.groupingBy { it.timestamp }
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
               if (!event.text.isEmpty()){
                   viewModelScope.launch {
                       val item = Message(
                           "",
                           senderId = currentUsrUid,
                           text = event.text
                       )
                       sendMessage(item)
                   }
               }else{
                   Log.d(TAG,"Пустая строка")
               }
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

    fun saveNecessaryInfo(groupUid:String, companionUid:String){
        _state.value = state.value.copy(
            groupId = groupUid,
            companionId = companionUid
        )
    }


    private fun getCurrentCompanion(){

    }
}