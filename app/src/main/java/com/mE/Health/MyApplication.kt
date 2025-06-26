package com.mE.Health

import android.app.Application
import com.mE.Health.di.ApplicationComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}