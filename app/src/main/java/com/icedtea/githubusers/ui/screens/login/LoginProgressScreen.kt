package com.icedtea.githubusers.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.icedtea.githubusers.R
import com.icedtea.githubusers.ui.common.SnackbarHostComposable
import com.icedtea.githubusers.ui.common.launchSnackBar
import com.icedtea.githubusers.utils.ResourceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginProgressScreen(
    viewModel: LoginProgressViewModel = hiltViewModel(),
    code: String,
    goToLoginScreen: () -> Unit,
    goToUserScreen: (String) -> Unit
) {

    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val defaultSnackbarActionLabel = stringResource(id = R.string.ok)
    val failedToLoginMsg = stringResource(id = R.string.msg_err_failed_to_login)

    LaunchedEffect(key1 = Unit) {
        viewModel.login(code)
    }

    SnackbarHostComposable(hostState = snackState)
    LaunchedEffect(key1 = Unit) {
        viewModel.onLoginAttempt.collectLatest {
            fun handleError(
                it: ResourceState<String>,
                failedToLoginMsg: String,
                snackState: SnackbarHostState,
                scope: CoroutineScope,
                defaultSnackbarActionLabel: String,
                goToLoginScreen: () -> Unit
            ) {
                launchSnackBar(
                    message = it.message ?: failedToLoginMsg,
                    snackState = snackState,
                    scope = scope,
                    actionLabel = defaultSnackbarActionLabel
                )
                goToLoginScreen()
            }

            when (it) {
                is ResourceState.Error -> {
                    handleError(
                        it,
                        failedToLoginMsg,
                        snackState,
                        scope,
                        defaultSnackbarActionLabel,
                        goToLoginScreen
                    )
                }
                is ResourceState.Success -> {
                    it.data?.let { token ->
                        goToUserScreen(token)
                    } ?: kotlin.run {
                        handleError(
                            it,
                            failedToLoginMsg,
                            snackState,
                            scope,
                            defaultSnackbarActionLabel,
                            goToLoginScreen
                        )
                    }
                }
            }
        }
    }
    LoginProgressScreenBasic()
}

@Composable
private fun LoginProgressScreenBasic() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}


@Preview
@Composable
fun LoginProgressPreview() {
    LoginProgressScreenBasic()
}