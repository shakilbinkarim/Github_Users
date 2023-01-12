package com.icedtea.githubusers.data.users

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.icedtea.githubusers.domain.users.User
import com.icedtea.githubusers.domain.users.UsersRepository
import com.icedtea.githubusers.utils.DEFAULT_PAGE_SIZE

class UsersDataSource(
    private val repo: UsersRepository
) : PagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(DEFAULT_PAGE_SIZE - 1) ?: page?.nextKey?.plus(DEFAULT_PAGE_SIZE - 1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val page = params.key ?: 0
            val response = repo.getUsersList(page)

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if(response.isNotEmpty()){
                    val sortedList = response.sortedBy{
                        it.id
                    }
                    sortedList.last().id
                } else {
                    null
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}