package com.icedtea.githubusers.ui.screens.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun UsersScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = {

            }
        ) {
            Text(text = "It's good that you know!")
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    UsersScreen()
}