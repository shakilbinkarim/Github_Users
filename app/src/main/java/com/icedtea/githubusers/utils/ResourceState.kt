package com.icedtea.githubusers.utils

sealed class ResourceState<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): ResourceState<T>(data)
    class Error<T>(message: String, data: T? = null): ResourceState<T>(data, message)
}
