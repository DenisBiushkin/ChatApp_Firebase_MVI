package com.example.unmei.domain.usecase.messages

import android.net.Uri
import android.util.Log
import com.example.unmei.domain.model.AttachmentDraft
import com.example.unmei.domain.model.TypeMessageResp
import com.example.unmei.domain.model.UploadProgress
import com.example.unmei.domain.model.messages.Attachment
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.RoomDetail
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.util.ConstansApp.STORAGE_ROOM_PHOTO_FOLDER
import com.example.unmei.util.ConstansApp.STORAGE_ROOM_REFERENCE
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class SendMessageByChatIdWithLoadingFlow(
    private val repository: MainRepository,
    private val notificationRepository: NotificationRepository
) {

    operator fun invoke(
        chatId:String,
        senderId:String,
        offlineUsersIds:List<String>,
        prevUnreadCount:Map<String,Int>,
        roomDetail: RoomDetail,
        textMessage:String?=null,
        attachmentDraft:List<AttachmentDraft> = emptyList(),
    ): Flow<Resource<UploadProgress>> = callbackFlow{

        val attachments= mutableListOf<Attachment>()

        val jobs = attachmentDraft.map { attachDraft ->
            val folder = STORAGE_ROOM_PHOTO_FOLDER
            async {
                repository.uploadAttachmentWithProgressRemote(
                    pathString = "$STORAGE_ROOM_REFERENCE/$chatId/$folder",
                    draft = attachDraft
                ).collect { uploadingProgress ->
                    when (uploadingProgress) {
                        is UploadProgress.Failed -> {
                            trySend(Resource.Error(message = "Ошибка загрузки Media"))
                            return@collect
                        }
                        is UploadProgress.Success -> {
                            trySend(Resource.Loading(uploadingProgress))
                            attachments.add(uploadingProgress.attachment)
                        }
                        is UploadProgress.Uploading -> {
                            trySend(Resource.Loading(uploadingProgress))
                        }
                    }
                }
            }
        }
        jobs.awaitAll()  // Теперь это будет работать корректно
        //если Uri есть а результатов загрузки нет
        if (attachments.isEmpty() && attachmentDraft.isNotEmpty()){
            trySend(Resource.Error(message ="Ошибка загрузки Media"))
            close()
        }
        val message = Message(
            senderId = senderId,
            text = textMessage,
            attachment = attachments,
            type = TypeMessageResp.ONLYPHOTO
        )
        val newMapUnread = offlineUsersIds.map {
            it to (prevUnreadCount[it]?.inc() ?: 1)
        }.toMap()

        val sendMessageResult =async {   repository.sendMessageAdv(message,chatId)}
        val summerisRusult =async { repository.updateUnreadCountInRoomSummaries(roomId = chatId, newUnreadCount = newMapUnread)}
        launch{  notificationRepository.notifySendMessageInRooms(roomDetail = roomDetail,message,offlineUsersIds)}
        if (
            sendMessageResult.await() is Resource.Success
            &&
            summerisRusult.await()
            ){
            trySend(Resource.Success(UploadProgress.Success(Uri.EMPTY,Attachment.Image(""))))
            close()
        }
        trySend(Resource.Error(message= "Случилось ГГ"))
        close()

    }
}