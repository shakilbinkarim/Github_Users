package com.icedtea.githubusers.ui.navigation

import android.content.Intent
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

sealed class Screen(protected val root: String) {

    object Start : Screen("start_screen") {
        val route = root
    }

    object Login : Screen("login_screen") {
        val route = root
        fun navigationRoute() = root
    }

    object LoginProgressScreen : Screen("login_progress_screen") {
        const val CODE_KEY = "code"

        val route = root
        val deepLinks = listOf(
            navDeepLink {
                uriPattern = "icedtea.io://testcb?$CODE_KEY={$CODE_KEY}"
                action = Intent.ACTION_VIEW
            }
        )
        val arguments = listOf(
            navArgument(CODE_KEY) {
                defaultValue = ""
            }
        )
    }

    object Users : Screen("users_screen") {
        const val TOKEN_KEY = "token"
        const val WILL_SAVE_KEY = "will_save"
        val route = "$root/{$TOKEN_KEY}?$WILL_SAVE_KEY={$WILL_SAVE_KEY}"

        val arguments = listOf(
            navArgument(TOKEN_KEY) {
                defaultValue = ""
            },
            navArgument(WILL_SAVE_KEY) {
                defaultValue = false
            }
        )

        fun navigationRoute(token: String, willSave: Boolean): String =
            "${withArgs(token)}?$WILL_SAVE_KEY=${willSave}"
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

