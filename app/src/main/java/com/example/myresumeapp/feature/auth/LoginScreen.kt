package com.example.myresumeapp.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.components.HedgehogMascot


@Composable
fun SquareGridBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val gridLineColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
    val glowingCellColor1 = Color(0x306200EE)
    val glowingCellColor2 = Color(0x3000E676)
    val glowingCellColor3 = Color(0x251976D2)

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val gridSize = 36.dp.toPx()

            drawCircle(
                brush = radialGradient(
                    colors = listOf(Color(0x3000E676), Color.Transparent),
                    center = Offset(width * 0.2f, height * 0.2f),
                    radius = width * 0.5f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x3000676), Color.Transparent),
                    center = Offset(width * 0.8f, height * 0.7f),
                    radius = width * 0.5f
                )
            )

            val cols = (width / gridSize).toInt() + 1
            val rows = (height / gridSize).toInt() + 1

            val highlightedCells = listOf(
                Pair(2, 3) to glowingCellColor1,
                Pair(cols - 3, 5) to glowingCellColor2,
                Pair(3, rows - 4) to glowingCellColor3,
                Pair(cols - 2, rows - 2) to glowingCellColor1,
                Pair(5, 8) to glowingCellColor2
            )
            highlightedCells.forEach { (coord, color) ->
                val (c, r) = coord
                drawRect(
                    color = color,
                    topLeft = Offset(c * gridSize, r * gridSize),
                    size = Size(gridSize, gridSize)
                )
            }

            var x = 0f
            while (x <= width) {
                drawLine(
                    color = gridLineColor, start = Offset(x, 0f),
                    end = Offset(x, height),
                    1.dp.toPx()
                )
                x += gridSize
            }

            var y = 0f
            while (y <= height) {
                drawLine(
                    color = gridLineColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
                y += gridSize
            }
        }

        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onGuestLogin: () -> Unit
) {

    var isLoginMode by remember { mutableStateOf(true) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var primaryGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF2B3A55), Color(0xFF1A2536))
    )

    val textColor = MaterialTheme.colorScheme.onBackground
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        focusedLabelColor = MaterialTheme.colorScheme.primary
    )
    SquareGridBackground {
        Scaffold(
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HedgehogMascot(size = 200.dp)

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(primaryGradient)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Rezume",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "ATS-Optimized • PDF Export • Live Preview",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = CircleShape
                            ) {
                                Text(
                                    text = "ATS Score 95%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = CircleShape
                            ) {
                                Text(
                                    text = "Instant PDF",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mode Switcher Pill (Login / Sign Up)
//                Surface(
//                    color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
//                    shape = CircleShape,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(4.dp)
//                    ) {
//                        Button(
//                            onClick = { isLoginMode = true },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = if (isLoginMode) MaterialTheme.colorScheme.primary else Color.Transparent,
//                                contentColor = if (isLoginMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
//                            ),
//                            shape = CircleShape,
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Login", fontWeight = FontWeight.Bold)
//                        }
//
//                        Button(
//                            onClick = { isLoginMode = false },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = if (!isLoginMode) MaterialTheme.colorScheme.primary else Color.Transparent,
//                                contentColor = if (!isLoginMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
//                            ),
//                            shape = CircleShape,
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Sign Up", fontWeight = FontWeight.Bold)
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(
                            alpha = 0.9f
                        )
                    ),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome back",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

//                        AnimatedVisibility(
//                            visible = !isLoginMode,
//                            enter = fadeIn(),
//                            exit = fadeOut()
//                        ) {
//                            Column {
//                                OutlinedTextField(
//                                    value = fullName,
//                                    onValueChange = { fullName = it },
//                                    label = { Text("Full Name") },
//                                    leadingIcon = {
//                                        Icon(
//                                            Icons.Default.Person,
//                                            contentDescription = null
//                                        )
//                                    },
//                                    singleLine = true,
//                                    shape = RoundedCornerShape(16.dp),
//                                    modifier = Modifier.fillMaxWidth()
//                                )
//                                Spacer(modifier = Modifier.height(12.dp))
//                            }
//                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))


                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle Password"
                                    )

                                }

                            },
//                Text(
//                    text = "Welcome Back",
//                    style = MaterialTheme.typography.headlineLarge,
//                    color = textColor
//                )
//                Text(
//                    text = "Sign in to continue your resume",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = textColor.copy(alpha = 0.7f)
//                )
//
//                Spacer(Modifier.height(32.dp))
//
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = textFieldColors,
//                    shape = RoundedCornerShape(16.dp),
//                    singleLine = true
//                )
//
//                Spacer(Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Password") },
//                    visualTransformation = PasswordVisualTransformation(),
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = textFieldColors,
//                    shape = RoundedCornerShape(16.dp),
//                    singleLine = true
//                )
//
//                Spacer(Modifier.height(24.dp))
//
//                Button(
//                    onClick = onLoginSuccess,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//                ) {
//                    Text("Login", color = MaterialTheme.colorScheme.onPrimary)
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                TextButton(onClick = onNavigateToSignup) {
//                    Text("Don't have an account? Sign up")
//                }
//
//                TextButton(onClick = onGuestLogin) {
//                    Text("Continue as Guest", color = Color.Gray)
//                }
//            }
//        }
//    }
//}
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Main Action Button
                        Button(
                            onClick = onLoginSuccess,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Sign In →",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onNavigateToSignup) {
                            Text("Don't have an account? Sign up")
                        }

                        // Guest Login Option
                        TextButton(onClick = onGuestLogin) {
                            Text(
                                text = "Continue as Guest",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}