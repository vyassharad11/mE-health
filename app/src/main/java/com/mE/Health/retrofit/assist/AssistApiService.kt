package com.mE.Health.retrofit.assist

import com.mE.Health.data.model.assist.AssistResponse
import com.mE.Health.utility.Constants
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface AssistApiService {
    @POST(Constants.ASSIST_DATA_LIST)
    suspend fun getAssistDataList(@QueryMap params: Map<String, String>): AssistResponse?
}