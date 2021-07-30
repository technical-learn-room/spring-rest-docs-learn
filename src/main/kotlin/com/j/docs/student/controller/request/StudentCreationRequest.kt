package com.j.docs.student.controller.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class StudentCreationRequest(
    @get:NotNull
    val student: StudentInfo,
) {

    data class StudentInfo(
        @get:NotBlank
        val firstName: String,

        @get:NotBlank
        val lastName: String,

        @get:Positive
        val grade: Int,

        @get:Positive
        val classroom: Int,

        @get:Positive
        val number: Int,
    )
}