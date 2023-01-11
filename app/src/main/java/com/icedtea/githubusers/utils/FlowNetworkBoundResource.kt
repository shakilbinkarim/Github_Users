package com.icedtea.githubusers.utils

import androidx.annotation.MainThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * A generic class that can provide a resource backed by both
 * the SQLite database and the network.
 *
 * [ResultType] represents the type for database.
 * [RequestType] represents the type for network.
 */
abstract class FlowNetworkBoundResource<ResultType, RequestType> {

    fun asFlow(): Flow<ResourceState<ResultType>> = flow {
        val dbValue = loadFromDb()
        if (shouldFetch(dbValue)) {
            val apiResponse = fetchFromNetwork()
            when {
                apiResponse.isSuccessful -> {
                    apiResponse.body()?.let {
                        saveNetworkResult(it)
                        // Emitting from db after fetching from server
                        // so that there is a single source of truth
                        emit(ResourceState.Success(loadFromDb()))
                    } ?: emit(ResourceState.Error("Couldn't load data"))
                }
                else -> {
                    emit(ResourceState.Success(dbValue))
                    onFetchFailed()
                    emit(ResourceState.Error("Couldn't load data"))
                }
            }
        } else {
            emit(ResourceState.Success(loadFromDb()))
        }
    }.catch {
        emit(ResourceState.Error("Couldn't load data"))
    }

    /**
     * useful for operations like retrying the request
     */
    protected open fun onFetchFailed() {}

    protected abstract suspend fun saveNetworkResult(result: RequestType)

    @MainThread
    protected abstract suspend fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun loadFromDb(): ResultType

    protected abstract suspend fun fetchFromNetwork(): Response<RequestType>
}