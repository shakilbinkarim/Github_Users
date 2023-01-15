package com.icedtea.githubusers.utils

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * A generic class that can provide a resource backed by both
 * the SQLite database and the network.
 *
 * [ResultType] represents the type for database.
 * [RequestType] represents the type for network.
 */
abstract class NetworkBoundResource<ResultType : Any, RequestType> {

    fun asFlow(): Flow<Either<String, ResultType>> = flow {
        val dbValue = loadFromDb().first()
        if (shouldFetch(dbValue)) {
            emit(Either.Right(dbValue))
            val apiResponse = fetchFromNetwork()
            when {
                apiResponse.isSuccessful -> {
                    apiResponse.body()?.let { saveNetworkResult(it) }
                    emitAll(loadFromDb().map { Either.Right(it) })
                }
                else -> {
                    onFetchFailed()
                    emit(
                        Either.Left(
                            apiResponse.errorBody()?.string() ?: UNKNOWN_ERROR
                        )
                    )
                }
            }
        } else {
            emitAll(loadFromDb().map { Either.Right(it) })
        }
    }.catch { e ->
        // emit errors as well as any saved data from the DB
        emit(
            Either.Left(
                e.message ?: UNKNOWN_ERROR
            )
        )
        emitAll(loadFromDb().map { Either.Right(it) })
    }

    /**
     * useful for operations like retrying the request
     */
    protected open fun onFetchFailed() {}

    @WorkerThread
    protected abstract suspend fun saveNetworkResult(result: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType>

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Response<RequestType>
}