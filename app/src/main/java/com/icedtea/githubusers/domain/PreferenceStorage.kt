package com.icedtea.githubusers.domain

import kotlinx.coroutines.flow.Flow

/***
 * A storage utility to persist data which will be available until the app is uninstalled or
 * [clearPreferenceStorage] is called. The variables and corresponding functions are
 * suspending in nature and throws [Exception] when there is some error in saving the data.
 */
interface PreferenceStorage {

    val accessToken: Flow<String>
    suspend fun setAccessToken(time: String)


    /***
     * clears all the stored data
     */
    suspend fun clearPreferenceStorage()
}