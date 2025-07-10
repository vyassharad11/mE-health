package com.mE.Health.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mE.Health.data.model.Result

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromResultList(value: List<Result>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toResultList(value: String?): List<Result>? {
        val listType = object : TypeToken<List<Result>>() {}.type
        return gson.fromJson(value, listType)
    }

}