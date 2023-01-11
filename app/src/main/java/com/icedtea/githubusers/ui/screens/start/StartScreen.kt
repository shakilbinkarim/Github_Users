package com.icedtea.githubusers.ui.screens.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icedtea.githubusers.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StartScreen(
    viewModel: StartViewModel = hiltViewModel(),
    goToLoginScreen: () -> Unit,
    goToUsersScreen: (token: String) -> Unit
) {
    val appName = stringResource(id = R.string.app_name)

    LaunchedEffect(key1 = Unit) {
        viewModel.token.collectLatest { token ->
            token?.let {
                if (it.isEmpty()) {
                    goToLoginScreen()
                } else {
                    goToUsersScreen(it)
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = appName,
            style = MaterialTheme.typography.h1
        )
    }
}
