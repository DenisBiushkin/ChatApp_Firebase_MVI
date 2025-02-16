package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.unmei.R

@Composable
fun BottomBarChatScreen(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
       //HorizontalDivider()
       IconButton(onClick = { /*TODO*/ }) {

           Icon(
               modifier = Modifier
                   .size(28.dp)
                   .alpha(alpha = 0.6f)
               ,painter = painterResource(id = R.drawable.paperclip_icon)
              ,contentDescription =null
           )
        }
        TextField(
            value = "",
            onValueChange ={},
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