package com.example.unmei.domain.usecase.user

import android.net.Uri
import com.example.unmei.domain.model.AttachmentDraft
import com.example.unmei.domain.model.UploadProgress
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.ConstansApp.STORAGE_USERS_PROFILES_REFERENCE
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.Resource
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UpdateProfilePhotoUseCase(
    private val mainRepository: MainRepository
){

    suspend operator fun invoke(
        userId: String,
        uri: Uri
    ): Resource<Unit> {
        val pathString = STORAGE_USERS_PROFILES_REFERENCE + "/" + userId
        var result: Resource<Unit> = Resource.Loading() // начальное состояние

        mainRepository.uploadAttachmentWithProgressRemote(
            pathString = pathString,
            draft = AttachmentDraft(
                uri = uri,
                mimeType = "photo/"
            )
        ).collect { progress ->
            result = when (progress) {
                is UploadProgress.Failed -> Resource.Error(message = progress.exception.toString())
                is UploadProgress.Success ->{

                    val resUpdateUrl=changePhotoUser(
                        userId=userId, newUrl = progress.attachment.attachUrl
                    )
                    if (resUpdateUrl){
                        Resource.Success(Unit)
                    }else{
                        Resource.Error(message = "Не удалось обновить фото")
                    }

                }
                is UploadProgress.Uploading -> Resource.Loading()
            }
        }

        return result
    }
    //перенести потом не другие слои
    private suspend fun changePhotoUser(
        userId: String,
        newUrl:String
    ):Boolean{
        try {
            val reference = FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
            val updates = mapOf(
                "$USERS_REFERENCE_DB/$userId/photo" to newUrl
            )
            reference.reference.updateChildren(updates).await()
            return true
        }catch (e:Exception){
            return false
        }
    }
}