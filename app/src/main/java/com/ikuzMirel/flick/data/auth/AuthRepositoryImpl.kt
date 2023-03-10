package com.ikuzMirel.flick.data.auth

import android.util.Log
import com.ikuzMirel.flick.data.user.UserPreferencesRepository
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val userPreferencesRepository: UserPreferencesRepository
): AuthRepository {

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.signIn(
                AuthRequest(
                    username = username,
                    password = password
                )
            )
            userPreferencesRepository.setJwt(response.token)
            userPreferencesRepository.setUsername(response.username)
            userPreferencesRepository.setUserId(response.userId)
            AuthResult.Authenticated()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthenticated()
            } else if (e.code() == 409) {
                AuthResult.LoginConflicted()
            } else {
                println("Net Exception: ${e.stackTraceToString()}")
                AuthResult.Error()
            }
        } catch (e: ConnectException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.NoConnectionError()
        } catch (e: JsonEncodingException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.JsonEncodingException()
        } catch (e: SocketTimeoutException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.SocketTimeoutException()
        } catch (e: Exception) {
            println("Exception: ${e.stackTraceToString()}")
            e.printStackTrace()
            AuthResult.Error()
        }
    }

    override suspend fun signUp(username: String, password: String, email: String): AuthResult<Unit> {
        return try {
            api.signUp(
                authRequest =  AuthRequest(
                    username = username,
                    password = password,
                    email = email
                )
            )
            signIn(username, password)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthenticated()
            } else if (e.code() == 409) {
                AuthResult.UsernameConflicted()
            } else {
                println("Net Exception: ${e.stackTraceToString()}")
                AuthResult.Error()
            }
        } catch (e: ConnectException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.NoConnectionError()
        } catch (e: JsonEncodingException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.JsonEncodingException()
        } catch (e: SocketTimeoutException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.SocketTimeoutException()
        } catch (e: Exception) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.Error()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = userPreferencesRepository.getJwt()
            if (token == "") {
                return AuthResult.Unauthenticated()
            }
            api.authenticate("Bearer $token")
            userPreferencesRepository.setUsername(null)
            AuthResult.Authenticated()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthenticated()
            } else {
                println("Net Exception: ${e.stackTraceToString()}")
                AuthResult.Error()
            }
        } catch (e: ConnectException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.NoConnectionError()
        } catch (e: JsonEncodingException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.JsonEncodingException()
        } catch (e: SocketTimeoutException) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.SocketTimeoutException()
        } catch (e: Exception) {
            println("Exception: ${e.stackTraceToString()}")
            AuthResult.Error()
        }
    }
}