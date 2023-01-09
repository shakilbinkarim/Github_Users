package com.icedtea.githubusers.di

import com.icedtea.githubusers.data.PreferenceStorageImpl
import com.icedtea.githubusers.domain.PreferenceStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/***
 * A Dagger [Module] to provide configurations specific to persistence storage
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    abstract fun providesPreferenceStorage(
        sharedPreferenceStorage: PreferenceStorageImpl
    ): PreferenceStorage
}