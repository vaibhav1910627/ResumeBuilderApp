package com.example.myresumeapp.viewmodel

//import androidx.wear.compose.foundation.size
import ResumeRepository
import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myresumeapp.data.Achievements
import com.example.myresumeapp.data.CareerObjective
import com.example.myresumeapp.data.Certificates
import com.example.myresumeapp.data.Experience
import com.example.myresumeapp.data.HigherEducation
import com.example.myresumeapp.data.Language
import com.example.myresumeapp.data.Project
import com.example.myresumeapp.data.Skills
import com.example.myresumeapp.data.UiState
import com.example.myresumeapp.utils.generateResumeImageToURI
import com.example.myresumeapp.utils.generateResumePDFToURI
import com.example.myresumeapp.utils.toSoftwareBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResumeViewModel(app: Application): AndroidViewModel(app) {
    private val resumeRepository = ResumeRepository()
    private val internalUiState = mutableStateOf(UiState())
    val uiState: UiState get() = internalUiState.value

    fun setPhotoUri(uri: Uri?) {
        internalUiState.value = internalUiState.value.copy(localPhotoUri = uri)

    }

    //it converts a file path into a bitmap(actual image pixels) to show us, and also handles different android versions..

    private fun localBitmapOrNull(): Bitmap? {
        val appContext = getApplication<Application>()
        val uri = internalUiState.value.localPhotoUri ?: return null
        return runCatching {
            val bitmap = if (Build.VERSION.SDK_INT >= 28) {
                val imgSource = ImageDecoder.createSource(appContext.contentResolver, uri)
                ImageDecoder.decodeBitmap(imgSource)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(appContext.contentResolver, uri)
            }
            bitmap.toSoftwareBitmap()
        }.getOrNull()
    }

    fun updatePersonal(
        name: String?,
        email: String?,
        phone: String?,
        githublink: String?,
        linkedlink: String?,
        location: String?
    ) {
        val currentResume = internalUiState.value.resume
        internalUiState.value = internalUiState.value.copy(
            resume = currentResume.copy(
                personal = currentResume.personal.copy(
                    name = name ?: currentResume.personal.name,
                    email = email ?: currentResume.personal.email,
                    phone = phone ?: currentResume.personal.phone,
                    githublink = githublink ?: currentResume.personal.githublink,
                    linkedinlink = linkedlink ?: currentResume.personal.linkedinlink,
                    location = location ?: currentResume.personal.location
                )
            )
        )
    }

    fun updateCareerObjective(
        careerDescription: String?
    ) {
        val currentCareerObjective = internalUiState.value.resume
        internalUiState.value = internalUiState.value.copy(
            resume = currentCareerObjective.copy(
                career = currentCareerObjective.career.copy(
careerDescription = careerDescription ?: currentCareerObjective.career.careerDescription
                )
            )
        )
    }


//    fun addQualification() {
//        val currentList = internalUiState.value.resume.qualificationSkills
//        updateResume { it.copy(qualifications = currentList + QualificationSkills()) }
//    }
//fun addQualification(degree: String?, institution: String?, startYear: String?, endYear: String?, skills: String?) {
//    val newQualification = QualificationSkills(
//        degree = degree ?: "",
//        institution = institution ?: "",
//        startYear = startYear ?: "",
//        endYear = endYear ?: "",
//        skills = skills ?: ""
//    )
//
//    val currentQualifications = internalUiState.value.resume.qualificationSkills.toMutableList()
//    currentQualifications.add(newQualification)
//
//    internalUiState.value = internalUiState.value.copy(
//        resume = internalUiState.value.resume.copy(qualificationSkills = currentQualifications)
//    )
//}

    fun updateSchool12Education(
        school12: String?,
        board12: String?,
        startYear12: String?,
        endYear12: String?,
        percentage12: String?
    ) {
        val currentResume = internalUiState.value.resume
        internalUiState.value = internalUiState.value.copy(
            resume = currentResume.copy(
                school12Education = currentResume.school12Education.copy(

                    school12 = school12 ?: currentResume.school12Education.school12,
                    board12 = board12 ?: currentResume.school12Education.board12,
                    startYear12 = startYear12 ?: currentResume.school12Education.startYear12,
                    endYear12 = endYear12 ?: currentResume.school12Education.endYear12,
                    percentage12 = percentage12 ?: currentResume.school12Education.percentage12
                )
            )
        )
    }
//        internalUiState.value = internalUiState.value.copy(
//            resume = internalUiState.value.resume.copy(school12Education = currentschool12Qualifications)
//        )
//    }

    fun updateSchool10Education(
        school10: String?,
        board10: String?,
        startYear10: String?,
        endYear10: String?,
        percentage10: String?
    ) {
        val currentResume = internalUiState.value.resume
        internalUiState.value = internalUiState.value.copy(
            resume = currentResume.copy(
                school10Education = currentResume.school10Education.copy(

                    school10 = school10 ?: currentResume.school10Education.school10,
                    board10 = board10 ?: currentResume.school10Education.board10,
                    startYear10 = startYear10 ?: currentResume.school10Education.startYear10,
                    endYear10 = endYear10 ?: currentResume.school10Education.endYear10,
                    percentage10 = percentage10 ?: currentResume.school10Education.percentage10
                )
            )
        )
    }

    fun addHigherEducation() {
        val currentList = internalUiState.value.resume.higherEducation.toMutableList()
        currentList.add(HigherEducation())

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(
                higherEducation = currentList
            )
        )
    }


    fun removeHigherEducation(index: Int) {
        val currentQualifications = internalUiState.value.resume.higherEducation.toMutableList()
        if (currentQualifications.size > 1) { // Keep at least one qualification
            currentQualifications.removeAt(index)

            internalUiState.value = internalUiState.value.copy(
                resume = internalUiState.value.resume.copy(higherEducation = currentQualifications)
            )
        }
    }

    // Remove a qualification at a specific index
