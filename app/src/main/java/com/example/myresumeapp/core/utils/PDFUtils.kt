package com.example.myresumeapp.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.example.myresumeapp.data.model.ResumeData

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842

fun generateResumePDFToURI(
    context: Context,
    resume: ResumeData,
    photo: Bitmap?,
    outputURI: Uri
) {
    val pdf = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
    val page = pdf.startPage(pageInfo)

    drawResumeContent(page.canvas, resume, photo)

    pdf.finishPage(page)

    context.contentResolver.openOutputStream(outputURI)?.use { out ->
        pdf.writeTo(out)
    }
    pdf.close()
}

fun drawResumeContent(
    canvas: Canvas,
    resume: ResumeData,
    photo: Bitmap?
) {
    val pageWidth = PAGE_WIDTH
    val centerX = (pageWidth / 2).toFloat()

    val primaryColor = Color.rgb(26, 26, 26)
    val headerColor = Color.rgb(43, 58, 85)
    val dividerColor = Color.rgb(136, 136, 136)

    val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        color = primaryColor
    }

    val centeredDetailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 10f
        textAlign = Paint.Align.CENTER
        color = primaryColor
    }

    val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 12f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = headerColor
    }

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 10f
        color = primaryColor
    }

    val boldTextPaint = Paint(textPaint).apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val italicPaint = Paint(textPaint).apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        color = Color.DKGRAY
    }

    val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 9.5f
        textAlign = Paint.Align.RIGHT
        color = primaryColor
    }

    val divider = Paint().apply {
        color = dividerColor
        strokeWidth = 1f
    }

    var y = 45f

    // -------- Photo (Top Center) ----------
    photo?.let {
        val size = 70
        val scaled = Bitmap.createScaledBitmap(it, size, size, true)
        canvas.drawBitmap(scaled, (pageWidth / 2 - size / 2).toFloat(), y, null)
        y += size + 15f
    }

    // -------- Centered Personal Details & Links ----------
    val capitalizedName = resume.personal.name.uppercase()
    canvas.drawText(capitalizedName, centerX, y, namePaint)
    y += 20f

    val contactInfo = "${resume.personal.email}  |  ${resume.personal.phone}  |  ${resume.personal.location}"
    canvas.drawText(contactInfo, centerX, y, centeredDetailPaint)
    y += 14f

    // Including GitHub, LinkedIn, and LeetCode link elements
    val links = listOfNotNull(
        resume.personal.githublink.takeIf { it.isNotBlank() },
        resume.personal.linkedinlink.takeIf { it.isNotBlank() },
        "LeetCode" // Visual fallback anchor tag match string
    ).joinToString("  |  ")

    if (links.isNotEmpty()) {
        canvas.drawText(links, centerX, y, centeredDetailPaint)
        y += 16f
    }

    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 18f

    // -------- TECHNICAL SKILLS SECTION ----------
    canvas.drawText("TECHNICAL SKILLS", 40f, y, headerPaint)
    y += 16f

    val skillCategories = listOf(
        "Languages" to resume.skills.map { it.skill },
        "Mobile" to resume.skills2.map { it.skill2 },
        "ML & Computer Vision" to resume.skills3.map { it.skill3 },
        "IoT & Embedded" to listOf("Microcontroller board, IMU sensor, BLE, real-time systems"),
        "Backend & Web" to listOf("Web framework, REST APIs, Database, webhooks"),
        "Core CS" to listOf("Data Structures & Algorithms, OOP, Low-Latency Design"),
        "Tools" to listOf("Git, GitHub, Linux"),
        "Problem Solving" to listOf("300+ LeetCode problems solved across dynamic data structures")
    )

    skillCategories.forEach { (category, skillsList) ->
        val cleanSkills = skillsList.filter { it.isNotBlank() }.joinToString(", ")
        if (cleanSkills.isNotEmpty()) {
            canvas.drawText("$category: ", 40f, y, boldTextPaint)
            val prefixWidth = boldTextPaint.measureText("$category: ")
            y = drawMultiLine(canvas, cleanSkills, 40f + prefixWidth, y, (pageWidth - 80 - prefixWidth).toInt(), textPaint)
        }
    }
    y += 4f
    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 18f

    // -------- PROJECTS SECTION ----------
    if (resume.projects.any { it.projectName.isNotBlank() }) {
        canvas.drawText("PROJECTS", 40f, y, headerPaint)
        y += 16f
        resume.projects.forEach { proj ->
            canvas.drawText("• ${proj.projectName}", 40f, y, boldTextPaint)
            val projTitleWidth = boldTextPaint.measureText("• ${proj.projectName}")
            canvas.drawText(" — (${proj.technology})", 40f + projTitleWidth, y, italicPaint)
            y += 14f
            y = drawMultiLine(canvas, proj.description, 55f, y, pageWidth - 95, textPaint)
            y += 6f
        }
        canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
        y += 18f
    }

    // -------- EXPERIENCE SECTION ----------
    if (resume.experience.any { it.company.isNotBlank() }) {
        canvas.drawText("EXPERIENCE", 40f, y, headerPaint)
        y += 16f
        resume.experience.forEach { exp ->
            canvas.drawText("• ${exp.company}", 40f, y, boldTextPaint)
            val compWidth = boldTextPaint.measureText("• ${exp.company}")
            canvas.drawText(" — (${exp.role})", 40f + compWidth, y, italicPaint)
            canvas.drawText("${exp.startMonthYear} - ${exp.endMonthYear}", (pageWidth - 40).toFloat(), y, datePaint)
            y += 14f
            y = drawMultiLine(canvas, exp.responsibilities, 55f, y, pageWidth - 95, textPaint)
            y += 6f
        }
        canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
        y += 18f
    }

    // -------- EDUCATION SECTION ----------
    canvas.drawText("EDUCATION", 40f, y, headerPaint)
    y += 16f

    resume.higherEducation.forEach { qual ->
        canvas.drawText("• ${qual.degree}, ${qual.institution}", 40f, y, boldTextPaint)
        canvas.drawText("${qual.startYear} - ${qual.endYear}", (pageWidth - 40).toFloat(), y, datePaint)
        y += 14f
        canvas.drawText("CGPA: ${qual.CGPA}", 55f, y, textPaint)
        y += 14f
    }

    resume.school12Education.let { edu ->
        if (edu.school12.isNotBlank()) {
            canvas.drawText("• Class 12, ${edu.school12}", 40f, y, textPaint)
            canvas.drawText("${edu.startYear12} - ${edu.endYear12}", (pageWidth - 40).toFloat(), y, datePaint)
            y += 14f
            canvas.drawText("Percentage: ${edu.percentage12}%", 55f, y, textPaint)
            y += 14f
        }
    }

    resume.school10Education.let { edu ->
        if (edu.school10.isNotBlank()) {
            canvas.drawText("• Class 10, ${edu.school10}", 40f, y, textPaint)
            canvas.drawText("${edu.startYear10} - ${edu.endYear10}", (pageWidth - 40).toFloat(), y, datePaint)
            y += 14f
            canvas.drawText("Percentage: ${edu.percentage10}%", 55f, y, textPaint)
            y += 8f
        }
    }
    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 18f

    // -------- CERTIFICATIONS SECTION ----------
    if (resume.certificate.any { it.topic.isNotBlank() }) {
        canvas.drawText("CERTIFICATIONS", 40f, y, headerPaint)
        y += 16f
        resume.certificate.forEach { cert ->
            canvas.drawText("• ${cert.topic} — ${cert.association}", 40f, y, textPaint)
            canvas.drawText("${cert.startMonthYear} - ${cert.endMonthYear}", (pageWidth - 40).toFloat(), y, datePaint)
            y += 14f
        }
    }
}

private fun drawMultiLine(
    canvas: Canvas,
    text: String,
    x: Float,
    startY: Float,
    maxWidth: Int,
    paint: Paint
): Float {
    val words = text.split(Regex("\\s+"))
    var y = startY
    var line = ""
    for (w in words) {
        val trial = if (line.isEmpty()) w else "$line $w"
        if (paint.measureText(trial) > maxWidth) {
            canvas.drawText(line, x, y, paint)
            y += 14f
            line = w
        } else {
            line = trial
        }
    }
    if (line.isNotEmpty()) {
        canvas.drawText(line, x, y, paint)
        y += 14f
    }
    return y
}
