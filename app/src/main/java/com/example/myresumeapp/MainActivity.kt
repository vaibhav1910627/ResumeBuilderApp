package com.example.myresumeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myresumeapp.navigation.AppNavigation
import com.example.myresumeapp.core.theme.MyResumeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //i changed this while impplementing hilt
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
