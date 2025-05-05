package com.example.unmei.presentation.profile_user_feature.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.profile_user_feature.model.BlockInfo
import com.example.unmei.presentation.util.ui.theme.colorApp



@Composable
fun BlockInfo(
    modifier: Modifier = Modifier,
    text:String,
    trailingText:String
){
  Column(
      modifier = modifier//.padding(vertical = 2.dp)
  ) {
      Text(
          text = text,
          fontWeight = FontWeight.Medium,
          fontSize = 17.sp,
          color = Color.Black.copy(0.9f)
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
          text = trailingText,
          fontWeight = FontWeight.Normal,
          fontSize = 14.sp,
          color = Color.Black.copy(0.5f)
      )
      Spacer(modifier = Modifier.height(4.dp))
      HorizontalDivider(modifier = Modifier)
  }
}
