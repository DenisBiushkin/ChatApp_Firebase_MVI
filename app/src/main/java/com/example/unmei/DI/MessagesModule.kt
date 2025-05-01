package com.example.unmei.DI

import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.domain.usecase.CreateNewRoomAdvenceUseCase
import com.example.unmei.domain.usecase.messages.CreatePrivateChatUseCase
import com.example.unmei.domain.usecase.messages.CreatePublicChatUseCase
import com.example.unmei.domain.usecase.messages.EnterChatUseCase
import com.example.unmei.domain.usecase.messages.LeftChatUseCase
import com.example.unmei.domain.usecase.messages.NotifySendMessageUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomAdvanceUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatsByUserIdUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomSummariesUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.messages.SendMessageByChatIdWithLoadingFlow
import com.example.unmei.domain.usecase.messages.SendMessageUseCaseById
import com.example.unmei.domain.usecase.messages.SetTypingStatusUseCase
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
    fun provideObserveChatRoomUseCase(mainRepository: MainRepository): ObserveChatRoomAdvanceUseCase {
        return ObserveChatRoomAdvanceUseCase(mainRepository)
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
    fun provideSendMessageUseCaseById(
        mainRepository: MainRepository,
        notificationRepository: NotificationRepository
    ): SendMessageUseCaseById {
        return  SendMessageUseCaseById(mainRepository,notificationRepository)
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
    @Provides
    fun provideLeftChatUseCase(mainRepository: MainRepository): LeftChatUseCase {
        return LeftChatUseCase(mainRepository)
    }
    @Provides
    fun provideEnterChatUseCase(mainRepository: MainRepository):EnterChatUseCase{
        return EnterChatUseCase(mainRepository)
    }

    @Provides
    fun provideObserveChatsByUserId(mainRepository: MainRepository,remoteDataSource: RemoteDataSource):ObserveChatsByUserIdUseCase{
        return ObserveChatsByUserIdUseCase(mainRepository,remoteDataSource)
    }

    @Provides
    fun provideSetTypingStatusUseCase(mainRepository: MainRepository):SetTypingStatusUseCase{
        return SetTypingStatusUseCase(mainRepository)
    }

    @Provides
    fun provideCreatePublicChatUseCase(
        mainRepository: MainRepository,
        notificationRepository: NotificationRepository
    ):CreatePublicChatUseCase{
        return CreatePublicChatUseCase(mainRepository,notificationRepository)
    }
    @Provides
    fun provideSendMessageByChatIdWithLoadingFlow(
        mainRepository: MainRepository,
        notificationRepository: NotificationRepository
    ): SendMessageByChatIdWithLoadingFlow {
        return SendMessageByChatIdWithLoadingFlow(
            mainRepository,notificationRepository
        )
    }

}