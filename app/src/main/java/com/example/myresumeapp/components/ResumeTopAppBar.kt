package com.example.myresumeapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Global CompositionLocals so any screen can access the drawer state automatically
val LocalDrawerState = compositionLocalOf<DrawerState?> { null }
val LocalCoroutineScope = compositionLocalOf<CoroutineScope?> { null }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeTopAppBar(
    title: String,
    onBack: (() -> Unit)? = null,
    onPreviewClick: (() -> Unit)?= null
) {
    val drawerState = LocalDrawerState.current
    val scope = LocalCoroutineScope.current

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
            if(onPreviewClick != null) {
                IconButton(onClick = onPreviewClick) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Live Preview"
                    )
                }
            }
            if (onBack != null) {
                TextButton(onClick = onBack) {
                    Text("Back")
                }
            }
        }
    )
}