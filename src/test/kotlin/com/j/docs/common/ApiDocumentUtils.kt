package com.j.docs.common

import org.springframework.restdocs.operation.preprocess.Preprocessors.*

fun getDocumentRequest() =
    preprocessRequest(
        modifyUris()
            .scheme("http")
            .host("127.0.0.1")
            .port(6180),
        prettyPrint(),
    )

fun getDocumentResponse() = preprocessResponse(prettyPrint())