package com.aksestani.pos.di

import com.kreasirumahaplikasi.mahasiswakreasi.app.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }
}