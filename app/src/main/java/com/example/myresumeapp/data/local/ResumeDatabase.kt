package com.example.myresumeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [ResumeEntity::class], version = 1, exportSchema = false)
@TypeConverters(ResumeTypeConverters::class)
abstract class ResumeDatabase: RoomDatabase() {
    abstract fun resumeDao(): ResumeDao
}