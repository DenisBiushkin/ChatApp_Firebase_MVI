package com.example.unmei.presentation.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.R
import com.example.unmei.presentation.util.ui.theme.Black
import com.example.unmei.presentation.util.ui.theme.BlueGray
import com.example.unmei.presentation.util.LoginTextField

@Preview(showBackground = true)
@Composable
fun showLoginScreen() {
    SignInScreen(navController = rememberNavController())
}
@Composable
fun SignInScreen(
    navController: NavController
) {
    val passwordTextField = remember {
        mutableStateOf("")
    }
    val emailTextField = remember {
        mutableStateOf("")
    }
    Surface(

    ) {//различие от Box,он подцепляет значение Material(dar/light)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopPart()
            Spacer(modifier = Modifier.height(25.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp)
                //.background(Color.Red)
            ) {
                LoginTextField(
                    label = "Email",
                    trailing = "",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textvalue = emailTextField.value,
                    onvalueChanged = {
                        emailTextField.value = it
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                LoginTextField(
                    label = "Password",
                    trailing = "Forgot?",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textvalue = passwordTextField.value,
                    onvalueChanged = {
                        passwordTextField.value = it
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) BlueGray else Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(size = 4.dp), onClick = {
                        //переход по контроллеру с передачей обязательного аргумента
                       // println(Screen.Main.route)
                        //println(Screen.Main.passIdandName(10, "Denis"))
//                        navController
//                            .navigate(
//                                // route=Screen.Dateil.passIdandName(10,"Denis")
//                               // route = Screen.Main.passId(12)
//                                // route = Screen.Dateil.route//переход без передачи данных
//                            )
                    }
                ) {
                    Text(
                        text = "Log in",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

    }
}

@Composable
fun TopPart() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.46f)//46%
            , painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds//заполнить все отведенное про-во
        )
        Row(
            modifier = Modifier
                .padding(start = 0.dp)
                .padding(top = 80.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(id = R.drawable.login_tohsaka),
                contentDescription = stringResource(id = R.string.logo),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_log),
                    style = MaterialTheme.typography.headlineMedium,
                    color = uiColor
                )
                Text(
                    text = stringResource(id = R.string.app_house),
                    style = MaterialTheme.typography.titleMedium,
                    color = uiColor
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter)//выровнить
            , text = stringResource(id = R.string.app_login_text),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}
