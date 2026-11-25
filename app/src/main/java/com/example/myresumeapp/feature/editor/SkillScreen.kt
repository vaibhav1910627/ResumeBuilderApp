package com.example.myresumeapp.feature.editor

//import androidx.wear.compose.foundation.weight
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.components.ResumeCard
import com.example.myresumeapp.components.ResumeTopAppBar
import com.example.myresumeapp.core.theme.MyResumeAppTheme
import com.example.myresumeapp.data.model.Skills
import com.example.myresumeapp.data.model.UiState
import com.google.gson.annotations.Expose

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SmartDomainSkillPicker(
    skillsList: List<Skills>,
    onAddSkill: (String) -> Unit,
    onRemoveSkill: (Int) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var selectedDomain by remember { mutableStateOf("Android") }
    var expanded by remember { mutableStateOf(false) }
    val domainPresets = mapOf(

        "Android 📱" to listOf(
            "Kotlin",
            "Jetpack Compose",
            "Android SDK",
            "Coroutines",
            "Dagger Hilt",
            "Room DB",
            "Retrofit",
            "MVVM",
            "Clean Architecture",
            "Material 3"
        ),
        "Web 🌐" to listOf(
            "HTML5",
            "CSS3",
            "JavaScript",
            "TypeScript",
            "React.js",
            "Node.js",
            "Next.js",
            "Tailwind CSS",
            "Vue.js",
            "REST APIs"
        ),
        "Backend ⚙️" to listOf(
            "Java",
            "Python",
            "Spring Boot",
            "PostgreSQL",
            "MongoDB",
            "Docker",
            "Kubernetes",
            "AWS",
            "Firebase",
            "GraphQL"
        ),
        "AI & Data 🧠" to listOf(
            "Python",
            "R",
            "SQL",
            "TensorFlow",
            "PyTorch",
            "Pandas",
            "NumPy",
            "Scikit-Learn",
            "Machine Learning"
        ),
        "UI/UX 🎨" to listOf(
            "Figma",
            "Adobe XD",
            "Wireframing",
            "Prototyping",
            "User Research",
            "Material Design",
            "Design Systems"
        ),
        "Tools & DevOps 🛠️" to listOf(
            "Git",
            "GitHub Actions",
            "CI/CD",
            "JUnit",
            "MockK",
            "Linux",
            "Docker",
            "Postman",
            "Nginx"
        ),
        "Soft Skills 💼" to listOf(
            "Problem Solving",
            "Agile/Scrum",
            "Communication",
            "Leadership",
            "Team Collaboration",
            "Time Management"
        )
    )

    val masterSkillsList = remember { domainPresets.values.flatten().distinct() }
    var filteredSuggestions = masterSkillsList.filter {
        it.contains(textInput, true) &&
                !skillsList.any { s -> s.skill.equals(it, ignoreCase = true) }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "Custom Skill Input:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        val showAddHint = textInput.isNotBlank() && filteredSuggestions.isEmpty()
        AnimatedVisibility(
            visible = textInput.isNotBlank() && filteredSuggestions.isEmpty(),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End // 👈 Aligns box to the right above '+'
            ) {
                Surface(
                    color = Color(0xFF2E7D32).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 2.dp //
                    ),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            "💡 No matching skills found? Click '+' to add \"${textInput.trim()}\" as a custom skill!",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }

            }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded && filteredSuggestions.isNotEmpty() && textInput.isNotBlank(),
                onExpandedChange = { expanded = it },
                modifier = Modifier.weight(1f),

            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = {
                        textInput = it
                        expanded = true
                    },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text("Search Skill") },
                    placeholder = {Text("e.g., Solidity ⛓️, Ktor, Figma") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (textInput.isNotBlank()) {
                                onAddSkill(textInput.trim())
                                textInput = ""
                                expanded = false
                            }
                        }
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded && filteredSuggestions.isNotEmpty() && textInput.isNotBlank(),
                    onDismissRequest = { expanded = false },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f),
                    modifier = Modifier
                        .border(
                            width = 1.5.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 6.dp)

                ) {
                    filteredSuggestions.take(6).forEach { suggestion ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    suggestion,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onAddSkill(suggestion)
                                textInput = ""
                                expanded = false
                            },
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                    }
                }
            }
            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        onAddSkill(textInput.trim())
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Custom Skill",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }


            }
        // 2. Domain Filter Chips Row
        Text(
            "Or Choose Domain Presets:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        FlowRow(
//                modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            domainPresets.keys.forEach { domain ->
                FilterChip(
                    selected = selectedDomain == domain,
                    onClick = { selectedDomain = domain },
                    label = { Text(domain) }
                )
            }
        }

        val currentPresets = domainPresets[selectedDomain] ?: emptyList()

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            currentPresets.forEach { presetSkill ->
                val isAlreadyAdded =
                    skillsList.any { it.skill.equals(presetSkill, ignoreCase = true) }
                FilterChip(
                    selected = isAlreadyAdded,
                    onClick = {
                        if (!isAlreadyAdded) {
                            onAddSkill(presetSkill)
                        }
                    },
                    label = { Text(presetSkill) },
                    leadingIcon = if (isAlreadyAdded) {
                        {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else null
                )
            }
        }

        // Selected Skills Chip Tags
        if (skillsList.isNotEmpty() && skillsList.any { it.skill.isNotBlank() }) {
            Text(
                "Selected Resume Skills (${skillsList.filter { it.skill.isNotBlank() }.size}):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                skillsList.forEachIndexed { index, skl ->
                    if (skl.skill.isNotBlank()) {
                        InputChip(
                            selected = false,
                            onClick = {},
                            label = { Text(skl.skill) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { onRemoveSkill(index) }
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
    onAdd: (String) -> Unit,
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
    var showLivePreview by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            ResumeTopAppBar(
                title = "Skill",
                onBack = onPrev,
                onPreviewClick = { showLivePreview = true }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Skills", style = MaterialTheme.typography.headlineMedium)
            }

            // Loop through the list of qualifications
            item {
                SmartDomainSkillPicker(
                    uiState.resume.skills,
                    onAddSkill = { newSkill ->
                        onAdd(newSkill)
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

        if (showLivePreview) {
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
                    Text("Live Resume Preview", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))

                    ResumeCard(
                        resume = uiState.resume,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

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
    }
}

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