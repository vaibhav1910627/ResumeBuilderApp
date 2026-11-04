package com.example.myresumeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.myresumeapp.data.model.ResumeData
import com.google.gson.Gson

@Entity(tableName = "resume_table")
data class ResumeEntity(
    @PrimaryKey
    val id: String,
    val resumeJson: String,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

class ResumeTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromResumeData(resume: ResumeData): String = gson.toJson(resume)

    @TypeConverter
    fun toResumeData(json: String): ResumeData = gson.fromJson(json, ResumeData::class.java)

}