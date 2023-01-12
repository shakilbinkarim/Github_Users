package com.icedtea.githubusers.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icedtea.githubusers.ui.screens.login.LoginProgressScreen
import com.icedtea.githubusers.ui.screens.start.StartScreen
import com.icedtea.githubusers.ui.screens.login.LoginScreen
import com.icedtea.githubusers.ui.screens.userdetails.UserDetailsScreen
import com.icedtea.githubusers.ui.screens.users.UsersScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Start.route,
        modifier = modifier
    ) {
        composable(route = Screen.Start.route) {
            StartScreen(
                goToLoginScreen = {
                    navigateToLogin(navController)
                },
                goToUsersScreen = { token ->
                    navController.navigate(
                        Screen.Users.navigationRoute(token, false),
                    )
                }
            )
        }
        composable(route = Screen.Login.route) {
            LoginScreen()
        }
        composable(
            route = Screen.LoginProgressScreen.route,
            deepLinks = Screen.LoginProgressScreen.deepLinks,
            arguments = Screen.LoginProgressScreen.arguments
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(Screen.LoginProgressScreen.CODE_KEY)?.let { code ->
                if (code.isNotEmpty()) {
                    LoginProgressScreen(
                        code = code,
                        goToLoginScreen = { navigateToLogin(navController) },
                        goToUserScreen =  {
                            navController.navigate(
                                Screen.Users.navigationRoute(it, true)
                            )
                        }

                    )
                } else {
                    navigateToLogin(navController)
                }
            } ?: kotlin.run {
                navigateToLogin(navController)
            }
        }
        composable(
            route = Screen.Users.route,
            arguments = Screen.Users.arguments
        ) { backStackEntry ->
            backStackEntry.arguments?.let { bundle ->
                val willSave = bundle.getBoolean(Screen.Users.WILL_SAVE_KEY)
                bundle.getString(Screen.Users.TOKEN_KEY)?.let { token ->
                    if (token.isNotEmpty()) {
                        UsersScreen(
                            token = token,
                            willSave = willSave,
                            onUserClick = {
                                navController.navigate(
                                    Screen.UserDetail.navigationRoute(it)
                                )
                            }
                        )
                    } else {
                        navigateToLogin(navController)
                    }
                }
            }?: kotlin.run {
                navigateToLogin(navController)
            }
        }
        composable(
            route = Screen.UserDetail.route,
            arguments = Screen.UserDetail.arguments
        ) { backStackEntry ->
            backStackEntry.arguments?.let { bundle ->
                bundle.getString(Screen.UserDetail.USER_NAME_KEY)?.let { userName ->
                    if (userName.isNotEmpty()) {
                        UserDetailsScreen(
                            userName = userName
                        )
                    } else {
                        navigateToLogin(navController)
                    }
                }
            }?: kotlin.run {
                navigateToLogin(navController)
            }
        }
    }
}

private fun navigateToLogin(navController: NavHostController) {
    navController.navigate(Screen.Login.navigationRoute()) {
        popUpTo(Screen.Login.route) { inclusive = true }
    }
}
