package com.aksestani.pos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM Student")
    fun findAll(): List<Student>

    @Query("SELECT * FROM student WHERE nim like :key OR name like :key")
    fun searchStudent(key: String): List<Student>

    @Insert
    fun addAll(students : List<Student>)

    @Insert
    fun add(students : Student)

    @Query("UPDATE student SET name=:name, nim=:nim WHERE id LIKE :id")
    open fun editStudent(name : String, nim : String, id : Int)

    @Query("DELETE FROM Student WHERE id LIKE :key")
    open fun delete(key: Int)

    @Query("DELETE FROM Student")
    fun deleteAll()
}