package com.icedtea.githubusers.ui.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icedtea.githubusers.ui.screens.login.LoginScreen
import com.icedtea.githubusers.ui.screens.users.UsersScreen
import com.icedtea.githubusers.utils.REDIRECT_URL

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    HandleIntents(navController)
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(route = Screen.Welcome.route) {
            LoginScreen()
        }
        composable(
            route = Screen.Home.route,
            deepLinks = Screen.Home.deepLinks
        ) {
            UsersScreen()
        }
    }
}

@Composable
private fun HandleIntents(
    navController: NavHostController
) {
    val activity = (LocalContext.current.getActivity() as ComponentActivity)
    DisposableEffect(navController) {
        val listener = Consumer<Intent> { intent ->
            intent?.data?.let { uri ->
                if (uri.toString().startsWith(REDIRECT_URL)) {
                    navController.navigate(Screen.Home.navigationRoute())
                }
            }
        }
        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
}

fun Context.getActivity(): Activity = when (this) {
    is Activity -> {
        this
    }
    is ContextWrapper -> {
        baseContext.getActivity()
    }
    else -> {
        getActivity()
    }
}