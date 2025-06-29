package com.example.unmei.presentation.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.util.ui.theme.BlueGray
import com.example.unmei.presentation.util.ui.theme.LightBlueWhite

@Composable
fun SocialMediaLogIn(
    @DrawableRes icon:Int,
    text:String,
    OnClick:()->Unit,
    modifier: Modifier = Modifier
){
   Row(
       modifier = modifier
           .clip(RoundedCornerShape(4.dp))
           .socialMedia()
           .clickable { OnClick() }
           .height(40.dp)
       ,
       verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.Center
   ){
           Image(
               painter = painterResource(id = icon),
               contentDescription = "",
               modifier = Modifier.size(16.dp)
           )
           Spacer(modifier = Modifier.width(5.dp))
           Text(
               text = text,
               style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
           )
   }

}

fun Modifier.socialMedia() : Modifier = composed{
    if(isSystemInDarkTheme()){
        background(Color.Transparent).border(
            width = 1.dp,
            color = BlueGray,
            shape = RoundedCornerShape(4.dp)
        )
    }else{
        background(LightBlueWhite)
    }
}