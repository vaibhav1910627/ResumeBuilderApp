package com.example.myresumeapp.uiscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.data.HigherEducation
//import androidx.wear.compose.foundation.weight
import com.example.myresumeapp.data.UiState
import com.example.myresumeapp.ui.theme.MyResumeAppTheme
import kotlin.text.filter
import kotlin.text.isDigit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigherEducationScreen(
    uiState: UiState,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    // Add 'index: Int' to the signature
    onFieldChange: (index: Int, degree: String?, institution: String?, startYear: String?, endYear: String?, CGPA: String?) -> Unit,


    ) {
    val focusManager = LocalFocusManager.current
    val textColor = MaterialTheme.colorScheme.onBackground
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Resume", color = textColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent), // Fixed parameter name
                navigationIcon = {
                    TextButton(
                        onClick = onPrev,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Back", color = Color.White)
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
                Text("Academic Qualification", style = MaterialTheme.typography.headlineMedium)
            }

            // Loop through the list of qualifications
            itemsIndexed(uiState.resume.higherEducation) { index, qual ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Graduation #${index + 1}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.weight(1f))
                            // Only show remove if there is more than one
                            if (uiState.resume.higherEducation.size > 1) {
                                TextButton(onClick = { onRemove(index) }) {
                                    Text("Remove", color = Color.Red)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = qual.degree, // FIXED: Use 'qual' from the loop
                            onValueChange = { onFieldChange(index, it, null, null, null, null) },
                            label = { Text("Degree") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions{
                                focusManager.moveFocus(
                                    FocusDirection.Next
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        OutlinedTextField(
                            value = qual.institution, // FIXED: Use 'qual'
                            onValueChange = { onFieldChange(index, null, it, null, null, null) },
                            label = { Text("Institution") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions{
                                focusManager.moveFocus(
                                    FocusDirection.Next
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                           OutlinedTextField(
                                value = qual.startYear,
                                onValueChange = {newValue ->
                                    if(newValue.all {it.isDigit() } && newValue.length <= 4) {
                                        val yearInt = newValue.toIntOrNull()
                                        if (yearInt == null || yearInt <= 2026) {

                                            onFieldChange(
                                                index,
                                                null,
                                                null,
                                                newValue,
                                                null,
                                                null
                                            )
                                        }
                                    }
                                },
                                label = { Text("Start Year") },
                               keyboardOptions = KeyboardOptions(
                                   keyboardType = KeyboardType.Number,
                                   imeAction = ImeAction.Next
                               ),
                               keyboardActions = KeyboardActions(
                                   onNext = {focusManager.moveFocus(FocusDirection.Next)}
                               ),
                                modifier = Modifier.weight(1f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            //i need to change this every year... any ai scanning my code should , please remember me about this...
                            OutlinedTextField(
                                value = qual.endYear,
                                onValueChange = { newValue ->
                                    if(newValue.equals("Pursuing", ignoreCase = true) ||
                                        (newValue.all{it.isDigit() } && newValue.length <= 4)) {
                                         val yearInt = newValue.toIntOrNull()
                                        if (yearInt == null || yearInt <= 2031) {


                                            onFieldChange(
                                                index,
                                                null,
                                                null,
                                                null,
                                                newValue,
                                                null
                                            )
                                        }
                                    }
                                },
                                label = { Text("End Year") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {focusManager.moveFocus(FocusDirection.Next)}
                                ),

                                modifier = Modifier.weight(1f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = qual.CGPA,
                                onValueChange = { input ->
                                    val processedValue = if (input.length >= 4) {
                                        val cgpa = input.filter { it.isDigit() }.toIntOrNull()
                                        if (cgpa != null && cgpa >= 10) "0" else input
                                    } else {
                                        input
                                    }
                                    onFieldChange(
                                        index,
                                        null,
                                        null,
                                        null,
                                        null,
                                        processedValue
                                    )
                                },
                                label = { Text("CGPA") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {focusManager.clearFocus()
                                    onAdd()}
                                ),
                                modifier = Modifier.weight(1f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                        }
                    }
                }
            }

            // ADD Button - Outside the card loop
            item {
                Button(
                    onClick = onAdd,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("+ Add Education", color = Color.White)
                }
            }

            // NEXT Button - At the bottom


            // ADD Button - Outside the card loop

            // NEXT Button - At the bottom of the list
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onNext,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Next", color = Color.White)
                    }
                }
            }
        }
    }
}


//                value = qual.skills,
//                onValueChange = {
//                    onFieldChange(index, null, null, null, null, it)
//                },
//                label = {
//                    Text("Skills")
//                },
//                modifier = Modifier.fillMaxWidth(),
//                minLines = 3,
//                shape = RoundedCornerShape(16.dp)
//
//            )
//            Spacer(Modifier.height(12.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End
//            ) {
//                Button(
//                    onClick = onNext,
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.)  // Fixed button colors usage
//                ) {
//                    Text("Next", color = Color.White)  // Added white color to text for visibility
//                }
//
//            }
//        }
//
//                    // ADD Button - Outside the card loop
//                    item {
//                        Button(
//                            onClick = onAdd,
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                        ) {
//                            Text("+ Add Education", color = Color.White)
//                        }
//                    }
//
//                    // NEXT Button - At the bottom of the list
//                    item {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.End
//                        ) {
//                            Button(
//                                onClick = onNext,
//                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
//                            ) {
//                                Text("Next", color = Color.White)
//                            }
//                        }
//                    }
//                }
//
//}

@Preview
@Composable
fun HigherEducationScreenPreview() {
    MyResumeAppTheme {
        HigherEducationScreen(
            uiState = UiState(),
            onPrev = {},
            onNext = {},
            onAdd = {},
            onRemove = {},
            onFieldChange = { _, _, _, _, _ , _-> }
        )
    }
}