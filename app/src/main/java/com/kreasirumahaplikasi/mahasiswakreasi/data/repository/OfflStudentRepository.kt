package com.kreasirumahaplikasi.mahasiswakreasi.data.repository

import com.aksestani.pos.db.dao.StudentDao
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student
import com.kreasirumahaplikasi.mahasiswakreasi.domain.repository.IStudentRepository
import com.kreasirumahaplikasi.mahasiswakreasi.utils.Either
import com.kreasirumahaplikasi.mahasiswakreasi.utils.ext.coroutineLaunch
import kotlinx.coroutines.Dispatchers

class OfflStudentRepository(private val studentDao : StudentDao) : IStudentRepository.Offline{

    override fun addStudent(student: Student) {
        coroutineLaunch(Dispatchers.IO) {
            studentDao.add(student)
        }
    }

    override fun deleteAll() {
        coroutineLaunch(Dispatchers.IO) {
            studentDao.deleteAll()
        }
    }

    override fun deleteStudent(student: Student) {
        coroutineLaunch(Dispatchers.IO) {
            studentDao.delete(student.id)
        }
    }

    override fun editStudent(student: Student) {
        coroutineLaunch(Dispatchers.IO) {
            studentDao.editStudent(student.name, student.nim, student.id)
        }
    }

    override fun getStudents(callback: (Either<List<Student>?, Throwable>) -> Unit) {
        coroutineLaunch(Dispatchers.IO) {
            callback(Either.Left(studentDao.findAll()))
        }
    }

    override fun searchStudents(
        key: String,
        callback: (Either<List<Student>?, Throwable>) -> Unit
    ) {
        coroutineLaunch(Dispatchers.IO) {
            callback(Either.Left(studentDao.searchStudent(key)))
        }
    }

}