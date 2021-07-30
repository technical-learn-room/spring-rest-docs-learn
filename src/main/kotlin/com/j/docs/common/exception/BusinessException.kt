package com.j.docs.common.exception

import org.springframework.http.HttpStatus

open class BusinessException(
    val errorCode: String,
    val errorMessage: String,
    val httpStatusCode: HttpStatus,
) : RuntimeException(errorMessage)