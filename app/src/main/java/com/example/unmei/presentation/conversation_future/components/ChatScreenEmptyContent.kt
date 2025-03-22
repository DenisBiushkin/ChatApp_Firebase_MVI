package com.example.unmei.presentation.conversation_future.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unmei.R
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor


@Preview(showBackground = true)
@Composable
fun showChatScreenEmptyContent(){
    ChatScreenEmptyContent(
        backgroundColor = chatBacgroundColor
    )
}
@Composable
fun ChatScreenEmptyContent(
    modifier:Modifier = Modifier,
    backgroundColor: Color = Color.White
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
        ,
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(60.dp)
                ,
                tint = Color.Black,
                painter = painterResource(id = R.drawable.chat_48px ),
                 contentDescription = "Приветственное изображение для диалога"
            )
            Text(text = "Отправьте первое сообщение для начала диалога!")
        }
       
    }
}