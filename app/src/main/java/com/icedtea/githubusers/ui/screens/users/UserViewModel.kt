package com.icedtea.githubusers.ui.screens.users

import android.os.UserManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.icedtea.githubusers.data.users.UsersDataSource
import com.icedtea.githubusers.domain.PreferenceStorage
import com.icedtea.githubusers.domain.users.UsersRepository
import com.icedtea.githubusers.utils.DEFAULT_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val repository: UsersRepository,
) : ViewModel() {

    fun saveAccessToken(token: String) = viewModelScope.launch {
        preferenceStorage.saveAccessToken(token)
    }

    val usersPager = Pager(
        PagingConfig(pageSize = DEFAULT_PAGE_SIZE)
    ) {
        UsersDataSource(repository)
    }.flow.cachedIn(viewModelScope)

}