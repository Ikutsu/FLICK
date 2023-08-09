package com.ikuzMirel.flick.data.repositories

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.ikuzMirel.flick.data.remote.auth.AuthRemote
import com.ikuzMirel.flick.data.requests.LoginRequest
import com.ikuzMirel.flick.data.requests.SignupRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.worker.SyncWorker
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val remote: AuthRemote,
    private val preferencesRepository: PreferencesRepository,
    private val context: Context
): AuthRepository {

    override suspend fun login(request: LoginRequest): Flow<BasicResponse<String>> {
        return flow {
            when (val response = remote.login(request)) {
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    if (response.data != null) {
                        preferencesRepository.setValue(PreferencesRepository.TOKEN, response.data.token)
                        preferencesRepository.setValue(PreferencesRepository.USERNAME, response.data.username)
                        preferencesRepository.setValue(PreferencesRepository.USERID, response.data.userId)
                        client.plugin(Auth).providers
                            .filterIsInstance<BearerAuthProvider>()
                            .first().clearToken()
                        WorkManager.getInstance(context).enqueueUniqueWork(
                            SyncWorker.WORK_NAME,
                            ExistingWorkPolicy.KEEP,
                            SyncWorker.startWork()
                        )
                        emit(BasicResponse.Success())
                    }
                }
            }
        }
    }

    override suspend fun signUp(request: SignupRequest): Flow<BasicResponse<String>> {
        return flow {
            when (val response = remote.signup(request)) {
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success())
                }
            }
        }
    }

    override suspend fun authenticate(): Flow<BasicResponse<String>> {
        return flow {
            val response = remote.authenticate()
            if (response is BasicResponse.Error) {
                emit(BasicResponse.Error(response.errorMessage))
                return@flow
            }

            emit(BasicResponse.Success())
        }
    }
}