package com.mE.Health.di

import android.content.Context
import androidx.room.Room
import com.mE.Health.db.FakerDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideFakerDB(@ApplicationContext context : Context) : FakerDB {
        return Room.databaseBuilder(context, FakerDB::class.java, "FakerDB").build()
    }
}