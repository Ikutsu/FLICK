package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.utils.ResponseResult

interface PreferencesRepository {
    suspend fun getJwt(): ResponseResult.Success<String>
    suspend fun setJwt(token: String): ResponseResult<String>

    suspend fun getUsername(): ResponseResult.Success<String>
    suspend fun setUsername(newUsername: String): ResponseResult<String>

    suspend fun getUserId(): ResponseResult.Success<String>
    suspend fun setUserId(newUserId: String): ResponseResult<String>

    suspend fun getEmail(): ResponseResult.Success<String>
    suspend fun setEmail(newEmail: String): ResponseResult<String>

    suspend fun getIsFirstTime(): ResponseResult.Success<Boolean>
    suspend fun setIsFirstTime(newValue: Boolean): ResponseResult<String>

    suspend fun clear(): ResponseResult<String>
}