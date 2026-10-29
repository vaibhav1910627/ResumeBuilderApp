package com.example.myresumeapp.uiscreens

import android.net.Uri
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myresumeapp.R
import com.example.myresumeapp.data.UiState




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalScreen(
    uiState: UiState,
    onNext: () -> Unit,
    onPhotoPicked: (Uri?) -> Unit,
    onFieldChange: (name: String?, email: String?, phone: String?, github: String?, linkedin: String?, location: String?) -> Unit
) {

    val pickPhoto =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            onPhotoPicked(uri)
        }

    // Function to validate the URL (for GitHub and LinkedIn)
    fun isValidGitHubOrLinkedInUrl(url: String, type: String): Boolean {
        val regex = when (type) {
            "github" -> "https://github.com/.*".toRegex()
            "linkedin" -> "https://www.linkedin.com/in/.*".toRegex()
            else -> return false
        }
        return regex.matches(url)
    }

    // Define colors based on theme (light or dark mode)
    val textColor = MaterialTheme.colorScheme.onBackground // Text color based on theme
    val backgroundColor = MaterialTheme.colorScheme.background // Background color based on theme
    val buttonColor = MaterialTheme.colorScheme.primary // Button color based on theme
    val buttonTextColor = MaterialTheme.colorScheme.onPrimary // Button text color based on theme


    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        focusedLabelColor = MaterialTheme.colorScheme.primary
    )


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Resume", color = textColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                        navigationIcon = {
                            Button(
                        onClick = {/*  */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    model = uiState.localPhotoUri ?: R.drawable.baseline_image_24,
                    contentDescription = "Selected Photo",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.resume.personal.name,
                    onValueChange = { onFieldChange(it, null, null, null, null, null) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.resume.personal.email,
                    onValueChange = { onFieldChange(null, it, null, null, null, null) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.resume.personal.phone,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            onFieldChange(null, null, newValue, null, null, null)
                        }
                    },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors
                )
            }

            // GitHub URL (Optional with Validation)
            item {
                OutlinedTextField(
                    value = uiState.resume.personal.githublink ?: "",
                    onValueChange = { newValue ->
                        onFieldChange(null, null, null, newValue, null, null)
                    },
                    label = { Text("GitHub (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    isError = uiState.resume.personal.githublink?.let { !isValidGitHubOrLinkedInUrl(it, "github") } ?: false,
                    colors = textFieldColors
                )
            }

            // LinkedIn URL (Optional with Validation)
            item {
                OutlinedTextField(
                    value = uiState.resume.personal.linkedinlink ?: "",
                    onValueChange = { newValue ->
                        onFieldChange(null, null, null, null, newValue, null)
                    },
                    label = { Text("LinkedIn (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    isError = uiState.resume.personal.linkedinlink?.let { !isValidGitHubOrLinkedInUrl(it, "linkedin") } ?: false,
                    colors = textFieldColors
                )
            }


            item {
                OutlinedTextField(
                    value = uiState.resume.personal.location,
                    onValueChange = { onFieldChange(null, null, null, null, null, it) },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors
                )
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    val isPhoneValid = uiState.resume.personal.phone.all { it.isDigit() } &&
                            uiState.resume.personal.phone.isNotBlank()
                    Button(
                        onClick = onNext, colors = ButtonDefaults.buttonColors(Color.Gray),
                        enabled = uiState.resume.personal.name.isNotBlank() && uiState.resume.personal.email.isNotBlank()
                    ) {
                        Text("Next", color = Color.White)
                    }
                }
            }
        }
    }
}
