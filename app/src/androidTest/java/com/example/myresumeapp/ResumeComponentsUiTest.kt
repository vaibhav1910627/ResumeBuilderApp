package com.example.myresumeapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myresumeapp.components.AtsScoreBottomSheet
import com.example.myresumeapp.components.ResumeCard
import com.example.myresumeapp.components.ResumeTopAppBar
import com.example.myresumeapp.core.theme.MyResumeAppTheme
import com.example.myresumeapp.core.utils.AtsScoreResult
import com.example.myresumeapp.core.utils.SectionScore
import com.example.myresumeapp.data.model.PersonalDetails
import com.example.myresumeapp.data.model.ResumeData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResumeComponentsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testResumeCardRendersUserData() {
        val sampleResume = ResumeData(
            personal = PersonalDetails(
                name = "Alex Vance",
                email = "alex.vance@example.com",
                phone = "+1 555-019-2834"
            )
        )

        composeTestRule.setContent {
            MyResumeAppTheme {
                ResumeCard(resume = sampleResume)
            }
        }

        composeTestRule.onNodeWithText("ALEX VANCE").assertIsDisplayed()
        composeTestRule.onNodeWithText("alex.vance@example.com • +1 555-019-2834", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun testAtsScoreBottomSheetDisplayScore() {
        val sampleAtsResult = AtsScoreResult(
            overallScore = 85,
            grade = "Excellent",
            sectionScores = listOf(
                SectionScore("Personal Details", 25, 25)

            ),
            suggestions = emptyList()
        )

        composeTestRule.setContent {
            MyResumeAppTheme {
                AtsScoreBottomSheet(
                atsResult = sampleAtsResult,
                onDismiss = {},
                onNavigateToSection = {}
                )
        }
    }

    composeTestRule.onNodeWithText("ATS Completeness Score").assertIsDisplayed()
    composeTestRule.onNodeWithText("85%").assertIsDisplayed()
    composeTestRule.onNodeWithText("Grade: Excellent").assertIsDisplayed()
}

@Test
fun testTopAppBarRendersTitleAndAtsChip() {
    composeTestRule.setContent {
        MyResumeAppTheme {
            ResumeTopAppBar(
                title = "Career Objective",
                atsScore = 90,
                onAtsClick = {}
            )
        }
    }

    composeTestRule.onNodeWithText("Career Objective").assertIsDisplayed()
    composeTestRule.onNodeWithText("ATS 90%").assertIsDisplayed()
}
}

