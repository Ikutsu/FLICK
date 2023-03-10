package com.ikuzMirel.flick.data.auth

import com.ikuzMirel.flick.data.user.UserInfoRequest
import com.ikuzMirel.flick.data.user.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("signIn")
    suspend fun signIn(
        @Body authRequest: AuthRequest
    ): AuthResponse

    @POST("signUp")
    suspend fun signUp(
        @Body authRequest: AuthRequest
    )

    @GET("authenticate")
    suspend fun authenticate(
        @Header ("Authorization") token: String
    )

    @POST("user")
    suspend fun getUserInfo(
        @Body userInfoRequest: UserInfoRequest
    ): UserInfoResponse
}