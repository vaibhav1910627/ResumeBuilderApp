package com.example.myresumeapp.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
val LocalStepProgress = compositionLocalOf<Float?> { null }

@Composable
fun StepProgressLine(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0.05f, 1f),
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "stepProgress"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val totalWith = this.maxWidth
        val stopperX = (totalWith * animatedProgress)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
        )
        Box(
            modifier = Modifier
                .width(stopperX)
                .height(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        // 3. Vertical Stopper Pin
        Box(
            modifier = Modifier
                .offset(x = (stopperX - 3.dp).coerceAtLeast(0.dp))
                .width(6.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeTopAppBar(
    title: String,
    onBack: (() -> Unit)? = null,
    onPreviewClick: (() -> Unit)? = null,
    atsScore: Int? = null,
    onAtsClick: (() -> Unit)? = null
) {
    val drawerState = LocalDrawerState.current
    val scope = LocalCoroutineScope.current

    val currentAtsScore = atsScore ?: LocalAtsScore.current
    val currentOnAtsClick = onAtsClick ?: LocalOnAtsClick.current
    val currentProgress = LocalStepProgress.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    ) {
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
                        label = {
                            Text("ATS ${currentAtsScore}%", fontWeight = FontWeight.Bold)
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

                if (onPreviewClick != null) {
                    IconButton(onClick = onPreviewClick) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Live Preview"
                        )
                    }
                }

            }
        )
        if (currentProgress != null) {

            Spacer(modifier = Modifier.height(2.dp))
            StepProgressLine(progress = currentProgress)

        }
    }
}