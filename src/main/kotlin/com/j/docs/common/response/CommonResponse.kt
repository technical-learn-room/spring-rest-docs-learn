package com.j.docs.common.response

import com.j.docs.common.exception.BusinessException

class CommonResponse<T> private constructor(
    val response: T?,
    val errorCode: String?,
    val errorMessage: String?,
) {
    companion object {
        fun <T> createDataResponse(
            responseData: T,
        ) = CommonResponse(
            response = responseData,
            errorCode = null,
            errorMessage = null,
        )

        fun createErrorResponse(
            exception: BusinessException,
        ) = CommonResponse(
            response = null,
            errorCode = exception.errorCode,
            errorMessage = exception.errorMessage,
        )

        fun createEmptyResponse() =
            CommonResponse(
                response = null,
                errorCode = null,
                errorMessage = null,
            )
    }
}