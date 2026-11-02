package com.example.myresumeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myresumeapp.navigation.AppNavigation
import com.example.myresumeapp.ui.theme.MyResumeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyResumeAppTheme {
                AppNavigation()
            }
        }
    }
}

///yet to fix:
//gmail on personalscreen, valid format..
        //location drop down...
//school name career objective... words than lines...
//selected skills not available...
//projects project name...
//certificates and achievement add project text...
//if no expiry date only start date option in certifications in...
// i can ask the user to create next graduation card before making a new one automatically btw...
