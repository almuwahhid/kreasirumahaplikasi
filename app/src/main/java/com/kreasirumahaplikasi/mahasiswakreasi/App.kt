package com.kreasirumahaplikasi.mahasiswakreasi

import android.app.Application
import com.aksestani.pos.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    companion object {
        @JvmStatic
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@App)
            modules(
                    listOf(
                            appModule,
                            databaseModule,
                            repoModule,
                            viewModelModule,
                            useCaseModule
                    )
            )
        }
    }
}