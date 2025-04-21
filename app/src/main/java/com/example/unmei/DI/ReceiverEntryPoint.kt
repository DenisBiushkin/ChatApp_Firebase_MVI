package com.example.unmei.DI

import com.example.unmei.android_frameworks.notification.MessageNotificationHelper
import com.example.unmei.domain.usecase.messages.NotifySendMessageUseCase
import com.example.unmei.domain.usecase.messages.SendMessageUseCaseById
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReceiverEntryPoint {
    fun provideMessageNotificationHelper(): MessageNotificationHelper
    fun provideNotifySendMessageUseCase(): NotifySendMessageUseCase
    fun provideSendMessageUseCase():SendMessageUseCaseById
}