package com.j.docs.common

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
    queryParams: List<Pair<String, Any>> = listOf(),
    pathVariables: List<Pair<String, Any>> = listOf(),
): MockHttpServletRequestBuilder =
    RestDocumentationRequestBuilders.request(
        httpMethod,
        applyPathVariable(url, pathVariables),
        pathVariables)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .content(objectMapper.writeValueAsString(requestBody))
        .queryParams(LinkedMultiValueMap<String, String>().apply {
            queryParams.forEach { (key, value) ->
                add(key, value.toString())
            }
        })

private fun applyPathVariable(url: String, pathVariables: List<Pair<String, Any>>): String {
    var urlWithPathVariableApplied = url
    pathVariables.forEach { (path, pathVariable) ->
        urlWithPathVariableApplied =
            urlWithPathVariableApplied.replace("{$path}", pathVariable.toString())
    }
    return urlWithPathVariableApplied
}

inline fun <reified T> String.toObject() =
    ObjectMapper().findAndRegisterModules().readValue<T>(this)

fun Any.toJson() =
    ObjectMapper().writeValueAsString(this)