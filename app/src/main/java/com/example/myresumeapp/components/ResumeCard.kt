package com.example.myresumeapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myresumeapp.data.ResumeData

@Composable
fun ResumeCard(resume: ResumeData,modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
//                AsyncImage(
//                    model = resume.personal.photoUrl.ifEmpty { null },
//                    contentDescription = "Profile Photo",
//                    modifier = Modifier.size(96.dp),
//                    contentScale = ContentScale.Crop
//                )

            Text(
                resume.personal.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${resume.personal.email}*${resume.personal.phone}*${resume.personal.location}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "${resume.personal.githublink}*${resume.personal.linkedinlink}",
                style = MaterialTheme.typography.bodyMedium
            )


            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            Text(
                "Qualification",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            resume.higherEducation.forEach { qualification ->
                Spacer(Modifier.height(8.dp))

                // Row to handle Degree/Institution and the Date Range
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    // Left side: Degree and Institution
                    Text(
                        text = "${qualification.degree} . ${qualification.institution}",
                        modifier = Modifier.weight(1f), // Allow this to take space but not push the date off screen
                        style = MaterialTheme.typography.bodyLarge
                    )

                    // Right side: Date Range
                    Text(
                        text = "${qualification.startYear} - ${qualification.endYear}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray // Optional: makes the date look cleaner
                    )
                }
            }
//            if (resume.skills.skill.isNotBlank()) {
//                Spacer(Modifier.height(12.dp))
//                HorizontalDivider()
//                Text("Skills", style = MaterialTheme.typography.titleMedium)
//                Text(resume.skills.skill)
//            }
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))


        Text(
            "Experience",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        resume.experience.forEach { exp ->
            Text("${exp.company} * ${exp.startMonthYear}-${exp.endMonthYear}")
            Text(exp.role)
            Text(exp.responsibilities.ifBlank { "Present" })
        }
    }

}