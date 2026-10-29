package com.example.myresumeapp.uiscreens

import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToSignup: () -> Unit, onGuestLogin:() -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val textColor = MaterialTheme.colorScheme.onBackground

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome Back", style = MaterialTheme.typography.headlineLarge, color = textColor)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                // visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { /* Call ViewModel Login */ onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ) {
                Text("Login")
            }

            TextButton(onClick = onNavigateToSignup) {
                Text("Don't have an account? Sign Up", color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = {
                    // Add validation logic here before success
                    onGuestLogin()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color.Gray)
//                enabled = name.isNotBlank() && email.isNotBlank() && password.length >= 6
            ) {
                Text("Guest",style = MaterialTheme.typography.headlineLarge, color = Color.White)
            }
            Text("We dont want your data")
        }
    }
}
