package com.example.myresumeapp.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myresumeapp.data.local.ResumeDao
import com.example.myresumeapp.data.model.ResumeData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class SyncResumeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val resumeDao: ResumeDao
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val entity = resumeDao.getResume() ?: return Result.success()
            val resume = Gson().fromJson(entity.resumeJson, ResumeData::class.java)

            val firebaseDatabase = Firebase.database.reference
            val userID = Firebase.auth.currentUser?.uid ?: "guest"

            firebaseDatabase.child("users/$userID/resumes/${entity.id}").setValue(resume).await()

            resumeDao.updateSyncStatus(entity.id, true)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}