package com.example.unmei.presentation.messanger_feature.util

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.unmei.R
import com.example.unmei.presentation.messanger_feature.model.MessageStatus

@Composable
fun MessageIconStatus(
    status: MessageStatus = MessageStatus.Readed,
    sizeIcon: Dp = 20.dp,
    colorIcon: Color = Color.Black
){
    val doneicon= ImageVector.vectorResource (id = R.drawable.done_all_24px)
    val errorIcon =ImageVector.vectorResource (id = R.drawable.error_24px)
        val loadingIcon=ImageVector.vectorResource (id = R.drawable.schedule_24px)
  when(status){
      MessageStatus.Error -> {
          Icon(
              modifier = Modifier
                  .size(sizeIcon),
              imageVector = errorIcon,
              contentDescription ="",
              tint = Color(0xFFcc0000),
          )
      }
      MessageStatus.Loading -> {
          Icon(
              modifier = Modifier
                  .size(sizeIcon),
              imageVector = loadingIcon,
              contentDescription ="",
              tint = colorIcon.copy(alpha = 0.5f),
          )
      }
      MessageStatus.Readed -> {
          Icon(
              modifier = Modifier
                  .size(sizeIcon),
              imageVector = doneicon,
              contentDescription ="",
              tint = colorIcon,
          )
      }
      MessageStatus.Send ->{
          Icon(
              modifier = Modifier
                  .size(sizeIcon),
              imageVector = doneicon,
              contentDescription ="",
              tint =colorIcon.copy(alpha = 0.5f),
          )
      }

      MessageStatus.None -> {

      }
  }
}