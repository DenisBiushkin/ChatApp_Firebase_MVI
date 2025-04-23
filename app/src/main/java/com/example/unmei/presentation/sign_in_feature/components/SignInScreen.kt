package com.example.unmei.presentation.sign_in_feature.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.R
import com.example.unmei.presentation.util.ui.theme.Black
import com.example.unmei.presentation.util.ui.theme.BlueGray
import com.example.unmei.presentation.util.LoginTextField
import com.example.unmei.presentation.util.SocialMediaLogIn

@Preview(showBackground = true)
@Composable
fun showLoginScreen() {
    SignInScreen(
      signInWithGoogleOnClick = {

      },
        signInOnClick = {

        },
        forgotOnClick = {

        },
        createAccountOnClick = {

        },
        passwordField = "",
        passwordFieldOnChange = {

        },
        emailField = "",
        emailFieldOnChange = {

        }
    )
}
@Composable
fun SignInScreen(
   signInOnClick:()->Unit,
   signInWithGoogleOnClick:()->Unit,
   forgotOnClick:()->Unit,
   createAccountOnClick:()->Unit,
   passwordFieldOnChange: (String)->Unit,
   passwordField:String,
   emailFieldOnChange:(String)->Unit,
   emailField:String
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
                    label = "Почта",
                    trailing = "",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textvalue = emailField,
                    onvalueChanged = {
                        //emailTextField.value = it
                        emailFieldOnChange(it)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                LoginTextField(
                    label = "Пароль",
                    trailing = "Забыли?",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textvalue = passwordField,
                    onvalueChanged = {
                        passwordFieldOnChange(it)
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
                    shape = RoundedCornerShape(size = 4.dp),
                    onClick =  signInOnClick
                ) {
                    Text(
                        text = "Войти",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.clickable {
                            signInOnClick()
                        }
                    )
                }

                ///lhkjhkjhkljlkjljjklj
                Spacer(
                    modifier = Modifier.height(30.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text="Продолжить с",
                        style=MaterialTheme.typography.labelMedium.copy(color=Color(0xFF64748B))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ){
                        SocialMediaLogIn(
                            icon = R.drawable.google,
                            text = "Google",
                            OnClick = signInWithGoogleOnClick,
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }
                val uiColor = if (isSystemInDarkTheme()) Color.White else Black

                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.6f)
                        .fillMaxWidth()
                    ,
                    contentAlignment = Alignment.BottomCenter
                ){
                    Row(){
                        Text(
                            text= buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color=Color(0xff94A3B8),
                                        fontSize = 14.sp,
                                        fontWeight= FontWeight.Normal,
                                        fontFamily = FontFamily.Default
                                    )
                                ){
                                    append(text = "Нет аккаунта?")
                                }
                            }
                        )
                        Text(text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color= uiColor,
                                    fontSize = 14.sp,
                                    fontWeight= FontWeight.Medium,
                                    fontFamily = FontFamily.Default
                                )
                            ){
                                append(text = " Создать..")
                            }
                        }
                        , modifier = Modifier.clickable {
                            createAccountOnClick()
                            })
                    }

                }
            }

        }

    }
}

@Composable
private fun LoginSection(
    emailTextField: MutableState<String>,
    passwordTextField: MutableState<String>
) {

}

@Composable
fun TopPart(
    topLogo:String = "Вход"
) {
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
            , text =topLogo,
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}

