package com.icedtea.githubusers.ui.screens.userdetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.icedtea.githubusers.R
import com.icedtea.githubusers.domain.users.UserDetail
import com.icedtea.githubusers.ui.common.SnackbarHostComposable
import com.icedtea.githubusers.ui.common.launchSnackBar
import kotlinx.coroutines.flow.collectLatest

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
    SnackbarHostComposable(hostState = snackState)
    LaunchedEffect(key1 = Unit){
        viewModel.error.collectLatest {
            launchSnackBar(
                message = it,
                snackState = snackState,
                scope = scope,
                actionLabel = defaultSnackbarActionLabel
            )
        }
    }

    val userDetails by viewModel.userDetail.collectAsState(initial = null)

    LazyColumn(
        modifier = Modifier.padding(10.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userDetails?.let {
            item {
                UserNameAndPhoto(it)
            }
            item {Spacer(modifier = Modifier.size(20.dp))}
            item { UserDetails(user = it)}
        }
    }
}

@Composable
fun UserNameAndPhoto(
    user: UserDetail
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.avatarUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(
                    R.drawable.ic_launcher_background
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .width(300.dp)
                    .height(300.dp)

            )
            Text(
                text = user.login ?: "Name not available!",
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun UserDetails(
    user: UserDetail
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            UserInfoRow("Name:", user.name)
            UserInfoRow("Email:", user.email)
            UserInfoRow("Company:", user.company)
            UserInfoRow("Followers:", user.followers.toString())
            UserInfoRow("Following:", user.following.toString())
        }
    }
}

@Composable
private fun UserInfoRow(label: String, value: String?) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1.0f)
        )
        Text(
            text = value ?: "Not available",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1.0f)
        )
    }
}