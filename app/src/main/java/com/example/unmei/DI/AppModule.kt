package com.example.unmei.DI


import android.app.Application
import androidx.room.Room
import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.data.repository.MainRepositoryImpl
import com.example.unmei.data.source.LocalDataSource
import com.example.unmei.data.source.UserDatabase
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.usecase.ObserveChatRoomUseCase
import com.example.unmei.domain.usecase.ObserveRoomsUserUseCase
import com.example.unmei.domain.usecase.ObserveUserUseCase
import com.example.unmei.domain.usecase.SaveUserOnceUseCase
import com.example.unmei.domain.usecase.SaveUserUseCase
import com.example.unmei.util.ConstansDev
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.example.Mappers.base.Mapper
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
    fun provideRemoteDatasource():RemoteDataSource{
        return RemoteDataSource(FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB))
    }
    @Provides
    fun provideMainRepository(
        localDataSourc:LocalDataSource,
        remoteDataSource: RemoteDataSource,
        mapperChatRoom: Mapper<ChatRoomResponse,ChatRoom>
    ):MainRepository{
        return MainRepositoryImpl(
            localDataSource = localDataSourc,
            remoteDataSource= remoteDataSource,
            mapperChatRoom = mapperChatRoom
        )
    }
    @Provides
    fun provideSaveUserUseCase(mainRepository: MainRepository):SaveUserUseCase{
        return SaveUserUseCase(mainRepository)
    }
    @Provides
    fun provideSaveUserOnceUseCase(mainRepository: MainRepository):SaveUserOnceUseCase{
        return SaveUserOnceUseCase(mainRepository)
    }

    @Provides
    fun provideObserveUserUseCase(mainRepository: MainRepository):ObserveUserUseCase{
        return ObserveUserUseCase(mainRepository)
    }
    @Provides
    fun provideObserveChatItemUseCase(mainRepository: MainRepository):ObserveRoomsUserUseCase{
        return ObserveRoomsUserUseCase(mainRepository)
    }

    @Provides
    fun provideObserveChatRoomUseCase(mainRepository: MainRepository): ObserveChatRoomUseCase {
        return ObserveChatRoomUseCase(mainRepository)
    }

}