package com.icedtea.githubusers.ui.screens.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun UsersScreen(
    viewModel: UserViewModel = hiltViewModel(),
    token: String,
    willSave: Boolean = false
) {
    LaunchedEffect(key1 = Unit) {
        if (willSave) {
            viewModel.saveAccessToken(token)
        }
    }
    UserScreenBasic(token)
}

@Composable
private fun UserScreenBasic(token: String,) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = {

            }
        ) {
            Text(text = token)
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    UserScreenBasic("dbdsbds")
}