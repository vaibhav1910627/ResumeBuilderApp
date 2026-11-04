package com.example.myresumeapp.data.repository

import android.graphics.Bitmap
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.myresumeapp.data.local.ResumeDao
import com.example.myresumeapp.data.local.ResumeEntity
import com.example.myresumeapp.data.model.ResumeData
import com.example.myresumeapp.data.worker.SyncResumeWorker
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class ResumeRepository @Inject constructor(
    private val resumeDao: ResumeDao,
    private val workManager: WorkManager
) {
    private val firebaseDatabase: DatabaseReference = Firebase.database.reference
    private val firebaseStorage: StorageReference = Firebase.storage.reference
    private val userID: String get() = Firebase.auth.currentUser?.uid ?: "guest"

    suspend fun getLocalResume(): ResumeData? {
        return resumeDao.getResume()?.let { entity ->
            Gson().fromJson(entity.resumeJson, ResumeData::class.java)
        }
    }

    suspend fun saveResumeLocally(resume: ResumeData, localPhotoBitmap: Bitmap?) {
        val id = resume.id.ifEmpty { UUID.randomUUID().toString() }
        val updatedResume = resume.copy(id = id)

        val entity = ResumeEntity(
            id = id,
            resumeJson = Gson().toJson(updatedResume),
            isSynced = false
        )

        resumeDao.insertResume(entity)

        val syncRequest = OneTimeWorkRequestBuilder<SyncResumeWorker>()
            .setInputData(workDataOf("RESUME_ID" to id))
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        workManager.enqueue(syncRequest)
    }

    suspend fun createOrUpdate(resume: ResumeData, localPhotoBitmap: Bitmap?): ResumeData {
        val id = resume.id.ifEmpty {
            firebaseDatabase.child("users/$userID/resumes").push().key ?: UUID.randomUUID().toString()
        }

        var photoUrl = resume.personal.photoUrl
        if (localPhotoBitmap != null) {
            val bytes = ByteArrayOutputStream().use { out ->
                localPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
                out.toByteArray()
            }
            val photoPath = "users/$userID/$id/photo.jpg"
            val photoStorage = firebaseStorage.child(photoPath)
            photoStorage.putBytes(bytes).await()
            photoUrl = photoStorage.downloadUrl.await().toString()
        }

        val updated = resume.copy(
            id = id,
            personal = resume.personal.copy(photoUrl = photoUrl)
        )

        // Save locally and trigger background sync
        saveResumeLocally(updated, null)
        return updated
    }

    suspend fun delete(id: String) {
        firebaseDatabase.child("users/$userID/resumes/$id").removeValue().await()
        runCatching { firebaseStorage.child("users/$userID/$id/photo.jpg").delete().await() }
    }
}