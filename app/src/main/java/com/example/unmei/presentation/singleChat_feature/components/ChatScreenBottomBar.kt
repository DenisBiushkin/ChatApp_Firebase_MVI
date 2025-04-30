package com.example.unmei.presentation.singleChat_feature.components

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unmei.R
import com.example.unmei.presentation.util.CompactTextField


@Composable
@Preview(showBackground = true)
fun showChatScreenBottomBar(){
    ChatScreenBottomBar(
        textMessage = "",
        onValueChangeTextMessage = {},
        onClickContent = { /*TODO*/ }
    ) {

    }
}
@Composable
fun ChatScreenBottomBar(
    textMessage: String,
    onValueChangeTextMessage: (String) -> Unit,
    onClickContent: () -> Unit,
    onClickSendMessage: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 6.dp
                )
                .heightIn(min = 56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClickContent,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.attach_file_24px),
                    contentDescription = "Прикрепить",
                    tint = Color.Gray
                )
            }
            CompactTextField(
                value = textMessage,
                onValueChange = { onValueChangeTextMessage(it) },
                placeholder = "Сообщение...",
                modifier = Modifier.weight(1f)
            )


            IconButton(
                onClick = onClickSendMessage,
                modifier = Modifier.size(40.dp)
                    .padding(start = 6.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.send_24px),
                    contentDescription = "Отправить",
                    tint = Color(0xFF1976D2) // приятный синий акцент
                )
            }
        }
    }
}

