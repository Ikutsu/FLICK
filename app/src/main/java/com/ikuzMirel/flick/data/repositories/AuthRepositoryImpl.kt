package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.dto.login.request.LoginRequestDto
import com.ikuzMirel.flick.data.remote.auth.AuthRemote
import com.ikuzMirel.flick.data.dto.signup.request.SignupRequestDto
import com.ikuzMirel.flick.data.utils.ResponseResult
import com.ikuzMirel.flick.data.remote.websocket.WebSocketService
import com.ikuzMirel.flick.data.utils.UNAUTHENTICATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemote,
    private val preferencesRepository: PreferencesRepository,
    private val webSocketRepository: WebSocketService
): AuthRepository {

    override suspend fun login(request: LoginRequestDto): Flow<ResponseResult<String>> {
        return flow {
            when (val response = remote.login(request)) {
                is ResponseResult.Error -> {
                    emit(ResponseResult.error(response.errorMessage.error!!))
                }
                is ResponseResult.Success -> {
                    if (response.data?.data != null) {
                        preferencesRepository.setJwt(response.data.data.token)
                        preferencesRepository.setUsername(response.data.data.username)
                        preferencesRepository.setUserId(response.data.data.userId)
                        emit(ResponseResult.success())
                    }
                }
            }
        }
    }

    override suspend fun signUp(request: SignupRequestDto): Flow<ResponseResult<String>> {
        return flow {
            when (val response = remote.signup(request)) {
                is ResponseResult.Error -> {
                    println("$response AuthRepo")
                    emit(ResponseResult.error(response.errorMessage))
                }
                is ResponseResult.Success -> {
                    println("$response AuthRepo")
                    emit(ResponseResult.success())
                }
            }
        }
    }

    override suspend fun authenticate(): Flow<ResponseResult<String>> {
        return flow {

            val token = preferencesRepository.getJwt().data
            if (token == null || token.isBlank()) {
                emit(ResponseResult.error(UNAUTHENTICATED))
                return@flow
            }

            val response = remote.authenticate(token)
            if (response is ResponseResult.Error) {
                emit(ResponseResult.error(response.errorMessage))
                return@flow
            }

            emit(ResponseResult.success())
        }
    }
}