package com.example.myresumeapp.core.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.glance.layout.Column
import com.example.myresumeapp.data.model.ResumeData
import com.example.myresumeapp.navigation.Routes
import com.itextpdf.text.Section

data class AtsSuggestion(
    val title: String,
    val description: String,
    val potentialPoints: Int,
    val targetRoute: String
)

data class SectionScore(
    val sectionName: String,
    val score: Int,
    val maxScore: Int
)

data class AtsScoreResult(
    val overallScore: Int,
    val grade: String,
    val sectionScores: List<SectionScore>,
    val suggestions: List<AtsSuggestion>
)

object AtsScoreAnalyser{

    private val ACTION_VERBS = setOf(
        "developed", "managed", "designed", "built", "implemented", "engineered",
        "spearheaded", "led", "created", "improved", "optimized", "architected",
        "delivered", "launched", "executed", "formulated", "headed", "orchestrated"
    )

    fun analyze(resume: ResumeData): AtsScoreResult {
        val sectionScores = mutableListOf<SectionScore>()
        val suggestions = mutableListOf<AtsSuggestion>()

        var personalScore = 0
        val p = resume.personal
        if(p.name.isNotBlank()) personalScore += 5 else {
            suggestions.add(
                AtsSuggestion(
                    title = "Add full name",
                    description = "A complete full name is mandatory for ATS parsers.",
                    potentialPoints = 5,
                    targetRoute = Routes.PERSONAL
                )
            )
        }

        if (p.email.isNotBlank() && p.email.contains("@")) personalScore += 5 else {
            suggestions.add(
                AtsSuggestion(
                    title = "Add Valid Email",
                    description = "Recruiter and ATS systems require a valid email address.",
                    potentialPoints = 5,
                    targetRoute = Routes.PERSONAL
                )
            )
        }

        if (p.phone.isNotBlank()) personalScore += 5 else {
            suggestions.add(
                AtsSuggestion(
                    title = "Add Contact Number",
                    description = "Phone numbers are essential for recruiter callbacks.",
                    potentialPoints = 5,
                    targetRoute = Routes.PERSONAL
                )
            )
        }

        if (p.location.isNotBlank()) personalScore += 5 else {
            suggestions.add(
                AtsSuggestion(
                    title = "Add Location",
                    description = "Location (City, Country) helps ATS filter local candidates.",
                    potentialPoints = 5,
                    targetRoute = Routes.PERSONAL
                )
            )
        }

        if (p.linkedinlink.isNotBlank() || p.githublink.isNotBlank()) personalScore += 5 else {
            suggestions.add(
                AtsSuggestion(
                    title = "Add LinkedIn or GitHub Link",
                    description = "Professional links boost recruiter trust and verification.",
                    potentialPoints = 5,
                    targetRoute = Routes.PERSONAL
                )
            )
        }
        sectionScores.add(SectionScore("Personal Details", personalScore, 25))

        var careerScore = 0
        val careerDesc = resume.career.careerDescription.trim()
        val wordCount = if (careerDesc.isBlank()) 0 else careerDesc.split("\\s+".toRegex()).size

        if(careerDesc.isNotBlank()) {
            careerScore += 5
            if (wordCount >= 25) careerScore += 5 else {
                suggestions.add(
                    AtsSuggestion(
                        title = "Expand Career Objective",
                        description = "Aim for at least 25-50 words outlining your core strengths.",
                        potentialPoints = 5,
                        targetRoute = Routes.CAREER
                    )
                )
            }
            val hasActionVerb = careerDesc.split("\\s+".toRegex()).any { it in ACTION_VERBS}
            if(hasActionVerb) careerScore += 5 else {
                suggestions.add(
                    AtsSuggestion(
                        title = "Use Strong Action Verbs in Objective",
                        description = "Include verbs like 'engineered', 'spearheaded', or 'managed'.",
                        potentialPoints = 5,
                        targetRoute = Routes.CAREER
                    )
                )
            }
        } else {
            suggestions.add(
                AtsSuggestion(
                    title = "Write a Career Objective",
                    description = "A compelling summary grabs recruiter attention immediately.",
                    potentialPoints = 15,
                    targetRoute = Routes.CAREER
                )
                )
        }
        sectionScores.add(SectionScore("Career Summary", careerScore, 15))

        var expScore = 0
        val validExperiences = resume.experience.filter { it.company.isNotBlank() || it.role.isNotBlank() }
        if (validExperiences.isNotEmpty()) {
            expScore += 10
            val hasDetailedResp = validExperiences.any { exp ->
                    val resp = exp.responsibilities.trim()
                    val respWords = if (resp.isBlank()) 0 else resp.split("\\s+".toRegex()).size
                    val hasMetrics = resp.contains(Regex("\\d+|%|\\$"))
                    respWords >= 15 || hasMetrics
                }
            if (hasDetailedResp) {
                    expScore += 10
            } else {
                suggestions.add(
                    AtsSuggestion(
                    title = "Add Measurable Metrics in Experience",
                    description = "Include percentages (%), numbers, or detailed responsibilities.",
                    potentialPoints = 10,
                    targetRoute = Routes.EXPERIENCE
                )
                )
            }
            } else {
                suggestions.add(
                    AtsSuggestion(
                        title = "Add Work Experience",
                        description =  "List your work roles, internships, or relevant work experience.",
                        20,
                        Routes.EXPERIENCE
                    )
                )
            }

            sectionScores.add(SectionScore("Experience", expScore, 20 ))

        // 4. Education (Max 15 Pts)
        var eduScore = 0
        val validHigherEdu = resume.higherEducation.filter { it.degree.isNotBlank() || it.institution.isNotBlank() }
        val has10th = resume.school10Education.school10.isNotBlank()
        val has12th = resume.school12Education.school12.isNotBlank()

        if (validHigherEdu.isNotEmpty()) eduScore += 10
        if (has10th || has12th) eduScore += 5

        if (validHigherEdu.isEmpty() && !has10th && !has12th) {
            suggestions.add(
                AtsSuggestion(
                    title = "Add Educational Background",
                    description = "Specify your degree, college, or school education.",
                    potentialPoints = 15,
                    targetRoute = Routes.HIGHEREDUCATION
                )
            )
        } else if (validHigherEdu.isEmpty()) {
            suggestions.add(
                AtsSuggestion(
                    title = "Add Higher Education Degree",
                    description = "Include your college degree, university name, or specialization.",
                    potentialPoints = 10,
                    targetRoute = Routes.HIGHEREDUCATION
                )
            )
        }
        sectionScores.add(SectionScore("Education", eduScore, 15))

        var skillScore = 0
        val allSkills = (resume.skills.map { it.skill } +
                resume.skills2.map { it.skill2 } +
                resume.skills3.map { it.skill3 })
            .filter { it.isNotBlank() }
            .distinct()

        if (allSkills.size >= 5) {
            skillScore = 15
        } else if (allSkills.size >= 3) {
            skillScore = 10
            suggestions.add(
                AtsSuggestion(
                    title = "Add More Skills",
                    description = "Aim for at least 5 technical/soft skills to match ATS keywords.",
                    potentialPoints = 5,
                    targetRoute = Routes.SKILLS
                )
            )
        } else {
            skillScore = if (allSkills.isNotEmpty()) 5 else 0
            suggestions.add(
                AtsSuggestion(
                    title = "Add Key Skills",
                    description = "ATS filters rely heavily on technical and domain skills.",
                    potentialPoints = 15 - skillScore,
                    targetRoute = Routes.SKILLS
                )
            )
        }
        sectionScores.add(SectionScore("Skills", skillScore, 15))

        // 6. Projects & Certifications (Max 10 Pts)
        var projCertScore = 0
        val validProjects = resume.projects.filter { it.projectName.isNotBlank() }
        val validCerts = resume.certificate.filter { it.topic.isNotBlank() || it.association.isNotBlank() }
        val validAchievements = resume.achievements.filter { it.achievements.isNotBlank() }

        if (validProjects.isNotEmpty()) projCertScore += 5 else {
            suggestions.add(
                AtsSuggestion(
                    title = "Add a Project",
                    description = "Highlight key projects with technologies used and achievements.",
                    potentialPoints = 5,
                    targetRoute = Routes.PROJECT
                )
            )
        }

        if (validCerts.isNotEmpty() || validAchievements.isNotEmpty()) projCertScore += 5 else {
            suggestions.add(
                AtsSuggestion(
                    title = "Add Certificates or Achievements",
                    description = "Include relevant certifications to stand out to recruiters.",
                    potentialPoints = 5,
                    targetRoute = Routes.CERTIFICATES
                )
            )
        }
        sectionScores.add(SectionScore("Projects & Certifications", projCertScore, 10))

        val overallScore = sectionScores.sumOf { it.score }.coerceIn(0, 100)
        val grade = when {
            overallScore >= 85 -> "Excellent"
            overallScore >= 70 -> "Good"
            overallScore >= 50 -> "Needs Improvement"
            else -> "Incomplete"
        }

        return AtsScoreResult(
            overallScore = overallScore,
            grade = grade,
            sectionScores = sectionScores,
            suggestions = suggestions.sortedByDescending { it.potentialPoints }
        )
    }
}