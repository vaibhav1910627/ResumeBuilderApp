package com.example.myresumeapp.uiscreens

import android.R
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myresumeapp.components.ResumeCard
import com.example.myresumeapp.data.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    uiState: UiState,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSaveDatabase: () -> Unit,
    onDownloadPDF: (Uri?) -> Unit,
    onDownloadImage: (Uri?) -> Unit
) {

    val textColor = MaterialTheme.colorScheme.onBackground
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    val createPDF = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) {
        uri->onDownloadPDF(uri)
    }
    val createImage = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("image/png")) {
            uri -> onDownloadImage(uri)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text("My Resume", color = textColor)
            },
            colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
            navigationIcon = {
                TextButton(onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                    Text("Edit", color = Color.White)
                }
            }, actions = {
                TextButton(onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                    Text("Delete", color = Color.White)
                }
            }
        )
    }){
        paddingValues ->
        Column(Modifier.padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Resume Preview", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(6.dp))

            //resume card

            ResumeCard(resume = uiState.resume, modifier = Modifier.fillMaxWidth())

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onSaveDatabase,
                    enabled = !uiState.isSaving,
                    colors = ButtonDefaults.buttonColors(Color.Gray)) {
                    Text(if (uiState.isSaving) "Saving..." else "Save", color = Color.White)
                }
                Button(onClick = {
                    createPDF.launch("Resume_${uiState.resume.personal.name.ifBlank{"My"}}.pdf")
                }, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                    Text("Download PDF", color = Color.White)
                }
                Button(onClick = {
                    createImage.launch("Resume_${uiState.resume.personal.name.ifBlank{"My"}}.pdf")
                }, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                    Text("Download as Image", color = Color.White)
                }
            }
            uiState.message?.let {
                Text(it,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}