package com.icedtea.githubusers.data.users.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<UsersRemoteKeys>)

    @Query("Select * From user_remote_key Where id = :id")
    suspend fun getRemoteKeyByUserId(id: Int): UsersRemoteKeys?

    @Query("Delete From user_remote_key")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From user_remote_key Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}