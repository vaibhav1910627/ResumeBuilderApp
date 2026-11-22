package com.example.myresumeapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myresumeapp.data.model.ResumeData

@Composable
fun ResumeCard(resume: ResumeData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//                AsyncImage(
//                    model = resume.personal.photoUrl.ifEmpty { null },
//                    contentDescription = "Profile Photo",
//                    modifier = Modifier.size(96.dp),
//                    contentScale = ContentScale.Crop
//                )
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (resume.personal.name.isNotBlank()) {

                    Text(
                        resume.personal.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                val contactInfo = listOfNotNull(
                    resume.personal.email.takeIf { it.isNotBlank() },
                    resume.personal.phone.takeIf { it.isNotBlank() },
                    resume.personal.location.takeIf { it.isNotBlank() }
                ).joinToString(" • ")

                if (contactInfo.isNotBlank()) {
                    Text(
                        text = contactInfo,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF424242),
                        textAlign = TextAlign.Center
                    )
                }

                val links = listOfNotNull(
                    resume.personal.githublink.takeIf { it.isNotBlank() },
                    resume.personal.linkedinlink.takeIf { it.isNotBlank() }
                ).joinToString(" • ")

                if (links.isNotBlank()) {
                    Text(
                        text = links,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1976D2),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFBDBDBD), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // 2. Career Objective
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                if (resume.career.careerDescription.isNotBlank()) {
                    ResumeSectionHeader("CAREER OBJECTIVE")
                    Text(
                        text = resume.career.careerDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF212121),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 3. Technical Skills
                val programmingLangs = resume.skills.map { it.skill }.filter { it.isNotBlank() }
                val frameworks = resume.skills2.map { it.skill2 }.filter { it.isNotBlank() }
                val toolsAndPlatforms = resume.skills3.map { it.skill3 }.filter { it.isNotBlank() }
                val spokenLangs = resume.language.filter { it.language.isNotBlank() }.map {
                    if (it.proficiency.isNotBlank()) "${it.language} (${it.proficiency})" else it.language
                }

                if (programmingLangs.isNotEmpty() || frameworks.isNotEmpty() || toolsAndPlatforms.isNotEmpty() || spokenLangs.isNotEmpty()) {
                    ResumeSectionHeader("TECHNICAL SKILLS & LANGUAGES")

                    if (programmingLangs.isNotEmpty()) {
                        SkillCategoryRow("Programming Languages", programmingLangs)
                    }
                    if (frameworks.isNotEmpty()) {
                        SkillCategoryRow("Frameworks & Libraries", frameworks)
                    }
                    if (toolsAndPlatforms.isNotEmpty()) {
                        SkillCategoryRow("Tools & Platforms", toolsAndPlatforms)
                    }
                    if (spokenLangs.isNotEmpty()) {
                        SkillCategoryRow("Languages Spoken", spokenLangs)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }


                // 4. Experience
                val validExp =
                    resume.experience.filter { it.company.isNotBlank() || it.role.isNotBlank() }
                if (validExp.isNotEmpty()) {
                    ResumeSectionHeader("WORK EXPERIENCE")
                    validExp.forEach { exp ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = exp.company,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = "${exp.startMonthYear} - ${exp.endMonthYear}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF616161)
                            )
                        }
                        if (exp.role.isNotBlank()) {
                            Text(
                                text = exp.role,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF424242)
                            )
                        }
                        if (exp.responsibilities.isNotBlank()) {
                            Text(
                                text = exp.responsibilities,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF212121)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 5. Higher Education
                val validEdu =
                    resume.higherEducation.filter { it.degree.isNotBlank() || it.institution.isNotBlank() }
                if (validEdu.isNotEmpty()) {
                    ResumeSectionHeader("EDUCATION")
                    validEdu.forEach { edu ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${edu.degree} - ${edu.institution}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${edu.startYear} - ${edu.endYear}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF616161)
                            )
                        }
                        if (edu.CGPA.isNotBlank()) {
                            Text(
                                text = "CGPA: ${edu.CGPA}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF424242)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
            // 6. Projects
            val validProjects = resume.projects.filter { it.projectName.isNotBlank() }
            if (validProjects.isNotEmpty()) {
                ResumeSectionHeader("PROJECTS")
                validProjects.forEach { proj ->
                    Text(
                        text = proj.projectName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    if (proj.technology.isNotBlank()) {
                        Text(
                            text = "Technologies: ${proj.technology}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF424242)
                        )
                    }
                    if (proj.description.isNotBlank()) {
                        Text(
                            text = proj.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF212121)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Certificates
            val validCerts = resume.certificate.filter { it.topic.isNotBlank() || it.association.isNotBlank() }
            if (validCerts.isNotEmpty()) {
                ResumeSectionHeader("CERTIFICATIONS")
                validCerts.forEach { cert ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "${cert.topic} (${cert.association})", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        if (cert.startMonthYear.isNotBlank()) Text(text = cert.startMonthYear, style = MaterialTheme.typography.bodySmall, color = Color(0xFF616161))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

        }
    }
}

@Composable
private fun ResumeSectionHeader(title: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2B3A55),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider(color = Color(0xFF2B3A55), thickness = 1.dp)
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
private fun SkillCategoryRow(domainName: String, skillsList: List<String>) {
    val annotatedString = buildAnnotatedString {
        // Dark Bold Domain Header
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))) {
            append("$domainName: ")
        }
        // Regular Skill Text
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color(0xFF424242))) {
            append(skillsList.joinToString(", "))
        }
    }
    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    )
}