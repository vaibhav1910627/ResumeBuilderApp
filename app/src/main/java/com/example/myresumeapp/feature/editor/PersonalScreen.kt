package com.example.myresumeapp.feature.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myresumeapp.R
import com.example.myresumeapp.components.ResumeTopAppBar
import com.example.myresumeapp.data.model.UiState
import com.google.gson.annotations.Expose


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalScreen(
    uiState: UiState,
    onNext: () -> Unit,
    onPhotoPicked: (Uri?) -> Unit,
    onFieldChange: (name: String?, email: String?, phone: String?, country: String?, countryCode: String?, github: String?, linkedin: String?, location: String?) -> Unit
) {
    val focusManager = LocalFocusManager.current
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
            ResumeTopAppBar(
                title = "Personal Details",
                onBack = {}
            )
//                title = { Text("My Resume", color = textColor) },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                navigationIcon = {
//                    Button(
//                        onClick = {/*  */ },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Menu,
//                            contentDescription = "Open Navigation"
//                        )
//                    }
//                }
//            )
//        }
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
                    onValueChange = { onFieldChange(it, null, null, null,null, null ,null, null) },
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions( capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.resume.personal.email,
                    onValueChange = { onFieldChange(null, it, null, null,null,null, null, null) },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions( capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
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
                            onFieldChange(null, null, newValue, null, null, null, null, null)
                        }
                    },
                    label = { Text("Phone") },
                    prefix = {Text("${uiState.resume.personal.countryCode} ") },
                    keyboardOptions = KeyboardOptions( capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
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
                        onFieldChange(null, null, null, null, null,newValue, null, null)
                    },
                    label = { Text("GitHub (Optional)") },
                    keyboardOptions = KeyboardOptions( capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    isError = uiState.resume.personal.githublink?.let {
                        !isValidGitHubOrLinkedInUrl(
                            it,
                            "github"
                        )
                    } ?: false,
                    colors = textFieldColors
                )
            }

            // LinkedIn URL (Optional with Validation)
            item {
                OutlinedTextField(
                    value = uiState.resume.personal.linkedinlink ?: "",
                    onValueChange = { newValue ->
                        onFieldChange(null, null, null, null, null, null, newValue, null)
                    },
                    label = { Text("LinkedIn (Optional)") },
                    keyboardOptions = KeyboardOptions( capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    isError = uiState.resume.personal.linkedinlink?.let {
                        !isValidGitHubOrLinkedInUrl(
                            it,
                            "linkedin"
                        )
                    } ?: false,
                    colors = textFieldColors
                )
            }


            item {
                OutlinedTextField(
                    value = uiState.resume.personal.location,
                    onValueChange = { onFieldChange(null, null, null, null, null, null , null, it) },
                    label = { Text("Location") },
                    keyboardOptions = KeyboardOptions( capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()

                            if (uiState.resume.personal.name.isNotBlank()) {
                                onNext()
                            }
                        }
                    ),
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

data class CountryInfo(val name: String, val code: String)

val popularCountries = listOf(
    CountryInfo("India", "+91"),
    CountryInfo("United States", "+1"),
    CountryInfo("United Kingdom", "+44"),
    CountryInfo("Canada", "+1"),
    CountryInfo("Australia", "+61"),
    CountryInfo("Germany", "+49"),
    CountryInfo("France", "+33"),
    CountryInfo("Japan", "+81"),
    CountryInfo("China", "+86"),
    CountryInfo("Brazil", "+55"),
    CountryInfo("Italy", "+39"),
    CountryInfo("Spain", "+34")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryAutocompleteField(
    selectedCountry: String,
    selectedCode: String,
    onCountrySelected: (String, String) -> Unit,
    textFieldColors: androidx.compose.material3.TextFieldColors


) {
    var query by remember { mutableStateOf(selectedCountry.ifEmpty { selectedCode }) }
    var expanded by remember {mutableStateOf(false)}

    var filteredCountries = popularCountries.filter {
        it.name.contains(query, ignoreCase = true) || it.code.contains(query, ignoreCase = true)

    }

    ExposedDropdownMenuBox(
        expanded = expanded && filteredCountries.isNotEmpty(),
        onExpandedChange = {expanded=it},
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(

            value = query,
            onValueChange = {
                query = it
                expanded = true
            },
            label = {Text("Country / Country Code(e.g. India or +91") },
            modifier = Modifier
                .fillMaxWidth()
            .menuAnchor(),
            shape = RoundedCornerShape(16.dp),
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next)
        )

        ExposedDropdownMenu(
            expanded = expanded && filteredCountries.isNotEmpty(),
            onDismissRequest = {expanded = false},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp) )
        ) {
            filteredCountries.forEach { country ->
                DropdownMenuItem(
                    text = { Text("${country.name} (${country.code}")},
                    onClick = {
                        query = "${country.name} (${country.code})"
                        onCountrySelected(country.name, country.code)
                        expanded = false
                    }
                )
            }
        }
    }

}