package com.example.myresumeapp.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Global CompositionLocals so any screen can access the drawer state automatically
val LocalDrawerState = compositionLocalOf<DrawerState?> { null }
val LocalCoroutineScope = compositionLocalOf<CoroutineScope?> { null }

val LocalAtsScore = compositionLocalOf<Int?> { null }
val LocalOnAtsClick = compositionLocalOf<(() -> Unit)?> { null }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeTopAppBar(
    title: String,
    onBack: (() -> Unit)? = null,
    onPreviewClick: (() -> Unit)?= null,
    atsScore: Int? = null,
    onAtsClick: (() -> Unit)? = null
) {
    val drawerState = LocalDrawerState.current
    val scope = LocalCoroutineScope.current

    val currentAtsScore = atsScore ?: LocalAtsScore.current
    val currentOnAtsClick = onAtsClick ?: LocalOnAtsClick.current
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {

            if (drawerState != null && scope != null) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Navigation Drawer"
                    )
                }
            }
        },
        actions = {
            if (currentAtsScore != null && currentOnAtsClick != null) {
                AssistChip(
                    onClick = currentOnAtsClick,
                    label = { Text("ATS ${currentAtsScore}%", fontWeight = FontWeight.Bold)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "ATS Score",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            if(onPreviewClick != null) {
                IconButton(onClick = onPreviewClick) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Live Preview"
                    )
                }
            }

        }
    )
}