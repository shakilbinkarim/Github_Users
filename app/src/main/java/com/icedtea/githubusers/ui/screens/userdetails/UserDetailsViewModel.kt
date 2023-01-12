package com.icedtea.githubusers.ui.screens.userdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icedtea.githubusers.domain.users.UserDetail
import com.icedtea.githubusers.domain.users.UsersRepository
import com.icedtea.githubusers.utils.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val repository: UsersRepository
) : ViewModel() {

    private var userDetailJob: Job? = null

    private val _userDetail = Channel<Either<String, UserDetail>>()
    val userDetail: Flow<Either<String, UserDetail>> = _userDetail.receiveAsFlow()

    fun loadUserDetails(userName: String) {
        userDetailJob?.cancel()
        userDetailJob = viewModelScope.launch {
            val res = repository.getUserDetail(
                userName
            )
            _userDetail.send(res)
        }
    }

}