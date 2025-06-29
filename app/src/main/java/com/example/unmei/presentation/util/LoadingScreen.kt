package com.example.unmei.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun LoadingScreen (
   modifier: Modifier = Modifier
){
    Box(
        modifier =  modifier
            .fillMaxSize()
           // .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = false) {} // блокирует клики под ним
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary
        )
    }
}