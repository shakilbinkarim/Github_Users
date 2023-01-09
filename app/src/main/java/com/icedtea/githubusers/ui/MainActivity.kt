package com.icedtea.githubusers.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.icedtea.githubusers.ui.navigation.AppNavigation
import com.icedtea.githubusers.ui.theme.GithubUsersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubUsersTheme {
                AppNavigation()
            }
        }
    }
}
