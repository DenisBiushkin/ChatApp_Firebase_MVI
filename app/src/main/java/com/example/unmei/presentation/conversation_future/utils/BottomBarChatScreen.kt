package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.unmei.R
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import kotlinx.coroutines.launch

@Composable
fun BottomBarChatScreen(
    vmState: State<ConversationVMState>,
    viewModel: ConversationViewModel,
    lazyState: LazyListState
){

    val text =  remember {
      mutableStateOf("")
    }

    val scope= rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
       //HorizontalDivider()
       IconButton(
           onClick = {
               viewModel.sendMessage(text.value)
               scope.launch {
                   lazyState.animateScrollToItem(lazyState.layoutInfo.totalItemsCount-1)
               }


               text.value = ""
            }
       ) {

           Icon(
               modifier = Modifier
                   .size(28.dp)
                   .alpha(alpha = 0.6f)
               ,painter = painterResource(id = R.drawable.paperclip_icon)
              ,contentDescription =null
           )
        }
        TextField(
            value = text.value,
            onValueChange ={
                text.value = it
            },
            placeholder = {Text(text="Сообщение")},

            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,

                focusedIndicatorColor = Color.Transparent, // Убираем подчеркивание в фокусе
                unfocusedIndicatorColor = Color.Transparent, // Убираем подчеркивание без фокуса
                disabledIndicatorColor = Color.Transparent // Убираем подчеркивание в неактивном состоянии
            )
        )

    }
}