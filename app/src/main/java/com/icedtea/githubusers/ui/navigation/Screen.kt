package com.icedtea.githubusers.ui.navigation

import android.content.Intent
import androidx.navigation.navDeepLink

sealed class Screen(protected val root: String) {
    object Welcome : Screen("welcome_screen") {
        val route = root
        fun navigationRoute() = root
    }

    object Home : Screen("home_screen") {
        val route = root
        val deepLinks = listOf(
            navDeepLink {
                uriPattern = "icedtea.io://testcb"
                action = Intent.ACTION_VIEW
            }
        )
        fun navigationRoute() = root
    }

    protected fun withArgs(vararg args: String?): String = buildString {
        append(root)
        args.forEach { arg ->
            arg?.let {
                append("/$it")
            }
        }
    }

}

