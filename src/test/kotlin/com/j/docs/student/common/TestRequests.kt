package com.j.docs.student.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.util.LinkedMultiValueMap
import com.fasterxml.jackson.module.kotlin.readValue

private val objectMapper = jacksonObjectMapper().findAndRegisterModules()

fun <T> createRequest(
    httpMethod: HttpMethod,
    url: String,
    requestBody: T? = null,
    vararg queryParams: Pair<String, Any>,
): MockHttpServletRequestBuilder =
    RestDocumentationRequestBuilders.request(httpMethod, url)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .content(objectMapper.writeValueAsString(requestBody))
        .queryParams(LinkedMultiValueMap<String, String>().apply {
            queryParams.forEach { (key, value) ->
                add(key, value.toString())
            }
        })

inline fun <reified T> String.toObject() =
    ObjectMapper().findAndRegisterModules().readValue<T>(this)