package com.kmp.newtest

import android.app.Application
import com.kmp.newtest.di.initKoin
import org.koin.android.ext.koin.androidContext

class BookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BookApplication)

        }
    }
}