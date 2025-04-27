package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.util.ui.theme.colorApp

@Composable
fun LoadingContentProgressIndicator(
    modifier: Modifier = Modifier,
    visibility: Boolean,

    ) {
   if(visibility){
     Box(
         modifier = modifier
             .heightIn(min = 60.dp, max = 60.dp)
             .fillMaxWidth()
           //  .background(Color.LightGray)
         ,
         contentAlignment = Alignment.Center,

     ){
         CircularProgressIndicator(
             modifier = Modifier
                 .size(30.dp),
             color = colorApp
         )
       }
   }
}