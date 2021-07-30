package com.j.docs.student.controller.response

data class StudentAllSearchResponse(
    val students: List<StudentInfo>
) {

    data class StudentInfo(
        val id: Long,
        val name: String,
        val gradeClassNumber: String,
    )
}