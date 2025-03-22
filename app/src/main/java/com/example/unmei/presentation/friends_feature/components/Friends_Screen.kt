package com.example.unmei.presentation.friends_feature.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Preview(showBackground = true)
@Composable
fun show15(){
    Friends_Screen(navController = rememberNavController())
}

@Composable
fun Friends_Screen(
    navController: NavController
){

}