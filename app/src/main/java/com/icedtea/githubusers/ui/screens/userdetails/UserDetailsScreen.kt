package com.icedtea.githubusers.ui.screens.userdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.icedtea.githubusers.R

@Composable
fun UserDetailsScreen(
    viewModel: UserDetailsViewModel = hiltViewModel(),
    userName: String
) {
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val defaultSnackbarActionLabel = stringResource(id = R.string.ok)

    LaunchedEffect(key1 = Unit) {
        viewModel.loadUserDetails(userName)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

    }
}