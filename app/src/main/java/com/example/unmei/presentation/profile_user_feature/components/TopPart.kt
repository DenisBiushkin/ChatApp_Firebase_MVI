package com.example.unmei.presentation.profile_user_feature.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopPart(
    backOnClick:()->Unit,
    fullName:String,
    statusOnline:String,
    painterIcon: Painter,
){
    val colorText = Color(0xFFFFFFFF)

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
           // .background(Color.Blue),
        ,// contentAlignment = Alignment.BottomCenter
    ){
      Image(
          modifier = Modifier.matchParentSize(),
          painter = painterIcon ,
          contentScale = ContentScale.Crop,
          contentDescription =""
      )
      Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.SpaceBetween
      ) {
          //navigation
           TopAppBar(
               modifier = Modifier.padding(
                   top= 25.dp
               ),
               title = { /*TODO*/
               },
               backgroundColor = Color.Transparent,
               elevation = 0.dp,
               navigationIcon = {
                   Icon(
                       modifier = Modifier
                           .padding(start = 16.dp)
                           .clickable {
                               backOnClick()
                           },
                       imageVector =  Icons.Default.ArrowBack,
                       tint = Color.White,
                       contentDescription =""
                   )
               },
//               actions = {
//                   Icon(
//                       modifier = Modifier
//                           .padding(start = 16.dp)
//                           .clickable {
//
//                           },
//                       imageVector=Icons.Default.MoreVert,
//                       tint = Color.White,
//                       contentDescription =""
//                   )
//               }
           )
          //Info User
         Row (
             modifier = Modifier
                 .padding(
                     horizontal = 20.dp,
                     vertical = 10.dp
                 )
             ,
             horizontalArrangement = Arrangement.Start,
             verticalAlignment = Alignment.CenterVertically
         ){
             Column (
                 verticalArrangement = Arrangement.spacedBy(7.dp)
             ){
                 Text(
                     text = fullName,
                     //fontFamily = lexendFontFamily,
                     fontWeight = FontWeight.Medium,
                     fontSize = 18.sp,
                     color = colorText
                 )
                 Text(
                     text =  statusOnline,
                     //fontFamily = lexendFontFamily,
                     fontWeight = FontWeight.Medium,
                     fontSize = 14.sp,
                     color = colorText.copy(0.6f)
                 )
             }

         }
      }

    }
}