//    fun removeQualification(index: Int) {
//        val currentList = internalUiState.value.resume.qualificationSkills.toMutableList()
//        if (currentList.size > 1) { // Keep at least one
//            currentList.removeAt(index)
//            updateResume { it.copy(qualifications = currentList) }
//        }
//    }

    fun updateHigherEducation(
        index: Int,
        degree: String?,
        institution: String?,
        startYear: String?,
        endYear: String?,
        CGPA: String?
    ) {
//        val currentResume = internalUiState.value.resume
//        internalUiState.value = internalUiState.value.copy(
//            resume = currentResume.copy(
//                qualificationSkills = currentResume.qualificationSkills.copy(
//                    degree = degree ?: currentResume.qualificationSkills.degree,
//                    institution = institution ?: currentResume.qualificationSkills.institution,
//                    startYear = startYear ?: currentResume.qualificationSkills.startYear,
//                    endYear = endYear ?: currentResume.qualificationSkills.endYear,
//                    skills = skills ?: currentResume.qualificationSkills.skills
//                )
//            )
//        )
//    }

        val currentQualifications = internalUiState.value.resume.higherEducation.toMutableList()
        val qualificationToUpdate = currentQualifications[index]

        currentQualifications[index] = qualificationToUpdate.copy(
            degree = degree ?: qualificationToUpdate.degree,
            institution = institution ?: qualificationToUpdate.institution,
            startYear = startYear ?: qualificationToUpdate.startYear,
            endYear = endYear ?: qualificationToUpdate.endYear,
            CGPA = CGPA ?: qualificationToUpdate.CGPA
        )

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(higherEducation = currentQualifications)
        )
    }


//    fun updateQualification(index: Int, degree: String?, institution: String?, startYear: String?, endYear: String?, skills: String?) {
//        val currentList = internalUiState.value.resume.qualificationSkills.toMutableList()
//        val item = currentList[index]
//
//        currentList[index] = item.copy(
//            degree = degree ?: item.degree,
//            institution = institution ?: item.institution,
//            startYear = startYear ?: item.startYear,
//            endYear = endYear ?: item.endYear,
//            skills = skills ?: item.skills
//        )
//        updateResume { it.copy(qualifications = currentList) }
//    }


