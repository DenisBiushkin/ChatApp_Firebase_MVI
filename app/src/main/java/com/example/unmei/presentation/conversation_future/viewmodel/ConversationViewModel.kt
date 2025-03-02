package com.example.unmei.presentation.conversation_future.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unmei.data.model.MessageResponse
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Message
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.util.ConstansApp.MESSAGES_REFERENCE_DB
import com.example.unmei.util.ConstansApp.ROOMS_REFERENCE_DB
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class ConversationViewModel @Inject constructor(
  val remote:RemoteDataSource
):ViewModel() {

    val _state = MutableStateFlow<ConversationVMState>(ConversationVMState())
    val state:StateFlow<ConversationVMState> = _state.asStateFlow()

    val refMessages = FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB).getReference(
        MESSAGES_REFERENCE_DB)


    init {
        val chatId="-OGfKvopKTMOCp_bxJqe"


        viewModelScope.launch {
            val uidMessage= refMessages.push().key
            val message = MessageResponse(
                id = uidMessage!!,
                senderId = "auegBKgwyvbwge7PQs9nfFRRFfj1",
                text="11 сообщение",
                timestamp = ServerValue.TIMESTAMP,
                type = "text",
                readed = true,
                mediaUrl = "url",
                edited = false
            )
//            for (i in 1..10){
//                //val uidMessage= refMessages.push().key
//
//
//              // refMessages.child(chatId).child(uidMessage).setValue(message).await()
//                delay(200)
//            }
            remote.observeMessageInChat(chatId).collect{
                Log.d(TAG,it.toString())
            }
         //   refMessages.child(chatId).child(uidMessage).setValue(message).await()
        }
    }
    fun onEvent(){

    }

    private fun getCurrentCompanion(){

    }
}