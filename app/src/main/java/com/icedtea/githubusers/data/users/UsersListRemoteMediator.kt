package com.icedtea.githubusers.data.users

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.icedtea.githubusers.data.GithubUsersDatabase
import com.icedtea.githubusers.data.users.local.UsersRemoteKeys
import com.icedtea.githubusers.domain.users.User
import com.icedtea.githubusers.utils.DEFAULT_LOCAL_DATA_CACHE_OUT_TIME_HOURS
import com.icedtea.githubusers.utils.DEFAULT_PAGE_SIZE
import com.icedtea.githubusers.utils.Either
import com.icedtea.githubusers.utils.UNKNOWN_ERROR
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@OptIn(ExperimentalPagingApi::class)
@Singleton
class UsersListRemoteMediator @Inject constructor(
    private val service: UsersService,
    private val db: GithubUsersDatabase
) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        // Checking to see if we should clear local db to force
        // data being fetched from the server
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(
            DEFAULT_LOCAL_DATA_CACHE_OUT_TIME_HOURS,
            TimeUnit.HOURS
        )

        val currentTimeMillis = System.currentTimeMillis()
        val lastItemCreated = (db.getUsersRemoteKeysDao().getCreationTime() ?: 0)
        return if (
            currentTimeMillis - lastItemCreated < cacheTimeout
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        val pageKey: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.prevKey?.minus(DEFAULT_PAGE_SIZE) ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }
        }

        try {
            val response = service.getUsersList(pageKey, DEFAULT_PAGE_SIZE)
            var users: List<User> = emptyList()
            if (response.isSuccessful) {
                val body = response.body()
                val either = if (body == null) {
                    Either.Left(response.errorBody()?.string() ?: UNKNOWN_ERROR)
                } else {
                    Either.Right(body)
                }
                either.suspendedFold({
                    throw HttpException(response)
                }) {
                    users = it
                    Unit
                }
            } else {
                throw HttpException(response)
            }
            val endOfPaginationReached = users.isEmpty()

            db.withTransaction {
                val sortedUsers = users.sortedBy {
                    it.id
                }
                if (loadType == LoadType.REFRESH) {
                    db.getUsersRemoteKeysDao().clearRemoteKeys()
                    db.getUsersDao().clearAll()
                }
                val prevKey = if (pageKey > 1) max(
                    sortedUsers.first().id - 1,
                    0
                ) else null // TODO:write proper comment for logic used
                val nextKey = if (endOfPaginationReached) null else sortedUsers.last().id
                val remoteKeys = users.map {
                    UsersRemoteKeys(
                        userId = it.id,
                        prevKey = prevKey,
                        currentPage = pageKey,
                        nextKey = nextKey
                    )
                }
                db.getUsersRemoteKeysDao().insertAll(remoteKeys)
                db.getUsersDao().insertAll(users)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, User>): UsersRemoteKeys? {
        return state.anchorPosition?.let { position ->
            val closestItemToPosition = state.closestItemToPosition(position)
            closestItemToPosition?.id?.let { id ->
                db.getUsersRemoteKeysDao().getRemoteKeyByUserId(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, User>): UsersRemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { user ->
            db.getUsersRemoteKeysDao().getRemoteKeyByUserId(user.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, User>): UsersRemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { user ->
            db.getUsersRemoteKeysDao().getRemoteKeyByUserId(user.id)
        }
    }

}