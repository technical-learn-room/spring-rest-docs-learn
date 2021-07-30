package com.j.docs.student.repository

import com.j.docs.student.exception.StudentNotFoundException
import org.springframework.stereotype.Repository

@Repository
class StudentRepository {
    private val database = hashMapOf<Long, Student>()

    fun save(student: Student) {
        student.createId(
            id = generateStudentId(),
        )
        database.plus(student.id to student)
    }

    private fun generateStudentId() =
        (database.keys.maxOfOrNull { it } ?: 0) + 1

    fun findById(studentId: Long) =
        database[studentId] ?: throw StudentNotFoundException(studentId)

    fun findAll() = database.values
}