package com.aksestani.pos.di

import com.kreasirumahaplikasi.mahasiswakreasi.data.repository.OfflStudentRepository
import org.koin.dsl.module

val repoModule = module {

    single {
        OfflStudentRepository(get())
    }
}