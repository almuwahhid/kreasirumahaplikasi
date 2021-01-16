package com.aksestani.pos.di

import com.kreasirumahaplikasi.mahasiswakreasi.domain.interactor.GetStudent
import org.koin.dsl.module

val useCaseModule = module {
    single {
        GetStudent(get())
    }
}