//    fun updateSkill(skills: String? ) {
//        val currentResume = internalUiState.value.resume
//        internalUiState.value = internalUiState.value.copy(
//            resume = currentResume.copy(
//                skills = currentResume.skills.copy(
//                    skill = skills ?: currentResume.skills.skill
//
//                )
//            )
//        )
//    }

    fun addExperience() {
        val currentList = internalUiState.value.resume.experience.toMutableList()
        currentList.add(Experience())

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(
                experience = currentList
            )
        )
    }


    fun removeExperience(index: Int) {
        val currentExperience = internalUiState.value.resume.experience.toMutableList()
        if (currentExperience.size > 1) { // Keep at least one qualification
            currentExperience.removeAt(index)

            internalUiState.value = internalUiState.value.copy(
                resume = internalUiState.value.resume.copy(experience = currentExperience)
            )
        }
    }


    fun updateExperience(
        index: Int,
        company: String?,
        role: String?,
        startMonthYear: String?,
        endMonthYear: String?,
        responsibilities: String?
    ) {


        val currentExperience = internalUiState.value.resume.experience.toMutableList()
        val experienceToUpdate = currentExperience[index]

        currentExperience[index] = experienceToUpdate.copy(
            company = company ?: experienceToUpdate.company,
            role = role ?: experienceToUpdate.role,
            startMonthYear = startMonthYear ?: experienceToUpdate.startMonthYear,
            endMonthYear = endMonthYear ?: experienceToUpdate.endMonthYear,
            responsibilities = responsibilities ?: experienceToUpdate.responsibilities
        )

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(experience = currentExperience)
        )
    }


    fun deleteAll(onDone: () -> Unit) {
        val id = internalUiState.value.resume.id
        viewModelScope.launch {
            internalUiState.value = internalUiState.value.copy(isSaving = true, message = null)
            runCatching {
                if (id.isNotEmpty()) resumeRepository.delete(id)
            }.onSuccess {
                internalUiState.value = UiState()
                onDone()
            }.onFailure {
                internalUiState.value = internalUiState.value.copy(
                    isSaving = false,
                    message = "Delete Failed: ${it.message}"
                )
            }
        }
    }

    fun saveFirebase() {
        viewModelScope.launch {
            internalUiState.value = internalUiState.value.copy(isSaving = true, message = null)
            val bitmap = withContext(Dispatchers.IO) { localBitmapOrNull() }
            runCatching {
                resumeRepository.createOrUpdate(internalUiState.value.resume, bitmap)
            }.onSuccess { saved ->
                internalUiState.value =
                    internalUiState.value.copy(isSaving = false, resume = saved, message = "Saved")
            }.onFailure {
                internalUiState.value = internalUiState.value.copy(
                    isSaving = false,
                    message = "Save Failed: ${it.message}"
                )
            }
        }
    }

    fun downloadPDF(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val bitmap = localBitmapOrNull()
                val appContext = getApplication<Application>()
                generateResumePDFToURI(appContext, internalUiState.value.resume, bitmap, uri)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    internalUiState.value = internalUiState.value.copy(
                        message = "PDF saved"
                    )
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    internalUiState.value = internalUiState.value.copy(
                        message = "PDF Saving Failed: ${it.message}"
                    )
                }
            }
        }
    }

    fun downloadImage(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val bitmap = localBitmapOrNull()
                val appContext = getApplication<Application>()
                generateResumeImageToURI(appContext, internalUiState.value.resume, bitmap, uri)
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    internalUiState.value = internalUiState.value.copy(
                        message = "Image saved"
                    )
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    internalUiState.value = internalUiState.value.copy(
                        message = "Image Saving Failed: ${it.message}"
                    )
                }
            }
        }
    }


    /*
        *
        * //        val school12qualificationToUpdate = currentschool12Qualifications[index]
//        val currentResume = internalUiState.value.resume
//        internalUiState.value = internalUiState.value.copy(
//            resume = currentResume.copy(
//        currentschool12Qualifications[index] = school12qualificationToUpdate.copy(*/


    fun addSkill() {
        val currentList = internalUiState.value.resume.skills.toMutableList()
        currentList.add(Skills())

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(
                skills = currentList
            )
        )
    }


    fun removeSkill(index: Int) {
        val currentSkill = internalUiState.value.resume.skills.toMutableList()
        if (currentSkill.size > 1) { // Keep at least one qualification
            currentSkill.removeAt(index)

            internalUiState.value = internalUiState.value.copy(
                resume = internalUiState.value.resume.copy(skills = currentSkill)
            )
        }
    }


    fun updateSkill(
        index: Int,
        skill: String?
    ) {

        val currentSkills = internalUiState.value.resume.skills.toMutableList()
        val skillToUpdate = currentSkills[index]

        currentSkills[index] = skillToUpdate.copy(
            skill = skill  ?: skillToUpdate.skill
        )

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(skills = currentSkills)
        )
    }

    fun addLanguage() {
        val currentLanguage = internalUiState.value.resume.language.toMutableList()
        currentLanguage.add(Language())

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(
                language = currentLanguage
            )
        )
    }


    fun removeLanguage(index: Int) {
        val currentLanguage = internalUiState.value.resume.language.toMutableList()
        if (currentLanguage.size > 1) { // Keep at least one qualification
            currentLanguage.removeAt(index)

            internalUiState.value = internalUiState.value.copy(
                resume = internalUiState.value.resume.copy(language = currentLanguage)
            )
        }
    }


    fun updateLanguage(
        index: Int,
        language: String?
    ) {


        val currentLanguage = internalUiState.value.resume.language.toMutableList()
        val languageToUpdate = currentLanguage[index]

        currentLanguage[index] = languageToUpdate.copy(
            language = language ?: languageToUpdate.language
        )

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(language = currentLanguage)
        )
    }

    fun addProject() {
        val currentProject = internalUiState.value.resume.projects.toMutableList()
        currentProject.add(Project())

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(
                projects = currentProject
            )
        )
    }


    fun removeProject(index: Int) {
        val currentProject = internalUiState.value.resume.projects.toMutableList()
        if (currentProject.size > 1) { // Keep at least one qualification
            currentProject.removeAt(index)

            internalUiState.value = internalUiState.value.copy(
                resume = internalUiState.value.resume.copy(projects = currentProject)
            )
        }
    }


    fun updateProject(
    index: Int,
    projecName: String?,
    technology: String?,
    description: String?
    ) {

        val currentProject = internalUiState.value.resume.projects.toMutableList()
        val projectToUpdate = currentProject[index]

        currentProject[index] = projectToUpdate.copy(
            projectName = projecName  ?: projectToUpdate.projectName,
            technology = technology ?: projectToUpdate.technology,
            description = description ?: projectToUpdate.description
        )

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(projects = currentProject)
        )
    }

    fun addCertificate() {
        val currentCertificate = internalUiState.value.resume.certificate.toMutableList()
        currentCertificate.add(Certificates())

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(
                certificate = currentCertificate
            )
        )
    }


    fun removeCertificate(index: Int) {
        val currentCertificate = internalUiState.value.resume.certificate.toMutableList()
        if (currentCertificate.size > 1) { // Keep at least one qualification
            currentCertificate.removeAt(index)

            internalUiState.value = internalUiState.value.copy(
                resume = internalUiState.value.resume.copy(certificate = currentCertificate)
            )
        }
    }


    fun updateCertificate(
        index: Int,
        association: String?,
        monthYear: String?,
        topic: String?
    ) {


        val currentCertificate = internalUiState.value.resume.certificate.toMutableList()
        val certificateToUpdate = currentCertificate[index]

        currentCertificate[index] = certificateToUpdate.copy(
            association = association ?: certificateToUpdate.association,
            monthYear = monthYear ?: certificateToUpdate.monthYear,
            topic = topic ?: certificateToUpdate.topic
        )

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(certificate = currentCertificate)
        )
    }

    fun addAchievement() {
        val currentAchievement = internalUiState.value.resume.achievements.toMutableList()
        currentAchievement.add(Achievements())

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(
                achievements = currentAchievement
            )
        )
    }


    fun removeAchievement(index: Int) {
        val currentAchievement = internalUiState.value.resume.achievements.toMutableList()
        if (currentAchievement.size > 1) { // Keep at least one qualification
            currentAchievement.removeAt(index)

            internalUiState.value = internalUiState.value.copy(
                resume = internalUiState.value.resume.copy(achievements = currentAchievement)
            )
        }
    }




    fun updateAchievement(
        index: Int,
        achievements: String?,
        description: String?
    ) {


        val currentAchievement = internalUiState.value.resume.achievements.toMutableList()
        val achievementToUpdate = currentAchievement[index]

        currentAchievement[index] = achievementToUpdate.copy(
            achievements = achievements ?: achievementToUpdate.achievements,
            description = description ?: achievementToUpdate.description
        )

        internalUiState.value = internalUiState.value.copy(
            resume = internalUiState.value.resume.copy(achievements = currentAchievement)
        )
    }
}