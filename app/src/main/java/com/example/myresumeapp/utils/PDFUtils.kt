package com.example.myresumeapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.data.Achievements
import com.example.myresumeapp.data.CareerObjective
import com.example.myresumeapp.data.Certificates
import com.example.myresumeapp.data.Experience
import com.example.myresumeapp.data.HigherEducation
import com.example.myresumeapp.data.Language
import com.example.myresumeapp.data.PersonalDetails
import com.example.myresumeapp.data.Project
import com.example.myresumeapp.data.ResumeData
import com.example.myresumeapp.data.School10Education
import com.example.myresumeapp.data.School12Education
import com.example.myresumeapp.data.Skills
import com.example.myresumeapp.data.Skills2
import com.example.myresumeapp.data.Skills3

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842

fun generateResumePDFToURI(
    context: Context,
    resume: ResumeData,
    photo: Bitmap?,
    outputURI: Uri
) {
    val pdf = android.graphics.pdf.PdfDocument()
    val pageInfo =
        android.graphics.pdf.PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
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
        textSize = 28f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        color = primaryColor
    }

    // Paint for Personal Details (Centered)
    val centeredDetailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 11f
        textAlign = Paint.Align.CENTER
        color = primaryColor
    }

    val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 13f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = headerColor
    }

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 11f
        color = primaryColor
    }
//    val textPaint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 12f }
    val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 10.5f
        textAlign = Paint.Align.RIGHT
//        color = Color.GRAY
        color = primaryColor
    }
    val divider = Paint().apply { color = dividerColor
        strokeWidth = 1f }

    var y = 50f

    // -------- Photo (Top Center) ----------
    photo?.let {
        val size = 80
        val scaled = Bitmap.createScaledBitmap(it, size, size, true)
        canvas.drawBitmap(scaled, (pageWidth / 2 - size / 2).toFloat(), y, null)
        y += size + 20f
    }

    // -------- Centered Personal Details ----------
    val capitalizedName = resume.personal.name.uppercase()
    canvas.drawText(capitalizedName, centerX, y, namePaint)
    y += 25f

    val contactInfo =
        "${resume.personal.email}  |  ${resume.personal.phone}  |  ${resume.personal.location}"
    canvas.drawText(contactInfo, centerX, y, centeredDetailPaint)
    y += 18f

    val links = listOfNotNull(
        resume.personal.githublink.takeIf { it.isNotBlank() },
        resume.personal.linkedinlink.takeIf { it.isNotBlank() }
    ).joinToString("  •  ")

    if (links.isNotEmpty()) {
        canvas.drawText(links, centerX, y, centeredDetailPaint)
        y += 20f
    }

    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 20f

    // -------- Career Objective Section ----------
    canvas.drawText("Career Objective", 40f, y, headerPaint)
    y += 20f

    resume.career.let { car ->
        y = drawMultiLine(canvas, car.careerDescription, 40f, y, pageWidth - 80, textPaint)
    }
    
//    y += 5f
    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 20f

    // -------- EDUCATION Section ----------
    canvas.drawText("EDUCATION", 40f, y, headerPaint)
    y += 20f

    resume.higherEducation.forEach { qual ->
        canvas.drawText("• ${qual.degree}", 40f, y, textPaint)
        canvas.drawText(
            "${qual.startYear} - ${qual.endYear}",
            (pageWidth - 40).toFloat(),
            y,
            datePaint
        )
        y += 16f
        canvas.drawText(qual.institution, 55f, y, textPaint)
        canvas.drawText(
            "${qual.CGPA} CGPA",
            (pageWidth - 40).toFloat(),
            y,
            datePaint
        )
        y += 16f
    }

    resume.school12Education.let { edu ->
        if (edu.school12.isNotBlank()) {
            canvas.drawText("• SENIOR SECONDARY EDUCATION", 40f, y, textPaint)
            canvas.drawText(
                "${edu.startYear12} - ${edu.endYear12}",
                (pageWidth - 40).toFloat(),
                y,
                datePaint
            )
            y += 16f
            canvas.drawText("${edu.school12} (${edu.board12})", 55f, y, textPaint)
            canvas.drawText(
                "${edu.percentage12}%",
                (pageWidth - 40).toFloat(),
                y,
                datePaint
            )
            y += 16f
        }
    }

    resume.school10Education.let { edu ->
        if (edu.school10.isNotBlank()) {
            canvas.drawText("• SECONDARY EDUCATION", 40f, y, textPaint)
            canvas.drawText(
                "${edu.startYear10} - ${edu.endYear10}",
                (pageWidth - 40).toFloat(),
                y,
                datePaint
            )
            y += 16f
            canvas.drawText("${edu.school10} (${edu.board10})", 55f, y, textPaint)
            canvas.drawText(
                "${edu.percentage10}%",
                (pageWidth - 40).toFloat(),
                y,
                datePaint
            )
            y += 10f
        }
    }

