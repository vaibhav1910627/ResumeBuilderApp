package com.example.myresumeapp.data.local

import android.R
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {

    @Query("Select * FROM resume_table")
    suspend fun getAllProfiles(): List<ResumeEntity>

    @Query("Select * FROM resume_table WHERE id = :id")
    suspend fun getProfileById(id: String): ResumeEntity?

    @Query("Select * FROM resume_table LIMIT 1")
    suspend fun getResume(): ResumeEntity?

    @Query("SELECT * FROM resume_table LIMIT 1")
    fun getResumeFlow(): Flow<ResumeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: ResumeEntity)

    @Query("UPDATE resume_table SET isSynced = :synced WHERE id = :id")
    suspend fun updateSyncStatus(id: String, synced: Boolean)

    @Query("DELETE FROM resume_table WHERE id = :id")
    suspend fun deleteProfile(id: String)

}