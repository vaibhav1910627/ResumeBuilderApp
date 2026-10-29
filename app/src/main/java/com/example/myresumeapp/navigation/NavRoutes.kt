package com.example.myresumeapp.navigation

import AchievementScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myresumeapp.data.Achievements
import com.example.myresumeapp.data.CareerObjective
import com.example.myresumeapp.uiscreens.CareerObjectiveScreen
import com.example.myresumeapp.uiscreens.CertificateScreen
import com.example.myresumeapp.uiscreens.ExperienceScreen
import com.example.myresumeapp.uiscreens.HigherEducationScreen
import com.example.myresumeapp.uiscreens.LanguageScreen
import com.example.myresumeapp.uiscreens.LoginScreen
import com.example.myresumeapp.uiscreens.PersonalScreen
import com.example.myresumeapp.uiscreens.PreviewScreen
import com.example.myresumeapp.uiscreens.ProjectScreen
import com.example.myresumeapp.uiscreens.SchoolEducation10Screen
import com.example.myresumeapp.uiscreens.SchoolEducation12Screen
import com.example.myresumeapp.uiscreens.SignupScreen
import com.example.myresumeapp.uiscreens.SkillScreen
import com.example.myresumeapp.viewmodel.ResumeViewModel

object Routes {
    const val SIGNUP = "signup"

    const val LOGIN = "login"
    const val PERSONAL = "personal"

    const val CAREER = "CAREER"

    const val SCHOOL10EDUCATION = "school10Education"

    const val SCHOOL12EDUCATION = "school12Education"

    const val HIGHEREDUCATION = "higherEducation"

    const val SKILLS = "skills"
    const val EXPERIENCE = "experience"

    const val LANGUAGE = "language"
    const val PROJECT  = "project"
    const val CERTIFICATES = "certificates"
    const val ACHIEVEMENTS = "achievements"

    const val PREVIEW = "PREVIEW"
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val resumeViewModel: ResumeViewModel = viewModel()
    NavHost(navController, startDestination = Routes.SIGNUP) {
        composable(Routes.SIGNUP) {
            SignupScreen(
                onSignupSuccess = { navController.navigate(Routes.PERSONAL) },
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onGuestLogin = {navController.navigate(Routes.PERSONAL)}
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.PERSONAL) },
                onNavigateToSignup = { navController.navigate(Routes.SIGNUP) },
                onGuestLogin = {navController.navigate(Routes.PERSONAL)}
            )
        }

        composable(Routes.PERSONAL) {
            PersonalScreen(
                uiState = resumeViewModel.uiState,
                onNext = {navController.navigate(Routes.SKILLS)},
                onPhotoPicked = resumeViewModel::setPhotoUri,
                onFieldChange = resumeViewModel::updatePersonal
            )
        }
        composable(Routes.CAREER) {
            CareerObjectiveScreen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.SKILLS)},
                onFieldChange = resumeViewModel::updateCareerObjective
            )
        }

        composable(Routes.SKILLS) {
            SkillScreen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.SCHOOL10EDUCATION)},
                onAdd = resumeViewModel::addSkill,
                onRemove = resumeViewModel::removeSkill,
                onFieldChange = resumeViewModel::updateSkill
            )
        }

        composable(Routes.SCHOOL10EDUCATION) {
            SchoolEducation10Screen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.SCHOOL12EDUCATION)},
                onFieldChange = resumeViewModel::updateSchool10Education
            )
        }
        composable(Routes.SCHOOL12EDUCATION) {
            SchoolEducation12Screen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.HIGHEREDUCATION)},
                onFieldChange = resumeViewModel::updateSchool12Education
            )
        }
        composable(Routes.HIGHEREDUCATION) {
            HigherEducationScreen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.EXPERIENCE)},
                onAdd = resumeViewModel::addHigherEducation,
                onRemove = resumeViewModel::removeHigherEducation,
                onFieldChange = resumeViewModel::updateHigherEducation
            )
        }

        composable(Routes.EXPERIENCE) {

            ExperienceScreen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.PROJECT)},
                onAdd = resumeViewModel::addExperience,
                onRemove = resumeViewModel::removeExperience,
                onFieldChange = resumeViewModel::updateExperience
            )

        }

        composable(Routes.PROJECT) {
            ProjectScreen(
                uiState = resumeViewModel.uiState,
                onPrev = { navController.popBackStack() },
                onNext = { navController.navigate(Routes.ACHIEVEMENTS) },
                onAdd = resumeViewModel::addProject,
                onRemove = resumeViewModel::removeProject,
                onFieldChange = resumeViewModel::updateProject
            )
        }

        composable(Routes.ACHIEVEMENTS) {
            AchievementScreen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.CERTIFICATES)},
                onAdd = resumeViewModel::addAchievement,
                onRemove = resumeViewModel::removeAchievement,
                onFieldChange = resumeViewModel::updateAchievement
            )
        }

        composable(Routes.CERTIFICATES) {
            CertificateScreen(
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.LANGUAGE)},
                onAdd = resumeViewModel::addCertificate,
                onRemove = resumeViewModel::removeCertificate,
                onFieldChange = resumeViewModel::updateCertificate
            )
        }

        composable(Routes.LANGUAGE) {
            LanguageScreen (
                uiState = resumeViewModel.uiState,
                onPrev = {navController.popBackStack()},
                onNext = {navController.navigate(Routes.PREVIEW)},
                onAdd = resumeViewModel::addLanguage,
                onRemove = resumeViewModel::removeLanguage,
                onFieldChange = resumeViewModel::updateLanguage
            )
        }
        composable(Routes.PREVIEW) {
            PreviewScreen(
                uiState = resumeViewModel.uiState,
                onEdit = {navController.popBackStack(Routes.PERSONAL, inclusive = false)},
                onDelete = {
                    resumeViewModel.deleteAll {
                        navController.popBackStack(Routes.PERSONAL, inclusive = false)
                        navController.navigate(Routes.PERSONAL)
                    }
                },
                onSaveDatabase = resumeViewModel::saveFirebase,
                onDownloadPDF = resumeViewModel::downloadPDF,
                onDownloadImage = resumeViewModel::downloadImage
            )
        }
    }
}