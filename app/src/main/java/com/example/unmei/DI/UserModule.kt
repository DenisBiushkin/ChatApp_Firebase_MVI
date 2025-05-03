package com.example.unmei.DI

import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.usecase.user.GetFriendsByUserIdUseCase
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.user.GetUsersExByFullName
import com.example.unmei.domain.usecase.user.GetUsersWithStatus
import com.example.unmei.domain.usecase.user.GetUsersWithStatusUseCase
import com.example.unmei.domain.usecase.user.ObserveUserUseCase
import com.example.unmei.domain.usecase.user.SaveUserOnceUseCase
import com.example.unmei.domain.usecase.user.SaveUserUseCase
import com.example.unmei.domain.usecase.user.SetStatusUserUseCase
import com.example.unmei.domain.usecase.user.UpdateProfileFullNameById
import com.example.unmei.domain.usecase.user.UpdateProfileUserNameById
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserModule {


    @Provides
    fun provideGetFriendsByUserId(mainRepository: MainRepository): GetFriendsByUserIdUseCase{
        return GetFriendsByUserIdUseCase(mainRepository)
    }
    @Provides
    fun provideSaveUserUseCase(mainRepository: MainRepository): SaveUserUseCase {
        return SaveUserUseCase(mainRepository)
    }
    @Provides
    fun provideSaveUserOnceUseCase(mainRepository: MainRepository): SaveUserOnceUseCase {
        return SaveUserOnceUseCase(mainRepository)
    }

    @Provides
    fun provideObserveUserUseCase(mainRepository: MainRepository): ObserveUserUseCase {
        return ObserveUserUseCase(mainRepository)
    }
    @Provides
    fun provideSetStatusUserUseCase(mainRepository: MainRepository): SetStatusUserUseCase {
        return SetStatusUserUseCase(mainRepository)
    }

    @Provides
    fun provideGetUserByIdUseCase(mainRepository: MainRepository): GetUserByIdUseCase {
        return GetUserByIdUseCase(mainRepository)
    }

    @Provides
    fun provideGetUsersWithStatus(mainRepository: MainRepository):GetUsersWithStatus{
        return GetUsersWithStatus(mainRepository)
    }

    @Provides
    fun provideUpdateProfileUserNameById(mainRepository: MainRepository): UpdateProfileUserNameById{
        return UpdateProfileUserNameById(mainRepository)
    }
    @Provides
    fun provideUpdateProfileFullNameById(mainRepository: MainRepository): UpdateProfileFullNameById {
        return UpdateProfileFullNameById(mainRepository)
    }
    @Provides
    fun provideGetUsersExByFullName(mainRepository: MainRepository):GetUsersExByFullName{
        return GetUsersExByFullName(mainRepository)
    }
    @Provides
    fun provideGetUsersWithStatusUseCase(mainRepository: MainRepository):GetUsersWithStatusUseCase{
        return GetUsersWithStatusUseCase(mainRepository)
    }
}