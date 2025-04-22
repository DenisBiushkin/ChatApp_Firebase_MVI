package com.example.unmei.presentation.registration_feature.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.registration_feature.model.RegistrationVMEvent
import com.example.unmei.presentation.registration_feature.viewmodel.RegistrationViewModel
import com.example.unmei.presentation.sign_in_feature.components.TopPart
import com.example.unmei.presentation.util.LoginTextField
import com.example.unmei.presentation.util.ui.theme.Black
import com.example.unmei.presentation.util.ui.theme.BlueGray


@Composable
@Preview(showBackground = true)
fun showRegistrationScreen(){
    RegistrationScreen(
        navController = rememberNavController()
    )
}
@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = hiltViewModel()
){

    val state= viewModel.state.collectAsState()
    Surface {
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            TopPart(
                topLogo = "Регистрация"
            )
            Spacer(modifier = Modifier.height(10.dp))
             RegistrationBottomPart(
                 fullName=state.value.fullName,
                 fullNameValueChange={viewModel.onEvent(RegistrationVMEvent.FullNameChange(it))},
                 email_phoneField =state.value.emailOrPhone,
                 email_phoneOnValueChange = {viewModel.onEvent(RegistrationVMEvent.EmailOrPhoneChange(it))},
                 firstPasswordField = state.value.firstPassword,
                 firstPasswordOnValueChange = {viewModel.onEvent(RegistrationVMEvent.FirstPasswordChange(it))},
                 secondPasswordField = state.value.secondPassword,
                 secondPasswordOnValueChange ={viewModel.onEvent(RegistrationVMEvent.SecondPasswordChange(it))},
                 onClickRegistration = {
                     viewModel.onEvent(RegistrationVMEvent.Registration)
                     navController.navigate(Screens.SignIn.route)
                 }
             )
        }
    }

}

@Composable
fun RegistrationBottomPart(
    fullName:String,
    fullNameValueChange:(String)->Unit,
    email_phoneField:String,
    email_phoneOnValueChange:(String)->Unit,
    firstPasswordField:String,
    firstPasswordOnValueChange:(String)->Unit,
    secondPasswordField:String,
    secondPasswordOnValueChange:(String)->Unit,
    onClickRegistration:()->Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
        //.background(Color.Red)
    ) {
        LoginTextField(
            enableTrailing = false,
            label = "Полное имя",
            trailing = "",
            modifier = Modifier
                .fillMaxWidth(),
            textvalue = fullName,
            onvalueChanged = {
                //emailTextField.value = it
                fullNameValueChange(it)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        LoginTextField(
            enableTrailing = false,
            label = "Почта или телефон",
            trailing = "",
            modifier = Modifier
                .fillMaxWidth(),
            textvalue = email_phoneField,
            onvalueChanged = {
                //emailTextField.value = it
                email_phoneOnValueChange(it)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        LoginTextField(
            enableTrailing = false,
            label = "Пароль",
            trailing = "",
            modifier = Modifier
                .fillMaxWidth(),
            textvalue = firstPasswordField,
            onvalueChanged = {
                firstPasswordOnValueChange(it)
                //emailTextField.value = itашкemail_phoneOnValueChange(it)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        LoginTextField(
            enableTrailing = false,
            label = "Подтвердите пароль",
            trailing = "",
            modifier = Modifier
                .fillMaxWidth(),
            textvalue = secondPasswordField,
            onvalueChanged = {
                //emailTextField.value = it
                secondPasswordOnValueChange(it)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        RegisterORSignButton(
            textButton = "Зарегестрироваться",
            onClick = onClickRegistration
        )
    }
}
@Composable
fun RegisterORSignButton(
    textButton:String,
    onClick:()->Unit
){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlueGray else Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 4.dp),
        onClick = onClick
    ) {
        Text(
            text = textButton,
            style = MaterialTheme.typography.labelMedium,
//            modifier = Modifier.clickable {
//                signInOnClick()
//            }
        )
    }
}