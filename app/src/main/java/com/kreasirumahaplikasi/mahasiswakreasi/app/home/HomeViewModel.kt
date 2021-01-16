package com.kreasirumahaplikasi.mahasiswakreasi.app.home

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.kreasirumahaplikasi.mahasiswakreasi.base.BaseViewModel
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student
import com.kreasirumahaplikasi.mahasiswakreasi.domain.interactor.GetStudent
import com.kreasirumahaplikasi.mahasiswakreasi.utils.Either
import com.kreasirumahaplikasi.mahasiswakreasi.utils.ext.toSingleEvent
import timber.log.Timber
import java.math.BigInteger

class HomeViewModel(val getStudent: GetStudent) : BaseViewModel() {
    val result = MutableLiveData<List<Student>>().toSingleEvent()

    fun gets(){
        isLoading.postValue(true)
        getStudent.getStudents {
            isLoading.postValue(false)
            when(it){
                is Either.Left -> {
                    result.postValue(it.left)
                }
            }
        }
    }

    fun search(key : String){
        isLoading.postValue(true)
        getStudent.searchStudents(key) {
            isLoading.postValue(false)
            when(it){
                is Either.Left -> {
                    result.postValue(it.left)
                }
            }
        }
    }

    fun add(student: Student){
        getStudent.addStudent(student)
    }
    
    fun edit(student: Student){
        getStudent.editStudent(student)
    }
    
    fun delete(student: Student){
        getStudent.deleteStudent(student)
    }
}