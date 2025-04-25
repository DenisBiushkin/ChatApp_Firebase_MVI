package com.example.unmei.domain.usecase.user

import androidx.compose.ui.text.toLowerCase
import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.repository.MainRepository
import java.util.Locale

class GetUsersExByFullName(
    private val mainRepository: MainRepository
) {
    suspend fun execute(fullName:String):List<UserExtended>{
       val normalizeFullName = fullName.replace(" ","").lowercase()
        val ids=mainRepository.getUsersIdsByFullName(normalizeFullName)
        if (ids.isEmpty())
            return emptyList()
        return mainRepository.getUsersWithStatus(ids)?: emptyList()
    }
}