package com.example.ecosheher.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecosheher.R
import com.example.ecosheher.ui.theme.opansnaps

@Composable
fun SignUpPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                navController.navigate("homepage")
            }
            is AuthState.Error-> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("Let's Sign")
                }
                withStyle(style = SpanStyle(color = Color(0xFF009951))) {
                    append(" Up.")
                }
            },
            fontWeight = FontWeight.ExtraBold,
            fontSize = 35.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Create your account.",
            color = colorResource(id = R.color.grey),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.b),
            contentDescription = "authentication image",
            modifier = Modifier
                .size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

//        OutlinedTextField(
//            value = username,
//            onValueChange = {
//                username = it
//            },
//            label = {
//                Text(text = "Username", color = Color.Gray, fontSize = 14.sp)
//            }
//        )
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = {
//                email = it
//            },
//            label = {
//                Text(text = "Email", color = Color.Gray, fontSize = 14.sp)
//            }
//        )
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = {
//                password = it
//            },
//            label = {
//                Text(text = "Password", color = Color.Gray, fontSize = 14.sp)
//            }
//        )
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        OutlinedTextField(
//            value = confirmpassword,
//            onValueChange = {
//                confirmpassword = it
//            },
//            label = {
//                Text(text = "Confirm Password", color = Color.Gray, fontSize = 14.sp)
//            }
//        )
        // Username Input
        CustomTextField(
            value = username,
            onValueChange = { username = it },
            labelText = "Username"
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Email Input
        CustomTextField(value = email, onValueChange = { email = it }, labelText = "Email")

        Spacer(modifier = Modifier.height(2.dp))

        // Password Input
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            labelText = "Password",
            isPassword = true
        )

        // Confirm Password Input
        CustomTextField(
            value = confirmpassword,
            onValueChange = { confirmpassword = it },
            labelText = "Password",
            isPassword = true
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.signup(username, email, password) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009951)), // Green color
            shape = RoundedCornerShape(20.dp), // Match Sign-In button
            modifier = Modifier
                .fillMaxWidth(0.9f) // Ensure same width as Sign-In
                .height(50.dp) // Ensure same height as Sign-In
        ) {
            Text(
                text = "Sign Up", // Keep text consistent
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth() // Ensures it takes up available width
                .padding(top = 1.dp) // Adds spacing from the button above
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF333333))) {
                        append("Already have an account? ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF009951), fontWeight = FontWeight.Bold)) {
                        append("Sign In")
                    }
                },
                fontSize = 14.sp, // Ensures readability
                //fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center // Centers text properly
            )
        }
    }
}
