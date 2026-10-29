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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.data.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolEducation12Screen(
    uiState: UiState,
    onPrev: () -> Unit,
    onNext: () -> Unit,
//    onAdd: () -> Unit,
//    onRemove: (Int) -> Unit,
    // Add 'index: Int' to the signature

    onFieldChange: (school12: String?, board12: String?, startYear12: String?, endYear12: String?, percent12: String?) -> Unit

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
                Text("Education", style = MaterialTheme.typography.headlineMedium)
            }

            // Loop through the list of qualifications
            val edu2 = uiState.resume.school12Education
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            "Senior Secondary Education(Class 12)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.weight(1f))



                        OutlinedTextField(
                            value = edu2.school12 ,// FIXED: Use 'qual' from the loop
                            onValueChange = { onFieldChange( it, null, null, null, null) },
                            label = { Text("School Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        OutlinedTextField(
                            value = edu2.board12, // FIXED: Use 'qual'
                            onValueChange = { onFieldChange(null, it, null, null, null) },
                            label = { Text("Board") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = edu2.startYear12,
                                onValueChange = { input ->
                                    val processedValue = if (input.length >= 4) {
                                        val year = input.filter { it.isDigit() }.toIntOrNull()
                                        if (year != null && year > 2025) "0" else input
                                    } else {
                                        input
                                    }
                                    onFieldChange(
                                        null,
                                        null,
                                        processedValue,
                                        null,
                                        null
                                    )
                                },
                                label = { Text("Start Year") },
                                modifier = Modifier.weight(1f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            OutlinedTextField(
                                value = edu2.endYear12,
                                onValueChange = { input ->
                                    val processedValue = if (input.length >= 4) {
                                        val year = input.filter { it.isDigit() }.toIntOrNull()
                                        if (year != null && year >= 2025) "Pursuing" else input
                                    } else {
                                        input
                                    }
                                    onFieldChange(
                                        null,
                                        null,
                                        null,
                                        processedValue, null
                                    )
                                },
                                label = { Text("End Year") },
                                modifier = Modifier.weight(1f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = edu2.percentage12,
                                onValueChange = { input ->
                                    val processedValue = if (input.length >= 4) {
                                        val percentageedu = input.filter { it.isDigit() }.toIntOrNull()
                                        if (percentageedu != null && percentageedu > 100) "0" else input
                                    } else {
                                        input
                                    }
                                    onFieldChange(
                                        null,
                                        null,
                                        null,
                                        null,
                                        processedValue
                                    )
                                },
                                label = { Text("Percentage") },
                                modifier = Modifier.weight(1f),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )

                        }
                    }
                }
            }



            // NEXT Button - At the bottom
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
