package com.icedtea.githubusers.ui.screens.login

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.icedtea.githubusers.utils.AUTH_URL

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL))
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = {
                context.startActivity(intent)
            }
        ) {
            Text(text = "Login")
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    LoginScreen()
}