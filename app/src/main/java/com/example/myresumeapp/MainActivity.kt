package com.example.myresumeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myresumeapp.navigation.AppNavigation
import com.example.myresumeapp.core.theme.MyResumeAppTheme
import dagger.hilt.android.AndroidEntryPoint

//import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //i changed this while impplementing hilt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MyResumeAppTheme {
                AppNavigation()
            }
        }
    }
}