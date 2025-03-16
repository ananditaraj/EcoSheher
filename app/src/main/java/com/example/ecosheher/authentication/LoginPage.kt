package com.example.ecosheher.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import com.example.ecosheher.R
import com.example.ecosheher.ui.theme.opansnaps

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Sign In successfully!", Toast.LENGTH_SHORT).show()
                navController.navigate("homepage")
            }
            is AuthState.Error -> Toast.makeText(context,
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
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("Let's get you ")
                }
                withStyle(style = SpanStyle(color = Color(0xFF009951))) {
                    append("in.")
                }
            },
            fontWeight = FontWeight.ExtraBold,
            fontSize = 35.sp
        )

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            color = colorResource(R.color.grey),
            text = "Access your account.",
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.c),
            contentDescription = "authentication image",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

        if (authState.value is AuthState.Loading) {
            CircularProgressIndicator(color = colorResource(R.color.main_color))
        } else {
            Button(
                onClick = { authViewModel.login(email, password) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009951)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val signUpText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF333333))) {
                append("Don't have an account? ")
            }
            val startIndex = length // Track where "Signup" starts
            withStyle(style = SpanStyle(color = Color(0xFF009951), fontWeight = FontWeight.Bold)) {
                append("Sign Up")
            }
            addStringAnnotation(
                tag = "SIGNUP",
                annotation = "signup",
                start = startIndex,
                end = length
            )
        }

        ClickableText(
            text = signUpText,
            onClick = { offset ->
                signUpText.getStringAnnotations("SIGNUP", offset, offset)
                    .firstOrNull()?.let {
                        navController.navigate("signup")
                    }
            }
        )
    }
}
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    isPassword: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 14.sp,
            color = Color(0xFF333333)
        ),
        label = {
            Text(
                text = labelText,
                fontSize = 13.sp,
                color = if (isFocused) Color(0xFF009951) else Color(0xFFC4C4C4) // 🔥 Turns green when focused
            )
        },
        shape = RoundedCornerShape(20.dp), // 🔥 Matches Sign-In button's roundness
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF333333),
            unfocusedTextColor = Color(0xFF333333),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedBorderColor = Color(0xFF009951),
            unfocusedBorderColor = Color(0xFFC4C4C4),
            focusedLabelColor = Color(0xFF009951), // 🔥 Label moves up & turns green
            unfocusedLabelColor = Color(0xFFC4C4C4),
            cursorColor = Color(0xFF009951) // 🔥 Green cursor
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        singleLine = true,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .defaultMinSize(minHeight = 50.dp) // 🔥 Keeps height stable
            .padding(vertical = 2.dp)
            .onFocusChanged { isFocused = it.isFocused } // Detects focus change
    )
}
