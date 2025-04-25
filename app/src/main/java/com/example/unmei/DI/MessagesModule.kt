package com.example.unmei.DI

import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.domain.usecase.CreateNewRoomAdvenceUseCase
import com.example.unmei.domain.usecase.messages.CreatePrivateChatUseCase
import com.example.unmei.domain.usecase.messages.NotifySendMessageUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomSummariesUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.messages.SendMessageUseCaseById
import com.example.unmei.domain.usecase.user.ObserveUserStatusByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object MessagesModule {

    @Provides
    fun provideObserveChatItemUseCase(mainRepository: MainRepository): ObserveRoomsUserUseCase {
        return ObserveRoomsUserUseCase(mainRepository)
    }

    @Provides
    fun provideObserveChatRoomUseCase(mainRepository: MainRepository): ObserveChatRoomUseCase {
        return ObserveChatRoomUseCase(mainRepository)
    }

    @Provides
    fun provideObserveUserStatusByIdUseCase(mainRepository: MainRepository): ObserveUserStatusByIdUseCase {
        return  ObserveUserStatusByIdUseCase(mainRepository)
    }

    @Provides
    fun provideCreateNewRoomAdvenceUseCase(mainRepository: MainRepository): CreateNewRoomAdvenceUseCase {
        return  CreateNewRoomAdvenceUseCase(mainRepository)
    }

    @Provides
    fun provideCreatePrivateChatUseCase(mainRepository: MainRepository): CreatePrivateChatUseCase {
        return  CreatePrivateChatUseCase(mainRepository)
    }

    @Provides
    fun provideSendMessageUseCaseById(mainRepository: MainRepository): SendMessageUseCaseById {
        return  SendMessageUseCaseById(mainRepository)
    }

    @Provides
    fun provideNotifySendMessageUseCase(
        notificationRepository: NotificationRepository
    ): NotifySendMessageUseCase {
        return NotifySendMessageUseCase(notificationRepository)
    }

    @Provides
    fun provideObserveRoomSummariesUseCase(mainRepository: MainRepository): ObserveRoomSummariesUseCase {
        return ObserveRoomSummariesUseCase(mainRepository)
    }

}