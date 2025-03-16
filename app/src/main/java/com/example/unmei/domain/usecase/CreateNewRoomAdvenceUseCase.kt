package com.example.unmei.domain.usecase

import android.net.Uri
import com.example.unmei.domain.model.NewRoomModel
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import javax.inject.Inject


@Deprecated("НЕ ИСПОЛЬЗОВАТЬ")
class CreateNewRoomAdvenceUseCase (
    private val repository: MainRepository
) {

    suspend fun execute(
        newRoomModel: NewRoomModel
    ):Resource<String>{
        if (newRoomModel.chatName.isEmpty())
            return Resource.Error(message = "Имя чата не должно быть пустым")
        if (newRoomModel.membersIds.size<2)
            return Resource.Error(message = "В чате не может быть меньше 2 человек")
        when(newRoomModel.type){
            TypeRoom.PRIVATE -> {
               if (newRoomModel. iconUrl.isEmpty() && newRoomModel.standartUrlIcon.isEmpty())
                   return Resource.Error(message = "Хоть какая иконка должна быть!!")
            }
            TypeRoom.PUBLIC -> {
                if (newRoomModel.moderatorsIds.size<1)
                    return Resource.Error(message = "Должно быть не менее 1 модератора в группе")
                if (newRoomModel.iconUri == Uri.EMPTY && newRoomModel.standartUrlIcon.isEmpty())
                    return  Resource.Error(message = "Хоть какая иконка должна быть!!")
            }
        }
        return  repository.createNewChatAdvence(newRoomModel)
    }
}