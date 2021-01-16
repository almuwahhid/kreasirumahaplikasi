package com.kreasirumahaplikasi.mahasiswakreasi.domain.interactor

import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student
import com.kreasirumahaplikasi.mahasiswakreasi.data.repository.OfflStudentRepository
import com.kreasirumahaplikasi.mahasiswakreasi.utils.Either


class GetStudent(private val offlinerepo : OfflStudentRepository) {
    fun getStudents(callback: (Either<List<Student>?, Throwable>) -> Unit) { offlinerepo.getStudents(callback) }
    fun searchStudents(key : String, callback: (Either<List<Student>?, Throwable>) -> Unit) { offlinerepo.searchStudents(key, callback) }
    fun editStudent(student: Student) = offlinerepo.editStudent(student)
    fun addStudent(student: Student) = offlinerepo.addStudent(student)
    fun deleteStudent(student: Student) = offlinerepo.deleteStudent(student)
}