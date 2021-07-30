package com.j.docs.common.handler

import com.j.docs.common.exception.BusinessException
import com.j.docs.common.response.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun businessExceptionHandler(e: BusinessException): ResponseEntity<CommonResponse<Nothing>> =
        ResponseEntity.status(e.httpStatusCode)
            .body(CommonResponse.createErrorResponse(exception = e))
}