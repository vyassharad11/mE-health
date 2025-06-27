package com.mE.Health.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.mE.Health.utility.AppSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object PreferencesModule {

    private const val PREF_FILE_KEY = "mE-Health"

    @Singleton
    @Provides
    fun provideAuthenticationSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAppSession(sharedPref: SharedPreferences): AppSession {
        return AppSession(sharedPref)
    }
}
