package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.util.ui.theme.colorApp

@Composable
fun LoadingCircleProgressNewMessages(
    isLoading: Boolean = false
){
    if(isLoading){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

            , horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(30.dp)
                ,
                color = colorApp
            )
        }
    }
}