package com.j.docs.student.exception

import com.j.docs.common.exception.BusinessException
import org.springframework.http.HttpStatus

class StudentNotFoundException(
    studentId: Long,
) : BusinessException(
    errorCode = "STUDENT_NOT_FOUND",
    errorMessage = "studentId가 ${studentId}인 학생을 찾을 수가 없습니다.",
    httpStatusCode = HttpStatus.NOT_FOUND,
)