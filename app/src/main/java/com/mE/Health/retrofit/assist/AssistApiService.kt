package com.mE.Health.retrofit.assist

import com.mE.Health.data.model.apiRequest.ChatRequest
import com.mE.Health.data.model.apiResponse.ChatResponse
import com.mE.Health.data.model.assist.AssistResponse
import com.mE.Health.utility.ApiConstant
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface AssistApiService {
    @POST(ApiConstant.ASSIST_DATA_LIST)
    suspend fun getAssistDataList(@QueryMap params: Map<String, String>): AssistResponse?

    @Headers("Authorization: Token 40830e6a3ae767cb39c55fe16b6ed3a1dfa2f0dd")
    @POST(ApiConstant.LLM_CHAT)
    suspend fun generateLlmChat(@Body request: ChatRequest): ChatResponse?
}