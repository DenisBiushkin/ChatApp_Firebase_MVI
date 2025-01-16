package com.example.unmei.presentation.messanger_feature.viewmodel

import androidx.lifecycle.ViewModel
import com.example.unmei.util.ConstansDev
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(

):ViewModel() {


    val db= FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
    val groupsRef = ""
    val refGroups= db.getReference("groups")
    val refMessages=db.getReference("messages")


    enum class GroupType(
        val type:String
    ){
        PRIVATE("private"),
        PUBLIC("public")
    }
    data class Group(
        val id :String ="",//id группы == узлу ген Firebase
        val type: String="",//приватный или чатсный
        val timestamp:Map<String,String> = emptyMap(),//время создания
        val moderators: Map<String,Boolean> = emptyMap(),//для группового чата()
        val members:Map<String,Boolean> = emptyMap()
    )
    data class User(
        val uid:String="",//
        val fullname:String="",//надо
        val userName:String="",//==uid потом можно будет изменить
        val phoneNumber:String="",//надо
        val status:String,//online/ofline/
        val email:String="",//хз
        val age:String="",//хз
        val photoBase64:String="",//надо
        val groups:Map<String,String> = emptyMap()//надо
    )

    data class Message(
        val id:String="",
        val senderId: String = "",//отправитель
        val text: String = "",
        val timestamp:Map<String,String> = emptyMap(),//время создания
        val type: String ="",//image,file,text,audio
        val readed: Boolean = false,//отправлено, прочитано
        val mediaUrl: String = "",
        val edited: Boolean = false,//изменено
        )


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
            Group(
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