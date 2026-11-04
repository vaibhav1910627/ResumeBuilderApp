package com.example.myresumeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {
    @Query("Select * FROM resume_table LIMIT 1")
    suspend fun getResume(): ResumeEntity?

    @Query("SELECT * FROM resume_table LIMIT 1")
    fun getResumeFlow(): Flow<ResumeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: ResumeEntity)

    @Query("UPDATE resume_table SET isSynced = :synced WHERE id = :id")
    suspend fun updateSyncStatus(id: String, synced: Boolean)

}