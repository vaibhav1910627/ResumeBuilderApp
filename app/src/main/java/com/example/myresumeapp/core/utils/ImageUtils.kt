package com.example.myresumeapp.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import com.example.myresumeapp.data.model.ResumeData

fun generateResumeImageToURI(
    context: Context,
    resume: ResumeData,
    photo: Bitmap?,
    outputURI: Uri
) {
    val width = 1240
    val height = 1754
    val scaleFactor = 2.0f

    val centerX = (width / 2).toFloat()
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    canvas.drawColor(Color.WHITE)

    val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 24f * scaleFactor
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }

    val centeredDetailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 10f * scaleFactor
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }

    val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 12f * scaleFactor
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = Color.rgb(43, 58, 85)
    }

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 10f * scaleFactor
        color = Color.BLACK
    }

    val boldTextPaint = Paint(textPaint).apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val italicPaint = Paint(textPaint).apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        color = Color.DKGRAY
    }

    val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 9.5f * scaleFactor
        textAlign = Paint.Align.RIGHT
        color = Color.BLACK
    }

    val divider = Paint().apply { color = Color.LTGRAY; strokeWidth = 1.0f * scaleFactor }

    var y = 50f * scaleFactor

    // -------- Photo ----------
    photo?.let {
        val size = (70 * scaleFactor).toInt()
        val scaled = Bitmap.createScaledBitmap(it, size, size, true)
        canvas.drawBitmap(scaled, (width / 2 - size / 2).toFloat(), y, null)
        y += size + (15f * scaleFactor)
    }

    // -------- Header Details & Links ----------
    val capitalizedName = resume.personal.name.uppercase()
    canvas.drawText(capitalizedName, centerX, y, namePaint)
    y += 20f * scaleFactor

    val contactInfo = "${resume.personal.email}  |  ${resume.personal.phone}  |  ${resume.personal.location}"
    canvas.drawText(contactInfo, centerX, y, centeredDetailPaint)
    y += 14f * scaleFactor

    val links = listOfNotNull(
        resume.personal.githublink?.takeIf { it.isNotBlank() },
        resume.personal.linkedinlink?.takeIf { it.isNotBlank() },
        "LeetCode"
    ).joinToString("  |  ")

    if (links.isNotEmpty()) {
        canvas.drawText(links, centerX, y, centeredDetailPaint)
        y += 16f * scaleFactor
    }

    canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
    y += 25f * scaleFactor

    // -------- TECHNICAL SKILLS ----------
    canvas.drawText("TECHNICAL SKILLS", 80f, y, headerPaint)
    y += 18f * scaleFactor

    val skillCategories = listOf(
        "Languages" to resume.skills?.map { it.skill }.orEmpty(),
        "Mobile" to resume.skills2?.map { it.skill2 }.orEmpty(),
        "ML & Computer Vision" to resume.skills3?.map { it.skill3 }.orEmpty(),
        "IoT & Embedded" to listOf("Microcontroller board, IMU sensor, BLE, real-time systems"),
        "Backend & Web" to listOf("Web framework, REST APIs, Database, webhooks"),
        "Core CS" to listOf("Data Structures & Algorithms, OOP, Low-Latency Design"),
        "Tools" to listOf("Git, GitHub, Linux"),
        "Problem Solving" to listOf("300+ LeetCode problems solved across dynamic data structures")
    )

    skillCategories.forEach { (category, skillsList) ->
        val cleanSkills = skillsList.filter { it.isNotBlank() }.joinToString(", ")
        if (cleanSkills.isNotEmpty()) {
            canvas.drawText("$category: ", 80f, y, boldTextPaint)
            val prefixWidth = boldTextPaint.measureText("$category: ")
            y = drawMultiLine(canvas, cleanSkills, 80f + prefixWidth, y, (width - 160 - prefixWidth).toInt(), textPaint, 14f * scaleFactor)
        }
    }
    y += 5f * scaleFactor
    canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
    y += 25f * scaleFactor

    // -------- PROJECTS ----------
    if (!resume.projects.isNullOrEmpty()) {
        canvas.drawText("PROJECTS", 80f, y, headerPaint)
        y += 18f * scaleFactor
        resume.projects.forEach { proj ->
            if (proj.projectName.isNotBlank()) {
                canvas.drawText("• ${proj.projectName}", 80f, y, boldTextPaint)
                val projTitleWidth = boldTextPaint.measureText("• ${proj.projectName}")
                canvas.drawText(" — (${proj.technology})", 80f + projTitleWidth, y, italicPaint)
                y += 14f * scaleFactor
                y = drawMultiLine(canvas, proj.description, 100f, y, width - 180, textPaint, 14f * scaleFactor)
                y += 8f * scaleFactor
            }
        }
        canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
        y += 25f * scaleFactor
    }

    // -------- EXPERIENCE ----------
    if (!resume.experience.isNullOrEmpty()) {
        canvas.drawText("EXPERIENCE", 80f, y, headerPaint)
        y += 18f * scaleFactor
        resume.experience.forEach { exp ->
            if (exp.company.isNotBlank()) {
                canvas.drawText("• ${exp.company}", 80f, y, boldTextPaint)
                val compWidth = boldTextPaint.measureText("• ${exp.company}")
                canvas.drawText(" — (${exp.role})", 80f + compWidth, y, italicPaint)
                canvas.drawText("${exp.startMonthYear} - ${exp.endMonthYear}", (width - 80).toFloat(), y, datePaint)
                y += 14f * scaleFactor
                y = drawMultiLine(canvas, exp.responsibilities, 100f, y, width - 180, textPaint, 14f * scaleFactor)
                y += 8f * scaleFactor
            }
        }
        canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
        y += 25f * scaleFactor
    }

    // -------- EDUCATION ----------
    canvas.drawText("EDUCATION", 80f, y, headerPaint)
    y += 18f * scaleFactor

    resume.higherEducation?.forEach { qual ->
        canvas.drawText("• ${qual.degree}, ${qual.institution}", 80f, y, boldTextPaint)
        canvas.drawText("${qual.startYear} - ${qual.endYear}", (width - 80).toFloat(), y, datePaint)
        y += 14f * scaleFactor
        canvas.drawText("CGPA: ${qual.CGPA}", 100f, y, textPaint)
        y += 14f * scaleFactor
    }

    resume.school12Education?.let { edu ->
        if (edu.school12.isNotBlank()) {
            canvas.drawText("• Class 12, ${edu.school12}", 80f, y, textPaint)
            canvas.drawText("${edu.startYear12} - ${edu.endYear12}", (width - 80).toFloat(), y, datePaint)
            y += 14f * scaleFactor
            canvas.drawText("Percentage: ${edu.percentage12}%", 100f, y, textPaint)
            y += 14f * scaleFactor
        }
    }

    resume.school10Education?.let { edu ->
        if (edu.school10.isNotBlank()) {
            canvas.drawText("• Class 10, ${edu.school10}", 80f, y, textPaint)
            canvas.drawText("${edu.startYear10} - ${edu.endYear10}", (width - 80).toFloat(), y, datePaint)
            y += 14f * scaleFactor
            canvas.drawText("Percentage: ${edu.percentage10}%", 100f, y, textPaint)
            y += 10f * scaleFactor
        }
    }
    canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
    y += 25f * scaleFactor

    // -------- CERTIFICATIONS ----------
    if (!resume.certificate.isNullOrEmpty()) {
        canvas.drawText("CERTIFICATIONS", 80f, y, headerPaint)
        y += 18f * scaleFactor
        resume.certificate.forEach { cert ->
            if (cert.topic.isNotBlank()) {
                canvas.drawText("• ${cert.topic} — ${cert.association}", 80f, y, textPaint)
                canvas.drawText("${cert.startMonthYear} - ${cert.endMonthYear}", (width - 80).toFloat(), y, datePaint)
                y += 14f * scaleFactor
            }
        }
    }

    // --- Save output compressed stream ---
    context.contentResolver.openOutputStream(outputURI)?.use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
}

private fun drawMultiLine(
    canvas: Canvas,
    text: String,
    x: Float,
    startY: Float,
    maxWidth: Int,
    paint: Paint,
    lineHeight: Float
): Float {
    val words = text.split(Regex("\\s+"))
    var y = startY
    var line = ""
    for (w in words) {
        val trial = if (line.isEmpty()) w else "$line $w"
        if (paint.measureText(trial) > maxWidth) {
            canvas.drawText(line, x, y, paint)
            y += lineHeight
            line = w
        } else {
            line = trial
        }
    }
    if (line.isNotEmpty()) {
        canvas.drawText(line, x, y, paint)
        y += lineHeight
    }
    return y
}
