package com.j.docs.student.service

import com.j.docs.student.dto.StudentSearchDto
import com.j.docs.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentSearchService(
    private val studentRepository: StudentRepository,
) {

    fun search(studentId: Long) =
        studentRepository.findById(studentId)
            .let {
                StudentSearchDto(
                    studentId = it.id!!,
                    studentName = it.name,
                    studentGradeClassNumber = it.gradeClassNumber,
                )
            }

    fun searchAll() =
        studentRepository.findAll()
            .map {
                StudentSearchDto(
                    studentId = it.id!!,
                    studentName = it.name,
                    studentGradeClassNumber = it.gradeClassNumber,
                )
            }
}