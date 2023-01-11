package com.icedtea.githubusers.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.icedtea.githubusers.ui.theme.GithubUsersTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
* Have to be put in the parent composable
* of where we would like to show a snackbar.
* */
@Composable
fun SnackbarHostComposable(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
    actionOnNewLine: Boolean = false,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color = SnackbarDefaults.backgroundColor,
    contentColor: Color = MaterialTheme.colors.surface,
    elevation: Dp = 6.dp
) {
    GithubUsersTheme {
        Row(
            modifier = modifier.fillMaxHeight(),
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1.0f)
            )
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.Bottom)
            ) { data ->
                val actionLabel = data.actionLabel
                val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
                    @Composable {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colors.primary
                            ),
                            onClick = { data.performAction() },
                            content = { Text(actionLabel) }
                        )
                    }
                } else {
                    null
                }
                Snackbar(
                    modifier = modifier.padding(12.dp),
                    content = {
                        Text(
                            data.message,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    action = actionComposable,
                    actionOnNewLine = actionOnNewLine,
                    shape = shape,
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    elevation = elevation,
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1.0f)
            )
        }
    }
}

fun launchSnackBar(
    message: String,
    snackState: SnackbarHostState,
    scope: CoroutineScope,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onDismiss: () -> Unit = {},
    onActionPerformed: () -> Unit = {},
) {
    scope.launch {
        val result = snackState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        )
        when (result) {
            SnackbarResult.Dismissed -> onDismiss()
            SnackbarResult.ActionPerformed -> onActionPerformed()
        }
    }
}