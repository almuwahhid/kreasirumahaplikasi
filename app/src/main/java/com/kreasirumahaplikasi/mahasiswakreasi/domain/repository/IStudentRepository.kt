package com.kreasirumahaplikasi.mahasiswakreasi.domain.repository

import com.aksestani.pos.db.StudentDb
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student
import com.kreasirumahaplikasi.mahasiswakreasi.utils.Either


interface IStudentRepository {
    interface Offline{
        fun addStudent(student: Student)
        fun deleteAll()
        fun deleteStudent(student: Student)
        fun editStudent(student: Student)
        fun getStudents(callback: (Either<List<Student>?, Throwable>) -> Unit)
        fun searchStudents(key : String, callback: (Either<List<Student>?, Throwable>) -> Unit)
    }
}