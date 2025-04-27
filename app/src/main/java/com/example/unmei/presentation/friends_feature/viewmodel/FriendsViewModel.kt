package com.example.unmei.presentation.friends_feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.FriendRepository
import com.example.unmei.domain.usecase.user.GetFriendsByUserId
import com.example.unmei.domain.usecase.user.GetUsersExByFullName
import com.example.unmei.domain.usecase.user.UpdateProfileFullNameById
import com.example.unmei.domain.usecase.user.UpdateProfileUserNameById
import com.example.unmei.domain.util.OrderType
import com.example.unmei.presentation.friends_feature.model.FriendItemUi
import com.example.unmei.presentation.friends_feature.model.FriendVMEvent
import com.example.unmei.presentation.friends_feature.model.FriendsContentState
import com.example.unmei.presentation.friends_feature.model.FriendsVMState
import com.example.unmei.util.ChatSessionManager
import com.example.unmei.util.ConstansDev.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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
    private val myFriendsUids = MutableStateFlow<Set<String>>(emptySet())

    private val _state = MutableStateFlow(FriendsVMState())
    val state:StateFlow<FriendsVMState> = _state.asStateFlow()


    private var getMyFriends: Job? = null

    init {
        currentUser.value = chatSessionManager.getCurrentUser()
        myFriendsUids.value = currentUser.value?.friends?.toSet() ?: emptySet()
        Log.d(TAG,"USER FROM Prefernece "+chatSessionManager.getCurrentUser().toString())
        getMyFriends(orderType = OrderType.Descending)
        observeTextFieldForQuery()
    }

    private fun observeTextFieldForQuery(){
        viewModelScope.launch {
            state.map { it.searchQuery }
                .debounce(500)
                .distinctUntilChanged()
                // .filter { it.isNotBlank() }
                .collectLatest { query ->
                    _state.update { it.copy(contentState = FriendsContentState.Loading) }
                    if (query.isBlank()){
                        _state.update { it.copy(
                            searchResultList = emptyList(),
                            contentState = FriendsContentState.Content
                        ) }
                        return@collectLatest
                    }
                    val userByFullNameUI=getUsersExByFullName.execute(query).map {
                        it.toFriendItemUi()
                            .copy(isFriend = myFriendsUids.value.contains(it.user.uid))

                    }
                    _state.update { it.copy(
                        contentState = FriendsContentState.Content,
                        searchResultList = userByFullNameUI,
                        isSearchActive = true
                    )
                    }
                }

        }
    }
    private fun getMyFriends(orderType: OrderType){
        getMyFriends=null
        getMyFriends=viewModelScope.launch {
            _state.update { state.value.copy(contentState = FriendsContentState.Loading) }

            val friendsCurrentUser = getFriendsByUserId.execute(currentUser.value!!.uid) ?:

            return@launch _state.update { state.value.copy(
                contentState = FriendsContentState.Content,
                myFriendsList = emptyList()
                )
            }

            val friendsUi = friendsCurrentUser.map { it.toFriendItemUi() }
            _state.value=state.value.copy(
                myFriendsList = friendsUi,
                contentState = FriendsContentState.Content
            )
        }
    }
    fun onEvent(event: FriendVMEvent){
        when(event){
            is FriendVMEvent.SearchFieldChanged ->_state.update { state.value.copy(searchQuery = event.value) }
            is FriendVMEvent.SearchActiveChanged -> changeSearchActive(event.value)
            is FriendVMEvent.AddNewFriend -> addNewFriend(event.value)
        }
    }

    private fun addNewFriend(userId:String){
        viewModelScope.launch {
            val result=friendRepository.addFriendById(
                userId = currentUser.value!!.uid,
                friendId = userId
            )
            if (!result)
                return@launch
            _state.update {
                it.copy(
                    searchResultList = it.searchResultList.map { user ->
                        if (user.uid == userId) user.copy(isFriend = true)
                        else user
                    }
                )
            }
        }
    }
    private fun changeSearchActive(isActive: Boolean){
        if(isActive && state.value.searchQuery.isEmpty()){
            _state.update {
                it.copy(
                    searchResultList = emptyList(),
                    isSearchActive = isActive
                )
            }
            return
        }
        if (state.value.isSearchActive && !isActive){
            getMyFriends(OrderType.Ascending)
            _state.update { it.copy(
                searchQuery = "",
                isSearchActive = false
            ) }
            return
        }
        _state.update {
            it.copy(
                isSearchActive = isActive
            )
        }

    }

    fun focusChanged(focus:Boolean){

    }
}