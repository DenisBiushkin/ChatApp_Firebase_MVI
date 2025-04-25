package com.example.unmei.presentation.friends_feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview(showBackground = true)
fun showSearch(){
    Box(modifier = Modifier.fillMaxSize()){
        SearchBar(
            query = "",
            onQueryChange = {

            },
            onFocusedChanged = {

            }
        )
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFocusedChanged:(FocusState)->Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier

            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = Color(0xFFF0F1F5),
                shape = RoundedCornerShape(10.dp)
            )
            .onFocusChanged { onFocusedChanged(it) }
        ,
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text="Введите имя и фамилию",
                fontSize = 14.sp
                )
        },
        leadingIcon = {
            Icon(
//                modifier = Modifier.size(
//                    20.dp
//                ),
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(10.dp),
        singleLine = true
    )
}