package com.example.unmei.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.unmei.presentation.ui.theme.Black
import com.example.unmei.presentation.ui.theme.focusedTextFieldText
import com.example.unmei.presentation.ui.theme.textFieldContainer
import com.example.unmei.presentation.ui.theme.unfocusedTextFieldText


@Composable
fun LoginTextField(
    modifier: Modifier= Modifier,
    label:String,
    trailing:String,//заверщающая строка
    onvalueChanged:(String)->Unit,
    textvalue:String
){

    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    TextField(
        modifier = modifier,
        value = textvalue
        , onValueChange ={//при изменении
            it->onvalueChanged(it)
        }, label = {Text(
                text = label
                ,style=MaterialTheme
                    .typography.labelMedium
                ,color=uiColor
            )
        }, colors = TextFieldDefaults.colors(
            //фон подсказывающего текста
            unfocusedPlaceholderColor  = MaterialTheme.colorScheme.unfocusedTextFieldText,
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
            //фон контейнера
            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,//не выбран(не в фокусе)
            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer//выбран(в фокусе)
        ),
        trailingIcon ={//значек с правого конца конйтенера
            TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = trailing,
                        style=MaterialTheme
                            .typography
                            .labelMedium
                            .copy(fontWeight = FontWeight.Bold),
                        color=uiColor
                    )
            }
        }

    )

}