package com.example.myresumeapp.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.data.Skills
//import androidx.wear.compose.foundation.weight
import com.example.myresumeapp.data.UiState
import com.example.myresumeapp.ui.theme.MyResumeAppTheme
import kotlin.text.filter
import kotlin.text.isDigit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSkillInputSection(
    skillsList: List<Skills>,
    onAddSkill: (String) -> Unit,
    onRemoveSkill: (Int) -> Unit
) {
    var textInput by remember {mutableStateOf("")}
    var expanded by remember {mutableStateOf(false)}

    val predefinedSkills = listOf("Kotlin", "Java", "Jetpack Compose", "Android SDK", "Firebase",
        "Python", "JavaScript", "TypeScript", "React", "SQL", "Git", "C++", "HTML", "CSS", "Node.js", "Flutter", "C#"
    )
    val filteredSkills = predefinedSkills.filter {
        it.contains(textInput, ignoreCase = true) &&
                !skillsList.any { s ->
                    s.skill.equals(it, ignoreCase = true)
                }
    }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded && filteredSkills.isNotEmpty() && textInput.isNotBlank(),
                    onExpandedChange = {expanded = it},
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = {
                            textInput = it
                            expanded = true
                        },
                        label = {Text("Add a skill (e.g. Python 🐍)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded && filteredSkills.isNotEmpty()
                                && textInput.isNotBlank(),
                        onDismissRequest = {expanded = false}
                    ) {
                        filteredSkills.forEach {
                            suggestion ->
                            DropdownMenuItem(
                                text = {Text(suggestion) },
                                onClick = {
                                    textInput = suggestion
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            onAddSkill(textInput.trim())
                            textInput = ""
                            expanded = false
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Skill",
                        tint = MaterialTheme.colorScheme.onPrimary

                    )
            }
        }
            if(skillsList.isNotEmpty() && skillsList.any {it.skill.isNotBlank()}) {
                FlowRow(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    skillsList.forEachIndexed { index, skl ->
                        if(skl.skill.isNotBlank()) {
                            InputChip(
                                selected =  false,
                                onClick = {},
                                label = {Text(skl.skill)},
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Skill",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {onRemoveSkill(index)}
                                    )
                                }
                            )
                        }
                    }
                }
            }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillScreen(
    uiState: UiState,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    // Add 'index: Int' to the signature
    onFieldChange: (index: Int, skills: String?) -> Unit,


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
                Text("Skills", style = MaterialTheme.typography.headlineMedium)
            }

            // Loop through the list of qualifications
            item {
                SmartSkillInputSection(
                    uiState.resume.skills,
                    onAddSkill = { newSkill ->
                        onAdd()
                    },
                    onRemoveSkill = { index ->
                        onRemove(index)
                    })
//            }{ index, skl ->
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        elevation = CardDefaults.cardElevation(4.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp),
//                            verticalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Text(
//                                    "Skill #${index + 1}",
//                                    style = MaterialTheme.typography.titleMedium
//                                )
//                                Spacer(Modifier.weight(1f))
//                                // Only show remove if there is more than one
//                                if (uiState.resume.skills.size > 1) {
//                                    TextButton(onClick = { onRemove(index) }) {
//                                        Text("Remove", color = Color.Red)
//                                    }
//                                }
            }

//                            OutlinedTextField(
//                                value = skl.skill, // FIXED: Use 'qual' from the loop
//                                onValueChange = { onFieldChange(index, it) },
//                                label = { Text("Skill") },
//                                modifier = Modifier.fillMaxWidth(),
//                                shape = RoundedCornerShape(16.dp)
//                            )
//
//
//                        }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onNext,
                        colors = ButtonDefaults.buttonColors(contentColor = Color.Gray)
                    ) {
                        Text("Next", color = Color.White)
                    }

                }
            }
        }


//            // ADD Button - Outside the card loop
//            item {
//                Button(
//                    onClick = onAdd,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                ) {
//                    Text("+ Add Skill", color = Color.White)
//                }
//            }

        // NEXT Button - At the bottom


        // ADD Button - Outside the card loop

        // NEXT Button - At the bottom of the list
//            item {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    Button(
//                        onClick = onNext,
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                    ) {
//                        Text("Next", color = Color.White)
//                    }
//                }
//            }
//        }
//    }
//}
    }}

@Preview(showBackground = true)
@Composable
fun SkillScreenPreview() {
    MyResumeAppTheme {
        SkillScreen(
            uiState = UiState(),
            onPrev = {},
            onNext = {},
            onAdd = {},
            onRemove = {},
            onFieldChange = { _, _ -> }
        )
    }
}