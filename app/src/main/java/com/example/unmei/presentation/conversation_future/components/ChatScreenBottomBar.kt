package com.example.unmei.presentation.conversation_future.components

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.unmei.R

@Composable
fun ChatScreenBottomBar(
    textMessage:String,
    onValueChangeTextMessage:(String)->Unit,
    onClickContent: () -> Unit,
    onClickSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .heightIn(min = 56.dp) // фиксированная минимальная высота
            .background(Color.White)
            .imePadding()
           , // Защита от навигационной панели
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClickContent
        ) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .alpha(0.6f),
                painter = painterResource(id = R.drawable.paperclip_icon),
                contentDescription = null
            )
        }

        TextField(
            value = textMessage,
            onValueChange = { onValueChangeTextMessage(it) },
            placeholder = { Text(text = "Сообщение") },
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp, max = 100.dp), // ограничиваем рост поля
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            maxLines = 4
        )

        IconButton(
            onClick = onClickSendMessage
        ) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .alpha(0.6f),
                imageVector = Icons.Default.Send,
                contentDescription = null
            )
        }
    }
}