//    y += 10f
    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 24f

    // -------- EXPERIENCE SECTION ----------
    if (resume.experience.any { it.company.isNotBlank() }) {
        canvas.drawText("EXPERIENCE", 40f, y, headerPaint)
        y += 20f
        resume.experience.forEach { exp ->
            val boldText = Paint(textPaint).apply { typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) }
            val italicPaint = Paint(textPaint).apply { typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC); color = Color.DKGRAY }

            canvas.drawText("• ${exp.company}", 40f, y, boldText)

            val companyWidthWithBullet = boldText.measureText("• ${exp.company}")
            canvas.drawText(" (${exp.role})", 40f + companyWidthWithBullet, y, italicPaint)

            canvas.drawText("${exp.startMonthYear} - ${exp.endMonthYear}", (pageWidth - 40).toFloat(), y, datePaint)
            y += 16f
            y = drawMultiLine(canvas, exp.responsibilities, 55f, y, pageWidth - 80, textPaint)
//            y += 10f
        }
//        y += 10f
        canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
        y += 20f
    }
    // -------- PROJECTS SECTION ----------
    if (resume.projects.any { it.projectName.isNotBlank() }) {
        canvas.drawText("PROJECTS", 40f, y, headerPaint)
        y += 20f
        resume.projects.forEach { proj ->
            val projTitlePaint = Paint(textPaint).apply { typeface = Typeface.DEFAULT_BOLD }
            canvas.drawText("• ${proj.projectName}", 40f, y, projTitlePaint)
            val projectsWidthWithBullet = projTitlePaint.measureText("• ${proj.projectName}")
            canvas.drawText(" (${proj.technology})", 40f + projectsWidthWithBullet, y, Paint(textPaint).apply { textSize = 12f; color = Color.DKGRAY })
            y += 16f
            y = drawMultiLine(canvas, proj.description, 55f, y, pageWidth - 90, textPaint)
            y += 10f
        }
//        y += 10f
        canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
        y += 20f
    }

    // -------- CERTIFICATIONS SECTION ----------
    if (resume.certificate.any { it.topic.isNotBlank() }) {
        canvas.drawText("CERTIFICATIONS", 40f, y, headerPaint)
        y += 20f
        resume.certificate.forEach { cert ->
            canvas.drawText("• ${cert.topic}", 40f, y, textPaint)
            canvas.drawText(cert.monthYear, (pageWidth - 40).toFloat(), y, datePaint)
            y += 16f
            canvas.drawText(cert.association, 55f, y, Paint(textPaint).apply { textSize = 11f; color = Color.DKGRAY })
            y += 18f
        }
//        y += 5f
        canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
        y += 20f
    }

    // -------- Skills Section ----------
    canvas.drawText("KEY SKILLS", 40f, y, headerPaint)
    y += 20f

//    val skillPaint = Paint(textPaint).apply { color = Color.GRAY; textSize = 11f }
//    val columnWidth = (pageWidth - 80) / 3f
//
//    var currentColumn = 0
//    val startX = 45f
//
//    resume.skills.forEach { sk ->
//        if (sk.skill.isNotBlank()) {
//            canvas.drawText("• ${sk.skill}", startX + (currentColumn * columnWidth), y, skillPaint)
//            currentColumn++
//            if (currentColumn >= 3) {
//                currentColumn = 0
//                y += 18f
//            }
//        }
//    }
//    if (currentColumn != 0) y += 18f

//    y += 10f
//
    val skillPaint = Paint(textPaint).apply { color = Color.GRAY; textSize = 11f }
    val columnWidth = (pageWidth - 80) / 3f

    var currentColumn = 0
    val startX = 45f

    resume.skills.forEach { sk ->
        if (sk.skill.isNotBlank()) {
            canvas.drawText("• Tech Skill: ", startX + (currentColumn * columnWidth), y, headerPaint)
            canvas.drawText(" ${sk.skill}", 120f, y, textPaint)
            y+=10f
//            canvas.drawText("• Soft Skill: ", startX + (currentColumn * columnWidth), y, headerPaint)

//            currentColumn++
//            if (currentColumn >= 3) {
//                currentColumn = 0
//                y += 18f
//            }
            y+=8f
        }
    }
    resume.skills2.forEach { sk ->
        if (sk.skill2.isNotBlank()) {
//            canvas.drawText("• Tech Skill: ", startX + (currentColumn * columnWidth), y, headerPaint)
//            y+=10f
            canvas.drawText("• Soft Skill: ", startX + (currentColumn * columnWidth), y, headerPaint)
            canvas.drawText("${sk.skill2}", /*startX + (currentColumn * columnWidth)*/ 120f, y, textPaint)
            y+=10f
//            currentColumn++
//            if (currentColumn >= 3) {
//                currentColumn = 0
//                y += 18f
//            }
            y+=8f
        }
    }
    resume.skills3.forEach { sk ->
        if (sk.skill3.isNotBlank()) {
//            canvas.drawText("• Tech Skill: ", startX + (currentColumn * columnWidth), y, headerPaint)
//            y+=10f
            canvas.drawText("• Language:  ", startX + (currentColumn * columnWidth), y, headerPaint)
            canvas.drawText("${sk.skill3}", /*startX + (currentColumn * columnWidth)*/ 120f, y, textPaint)

            currentColumn++
            if (currentColumn >= 3) {
                currentColumn = 0
                y += 18f
            }
        }
    }
