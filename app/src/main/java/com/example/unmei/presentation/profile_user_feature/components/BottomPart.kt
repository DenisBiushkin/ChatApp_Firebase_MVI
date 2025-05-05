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
import com.example.unmei.presentation.profile_user_feature.model.BlockInfo
import com.example.unmei.presentation.util.ui.theme.colorApp

@Composable
fun BottomPart(
    listInfoUser:List<BlockInfo>
){
    Box(
        modifier = Modifier.fillMaxSize()

    ){
        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Информация",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = colorApp
            )
            listInfoUser.forEach {
                BlockInfo(
                    text =it.text ,
                    trailingText =it.fieldName
                )
            }
        }
    }
}