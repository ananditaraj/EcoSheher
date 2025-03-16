package com.example.ecosheher.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecosheher.R
import com.example.ecosheher.ui.theme.opansnaps

@Composable
fun SignUpPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Sign Up",
            fontFamily = opansnaps,
            fontWeight = FontWeight.ExtraBold,
            color = colorResource(id = R.color.main_color)

        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create your account to EcoSheher.",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.signupimg),
            contentDescription = "authentication image",
            modifier = Modifier
                .size(100.dp))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            }, label = {
                Text(text = "Username", color = Color.Black)
            })

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            }, label = {
                Text(text = "Email", color = Color.Black)
            })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password", color = Color.Black)
            }

        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmpassword,
            onValueChange = {
                confirmpassword = it
            },
            label = {
                Text(text = "Confirm Password", color = Color.Black)
            }

        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle Login */ },

            ) {
            Text("Sing Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate(("login")) }) {
            Text(text = "Already have an account ? Sign In", color = Color.Black)
        }
    }


}