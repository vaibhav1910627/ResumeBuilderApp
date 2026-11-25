package com.example.myresumeapp.data.model

import android.net.Uri
import java.io.Serializable


data class Signup(
        val email: String = "",
        val password: String = ""
        ) : Serializable

data class Login(
    val email: String = "",
    val password: String = ""
): Serializable
data class PersonalDetails(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val country: String = "",
    val countryCode: String = "+91",
    val location: String = "",
    val photoUrl: String = "",
    val githublink: String = "",
    val linkedinlink: String = ""
): Serializable

data class CareerObjective(
    val careerDescription: String = ""
): Serializable
data class HigherEducation(
    val degree: String = "",
    val institution: String = "",
    val startYear: String = "",
    val endYear: String = "",
    val CGPA: String =  "",

) : Serializable

data class Language(
    val language: String = "",
    val proficiency: String = ""
) : Serializable

data class Skills(
    val skill: String = ""
): Serializable

data class Skills2(
    val skill2: String = ""
) : Serializable
data class Skills3(
    val skill3: String = ""
): Serializable

data class Experience(
    val company: String = "",
    val role: String = "",
    val startMonthYear: String = "",
    val endMonthYear: String = "",
    val responsibilities: String = "",
): Serializable

data class Achievements(
    val achievements: String = "",
    val description: String = ""
): Serializable
data class Project(
    val projectName: String = "",
    val technology: String = "",
    val description: String = ""
): Serializable

data class School10Education(
    val school10: String = "",
    val board10: String = "",
    val startYear10: String = "",
    val endYear10: String = "",
    val percentage10: String =  ""
): Serializable


data class Certificates(
    val association: String = "",
    val startMonthYear: String= "",
    val endMonthYear: String = "",
    val topic: String = ""
): Serializable
data class School12Education(
    val school12: String = "",
    val board12: String = "",
    val startYear12: String = "",
    val endYear12: String = "",
    val percentage12: String =  ""
): Serializable



data class ResumeData(
    val id: String = "",
    val signup: Signup = Signup(),
    val login: Login = Login(),
    val personal: PersonalDetails = PersonalDetails(),
    val career: CareerObjective = CareerObjective(),
    val higherEducation: List<HigherEducation> = listOf(HigherEducation()),
    val school10Education: School10Education = School10Education(),
    val school12Education: School12Education = School12Education(),
    val language: List<Language> = listOf(Language()),
    val skills: List<Skills>  = listOf(Skills()),
    val skills2: List<Skills2>  = listOf(Skills2()),
    val skills3: List<Skills3>  = listOf(Skills3()),
    val experience: List<Experience> = listOf(Experience()),
    val projects: List<Project> = listOf(Project()),
    val certificate: List<Certificates> = listOf(Certificates()),
    val achievements: List<Achievements> = listOf(Achievements())
): Serializable


data class UiState(
    val resume: ResumeData = ResumeData(),
    val localPhotoUri: Uri? = null,
    val isSaving: Boolean = false,
    val lastSaveId: String? = null,
    val message: String? = null
)
