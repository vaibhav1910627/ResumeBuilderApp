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
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val selectedCountryInfo = allCountriesList.find { it.code == uiState.resume.personal.countryCode }
    val minLength = selectedCountryInfo?.minLength ?: 7
    val maxLength = selectedCountryInfo?.maxLength ?: 15

    val phoneErrorText = if (minLength == maxLength) {
        "Must be exactly $minLength digits"
    } else {
        "Must be between $minLength and $maxLength digits"
    }

    val isEmailValid = isValidEmail(uiState.resume.personal.email)
    val isPhoneValid = uiState.resume.personal.phone.length in minLength..maxLength && uiState.resume.personal.phone.all {it.isDigit()}
    val isFormValid = uiState.resume.personal.name.isNotBlank() && isEmailValid && isPhoneValid
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

            // Country Autocomplete Picker
            item {
                CountryAutocompleteField(
                    selectedCountry = uiState.resume.personal.country,
                    selectedCode = uiState.resume.personal.countryCode,
                    onCountrySelected = { name, code ->
                        onFieldChange(null, null, null, name, code, null, null, null)
                    },
                    textFieldColors = textFieldColors
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.resume.personal.email,
                    onValueChange = { onFieldChange(null, it, null, null,null,null, null, null) },
                    label = { Text("Email") },
                    isError = uiState.resume.personal.email.isNotBlank() && !isEmailValid,
                    supportingText = {
                        if (uiState.resume.personal.email.isNotBlank() && !isEmailValid) {
                            Text("Please enter a valid email address (e.g. name@example.com)", color = MaterialTheme.colorScheme.error)
                        }
                    },
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
                        if (newValue.all { it.isDigit() } && newValue.length <= maxLength) {
                            onFieldChange(null, null, newValue, null, null, null, null, null)
                        }
                    },
                    label = { Text("Phone") },
                    prefix = {Text("${uiState.resume.personal.countryCode} ") },
                    isError = uiState.resume.personal.phone.isNotBlank() && !isPhoneValid,
                    supportingText = {
                        if (uiState.resume.personal.phone.isNotBlank() && !isPhoneValid) {
                            Text(
                               phoneErrorText,
                                color = MaterialTheme.colorScheme.error)
                        }
                    },
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

data class CountryInfo(val name: String, val code: String, val minLength: Int, val maxLength: Int)

val allCountriesList = listOf(
    CountryInfo("Afghanistan", "+93", 9, 9),
    CountryInfo("Albania", "+355", 8, 9),
    CountryInfo("Algeria", "+213", 9, 9),
    CountryInfo("Argentina", "+54", 10, 10),
    CountryInfo("Australia", "+61", 9, 9),
    CountryInfo("Austria", "+43", 10, 13),
    CountryInfo("Bangladesh", "+880", 10, 10),
    CountryInfo("Belgium", "+32", 9, 9),
    CountryInfo("Brazil", "+55", 10, 11),
    CountryInfo("Canada", "+1", 10, 10),
    CountryInfo("China", "+86", 11, 11),
    CountryInfo("Colombia", "+57", 10, 10),
    CountryInfo("Egypt", "+20", 10, 10),
    CountryInfo("France", "+33", 9, 9),
    CountryInfo("Germany", "+49", 10, 11),
    CountryInfo("Greece", "+30", 10, 10),
    CountryInfo("Hong Kong", "+852", 8, 8),
    CountryInfo("India", "+91", 10, 10),
    CountryInfo("Indonesia", "+62", 9, 12),
    CountryInfo("Iran", "+98", 10, 10),
    CountryInfo("Iraq", "+964", 10, 10),
    CountryInfo("Ireland", "+353", 9, 9),
    CountryInfo("Israel", "+972", 9, 9),
    CountryInfo("Italy", "+39", 9, 10),
    CountryInfo("Japan", "+81", 10, 10),
    CountryInfo("Malaysia", "+60", 9, 10),
    CountryInfo("Mexico", "+52", 10, 10),
    CountryInfo("Netherlands", "+31", 9, 9),
    CountryInfo("New Zealand", "+64", 8, 10),
    CountryInfo("Nigeria", "+234", 10, 10),
    CountryInfo("Pakistan", "+92", 10, 10),
    CountryInfo("Philippines", "+63", 10, 10),
    CountryInfo("Poland", "+48", 9, 9),
    CountryInfo("Portugal", "+351", 9, 9),
    CountryInfo("Russia", "+7", 10, 10),
    CountryInfo("Saudi Arabia", "+966", 9, 9),
    CountryInfo("Singapore", "+65", 8, 8),
    CountryInfo("South Africa", "+27", 9, 9),
    CountryInfo("South Korea", "+82", 9, 10),
    CountryInfo("Spain", "+34", 9, 9),
    CountryInfo("Sweden", "+46", 9, 9),
    CountryInfo("Switzerland", "+41", 9, 9),
    CountryInfo("Taiwan", "+886", 9, 9),
    CountryInfo("Thailand", "+66", 9, 9),
    CountryInfo("Turkey", "+90", 10, 10),
    CountryInfo("Ukraine", "+380", 9, 9),
    CountryInfo("United Arab Emirates", "+971", 9, 9),
    CountryInfo("United Kingdom", "+44", 10, 10),
    CountryInfo("United States", "+1", 10, 10),
    CountryInfo("Vietnam", "+84", 9, 10)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryAutocompleteField(
    selectedCountry: String,
    selectedCode: String,
    onCountrySelected: (String, String) -> Unit,
    textFieldColors: androidx.compose.material3.TextFieldColors


) {
    var query by remember(selectedCountry, selectedCode) { mutableStateOf(if (selectedCountry.isNotEmpty()) "$selectedCountry  ($selectedCode)" else selectedCode)}
    var expanded by remember {mutableStateOf(false)}

    var filteredCountries = allCountriesList.filter {
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

