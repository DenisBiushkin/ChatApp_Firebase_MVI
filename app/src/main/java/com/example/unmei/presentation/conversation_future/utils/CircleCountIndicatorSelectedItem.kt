package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.util.ui.theme.colorApp

@Composable
fun CircleCountIndicatorSelectedItem(
    modifier: Modifier = Modifier,
    isSelected:Boolean = false,
    count:Int = 1,
    onClickRound:()->Unit
){

    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.Transparent
            )
            .size(25.dp)
            .border(
                width = 2.dp,
                color = if (isSelected) Color.White else colorApp,
                shape = CircleShape,
            )
            .clickable(
                onClick = onClickRound
            )
        ,
        contentAlignment = Alignment.Center
    ){
      AnimatedVisibility(
          visible = isSelected,
          enter= fadeIn()+ expandIn(expandFrom = Alignment.Center),
          exit = fadeOut()
      ) {
          Box(
              modifier = Modifier
                  .size(22.dp)
                  .background(
                      shape = CircleShape,
                      color = colorApp
                  )
              ,
              contentAlignment = Alignment.Center
          ){
              Text(text = "$count", color = Color.White)
          }

      }
    }
}