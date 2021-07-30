package com.j.docs.student.service

import com.j.docs.student.repository.Student
import com.j.docs.student.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentCreationService(
    private val studentRepository: StudentRepository,
) {

    fun create(
        studentFirstName: String,
        studentLastName: String,
        studentGrade: Int,
        studentClassroom: Int,
        studentNumber: Int,
    ) {
        studentRepository.save(
            Student(
                name = studentFirstName + studentLastName,
                gradeClassNumber = String.format("%d%2d%2d", studentGrade, studentClassroom, studentNumber),
            )
        )
    }
}