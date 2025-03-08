package com.example.unmei.DI

import android.content.Context
import com.example.unmei.data.repository.MediaRepositoryImpl
import com.example.unmei.domain.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object MediaModule {

    @Provides
    @Singleton
    fun provideMediaRepository(context: Context):MediaRepository{
       return  MediaRepositoryImpl(context)
    }

}