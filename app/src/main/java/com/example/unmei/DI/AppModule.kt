package com.example.unmei.DI


import android.app.Application
import androidx.room.Room
import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.data.network.retrofit.FcmApi
import com.example.unmei.data.network.retrofit.NotificationTokenHeaderInterceptor
import com.example.unmei.data.repository.MainRepositoryImpl
import com.example.unmei.data.repository.NotificationRepositoryImpl
import com.example.unmei.data.repository.token.FcmTokenManager
import com.example.unmei.data.repository.token.FcmTokenManagerImpl
import com.example.unmei.data.repository.token.FirebaseTokenProvider
import com.example.unmei.data.source.LocalDataSource
import com.example.unmei.data.source.UserDatabase
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.domain.usecase.CreateNewRoomAdvenceUseCase
import com.example.unmei.domain.usecase.messages.CreatePrivateChatUseCase
import com.example.unmei.domain.usecase.user.GetUserByIdUseCase
import com.example.unmei.domain.usecase.messages.ObserveChatRoomUseCase
import com.example.unmei.domain.usecase.messages.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.user.ObserveUserStatusByIdUseCase
import com.example.unmei.domain.usecase.user.ObserveUserUseCase
import com.example.unmei.domain.usecase.user.SaveUserOnceUseCase
import com.example.unmei.domain.usecase.user.SaveUserUseCase
import com.example.unmei.domain.usecase.user.SetStatusUserUseCase
import com.example.unmei.domain.usecase.messages.SendMessageUseCaseById
import com.example.unmei.util.ConstansApp.FCM_SEND_NOTIFICATION_BASE_URL
import com.example.unmei.util.ConstansDev
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.example.Mappers.base.Mapper
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserDataBase(app: Application): UserDatabase {
        return  Room.databaseBuilder(
            context = app,
            klass = UserDatabase::class.java,
            name = UserDatabase.Db_name
        ).build()
    }

    @Provides
    fun provideLocalDataSource(db:UserDatabase): LocalDataSource {
        return LocalDataSource(db.dao)
    }

    @Provides
    @Singleton
    fun provideRemoteDatasource(context:Application):RemoteDataSource{
        return RemoteDataSource(
            db=FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB),
            storage= FirebaseStorage.getInstance(ConstansDev.YOUR_PATHFOLDER_STORAGE),
            context= context
        )
    }
    @Provides
    @Singleton
    fun provideNotificationRepositorty(
        fcmApi:FcmApi,
        context: Application
    ):NotificationRepository{
        return  NotificationRepositoryImpl(
            db=FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB),
            fcmApi = fcmApi,
            context = context
        )
    }

    @Singleton
    @Provides
    fun provideMainRepository(
        localDataSource:LocalDataSource,
        remoteDataSource: RemoteDataSource,
        mapperChatRoom: Mapper<ChatRoomResponse,ChatRoom>
    ):MainRepository{
        return MainRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource= remoteDataSource,
            mapperChatRoom = mapperChatRoom
        )
    }

    @Singleton
    @Provides
    fun provideFcmProvider(
        context: Application
    ):FirebaseTokenProvider= FirebaseTokenProvider(context)
    @Singleton
    @Provides
    fun provideFcmTokenManager(
        firebaseTokenProvider: FirebaseTokenProvider
    ):FcmTokenManager = FcmTokenManagerImpl(firebaseTokenProvider)

    @Singleton
    @Provides
    fun provideFcmApi(
        fcmTokenManager:FcmTokenManager
    ):FcmApi{
        val client = OkHttpClient.Builder().apply {
            addInterceptor(NotificationTokenHeaderInterceptor(
                tokenManager = fcmTokenManager
            ))
        }.build()
        return Retrofit.Builder()
            .baseUrl(FCM_SEND_NOTIFICATION_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(FcmApi::class.java)
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
    fun provideObserveChatItemUseCase(mainRepository: MainRepository): ObserveRoomsUserUseCase {
        return ObserveRoomsUserUseCase(mainRepository)
    }

    @Provides
    fun provideObserveChatRoomUseCase(mainRepository: MainRepository): ObserveChatRoomUseCase {
        return ObserveChatRoomUseCase(mainRepository)
    }

    @Provides
    fun provideSetStatusUserUseCase(mainRepository: MainRepository): SetStatusUserUseCase {
        return SetStatusUserUseCase(mainRepository)
    }

    @Provides
    fun provideObserveUserStatusByIdUseCase(mainRepository: MainRepository): ObserveUserStatusByIdUseCase {
        return  ObserveUserStatusByIdUseCase(mainRepository)
    }

    @Provides
    fun provideGetUserByIdUseCase(mainRepository: MainRepository): GetUserByIdUseCase {
        return GetUserByIdUseCase(mainRepository)
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

}