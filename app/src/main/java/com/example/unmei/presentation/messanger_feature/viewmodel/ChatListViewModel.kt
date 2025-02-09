package com.example.unmei.presentation.messanger_feature.viewmodel

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.GroupType
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.ObserveChatRoomUseCase
import com.example.unmei.domain.usecase.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.ObserveUserUseCase
import com.example.unmei.presentation.messanger_feature.model.ChatVMState
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatListViewModel @Inject constructor(
    val  observeUserUseCase: ObserveUserUseCase,
    val observeRoomsUserUseCase: ObserveRoomsUserUseCase,
    val observeChatRoomUseCase: ObserveChatRoomUseCase
):ViewModel() {
    private  val _state = MutableStateFlow<ChatVMState>(ChatVMState())
    val state: StateFlow<ChatVMState> = _state.asStateFlow()




    val db= FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
    val groupsRef = ""
    val refGroups= db.getReference("groups")
    val refMessages=db.getReference("messages")

    private val currentUserId ="u1DDSWtIHOSpcHIkLZl0SZGEsmB3";
    private val chatRoomId =observeRoomsUserUseCase
        .execute(currentUserId)
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf(""))

     init {

//         chatRoomId.map {
//             roomIdList->
//             roomIdList.map {
//                 roomId->
//                 observeChatRoomUseCase.execute(roomId).flatMapLatest {
//                     it
//                 }
//             }
//
//         }


     }
     private fun observeChatRooms(){

     }




    fun observeUser(userid:String = "auegBKgwyvbwge7PQs9nfFRRFfj1"){
        viewModelScope.launch {
            observeUserUseCase.execute(userid).collect{
                    user->
                _state.value = state.value.copy(
                    isOnline = user.online
                )
                    //Log.d(TAG,user.online.toString())
              //  Log.d(TAG,user.rooms.toString())

            }
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
       // val uid = refGroups.push().key
        //у каждой группы свой uid дублируется дублируется в id
        refGroups.child(uid!!).setValue(
            ChatRoom(
                id= uid,
                type= GroupType.PRIVATE.type,
                timestamp =ServerValue.TIMESTAMP,
                members = mapOf(
                    "userId1" to true,
                    "userId1" to true,
                )
            )
        )
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


