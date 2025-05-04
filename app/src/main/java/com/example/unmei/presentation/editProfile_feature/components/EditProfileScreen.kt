package com.example.unmei.presentation.editProfile_feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.unmei.presentation.util.ui.theme.Black
import com.example.unmei.presentation.util.ui.theme.BlueGray


@Composable
@Preview(showBackground = true)
fun showEditProfileScreen(){
    EditProfileScreen(
        fullName = Pair("",""),
        username = "@ksfhkfs",

        onValueChange = {
                firstName, lastName, username->
        },
        onBackClick = {

        },
        saveProfile = {

        }

    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    fullName: Pair<String, String>, // (firstName, lastName)
    username: String,
    profilePhotoUrl: String? = null,
    onBackClick: () -> Unit,
    onValueChange: (firstName: String, lastName: String, username: String) -> Unit,
    saveProfile:()->Unit
) {
    var firstName by remember { mutableStateOf(fullName.first) }
    var lastName by remember { mutableStateOf(fullName.second) }
    var user by remember { mutableStateOf(username) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile photo
            val sizeIcon = 150.dp
            if (profilePhotoUrl != null) {
                AsyncImage(
                    model = profilePhotoUrl,
                    contentDescription = "Profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(sizeIcon)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(sizeIcon)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("?", color = Color.White, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // First name
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    onValueChange(firstName, lastName, user)
                },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Last name
            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    onValueChange(firstName, lastName, user)
                },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Username
            OutlinedTextField(
                value = user,
                onValueChange = {
                    user = it
                    onValueChange(firstName, lastName, user)
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) BlueGray else Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(size = 4.dp),
                onClick =  saveProfile
            ) {
                Text(
                    text = "Сохранить",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable {

                    }
                )
            }
        }
    }
}