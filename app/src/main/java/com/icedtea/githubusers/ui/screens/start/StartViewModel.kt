package com.icedtea.githubusers.ui.screens.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icedtea.githubusers.domain.PreferenceStorage
import com.icedtea.githubusers.utils.FLOW_SUBSCRIPTION_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    val token: StateFlow<String?> = preferenceStorage.accessToken.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIME),
        null
    )

}