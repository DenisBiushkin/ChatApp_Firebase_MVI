package com.example.unmei.presentation.friends_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.FriendRepository
import com.example.unmei.domain.usecase.user.GetFriendsByUserId
import com.example.unmei.domain.usecase.user.GetUsersExByFullName
import com.example.unmei.domain.usecase.user.UpdateProfileFullNameById
import com.example.unmei.domain.usecase.user.UpdateProfileUserNameById
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.friends_feature.model.FriendItemUi
import com.example.unmei.presentation.friends_feature.model.FriendVMEvent
import com.example.unmei.presentation.friends_feature.model.FriendsVMState
import com.example.unmei.util.ChatSessionManager
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.measureTime


@OptIn(FlowPreview::class)
@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getFriendsByUserId: GetFriendsByUserId,
    private val chatSessionManager: ChatSessionManager,
    private val updateProfileUserNameById: UpdateProfileUserNameById,
    private val updateProfileFullNameById: UpdateProfileFullNameById,
    private val getUsersExByFullName: GetUsersExByFullName,
    private val friendRepository: FriendRepository
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
                        isFriend =true
                    )
                }
                _state.value=state.value.copy(myFriends = dataUi)
                Log.d(TAG,"Данные $data")
            }
            Log.d(TAG,"Время работы $time")
//            currentUser.value?.uid?.let {
//                val friendId="jDzBkPemwZSzXJPX3onV6TBcRG42"
//                friendRepository.addFriendById(userId = it, friendId)
//                delay(2000)
//                friendRepository.deleteFriendById(userId = it, friendId)
//            }
            val updateResult=updateProfileFullNameById.execute(
                "jDzBkPemwZSzXJPX3onV6TBcRG42",
                "Cha He In")
            when( updateResult){
                is Resource.Error -> Log.d(TAG,"UpdateProfile ${updateResult.message}")
                is Resource.Loading -> {}
                is Resource.Success ->  Log.d(TAG,"UpdateProfile Success}")
            }
        }

        observeTextFieldForQuery()

    }
    private fun observeTextFieldForQuery(){
        viewModelScope.launch {
            state
                .map { it.searchQuery }
                .debounce(300)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest { query ->
                    _state.update { it.copy(isLoading = true) }
                    val userByFullName=getUsersExByFullName.execute(query)
                    Log.d(TAG,"Quary: $query")
                    Log.d(TAG,"Result Quary: $userByFullName")

//                    val results = searchFriends(query)


                    // _state.update { it.copy(myFriends = results, isLoading = false) }
                }
        }
    }
    fun onEvent(event: FriendVMEvent){
        when(event){
            is FriendVMEvent.SearchFieldChanged ->_state.update { state.value.copy(searchQuery = event.value) }
        }
    }

    fun focusChanged(focus:Boolean){

    }
}