package com.example.myresumeapp.feature.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.components.ResumeCard
import com.example.myresumeapp.components.ResumeTopAppBar
//import androidx.wear.compose.foundation.weight
import com.example.myresumeapp.data.model.UiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceScreen(
    uiState: UiState,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    // Add 'index: Int' to the signature
    onFieldChange: (index: Int, company: String?, role: String?, startMonthYear: String?, endMonthYear: String?, responsibilites: String?) -> Unit
) {


    val textColor = MaterialTheme.colorScheme.onBackground
    val focusManager = LocalFocusManager.current
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )
    var showLivePreview by remember {mutableStateOf(false)}
    Scaffold(
        topBar = {
            ResumeTopAppBar(
                title = "Experience",
                onBack = onPrev,
                onPreviewClick = {showLivePreview = true}
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
                Text("Experience", style = MaterialTheme.typography.headlineMedium)
            }

            // Loop through the list of qualifications
            itemsIndexed(uiState.resume.experience) { index, exp ->
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
                                "Experience #${index + 1}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.weight(1f))
                            // Only show remove if there is more than one
                            if (uiState.resume.experience.size > 1) {
                                TextButton(onClick = { onRemove(index) }) {
                                    Text("Remove", color = Color.Red)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = exp.company, // FIXED: Use 'qual' from the loop
                            onValueChange = { onFieldChange(index, it, null, null, null, null) },
                            label = { Text("Company") },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            ),

                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Next) }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        OutlinedTextField(
                            value = exp.role, // FIXED: Use 'qual'
                            onValueChange = { onFieldChange(index, null, it, null, null, null) },
                            label = { Text("Role") },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            ),

                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Next) }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Start Date Field with Calendar
                            DateFieldWithPicker(
                                label = "Start Date",
                                value = exp.startMonthYear,
                                onValueChange = { onFieldChange(index, null, null, it, null, null) },
                                textFieldColors = textFieldColors,
                                modifier = Modifier.weight(1f)
                            )

                            // End Date Field with Calendar
                            DateFieldWithPicker(
                                label = "End Date",
                                value = exp.endMonthYear,
                                onValueChange = { onFieldChange(index, null, null, null, it, null) },
                                textFieldColors = textFieldColors,
                                modifier = Modifier.weight(1f)
                            )
                        }

                            OutlinedTextField(
                                value = exp.responsibilities,
                                onValueChange = {
                                    onFieldChange(
                                        index,
                                        null,
                                        null,
                                        null,
                                        null,
                                        it
                                    )
                                },
                                label = { Text("Responsibilities") },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),

                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )

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
                    Text("+ Add Experience", color = Color.White)
                }
            }

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

        if(showLivePreview) {
            ModalBottomSheet(
                onDismissRequest = {
                    showLivePreview = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Live Resume Preview",style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))

                    ResumeCard(
                        resume = uiState.resume,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFieldWithPicker(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    textFieldColors: TextFieldColors,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val focusManager = LocalFocusManager.current

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        val formatter =
                            SimpleDateFormat("MMM yyyy", Locale.getDefault())
                        onValueChange(formatter.format(Date(selectedDate)))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),

        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) }
        ),
        modifier = modifier,
        colors = textFieldColors,
        shape = RoundedCornerShape(16.dp),
        readOnly = true, // Force user to use the calendar for consistency
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date"
                )
            }
        }
    )
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