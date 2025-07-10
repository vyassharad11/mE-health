package com.mE.Health.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mE.Health.data.dao.MockDataDao
import com.mE.Health.data.network.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(appContext: Application): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Me_Health"
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): MockDataDao {
        return db.userDao()
    }
}
