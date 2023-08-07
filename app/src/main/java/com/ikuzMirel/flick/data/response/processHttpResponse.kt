package com.ikuzMirel.flick.data.response

import android.util.Log
import com.ikuzMirel.flick.data.constants.NO_INTERNET_CONNECTION_ERROR
import com.ikuzMirel.flick.data.constants.SOCKET_TIMEOUT_ERROR
import com.ikuzMirel.flick.data.constants.UNAUTHENTICATED
import com.ikuzMirel.flick.data.constants.UNKNOWN_ERROR
import com.ikuzMirel.flick.data.constants.UNKNOWN_HTTP_ERROR
import com.ikuzMirel.flick.utils.EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.NET_EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.TAG
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import java.net.ConnectException
import java.net.SocketTimeoutException

suspend inline fun <reified T> processHttpResponse(
    request: HttpResponse,
    specificError: String,
): BasicResponse<T> {
    return try {
        when (request.status.value) {
            200 -> {
                BasicResponse.Success(request.body<T>())
            }

            401 -> {
                BasicResponse.Error(UNAUTHENTICATED)
            }

            409 -> {
                BasicResponse.Error(specificError)
            }

            else -> {
                Log.e(
                    TAG,
                    NET_EXCEPTION_MESSAGE + request.status.value + request.status.description
                )
                BasicResponse.Error(UNKNOWN_HTTP_ERROR)
            }
        }
    } catch (e: ConnectException) {
        Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
        BasicResponse.Error(NO_INTERNET_CONNECTION_ERROR)
    } catch (e: SocketTimeoutException) {
        Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
        BasicResponse.Error(SOCKET_TIMEOUT_ERROR)
    } catch (e: Exception) {
        Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
        BasicResponse.Error(UNKNOWN_ERROR)
    }
}