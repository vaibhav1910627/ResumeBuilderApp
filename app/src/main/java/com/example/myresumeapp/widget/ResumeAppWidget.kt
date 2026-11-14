package com.example.myresumeapp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.room.Room
import com.example.myresumeapp.MainActivity
import com.example.myresumeapp.data.local.ResumeDatabase
import com.example.myresumeapp.data.model.ResumeData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResumeAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val resumeData = withContext(Dispatchers.IO) {
            runCatching {
                val db =
                    Room.databaseBuilder(context, ResumeDatabase::class.java, "resume_database")
                        .build()
                val entity = db.resumeDao().getResume()
                db.close()
                entity?.let {
                    Gson().fromJson(it.resumeJson, ResumeData::class.java)
                }
            }.getOrNull()
        }

        provideContent {
            GlanceTheme {
                WidgetContent(context, resumeData)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    fun WidgetContent(context: Context, resume: ResumeData?) {
        val intent = Intent(context, MainActivity::class.java)

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .clickable(actionStartActivity(intent)),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "My Resume",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ColorProvider(Color.DarkGray)
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            if (resume != null && resume.personal.name.isNotBlank()) {

                Text(
                    text = "Build and export your professional resume",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ColorProvider(Color.Black)
                    )
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = resume.career.careerDescription.ifBlank { "Professional Resume" },
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(Color.Gray)
                    ),
                    maxLines = 1
                )
            } else {
                Text(
                    text = "Tap to create your resume",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ColorProvider(Color.Gray)
                    )
                )
            }
        }
    }
}