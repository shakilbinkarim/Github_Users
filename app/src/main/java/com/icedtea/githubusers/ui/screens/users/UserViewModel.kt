package com.icedtea.githubusers.ui.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icedtea.githubusers.domain.PreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    fun saveAccessToken(token: String) = viewModelScope.launch {
        preferenceStorage.saveAccessToken(token)
    }



}