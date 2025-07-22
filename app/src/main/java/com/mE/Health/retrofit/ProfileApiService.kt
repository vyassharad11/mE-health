package com.mE.Health.retrofit

import com.mE.Health.models.ProfileResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ProfileApiService {
    @POST("user/get-profile/")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Body userId: Map<String, Int>
    ): ProfileResponse
}
