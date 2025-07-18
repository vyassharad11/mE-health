package com.mE.Health.retrofit

import com.mE.Health.data.model.ProviderResponse
import com.mE.Health.models.CountryStateData
import com.mE.Health.models.LoginRequest
import com.mE.Health.models.ProviderData
import com.mE.Health.models.UserDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {

    @POST("user/login/")
    suspend fun userLogin(@Body request: LoginRequest): Response<UserDataResponse>

    @GET("api/health/practices/stats")
    suspend fun stateList(
        @Header("Authorization") authorization: String
    ): Response<CountryStateData>

    @GET("api/health/practices/")
    suspend fun providerStateList(
        @Header("Authorization") authorization: String,
        @Query("search") search: String,
        @Query("state") state: String
    ): Response<ProviderData>

    @GET("api/health/practices/")
    suspend fun providerCountryList(
        @Header("Authorization") authorization: String,
        @Query("country") state: String,
        @Query("search") search: String
    ): Response<ProviderData>


    @GET("api/health/practices/")
    suspend fun providerList(
        @Header("Authorization") authorization: String
    ): Response<ProviderResponse>
}