package com.mE.Health.data.repository

import com.mE.Health.data.dao.AssistDao
import com.mE.Health.data.helper.NetworkBoundResource
import com.mE.Health.data.helper.NetworkStatusProvider
import com.mE.Health.data.helper.Resource
import com.mE.Health.data.model.advice.AdviceInteraction
import com.mE.Health.data.model.apiRequest.ChatRequest
import com.mE.Health.data.model.assist.AssistItem
import com.mE.Health.retrofit.assist.AssistApiService
import com.mE.Health.utility.AppSession
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utils
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AssistRepository @Inject constructor(
    private val appSession: AppSession,
    private val apiService: AssistApiService,
    private val myItemDao: AssistDao,
    private val networkStatusProvider: NetworkStatusProvider
) {
    fun fetchAssistItems(): Flow<Resource<List<AssistItem>>> {
        val body = mapOf("application" to "Health")
        return NetworkBoundResource(
            isNetworkAvailable = networkStatusProvider.isNetworkAvailable(),
            networkCall = { apiService.getAssistDataList(body)?.data },
            saveToDb = { remoteList ->
                val entities = remoteList
                    .filter { it.application == "Health" }
                    .map {
                        AssistItem(
                            id = it.id,
                            application = it.application,
                            category = it.category,
                            item = it.item,
                            default_activation_for_assist = it.default_activation_for_assist,
                            frequency = it.frequency,
                            frequency_in_days = it.frequency_in_days
                        )
                    }
                myItemDao.insertAll(entities)
            },
            queryDb = { myItemDao.getAllItems() }
        )
    }

    fun generateLlmChat(
        title: String,
        assistId: String,
        request: ChatRequest,
        daysFrequency: Int
    ): Flow<Resource<List<AdviceInteraction>>> {
        return flow {
            emit(Resource.Loading())

            if (!networkStatusProvider.isNetworkAvailable()) {
                emit(Resource.Error("No internet connection"))
                return@flow
            }

            try {
                val response = apiService.generateLlmChat(request)

                val currentDate = Utils.getCurrentTimeMillisPlusDays()
                val nextDate = Utils.getCurrentTimeMillisPlusDays(daysFrequency)

                if (!response?.choices.isNullOrEmpty()) {
                    val adviceList = response.choices.map {
                        AdviceInteraction(
                            userId = appSession.getStringPreference(Constants.USER_ID),
                            assistId = assistId,
                            adviceDate = "$currentDate",
                            title = title,
                            nextAdvice = "$nextDate",
                            advice = it.message.content
                        )
                    }
                    myItemDao.insertAdvice(adviceList)
                }

                // Return the list from DB or mock your own
                val assistItems = myItemDao.getAdviceList()
                emit(Resource.Success(assistItems))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "API call failed"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAdviceList() = myItemDao.getAdviceList()

}