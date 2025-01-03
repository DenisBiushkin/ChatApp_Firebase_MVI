package com.example.unmei.presentation.components

import androidx.annotation.OptIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Constraints
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG

@OptIn(UnstableApi::class)
@Composable
fun MainScreen(
    navController: NavController
){
    navController.clearBackStack(0)
    Surface(onClick = { /*TODO*/ }) {
        Text(text = "You Sign in ChatAPP!!!")

        navController.backQueue.forEachIndexed { index, navBackStackEntry ->
            Log.d(TAG, "Route $index: ${navBackStackEntry.destination.route}")
        }
    }
}