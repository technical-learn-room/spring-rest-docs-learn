package com.j.docs.student.controller

import com.j.docs.common.response.CommonResponse
import com.j.docs.student.controller.request.StudentCreationRequest
import com.j.docs.student.controller.response.StudentAllSearchResponse
import com.j.docs.student.controller.response.StudentSearchResponse
import com.j.docs.student.service.StudentCreationService
import com.j.docs.student.service.StudentSearchService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/students")
class StudentController(
    private val studentCreationService: StudentCreationService,
    private val studentSearchService: StudentSearchService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registerStudent(
        @RequestBody @Valid request: StudentCreationRequest,
    ): CommonResponse<Nothing> {
        studentCreationService.create(
            studentFirstName = request.student.firstName,
            studentLastName = request.student.lastName,
            studentGrade = request.student.grade,
            studentClassroom = request.student.classroom,
            studentNumber = request.student.number,
        )
        return CommonResponse.createEmptyResponse()
    }

    @GetMapping("/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    fun searchStudent(
        @PathVariable("studentId") studentId: Long,
    ) = CommonResponse.createDataResponse(
        responseData = studentSearchService.search(studentId)
            .let {
                StudentSearchResponse(
                    student = StudentSearchResponse.StudentInfo(
                        id = it.studentId,
                        name = it.studentName,
                        gradeClassNumber = it.studentGradeClassNumber,
                    )
                )
            }
    )

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun searchAllStudent() =
        CommonResponse.createDataResponse(
            responseData = StudentAllSearchResponse(
                students = studentSearchService.searchAll()
                    .map {
                        StudentAllSearchResponse.StudentInfo(
                            id = it.studentId,
                            name = it.studentName,
                            gradeClassNumber = it.studentGradeClassNumber,
                        )
                    }
            )
        )
}