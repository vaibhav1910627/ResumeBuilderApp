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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.data.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificateScreen(
    uiState: UiState,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    // Add 'index: Int' to the signature
    onFieldChange: (index: Int, association: String?, monthYear: String?, topic: String?) -> Unit,


    ) {

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
                Text("Certificates", style = MaterialTheme.typography.headlineMedium)
            }

            // Loop through the list of qualifications
            itemsIndexed(uiState.resume.certificate) { index, cert ->
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
                                "Certificate #${index + 1}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.weight(1f))
                            // Only show remove if there is more than one
                            if (uiState.resume.certificate.size > 1) {
                                TextButton(onClick = { onRemove(index) }) {
                                    Text("Remove", color = Color.Red)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = cert.association, // FIXED: Use 'qual' from the loop
                            onValueChange = { onFieldChange(index, it, null, null) },
                            label = { Text("Association") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        DateFieldWithPicker(
                            label = "Month",
                            value = cert.monthYear,
                            onValueChange = { onFieldChange(index, null, it, null) },
                            textFieldColors = textFieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )


                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = cert.topic,
                                onValueChange = {
                                    onFieldChange(
                                        index,
                                        null,
                                        null,
                                        it
                                    )
                                },
                                label = { Text("Start Year") },
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
                    Text("+ Add Project", color = Color.White)
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


//
//item {
//    Button(
//        onClick = {
//            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//        },
//        colors = ButtonDefaults.buttonColors(Color.Gray)
//    ) {
//        Text(if (uiState.localPhotoUri == null) "Choose Photo" else "Change Photo", color = Color.White)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DateFieldWithPicker(
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//    textFieldColors: TextFieldColors,
//    modifier: Modifier = Modifier
//) {
//    var showDatePicker by remember { mutableStateOf(false) }
//    val datePickerState = rememberDatePickerState()
//
//    // Date Picker Dialog
//    if (showDatePicker) {
//        DatePickerDialog(
//            onDismissRequest = { showDatePicker = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    val selectedDate = datePickerState.selectedDateMillis
//                    if (selectedDate != null) {
//                        val formatter =
//                            java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault())
//                        onValueChange(formatter.format(java.util.Date(selectedDate)))
//                    }
//                    showDatePicker = false
//                }) { Text("OK") }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
//            }
//        ) {
//            DatePicker(state = datePickerState)
//        }
//    }
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(label) },
//        modifier = modifier,
//        colors = textFieldColors,
//        shape = RoundedCornerShape(16.dp),
//        readOnly = true, // Force user to use the calendar for consistency
//        trailingIcon = {
//            IconButton(onClick = { showDatePicker = true }) {
//                Icon(
//                    imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
//                    contentDescription = "Select Date"
//                )
//            }
//        }
//    )
//}
