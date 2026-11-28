package com.example.myresumeapp.core.utils

import com.example.myresumeapp.data.model.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class AtsScoreAnalyserTest {
    @Test
    fun `analyze returns zero score and Incomplete grade for empty resume`() {
        val emptyResume = ResumeData()
        val result = AtsScoreAnalyser.analyze(emptyResume)

        assertEquals(0, result.overallScore)
        assertEquals("Incomplete", result.grade)
        assertTrue(result.suggestions.isNotEmpty())
    }

    @Test
    fun `analyze returns high score and Excellent grade for fully filled resume`() {
        val completeResume = ResumeData(
            personal = PersonalDetails(
                name = "John Doe",
                email = "john@example.com",
                phone = "1234567890",
                location = "New York, NY",
                linkedinlink = "https://linkedin.com/in/johndoe"

        ),
        career = CareerObjective(
            careerDescription = "Engineered and developed high-performance mobile applications with Kotlin, Jetpack Compose, and Clean Architecture principles, delivering robust software solutions."
        ),
            experience = listOf(
                Experience(
                    company = "Tech Corp",
                    role = "Android Engineer",
                    responsibilities = "Developed 5 core features, improving app performace by 35% and reducing crashes."

                )
            ),
            higherEducation = listOf(
                HigherEducation(
                    degree = "B.Tech in Computer Science",
                    institution = "University"
                )
            ),
            skills = listOf(
                Skills("Kotlin"),
                Skills("Jetpack Compose"),
                Skills("Android SDK"),
                Skills("Coroutines"),
                Skills("Room DB")
            ),
            projects = listOf(
                Project(
                    projectName = "Resume Builder",
                    description = "Built a native Android app with MVVM."
                )
            ),
            certificate = listOf(
                Certificates(
                    topic = "Android Developer",
                    association = "Google"
                )
            )
        )

        val result = AtsScoreAnalyser.analyze(completeResume)
        assertTrue("Expected score >= 85, but got ${result.overallScore}", result.overallScore >= 85)
        assertEquals("Excellent", result.grade)
    }
}