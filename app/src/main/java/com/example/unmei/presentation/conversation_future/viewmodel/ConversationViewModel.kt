package com.example.unmei.presentation.conversation_future.viewmodel

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.data.model.MessageResponse
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.model.Attachment
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.model.MessageListItemUI
import com.example.unmei.presentation.conversation_future.model.MessageType
import com.example.unmei.util.ConstansApp.MESSAGES_REFERENCE_DB
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject



@RequiresApi(35)
@HiltViewModel
class ConversationViewModel @Inject constructor(
  val remote:RemoteDataSource,
    val repository: MainRepository
):ViewModel() {

    val _state = MutableStateFlow<ConversationVMState>(ConversationVMState())
    val state:StateFlow<ConversationVMState> = _state.asStateFlow()

    val refMessages = FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB).getReference(
        MESSAGES_REFERENCE_DB)

    private var lastLoadedTimestamp: Long = Long.MAX_VALUE
    private var lastLoadedIdMessage: String = ""

    private val currentUsrUid = Firebase.auth.currentUser!!.uid

    private val chatId="-OGfKvopKTMOCp_bxJqe"



    init {

        viewModelScope.launch {

            initFirstMassages(chatId)

            listenForNewMessages(chatId).collect{
                val data= it.toMessageListItemUI(currentUsrUid)
                Log.d(TAG,"ChildEVENT ${data.text}")
                //сравниваем последнее текущее сообщение и полученное
                if (data !=state.value.listMessage.first()){
                    val currentList = state.value.listMessage.toMutableList()
                    currentList.add(0, data)

                    _state.value= state.value.copy(
                        listMessage = currentList
                    )
                }

            }

        }
    }




    private fun onChangeSelectedMessages(
        messageId:String
    ){
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



    fun testFun(){
        val item = MessageListItemUI(
            text = "",
            timestamp = 19380313,
            timeString = "3:07",
            fullName = "",
            isOwn = true,
            isChanged = true,
            type = MessageType.Image(""),
            attachments = listOf(Attachment.Image(state.value.selectedUrisForRequest.first().toString()))
        )
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
                            lastLoadedTimestamp = uiMessages.first().timestamp
                            lastLoadedIdMessage = uiMessages.first().messageId
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
                refMessages.child(chatId).child(uidMessage).
                setValue(MessageResponse().fromMessageToResp(message).copy(
                    id= uidMessage
                )).await()

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
                        val uiMessages = responseMessages.map {
                                it.toMessageListItemUI(currentUsrUid)
                                }
                            .asReversed()
                     //   val uiMessageReversed = uiMessages.reversed()

                        Log.d(TAG,"first: ${uiMessages.first().timestamp} last: ${uiMessages.last().timestamp}")
                        lastLoadedTimestamp = uiMessages.first().timestamp
                        lastLoadedIdMessage = uiMessages.first().messageId

                        _state.value = state.value.copy(
                            listMessage = uiMessages,
                            loadingScreen = false
                        )

                    }
                }
            }

        }
    }
    private fun listenForNewMessages(roomId: String):Flow<Message> = callbackFlow{
        var index = 0
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newMessage = snapshot.getValue(MessageResponse::class.java)
                newMessage?.let {
                    index+=1
                    Log.d(TAG,"Обртка с ChildEvent: $index timestampItem: ${it.timestamp}")

                    trySend(it.toMessage())

                    // lastLoadedTimestamp = it.timestamp
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }
       val ref= refMessages.child(roomId).limitToLast(1)//только новые
            // .orderByChild("timestamp")
            //  .startAt(lastLoadedTimestamp)
            ref.addChildEventListener(listener)
        awaitClose{
            ref.removeEventListener(listener)
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