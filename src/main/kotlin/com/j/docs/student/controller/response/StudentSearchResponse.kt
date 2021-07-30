package com.j.docs.student.controller.response

data class StudentSearchResponse(
    val student: StudentInfo,
) {

    data class StudentInfo(
        val id: Long,
        val name: String,
        val gradeClassNumber: String,
    )
}