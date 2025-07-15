package com.mE.Health.di

import android.content.Context
import androidx.room.Room
import com.mE.Health.data.dao.AssistDao
import com.mE.Health.data.dao.MockDataDao
import com.mE.Health.data.network.AppDatabase
import com.mE.Health.data.network.AppMockDatabase
import com.mE.Health.di.qualifiers.AppDb
import com.mE.Health.di.qualifiers.MockDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    @MockDb
    fun provideMockDatabase(@ApplicationContext context: Context): AppMockDatabase {
        return Room.databaseBuilder(
            context,
            AppMockDatabase::class.java,
            "Mock_Database"
        ).build()
    }

    @Provides
    fun provideMockDataDao(@MockDb db: AppMockDatabase): MockDataDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    @AppDb
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Me_Health"
        ).build()
    }

    @Provides
    fun provideMyItemDao(@AppDb db: AppDatabase): AssistDao {
        return db.assistDao()
    }
}
