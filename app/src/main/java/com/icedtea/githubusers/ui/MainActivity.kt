package com.icedtea.githubusers.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.CallSuper
import com.icedtea.githubusers.ui.navigation.AppNavigation
import com.icedtea.githubusers.ui.theme.GithubUsersTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubUsersTheme {
                AppNavigation()
            }
        }
    }

    @CallSuper
    override fun onNewIntent(
        @SuppressLint("UnknownNullness", "MissingNullability") intent: Intent?
    ) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
