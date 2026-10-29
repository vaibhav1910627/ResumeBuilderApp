package com.example.myresumeapp.data

import android.net.Uri
import javax.security.cert.Certificate


data class Signup(
        val email: String = "",
        val password: String = ""
        )

data class Login(
    val email: String = "",
    val password: String = ""
)
data class PersonalDetails(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val photoUrl: String = "",
    val githublink: String = "",
    val linkedinlink: String = ""
)

data class CareerObjective(
    val careerDescription: String = ""
)
data class HigherEducation(
    val degree: String = "",
    val institution: String = "",
    val startYear: String = "",
    val endYear: String = "",
    val CGPA: String =  "",

)

data class Language(
    val language: String = ""
)

data class Skills(
    val skill: String = ""
)

data class Skills2(
    val skill2: String = ""
)
data class Skills3(
    val skill3: String = ""
)

data class Experience(
    val company: String = "",
    val role: String = "",
    val startMonthYear: String = "",
    val endMonthYear: String = "",
    val responsibilities: String = "",
)

data class Achievements(
    val achievements: String = "",
    val description: String = ""
)
data class Project(
    val projectName: String = "",
    val technology: String = "",
    val description: String = ""
)

data class School10Education(
    val school10: String = "",
    val board10: String = "",
    val startYear10: String = "",
    val endYear10: String = "",
    val percentage10: String =  ""
)


data class Certificates(
    val association: String = "",
    val monthYear: String= "",
    val topic: String = ""
)
data class School12Education(
    val school12: String = "",
    val board12: String = "",
    val startYear12: String = "",
    val endYear12: String = "",
    val percentage12: String =  ""
)



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
)


data class UiState(
    val resume: ResumeData = ResumeData(),
    val localPhotoUri: Uri? = null,
    val isSaving: Boolean = false,
    val lastSaveId: String? = null,
    val message: String? = null
)
