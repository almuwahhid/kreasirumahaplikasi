package com.aksestani.pos.db

import aish.android.countries.db.converter.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aksestani.pos.db.dao.StudentDao
import com.kreasirumahaplikasi.mahasiswakreasi.data.entity.Student

@Database(
        entities = [Student::class],
        version = 1, exportSchema = false
)

@TypeConverters(
        Converters::class)
abstract class StudentDb : RoomDatabase() {
    abstract val studentDao : StudentDao
}