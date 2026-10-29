package com.example.myresumeapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import com.example.myresumeapp.data.ResumeData

fun generateResumeImageToURI(
    context: Context,
    resume: ResumeData,
    photo: Bitmap?,
    outputURI: Uri
) {
    // A4 dimensions at 150 DPI for better image quality
    val width = 1240
    val height = 1754
    val scaleFactor = 2.0f // Scale logic to match your PDF layout coordinates

    val centerX = (width / 2).toFloat()

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Set Background to White
    canvas.drawColor(Color.WHITE)

    // --- Paints (Scaled for higher resolution) ---
    val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 30f * scaleFactor
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }

    val centeredDetailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 12f * scaleFactor
        textAlign = Paint.Align.CENTER
        color = Color.DKGRAY
    }

    val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 14f * scaleFactor
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = Color.BLACK
    }

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 12f * scaleFactor }

    val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 11f * scaleFactor
        textAlign = Paint.Align.RIGHT
        color = Color.GRAY
    }

    val divider = Paint().apply { color = Color.LTGRAY; strokeWidth = 1.5f * scaleFactor }

    var y = 80f * scaleFactor

    // -------- Photo (Top Center) ----------
    photo?.let {
        val size = (80 * scaleFactor).toInt()
        val scaled = Bitmap.createScaledBitmap(it, size, size, true)
        canvas.drawBitmap(scaled, (width / 2 - size / 2).toFloat(), y, null)
        y += size + (20f * scaleFactor)
    }

    // -------- Centered Personal Details ----------
    val capitalizedName = resume.personal.name.uppercase()
    canvas.drawText(capitalizedName, centerX, y, namePaint)
    y += 30f * scaleFactor

    val contactInfo = "${resume.personal.email}  |  ${resume.personal.phone}  |  ${resume.personal.location}"
    canvas.drawText(contactInfo, centerX, y, centeredDetailPaint)
    y += 20f * scaleFactor

    val links = listOfNotNull(
        resume.personal.githublink?.takeIf { it.isNotBlank() },
        resume.personal.linkedinlink?.takeIf { it.isNotBlank() }
    ).joinToString("  •  ")

    if (links.isNotEmpty()) {
        canvas.drawText(links, centerX, y, centeredDetailPaint)
        y += 25f * scaleFactor
    }

    y += 10f * scaleFactor
    canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
    y += 40f * scaleFactor

    // -------- Education Section ----------
    canvas.drawText("EDUCATION", 80f, y, headerPaint)
    y += 25f * scaleFactor

    resume.higherEducation?.forEach { qual ->
        canvas.drawText("${qual.degree} ", 80f, y, textPaint)
        canvas.drawText("${qual.startYear} - ${qual.endYear}", (width - 80).toFloat(), y, datePaint)
        y += 20f * scaleFactor
        canvas.drawText("${qual.institution}", 80f, y, textPaint)
        canvas.drawText("${qual.CGPA} CGPA", (width - 80).toFloat(), y, datePaint)
        y += 25f * scaleFactor
    }

    // Class 12
    resume.school12Education?.let { edu ->
        canvas.drawText("SENIOR SECONDARY EDUCATION", 80f, y, textPaint)
        canvas.drawText("${edu.startYear12} - ${edu.endYear12}", (width - 80).toFloat(), y, datePaint)
        y += 20f * scaleFactor
        canvas.drawText("${edu.school12} (${edu.board12})", 80f, y, textPaint)
        canvas.drawText("${edu.percentage12}%", (width - 80).toFloat(), y, datePaint)
        y += 25f * scaleFactor
    }

    // Class 10
    resume.school10Education?.let { edu ->
        canvas.drawText("SECONDARY EDUCATION (CLASS 10)", 80f, y, textPaint)
        canvas.drawText("${edu.startYear10} - ${edu.endYear10}", (width - 80).toFloat(), y, datePaint)
        y += 20f * scaleFactor
        canvas.drawText("${edu.school10} (${edu.board10})", 80f, y, textPaint)
        canvas.drawText("${edu.percentage10}%", (width - 80).toFloat(), y, datePaint)
        y += 30f * scaleFactor
    }

    y += 10f * scaleFactor
    canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
    y += 40f * scaleFactor

    // -------- Skills Section ----------
    canvas.drawText("SKILLS", 80f, y, headerPaint)
    y += 25f * scaleFactor

//    if (resume.skills.skill.isNotBlank()) {
//        val skillPaint = Paint(textPaint).apply { color = Color.GRAY; textSize = 11f * scaleFactor }
//        val skillList = resume.skills.skill.split(",").map { it.trim() }.filter { it.isNotEmpty() }
//        skillList.forEach { skill ->
//            canvas.drawText("• $skill", 90f, y, skillPaint)
//            y += 20f * scaleFactor
//        }
//    }

    y += 10f * scaleFactor
    canvas.drawLine(80f, y, (width - 80).toFloat(), y, divider)
    y += 40f * scaleFactor

    // -------- Experience Section ----------
    canvas.drawText("EXPERIENCE", 80f, y, headerPaint)
    y += 25f * scaleFactor

    resume.experience.forEach { exp ->
        val boldText = Paint(textPaint).apply { typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) }
        canvas.drawText(exp.company, 80f, y, boldText)
        canvas.drawText("${exp.startMonthYear} - ${exp.endMonthYear}", (width - 80).toFloat(), y, datePaint)
        y += 20f * scaleFactor

        val italicPaint = Paint(textPaint).apply { typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC); color = Color.DKGRAY }
        canvas.drawText(exp.role, 80f, y, italicPaint)
        y += 22f * scaleFactor

        // Reuse multiline logic (scaled)
        y = drawMultiLine(canvas, exp.responsibilities, 80f, y, width - 160, textPaint, 20f * scaleFactor)
        y += 20f * scaleFactor
    }

    // --- Save to URI ---
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