//    if (currentColumn != 0) y += 18f

    y += 10f


    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 20f


// -------- ACHIEVEMENT SECTION ----------
if (resume.achievements.any { it.achievements.isNotBlank() }) {
    canvas.drawText("Co Curricular Activities", 40f, y, headerPaint)
    y += 20f
    resume.achievements.forEach { ach ->
        canvas.drawText("• ${ach.achievements}", 40f, y, textPaint)
//        canvas.drawText(ach.achievements, (pageWidth - 40).toFloat(), y, datePaint)
        y += 16f
        canvas.drawText(ach.description, 55f, y, Paint(textPaint).apply { textSize = 11f; color = Color.DKGRAY })
        y += 18f
    }
//        y += 5f
    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
    y += 20f
}

//
//    if (resume.achievements.any { it.achievements.isNotBlank() }) {
//    canvas.drawText("Extra Curricular Activities", 40f, y, headerPaint)
//    y += 20f
//    resume.achievements.forEach { ach ->
//        canvas.drawText("• ${ach.achievements}", 40f, y, textPaint)
////        canvas.drawText(ach.achievements, (pageWidth - 40).toFloat(), y, datePaint)
//        y += 16f
//        canvas.drawText(ach.description, 55f, y, Paint(textPaint).apply { textSize = 11f; color = Color.DKGRAY })
//        y += 18f
//    }
////        y += 5f
//    canvas.drawLine(40f, y, (pageWidth - 40).toFloat(), y, divider)
//    y += 20f
//}
    // -------- LANGUAGES SECTION ----------
//    canvas.drawText("LANGUAGE", 40f, y, headerPaint)
//    y += 20f
//
//    var currentLangColumn = 0
//    resume.language.forEach { lng ->
//        if (lng.language.isNotBlank()) {
//            canvas.drawText("• ${lng.language}", startX + (currentLangColumn * columnWidth), y, skillPaint)
//            currentLangColumn++
//            if (currentLangColumn >= 3) {
//                currentLangColumn = 0
//                y += 18f
//            }
//        }
//    }
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
            y += 16f
            line = w
        } else {
            line = trial
        }
    }
    if (line.isNotEmpty()) {
        canvas.drawText(line, x, y, paint)
        y += 16f
    }
    return y
}

@Preview(showBackground = true)
@Composable
fun ResumePDFPreview() {
    val sampleData = ResumeData(
        personal = PersonalDetails(
            name = "",
            email = "",
            phone = "",
            location = "",
            githublink = "",
            linkedinlink = ""
        ),
        career = CareerObjective(careerDescription = ""),
        skills = listOf(Skills("")),
        skills2 = listOf(Skills2("")),
        skills3 = listOf(Skills3(" ")),
        school10Education = School10Education(" ","","","", ""),
        school12Education = School12Education("","","","", ""),
achievements = listOf(Achievements("", ""),Achievements("", "")),
        higherEducation = listOf(HigherEducation("", "", "", "", "")),
        certificate = listOf(Certificates("", "",""), Certificates("", "",""),
            Certificates("","", "")),
        language = listOf(Language(""), Language(""), Language("")),
        experience = listOf(Experience("", "", "", "", "")),
        projects = listOf(Project("", "", ""), Project("", "", ""))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ComposeCanvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(PAGE_WIDTH.toFloat() / PAGE_HEIGHT)
        ) {
            drawIntoCanvas { canvas ->
                // Scale the canvas to fit the preview area
                val scale = size.width / PAGE_WIDTH
                canvas.nativeCanvas.scale(scale, scale)
                
                // Draw white background
                val bgPaint = Paint().apply { color = Color.WHITE }
                canvas.nativeCanvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), bgPaint)
                
                drawResumeContent(canvas.nativeCanvas, sampleData, null)
            }
        }
    }
}
/*
*
*
*
*
*
* */
