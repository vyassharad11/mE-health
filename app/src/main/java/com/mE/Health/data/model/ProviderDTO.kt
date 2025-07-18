package com.mE.Health.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ProviderResponse(
    val message: String,
    val data: List<ProviderDTO>,
    val success: Boolean
)

@Entity(tableName = "provider_items")
data class ProviderDTO(
    @PrimaryKey val id: String,
    val practice_name: String,
    val region: String,
    val city: String,
    val state: String,
    val country: String,
    val ehr_vendor: String,
    val website: Boolean,
    val logo_url: String,
    val connection_url: String,
    val full_location: String,
    var isRecent:Boolean = false,
    var isConnected:Boolean = false
)