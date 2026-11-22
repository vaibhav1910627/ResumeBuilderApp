package com.example.myresumeapp.navigation

import androidx.activity.compose.LocalActivityResultRegistryOwner.provides
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons.Default as Default
import com.example.myresumeapp.feature.editor.AchievementScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics

import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myresumeapp.components.AtsScoreBottomSheet
import com.example.myresumeapp.components.LocalAtsScore
import com.example.myresumeapp.components.LocalCoroutineScope
import com.example.myresumeapp.components.LocalDrawerState
import com.example.myresumeapp.components.LocalOnAtsClick
import com.example.myresumeapp.components.LocalStepProgress
import com.example.myresumeapp.feature.editor.CareerObjectiveScreen
import com.example.myresumeapp.feature.editor.CertificateScreen
import com.example.myresumeapp.feature.editor.ExperienceScreen
import com.example.myresumeapp.feature.editor.HigherEducationScreen
import com.example.myresumeapp.feature.editor.LanguageScreen
import com.example.myresumeapp.feature.auth.LoginScreen
import com.example.myresumeapp.feature.editor.PersonalScreen
import com.example.myresumeapp.feature.preview.PreviewScreen
import com.example.myresumeapp.feature.editor.ProjectScreen
import com.example.myresumeapp.feature.editor.SchoolEducation10Screen
import com.example.myresumeapp.feature.editor.SchoolEducation12Screen
import com.example.myresumeapp.feature.auth.SignupScreen
import com.example.myresumeapp.feature.editor.AchievementScreen
import com.example.myresumeapp.feature.editor.SkillScreen
import com.example.myresumeapp.feature.viewmodel.ResumeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

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
    const val PROJECT = "project"
    const val CERTIFICATES = "certificates"
    const val ACHIEVEMENTS = "achievements"

    const val PREVIEW = "PREVIEW"
}

val editorRoutes = listOf(
    Routes.PERSONAL,
    Routes.CAREER,
    Routes.SKILLS,
    Routes.SCHOOL10EDUCATION,
    Routes.SCHOOL12EDUCATION,
    Routes.HIGHEREDUCATION,
    Routes.EXPERIENCE,
    Routes.PROJECT,
    Routes.ACHIEVEMENTS,
    Routes.CERTIFICATES,
    Routes.LANGUAGE,
    Routes.PREVIEW
)

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val resumeViewModel: ResumeViewModel = hiltViewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //to be erased in future mb
    var showAtsSheet by remember { mutableStateOf(false) }
    val atsResult = resumeViewModel.atsScoreResult

    if (showAtsSheet) {
        com.example.myresumeapp.components.AtsScoreBottomSheet(
            atsResult = atsResult,
            onDismiss = { showAtsSheet = false },
            onNavigateToSection = { route ->
                showAtsSheet = false
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
//                    restoreState = true
                }
            }
        )
    }


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showDrawer = currentRoute != Routes.SIGNUP && currentRoute != Routes.LOGIN

    val drawerContent: @Composable () -> Unit = {
        ModalDrawerSheet {
            Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
            Text(
                "My resume Builder",
                modifier = androidx.compose.ui.Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

            Card(
                onClick = {
                    scope.launch { drawerState.close() }
                    showAtsSheet = true
                },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("ATS Completeness", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "${atsResult.overallScore}% • ${atsResult.grade}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(imageVector = Icons.Default.Analytics, contentDescription = "ATS Score")
                }
            }
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

            val navItems = listOf(
                Triple("Personal Details", Routes.PERSONAL, Default.Person),
                Triple("Career Objective", Routes.CAREER, Default.Star),
                Triple("Skills", Routes.SKILLS, Default.Build),
                Triple("Higher Education", Routes.HIGHEREDUCATION, Default.School),
                Triple("Experience", Routes.EXPERIENCE, Default.Work),
                Triple("Projects", Routes.PROJECT, Default.Code),
                Triple("Achievements", Routes.ACHIEVEMENTS, Default.CheckCircle),
                Triple("Certificates", Routes.CERTIFICATES, Default.Check),
                Triple("Languages", Routes.LANGUAGE, Default.Info),
                Triple("Preview & Export", Routes.PREVIEW, Default.Share)
            )

            navItems.forEach { (title, route, icon) ->
                val isSelected = currentRoute == route
                NavigationDrawerItem(
                    icon = {
                        Icon(icon, contentDescription = title)
                    },
                    label = { Text(title) },
                    selected = isSelected,
                    onClick = {
                        scope.launch { drawerState.close() }
                        if (!isSelected) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
//                                restoreState = true
                            }
                        }
                    },
                    modifier = androidx.compose.ui.Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

            }


        }
    }

    val currentStepIndex = editorRoutes.indexOf(currentRoute).coerceAtLeast(0)
    val currentProgress = (currentStepIndex + 1).toFloat() / editorRoutes.size
    if (showDrawer) {
        CompositionLocalProvider(
            LocalDrawerState provides drawerState,
            LocalCoroutineScope provides scope,
            LocalAtsScore provides atsResult.overallScore,
            LocalOnAtsClick provides {showAtsSheet = true},
            LocalStepProgress provides currentProgress
        ) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = drawerContent
            ) {
                ResumeNavHost(navController, resumeViewModel)
            }
        }
    } else {
        ResumeNavHost(navController, resumeViewModel)

    }


}

