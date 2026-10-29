package com.example.myresumeapp.uiscreens

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(onSignupSuccess: () -> Unit, onNavigateToLogin: () -> Unit, onGuestLogin: () -> Unit) {
    // State variables for the fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Theme-adaptive colors (Same as LoginScreen)
    val textColor = MaterialTheme.colorScheme.onBackground
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        focusedLabelColor = MaterialTheme.colorScheme.primary
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
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                color = textColor
            )
            Text(
                text = "Join us to build your professional resume",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(32.dp))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = confirmPassword.isNotEmpty() && password != confirmPassword
            )

            Spacer(Modifier.height(32.dp))

            // Signup ButtonMaterialTheme.typography.headlineLarge,
            Button(
                onClick = {
                    // Add validation logic here before success
                    if (password == confirmPassword && email.isNotBlank()) {
                        onSignupSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank() && email.isNotBlank() && password.length >= 6
            ) {
                Text("Sign Up")
            }

            // Navigate back to Login
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "Already have an account? Login",
                    color = MaterialTheme.colorScheme.primary
                )
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
            Text("We don't want your data")
    }
}
}
