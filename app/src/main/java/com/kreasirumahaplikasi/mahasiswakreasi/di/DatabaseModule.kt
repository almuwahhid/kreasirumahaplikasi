package com.aksestani.pos.di

import android.app.Application
import androidx.room.Room
import com.aksestani.pos.db.StudentDb
import com.aksestani.pos.db.dao.StudentDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): StudentDb {
       return Room.databaseBuilder(application, StudentDb::class.java, "studentkreasi")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideStudentDao(database: StudentDb): StudentDao {
        return  database.studentDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideStudentDao(get()) }


}
