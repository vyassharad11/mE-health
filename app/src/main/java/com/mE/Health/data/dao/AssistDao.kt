package com.mE.Health.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mE.Health.data.model.assist.AssistItem

@Dao
interface AssistDao {
    @Query("SELECT * FROM assist")
    suspend fun getAllItems(): List<AssistItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AssistItem>)
}