package com.example.myresumeapp.core.utils

import android.content.Context
import android.net.Uri
import com.example.myresumeapp.data.local.ResumeDao
import com.example.myresumeapp.data.model.ResumeData
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object JsonBackupUtils{
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun exportResumeToJson(context: Context, resume: ResumeData, uri: Uri): Boolean {
        return runCatching {
            val jsonString = gson.toJson(resume)
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(jsonString)
                }
            }
            true
        }.getOrDefault(false)
    }

    fun importResumeFromJson(context: Context, uri: Uri): ResumeData? {
        return runCatching {
            context.contentResolver.openInputStream(uri)?.use {
                inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use {
                    reader ->
                    val jsonString = reader.readText()
                    gson.fromJson(jsonString, ResumeData::class.java)
                }
            }
        }.getOrNull()
    }
}