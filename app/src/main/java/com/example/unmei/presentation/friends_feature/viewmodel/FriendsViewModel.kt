package com.example.unmei.presentation.friends_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.User
import com.example.unmei.domain.usecase.user.GetFriendsByUserId
import com.example.unmei.presentation.friends_feature.model.FriendItemUi
import com.example.unmei.presentation.friends_feature.model.FriendsVMState
import com.example.unmei.util.ChatSessionManager
import com.example.unmei.util.ConstansDev.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.measureTime


@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getFriendsByUserId: GetFriendsByUserId,
    private val chatSessionManager: ChatSessionManager,
):ViewModel() {

    private val currentUser = MutableStateFlow<User?>(null)

    private val _state = MutableStateFlow(FriendsVMState())
    val state:StateFlow<FriendsVMState> = _state.asStateFlow()

    init {
        currentUser.value = chatSessionManager.getCurrentUser()

        Log.d(TAG,"USER FROM Prefernece "+chatSessionManager.getCurrentUser().toString())

        viewModelScope.launch {
            val time= measureTime {
                val data = getFriendsByUserId.execute("jDzBkPemwZSzXJPX3onV6TBcRG42") ?: return@launch
                val dataUi = data.mapIndexed(){ index,it->
                    FriendItemUi(
                        uid = it.user.uid,
                        fullName = it.user.fullName,
                        iconUrl = it.user.photoUrl,
                        isOnline = it.statusUser.status == Status.ONLINE,
                        isFriend = index %2 ==0
                    )
                }
                _state.value=state.value.copy(myFriends = dataUi)

                Log.d(TAG,"Данные $data")
            }
            Log.d(TAG,"Время работы $time")
        }

    }
}