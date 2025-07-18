package com.mE.Health.data.network

import androidx.room.Database
import androidx.room.RoomDatabase

import com.mE.Health.data.dao.AssistDao
import com.mE.Health.data.model.ProviderDTO
import com.mE.Health.data.model.assist.AssistItem

@Database(entities = [AssistItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun assistDao(): AssistDao
}