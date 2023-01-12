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

    private val _userDetail = Channel<UserDetail>()
    val userDetail: Flow<UserDetail> = _userDetail.receiveAsFlow()

    private val _error = Channel<String>()
    val error: Flow<String> = _error.receiveAsFlow()

    fun loadUserDetails(userName: String) {
        userDetailJob?.cancel()
        userDetailJob = viewModelScope.launch {
            val res = repository.getUserDetail(
                userName
            )
            res.suspendedFold(
                {
                    _error.send(it)
                }
            ) {
                _userDetail.send(it)
            }
        }
    }

}