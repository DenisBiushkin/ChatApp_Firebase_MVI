package com.example.unmei.presentation.conversation_future.viewmodel

import androidx.lifecycle.ViewModel
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ConversationViewModel @Inject constructor(

):ViewModel() {

    val _state = MutableStateFlow<ConversationVMState>(ConversationVMState())
    val state:StateFlow<ConversationVMState> = _state.asStateFlow()

}