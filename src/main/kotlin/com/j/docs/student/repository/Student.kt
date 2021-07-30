package com.j.docs.student.repository

class Student(
    val gradeClassNumber: String,
    val name: String,
) {
    var id: Long? = null
        private set

    private var isFrozen = false

    fun createId(id: Long) {
        if (!isFrozen) {
            this.id = id
            this.isFrozen = true
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Student

        if (gradeClassNumber != other.gradeClassNumber) return false
        if (name != other.name) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gradeClassNumber.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Student(id=$id, gradeClassNumber='$gradeClassNumber', name='$name')"
    }
}