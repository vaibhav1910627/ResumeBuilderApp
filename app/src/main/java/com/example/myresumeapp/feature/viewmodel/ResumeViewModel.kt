package com.example.myresumeapp.feature.viewmodel

//import androidx.wear.compose.foundation.size
//import ResumeRepository
import com.example.myresumeapp.data.repository.ResumeRepository
import android.app.Application
import android.app.Person
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.myresumeapp.data.model.Achievements
import com.example.myresumeapp.data.model.Certificates
import com.example.myresumeapp.data.model.Experience
import com.example.myresumeapp.data.model.HigherEducation
import com.example.myresumeapp.data.model.Language
import com.example.myresumeapp.data.model.Project
import com.example.myresumeapp.data.model.Skills
import com.example.myresumeapp.data.model.UiState
import com.example.myresumeapp.core.utils.generateResumeImageToURI
import com.example.myresumeapp.core.utils.generateResumePDFToURI
import com.example.myresumeapp.core.utils.toSoftwareBitmap
import com.example.myresumeapp.data.model.CareerObjective
import com.example.myresumeapp.data.model.PersonalDetails
import com.example.myresumeapp.data.model.ResumeData
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor (
    application: Application,
    private val resumeRepository: ResumeRepository,
    private val savedStateHandle: SavedStateHandle
): AndroidViewModel(application) {
    //    private val resumeRepository = ResumeRepository()

    companion object {
        private const val RESUME_STATE_KEY = "resume_data_state"
    }
    private val internalUiState = mutableStateOf(UiState(resume = savedStateHandle.get<ResumeData>(RESUME_STATE_KEY) ?: ResumeData()))
    val uiState: UiState get() = internalUiState.value

    private fun updateResumeState(newResume: ResumeData) {
        internalUiState.value = internalUiState.value.copy(resume = newResume)
        savedStateHandle[RESUME_STATE_KEY] = newResume

    }

    val atsScoreResult: com.example.myresumeapp.core.utils.AtsScoreResult
        get() = com.example.myresumeapp.core.utils.AtsScoreAnalyser.analyze(uiState.resume)

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
        country: String?,
        countryCode: String?,
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
                    country = country ?: currentResume.personal.country,
                    countryCode = countryCode ?: currentResume.personal.countryCode,
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
                    careerDescription = careerDescription
                        ?: currentCareerObjective.career.careerDescription
                )
            )
        )
    }

    fun exportBackupJson(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>()
            val success = com.example.myresumeapp.core.utils.JsonBackupUtils.exportResumeToJson(context, internalUiState.value.resume, uri)
            withContext(Dispatchers.Main) {
                internalUiState.value = internalUiState.value.copy(
                    message = if (success) "Backup exported successfully!" else "Failed to export backup"
                )
            }
        }
    }

    fun importBackupJson(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>()
            val importedResume = com.example.myresumeapp.core.utils.JsonBackupUtils.importResumeFromJson(context, uri)
            withContext(Dispatchers.Main) {
                if (importedResume != null) {
                    internalUiState.value = internalUiState.value.copy(
                        resume = importedResume,
                        message = "Resume imported successfully!"
                    )
                    // Persist to local database
                    saveFirebase()
                } else {
                    internalUiState.value = internalUiState.value.copy(
                        message = "Invalid JSON backup file"
                    )
                }
            }
        }
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


    fun addSkill(initialSkill: String = "") {
        val currentList = internalUiState.value.resume.skills.toMutableList()
        currentList.add(Skills(skill = initialSkill))

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
            skill = skill ?: skillToUpdate.skill
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
        language: String?,
        proficiency: String?
    ) {


        val currentLanguage = internalUiState.value.resume.language.toMutableList()
        val languageToUpdate = currentLanguage[index]

        currentLanguage[index] = languageToUpdate.copy(
            language = language ?: languageToUpdate.language,
            proficiency = proficiency ?: languageToUpdate.proficiency
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
            projectName = projecName ?: projectToUpdate.projectName,
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
        startMonthYear: String?,
        endMonthYear: String?,
        topic: String?
    ) {


        val currentCertificate = internalUiState.value.resume.certificate.toMutableList()
        val certificateToUpdate = currentCertificate[index]

        currentCertificate[index] = certificateToUpdate.copy(
            association = association ?: certificateToUpdate.association,
            startMonthYear = startMonthYear ?: certificateToUpdate.startMonthYear,
            endMonthYear = endMonthYear ?: certificateToUpdate.endMonthYear,
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

    val profilesList = mutableStateOf<List<Pair<String, String>>>(emptyList())

    init {
        loadProfiles()
    }

    fun loadProfiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val profiles = resumeRepository.getAllProfiles()
            withContext(Dispatchers.Main) {
                profilesList.value = profiles.map { it.first to (it.second.personal.name.ifBlank { "Untitled Resume" }) }
            }
        }
    }

    fun createNewProfile(title: String, onCreated: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newResume = resumeRepository.createNewProfile(title)
            withContext(Dispatchers.Main) {
                internalUiState.value = internalUiState.value.copy(resume = newResume)
                loadProfiles()
                onCreated()
            }
        }
    }

    fun switchProfile(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = resumeRepository.getProfileById(id)
            if (entity != null) {
                val resumeData = Gson().fromJson(entity.resumeJson, ResumeData::class.java)
                withContext(Dispatchers.Main) {
                    internalUiState.value = internalUiState.value.copy(resume = resumeData)
                }
            }

            loadProfiles()
        }
    }
    fun autofillSampleData() {
        val sampleResume = ResumeData(
            id = "sample_resume",
            personal = PersonalDetails(
            name = "Alex Vance",
            email = "alex.vance@example.com",
            phone = "+1 555-019-2834",
            country = "United-States",
            countryCode = "+1",
            location = "San Francisco, CA",
            githublink = "github.com/alexvance",
            linkedinlink = "linkedin.com/in/alexvance"
        ),
            career = CareerObjective(
                careerDescription = "Passionate Senior Android Developer with 4+ years of experience building scalable, offline-first mobile applications using Kotlin, Jetpack Compose, and Dagger Hilt."
            ),
            higherEducation = listOf(
                HigherEducation(
                    degree = "B.S. in Computer Science",
                    institution = "UC Berkeley",
                    startYear = "2018",
                    endYear = "2022",
                    CGPA = "3.85"
                )
            ),
            school10Education = com.example.myresumeapp.data.model.School10Education(
                school10 = "St. Jude High School", board10 = "CBSE", startYear10 = "2014", endYear10 = "2016", percentage10 = "94%"
            ),
            school12Education = com.example.myresumeapp.data.model.School12Education(
                school12 = "St. Jude Senior Academy", board12 = "CBSE", startYear12 = "2016", endYear12 = "2018", percentage12 = "92%"
            ),
            skills = listOf(
                com.example.myresumeapp.data.model.Skills("Kotlin"),
                com.example.myresumeapp.data.model.Skills("Java"),
                com.example.myresumeapp.data.model.Skills("Python"),
                com.example.myresumeapp.data.model.Skills("SQL")
            ),
            skills2 = listOf(
                com.example.myresumeapp.data.model.Skills2("Jetpack Compose"),
                com.example.myresumeapp.data.model.Skills2("Coroutines & Flow"),
                com.example.myresumeapp.data.model.Skills2("Dagger Hilt"),
                com.example.myresumeapp.data.model.Skills2("Room DB"),
                com.example.myresumeapp.data.model.Skills2("Retrofit")
            ),
            skills3 = listOf(
                com.example.myresumeapp.data.model.Skills3("Git & GitHub"),
                com.example.myresumeapp.data.model.Skills3("Docker"),
                com.example.myresumeapp.data.model.Skills3("Firebase"),
                com.example.myresumeapp.data.model.Skills3("CI/CD")
            ),
            language = listOf(
                com.example.myresumeapp.data.model.Language("English", "Native"),
                com.example.myresumeapp.data.model.Language("Spanish", "Intermediate")
            ),
            experience = listOf(
                Experience(
                    company = "TechCorp Mobile",
                    role = "Senior Android Engineer",
                    startMonthYear = "Jan 2022",
                    endMonthYear = "Present",
                    responsibilities = "Spearheaded the redesign of the core Android app using Jetpack Compose and MVVM, improving app performance by 35% across 500k+ active users."
                )
            ),
            projects = listOf(
                com.example.myresumeapp.data.model.Project(
                    projectName = "MyResumeApp - Native Android Resume Builder",
                    technology = "Kotlin, Jetpack Compose, Dagger Hilt, Room DB, Glance",
                    description = "Offline-first Android application enabling job seekers to create, ATS-optimize, and export professional PDF resumes with real-time preview."
                ),
                com.example.myresumeapp.data.model.Project(
                    projectName = "E-Commerce Cloud Storefront",
                    technology = "Kotlin, Firebase Auth, Coroutines, Retrofit, Stripe API",
                    description = "Architected a full-featured shopping app with real-time product sync, secure checkout, and cloud order tracking."
                ),
                com.example.myresumeapp.data.model.Project(
                    projectName = "DevPulse - Developer Community Platform",
                    technology = "Java, Spring Boot, PostgreSQL, Docker, REST APIs",
                    description = "Built a high-throughput backend API service powering developer discussion forums and codebase sharing."
                )
            ),

            certificate = listOf(
                com.example.myresumeapp.data.model.Certificates(
                    association = "Google Cloud",
                    topic = "Associate Android Developer Certification",
                    startMonthYear = "Aug 2022",
                    endMonthYear = ""
                ),
                com.example.myresumeapp.data.model.Certificates(
                    association = "Meta / Coursera",
                    topic = "Meta Android Developer Professional Certificate",
                    startMonthYear = "Mar 2023",
                    endMonthYear = ""
                ),
                com.example.myresumeapp.data.model.Certificates(
                    association = "Amazon Web Services",
                    topic = "AWS Certified Solutions Architect",
                    startMonthYear = "Jan 2024",
                    endMonthYear = ""
                )
            ),
        )

        internalUiState.value = internalUiState.value.copy(
            resume = sampleResume,
            message = "Sample resume data loaded!"
        )
        saveFirebase()
    }
}