@Composable
fun ResumeNavHost(
    navController: NavHostController,
    resumeViewModel: ResumeViewModel
) {
    var showAtsSheet by remember { mutableStateOf(false) }
    val atsResult = resumeViewModel.atsScoreResult

    if (showAtsSheet) {
        AtsScoreBottomSheet(
            atsResult = atsResult,
            onDismiss = {
                showAtsSheet = false
            },
            onNavigateToSection = { route ->
                showAtsSheet = false
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
//                    restoreState = true
                }
            }
        )

    }
    CompositionLocalProvider(
        LocalAtsScore provides atsResult.overallScore,
        LocalOnAtsClick provides { showAtsSheet = true }
    ) {
        NavHost(navController, startDestination = Routes.SIGNUP) {
            composable(Routes.SIGNUP) {
                SignupScreen(
                    onSignupSuccess = { navController.navigate(Routes.PERSONAL) },
                    onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                    onGuestLogin = { navController.navigate(Routes.PERSONAL) }
                )
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = { navController.navigate(Routes.PERSONAL) },
                    onNavigateToSignup = { navController.navigate(Routes.SIGNUP) },
                    onGuestLogin = { navController.navigate(Routes.PERSONAL) }
                )
            }

            composable(Routes.PERSONAL) {
                PersonalScreen(
                    uiState = resumeViewModel.uiState,
                    onNext = { navController.navigate(Routes.CAREER) },
                    onPhotoPicked = resumeViewModel::setPhotoUri,
                    onFieldChange = resumeViewModel::updatePersonal
                )
            }
            composable(Routes.CAREER) {
                CareerObjectiveScreen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.SKILLS) },
                    onFieldChange = resumeViewModel::updateCareerObjective
                )
            }

            composable(Routes.SKILLS) {
                SkillScreen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.SCHOOL10EDUCATION) },
                    onAdd = resumeViewModel::addSkill,
                    onRemove = resumeViewModel::removeSkill,
                    onFieldChange = resumeViewModel::updateSkill
                )
            }

            composable(Routes.SCHOOL10EDUCATION) {
                SchoolEducation10Screen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.SCHOOL12EDUCATION) },
                    onFieldChange = resumeViewModel::updateSchool10Education
                )
            }
            composable(Routes.SCHOOL12EDUCATION) {
                SchoolEducation12Screen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.HIGHEREDUCATION) },
                    onFieldChange = resumeViewModel::updateSchool12Education
                )
            }
            composable(Routes.HIGHEREDUCATION) {
                HigherEducationScreen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.EXPERIENCE) },
                    onAdd = resumeViewModel::addHigherEducation,
                    onRemove = resumeViewModel::removeHigherEducation,
                    onFieldChange = resumeViewModel::updateHigherEducation
                )
            }

            composable(Routes.EXPERIENCE) {

                ExperienceScreen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.PROJECT) },
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
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.CERTIFICATES) },
                    onAdd = resumeViewModel::addAchievement,
                    onRemove = resumeViewModel::removeAchievement,
                    onFieldChange = resumeViewModel::updateAchievement
                )
            }

            composable(Routes.CERTIFICATES) {
                CertificateScreen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.LANGUAGE) },
                    onAdd = resumeViewModel::addCertificate,
                    onRemove = resumeViewModel::removeCertificate,
                    onFieldChange = resumeViewModel::updateCertificate
                )
            }


            composable(Routes.LANGUAGE) {
                LanguageScreen(
                    uiState = resumeViewModel.uiState,
                    onPrev = { navController.popBackStack() },
                    onNext = { navController.navigate(Routes.PREVIEW) },
                    onAdd = resumeViewModel::addLanguage,
                    onRemove = resumeViewModel::removeLanguage,
                    onFieldChange = resumeViewModel::updateLanguage
                )
            }
            composable(Routes.PREVIEW) {
                PreviewScreen(
                    uiState = resumeViewModel.uiState,
                    onEdit = { navController.navigate(Routes.PERSONAL) {
                        popUpTo(Routes.PERSONAL) { inclusive = true }
                    }
                    },
                    onDelete = {
                        resumeViewModel.deleteAll {
                            navController.popBackStack(Routes.PERSONAL, inclusive = false)
                        }
                    },
                    onSaveDatabase = { resumeViewModel.saveFirebase() },
                    onDownloadPDF = { uri -> resumeViewModel.downloadPDF(uri) },
                    onDownloadImage = { uri -> resumeViewModel.downloadImage(uri) },
                    onImportBackup = { uri -> resumeViewModel.importBackupJson(uri) },
                    onExportBackup = { uri -> resumeViewModel.exportBackupJson(uri) }
//                        navController.navigate(Routes.PERSONAL)
//
                )
            }
        }
    }
}