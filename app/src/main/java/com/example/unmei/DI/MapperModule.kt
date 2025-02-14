package com.example.unmei.DI

import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.domain.mapper.ChatRoomMapper
import com.example.unmei.domain.model.ChatRoom
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.example.Mappers.base.Mapper


@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {//abstarct потому что Binds работает только abstract fun

    //Binds ипользуется когда у нас уже есть реализация интерфейса
    //и нам нужно просто указать Hilt что за класс
    @Binds
    abstract fun bindChatRoomMapper(
        mapper: ChatRoomMapper
    ):Mapper<ChatRoomResponse,ChatRoom>
}