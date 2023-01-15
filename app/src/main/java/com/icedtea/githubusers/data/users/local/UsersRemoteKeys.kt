package com.icedtea.githubusers.data.users.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_remote_key")
data class UsersRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val userId: Int,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
