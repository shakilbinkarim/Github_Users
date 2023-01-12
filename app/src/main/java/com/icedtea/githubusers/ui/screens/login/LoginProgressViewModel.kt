package com.icedtea.githubusers.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icedtea.githubusers.domain.login.LoginRepository
import com.icedtea.githubusers.utils.CLIENT_ID
import com.icedtea.githubusers.utils.CLIENT_SECRET
import com.icedtea.githubusers.utils.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginProgressViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private var loginJob: Job? = null

    private val _onLoginAttempt = Channel<Either<String, String>>()
    val onLoginAttempt: Flow<Either<String, String>> = _onLoginAttempt.receiveAsFlow()

    fun login(code: String) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            val res = loginRepository.login(
                clientId = CLIENT_ID,
                clientSecret = CLIENT_SECRET,
                code = code
            )
            _onLoginAttempt.send(res)
        }
    }
}