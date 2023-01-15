package com.icedtea.githubusers.data.users.local

import android.graphics.Movie
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.icedtea.githubusers.domain.users.User

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Query("Select * From users Order By id")
    fun getUsers(): PagingSource<Int, User>

    @Query("Delete From users")
    suspend fun clearAll()
}